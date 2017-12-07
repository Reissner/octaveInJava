/*
 * Copyright 2008, 2010 Ange Optimization ApS
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
/**
 * @author Kim Hansen
 */
package dk.ange.octave;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import dk.ange.octave.exception.OctaveEvalException;
import dk.ange.octave.exec.OctaveExec;
import dk.ange.octave.exec.ReadFunctor;
import dk.ange.octave.exec.ReaderWriteFunctor;
import dk.ange.octave.exec.WriteFunctor;
import dk.ange.octave.exec.WriterReadFunctor;
import dk.ange.octave.io.OctaveIO;
import dk.ange.octave.type.OctaveObject;
import dk.ange.octave.type.OctaveString;
import dk.ange.octave.type.cast.Cast;

/**
 * The connection to an octave process.
 *
 * This is inspired by the javax.script.ScriptEngine interface.
 */
public final class OctaveEngine {

    // ER: nowhere used except in method getFactory() 
    // which is in turn nowhere used. 
    private final OctaveEngineFactory factory;

    /**
     * The executor of this octave engine. 
     */
    private final OctaveExec octaveExec;

    private final OctaveIO octaveIO;

    // UTF-8 is a standart charset and is thus supported
    /**
     * The writer to write to stdout with generally supported charset 
     * <code>Charset.forName("UTF-8")}</code>. 
     */
    private Writer writer = new OutputStreamWriter(System.out,
						   Charset.forName("UTF-8"));

    /**
     * Describe variable <code>random</code> here.
     *
     */
    private final Random random = new Random();

    /**
     * Creates an octave egine with the given parameters. 
     */
    OctaveEngine(final OctaveEngineFactory factory,
		 final Writer octaveInputLog,
		 final Writer errorWriter,
		 final File octaveProgram,
		 final String[] argsArray,
		 final File workingDir) {
        this.factory = factory;
        this.octaveExec = new OctaveExec(octaveInputLog,
					 errorWriter,
					 octaveProgram,
					 argsArray,
					 null, // environment
					 workingDir);
        this.octaveIO = new OctaveIO(this.octaveExec);
    }

    // ER: see also {@link #eval(final String script)}
    /**
     * @param script
     *            the script to execute
     * @throws OctaveIOException
     *             if the script fails, this will kill the engine
     */
    public void unsafeEval(final String script) {
        this.octaveExec.eval(new WriteFunctor() {
		@Override
		public void doWrites(final Writer writer2) throws IOException {
		    writer2.write(script);
		}
	    },
	    getReadFunctor());
    }

    /**
     * Returns the according read functor: 
     * If {@link #writer} is non-zero, 
     * wrap it into a {@link WriterReadFunctor}. 
     * Otherwise, create functor from a reader 
     * which reads empty, i.e. without action, as long as the reader is empt. 
     * 
     */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    private ReadFunctor getReadFunctor() {
        if (this.writer == null) {
            // If writer is null create a "do nothing" functor
            return new ReadFunctor() {
                private final char[] buffer = new char[4096];

		@Override
		@SuppressWarnings("checkstyle:emptyblock")
		public void doReads(final Reader reader) throws IOException {
                    while (reader.read(buffer) != -1) { // NOPMD
                        // Do nothing
                    }
                }
            };
        } else {
            return new WriterReadFunctor(this.writer);
        }
    }

    /**
     * @param script
     *            the script to execute
     * @throws OctaveIOException
     *             if the script fails, this will kill the engine
     */
    public void unsafeEval(final Reader script) {
        this.octaveExec.eval(new ReaderWriteFunctor(script), getReadFunctor());
    }

