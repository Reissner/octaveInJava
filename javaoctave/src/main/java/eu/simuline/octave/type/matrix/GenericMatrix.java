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

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.Objects;

/**
 * General matrix with Object values 
 * which serves also as base class for 
 * {@link eu.simuline.octave.type.OctaveCell}. 
 * 
 * @param <T>
 *    Type of the values
 */
// used as superclass of class OctaveCell only 
public abstract class GenericMatrix<T> 
    extends AbstractGenericMatrix<T[], ObjectArrayList<T>> {

    /**
     * @param size
     */
    // used in OctaveCell(int...), 
    protected GenericMatrix(final int... size) {
        super(size);
    }

    /**
     * @param dataA
     * @param size
     */
    // used by end users 
    @SuppressWarnings("unchecked")
    protected GenericMatrix(final Object[] dataA, final int... size) {
        super((T[]) dataA, size);
    }

    /**
     * Copy constructor. 
     * 
     * @param o
     */
    // used in OctaveCell(final AbstractGenericMatrix), 
    protected GenericMatrix(final AbstractGenericMatrix<T[], ObjectArrayList<T>> o) {
        super(o);
    }

    @SuppressWarnings("unchecked")
    // **** is this a bug (ClassCastException)??
    protected final T[] newD(final int size) {
        return (T[]) new Object[size];
    }

    protected final ObjectArrayList<T> newL(final int size) {
        ObjectArrayList<T> list = new ObjectArrayList<T>(size);
	list.size(size);
	return list;
    }

    protected final int newL(T[] data, final int size) {
	this.dataL = new ObjectArrayList<T>(data);
	this.dataL.size(size);
	return data.length;
     }

    public final int dataLength() {
        return this.dataA.length;
    }

    protected final boolean dataEquals(final int usedLength,
				       final T[] otherData) {
        for (int i = 0; i < usedLength; i++) {
            final T o1 = this.dataA[i];
            final T o2 = otherData [i];
	    assert (o1 == null) == (this.dataL.get(i) == null);
	    assert (o1 == null) || o1.equals(this.dataL.get(i));
            if (!(o1 == null ? o2 == null : o1.equals(o2))) {
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
     * @see #setPlain(Object, int)
     */
    // overwritten in OctaveCell 
    // questionable: where is value==null needed? 
    // maybe this can be excluded right here. 
    // It is not worth fixing now because possibly redesign required. 
    @SuppressWarnings("checkstyle:designforextension")
    public void set(final T value, final int... pos) {
        resizeUp(pos);
        setPlain(value, pos2ind(pos));
    }

    /**
     * Set the value assuming resize is not necessary. 
     * 
     * @param value
     * @param pos
     * @see #set(Object, int[])
     */
    public final void setPlain(final T value, final int pos) {
         this.dataA[pos] = value;
	 this.dataL.set(pos, value);
    }

    // api-docs inherited from AbstractGenericMatrix 
    public final void setPlain(final String value, final int pos) {
	throw new UnsupportedOperationException();
    }

    /**
     * Get the value. 
     * 
     * @param pos
     * @return value at pos
     */
    // overwritten in OctaveCell 
    // see set(...)
    @SuppressWarnings("checkstyle:designforextension")
    public T get(final int... pos) {
	assert Objects.equals(this.dataA[pos2ind(pos)], 
			      this.dataL.get(pos2ind(pos)));
        //return this.dataA[pos2ind(pos)];
	return this.dataL.get(pos2ind(pos));
    }

    // **** may dataL be null??  
    public final String getPlainString(int pos) {
	StringUtil.toString(this.dataL.get(pos));
	assert      StringUtil.toString(this.dataL.get(pos))
	    .equals(StringUtil.toString(this.dataA[pos]));
	return StringUtil.toString(this.dataL.get(pos));
	//return this.dataA[pos].toString();
	//return StringUtil.toString(this.dataA[pos]);
    }
}
