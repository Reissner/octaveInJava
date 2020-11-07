/*
 * Copyright 2008 Ange Optimization ApS
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
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.Arrays;
import java.util.Objects;

/**
 * Factory that creates OctaveEngines. 
 * First of all, create an OctaveEngineFactory 
 * using the default constructor {@link #OctaveEngineFactory()} 
 * then, optionally, change parameters 
 * and finally create an Octave Engine using {@link #getScriptEngine()} 
 * with the current parameters. 
 * To set a parameter, use the various setter methods. 
 * In the documentation of each setter method, 
 * also the default value is documented 
 * which is used to create an {@link OctaveEngine} 
 * if the setter method is not invoked. 
 * Note that setter methods return the factory after modification 
 * and so setter methods may be queued also. 
 * 
 */
public final class OctaveEngineFactory {

    /**
     * The custom system property 
     * which determines where the executable is found if {@link #octaveProgramFile} is not set. 
     * A custom property is set when invoking the runtime with the option <code>-D</code> 
     * For example <code>java -Deu.simuline.octave.executable=myoctave Application</code> 
     * runs class <code>Application</code> 
     * setting the system property <code>eu.simuline.octave.executable</code> 
     * to 
     */
    public static final String PROPERTY_EXECUTABLE = 
	"eu.simuline.octave.executable";

    /**
     * If this is not <code>null</code>, the octave engine created 
     * writes the output to that log writer also. 
     * By default, this is <code>null</code>. 
     * The according setter method is {@link #setOctaveInputLog(Writer)}. 
     */
    private Writer octaveInputLog = null;

    /**
     * The error writer for the octave process. 
     * By default, this is just {@link System#err}. <!-- TBD: clarify encoding -->
     * The according setter method is {@link #setErrorWriter(Writer)}. 
     */
    private Writer errWriter = new OutputStreamWriter(System.err, 
						      OctaveUtils.getUTF8());

    /**
     * The file containing the octave program or is <code>null</code>. 
     * In the latter case, the name of the octave program command 
     * is determined as described for {@link #octaveProgramCmd}. 
     * By default, this is <code>null</code>. 
     * The according setter method is {@link #setOctaveProgramFile(File)}. 
     */
    private File octaveProgramFile = null;

    /**
     * The command which determines the octave executable 
     * if {@link #octaveProgramFile} is <code>null</code> 
     * and if the property {@link #PROPERTY_EXECUTABLE} is not set. 
     * By default, this is "<code>octave</code>". 
     * The according setter method is {@link #setOctaveProgramCmd(String)}. 
     */
    private String octaveProgramCmd = "octave";

    /**
     * The array of arguments of the octave engines created. 
     * For details, see octave user manual, version 5.2.0, Section 2.1.1. 
     * <p>
     * Default value of this field is default value for octave engines created.
     * The default value consists of the following components: 
     * <ul>
     * <li>
     * <code>--silent</code>: 
     * prevents octave from printing the usual greeting and version message 
     * at startup. 
     * <li>
     * <code>--no-init-file</code>, <code>--no-site-file</code> 
     * prevents octave from reading the initialization files 
     * <code>~/.octaverc</code>, <code>.octaverc</code> and 
     * site-wide <code>octaverc</code>. 
     * <li>
     * </ul>
     *
     * The only mandatory argument is <code>--silent</code>: 
     * If not set this, octave's greeting message causes an exception. 
     * Option <code>--no-init-file</code> makes the result independent 
     * of user input, 
     * whereas <code>--no-init-file</code> and <code>--no-site-file</code> 
     * makes it independent of initialization files. 
     * Since this is used to create scripting engines, 
     * line editing and history seem superfluous 
     * and so <code>--no-line-editing</code> and <code>--no-history</code> 
     * seem appropriate. 
     * Note that <code>--no-init-file</code> and <code>--no-site-file</code> 
     * may be appropriate or not. 
     * Instead of combining <code>--no-init-file</code> and <code>--no-site-file</code> 
     * equivalently one can specify <code>--norc</code>. 
     * As this is the default,  <code>--no-gui?</code> is not needed. 
     * TBD: discuss --no-window-system
     * Note that this array may be empty but can never be <code>null</code>. 
     */
    private String[] argsArray = {
    	"--silent",          // greeting message causes exception **** 
    	"--no-init-file",    // makes result depend on init file 
     	"--no-site-file",    // see --no-init-file
   	"--no-line-editing", // make independent of user input 
    	"--no-history"       // superfluous, because commands come from scripts 
    };


    /**
     * An array of strings of the form <code>name=value</code> 
     * representing the environment, i.e. the set of environment variables 
     * or <code>null</code>. 
     * In the latter case, 
     * the environment is inherited from the current process. 
     * This field is initialized with <code>null</code>. 
     */
     private String[] environment = null;

    /**
     * The file representing the working directory or <code>null</code>. 
     * In the latter case, 
     * the working directory is inherited from the current process. 
     * By default, this is <code>null</code>. 
     */
    private File workingDir = null;

    /**
     * The number of threads to be reused or 
     * <code>-1</code> if there is no limit. 
     * By default, this is <code>2</code>. 
     */
    private int numThreadsReuse = 2;

    /**
     * Default constructor creating a factory with default parameters. 
     */
    public OctaveEngineFactory() {
        // Empty constructor
    }

