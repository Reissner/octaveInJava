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

import java.nio.charset.Charset;

import java.util.Arrays;

/**
 * Factory that creates OctaveEngines. 
 */
public final class OctaveEngineFactory {

    private Writer octaveInputLog = null;

    private Writer errWriter = new OutputStreamWriter(System.err, 
						      Charset.forName("UTF-8"));

    /**
     * The file containing the octave program. 
     * This field is initialize with <code>null</code>. 
     */
    private File octaveProgram = null;

    /**
     * The array of arguments of the octave engines created. 
     * For details, see octave user manual, version 3.4.0.+, Section 2.2.1. 
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
     * ***** why not needed --no-gui? --no-window-system
     */
    private String[] argsArray = {
    	"--silent",          // greeting message causes exception **** 
    	"--no-init-file",    // makes result depend on init file 
     	"--no-site-file",    // see --no-init-file
   	"--no-line-editing", // make independent of user input 
    	"--no-history"       // superfluous, because commands come from scripts 
    };

    /**
     * The number of threads to be reused. 
     * This field is initialize with <code>2</code>. 
     */
    private int numThreadsReuse = 2;

    private File workingDir = null;

    /**
     * Default constructor creating a factory with default parameters. 
     */
    public OctaveEngineFactory() {
        // Empty constructor
    }

    /**
     * Returns a script engine with the parameters set for this factory. 
     *
     * @return a new OctaveEngine
     */
    public OctaveEngine getScriptEngine() {
        return new OctaveEngine(this, 
				this.numThreadsReuse,
				this.octaveInputLog, 
				this.errWriter, 
				this.octaveProgram, 
				argsArray.clone(),
				this.workingDir);
    }

    /**
     * @param octaveInputLog
     *    the octaveInputLog to set
     */
    public void setOctaveInputLog(final Writer octaveInputLog) {
        this.octaveInputLog = octaveInputLog;
    }

    /**
     * @param errWriter
     *    the errWriter to set
     */
    public void setErrorWriter(final Writer errWriter) {
        this.errWriter = errWriter;
    }

    /**
     * @param octaveProgram
     *            the octaveProgram to set
     */
    public void setOctaveProgram(final File octaveProgram) {
        this.octaveProgram = octaveProgram;
    }

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
     */
    public void setArgsArray(final String[] argsArray) {
        this.argsArray = Arrays.copyOf(argsArray, argsArray.length);
    }

    /**
     * @param workingDir
     *    the workingDir to set
     */
    public void setWorkingDir(final File workingDir) {
        this.workingDir = workingDir;
    }

    /**
     * Sets the number of threads to be reused. 
     * The default value is 2 but this can be speed optimized 
     * depending on the hardware. 
     * The number of threads to be created shall be positive 
     * but the value may also be -1 indicating a cached thread pool. 
     */
    // **** with -1 seems not to work: cached pool 
    public void setNumThreadsReuse(int numThreadsReuse) {
	if (numThreadsReuse == 0 || numThreadsReuse < -1) {
	    throw new IllegalArgumentException();
	}
	this.numThreadsReuse = numThreadsReuse;
    }
}
