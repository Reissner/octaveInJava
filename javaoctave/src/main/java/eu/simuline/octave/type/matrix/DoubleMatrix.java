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

import java.util.Arrays;

/**
 * General matrix with double values. 
 */
public class DoubleMatrix extends AbstractGenericMatrix<double[]> {

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

    protected final int dataLength() {
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
     * Set the value. 
     * 
     * @param value
     * @param pos
     */
    public final void set(final double value, final int... pos) {
        resizeUp(pos);
        this.data[pos2ind(pos)] = value;
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

}