    /**
     * Returns a script engine with the parameters set for this factory. 
     *
     * @return 
     *    a new OctaveEngine with the current parameters. 
     */
    public OctaveEngine getScriptEngine() {
	// determine the command/path of the octave program 
	String octaveProgramPathCmd = (this.octaveProgramFile == null)
	    ? System.getProperty(PROPERTY_EXECUTABLE, this.octaveProgramCmd)
	    : this.octaveProgramFile.getPath();

	// determine the command array 
	final String[] cmdArray = new String[this.argsArray.length + 1];
	cmdArray[0] = octaveProgramPathCmd;
	System.arraycopy(this.argsArray, 0, cmdArray, 1, this.argsArray.length);

        return new OctaveEngine(this, 
				this.numThreadsReuse,
				this.octaveInputLog, 
				this.errWriter,
				cmdArray,
				this.environment,
				this.workingDir);
    }

    /**
     * Setter method for {@link #octaveInputLog}. 
     *
     * @param octaveInputLog
     *    the octave input log to set or <code>null</code> if no such log is wanted. 
     * @return
     *    this octave engine factory after modification. 
     */
    public OctaveEngineFactory setOctaveInputLog(final Writer octaveInputLog) {
        this.octaveInputLog = octaveInputLog;
        return this;
    }

    /**
     * Setter method for {@link #errWriter}. 
     *
     * @param errWriter
     *    the errWriter to set
     *    This may be null TBC 
     * @return
     *    this octave engine factory after modification. 
     */
    public OctaveEngineFactory setErrorWriter(final Writer errWriter) {
        this.errWriter = errWriter;
        return this;
    }

    /**
     * Setter method for {@link #octaveProgramFile}. 
     *
     * @param octaveProgramFile
     *    the octaveProgramFile to set or <code>null</code>. 
     * @return
     *    this octave engine factory after modification. 
     */
    public OctaveEngineFactory setOctaveProgramFile(final File octaveProgramFile) {
        this.octaveProgramFile = octaveProgramFile;
        return this;
    }

    /**
     * Setter method for {@link #octaveProgramCmd}. 
     * This takes effect only, 
     * if {@link #octaveProgramFile} is <code>null</code> 
     * and if the property {@link #PROPERTY_EXECUTABLE} is not set. 
     *
     * @param octaveProgramCmd
     *    the octave program executable to set
     * @return
     *    this octave engine factory after modification. 
     * @throws NullPointerException
     *    if <code>octaveProgramCmd</code> is <code>null</code>. 
     */
    public OctaveEngineFactory setOctaveProgramCmd(final String octaveProgramCmd) {
	Objects.requireNonNull(octaveProgramCmd, 
		"Octave command shall be non-null. ");
        this.octaveProgramCmd = octaveProgramCmd;
        return this;
    }

    // TBD: optional: check that each entry does not contain a blank
    /**
     * Sets an array of arguments <code>argsArray</code> 
     * used when creating an {@link OctaveEngine}. 
     * The validity of the argument string is not proved. 
     * Note that subsequent changes on the array <code>argsArray</code> 
     * do not have any influence on this factory. 
     * The default options 
     * and a discussion of necessary options are 
     * documented with {@link #argsArray}. 
     *
     * @param argsArray
     *    the arguments as an array to set
     * @return
     *   this octave engine factory after modification. 
     * @throws NullPointerException
     *    if <code>argsArray</code> is <code>null</code> 
     *    or contains a <code>null</code> entry. 
     */
    public OctaveEngineFactory setArgsArray(final String[] argsArray) {
        // TBD: better to see the index where the null occurs. 
        if (Arrays.stream(argsArray).anyMatch(p -> p == null)) {
            throw new NullPointerException();
        }
        this.argsArray = Arrays.copyOf(argsArray, argsArray.length);
        return this;
    }

    /**
     * Setter method for {@link #environment}. 
     * Note that subsequent changes on the array <code>environment</code> 
     * do not have any influence on this factory. 
     * The details are documented with {@link #environment}. 
     *
     * @param environment
     *    the environment 
     *    <ul>
     *    <li>as an array of entries 
     *    of the form <code>name=value</code> </li>
     *    <li>or <code>null</code> signifying 
     *    that the environment is inherited from the invoking process. </li>  
     * @return
     *   this octave engine factory after modification. 
     */
    public OctaveEngineFactory setEnvironment(final String[] environment) {
	if (environment == null) {
	    this.environment = null;
	    return this;
	}
        // TBD: better to see the index where the null occurs. 
	// TBD: check the form 'name=value'
        if (Arrays.stream(environment).anyMatch(p -> p == null)) {
            throw new NullPointerException();
        }
        this.environment = Arrays.copyOf(environment, environment.length);
        return this;
    }

    /**
     * Setter method for {@link #workingDir}. 
     *
     * @param workingDir
     *    the working directory to set 
     *    or <code>null</code> signifying the current directory. 
     * @return
     *   this octave engine factory after modification. 
     */
    public OctaveEngineFactory setWorkingDir(final File workingDir) {
        this.workingDir = workingDir;
        return this;
    }

    /**
     * Sets the number of threads to be reused or <code>-1</code> 
     * which indicates no limit. 
     * The default value is 2 but this can be speed optimized 
     * depending on the hardware. 
     * The number of threads to be created shall be positive 
     * or <code>-1</code> otherwise throwing an exception. 
     * 
     * @param numThreadsReuse
     *    the number of threads for reuse which is positive or <code>-1</code>
     * @return
     *   this octave engine factory after modification. 
     */
    // TBC**** with -1 seems not to work: cached pool 
    public OctaveEngineFactory setNumThreadsReuse(int numThreadsReuse) {
	if (numThreadsReuse == 0 || numThreadsReuse < -1) {
	    throw new IllegalArgumentException
	    ("Expected positive number of threads or -1 but found " + 
	    numThreadsReuse + ". ");
	}
	this.numThreadsReuse = numThreadsReuse;
        return this;
    }
}
