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
package eu.simuline.octave.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import eu.simuline.octave.exception.OctaveIOException;
import eu.simuline.octave.exec.WriteFunctor;
import eu.simuline.octave.type.OctaveObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Write data from {@link OctaveObject}s into a {@link Map}. 
 */
// ER: Very strange: whereas this write functor writes a map, 
// the according read functor reads a single variable only: 
// see OctaveIO 
final class DataWriteFunctor implements WriteFunctor {

    private static final String MSG_IOE_UNEXP = "Unexpected IOException";

    private static final Log LOG = LogFactory.getLog(DataWriteFunctor.class);

    /**
     * A map from variable names to according octave objects. 
     */
    private final Map<String, OctaveObject> varName2Value;

    /**
     * @param varName2Value
     *    A map from variable names to according octave objects. 
     */
    DataWriteFunctor(final Map<String, OctaveObject> varName2Value) {
        this.varName2Value = varName2Value;
    }

    /**
     * Writes the name-value pairs of map {@link #varName2Value} 
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
		     : this.varName2Value.entrySet()) {

                final String        name = entry.getKey();
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
            LOG.debug(MSG_IOE_UNEXP, e);
            throw new OctaveIOException(MSG_IOE_UNEXP, e);
        }
    }

}
