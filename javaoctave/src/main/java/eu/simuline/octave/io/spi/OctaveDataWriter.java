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
package eu.simuline.octave.io.spi;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.spi.ServiceRegistry;

import eu.simuline.octave.type.OctaveObject;

/**
 * Service Provider Interface for the IO handler 
 * that can write {@link OctaveObject}s. 
 * The according implementations 
 * are in package {@link eu.simuline.octave.io.impl} 
 * and extend this class. 
 * These classes are registered in the jar-file 
 * under <code>META-INF/services/eu.simuline.octave.io.OctaveDataWriter</code>. 
 *
 * @param <T>
 *    the type extending {@link OctaveObject} this writer can write. 
 */
public abstract class OctaveDataWriter<T extends OctaveObject> {

    /**
     * Maps the {@link #javaType()} 
     * which represents an octave type 
     * of an {@link OctaveDataWriter} to the {@link OctaveDataWriter} itself 
     * which is able to write the octave type to a writer. 
     */
    private static 
	Map<Class<? extends OctaveObject>, OctaveDataWriter<?>> wRITERS;

    /**
     * @param <T>
     * @param type
     * @return The OctaveDataWriter or null if it does not exist
     */
    @SuppressWarnings("unchecked")
    public static <T extends OctaveObject> 
	OctaveDataWriter<T> getOctaveDataWriter(final T type) {
        initWriterIfNecessary();
        return (OctaveDataWriter<T>) wRITERS.get(type.getClass());
    }

    //synchronized
    private static synchronized void initWriterIfNecessary() {
	if (wRITERS != null) {
	    return;
	}
	wRITERS = new HashMap
	    <Class<? extends OctaveObject>, OctaveDataWriter<?>>();
	@SuppressWarnings("rawtypes")
	final Iterator<OctaveDataWriter> sp = 
	    ServiceRegistry.lookupProviders(OctaveDataWriter.class);
	OctaveDataWriter<?> odw, odwOrg;
	while (sp.hasNext()) {
	    odw = sp.next();
	    assert odw != null;
	    odwOrg = wRITERS.put(odw.javaType(), odw);
	    if (odwOrg != null) {
		// Here, for one type more than one writer is defined. 
		throw new IllegalStateException
		    ("Java type " + odw.javaType() + 
		     " has writers of type " + odw.getClass() + 
		     " and " + odwOrg.getClass() + ". ");
	    }
	}
    }

    /**
     * Could be OctaveScalar or OctaveMatrix. 
     *
     * @return the {@link Class} of the {@link OctaveObject} 
     *    that this IO handler loads and saves
     */
    public abstract Class<T> javaType();

    /**
     * @param writer
     *            the Writer to write to
     * @param octaveType
     *            the value to write
     * @throws IOException
     */
    public abstract void write(Writer writer, T octaveType) throws IOException;

}
