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
package eu.simuline.octave.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

import eu.simuline.octave.exception.OctaveClassCastException;
import eu.simuline.octave.exception.OctaveIOException;
import eu.simuline.octave.exception.OctaveParseException;
import eu.simuline.octave.exec.OctaveExec;
import eu.simuline.octave.exec.ReaderWriteFunctor;
import eu.simuline.octave.exec.WriteFunctor;
import eu.simuline.octave.exec.WriterReadFunctor;
import eu.simuline.octave.io.spi.OctaveDataReader;
import eu.simuline.octave.io.spi.OctaveDataWriter;
import eu.simuline.octave.type.OctaveObject;

// ER: Has only static methods or methods based on {@link #octaveExec} 
/**
 * The object controlling IO of Octave data of {@link #octaveExec}. 
 * The basic operations are to 
 * <ul>
 * <li>
 * set a map of variable names to their values 
 * via {@link #set(Map)} (no setting of a single value), 
 * <li>
 * get the value for a variable name via {@link #get(String)}, 
 * <li>
 * check whether a variable with a given name exists 
 * via {@link #existsVar(String)}. 
 * </ul>
 * The rest are static utility methods. 
 * Part is for reading objects from a reader: 
 * <ul>
 * <li>
 * {@link #readerReadLine(BufferedReader)} reads a line 
 * <li>
 * {@link #read(BufferedReader)} reads an object 
 * <li>
 * {@link #readWithName(BufferedReader)} yields a singleton map name-->object, 
 * where name is the name of a variable 
 * <li>
 * {@link #readWithName(String)} yields a singleton map name-->object, 
 * as above but reading from a string. 
 * </ul>
 * Part is for writing: 
 * <ul>
 * <li>
 * {@link #write(Writer, OctaveObject)}
write(Writer, String, OctaveObject)

toText(String, OctaveObject)
toText(OctaveObject)
 * </ul>
 */
public final class OctaveIO {

    private static final String GLOBAL = "global ";
    private static final String TYPE   = "# type: ";
    private static final String NAME   = "# name: ";
 
    private final OctaveExec octaveExec;

    /**
     * @param octaveExec
     */
    public OctaveIO(final OctaveExec octaveExec) {
        this.octaveExec = octaveExec;
    }

    /**
     * Sets the map <code>values</code> 
     * mapping variable names to according values. 
     *
     * @param values
     */
    public void set(final Map<String, OctaveObject> values) {
        final StringWriter outputWriter = new StringWriter();
	this.octaveExec.evalRW(new DataWriteFunctor(values),
			       new WriterReadFunctor(outputWriter));
	
        final String output = outputWriter.toString();
        if (output.length() != 0) {
            throw new IllegalStateException
		("Unexpected output: '" + output + "'");
        }
    }

    /**
     * Gets the value of the variable <code>name</code> 
     * or null if this variable does not exist 
     * according to {@link #existsVar(String)}. 
     *
     * @param name
     *    the name of a variable 
     * @return 
     *    the value of the variable <code>name</code> from octave 
     *    or <code>null</code> if the variable does not exist. 
     * @throws OctaveClassCastException
     *    if the value can not be cast to T
     */
    public OctaveObject get(final String name) {
        if (!existsVar(name)) {
            return null;
        }
        final WriteFunctor writeFunctor = 
	    new ReaderWriteFunctor(new StringReader("save -text - " + name));
        final DataReadFunctor readFunctor = new DataReadFunctor(name);
        this.octaveExec.evalRW(writeFunctor, readFunctor);
        return readFunctor.getData();
    }

    /**
     * Returns whether the variable <code>name</code> exists. 
     *
     * @param name
     *    the name of a variable 
     * @return 
     *    whether the variable <code>name</code> exists. 
     */
    private boolean existsVar(final String name) {
	StringReader checkCmd = new StringReader
	    ("printf('%d', exist('" + name + "','var'));");
        final StringWriter existResult = new StringWriter();
        this.octaveExec.evalRW(new ReaderWriteFunctor(checkCmd),
			       new WriterReadFunctor(existResult));
        final String s = existResult.toString();
	// switch (s) {
	// case "1":
	//     return true;
	// case "0": 
	//     return false;
	// default:
	//     throw new OctaveParseException("Unexpected output '" + s + "'");
	// }

        if ("1".equals(s)) {
            return true;
        } else if ("0".equals(s)) {
            return false;
        } else {
            throw new OctaveParseException("Unexpected output '" + s + "'");
	}
    }

    /**
     * Reads a line from <code>reader</code> into a string if possible. 
     * Returns null at the end of the stream and throws an exception 
     * in case of io problems. 
     *
     * @param reader
     *    the reader to read a line from. 
     * @return 
     *    next line from <code>reader</code>, <code>null</code> at end of stream
     * @throws OctaveIOException
     *    in case of IOException reading from <code>reader</code>. 
     */
    public static String readerReadLine(final BufferedReader reader) {
        try {
            return reader.readLine();
        } catch(IOException e) {
            throw new OctaveIOException(e);
        }
    }

