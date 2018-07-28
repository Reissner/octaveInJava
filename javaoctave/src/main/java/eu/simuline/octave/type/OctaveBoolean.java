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

import it.unimi.dsi.fastutil.booleans.BooleanArrayList;

/**
 * Represents a Boolean matrix. 
 * Not so appropriate for sparse matrices. 
 *
 * @see OctaveSparseBoolean
 */
public final class OctaveBoolean 
    extends AbstractGenericMatrix<boolean[], BooleanArrayList> {

    /**
     * @param size
     */
    // Used in BooleanReader, BooleanSingleReader
    public OctaveBoolean(final int... size) {
        super(size);
    }

    /**
     * @param data
     * @param size
     */
    // used in shallowCopy(), used by end user 
    public OctaveBoolean(final boolean[] data, final int... size) {
        super(data, size);
    }

    private OctaveBoolean(OctaveBoolean o) {
        super(o);
    }

    protected final BooleanArrayList newL(final int size) {
	BooleanArrayList list = new BooleanArrayList(size);
	list.size(size);
	return list;
    }

    protected final int initL(boolean[] data, final int size) {
    	this.dataL = new BooleanArrayList(data);
    	this.dataL.size(size);
    	return data.length;
    }

    protected boolean[] getDataA() {
	return this.dataL.elements();
    }

    /**
     * Set the value resizing by need. 
     * 
     * @param value
     * @param pos
     * @see #setPlain(boolean, int)
     */
    public final void set(final boolean value, final int... pos) {
        resizeUp(pos);
        setPlain(value, pos2ind(pos));
    }

    /**
     * Set the value assuming resize is not necessary. 
     * 
     * @param value
     * @param pos
     * @see #set(boolean, int[])
     */
    public final void setPlain(final boolean value, final int pos) {
	this.dataL.set(pos, value);
    }

    // api-docs inherited from AbstractGenericMatrix 
    public final void setPlain(final String value, final int pos) {
	this.dataL.set(pos, StringUtil.parseBoolean(value));
    }

    /**
     * Get the value. 
     * 
     * @param pos
     * @return value at pos
     */
    public final boolean get(final int... pos) {
 	return this.dataL.getBoolean(pos2ind(pos));
    }

    public final String getPlainString(int pos) {
	return StringUtil.toString(this.dataL.getBoolean(pos));
    }

    // api-docs inherited from OctaveObject
    @Override
    public OctaveBoolean shallowCopy() {
        return new OctaveBoolean(this);
    }

}
