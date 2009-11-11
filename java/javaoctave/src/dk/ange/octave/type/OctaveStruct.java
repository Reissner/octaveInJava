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
/**
 * @author Kim Hansen
 * @author Esben Mose Hansen
 */
package dk.ange.octave.type;

import java.util.HashMap;
import java.util.Map;

import dk.ange.octave.exception.OctaveClassCastException;

/**
 * 1x1 struct
 */
public class OctaveStruct implements OctaveObject {

    private final Map<String, OctaveObject> data;

    /**
     * Create empty struct
     */
    public OctaveStruct() {
        data = new HashMap<String, OctaveObject>();
    }

    /**
     * Create struct from data
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
    public void set(final String name, final OctaveObject value) {
        if (value == null) {
            throw new NullPointerException("Can not set field to null");
        }
        data.put(name, value);
    }

    /**
     * @param <T>
     * @param key
     * @return (shallow copy of) value for this key, or null if key isn't there.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final String key) {
        final OctaveObject ot = data.get(key).shallowCopy();
        try {
            return (T) ot;
        } catch (final ClassCastException e) {
            throw new OctaveClassCastException(e, ot);
        }
    }

    /**
     * @return reference to internal map
     */
    public Map<String, OctaveObject> getData() {
        return data;
    }

    @Override
    public OctaveStruct shallowCopy() {
        return new OctaveStruct(data);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
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
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        return true;
    }

}
