/*
 * Copyright 2007, 2008, 2009 Ange Optimization ApS
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.simuline.octave.util.StringUtil;

/**
 * Reader that passes the reading on to the output from the octave process 
 * until the spacer reached, then it returns EOF. 
 * When this reader is closed 
 * the underlying reader is slurped up to the spacer. 
 */
final class OctaveExecuteReader extends Reader {

    private static final Log LOG = LogFactory.getLog(OctaveExecuteReader.class);

    /**
     * The wrapped reader. 
     */
    private final BufferedReader octaveReader;

    /**
     * The string signifying end of stream. 
     */
    private final String spacer;

    /**
     * The current line read from {@link #octaveReader} 
     * but not yet passed to a char-array by {@link #read(char[], int, int)}. 
     * If this buffer were empty, it is <code>null</code> instead, 
     * which is also the initial value. 
     * If this is not the first line, the line read from {@link #octaveReader} 
     * is preceeded by newline before being passed to {@link #buffer}. 
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
     * not really end of {@link #octaveReader}. 
     */
    private boolean eof = false;

    /**
     * This reader will read from <code>octaveReader</code> 
     * until a single line equal() <code>spacer</code> is read, 
     * after that this reader will return eof. 
     * When this reader is closed it will update the state of octave to NONE. 
     *
     * @param octaveReader
     *    the wrapped reader 
     * @param spacer
     *    the line signifying end of stream. 
     */
    OctaveExecuteReader(final BufferedReader octaveReader, 
			final String spacer) {
        this.octaveReader = octaveReader;
        this.spacer = spacer;
    }

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
     *    if null-line has been read from {@link #octaveReader}. 
     */
    @Override
    public int read(final char[] cbuf, final int off, final int len) 
	throws IOException {
        if (this.eof) {
            return -1;
        }
        if (this.buffer == null) {
            final String line = this.octaveReader.readLine();
            if (LOG.isTraceEnabled()) {
                LOG.trace("octaveReader.readLine() = " + 
			  StringUtil.jQuote(line));
            }
            if (line == null) {
                throw new IOException("Pipe to octave-process broken");
            }
            if (this.spacer.equals(line)) {
                this.eof = true;
                return -1;
            }

	    // line possibly preceeded by \n 
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
        while (read(buffer1) != -1) { // NOPMD 
            // Do nothing
        }
        if (this.octaveReader.ready()) {
            throw new IOException("octaveReader is ready()");
        }
        LOG.debug("Reader closed()");
    }

}
