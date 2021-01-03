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
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.simuline.octave.exception.OctaveEvalException;
import eu.simuline.octave.exception.OctaveClassCastException;
import eu.simuline.octave.exception.OctaveIOException;
import eu.simuline.octave.exec.OctaveExec;
import eu.simuline.octave.exec.ReadFunctor;
import eu.simuline.octave.exec.ReaderWriteFunctor;
import eu.simuline.octave.exec.WriteFunctor;
import eu.simuline.octave.exec.WriterReadFunctor;
import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.type.OctaveBoolean;
import eu.simuline.octave.type.OctaveCell;
import eu.simuline.octave.type.OctaveObject;
import eu.simuline.octave.type.OctaveString;
import eu.simuline.octave.type.OctaveStruct;
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
    private static final String ANS    = "ans";

    // TBC: in which version does this occur? 
    // seemingly not in 5.2.0. 
    // Just nargin and that only within functions. 
    // but in that context also nargout would be needed. 
    // Note also that it is nargin not __nargin__. 
    // In particular this variable is not returned by whos. 
    /**
     * A variable name not to be listed by {@link #listVars(OctaveEngine)}. 
     */
    private static final String NARGIN = "__nargin__";

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

    /**
     * Returns the file separator of this os given by the expression <code>filesep()</code>.
     * 
     * @return
     *    the file-separator for this os.
     */
    public String getFilesep() {
	eval("filesep();");
	return get(OctaveString.class, ANS).getString();
    }

    /**
     * Returns value in variable {@link #ANS} 
     * which is expected to be a cell array of strings, 
     * as a collection of strings. 
     * 
     * @return
     *    {@link #ANS} as a collection of strings. 
     */
    private Collection<String> getStringCellFromAns() {
	OctaveCell cell = get(OctaveCell.class, ANS);
	// it is known that cell contains strings only. 
	int len = cell.dataSize();
	Collection<String> collection = new HashSet<String>();
	for (int idx = 1; idx <= len; idx++) {
	    collection.add(cell.get(OctaveString.class, 1, idx).getString());
	}
	return collection;
    }

    // This is a little strange: pkg('list') returns a cell array 
    // but the entries have uniform type. 
    // TBD: we need more than the mere names. 
    // TBC: maybe this shall be reimplemented in terms of getPackagesInstalled()
    /**
     * Returns a collection of names of installed packages. 
     * 
     * @return
     *    a collection of names of installed packages. 
     * @see #getPackagesInstalled()
     */
    public Collection<String> getNamesOfPackagesInstalled() {
	eval("cellfun(@(x) x.name, pkg('list'), 'UniformOutput', false);");
	return getStringCellFromAns();
    }

