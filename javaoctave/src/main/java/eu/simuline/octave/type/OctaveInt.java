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

import eu.simuline.octave.type.matrix.AbstractGenericMatrix;

import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * Represents a matrix of ints. 
 */
public final class OctaveInt 
    extends AbstractGenericMatrix<int[], IntArrayList> {

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

    /**
     * Copy constructor. 
     * 
     * @param o
     */
    protected OctaveInt(final OctaveInt o) {
        super(o);
    }

    protected final IntArrayList newL(final int size) {
	IntArrayList list = new IntArrayList(size);
	list.size(size);
	return list;
    }

    protected final int initL(int[] data, final int size) {
	this.dataL = new IntArrayList(data);
	this.dataL.size(size);
	return data.length;
    }

    protected int[] getDataA() {
	return this.dataL.elements();
    }

    /**
     * Set the value resizing by need. 
     * 
     * @param value
     * @param pos
     * @see #setPlain(int, int)
     */
    public final void set(final int value, final int... pos) {
        resizeUp(pos);
        setPlain(value, pos2ind(pos));
    }

    /**
     * Set the value assuming resize is not necessary. 
     * 
     * @param value
     * @param pos
     * @see #set(int, int[])
     */
    public final void setPlain(final int value, final int pos) {
	this.dataL.set(pos, value);
    }

    // api-docs inherited from AbstractGenericMatrix 
    public final void setPlain(final String value, final int pos) {
	this.dataL.set(pos, Integer.parseInt(value.trim()));
    }

    /**
     * Get the value. 
     * 
     * @param pos
     * @return value at pos
     */
    public final int get(final int... pos) {
	return this.dataL.getInt(pos2ind(pos));
    }

    public final String getPlainString(int pos) {
	return Integer.toString(this.dataL.getInt(pos));
    }

    // api-docs inherited from OctaveObject
    @Override
    public OctaveInt shallowCopy() {
        return new OctaveInt(this);
    }

}
