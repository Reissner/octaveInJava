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

import eu.simuline.octave.type.matrix.AbstractGenericMatrix;

import eu.simuline.octave.util.StringUtil;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

/**
 * Represents a matrix of doubles. 
 */
public final class OctaveDouble 
    extends AbstractGenericMatrix<double[], DoubleArrayList> {

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
    protected OctaveDouble(final OctaveDouble o) {
        super(o);
    }

    protected final DoubleArrayList newL(final int size) {
	DoubleArrayList list = new DoubleArrayList(size);
	list.size(size);
	return list;
    }

    protected final int initL(double[] data, final int size) {
	this.dataL = new DoubleArrayList(data);
	this.dataL.size(size);
	return data.length;
    }

    protected double[] getDataA() {
	return this.dataL.elements();
    }


    /**
     * Set the value resizing by need. 
     * 
     * @param value
     * @param pos
     * @see #setPlain(double, int)
     */
    public final void set(final double value, final int... pos) {
        resizeUp(pos);
        setPlain(value, pos2ind(pos));
    }

    /**
     * Set the value assuming resize is not necessary. 
     * 
     * @param value
     * @param pos
     * @see #set(double, int[])
     */
    public final void setPlain(final double value, final int pos) {
	this.dataL.set(pos, value);
    }

    // api-docs inherited from AbstractGenericMatrix 
    public final void setPlain(final String value, final int pos) {
	this.dataL.set(pos, StringUtil.parseDouble(value));
    }

    /**
     * Get the value. 
     * 
     * @param pos
     * @return value at pos
     */
    public final double get(final int... pos) {
	return this.dataL.getDouble(pos2ind(pos));
    }

    public final String getPlainString(int pos) {
	return Double.toString(this.dataL.getDouble(pos));
    }

    // **** needed at least for OctaveComplex, 
    public OctaveDouble zero() {
	return new OctaveDouble(this.size);
    }

    // api-docs inherited from OctaveObject
    @Override
    public OctaveDouble shallowCopy() {
        return new OctaveDouble(this);
    }

}