//    // TBC: dependency only with name okg possible. 
//    public static class Dependency {
//	public final String pkg;
//	public final String operator;// TBC: maybe enum. 
//	public final String version;
//	
//    } // class Dependency

    // TBD: complete 
    // interesting is package struct
    // in particular, depends and autoload
    /**
     * Representation of a package as returned by the octave command
     * <code>pkg('list')</code> which returns a cell array
     * of structs with fields reflected by the fields of this class. 
     * Thus the constructor has a struct as parameter 
     * and is the only place to initialize the fields 
     * which are all public and final.
     * <p>
     * Most of the fields are defined by the DESCRIPTION file in the package 
     * as described in the manual 5.2.0, Section 37.4.1.
     * There are mandatory fields, 
     * optional fields and there may be fields in the struct
     * which are not documented. 
     * Currently, these are not reflected in this class.
     */
    public static class PackageDesc {
	/**
	 * The name of the package in lower case, 
	 * no matter how it is written in the DESCRIPTION FILE. 
	 */
	public final String name;

	/**
	 * A version string which typically consists of numbers separated by dots 
	 * but may also contain +, - and ~. 
	 * Documentation of function <code>compare_versions</code> 
	 * shows that the form is more restricted. 
	 * TBD: bugreport that versions shall be comparable. 
	 */
	public final String version;// TBC: add comparator 

	/**
	 * The date in iso form yyyy-mm-dd.
	 * TBD: add this to manual: make comparable.
	 * Also: seems reasonable, to allow time also. 
	 * TBD: entry in manual
	 */
	public final String date;

	// name and email in form 'Alexander Barth <barth.alexander@gmail.com>'
	/**
	 * The name of the original author, 
	 * convention (TBC) name <email>, 
	 * if more than one, separated by comma. 
	 */
	public final String author;
//	public final String maintainer;// may be list 
//	public final String title;
//	public final String description;
//	public final String categories;// TBD: optional 
//	public final String problems;// TBD: optional 
//	// shall be a list of url's
//	public final Set<URL> url;// sometimes url2, optional
//	// TBC: may also take octave into account 
//	// add also in arithintoctave
//	public final Set<Dependency> depends;
//	public final String license;// maybe comma separated, is optional
//	public final String systemRequirements;
//	public final String buildrequires;// TBC: similar:suggested, package io

	//These are the additional fields nowhere added 
	// they come from according entries in the DESCRPTION file 
	//public final Map<String, String> name2addArg;

	// from here on, these are states of the package 
	// which are not constant accross lifetime of a package 
	// and have thus nothing to do with the DESCRIPTION FILE 
	// TBD: these shall be documented in the manual. 
//	public final String autoload;// TBC: maybe enum 
//	public final String dir;
//	public final String archprefix;

	/**
	 * Whether the package is loaded. 
	 * This has nothing to do with the DESCRIPTION file. 
	 */
	public final boolean isLoaded;


	/**
	 * Creates a new package description from the given struct. 
	 * @param pkg
	 *    a struct representing a package 
	 *    which has a predefined set of mandatory fields, 
	 *    a predefined set of optional fields 
	 *    and which may have also additional fields 
	 *    going beyond what is documented. 
	 */
	PackageDesc(OctaveStruct pkg) {
	    this.name     = pkg.get(OctaveString .class, "name"   ).getString();
	    this.version  = pkg.get(OctaveString .class, "version").getString();
	    this.date     = pkg.get(OctaveString .class, "date"   ).getString();
	    this.author   = pkg.get(OctaveString .class, "author" ).getString();
	    this.isLoaded = pkg.get(OctaveBoolean.class, "loaded" ).get(1, 1);
	}

	@Override public String toString() {
	    StringBuilder res = new StringBuilder();
	    res.append("<package>\n");
	    res.append("name=    "+this.name+"\n");
	    res.append("version= "+this.version+"\n");
	    res.append("date=    "+this.date+"\n");
	    res.append("author=  "+this.author+"\n");
	    res.append("isLoaded="+this.isLoaded+"\n");
	    res.append("</package>\n");
	    return res.toString();
	}
    } // class PackageDesc 

    /**
     * Returns a map mapping the names of the installed packages 
     * to the description of the according package. 
     * 
     * @return
     *    a map from the names to the description of the packages installed. 
     * @see #getNamesOfPackagesInstalled()
     */
    public Map<String, PackageDesc> getPackagesInstalled() {
	eval(ANS + "=pkg('list');");
	// TBC: why ans=necessary??? without like pkg list .. bug? 
	OctaveCell cell = get(OctaveCell.class, ANS);
	int len = cell.dataSize();
	Map<String, PackageDesc> res = new HashMap<String, PackageDesc>();
	PackageDesc pkg;
	for (int idx = 1; idx <= len; idx++) {
	    pkg = new PackageDesc(cell.get(OctaveStruct.class, 1, idx));
	    res.put(pkg.name, pkg);
	}
	return res;
    }

    /**
     * Returns a collection of variables defined 
     * excluding variables like {@link #NARGIN} and {@link#ANS} 
     * but also those that are most likely to be created by this software. TBD: clarification 
     * 
     * @return collection of variables
     */
    public Collection<String> getVarNames() {
	// Justification: 3.0 are the earliest versions. 
	// all later ones don't use '-v' any more 
	String script = getOctaveVersion().startsWith("3.0.") ? "ans=whos -v()" : "ans=whos()";
	eval(script);
	// TBD: clarify: if we use who instead of whos, this can be simplified.  
	eval("{ans.name}");
	Collection<String> collection = getStringCellFromAns();
	collection.removeIf(p -> NARGIN.equals(p));
	collection.removeIf(p -> ANS.equals(p));
	// TBD: eliminate magic literal 
	Pattern pattern = Pattern.compile("javaoctave_[0-9a-f]{12}_eval");
	collection.removeIf(p -> pattern.matcher(p).matches());
	return collection;
    }

    /**
     * Describes the meaning of a name, provided this is tied to a variable 
     * or tied to a file. 
     * The name is replicated in {@link #name} 
     * and the category is given in {@link #category}. 
     * If it is a {@link Category#Variable}, 
     * of course no file is attached and also no type. 
     * TBD: change that. 
     * The type can be found out using <code>typeinfo(name)</code>
     * <p>
     * If it is no variable, 
     * then the case that it is not tied to a file is not taken into account. 
     * <p>
     * If the name points to an existing file, the category is {@link Category#FileEx}, 
     * no matter whether a directory or a proper file. 
     * <p>
     * If the name points to an object with a type 
     * defined by a file (seemingly function types only), 
     * then the category is {@link Category#TypeDefInFile}. 
     */
    public static class NameDesc {

	public enum Category {
	    Variable,
	    FileEx,
	    TypeDefInFile;//,
	    //Unknown;
	} // enum Category

	/**
	 * The name which is described by this {@link NameDesc}.
	 */
	public final String name;

	/**
	 * Whether {@link #name} refers to a variable. 
	 * If this is true, both, {@link #type} and {@link #file} are null.  
	 */
	public final Category category;

	// TBD: clarify whether this is true. 
	// TBD: avoid null
	/**
	 * The type of the object tied to {@link #name}.
	 * Currently, 
	 * none, i.e. <code>null</code> is tied only iff the name represents a variable. 
	 * If the name is tied to a file, this seems to be a function type. 
	 * Seemingly, "function" represents the type "user-defined function". 
	 */
	public final String type;

	// TBD: clarify
	// TBD: avoid null
	/**
	 * The file of the object tied to {@link #name}.
	 * Currently, 
	 * none, i.e. <code>null</code> is tied only iff the name represents a variable. 
	 * For built-in functions this seems to be something rooted in "libinterp" 
	 * but without leading file separator if returned by which. 
	 * This distinction is dropped here. 
	 * Thus for built-in functions, this 'file' does not exist. 
	 */
	public final File file;

	NameDesc(Matcher matcher) {
	    boolean found = matcher.find();
	    assert found;
	    this.name = matcher.group("name");
	    if (matcher.group("var") != null) {
		this.category = Category.Variable;
		this.type = null;
		this.file = null;
		return;
	    }
	    // This is what is implemented at the moment. 
	    // TBD: insert case where no file is attached. 
	    assert matcher.group("tfile") != null ^ matcher.group("sfile") != null;
	    if (matcher.group("sfile") != null) {
		this.category = Category.FileEx;
		this.file = new File(matcher.group("sfile"));
		this.type = null;
		assert this.file.exists();
		return;
	    }
	    this.category = Category.TypeDefInFile;
	    this.type = matcher.group("type");
	    this.file = new File(matcher.group("tfile"));
	}

	@Override
	public String toString() {
	    return "NameDesc [name=" + name + ", category=" + category +
		    ", type=" + type + ", file=" + file + "]";
	}
    } // class NameDesc 

    /**
     * The pattern for the answer to the command <code>which &lt;name&gt;</code>
     * which is implemented by {@link #getDescForName()} TBD: still partially, generalize
     * if the name <code>which &lt;name&gt;</code> is no variable 
     * but is tied to a type (maybe then always a function type) and a file. 
     */
    private final static Pattern PATTERN_NAME_TYPE_FILE =
	    Pattern.compile("'(?<name>.+)' is " +
                            "(a ((?<var>variable)|(?<type>.+) from the file (?<tfile>.+))" +
		            "|the (file|directory) (?<sfile>.+))");

    // TBD: fail gracefully, if nameTypeFileNoVar is sth not expected. 
    // TBD: clarify, what it could be if not a variable and no file is tied to it. 
    // Consult also which.m 
    // IN this case still to be distinguished if a type is attached. 
    //
    /**
     * Returns the description tied to the name <code>nameVarOrTypeFile</code>
     * provided it is either a variable or else is a file attached.
     * CAUTION: The cases where it is not a variable name and no file attached,
     * is not yet implemented.
     * If it is no variable, then it is either a sole existing file/directory attached
     * without type or it is a or it is a type attached and in addition a file.
     * TBD: clarify: seemingly, in the latter case it is a function type. 
     * This method is based on octaves command <code>which</code>.
     * 
     * @param nameVarOrTypeFile
     *    the name which is either a variable or it is tied to an existing file, 
     *    or to both a type and to a file. 
     *    TBD: clarify whether in the latter case, the type is then a function type. 
     * @return
     *    The description for the given name <code>nameTypeFileNoVar</code>,
     *    provided satisfies the above conditions.
     *    Iff it is a variable, {@link NameDesc#isVar} is set.
     *    Else, for the cases implemented, both,
     *    at least {@link NameDesc#file} is set.
     *    If {@link NameDesc#type} is not set, the file exists,
     *    and if the type exists, then the file is attached to the object of that type.
     */
    public NameDesc getDescForName(String nameVarOrTypeFile) {
	StringReader checkCmd = new StringReader(String.format("which %s", nameVarOrTypeFile));
        final StringWriter fileResultWr = new StringWriter();
        this.octaveExec.evalRW(new ReaderWriteFunctor(checkCmd),
        	new WriterReadFunctor(fileResultWr));
        return new NameDesc(PATTERN_NAME_TYPE_FILE.matcher(fileResultWr.toString()));
    }

    /**
     * A command in the package 'java'. 
     * This is used to determine both the installation home directory and the java home directory, 
     * which is the location of the m-file for {@link #JAVA_FUN}.
     */
    private final static String JAVA_FUN = "javaaddpath";

    /**
     * A pattern of the m-file for command {@link #JAVA_FUN} in package java, 
     * defining the installation home directory as returned by {@link #getInstHomeDir()} 
     * as its group with number two and the java home directory as its group number one. 
     * The pattern is made independent of the octave version and of the specific command. 
     */
    private final static String PATTERN_HOMEDIR = String
	    .format("(?<javaHome>(?<octHome>/.+)/share/octave/(?<octVrs>[^/]+)/m/java/)%s.m", JAVA_FUN)
	    .replace("/", File.separator);

    private File getHomeDir(String nameGrp) {
	Matcher matcher = Pattern.compile(PATTERN_HOMEDIR)
		.matcher(getDescForName(JAVA_FUN).file.toString());
	boolean found = matcher.find();
	assert found;
	assert matcher.group("octVrs").equals(getOctaveVersion());
	return new File(matcher.group(nameGrp));
    }

    // TBD: suggestion: replace OCTAVE_HOME by octaveHome() 
    /**
     * Returns the installation home directory, 
     * in the manual sometimes called octave-home. 
     * CAUTION: Initially, this is OCTAVE_HOME, but the latter can be overwritten. 
     * 
     * @return
     *    octave's installation home directory. 
     */
    public File getInstHomeDir() {
 	return this.getHomeDir("octHome");
     }

    // TBD: suggestion: replace OCTAVE_JAVA_DIR by octaveJavaDir() 
    /**
     * Returns the java home directory, which contains the m files of the java interface
     * like {@link #JAVA_FUN} but is also a search directory for files 
     * <code>javaclasspath.txt</code> (deprecated <code>classpath.txt</code>) 
     * but also the configuration file <code>java.opt</code>. 
     * CAUTION: Initially, this is OCTAVE_JAVA_DIR, but the latter can be overwritten. 
     * 
     * @return
     *    octave's java home directory. 
     */
    public File getJavaHomeDir() {
 	return this.getHomeDir("javaHome");
     }

}
