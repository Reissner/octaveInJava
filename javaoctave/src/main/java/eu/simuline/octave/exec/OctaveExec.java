/*
 * Copyright 2007, 2008 Ange Optimization ApS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file excep in compliance with the License.
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
package eu.simuline.octave.exec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import eu.simuline.octave.exception.OctaveException;
import eu.simuline.octave.exception.OctaveIOException;
import eu.simuline.octave.util.NamedThreadFactory;
import eu.simuline.octave.util.NoCloseWriter;
import eu.simuline.octave.util.ReaderWriterPipeThread;
import eu.simuline.octave.util.TeeWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The object connecting to the octave process. 
 */
public final class OctaveExec {

    public static final String MSG_IOE_NH = 
	"InterruptedException should not happen";

    public static final String MSG_EXE_NH = 
	"ExecutionException should not happen";

    public static final String MSG_RTE_NH = 
	"RuntimeException should not happen";

    private static final Log LOG = LogFactory.getLog(OctaveExec.class);

    /**
     * The octave process created in the constructor 
     * with given command, arguments, environment and working directory. 
     * This is initialized 
     * in {@link #OctaveExec(int, Writer, Writer, Charset, String[], String[], File)} 
     * and used in {@link #close()} and {@link #destroy()} only. 
     */
    private final Process process;

    /**
     * The output writer for {@link #process} derived from {@link Process#getOutputStream()}. 
     */
    private final Writer processWriter;

    /**
     * The input reader for {@link #process} derived from {@link Process#getInputStream()}. 
     * This is used by {@link #evalRW(WriteFunctor, ReadFunctor)} 
     * and used to close via {@link #close()}. 
     */
    private final BufferedReader processReader;

    /**
     * Used in method {@link #evalRW(WriteFunctor, ReadFunctor)} 
     * to submit essentially the write functor which submits the input 
     * and thereafter the read function which collects the output. 
     * Besides this, the executor is invoked to shutdown. 
     */
    private final ExecutorService executor;

    /**
     * The error thread of the error stream of {@link #process} 
     * writing the error stream to a given writer. 
     * This is used to close but also to change the error writer 
     * by {@link #setErrorWriter(Writer)}. 
     */
    private final ReaderWriterPipeThread errorStreamThread;

    private boolean destroyed = false;

    /**
     * Will start the octave process.
     *
     * @param numThreadsReuse
     *    the number of threads to be reused in a fixed thread pool. 
     *    This is either positive or <code>-1</code>, 
     *    which means that a cached thread pool is used instead of a fixed one. 
     * @param stdinLog
     *    This writer will capture all
     *    that is written to the octave process via stdin,
     *    if null the data will not be captured.
     * @param stderrLog
     *    This writer will capture all
     *    that is written from the octave process on stderr,
     *    if null the data will not be captured. 
     * @param charset
     *    the charset used for communication with the octave process. 
     * @param cmdArray
     *    The array consisting of command and arguments: 
     *    The 0th entry is either the path to the octave program,
     *    or the command found by looking at the built-in variable "paths" 
     *    reconstructing the path. 
     *    starting with the 1th entry, 
     *    may follow the array of arguments to start the octave program with. 
     *    CAUTION: allowed values depend on the octave version. 
     * @param environment
     *    Either the environment for the octave process, 
     *    i.e. the set of values of environment variables 
     *    with each entry of the form <code>name=value</code> 
     *    or null to make {@link #process}, 
     *    the process created, inherit the environment of the current process. 
     * @param workingDir
     *    Either the working directory for the octave process, or <code>null</code> to make {@link #process}, 
     *    the process created, inherit the working directory
     *    of the current process.
     * @throws OctaveIOException
     *    If execution
     */
    public OctaveExec(final int numThreadsReuse,
		      final Writer stdinLog, 
		      final Writer stderrLog, 
		      final Charset charset, // TBD: ensure that various charsets fit. 
		      final String[] cmdArray,
		      final String[] environment, // always invoked with null 
		      final File workingDir) {
	ThreadFactory threadFactory = new NamedThreadFactory();
	this.executor = numThreadsReuse == -1
	    ? Executors.newCachedThreadPool(threadFactory)
	    : Executors.newFixedThreadPool(numThreadsReuse, threadFactory);

	try {
	    // exec may throw 
	    // - SecurityException TBC
	    // - UnsupportedOperationException TBC
	    // - IOException (handled by catch)
	    // - NullPointerException if cmdArray is null 
	    //   or so is one of its components, 
	    // - IndexOutOfBoundsException if cmdArray is empty
	    // The latter two are excluded. 
            this.process = Runtime.getRuntime().exec(cmdArray, 
						     environment, 
						     workingDir);
        } catch (final IOException e) {
            throw new OctaveIOException(e);
        }
        // Connect stderr
        this.errorStreamThread = ReaderWriterPipeThread
	    .instantiate(new InputStreamReader(this.process.getErrorStream(), charset),
			 stderrLog);

        // Connect stdout
        this.processReader = new BufferedReader
	    (new InputStreamReader(this.process.getInputStream(), charset));

        // Connect stdin
	Writer pw = new OutputStreamWriter(this.process.getOutputStream(), charset);
	// all written to processWriter will go to pw and, 
	// if not null to stdinLog
	this.processWriter = (stdinLog == null)
	    ? pw
	    : new TeeWriter(new NoCloseWriter(stdinLog), pw);
    }

