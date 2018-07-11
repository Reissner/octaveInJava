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

import eu.simuline.octave.type.matrix.DoubleMatrix;

/**
 * Represents a matrix of doubles. 
 */
public final class OctaveDouble extends DoubleMatrix implements OctaveObject {

    /**
     * Create new matrix. 
     * 
     * @param size
     */
    // used by OctaveComplex, Octave, OctaveDouble, OctaveCell, 
    // Reader and Writer
    public OctaveDouble(final int... size) {
        super(size);
    }

    /**
     * Constructor that reuses the input data. 
     * 
     * @param data
     * @param size
     */
    // used by the end user only 
    public OctaveDouble(final double[] data, final int... size) {
        super(data, size);
    }

    /**
     * Copy constructor. 
     * 
     * @param o
     */
    // used by OctaveComplex, OctaveDouble
    public OctaveDouble(final OctaveDouble o) {
        super(o);
    }

    // superfluous? 
    public OctaveDouble zero() {
	return new OctaveDouble(this.size);
    }

    @Override
    public OctaveDouble shallowCopy() {
        return new OctaveDouble(this);
    }

}
