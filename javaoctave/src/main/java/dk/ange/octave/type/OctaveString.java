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
 * @author kim
 */
package dk.ange.octave.type;

/**
 * Represents an octave string. 
 */
public final class OctaveString implements OctaveObject {

    private static final int PRIME = 31;

    private String value;

    /**
     * @param string
     */
    public OctaveString(final String string) {
        this.value = string;
    }

    /**
     * @return the string
     */
    public String getString() {
        return value;
    }

    /**
     * @param string
     *            the string to set
     */
    public void setString(final String string) {
        this.value = string;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
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
        final OctaveString other = (OctaveString) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public OctaveString shallowCopy() {
        return new OctaveString(value);
    }

    @Override
    public String toString() {
        return "OctaveString[" + value + "]";
    }

}