    private final Random random = new Random();

    private String generateSpacer() {
        return "-=+X+=- Octave.java spacer -=+X+=- " + 
	    this.random.nextLong() + " -=+X+=-";
    }

    /**
     * Passes <code>input</code> to octave, get back <code>output</code> 
     * and throws according exceptions if reading or writing went wrong. 
     *
     * @param input
     *    a write functor which represents the script 
     *    to be executed in octave. 
     * @param output
     *    the read functor which reads the result of octave execution. 
     *    After evaluation of this method, 
     *    the <code>output</code> is asked for the result. 
     *///<code></code>
    // used in OctaveIO#set(Map), OctaveIO#get(String), 
    // OctaveIO#checkIfVarExists(String) and in 
    // OctaveEngine#unsafeEval(String) OctaveEngine#unsafeEval(Reader) and 
    // OctaveEngine#getVersion() only 
    // TBD: document which exceptions can be thrown in detail 
    public void evalRW(final WriteFunctor input, final ReadFunctor output) {
        final String spacer = generateSpacer();
        final Future<Void> writerFuture = 
	    this.executor.submit(new OctaveWriterCallable(this.processWriter, 
							  input, 
							  spacer));
        final Future<Void> readerFuture = 
	    this.executor.submit(new OctaveReaderCallable(this.processReader, 
							  output, 
							  spacer));
        final RuntimeException writerException = getFromFuture(writerFuture);
        // if (writerException instanceof CancellationException) {
        //     LOG.error("Did not expect writer to be canceled", 
	//               writerException);
        // }
        if (writerException != null) {
            if (writerException instanceof CancellationException) {
                LOG.error("Did not expect writer to be canceled", 
	    		  writerException);
            }
            readerFuture.cancel(true); // may interrupt if running 
	    throw writerException;
        }
        final RuntimeException readerException = getFromFuture(readerFuture);
        // if (writerException != null) {
        //     throw writerException;
        // }
        if (readerException != null) {
            // Only gets here when writerException==null, 
	    // and in that case we don't expect the reader to be cancelled
            if (readerException instanceof CancellationException) {
                LOG.error("Did not expect reader to be canceled", 
			  readerException);
            }
            throw readerException;
        }
    }

    /**
     * Completes computation on future 
     * and returns an exception thrown or null. 
     */
    private RuntimeException getFromFuture(Future<Void> future) {
	try {
            future.get();
        } catch (final InterruptedException e) {
            LOG.error(MSG_IOE_NH, e);
            return new RuntimeException(MSG_IOE_NH, e);
        } catch (final ExecutionException e) {
            if (e.getCause() instanceof OctaveException) {
                final OctaveException oe = (OctaveException) e.getCause();
                return reInstException(oe);
            }
            // Can happen when there is an error in a OctaveWriter
 	    LOG.error(MSG_EXE_NH, e);
	    return new RuntimeException(MSG_EXE_NH, e);
        } catch (final CancellationException e) {
            return e;
        } catch (final RuntimeException e) { // NOPMD 
            LOG.error(MSG_RTE_NH, e);
            return new RuntimeException(MSG_RTE_NH, e);
        }
        return null;
    }

