/*
 * Copyright 2008 Ange Optimization ApS
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
/**
 * @author Kim Hansen
 */
package eu.simuline.octave.io.spi;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.spi.ServiceRegistry;

import eu.simuline.octave.type.OctaveObject;

/**
 * Service Provider Interface for the IO handler 
 * that can read {@link OctaveObject}s. 
 * The octave type which can be read is given by {@link #octaveType()} 
 * whereas {@link #read(BufferedReader)} performs reading. 
 * <p>
 * The according implementations 
 * are in package {@link eu.simuline.octave.io.impl} 
 * and extend this class. 
 * These classes are registered in the jar-file 
 * under <code>META-INF/services/eu.simuline.octave.io.OctaveDataReader</code>. 
 */
public abstract class OctaveDataReader {

    /**
     * Maps the {@link #octaveType()} 
     * of an {@link OctaveDataReader} to the {@link OctaveDataReader} itself 
     * which is able to read the octave type from a reader. 
     */
    private static Map<String, OctaveDataReader> readers = null;

    /**
     * @param type
     * @return The OctaveDataReader or null if it does not exist
     */
    public static OctaveDataReader getOctaveDataReader(final String type) {
        initIfNecessary();
        return readers.get(type);
    }

    private static synchronized void initIfNecessary() {
        if (readers != null) {
            return;
        }
	readers = new HashMap<String, OctaveDataReader>();
	final Iterator<OctaveDataReader> sp = 
	    ServiceRegistry.lookupProviders(OctaveDataReader.class);
	while (sp.hasNext()) {
	    final OctaveDataReader odr = sp.next();
	    readers.put(odr.octaveType(), odr);
	}
    }

    /**
     * Could be "scalar" or "string" or something else. 
     *
     * @return
     *    the string representation of the octave type 
     *    read by this {@link OctaveDataReader} 
     */
    public abstract String octaveType();

    /**
     * Reads an {@link OctaveObject} from a Reader <code>reader</code>. 
     * @param reader
     *    the Reader to read from, will not close reader
     * @return
     *   the object read from <code>reader</code>. 
     */
    public abstract OctaveObject read(BufferedReader reader);

}
