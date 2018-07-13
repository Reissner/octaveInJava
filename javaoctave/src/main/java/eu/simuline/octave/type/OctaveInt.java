/*
 * Copyright 2010 Ange Optimization ApS
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

import eu.simuline.octave.type.matrix.IntMatrix;

/**
 * Represents a matrix of ints. 
 */
public final class OctaveInt extends IntMatrix implements OctaveObject {

    /**
     * Create new matrix. 
     * 
     * @param size
     */
    // used by reader 
    public OctaveInt(final int... size) {
        super(size);
    }

    /**
     * Constructor that reuses the input data. 
     * 
     * @param data
     * @param size
     */
    // used by shallowCopy, end user, tests 
    public OctaveInt(final int[] data, final int... size) {
        super(data, size);
    }

    // superfluous? 
    /**
     * Copy constructor. 
     * 
     * @param o
     */
    public OctaveInt(final OctaveInt o) {
        super(o);
    }

    @Override
    public OctaveInt shallowCopy() {
        return new OctaveInt(this);
    }

    public void main(String[] args) {

    }

}
