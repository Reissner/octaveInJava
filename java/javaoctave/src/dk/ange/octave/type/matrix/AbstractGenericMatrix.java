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
package dk.ange.octave.type.matrix;

import java.util.Arrays;

/**
 * A general matrix that does not even know that it is an array it stores its data in.
 * 
 * @param <D>
 *            an array
 */
abstract public class AbstractGenericMatrix<D> {

    /**
     * The dimensions, rows x columns x depth x ....
     */
    protected int[] size;

    /**
     * The data, vectorized.
     */
    protected D data;

    /**
     * Constructor that creates new blank matrix
     * 
     * @param size
     */
    public AbstractGenericMatrix(final int... size) {
        this.size = size;
        checkSize();
        this.data = newD(product(size));
    }

    /**
     * Constructor that reuses data in the new object
     * 
     * @param data
     * @param size
     */
    public AbstractGenericMatrix(final D data, final int... size) {
        this.size = size;
        checkSize();
        this.data = data;
        checkDataSize();
    }

    private void checkSize() throws IllegalArgumentException {
        if (size.length == 0) {
            throw new IllegalArgumentException("no size");
        }
        if (size.length < 2) {
            throw new IllegalArgumentException("size must have a least 2 dimenstions");
        }
        for (final int s : size) {
            if (s < 0) {
                throw new IllegalArgumentException("element in size less than zero. =" + s);
            }
        }
    }

    private void checkDataSize() {
        if (product(size) > dataLength()) {
            final StringBuilder text = new StringBuilder();
            text.append("length of data(");
            text.append(dataLength());
            text.append(") is smaller than size(");
            text.append("[");
            boolean first = true;
            for (final int i : size) {
                if (first) {
                    first = false;
                } else {
                    text.append(", ");
                }
                text.append(i);
            }
            text.append("]");
            text.append(")");
            throw new IllegalArgumentException(text.toString());
        }
    }

    /**
     * @param size_
     * @return new D[size]
     */
    abstract protected D newD(int size_);

    /**
     * @return data_.length
     */
    abstract protected int dataLength();

    /**
     * Fill data with the default value between fromIndex and toIndex.
     * 
     * Arrays.fill(data, fromIndex, toIndex, default);
     * 
     * @param fromIndex
     * @param toIndex
     */
    abstract protected void dataFillInit(int fromIndex, int toIndex);

    /**
     * @param usedLength
     * @param otherData
     * @return true if the used part of data is equal to otherData
     */
    abstract protected boolean dataEquals(int usedLength, D otherData);

    /**
     * @param ns
     * @return product of ns
     */
    protected static int product(final int... ns) {
        int p = 1;
        for (final int n : ns) {
            p *= n;
        }
        return p;
    }

    /**
     * Resize data to size pos
     * 
     * @param pos
     */
    protected void resize(final int... pos) {
        if (size.length != pos.length) {
            throw new UnsupportedOperationException("Change in number of dimenstions not supported");
        }
        // Resize from the smallest dimension. This is not the optimal way to do it, but it works.
        int smallest_dim = 0;
        final int[] newsize = new int[size.length];
        System.arraycopy(size, 0, newsize, 0, size.length);
        for (; smallest_dim < size.length; smallest_dim++) {
            if (pos[smallest_dim] > size[smallest_dim]) {
                newsize[smallest_dim] = pos[smallest_dim];
                resizework(smallest_dim, newsize);
            }
        }
    }

    /**
     * Do the resizing, this is a little magic...
     * 
     * @param smallest_dim
     * @param pos
     */
    private void resizework(final int smallest_dim, final int[] pos) {
        // Calculate blocksize
        int blocksize = 1;
        for (int dim = 0; dim < smallest_dim + 1; dim++) {
            blocksize *= size[dim];
        }

        // Calculate new dimensions
        final int[] newsize = new int[size.length];
        for (int dim = 0; dim < newsize.length; dim++) {
            newsize[dim] = Math.max(pos[dim], size[dim]);
        }

        // Calculate target stride
        int stride = 1;
        for (int dim = 0; dim < smallest_dim + 1; dim++) {
            stride *= newsize[dim];
        }

        // Allocate new data array if necessary
        final int neededSize = product(newsize);
        if (dataLength() < neededSize) {
            final D newdata = newD(neededSize * 2);
            // Move data into new array
            int src_offset = 0;
            for (int dest_offset = 0; dest_offset < neededSize; dest_offset += stride) {
                System.arraycopy(data, src_offset, newdata, dest_offset, blocksize);
                src_offset += blocksize;
            }

            // Sanity check
            if (src_offset != product(size)) {
                throw new IllegalStateException("Failed to copy all data in resize");
            }

            // Set data
            data = newdata;
        } else {
            // Move around the data
            int src_offset = product(size) - blocksize;
            int dest_offset = neededSize - stride;
            while (src_offset > 0) {
                System.arraycopy(data, src_offset, data, dest_offset, blocksize);
                dataFillInit(dest_offset + blocksize, dest_offset + stride);
                src_offset -= blocksize;
                dest_offset -= stride;
            }
        }

        // set new size
        size = newsize;
    }

    /**
     * @param pos
     * @return the index into data() for the position
     */
    public int pos2ind(final int... pos) {
        int idx = 0;
        int factor = 1;
        for (int dim = 0; dim < pos.length; ++dim) {
            if (pos[dim] > size[dim]) {
                throw new IndexOutOfBoundsException("pos exceeded dimension for dimension " + dim + " (" + pos[dim]
                        + " > " + size[dim] + ")");
            }
            idx += (pos[dim] - 1) * factor;
            factor *= size[dim];
        }
        return idx;
    }

    /**
     * @return the data
     */
    public D getData() {
        return data;
    }

    /**
     * @return the size
     */
    public int[] getSize() {
        return size;
    }

    /**
     * @param i
     *            dimension number in 1 based numbering, 1=row, 2=column
     * @return the size in dimension i
     */
    public int size(final int i) {
        return size[i - 1];
    }

    // FIXME delete
    /**
     * @return columns in matrix
     */
    public int columns() {
        return size[1];
    }

    // FIXME delete
    /**
     * @return rows in matrix
     */
    public int rows() {
        return size[0];
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + Arrays.hashCode(size);
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractGenericMatrix<D> other = (AbstractGenericMatrix<D>) obj;
        if (!Arrays.equals(size, other.size)) {
            return false;
        }
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!(data == other.data || dataEquals(product(size), other.data))) {
            return false;
        }
        return true;
    }

}
