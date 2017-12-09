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
 * Represents a complex matrix. 
 */
public final class OctaveComplex implements OctaveObject {

    private final DoubleMatrix real;

    private final DoubleMatrix imag;

    /**
     * @param size
     */
    public OctaveComplex(final int... size) {
        this.real = new DoubleMatrix(size);
        this.imag = new DoubleMatrix(size);
    }

    /**
     * Copy constructor. 
     * 
     * @param o
     */
    public OctaveComplex(final OctaveComplex o) {
        this.real = new DoubleMatrix(o.real);
        this.imag = new DoubleMatrix(o.imag);
    }

    /**
     * Reuse input data. 
     * 
     * @param realData
     * @param size
     */
    public OctaveComplex(final double[] realData, final int... size) {
        this.real = new DoubleMatrix(realData, size);
        this.imag = new DoubleMatrix(size);
    }

    public OctaveComplex(OctaveDouble r) {
	this.real = new DoubleMatrix(r);
	this.imag = r.zero();
    }

    /**
     * @param i
     *            dimension number in 1 based numbering, 1=row, 2=column
     * @return the size in dimension i
     */
    public int size(final int i) {
        return this.real.size(i);
    }

    /**
     * @param pos
     * @return the index into getReal() and getImag() for the position
     * @see dk.ange.octave.type.matrix.AbstractGenericMatrix#pos2ind(int[])
     */
    public int pos2ind(final int... pos) {
        return this.real.pos2ind(pos);
    }

    /**
     * @param value
     * @param pos
     */
    public void setReal(final double value, final int... pos) {
        this.real.set(value, pos);
        this.imag.resizeUp(pos);
    }

    /**
     * @param pos
     * @return the real value stored at pos
     */
    public double getReal(final int... pos) {
        return this.real.get(pos);
    }

    /**
     * @return the array of real value stored in the matrix
     */
    public double[] getReal() {
        return this.real.getData();
    }

    /**
     * @param value
     * @param pos
     */
    public void setImag(final double value, final int... pos) {
        this.real.resizeUp(pos);
        this.imag.set(value, pos);
    }

    /**
     * @param pos
     * @return the imaginary value stored at pos
     */
    public double getImag(final int... pos) {
        return this.imag.get(pos);
    }

    /**
     * @return the array of imaginary value stored in the matrix
     */
    public double[] getImag() {
        return imag.getData();
    }

    @Override
    public OctaveComplex shallowCopy() {
        return new OctaveComplex(this);
    }

}
