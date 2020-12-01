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
package eu.simuline.octave;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import java.nio.charset.Charset;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

import eu.simuline.octave.exception.OctaveEvalException;
import eu.simuline.octave.exception.OctaveClassCastException;
import eu.simuline.octave.exception.OctaveIOException;
import eu.simuline.octave.exec.OctaveExec;
import eu.simuline.octave.exec.ReadFunctor;
import eu.simuline.octave.exec.ReaderWriteFunctor;
import eu.simuline.octave.exec.WriteFunctor;
import eu.simuline.octave.exec.WriterReadFunctor;
import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.type.OctaveCell;
import eu.simuline.octave.type.OctaveObject;
import eu.simuline.octave.type.OctaveString;
import eu.simuline.octave.type.cast.Cast;

/**
 * The connection to an octave process.
 *
 * This is inspired by the javax.script.ScriptEngine interface.
 */
public final class OctaveEngine {
    
    // TBD: clarify whether these versions are the correct ones. 
    // In the repo for octave, typing `hg tags` I found the following release tags
    // release-5-2-0, release-5-1-0,
    // release-4-4-1, release-4-4-0,
    // release-4-2-2, release-4-2-1, release-4-2-0,
    // release-4-0-3, release-4-0-2, release-4-0-1, release-4-0-0,
    // release-3-8-2, release-3-8-1, release-3-8-0,
    // release-3-6-4, release-3-6-3, release-3-6-2, release-3-6-1, 
    // release-3-6-0 and release-3.6.0(!),
    // release-3-4-3, release-3-4-2, release-3-4-1, release-3-4-0,
    // release-3-2-4,
    // release-3-0-0
    // What about the gaps? 
    // What is prior to 3-0-0?
    // What is the difference between 3-0-0 and 3.0.0?
    // Note that the versions "3.0.5", "3.2.3" in javaoctave 
    // have no counterpart. 


    /**
     * The set of known versions of octave, i.e. those for which javaoctave shall work. 
     */
    private final static Set<String> KNOWN_OCTAVE_VERSIONS = new HashSet<String>
	    (Arrays.asList("3.0.5", "3.2.3", "3.2.4",
		    "3.6.2", "3.8.2",
		    // added by E.R.
		    "4.3.0+",
		    "4.4.0", "5.0.0", "5.2.0"));


    // ER: nowhere used except in method getFactory() 
    // which is in turn nowhere used. 
    /**
    * @deprecated
    */
    private final OctaveEngineFactory factory;

    /**
     * The executor of this octave engine. 
     */
    private final OctaveExec octaveExec;

    private final OctaveIO octaveIO;

    // TBD: it seems that the writer is not needed but the functor. 
    // maybe reuse that and set this field to the functor 
    // wrapping the writer instead of the writer. 
    // This is a way to avoid a null-value. 
    /**
     * The writer to write output from evaluation of a script.  
     * Initially this wraps {@link System#out}. 
     * This may also be <code>null</code> which indicates a 'do nothing functor'
     */
    private Writer writer;

    /**
     * Describe variable <code>random</code> here.
     *
     */
    private final Random random = new Random();


    // TBC: in which version does this occur in listVars? 
    // seemingly not in 5.2.0. 
    /**
     * A variable name not to be listed by {@link OctaveUtils#listVars(OctaveEngine)}. 
     */
    public static final String ANS    = "ans";

    /**
     * Creates an octave engine with the given parameters. 
     * The first one is nowhere used and the others are handed over to 
     * {@link OctaveExec#OctaveExec(int,Writer,Writer,Charset,String[],String[],File)}. 
     * 
     * @param factory
     *    the factory used to create this engine. 
     * @param numThreadsReuse
     *    TBC
     * @param octaveInputLog
     *    a writer to log octave's standard output to, if not <code>null</code>. 
     * @param errorWriter
     *     a writer to log octave's error output to ,if not <code>null</code>. 
     * @param charset
     *    the charset used for communication with the octave process. 
     * @param cmdArray
     *    an array with 0th entry the command 
     *    and the rest (optional) command line parameters. 
     *    
     */
    OctaveEngine(final OctaveEngineFactory factory,
		 final int numThreadsReuse,
		 final Writer octaveInputLog, // may be null 
		 final Writer errorWriter,// may be null 
		 final Charset charset,
		 final String[] cmdArray,
		 final String[] environment, // may be null 
		 final File workingDir) {
        this.factory = factory;
	// assert environment == null;

        this.writer = new OutputStreamWriter(System.out);

        this.octaveExec = new OctaveExec(numThreadsReuse,
					 octaveInputLog, // may be null
					 errorWriter,
					 charset,
					 cmdArray,
					 environment,
					 workingDir);
        this.octaveIO = new OctaveIO(this.octaveExec);
    }

    /**
     * Returns the according read functor: 
     * If {@link #writer} is non-null, 
     * wrap it into a {@link WriterReadFunctor}. 
     * Otherwise, create functor from a reader 
     * which reads empty, i.e. without action, as long as the reader is empty. 
     * 
     */
    @SuppressWarnings({"checkstyle:visibilitymodifier", 
	    "checkstyle:magicnumber"})
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
     * Execute the given script. 
     *
     * @param script
     *            the script to execute
     * @throws OctaveIOException
     *             if the script fails, this will kill the engine
     */
    public void unsafeEval(final Reader script) {
        this.octaveExec.evalRW(new ReaderWriteFunctor(script), 
			       getReadFunctor());
    }

