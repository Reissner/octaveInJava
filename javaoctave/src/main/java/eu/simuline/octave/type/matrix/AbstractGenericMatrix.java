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

import eu.simuline.octave.type.OctaveObject;

import java.util.Arrays;

/**
 * A general matrix that does not even know 
 * that it is an array it stores its  in.
 * 
 * @param <D>
 *            an array
 */
// used as superclass of classes of this package only 
public abstract class AbstractGenericMatrix<D> implements OctaveObject { //, E

    private static final int PRIME = 31;

    /**
     * The dimensions, rows x columns x depth x ....
     */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final int[] size;

    /**
     * The data, vectorized.
     */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected D data;
    // protected D dataA;
    // protected final List<E> dataL;

    /**
     * Constructor that creates new blank matrix. 
     * 
     * @param size
     */
    protected AbstractGenericMatrix(final int... size) {
        this.size = size.clone();
        checkSize();
        this.data = newD(product(size));
        // this.dataA = newD(product(size));
        // this.dataL = newL(product(size));
	// assert this.dataA != null;
	// assert this.dataL != null;
	// assert dataLength() == product(size);
    }

    /**
     * Constructor that reuses data in the new object. 
     * 
     * @param data
     * @param size
     *    must have at least two dimensions 
     */
    protected AbstractGenericMatrix(D data, int... size) { //List<E> dataL, 
        this.size = size;
        checkSize();
        this.data = data;
	// assert this.data != null;
	// assert dataLength() == product(size);
	// this.dataL = dataL;
	// assert this.dataL != null;
        checkDataSize();
    }

    /**
     * Copy constructor. 
     * 
     * @param o
     */
    protected AbstractGenericMatrix(final AbstractGenericMatrix<D> o) { //, E
        this.size = o.size.clone();
        this.data = newD(product(size));
//	assert this.data != null;
        System.arraycopy(o.data, 0, data, 0, product(size));
//	assert dataLength() == product(this.size);
//	this.dataL = o.dataL.clone();
//	this.dataL = new ArrayList<E>(o.dataL);
    }

    /**
     * Checks the field {@link #size}: dimension at lest two 
     * and each entry non-negative. 
     */
    private void checkSize() throws IllegalArgumentException {
	switch (this.size.length) {
	case 0:
	    throw new IllegalArgumentException("no size");
	case 1:
	    throw new IllegalArgumentException
		("size must have a least 2 dimensions");
	default:
	    for (final int s : this.size) {
		if (s < 0) {
		    throw new IllegalArgumentException
			("element in size less than zero. =" + s);
		}
	    }
	}
    }

    private void checkDataSize() {
        if (product(this.size) > dataLength()) {
            final StringBuilder text = new StringBuilder();
            text.append("length of data(");
            text.append(dataLength());
            text.append(") is smaller than size([");
            boolean first = true;
            for (final int i : this.size) {
                if (first) {
                    first = false;
                } else {
                    text.append(", ");
                }
                text.append(i);
            }
            text.append("])");
            throw new IllegalArgumentException(text.toString());
        }
    }

    /**
     * Returns a new data store with given size 
     * and entries carrying the default value. 
     * The latter depends on the types: false for boolean, 
     * 0 for int, 0.0 for double and null for GenericMatrix's. 
     * 
     * @param size
     * @return new D[size]
     */
    // used in constructors and resizeUp only 
    protected abstract D newD(int size);

    // protected abstract List<E> newL(int size);

    /**
     * @return data_.length
     */
    // used in checkDataSize and in MatrixReader only 
    public abstract int dataLength();

    /**
     * The number of data entries, which is at most {@link #dataLength()}. 
     * Note that it is heavy load to compute this at the moment. 
     */
    public final int dataSize() {
	return product(this.size);
    }

    /**
     * Returns whether data of this and <code>otherData</code> 
     * are equal in the first <code>usedLength</code> entries. 
     * Here, two null-values are considered equal. 
     *
     * @param usedLength
     * @param otherData
     * @return
     *    whether the used part of data is equal to otherData
     */
    // used in equals only 
    protected abstract boolean dataEquals(int usedLength, D otherData);

    /**
     * Sets the entry with plain position <code>pos</code> 
     * to value parsing the string <code>value</code>. 
     * Note that this base class cannot provide setter methods 
     * for java's primitive data types. 
     *
     * @param value
     *    
     * @param pos
     *    see e.g. {@link GenericMatrix#setPlain(String, int)} 
     *    and {@link DoubleMatrix#setPlain(String, int)} 
     */
    // throws UnsupportedOperationException for GenericMatrix by default 
    // design ok? **** 
    public abstract void setPlain(String value, int pos);

