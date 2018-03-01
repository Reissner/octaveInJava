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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Callable that reads from the octave process. 
 * Used in {@link OctaveExec#evalRW(WriteFunctor, ReadFunctor)} only. 
 */
final class OctaveReaderCallable implements Callable<Void> {

    private static final String MSG_IOE_READ = 
	"IOException from ReadFunctor";
    private static final String MSG_IOE_CLS = 
	"IOException during close";

    private static final Log LOG = LogFactory
	.getLog(OctaveReaderCallable.class);


    private final BufferedReader processReader;

    private final ReadFunctor readFunctor;

    private final String spacer;

    /**
     * @param processReader
     * @param readFunctor
     * @param spacer
     */
    OctaveReaderCallable(final BufferedReader processReader, 
			 final ReadFunctor readFunctor, 
			 final String spacer) {
        this.processReader = processReader;
        this.readFunctor   = readFunctor;
        this.spacer        = spacer;
    }

    /**
     * Calling reads from an {@link OctaveExecuteReader} 
     * made up from {@link #processReader} 
     * with end of stream sign {@link #spacer}. 
     * Exceptions are logged on {@link #LOG}. 
     *
     * @throws OctaveIOException 
     *    if underlying readers/read functors 
     *    have thrown an {@link IOException}. 
     */
    @Override
    public Void call() {
        final Reader reader = new OctaveExecuteReader(this.processReader, 
						      this.spacer);
        try {
            this.readFunctor.doReads(reader);
        } catch (final IOException e) {
	    LOG.debug(MSG_IOE_READ, e);
            throw new OctaveIOException(MSG_IOE_READ, e);
        } finally { // NOPMD
            try {
                reader.close();
            } catch (final IOException e) {
                LOG.debug(MSG_IOE_CLS, e);
                throw new OctaveIOException(MSG_IOE_CLS, e);
            }
        }
        return null;
    }

}
