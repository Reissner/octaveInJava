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
    protected static final String NDIMS = "# ndims: ";

    @Override
    public T read(final BufferedReader reader) {
        final String line = OctaveIO.readerReadLine(reader);
        // 2d or 2d+?
        if (line.startsWith("# rows: ")) {
            return read2dmatrix(reader, line);
        } else if (line.startsWith("# ndims: ")) {
            return readVectorizedMatrix(reader, line);
        } else {
            throw new OctaveParseException
		("Expected '# rows: ' or '# ndims: ', but got '" + line + "'");
        }
    }

    protected abstract T readVectorizedMatrix(final BufferedReader reader, 
					      final String ndimsLine);
 
    protected abstract T read2dmatrix(final BufferedReader reader, 
				      final String rowsLine);

    /**
     * @param ns
     * @return product of rs
     */
    // **** same as in AbstractGenericMatrix 
    protected static int product(final int... ns) {
        int p = 1;
        for (final int n : ns) {
            p *= n;
        }
        return p;
    }

}
