/*
 * Copyright 2007, 2008, 2009 Ange Optimization ApS
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
package eu.simuline.octave.type;

import java.util.Arrays;

/**
 * Represents a Boolean matrix and is appropriate for sparse matrices. 
 *
 * @see OctaveBoolean
 */
public final class OctaveSparseBoolean implements OctaveObject {

    private static final int PRIME = 31;

    private final int rows;

    private final int columns;

    // number of non-zero, i.e. of non-false, i.e. of true. 
    // meant number of entries with value true. 
    // seems to be rowIndices.length which equals columnIndices.length. 
    private int nnz;

    private final int[] rowIndexes;

    private final int[] columnIndexes;

    private final boolean[] data;

    private OctaveSparseBoolean(final int rows,
				final int columns,
				final int nnz,
				final int[] rowIndexes,
				final int[] columnIndexes,
				final boolean[] data) {
        this.rows = rows;
        this.columns = columns;
        this.nnz = nnz;
        this.rowIndexes = rowIndexes;
        this.columnIndexes = columnIndexes;
        this.data = data;
    }

    /**
     * @param rows
     * @param columns
     * @param nnz
     */
    public OctaveSparseBoolean(final int rows,
			       final int columns,
			       final int nnz) {
	// ****         nnz=0
        this(rows, columns, 0, new int[nnz], new int[nnz], new boolean[nnz]);
    }

    @Override
    public OctaveSparseBoolean shallowCopy() {
        return new OctaveSparseBoolean(this.rows,
				       this.columns,
				       this.nnz,
				       this.rowIndexes,
				       this.columnIndexes,
				       this.data);
    }

    /**
     * @param value
     * @param row
     * @param column
     */
    @SuppressWarnings("checkstyle:nowhitespaceafter")
    public void set(final boolean value, final int row, final int column) {
        this.data         [nnz] = value;
        this.rowIndexes   [nnz] = row;
        this.columnIndexes[nnz] = column;
        ++this.nnz;
    }

    /**
     * @return the rows
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * @return the columns
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * @return the nnz
     */
    public int getNnz() {
        return this.nnz;
    }

    /**
     * @return the rowIndexes
     */
    @edu.umd.cs.findbugs.annotations.SuppressWarnings
    (value = "EI_EXPOSE_REP", 
     justification = "Not woth fixing: class has to be rewritten anyway. ")
    public int[] getRowIndexes() {
        return this.rowIndexes;
    }

    /**
     * @return the columnIndexes
     */
    @edu.umd.cs.findbugs.annotations.SuppressWarnings
    (value = "EI_EXPOSE_REP", 
     justification = "Not woth fixing: class has to be rewritten anyway. ")
    public int[] getColumnIndexes() {
        return this.columnIndexes;
    }

    /**
     * @return the data
     */
    @edu.umd.cs.findbugs.annotations.SuppressWarnings
    (value = "EI_EXPOSE_REP", 
     justification = "Not woth fixing: class has to be rewritten anyway. ")
    public boolean[] getData() {
        return this.data;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = PRIME * result + Arrays.hashCode(columnIndexes);
        result = PRIME * result + columns;
        result = PRIME * result + Arrays.hashCode(data);
        result = PRIME * result + nnz;
        result = PRIME * result + Arrays.hashCode(rowIndexes);
        result = PRIME * result + rows;
        return result;
    }

    @Override
    @SuppressWarnings("PMD.NPathComplexity")
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
        final OctaveSparseBoolean other = (OctaveSparseBoolean) obj;
        if (!Arrays.equals(this.columnIndexes, other.columnIndexes)) {
            return false;
        }
        if (this.columns != other.columns) {
            return false;
        }
        if (!Arrays.equals(this.data, other.data)) {
            return false;
        }
        if (this.nnz != other.nnz) {
            return false;
        }
        if (!Arrays.equals(this.rowIndexes, other.rowIndexes)) {
            return false;
        }
	return this.rows == other.rows;
        // if (rows != other.rows) {
        //     return false;
        // }
        // return true;
    }

}
