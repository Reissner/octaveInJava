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


package dk.ange.octave.io.impl;

import java.io.BufferedReader;

import dk.ange.octave.exception.OctaveParseException;
import dk.ange.octave.io.OctaveIO;
import dk.ange.octave.io.spi.OctaveDataReader;
import dk.ange.octave.type.OctaveObject;

/**
 * Common Reader class for primitive java types: Boolean, Double, Integer.... 
 *
 * @param <T>
 *    the type to be read in. 
 */
abstract class AbstractPrimitiveMatrixReader<T extends OctaveObject> 
    extends OctaveDataReader {
    protected static final String NDIMS    = "# ndims: ";
    protected static final String NROWS    = "# rows: ";
    protected static final String NCOLUMNS = "# columns: ";

    @Override
    public T read(final BufferedReader reader) {
        final String line = OctaveIO.readerReadLine(reader);
        // 2d or 2d+?
        if (line.startsWith(NROWS)) {
            return read2dmatrix(reader, line);
        } else if (line.startsWith(NDIMS)) {
            return readVectorizedMatrix(reader, line);
        } else {
            throw new OctaveParseException
		("Expected <" + NROWS + "> or <" + NDIMS + 
		 ">, but got <" + line + ">. ");
        }
    }

    protected abstract T readVectorizedMatrix(BufferedReader reader, 
					      String ndimsLine);

    protected int[] readSizeVectorizedMatrix(BufferedReader reader, 
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


 
    protected abstract T read2dmatrix(BufferedReader reader, 
				      String rowsLine);

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

    /**
     * @param ns
     * @return product of rs
     */
    // **** same as in AbstractGenericMatrix 
    protected static final int product(final int... ns) {
        int p = 1;
        for (final int n : ns) {
            p *= n;
        }
        return p;
    }

}