    // ER: 
    // based on {@link #unsaveEval(final String script)} 
    // in contrast to {@link #unsaveEval(final String script)} 
    // errors are caught. 
    // Implementation is based on octave Built-in Function eval (try, catch)
    // both try and catch being strings. 
    // try is always evaluated and catch is evaluated in case of an error 
    // while evaluating try. 
    // The last error ist returned by built/in function lasterr(). 
    // 
    // evaluates 'eval(javaoctave_X_eval,"javaoctave_X_lasterr = lasterr();");'
    // where javaoctave_X_eval is a variable containing script as a string 
    // and X is some random number 
    //
    // That way, in case of an error, 
    // javaoctave_X_lasterr contains the string representtion of this error. 
    /**
     * A safe eval that will not break the engine on syntax errors 
     * or other errors. 
     *
     * @param script
     *            the script to execute
     * @throws OctaveEvalException
     *             if the script fails
     */
    public void eval(final String script) {
        final String tag = String.format("%06x%06x",
					 this.random.nextInt(1 << 23),
					 this.random.nextInt(1 << 23));
        put(String.format("javaoctave_%1$s_eval", tag),
	    new OctaveString(script));
        // Does not use lasterror() as that returns data in a matrix struct,
	// we can not read that yet
        unsafeEval(String.format("eval(javaoctave_%1$s_eval, " +
				 "\"javaoctave_%1$s_lasterr = lasterr();\");",
				 tag));
        final OctaveString lastError =
	    get(OctaveString.class,
		String.format("javaoctave_%1$s_lasterr", tag));
        unsafeEval(String.format("clear javaoctave_%1$s_eval  " +
				 "javaoctave_%1$s_lasterr", tag));
        if (lastError != null) {
            throw new OctaveEvalException(lastError.getString());
        }
    }

    /**
     * Sets a value in octave.
     *
     * @param key
     *            the name of the variable
     * @param value
     *            the value to set
     */
    public void put(final String key, final OctaveObject value) {
        this.octaveIO.set(Collections.singletonMap(key, value));
    }

    /**
     * Sets all the mappings in the specified map as variables in octave. 
     * These mappings replace any variable 
     * that octave had for any of the keys currently in the specified map. 
     *
     * @param vars
     *            the variables to be stored in octave
     */
    public void putAll(final Map<String, OctaveObject> vars) {
        this.octaveIO.set(vars);
    }

    /**
     * @param key
     *            the name of the variable
     * @return the value from octave or null if the variable does not exist
     */
    public OctaveObject get(final String key) {
        return this.octaveIO.get(key);
    }

    /**
     * @param castClass
     *            Class to cast to
     * @param key
     *            the name of the variable
     * @param <T>
     *            the class of the return value
     * @return shallow copy of value for this key, or null if key isn't there.
     * @throws OctaveClassCastException
     *             if the object can not be cast to a castClass
     */
    public <T extends OctaveObject> T get(final Class<T> castClass,
					  final String key) {
        return Cast.cast(castClass, get(key));
    }

    // ER: nowhere used
    /**
     * @return the factory that created this object
     */
    public OctaveEngineFactory getFactory() {
        return this.factory;
    }

    /**
     * Set the writer that the scripts output will be written to.
     *
     * This method is usually placed in ScriptContext.
     *
     * @param writer
     *            the writer to set
     */
    public void setWriter(final Writer writer) {
        this.writer = writer;
    }

    /**
     * Set the writer that the scripts error output will be written to.
     *
     * This method is usually placed in ScriptContext.
     *
     * @param errorWriter
     *            the errorWriter to set
     */
    public void setErrorWriter(final Writer errorWriter) {
        this.octaveExec.setErrorWriter(errorWriter);
    }

    /**
     * Close the octave process in an orderly fashion.
     */
    public void close() {
        this.octaveExec.close();
    }

    /**
     * Kill the octave process without remorse.
     */
    public void destroy() {
        this.octaveExec.destroy();
    }

    /**
     * Return the version of the octave implementation. 
     * E.g. a string like "3.0.5" or "3.2.3".
     *
     * @return Version of octave
     */
    public String getVersion() {
        final StringWriter version = new StringWriter();
	StringReader reader =
	    new StringReader("printf(\"%s\", OCTAVE_VERSION());");
        this.octaveExec.eval(new ReaderWriteFunctor(reader),
			     new WriterReadFunctor(version));
        return version.toString();
    }
}
