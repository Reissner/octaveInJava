/*
 * Copyright 2008, 2012 Ange Optimization ApS
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
package dk.ange.octave.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import dk.ange.octave.exception.OctaveIOException;
import dk.ange.octave.exec.WriteFunctor;
import dk.ange.octave.type.OctaveObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Write data from {@link OctaveObject}s in a {@link Map}. 
 */
final class DataWriteFunctor implements WriteFunctor {

    private static final Log LOG = LogFactory.getLog(DataWriteFunctor.class);

    /**
     * A map from variable names to according octave objects. 
     */
    private final Map<String, OctaveObject> octaveTypes;

    /**
     * @param octaveTypes
     *    A map from variable names to according octave objects. 
     */
    DataWriteFunctor(final Map<String, OctaveObject> octaveTypes) {
        this.octaveTypes = octaveTypes;
    }

    /**
     * Writes this functor representing the map {@link #octaveTypes} 
     * to <code>writer</code>. 
     *
     * @param writer
     *    the writer this variable to value configuration is to be written to. 
     */
    public void doWrites(final Writer writer) {
        try {
            // Enter octave in "read data from input mode"
	    LOG.trace("write: 'load(\"-text\", \"-\")' " + 
		      "to start read data from input mode");
	    writer.write("load(\"-text\", \"-\")\n");
            // Push the data into octave
            for (final Map.Entry<String, OctaveObject> entry 
		     : octaveTypes.entrySet()) {
                final String name = entry.getKey();
                final OctaveObject value = entry.getValue();
		if (LOG.isTraceEnabled()) {
                    LOG.trace("write: variable '" + name + 
			      "', value=<<<" + value + ">>>");
                }
                OctaveIO.write(writer, name, value);
            }
            // Exit octave from read data mode
            LOG.trace("write: '# name:' to exit octave from read data mode");
            writer.write("# name: \n");
            writer.flush();
        } catch (final IOException e) {
            // Will happen when we write to a dead octave process
            final String message = "Unexpected IOException";
            LOG.debug(message, e);
            throw new OctaveIOException(message, e);
        }
    }

}
