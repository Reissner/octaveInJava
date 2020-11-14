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
 * {@link Callable} that writes scripts to the octave process. 
 * Used in {@link OctaveExec#evalRW(WriteFunctor, ReadFunctor)} only. 
 * This is complementary to {@link OctaveReaderCallable}. 
 */
final class OctaveWriterCallable implements Callable<Void> {

    private static final Log LOG = LogFactory
	.getLog(OctaveWriterCallable.class);

    static final String MSG_IOE_WRITE =
	"IOException from WriteFunctor";

    static final String MSG_IOE_SPACER =
	"IOException when writing spacer";

    /**
     * The writer for scripts passed to octave. 
     * This is nothing but {@link OctaveExec#processWriter}. 
     */
    private final Writer processWriter;

    /**
     * The functor the writing task is delegated to. 
     */
    private final WriteFunctor writeFunctor;

    /**
     * A string essentially consisting of a unique hashvalue. 
     * It is printed to {@link #processWriter} 
     * after having applied {@link #writeFunctor}. 
     * That way the according {@link OctaveReaderCallable} 
     * detects the end of the read sequence from the octave process.   
     */
    private final String spacer;

    // TBC: strictly speaking,
    // this goes wrong with a small but positive probability. 
    /**
     * Creates a new OctaveWriterCallable with method {@link #call()} 
     * delegating writing 
     * 
     * @param processWriter
     *    the writer used for writing. 
     * @param writeFunctor
     *    the functor the writing process is delegated to. 
     * @param spacer
     *    a string essentially consisting of a unique hashvalue 
     *    printed after applying the write functor.  
     *    This indicates the end of the sunbsequent according reading process 
     *    in {@link OctaveExec#evalRW(WriteFunctor, ReadFunctor)}.
     */
    OctaveWriterCallable(final Writer processWriter, 
			 final WriteFunctor writeFunctor, 
			 final String spacer) {
        this.processWriter = processWriter;
        this.writeFunctor  = writeFunctor;
        this.spacer        = spacer;
    }

    /**
     * Calling writes to {@link #processWriter} 
     * representing the octave process: 
     * first according to {@link #writeFunctor}, 
     * i.e. delegates to {@link WriteFunctor#doWrites(Writer)},
     * then writes <code>printf</code> of {@link #spacer} and then flush. 
     * That way, {@link OctaveReaderCallable#call()} knows, that reading is completed, 
     * as soon as the spacer is detected. 
     * Exceptions are logged on {@link #LOG}. 
     * 
     * @throws OctaveIOException 
     *    if the underlying {@link #writeFunctor} or {@link #processWriter} 
     *    throws an {@link IOException}. 
     */
    @Override
    public Void call() {
        // Write to process
        try {
	    this.writeFunctor.doWrites(this.processWriter);
        } catch (final IOException e) {
            LOG.debug(MSG_IOE_WRITE, e);
            throw new OctaveIOException(MSG_IOE_WRITE, e);
        }
        try {
	    this.processWriter.write("\nprintf(\"\\n%s\\n\", \"" + 
				     this.spacer + "\");\n");
            this.processWriter.flush();
        } catch (final IOException e) {
            LOG.debug(MSG_IOE_SPACER, e);
            throw new OctaveIOException(MSG_IOE_SPACER, e);
        }
        LOG.debug("Has written all");
        return null;
    }

}
