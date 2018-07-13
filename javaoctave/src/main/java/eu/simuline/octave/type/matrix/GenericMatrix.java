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

    // api-docs inherited from base class 
    protected final ObjectArrayList<T> newL(final int size) {
        ObjectArrayList<T> list = new ObjectArrayList<T>(size);
	list.size(size);
	return list;
    }

    // api-docs inherited from base class 
    protected final int initL(T[] data, final int size) {
	this.dataL = new ObjectArrayList<T>(data);
	this.dataL.size(size);
	return data.length;
    }

    // api-docs inherited from base class 
    protected T[] getDataA() {
	return this.dataL.elements();
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
	return this.dataL.get(pos2ind(pos));
    }

    // **** may dataL be null??  
    public final String getPlainString(int pos) {
	return StringUtil.toString(this.dataL.get(pos));
    }
}
