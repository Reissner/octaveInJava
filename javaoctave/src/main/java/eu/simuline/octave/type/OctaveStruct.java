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
package eu.simuline.octave.type;

import java.util.Map;
import java.util.TreeMap;

import eu.simuline.octave.exception.OctaveClassCastException;
import eu.simuline.octave.type.cast.Cast;

/**
 * 1x1 struct. JavaOctave does not support the multidimensional structs 
 * that octave has.
 */
public final class OctaveStruct implements OctaveObject {


    private static final int PRIME = 31;

    // **** ER: I really have doubts that data can be null. 
    private final Map<String, OctaveObject> data;

    /**
     * Create empty struct. 
     */
    public OctaveStruct() {
	// Use a TreeMap in order to get a stable serialization
	// (I might want to use HashMap here and sort in OctaveIO.toText())
        this.data = new TreeMap<String, OctaveObject>();
    }

    /**
     * Create struct from data. 
     * 
     * @param data
     *            this data will be referenced, not copied
     */
    public OctaveStruct(final Map<String, OctaveObject> data) {
        this.data = data;
    }

    /**
     * @param name
     * @param value
     */
    @SuppressWarnings("PMD.AvoidThrowingNullPointerException")
    public void set(final String name, final OctaveObject value) {
        if (value == null) {
            throw new NullPointerException
		("Cannot set field with null-name in octave struct. ");
        }
        this.data.put(name, value);
    }

    /**
     * Get object from struct as plain OctaveObject. 
     * If you want to cast the object to a special type use 
     * {@link OctaveStruct#get(Class, String)}.
     * 
     * @param key
     * @return
     *    shallow copy of value for this key, or null if key isn't there.
     */
    public OctaveObject get(final String key) {
        final OctaveObject value = this.data.get(key);
	return (value == null) ? null : value.shallowCopy();
    }

    /**
     * @param castClass
     *            Class to cast to
     * @param key
     * @param <T>
     * @return shallow copy of value for this key, or null if key isn't there.
     * @throws OctaveClassCastException
     *             if the object can not be cast to a castClass
     */
    public <T extends OctaveObject> T get(final Class<T> castClass, 
					  final String key) {
        return Cast.cast(castClass, get(key));
    }

    /**
     * @return reference to internal map
     */
    public Map<String, OctaveObject> getData() {
        return this.data;
    }

    @Override
    public OctaveStruct shallowCopy() {
        return new OctaveStruct(this.data);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = PRIME * result + ((this.data == null) ? 0 : data.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OctaveStruct other = (OctaveStruct) obj;
        if (this.data == null) {
	    return other.data == null;
            // if (other.data != null) {
            //     return false;
            // }
        } else {
	    return this.data.equals(other.data);
	    // if (!this.data.equals(other.data)) {
	    // 	return false;
	    // }
	}
        // return true;
    }

}
