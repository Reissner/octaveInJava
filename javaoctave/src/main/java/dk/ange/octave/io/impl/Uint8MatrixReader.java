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
import dk.ange.octave.type.OctaveInt;

/**
 * The reader for the octave type "uint8 matrix" 
 * reading an {@link OctaveInt} from a {@link BufferedReader}. 
 */
public final class Uint8MatrixReader 
    extends AbstractPrimitiveMatrixReader<OctaveInt> {

    @Override
    public String octaveType() {
        return "uint8 matrix";
    }

    @Override
    protected OctaveInt readVectorizedMatrix(final BufferedReader reader,
					     final String ndimsLine) {
        String line = ndimsLine;
        if (!line.startsWith(NDIMS)) {
            throw new OctaveParseException
		("Expected '" + NDIMS + "', but got '" + line + "'");
        }
        final int ndims = Integer.parseInt(line.substring(NDIMS.length()));
        line = OctaveIO.readerReadLine(reader);
        final String[] split = line.substring(1).split(" ");
        if (split.length != ndims) {
            throw new OctaveParseException
		("Expected " + ndims + " dimesion, but got " + (split.length)
		 + " (line was '" + line + "')");
        }
        final int[] size = new int[split.length];
        for (int dim = 0; dim < split.length; dim++) {
            size[dim] = Integer.parseInt(split[dim]);
        }
        final int[] data = new int[product(size)];
        for (int idx = 0; idx < data.length; idx++) {
            line = OctaveIO.readerReadLine(reader);
            // In octave <= 3.6 it was: data[idx] = line.codePointAt(1);
            data[idx] = Integer.parseInt(line.trim());
        }
        return new OctaveInt(data, size);
    }

    @Override
    protected OctaveInt read2dmatrix(final BufferedReader reader,
				     final String rowsLine) {
        // # rows: 1
        String line = rowsLine;
        if (!line.startsWith("# rows: ")) {
            throw new OctaveParseException
		("Expected '# rows: ' got '" + line + "'");
        }
        final int rows = Integer.parseInt(line.substring(8));
        // # columns: 3
        line = OctaveIO.readerReadLine(reader);
        if (!line.startsWith("# columns: ")) {
            throw new OctaveParseException
		("Expected '# columns: ' got '" + line + "'");
        }
        final int columns = Integer.parseInt(line.substring(11));
        // 1 2 3
        final int[] size = new int[2];
        size[0] = rows;
        size[1] = columns;
        final int[] data = new int[rows * columns];
        for (int r = 1; r <= rows; ++r) {
            line = OctaveIO.readerReadLine(reader);
            final String[] split = line.split(" ");
            if (split.length != columns + 1) {
                throw new OctaveParseException
		    ("Error in matrix-format: '" + line + "'");
            }
            for (int c = 1; c < split.length; c++) {
                data[(r - 1) + (c - 1) * rows] = Integer.parseInt(split[c]);
            }
        }
        return new OctaveInt(data, size);
    }



}
