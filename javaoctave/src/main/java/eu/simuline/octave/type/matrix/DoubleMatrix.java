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
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

/**
 * General matrix with double values. 
 */
// used as superclass of class OctaveDouble and within OctaveComplex only 
public abstract class DoubleMatrix 
    extends AbstractGenericMatrix<double[], DoubleArrayList> {

    /**
     * @param size
     */
    public DoubleMatrix(final int... size) {
        super(size);
    }

    /**
     * Constructor that reuses the input data. 
     * 
     * @param dataA
     * @param size
     */
    public DoubleMatrix(final double[] dataA, final int... size) {
        super(dataA, size);
    }

    /**
     * Copy constructor. 
     * 
     * @param o
     */
    public DoubleMatrix(final DoubleMatrix o) {
        super(o);
    }

    protected final double[] newD(final int size) {
        return new double[size];
    }

    protected final DoubleArrayList​ newL(final int size) {
	DoubleArrayList​ list = new DoubleArrayList​(size);
	list.size(size);
	return list;
    }

    protected final DoubleArrayList​ newL(double[] data, final int size) {
	DoubleArrayList​ list = new DoubleArrayList​(data);
	list.size(size);
	return list;
     }

    public final int dataLength() {
        return this.dataA.length;
    }

    protected final boolean dataEquals(final int usedLength,
				       final double[] otherData) {
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
        this.dataA[pos] = value;
	this.dataL.set(pos, value);
    }

    // api-docs inherited from AbstractGenericMatrix 
    public final void setPlain(final String value, final int pos) {
        this.dataA[pos] = StringUtil.parseDouble(value);
	this.dataL.set(pos, StringUtil.parseDouble(value));
    }

    /**
     * Get the value. 
     * 
     * @param pos
     * @return value at pos
     */
    public final double get(final int... pos) {
	assert (Double.isNaN(this.dataL.get(pos2ind(pos))) && Double.isNaN(this.dataA[pos2ind(pos)]))
	    || this.dataL.get(pos2ind(pos)) == this.dataA[pos2ind(pos)];
        return this.dataA[pos2ind(pos)];
	//this.dataD.get(pos2ind(pos));
    }

    public final String getPlainString(int pos) {
	
	assert (Double.isNaN(this.dataL.get(pos)) && Double.isNaN(this.dataA[pos]))
	    || this.dataL.get(pos) == this.dataA[pos];
	//Double.toString(this.dataD.get(pos));
	return Double.toString(this.dataA[pos]);
    }

}
