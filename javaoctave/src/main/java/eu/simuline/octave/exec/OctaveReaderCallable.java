/*
 * Copyright 2008, 2009 Ange Optimization ApS
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
package eu.simuline.octave.exec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.Callable;

import eu.simuline.octave.exception.OctaveIOException;
import eu.simuline.octave.util.StringUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * {@link Callable} that reads from the octave process. 
 * Used in {@link OctaveExec#evalRW(WriteFunctor, ReadFunctor)} only. 
 * This is complementary to {@link OctaveWriterCallable}. 
 */
final class OctaveReaderCallable implements Callable<Void> {

    private static final Log LOG = LogFactory.getLog(OctaveReaderCallable.class);
    
   private static final String MSG_IOE_READ = 
	"IOException from ReadFunctor";
    private static final String MSG_IOE_CLS = 
	"IOException during close";

    /**
     * The reader for feedback on scripts received from octave. 
     * This is nothing but {@link OctaveExec#processReader}. 
     */
    private final BufferedReader processReader;

    /**
     * The functor the reading task is delegated to. 
     */
    private final ReadFunctor readFunctor;

    /**
     * A string essentially consisting of a unique hashvalue. 
     * It has been printed by the according {@link OctaveWriterCallable} 
     * after having applied the write functor 
     * and thus indicates the end of the read sequence of the octave process.  
     */
    private final String spacer;

    // TBC: strictly speaking, this goes wrong with a small but positive probability. 
    /**
     * @param processReader
     *    the reader used for reading. 
     * @param readFunctor
     *    the functor the reading process is delegated to. 
     * @param spacer
     *    a string essentially consisting of a unique hashvalue 
     *    printed at the end of the according write process 
     *    in {@link OctaveExec#evalRW(WriteFunctor, ReadFunctor)} 
     *    and thus signifying the end of the sequence to be read.
     */
    OctaveReaderCallable(final BufferedReader processReader, 
			 final ReadFunctor readFunctor, 
			 final String spacer) {
        this.processReader = processReader;
        this.readFunctor   = readFunctor;
        this.spacer        = spacer;
    }

    /**
     * Reader that passes the reading on to the output from the octave process, 
     * i.e. from {@link OctaveReaderCallable#processReader} 
     * until the spacer {@link OctaveReaderCallable#spacer} reached, then it returns EOF. 
     * When this reader is closed 
     * the underlying reader is slurped up to the spacer. 
     * <p>
     * This is used in {@link OctaveReaderCallable#call()} only. 
     */
    // TBC: maybe thus this shall be an inner class. 
    final class OctaveExecuteReader extends Reader {

        /**
         * The current line read from {@link OctaveReaderCallable#processReader} 
         * but not yet passed to a char-array by {@link #read(char[], int, int)}. 
         * If this buffer were empty, it is <code>null</code> instead, 
         * which is also the initial value. 
         * If this is not the first line, the line read from {@link OctaveReaderCallable#processReader} 
         * is preceded by newline before being passed to {@link #buffer}. 
         */
        private StringBuffer buffer = null;

        /**
         * Whether reading the first line. 
         * Initially, this is true. 
         * It is set to false, by {@link #read(char[], int, int)} 
         * if {@link #buffer} is filled for the first time. 
         */
        private boolean firstLine = true;

        /**
         * Whether end of reader found. 
         * Initially, this is false. 
         * It is set to false, by {@link #read(char[], int, int)} 
         * if {@link #buffer} equals {@link #spacer}, 
         * not really end of {@link OctaveReaderCallable#processReader}. 
         */
        private boolean eof = false;

        /**
         * Reads characters into a portion of an array. 
         * This method will block until some input is available, 
         * an I/O error occurs, or the end of the stream is reached.
         *
         * @param cbuf 
         *    Destination buffer
         * @param off 
         *    Offset at which to start storing characters. 
         * @param len 
         *    Maximum number of characters to read. 
         * @return
         *    The number of characters read, 
         *    or -1 if the end of the stream has been reached. 
         *    The latter is the case if {@link #eof} is set 
         *    which means that line {@link #spacer} has been found. 
         * @throws IOException 
         *    If an I/O error occurs. 
         *    This is true in particular, 
         *    if null-line has been read from {@link OctaveReaderCallable#processReader}. 
         */
        @Override
        public int read(final char[] cbuf, final int off, final int len) 
    	throws IOException {
            if (this.eof) {
                return -1;
            }
            if (this.buffer == null) {
                // may throw IOException 
                final String line = OctaveReaderCallable.this.processReader.readLine();
                if (OctaveReaderCallable.LOG.isTraceEnabled()) {
                    OctaveReaderCallable.LOG.trace("octaveReader.readLine() = " + 
    			  StringUtil.jQuote(line));
                }
                if (line == null) {
                    throw new IOException("Pipe to octave-process broken");
                }
                if (OctaveReaderCallable.this.spacer.equals(line)) {
                    this.eof = true;
                    return -1;
                }

    	    // line possibly preceded by \n 
                this.buffer = new StringBuffer(line.length() + 1);
                if (this.firstLine) {
                    this.firstLine = false;
                } else {
                    this.buffer.append('\n');
                }
                this.buffer.append(line);
            }
    	assert this.buffer != null;

            final int charsRead = Math.min(this.buffer.length(), len);
            this.buffer.getChars(0, charsRead, cbuf, off);
            if (charsRead == buffer.length()) {
                this.buffer = null;
            } else {
                this.buffer.delete(0, charsRead);
            }
            return charsRead;
        }

        @Override
        @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:emptyblock"})
        // length of buffer is immaterial for function 
        public void close() throws IOException {
            final char[] buffer1 = new char[4096];
            // Slurp the rest of the wrapped input
            // may throw IOException 
            while (read(buffer1) != -1) { // NOPMD 
                // Do nothing
            }
            // may throw IOException 
            if (OctaveReaderCallable.this.processReader.ready()) {
                throw new IOException("octaveReader is ready()");
            }
            OctaveReaderCallable.LOG.debug("Reader closed()");
        }

    } // class OctaveExecuteReader

    // TBC: what about spacer?
    // where do deviations from OctaveWriterCallable.call() come from? 
    /**
     * Calling essentially reads from {@link #processReader} representing the octave process: 
     * Reads until {@link #spacer} is detected which is not really read but is interpreted 
     * as an eof symbol. 
     * To that end, {@link #processReader} is wrapped into an {@link OctaveExecuteReader} 
     * with parameter {@link #spacer} which yields eof if the spacer is detected. 
     * Closing the {@link OctaveExecuteReader} reads the spacer without passing it further.  
     * Exceptions are logged on {@link #LOG}. 
     *
     * @throws OctaveIOException 
     *    if underlying {@link #readFunctor} or the reader {@link #processReader} or 
     *    the wrapping {@link  OctaveExecuteReader} 
     *    throws an {@link IOException}. 
     */
    @Override
    public Void call() {
        final Reader reader = new OctaveExecuteReader();
        try {
            this.readFunctor.doReads(reader);
        } catch (final IOException e) {
	    LOG.debug(MSG_IOE_READ, e);
            throw new OctaveIOException(MSG_IOE_READ, e);
        } finally { // NOPMD
            try {
        	// this slurps the spacer 
                reader.close();
            } catch (final IOException e) {
                LOG.debug(MSG_IOE_CLS, e);
                throw new OctaveIOException(MSG_IOE_CLS, e);
            }
        }
        return null;
    }

}
