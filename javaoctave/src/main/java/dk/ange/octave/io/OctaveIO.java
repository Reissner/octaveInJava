/*
 * Copyright 2007, 2008, 2009, 2012 Ange Optimization ApS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.ange.octave.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

import dk.ange.octave.exception.OctaveClassCastException;
import dk.ange.octave.exception.OctaveIOException;
import dk.ange.octave.exception.OctaveParseException;
import dk.ange.octave.exec.OctaveExec;
import dk.ange.octave.exec.ReaderWriteFunctor;
import dk.ange.octave.exec.WriteFunctor;
import dk.ange.octave.exec.WriterReadFunctor;
import dk.ange.octave.io.spi.OctaveDataReader;
import dk.ange.octave.io.spi.OctaveDataWriter;
import dk.ange.octave.type.OctaveObject;

// ER: Has only static methods or methods based on {@link #octaveExec} 
/**
 * The object controlling IO of Octave data. 
 */
public final class OctaveIO {

    private static final String TYPE = "# type: ";
    private static final String GLOBAL = "global ";
    private static final String TOKEN = "# name: ";
 
    private final OctaveExec octaveExec;

    /**
     * @param octaveExec
     */
    public OctaveIO(final OctaveExec octaveExec) {
        this.octaveExec = octaveExec;
    }

    /**
     * @param values
     */
    public void set(final Map<String, OctaveObject> values) {
        final StringWriter outputWriter = new StringWriter();
	this.octaveExec.eval(new DataWriteFunctor(values),
			     new WriterReadFunctor(outputWriter));
        final String output = outputWriter.toString();
        if (output.length() != 0) {
            throw new IllegalStateException
		("Unexpected output: '" + output + "'");
        }
    }

    /**
     * @param name
     * @return the value of the variable from octave 
     *    or null if the variable does not exist
     * @throws OctaveClassCastException
     *    if the value can not be cast to T
     */
    public OctaveObject get(final String name) {
        if (!checkIfVarExists(name)) {
            return null;
        }
        final WriteFunctor writeFunctor = 
	    new ReaderWriteFunctor(new StringReader("save -text - " + name));
        final DataReadFunctor readFunctor = new DataReadFunctor(name);
        this.octaveExec.eval(writeFunctor, readFunctor);
        return readFunctor.getData();
    }

    private boolean checkIfVarExists(final String name) {
        final StringWriter existResult = new StringWriter();
        this.octaveExec.eval(new ReaderWriteFunctor
			     (new StringReader("printf(\"%d\", exist(\"" + 
					       name + "\",\"var\"));")),
			     new WriterReadFunctor(existResult));
        final String s = existResult.toString();
        if ("1".equals(s)) {
            return true;
        } else if ("0".equals(s)) {
            return false;
        } else {
            throw new OctaveParseException("Unexpected output '" + s + "'");
        }
    }

    /**
     * Utility function.
     *
     * @param reader
     * @return next line from reader, null at end of stream
     */
    public static String readerReadLine(final BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (final IOException e) {
            throw new OctaveIOException(e);
        }
    }

    /**
     * Read a single object from Reader. 
     *
     * @param reader
     * @return OctaveObject read from Reader
     */
    public static OctaveObject read(final BufferedReader reader) {
        final String line = OctaveIO.readerReadLine(reader);
        if (line == null || !line.startsWith(TYPE)) {
            throw new OctaveParseException
		("Expected '" + TYPE + "' got '" + line + "'");
        }
        String typeGlobal = line.substring(TYPE.length());
        // Ignore "global " prefix to type (it is not really a type)
	String type = (typeGlobal != null && typeGlobal.startsWith(GLOBAL))
	    ? typeGlobal.substring(GLOBAL.length())
	    : typeGlobal;
        // if (typeGlobal != null && typeGlobal.startsWith(GLOBAL)) {
        //     type = typeGlobal.substring(GLOBAL.length());
        // } else {
        //     type = typeGlobal;
        // }
        final OctaveDataReader dataReader = 
	    OctaveDataReader.getOctaveDataReader(type);
        if (dataReader == null) {
            throw new OctaveParseException
		("Unknown octave type, type='" + type + "'");
        }
        return dataReader.read(reader);
    }

