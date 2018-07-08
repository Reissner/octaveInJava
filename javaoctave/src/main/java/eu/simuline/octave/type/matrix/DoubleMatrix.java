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

/**
 * General matrix with double values. 
 */
// used as superclass of class OctaveDouble and within OctaveComplex only 
public abstract class DoubleMatrix extends AbstractGenericMatrix<double[]> {

    /**
     * @param size
     */
    public DoubleMatrix(final int... size) {
        super(size);
    }

    /**
     * Constructor that reuses the input data. 
     * 
     * @param data
     * @param size
     */
    public DoubleMatrix(final double[] data, final int... size) {
        super(data, size);
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

    public final int dataLength() {
        return this.data.length;
    }

    protected final boolean dataEquals(final int usedLength,
				       final double[] otherData) {
        for (int i = 0; i < usedLength; i++) {
            if (this.data[i] != otherData[i]) {
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
        this.data[pos] = value;
    }

    // api-docs inherited from AbstractGenericMatrix 
    public final void setPlain(final String value, final int pos) {
        this.data[pos] = StringUtil.parseDouble(value);
    }

    /**
     * Get the value. 
     * 
     * @param pos
     * @return value at pos
     */
    public final double get(final int... pos) {
        return this.data[pos2ind(pos)];
    }

    public final String getPlainString(int pos) {
	return Double.toString(this.data[pos]);
    }

}
