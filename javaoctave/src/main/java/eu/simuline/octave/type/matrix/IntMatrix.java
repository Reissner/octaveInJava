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
package eu.simuline.octave.type.matrix;

/**
 * General matrix of <code>int</code> values. 
 */
// used as superclass of class OctaveInt only 
public abstract class IntMatrix extends AbstractGenericMatrix<int[]> {

    /**
     * @param size
     */
    public IntMatrix(final int... size) {
        super(size);
    }

    /**
     * Constructor that reuses the input data. 
     * 
     * @param data
     * @param size
     */
    public IntMatrix(final int[] data, final int... size) {
        super(data, size);
    }

    // superfluous? 
    /**
     * Copy constructor. 
     * 
     * @param o
     */
    public IntMatrix(final IntMatrix o) {
        super(o);
    }

    protected final int[] newD(final int size) {
        return new int[size];
    }

    public final int dataLength() {
        return this.data.length;
    }

    protected final boolean dataEquals(final int usedLength,
				       final int[] otherData) {
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
        this.data[pos] = value;
    }

    // api-docs inherited from AbstractGenericMatrix 
    public final void setPlain(final String value, final int pos) {
	this.data[pos] = Integer.parseInt(value.trim());
    }

    /**
     * Get the value. 
     * 
     * @param pos
     * @return value at pos
     */
    public final int get(final int... pos) {
        return this.data[pos2ind(pos)];
    }

    public String getPlainString(int pos) {
	return Integer.toString(this.data[pos]);
    }


}