    // ER: see also {@link #eval(final String script)}
    /**
     * Execute the given script. 
     *
     * @param script
     *            the script to execute
     * @throws OctaveIOException
     *             if the script fails, this will kill the engine
     */
    public void unsafeEval(final String script) {
        this.octaveExec.evalRW(new WriteFunctor() {
		@Override
		public void doWrites(final Writer writer2) throws IOException {
		    writer2.write(script);
		}
	    },
	    getReadFunctor());
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
    @SuppressWarnings("checkstyle:magicnumber")
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
     *     the name of the variable to be set to value <code>value</code>. 
     * @param value
     *     the value to set for the variable <code>key</code>
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
     *    a map from variable names to according values 
     *    o be stored in the according variables in octave. 
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
     * @deprecated
     */
    public OctaveEngineFactory getFactory() {
        return this.factory;
    }

    /**
     * Set the writer that the scripts output will be written to.
     *
     * This method is usually placed in ScriptContext.
     * It is used also for tests. 
     *
     * @param writer
     *    the writer to set
     *    This may be null which means that no writer is used. 
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
     *    the errorWriter to set
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
     * @return
     *    the version of the underlying octave program as a string. 
     * @deprecated
     *    use {@link #getOctaveVersion()} instead.     
     */
    public String getVersion() {
	return getOctaveVersion();
    }
    
    // TBD: synchronize with according class in maven-latex-plugin 
    // and extract into separate git repository. 
    static class ManifestInfo {
	private final static String META_FOLDER = "META-INF/";
	   

	private final static String MANIFEST_FILE = "MANIFEST.MF";
	
	// TBD: decide whether this is sensible 
	private final Manifest manifest;
	
	/**
	 * The main attributes of the manifest. 
	 */
	private final Attributes mAtts;
	

	ManifestInfo() throws IllegalStateException {
	    try {
		this.manifest = new Manifest
			(this.getClass().getClassLoader()
				.getResource(META_FOLDER + MANIFEST_FILE)
				.openStream());
	    } catch (IOException e) {
		throw new IllegalStateException("could not read properties" + e);
	    }
	    this.mAtts = this.manifest.getMainAttributes();

	}
	
	private String getAttrValue(Object name) {
	    // is in fact a string always but this is to detect null pointer exceptions 
	    return (String)this.mAtts.get(name);//.toString();
	}

	/**
	 * Returns the version of the implementation. 
	 * This is the version given by the maven coordinates.
	 * 
	 *  @return
	 */
	String getImplVersion() {
	    return getAttrValue(Attributes.Name.IMPLEMENTATION_VERSION);
	}
	
	String getImplVendor() {
	    return getAttrValue(Attributes.Name.IMPLEMENTATION_VENDOR);
	}

    } // class ManifestInfo
    
    private final static ManifestInfo MANIFEST_INFO = new ManifestInfo();

    // TBD: workaround.
    // This does not work only in context of junit tests (classloader!)
    // It does work if run standalone. 
    /**
     * Returns the vendor of this octave bridge as a string. 
     * @return
     *    The vendor of this octave bridge as a string. 
     */
    public String getVendor() {
	System.out.println("MANIFEST_INFO: "+MANIFEST_INFO);
	return MANIFEST_INFO.getImplVendor();
	//return this.getClass().getPackage().getImplementationVendor();
    }

    // TBD: workaround.
    // This does not work only in context of junit tests (classloader!)
    // It does work if run standalone. 
    /**
     * Returns the version of this octave bridge as a string. 
     * @return
     *    The version of this octave bridge as a string. 
     * @see #getOctaveVersion()
     */
    public String getOctaveInJavaVersion() {
	//System.out.println("MANIFEST_INFO: "+MANIFEST_INFO);
	//return MANIFEST_INFO.getImplVersion();
	return this.getClass().getPackage().getImplementationVersion();
    }

    /**
     * Return the version of the octave implementation invoked by this bridge. 
     * E.g. a string like "3.0.5" or "3.2.3".
     *
     * @return 
     *    The version of octave as a string. 
     * @see #getOctaveInJavaVersion()
     */
    public String getOctaveVersion() {
	eval("OCTAVE_VERSION();");
	return get(OctaveString.class, ANS).getString();
    }

    /**
     * Returns whether the version of the current octave installation 
     * given by {@link #getOctaveVersion()}
     * is supported by this octavejava bridge. 
     *
     * @return
     *    whether the version of the current octave installation 
     *    is supported by this octavejava bridge. 
     * @see #KNOWN_OCTAVE_VERSIONS
     */
    public boolean isOctaveVersionAllowed() {
	return KNOWN_OCTAVE_VERSIONS.contains(getOctaveVersion());
    }

    // This is a little strange: pkg('list') returns a cell array 
    // but the entries have uniform type. 
    /**
     * Returns the list of installed packages. 
     * 
     * @return
     *    the list of installed packages. 
     */
    public List<String> getNamesOfPackagesInstalled() {
	eval("cellfun(@(x) x.name, pkg('list'), 'UniformOutput', false);");
	OctaveCell cell = get(OctaveCell.class, ANS);
	// it is known that cell contains strings only. 
	int len = cell.dataSize();
	List<String> res = new ArrayList<String>();
	for (int idx = 0; idx < len; idx++) {
	    res.add(cell.get(OctaveString.class, 1, idx+1).getString());
	}
	return res;
    }

}
