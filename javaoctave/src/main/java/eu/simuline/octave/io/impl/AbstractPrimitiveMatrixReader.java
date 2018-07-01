/*
 * Copyright 2017 Ange Optimization ApS
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


package eu.simuline.octave.io.impl;

import java.io.BufferedReader;

import eu.simuline.octave.exception.OctaveParseException;
import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.io.spi.OctaveDataReader;
import eu.simuline.octave.type.matrix.AbstractGenericMatrix;

/**
 * Common Reader class for primitive java types: Boolean, Double, Integer.... 
 *
 * @param <T>
 *    the type to be read in which has to extend {@link AbstractGenericMatrix}. 
 * @param <D>
 *    see type parameter of {@link AbstractGenericMatrix}. 
 */
abstract class AbstractPrimitiveMatrixReader
    <T extends AbstractGenericMatrix<D>, D> 
    extends OctaveDataReader {

    // **** to be eliminated: is in according writer 
    protected static final String NDIMS    = "# ndims: ";
    protected static final String NROWS    = "# rows: ";
    protected static final String NCOLUMNS = "# columns: ";


    abstract T createOctaveValue(int[] size);

    /**
     * The matrix formats, e.g. <tt>matrix</tt> come in two variants: 
     * after line with "type" the next line either starts with 
     * <ul>
     * <li><tt># ndims: </tt> specifying the number of dimensions 
     * and in the next line the lengths in all these dimensions 
     * and all the following lines the vectorized data, 
     * each in a separate line. 
     * This is read by {@link #readVectorizedMatrix(BufferedReader, String)}. 
     * <li><tt># rows: </tt> specifying the number of rows 
     * and <tt># columns: </tt> specifying the number of columns 
     * in the next line (which works only for matrices, i.e. up to dimension 2) 
     * and then for each row a line follows 
     * each of which holds the entries separated by a blank. 
     * This is read by {@link #read2dmatrix(BufferedReader, String)}. 
     * </ul>
     */
    // **** caution: this distinction is valid only for floating point types. 
    // others use vectorized format only 
    // **** this may indicate inappropriate design. 
    @Override
    public T read(final BufferedReader reader) {
        final String line = OctaveIO.readerReadLine(reader);
        // 2d or 2d+?
        if (line.startsWith(NROWS)) {
	    // this case does not occur for int types, 
	    //just for float (including complex) and bool 
            return read2dmatrix(reader, line);
        } else if (line.startsWith(NDIMS)) {
	    return readVectorizedMatrix(reader, line);
        } else {
            throw new OctaveParseException
		("Expected <" + NROWS + "> or <" + NDIMS + 
		 ">, but got <" + line + ">. ");
        }
    }

    private T readVectorizedMatrix(BufferedReader reader, 
				   String dimsLine) {
	int[] size = readSizeVectorizedMatrix(reader, dimsLine);
 	T res = createOctaveValue(size);
  	String line;
	// **** in the long run dataLength is not what we need 
	// active entries only 
        for (int idx = 0; idx < res.dataLength(); idx++) {
            line = OctaveIO.readerReadLine(reader);
 	    res.setPlain(line, idx);
	}
        return res;
    }

    /**
     * Reads a line NDIMS &lt;num of dims> 
     * followed by a line of dimensions: integers separated by blank 
     * and returns an array with the according entries. 
     * In particluar the length is &lt;num of dims>. 
     */
    private int[] readSizeVectorizedMatrix(BufferedReader reader, 
					   String ndimsLine) {
        String line = ndimsLine;
        if (!line.startsWith(NDIMS)) {
            throw new OctaveParseException
		("Expected <" + NDIMS + ">, but got <" + line + ">. ");
        }
        final int ndims = Integer.parseInt(line.substring(NDIMS.length()));

        line = OctaveIO.readerReadLine(reader);
        final String[] split = line.substring(1).split(" ");
        if (split.length != ndims) {
            throw new OctaveParseException
		("Expected <" + ndims + "> dimension, but got <" + 
		 split.length + "> (line was <" + line + ">). ");
        }
        final int[] size = new int[split.length];
        for (int dim = 0; dim < split.length; dim++) {
            size[dim] = Integer.parseInt(split[dim]);
        }
	return size;
    }


    // maybe this just throws an exception because this case does not occur. 
    private T read2dmatrix(BufferedReader reader, 
			   String rowsLine) {
	int[] size = readSize2dmatrix(reader, rowsLine);
	T res = createOctaveValue(size);

	int rows    = size[0];
	int columns = size[1];
	String line;

	for (int r = 1; r <= rows; ++r) {
            line = OctaveIO.readerReadLine(reader);
            final String[] split = line.split(" ");
            if (split.length != columns + 1) {
                throw new OctaveParseException
		    ("Error in matrix-format: '" + line + "'");
            }
            for (int c = 1; c < split.length; c++) {
		res.setPlain(split[c], (r - 1) + (c - 1) * rows);
            }
        }
        return res;
    }

    /**
     * Reads lines NROWS &lt;num of rows> and NCOLUMNS &lt;num of cols>
     * and returns an array {nrows ncols}. 
     */
    protected int[] readSize2dmatrix(BufferedReader reader, 
				     String rowsLine) {
        // # rows: 1
        String line = rowsLine;
        if (!line.startsWith(NROWS)) {
            throw new OctaveParseException
		("Expected <" + NROWS + "> got <" + line + ">. ");
        }
        final int rows = Integer.parseInt(line.substring(NROWS.length()));
        // # columns: 3
        line = OctaveIO.readerReadLine(reader);
        if (!line.startsWith(NCOLUMNS)) {
            throw new OctaveParseException
		("Expected <" + NCOLUMNS + "> got <" + line + ">. ");
        }
        final int columns = Integer.parseInt(line.substring(NCOLUMNS.length()));
        // 1 2 3
        // final int[] size = new int[2];
        // size[0] = rows;
        // size[1] = columns;
        return new int[] {rows, columns};
    }
}