    /**
     * @param ns
     * @return product of ns
     */
    private static int product(final int... ns) {
        int p = 1;
        for (final int n : ns) {
	    // **** here a check against overflow is required. 
            p *= n;
        }
        return p;
    }

    /**
     * Resize matrix up to include pos if necessary, 
     * i.e. if an entry of <code>pos</code> is greater 
     * than the according entry in {@link #size}. 
     * 
     * @param pos
     *    an index vector with same dimension as {@link #size} 
     * @throws UnsupportedOperationException
     *   if <code>pos</code> has dimension other than that of {@link #size}. 
     */
    public final void resizeUp(final int... pos) {
        if (this.size.length != pos.length) {
            throw new UnsupportedOperationException
		("Change in number of dimensions not supported (" + size.length
		 + "!=" + pos.length + ")");
	}

	if (this.size.length == 0) {
	    // no entry and thus no resize 
	    return;
	}

	int[] orgSize = this.size.clone();
	boolean resizeNeeded = false;
	for (int idx = 0; idx < this.size.length; idx++) {
	    if (pos[idx] > this.size[idx]) {
		this.size[idx] = pos[idx];
		resizeNeeded = true;
	    }
	}
	// Here, this.size is updated whereas orgSize contains original size

	if (!resizeNeeded) {
	    return;
	}

	// Here, orgSize[0] is defined 
	final int cpyLen    = orgSize[0];
	final int osp = product(orgSize);

	// initialize resulting array with default values 
	D dataOut = newD(product(this.size));
	int idxSrc = 0;
	int idxTrg = 0;
	int[] idxTrgMulti = new int[orgSize.length]; // 0th entry not used 
	int idxIdx;
	int lenUnitGap;
	while (idxSrc < osp) {
	    System.arraycopy(this.data, idxSrc, dataOut, idxTrg, cpyLen);
	    idxSrc += cpyLen;

	    // update idxTrgMulti and idxTrg 
	    idxIdx = 1;
	    lenUnitGap = this.size[0];
	    while (idxIdx < orgSize.length 
	    	   &&  idxTrgMulti[idxIdx] >= orgSize[idxIdx] - 1) {
	    	assert idxTrgMulti[idxIdx] == orgSize[idxIdx] - 1;

		idxTrg -= lenUnitGap * idxTrgMulti[idxIdx];
	    	idxTrgMulti[idxIdx] = 0;

		lenUnitGap *= this.size[idxIdx];
	    	idxIdx++;
	    }
	    assert idxIdx == orgSize.length 
	    	||  idxTrgMulti[idxIdx] < orgSize[idxIdx];
//	    assert idxIdx < orgSize.length;
	    if (idxIdx >= orgSize.length) {
		break;
	    }
	    // update top 

	    idxTrg += lenUnitGap;
	    idxTrgMulti[idxIdx]++;
	} // while 

	this.data = dataOut;
    }

    /**
     * @param pos
     * @return the index into data() for the position
     */
    public final int pos2ind(final int... pos) {
        int idx = 0;
        int factor = 1;
        for (int dim = 0; dim < pos.length; ++dim) {
            if (pos[dim] > this.size[dim]) {
                throw new IndexOutOfBoundsException
		    ("pos exceeded dimension for dimension " + 
		     dim + " (" + pos[dim] + " > " + this.size[dim] + ")");
            }
            idx += (pos[dim] - 1) * factor;
            factor *= this.size[dim];
        }
        return idx;
    }

    // /**
    //  * @return the data
    //  * @deprecated
    //  */
    // // used nowhere 
    // public final D getDataA() {
    //     return this.data;
    // }

    /**
     * Returns the string representation of the given plain position. 
     */
    public abstract String getPlainString(int pos);

    public final int getSizeLength() {
        return this.size.length;
    }

    /**
     * @param i
     *            dimension number in 1 based numbering, 1=row, 2=column
     * @return the size in dimension i
     */
    public final int size(final int i) {
        return this.size[i - 1];
    }

    public final int getSize(final int i) {
        return this.size[i - 1];
    }

    @Override
    public final int hashCode() {
        int result = 1;
        result = PRIME * result + 
	    ((this.data == null) ? 0 : this.data.hashCode());
        result = PRIME * result + Arrays.hashCode(this.size);
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AbstractGenericMatrix<D> other = (AbstractGenericMatrix<D>) obj;
        if (!Arrays.equals(this.size, other.size)) {
	    return false;
        }
	
	return this.data == other.data 
	    || dataEquals(product(this.size), other.data);
    }

    // to implement OctaveObject 
    public abstract OctaveObject shallowCopy();

    public static void main(String[] args) {
	System.out.println("R" + product());
    }

}
