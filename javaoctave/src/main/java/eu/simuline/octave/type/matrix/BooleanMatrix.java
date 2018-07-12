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
package eu.simuline.octave.type.matrix;

import eu.simuline.octave.util.StringUtil;

import it.unimi.dsi.fastutil.booleans.BooleanArrayList;

/**
 * General matrix with boolean values. 
 */
public abstract class BooleanMatrix 
    extends AbstractGenericMatrix<boolean[], BooleanArrayList> {

    /**
     * @param size
     */
    // used in OctaveBoolean(int...) which in turn is used in readers 
    protected BooleanMatrix(final int... size) {
        super(size);
    }

    /**
     * @param dataA
     * @param size
     */
    protected BooleanMatrix(final boolean[] dataA, final int... size) {
        super(dataA, size);
    }

    protected final boolean[] newD(final int size) {
        return new boolean[size];
    }

    protected final BooleanArrayList​ newL(final int size) {
	BooleanArrayList​ list = new BooleanArrayList​(size);
	list.size(size);
	return list;
    }

    protected final int newL(boolean[] data, final int size) {
	this.dataL = new BooleanArrayList​(data);
	this.dataL.size(size);
	return data.length;
    }

    public final int dataLength() {
	//assert this.dataA.length == this.dataL.elements().length;
        return this.dataA.length;
    }

    protected final boolean dataEquals(final int usedLength,
				       final boolean[] otherData) {
        for (int i = 0; i < usedLength; i++) {
 	    assert this.dataA[i] == this.dataL.get(i);
	    if (this.dataA[i] != otherData[i]) {
                return false;
            }
        }
        return true;
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
        this.dataA[pos] = value;
	this.dataL.set(pos, value);
    }

    // api-docs inherited from AbstractGenericMatrix 
    public final void setPlain(final String value, final int pos) {
	this.dataA[pos] = StringUtil.parseBoolean(value);
	this.dataL.set(pos, StringUtil.parseBoolean(value));
    }

    /**
     * Get the value. 
     * 
     * @param pos
     * @return value at pos
     */
    public final boolean get(final int... pos) {
	assert this.dataL.getBoolean(pos2ind(pos)) == this.dataA[pos2ind(pos)];
        //return this.dataA[pos2ind(pos)];
	return this.dataL.getBoolean(pos2ind(pos));
    }

    public final String getPlainString(int pos) {
	assert this.dataL.getBoolean(pos) == this.dataA[pos];
	return StringUtil.toString(this.dataL.getBoolean(pos));
//	return StringUtil.toString(this.dataA[pos]);
    }


}
