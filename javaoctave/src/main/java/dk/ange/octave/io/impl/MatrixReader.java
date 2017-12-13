/*
 * Copyright 2008 Ange Optimization ApS
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
/**
 * @author Kim Hansen
 */
package dk.ange.octave.io.impl;

import java.io.BufferedReader;

import dk.ange.octave.exception.OctaveParseException;
import dk.ange.octave.io.OctaveIO;
import dk.ange.octave.io.spi.OctaveDataReader;
import dk.ange.octave.type.OctaveDouble;

/**
 * The reader for the octave type "matrix" (of double) 
 * reading an {@link OctaveDouble} from a {@link BufferedReader}. 
 */
public final class MatrixReader extends AbstractPrimitiveMatrixReader {

    @Override
    public String octaveType() {
        return "matrix";
    }

    @Override
    public OctaveDouble read(final BufferedReader reader) {
        String line = OctaveIO.readerReadLine(reader);
        // 2d or 2d+?
        if (line.startsWith("# rows: ")) {
            return read2dmatrix(reader, line);
        } else if (line.startsWith("# ndims: ")) {
            return readVectorizedMatrix(reader, line);
        } else {
            throw new OctaveParseException
		("Expected <# rows: > or <# ndims: >, but got <" + line + ">");
        }
    }

    private OctaveDouble readVectorizedMatrix(final BufferedReader reader, 
					      final String ndimsLine) {
        String line = ndimsLine;
        if (!line.startsWith(NDIMS)) {
            throw new OctaveParseException
		("Expected <" + NDIMS + ">, but got <" + line + ">");
        }
        final int ndims = Integer.parseInt(line.substring(NDIMS.length()));
        line = OctaveIO.readerReadLine(reader);
        final String[] split = line.substring(1).split(" ");
        if (split.length != ndims) {
            throw new OctaveParseException
		("Expected " + ndims + " dimesion, but got " + 
		 (split.length) + " (line was <" + line + ">)");
        }
        final int[] size = new int[split.length];
        for (int dim = 0; dim < split.length; dim++) {
            size[dim] = Integer.parseInt(split[dim]);
        }
        final double[] data = new double[product(size)];
        for (int idx = 0; idx < data.length; idx++) {
            line = OctaveIO.readerReadLine(reader);
            data[idx] = ScalarReader.parseDouble(line);
        }
        return new OctaveDouble(data, size);
    }

    private OctaveDouble read2dmatrix(final BufferedReader reader, 
				      final String rowsLine) {

        // # rows: 1
        String line = rowsLine;
        if (!line.startsWith("# rows: ")) {
            throw new OctaveParseException
		("Expected <# rows: > got <" + line + ">");
        }
        final int rows = Integer.parseInt(line.substring(8));
        // # columns: 3
        line = OctaveIO.readerReadLine(reader);
        if (!line.startsWith("# columns: ")) {
            throw new OctaveParseException
		("Expected <# columns: > got <" + line + ">");
        }
        final int columns = Integer.parseInt(line.substring(11));
        // 1 2 3
        final int[] size = new int[2];
        size[0] = rows;
        size[1] = columns;
        final double[] data = new double[rows * columns];
        for (int r = 1; r <= rows; ++r) {
            line = OctaveIO.readerReadLine(reader);
            final String[] split = line.split(" ");
            if (split.length != columns + 1) {
                throw new OctaveParseException
		    ("Error in matrix-format: '" + line + "'");
            }
            for (int c = 1; c < split.length; c++) {
                data[(r - 1) + (c - 1) * rows] = ScalarReader
		    .parseDouble(split[c]);
            }
        }
        return new OctaveDouble(data, size);
    }

}
