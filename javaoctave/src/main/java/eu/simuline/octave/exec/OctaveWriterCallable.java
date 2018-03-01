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

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;

import eu.simuline.octave.exception.OctaveIOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Callable that writes to the octave process. 
 * Used in {@link OctaveExec#evalRW(WriteFunctor, ReadFunctor)} only. 
 */
final class OctaveWriterCallable implements Callable<Void> {

    private static final Log LOG = LogFactory
	.getLog(OctaveWriterCallable.class);

    static final String EXCEPTION_MESSAGE_FUNCTOR = 
	"IOException from WriteFunctor";

    static final String EXCEPTION_MESSAGE_SPACER = 
	"IOException when writing spacer";


    private final Writer processWriter;

    private final WriteFunctor writeFunctor;

    private final String spacer;

    /**
     * @param processWriter
     * @param writeFunctor
     * @param spacer
     */
    OctaveWriterCallable(final Writer processWriter, 
			 final WriteFunctor writeFunctor, 
			 final String spacer) {
        this.processWriter = processWriter;
        this.writeFunctor  = writeFunctor;
        this.spacer        = spacer;
    }

    /**
     * Calling writes to {@link #processWriter}: 
     * first according to {@link #writeFunctor} 
     * then printf of {@link #spacer} and then flush. 
     * Exceptions are logged on {@link #LOG}. 
     */
    @Override
    public Void call() {
        // Write to process
        try {
	    this.writeFunctor.doWrites(this.processWriter);
        } catch (final IOException e) {
            LOG.debug(EXCEPTION_MESSAGE_FUNCTOR, e);
            throw new OctaveIOException(EXCEPTION_MESSAGE_FUNCTOR, e);
        }
        try {
	    this.processWriter.write("\nprintf(\"\\n%s\\n\", \"" + 
				     this.spacer + "\");\n");
            this.processWriter.flush();
        } catch (final IOException e) {
            LOG.debug(EXCEPTION_MESSAGE_SPACER, e);
            throw new OctaveIOException(EXCEPTION_MESSAGE_SPACER, e);
        }
        LOG.debug("Has written all");
        return null;
    }

}
