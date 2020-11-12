/*
 * Copyright 2007, 2008 Ange Optimization ApS
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
package eu.simuline.octave.util;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Will protect a Writer from being closed by {@link #close()}, 
 * useful for protecting stdout and stderr from being closed. 
 * Trying to close via {@link #close()} results in loosing connection 
 * whereas {@link #reallyClose()} really closes the writer. 
 * 
 * @author Kim Hansen
 */
public final class NoCloseWriter extends Writer {

    private static final Log LOG = LogFactory.getLog(NoCloseWriter.class);

    /**
     * A writer or <code>null</code>. 
     * The latter is the case if initialized with <code>null</code>, 
     * or if tried to close via {@link #close()}. 
     * In the latter case 
     * it is not even possible to close with {@link #reallyClose()}. 
     */
    private Writer writer;

    /**
     * Create a NoCloseWriter that will protect writer.
     * 
     * @param writer
     *    the writer to be protected from being closed by {@link #close()}.
     */
    public NoCloseWriter(final Writer writer) {
        this.writer = writer;
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) 
	throws IOException {

        if (this.writer == null) {
            return;
        }
        this.writer.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        if (this.writer == null) {
            return;
        }
        this.writer.flush();
    }

    /**
     * Flushes the writer and looses the connection to it. 
     * No close and also in future no close possible. 
     * 
     * @throws IOException
     *    from the underlying writer. 
     * @see #reallyClose()
     */
    @Override
    public void close() throws IOException {
        LOG.debug("ignoring close() on a writer");
        if (this.writer == null) {
            return;
        }
        this.writer.flush();
        this.writer = null;
    }

    /**
     * Really closes the underlying writer, with one exception ;-).
     * 
     * @throws IOException
     *    from the underlying writer.
     * @throws NullPointerException
     *    if the NoCloseWriter has been closed via {@link #close()}.
     */
    public void reallyClose() throws IOException {
        LOG.debug("reallyClose() a writer");
        this.writer.close();
    }

}