    /**
     * Read a single object from Reader. 
     *
     * @param reader
     * @return a singleton map with the name and object
     */
    public static 
	Map<String, OctaveObject> readWithName(final BufferedReader reader) {
        final String line = OctaveIO.readerReadLine(reader);
       if (!line.startsWith(TOKEN)) {
            throw new OctaveParseException
		("Expected '" + TOKEN + "', but got '" + line + "'");
        }
        final String name = line.substring(TOKEN.length());
        return Collections.singletonMap(name, read(reader));
    }

    /**
     * Read a single object from String, 
     * it is an error if there is data left after the object. 
     *
     * @param input
     * @return a singleton map with the name and object
     * @throws OctaveParseException
     *             if there is data left after the object is read
     */
    public static Map<String, OctaveObject> readWithName(final String input) {
        final BufferedReader bufferedReader = 
	    new BufferedReader(new StringReader(input));
        final Map<String, OctaveObject> map = readWithName(bufferedReader);
        try {
            final String line = bufferedReader.readLine();
            if (line != null) {
                throw new OctaveParseException
		    ("Too much data in input, first extra line is '" + 
		     line + "'");
            }
        } catch (IOException e) {
            throw new OctaveIOException(e);
        }
        return map;
    }

    /**
     * ER: 
     * Writes the {@link OctaveObject} <code>octaveType</code> (****bad name) 
     * to the writer <code>writer</code>. 
     * To that end, fetch an {@link OctaveDataWriter} 
     * of the appropriate type given by <code>octaveType</code> 
     * and use this writer to write <code>octaveType</code> 
     * onto <code>writer</code>. 

     * @param <T>
     *    the type of {@link OctaveObject} to be written. 
     * @param writer
     *    the writer to write the object <code>octaveType</code> onto. 
     * @param octaveType
     *    the object to write to <code>writer</code>. 
     * @throws OctaveParseException **** appropriate type? 
     *    if the type of <code>octaveType</code> is not registered 
     *    and so there is no appropriate writer. 
     * @throws IOException
     *    if the process of writing fails. 
     */
    public static <T extends OctaveObject> void write(final Writer writer,
						      final T octaveType) 
	throws IOException {
        final OctaveDataWriter<T> dataWriter = 
	    OctaveDataWriter.getOctaveDataWriter(octaveType);
        if (dataWriter == null) {
            throw new OctaveParseException
		("Unknown type, " + octaveType.getClass());
        }
	// may throw IOException 
        dataWriter.write(writer, octaveType);
    }

    /**
     * ER: 
     * Writes the name <code>name</code> 
     * and the {@link OctaveObject} <code>octaveType</code> (****bad name) 
     * to the writer <code>writer</code> 
     * using {@link #write(Writer, OctaveObject)}. 

     * @param writer
     *    the writer to write the object <code>octaveType</code> onto. 
     * @param name
     *    the name, **** of a variable 
     * @param octaveType
     *    the object to write to <code>writer</code>. 
     * @throws OctaveParseException **** appropriate type? 
     *    if the type of <code>octaveType</code> is not registered 
     *    and so there is no appropriate writer. 
     * @throws IOException
     *    if the process of writing fails. 
     */
    public static void write(final Writer writer,
			     final String name,
			     final OctaveObject octaveType) throws IOException {
	// may throw IOException 
        writer.write("# name: " + name + "\n"); // just a comment??? **** 
	// may throw IOException, OctaveParseException
        write(writer, octaveType);
    }

    /**
     * Returns  as a string how the variable <code>name</code> 
     * and the {@link OctaveObject} <code>octaveType</code> (****bad name) 
     * are written. 
     *
     * @param octaveType
     *    the object to write to <code>writer</code>. 
     * @param name
     *    the name, **** of a variable 
     * @return 
     *    The result from saving the value octaveType 
     *    in octave -text format
     */
    public static String toText(final OctaveObject octaveType,
				final String name) {
        try {
            final Writer writer = new StringWriter();
            write(writer, name, octaveType);
            return writer.toString();
        } catch (final IOException e) {
            throw new OctaveIOException(e);
        }
    }

    /**
     * Returns as a string how the {@link OctaveObject} <code>octaveType</code> 
     * (****bad name) is written without variable, 
     * i.e. with variable <code>"ans"</code>. 
     *
     * @param octaveType
     * @return toText(octaveType, "ans")
     */
    public static String toText(final OctaveObject octaveType) {
        return toText(octaveType, "ans");
    }

}
