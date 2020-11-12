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
import java.io.Reader;
import java.io.Writer;

import eu.simuline.octave.exception.OctaveIOException;
import eu.simuline.octave.exception.OctaveInterruptedException;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * A Thread that moves data from a Reader to a Writer. 
 * 
 * @author Kim Hansen
 */
public final class ReaderWriterPipeThread extends Thread {

    private static final Log LOG = LogFactory
	.getLog(ReaderWriterPipeThread.class);

    @SuppressWarnings("checkstyle:magicnumber")
    private static final char[] BUF = new char[4 * 1024];
 
    private final Reader reader;

    private Writer writer;

    /**
     * Will create a thread that reads from reader and writes to write 
     * until reader reaches EOF. 
     * Then the thread will close. 
     * Remember to join() this thread before closing reader or writer.
     * 
     * @param reader
     * @param writer
     *    may be null TBC: does this make sense? 
     * @return Returns the new thread
     */
    public static ReaderWriterPipeThread instantiate(final Reader reader, 
						     final Writer writer) {
        final ReaderWriterPipeThread readerWriterPipeThread = 
	    new ReaderWriterPipeThread(reader, writer);
        readerWriterPipeThread.setName(Thread.currentThread().getName() 
				       + "-javaoctave-"
				       + ReaderWriterPipeThread.class
				       .getSimpleName());
	readerWriterPipeThread
	    .setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
		    public void uncaughtException(Thread th, Throwable ex) {
			System.out.println("Uncaught : " + ex + 
					   " of thread " + th.getName());
		    }
		});
        readerWriterPipeThread.start();
        return readerWriterPipeThread;
    }

    /**
     * 
     * @param reader
     * @param writer
     *    may be null TBC: does this make sense? 
     */
    private ReaderWriterPipeThread(final Reader reader, final Writer writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            int len;
            try {
                len = this.reader.read(BUF);
            } catch (final IOException e) {
                LOG.error("Error when reading from reader", e);
                throw new OctaveIOException(e);
            }
            if (len == -1) {
                break;
            }
            try {
                synchronized (this) {
                    if (this.writer != null) {
                	this.writer.write(BUF, 0, len);
                	this.writer.flush();
                    }
                }
            } catch (final IOException e) {
                LOG.error("Error when writing to writer", e);
                throw new OctaveIOException(e);
            }
        }
        LOG.debug("ReaderWriterPipeThread finished without error");
    }

    /**
     * @param writer
     *    the writer to set
     *    This may be null TBC 
     */
    public void setWriter(final Writer writer) {
        synchronized (this) {
            this.writer = writer;
        }
    }

    /**
     * Close the thread.
     */
    public void close() {
        interrupt();
        try {
            join();
        } catch (final InterruptedException e) {
            throw new OctaveInterruptedException(e);
        }
    }

}
