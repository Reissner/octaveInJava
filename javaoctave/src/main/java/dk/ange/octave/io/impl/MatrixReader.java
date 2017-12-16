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
import dk.ange.octave.type.OctaveDouble;

/**
 * The reader for the octave type "matrix" (of double) 
 * reading an {@link OctaveDouble} from a {@link BufferedReader}. 
 */
public final class MatrixReader 
    extends AbstractPrimitiveMatrixReader<OctaveDouble> {

    @Override
    public String octaveType() {
        return "matrix";
    }

    @Override
    protected OctaveDouble readVectorizedMatrix(final BufferedReader reader, 
						final String ndimsLine) {
	final int[] size = readSizeVectorizedMatrix(reader,ndimsLine);
	String line;
        final double[] data = new double[product(size)];
        for (int idx = 0; idx < data.length; idx++) {
            line = OctaveIO.readerReadLine(reader);
            data[idx] = ScalarReader.parseDouble(line);
        }
        return new OctaveDouble(data, size);
    }

    @Override
    protected OctaveDouble read2dmatrix(final BufferedReader reader, 
					final String rowsLine) {
	int[] size = readSize2dmatrix(reader,rowsLine);
	int rows = size[0];
	int columns = size[1];
	String line;

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
