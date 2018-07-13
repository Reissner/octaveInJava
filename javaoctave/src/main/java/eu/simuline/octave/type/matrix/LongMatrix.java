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

import it.unimi.dsi.fastutil.longs.LongArrayList;

/**
 * General matrix of <code>int</code> values. 
 */
// used as superclass of class OctaveLong only 
public abstract class LongMatrix 
    extends AbstractGenericMatrix<long[], LongArrayList> {

    /**
     * @param size
     */
    public LongMatrix(final int... size) {
        super(size);
    }

    /**
     * Constructor that reuses the input data. 
     * 
     * @param dataA
     * @param size
     */
    public LongMatrix(final long[] dataA, final int... size) {
        super(dataA, size);
    }

    /**
     * Copy constructor. 
     * 
     * @param o
     */
    public LongMatrix(final LongMatrix o) {
        super(o);
    }

    protected final LongArrayList​ newL(final int size) {
	LongArrayList​ list = new LongArrayList​(size);
	list.size(size);
	return list;
    }

    protected final int initL(long[] data, final int size) {
	this.dataL = new LongArrayList​(data);
	this.dataL.size(size);
	return data.length;
    }

    protected long[] getDataA() {
	return this.dataL.elements();
    }

    /**
     * Set the value resizing by need. 
     * 
     * @param value
     * @param pos
     * @see #setPlain(long, int)
     */
    public final void set(final long value, final int... pos) {
        resizeUp(pos);
        setPlain(value, pos2ind(pos));
    }

    /**
     * Set the value assuming resize is not necessary. 
     * 
     * @param value
     * @param pos
     * @see #set(long, int[])
     */
    public final void setPlain(final long value, final int pos) {
	this.dataL.set(pos, value);
    }

    // api-docs inherited from AbstractGenericMatrix 
    public final void setPlain(final String value, final int pos) {
	this.dataL.set(pos, Long.parseLong(value.trim()));
    }

    /**
     * Get the value. 
     * 
     * @param pos
     * @return value at pos
     */
    public final long get(final int... pos) {
	return this.dataL.getLong(pos2ind(pos));
    }

    // api-docs inherited from AbstractGenericMatrix 
    public final String getPlainString(int pos) {
	return Long.toString(this.dataL.getLong(pos));
    }
}
