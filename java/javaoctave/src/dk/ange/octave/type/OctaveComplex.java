/*
 * Copyright 2009 Ange Optimization ApS
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
package dk.ange.octave.type;

import dk.ange.octave.type.matrix.DoubleMatrix;

/**
 * A complex matrix
 */
public class OctaveComplex implements OctaveObject {

    private final DoubleMatrix real;

    private final DoubleMatrix imag;

    /**
     * @param size
     */
    public OctaveComplex(final int... size) {
        real = new DoubleMatrix(size);
        imag = new DoubleMatrix(size);
    }

    /**
     * Copy constructor
     * 
     * @param o
     */
    public OctaveComplex(final OctaveComplex o) {
        real = new DoubleMatrix(o.real);
        imag = new DoubleMatrix(o.imag);
    }

    /**
     * Reuse input data
     * 
     * @param realData
     * @param size
     */
    public OctaveComplex(final double[] realData, final int... size) {
        real = new DoubleMatrix(realData, size);
        imag = new DoubleMatrix(size);
    }

    /**
     * @param value
     * @param pos
     */
    public void setReal(final double value, final int... pos) {
        real.set(value, pos);
        imag.resizeUp(pos);
    }

    /**
     * @param pos
     * @return the real value stored at pos
     */
    public double getReal(final int... pos) {
        return real.get(pos);
    }

    /**
     * @param value
     * @param pos
     */
    public void setImag(final double value, final int... pos) {
        real.resizeUp(pos);
        imag.set(value, pos);
    }

    /**
     * @param pos
     * @return the imaginary value stored at pos
     */
    public double getImag(final int... pos) {
        return imag.get(pos);
    }

    @Override
    public OctaveComplex shallowCopy() {
        return new OctaveComplex(this);
    }

}