    /**
     * Used by {@link #getFromFuture(Future)} 
     * to re-instantiate an {@link OctaveException} 
     * if this occurs as the cause of an {@link ExecutionException}. 
     */
    private OctaveException reInstException(OctaveException exc) {
        OctaveException res;
        try {
            res = exc.getClass()
		// may throw NoSuchMethodException 
		// isa ReflectiveOperationException, 
		// SecurityException isa RuntimeException 
		.getConstructor(String.class, Throwable.class)
		// may throw 
		// IllegalArgumentException, 
		// ReflectiveOperationException: 
		// - IllegalAccessException: constructor inaccessible 
		// - InstantiationException: Exception class is abstract 
		// - InvocationTargetException: if the constructor throws an exc
		// ExceptionInInitializerError
		.newInstance(exc.getMessage(), exc);
        } catch (RuntimeException e) { // NOPMD
            throw new IllegalStateException("Exception should not happen", e);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Exception should not happen", e);
        } catch (ExceptionInInitializerError e) {
            throw new IllegalStateException("Error should not happen", e);
	}
        if (isDestroyed()) {
            res.setDestroyed(true);
        }
        return res;
    }

    /**
     * Sets {@link #destroyed} to the parameter value given. 
     */
    private synchronized void setDestroyed(final boolean destroyed) {
        this.destroyed = destroyed;
    }

    /**
     * Returns {@link #destroyed}. 
     */
    private synchronized boolean isDestroyed() {
        return this.destroyed;
    }

    /**
     * Kill the octave process without remorse. 
     */
    public void destroy() {
        setDestroyed(true);
        this.executor.shutdownNow(); // returns list of tasks awaiting exec. 
        this.process.destroy();
        this.errorStreamThread.close();
        try {
            this.processWriter.close();
        } catch (final IOException e) {
            LOG.debug("Ignored error from processWriter.close() " + 
		      "in OctaveExec.destroy()", e);
	}
    }

    /**
     * Close the octave process in an orderly fashion: 
     * Send command <code>exit</code> and expect a single line in return, 
     * namely an empty one. 
     *
     * @throws OctaveIOException
     *    if 
     */
    public void close() {
        try {
            // it is not worth it to rewrite this 
	    // to use eval() and some specialized Functors
            // the next three commands all may throw IOException 
            this.processWriter.write("exit\n");
            this.processWriter.close();
            final String read1 = this.processReader.readLine();
            
            // Allow a single blank line, exit in octave 3.2 returns that:
            if (read1 != null && !"".equals(read1)) {
                throw new OctaveIOException
		    ("Expected a blank line, read '" + read1 + "'");
            }
            final String read2 = this.processReader.readLine();// may throw IOExceptino 
            if (read2 != null) {
                throw new OctaveIOException
		    ("Expected reader to be at end of stream, read '" + 
		     read2 + "'");
            }
            // may throw IOException 
            this.processReader.close();
            // may throw IOException 
            this.errorStreamThread.close();
            int exitValue;
            try {
                exitValue = this.process.waitFor();
            } catch (final InterruptedException e) {
                throw new OctaveIOException
		    ("Interrupted when waiting for octave process " + 
		     "to terminate", e);
            }
            if (exitValue != 0) {
                throw new OctaveIOException
		    ("octave process terminated abnormaly, exitValue='" + 
		     exitValue + "'");
            }
        } catch (final IOException e) {
            // TBD: correct this: may be also a problem with the writer. 
            // Reader: may be processReader or error stream. 
            final OctaveIOException octaveException = 
		new OctaveIOException("reader error", e);
            if (isDestroyed()) {
                octaveException.setDestroyed(true);
            }
            throw octaveException;
        } finally {
            this.executor.shutdown();
        }
    }

    /**
     * @param writer
     *    the new writer to write the error output to
     */
    public void setErrorWriter(final Writer writer) {
        this.errorStreamThread.setWriter(writer);
    }

}