    /**
     * Read a single object from Reader. 
     * The first line read determines the type of object 
     * and the rest of reading is delegated to the OctaveDataReader 
     * associated with that type given by 
     * {@link OctaveDataReader#getOctaveDataReader(String)}. 
     *
     * @param reader
     *    a reader starting with first line 
     *    <code>{@link #TYPE}[global ]type</code>, 
     *    i.e. <code>global </code> is optional 
     *    and type is the type of the object to be read. 
     * @return 
     *    OctaveObject read from Reader
     * @throws OctaveParseException **** appropriate type? 
     *    if the type read before is not registered 
     *    and so there is no appropriate reader. 
     */
    public static OctaveObject read(final BufferedReader reader) {
	// may throw OctaveIOException 
        final String line = OctaveIO.readerReadLine(reader);
	// line == null at end of stream 
        if (line == null || !line.startsWith(TYPE)) {
            throw new OctaveParseException
		("Expected '" + TYPE + "' got '" + line + "'");
        }
        String typeGlobal = line.substring(TYPE.length());
        // Ignore "global " prefix to type (it is not really a type)
	String type = typeGlobal.startsWith(GLOBAL)
	    ? typeGlobal.substring(GLOBAL.length())
	    : typeGlobal;
        final OctaveDataReader dataReader = 
	    OctaveDataReader.getOctaveDataReader(type);
        if (dataReader == null) {
            throw new OctaveParseException
		("Unknown octave type, type='" + type + "'");
        }
        return dataReader.read(reader);
    }

    /**
     * Read a single variable - object pair from Reader. 
     * The variable is given by its name. 
     *
     * @param reader
     *    a reader starting with first line <code>{@link #NAME}name</code>, 
     *    where name is the name of the variable. 
     *    the following lines represent the object stored in that variable. 
     * @return 
     *   a singleton map with the name of a variable and object stored therein. 
     */
    // used in DataReadFunctor.doReads(Reader) only and tests. 
    public static 
	Map<String, OctaveObject> readWithName(final BufferedReader reader) {

	// read name from the first line 
	// may throw OctaveIOException 
        final String line = OctaveIO.readerReadLine(reader);
	if (!line.startsWith(NAME)) {
            throw new OctaveParseException
		("Expected '" + NAME + "', but got '" + line + "'");
        }
        final String name = line.substring(NAME.length());
	// read value and put into singleton map 
        return Collections.singletonMap(name, read(reader));
    }

    /**
     * Read a single object from String, 
     * it is an error if there is data left after the object. 
     *
     * @param input
     *    
     * @return 
     *    a singleton map with the name and object
     * @throws OctaveParseException
     *    if there is data left after the object is read
     */
    public static Map<String, OctaveObject> readWithName(final String input) {
        final BufferedReader bufferedReader = 
	    new BufferedReader(new StringReader(input));
	// may throw OctaveIOException 
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

    // newly introduced, not yet used. 
    /**
     * Writes a line given by <code>strWithNl</code> to <code>writer</code> 
     * if possible. 
     *
     * @param writer
     * @param strWithNl
     * @throws OctaveIOException
     *    in case of IOException writing to <code>writer</code>. 
     */
    public static void writerWriteLine(Writer writer, String strWithNl) {
	try {
	    writer.write(strWithNl);
	} catch(IOException e) {
            throw new OctaveIOException(e);
        }
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
     *    the writer to write the object <code>octValue</code> onto. 
     * @param octValue
     *    the object to write to <code>writer</code>. 
     * @throws OctaveParseException **** appropriate type? 
     *    if the type of <code>octValue</code> is not registered 
     *    and so there is no appropriate writer. 
     * @throws IOException
     *    if the process of writing fails. 
     */
    public static <T extends OctaveObject> void write(final Writer writer,
						      final T octValue) 
	throws IOException {
        final OctaveDataWriter<T> dataWriter = 
	    OctaveDataWriter.getOctaveDataWriter(octValue);
        if (dataWriter == null) {
            throw new OctaveParseException
		("No writer for java type " + octValue.getClass() + ". ");
        }
	// may throw IOException 
        dataWriter.write(writer, octValue);
    }

    /**
     * ER: 
     * Writes the name <code>name</code> 
     * and the {@link OctaveObject} <code>octValue</code> 
     * to the writer <code>writer</code> 
     * using {@link #write(Writer, OctaveObject)}. 

     * @param writer
     *    the writer to write the object <code>octaveType</code> onto. 
     * @param name
     *    the name, **** of a variable 
     * @param octValue
     *    the object to write to <code>writer</code>. 
     * @throws OctaveParseException **** appropriate type? 
     *    if the type of <code>octaveType</code> is not registered 
     *    and so there is no appropriate writer. 
     * @throws IOException
     *    if the process of writing fails. 
     */
    public static void write(final Writer writer,
			     final String name,
			     final OctaveObject octValue) throws IOException {
	// may throw IOException 
        writer.write("# name: " + name + "\n"); // just a comment??? **** 
	// may throw IOException, OctaveParseException
        write(writer, octValue);
    }

    /**
     * Returns  as a string how the variable <code>name</code> 
     * and the {@link OctaveObject} <code>octaveType</code> (****bad name) 
     * are written. 
     *
     * @param name
     *    the name, **** of a variable 
     * @param octValue
     *    the object to write to <code>writer</code>. 
     * @return 
     *    The result from saving the value octaveType 
     *    in octave -text format
     */
    // seems to be used only locally and in tests 
    public static String toText(final String name,
				final OctaveObject octValue) {
        try {
            final Writer writer = new StringWriter();
            write(writer, name, octValue);
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
     * @param octValue
     * @return toText("ans", octValue)
     */
    public static String toText(final OctaveObject octValue) {
        return toText("ans", octValue);
    }

}
