/*
 * Copyright 2008, 2009 Ange Optimization ApS
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

import java.io.IOException;
import java.io.Writer;

import dk.ange.octave.io.spi.OctaveDataWriter;
import dk.ange.octave.type.OctaveInt;

/**
 * The writer for the octave type "bool matrix" (matrix with boolean entries) 
 * writing an {@link OctaveInt} to a {@link Writer}. 
 */
public final class Uint8MatrixWriter 
    extends AbstractPrimitiveMatrixWriter<OctaveInt> {

    @Override
    public Class<OctaveInt> javaType() {
        return OctaveInt.class;
    }

    @Override
    public void write(final Writer writer, 
		      final OctaveInt octaveInt) throws IOException {
        writer.write("# type: uint8 matrix\n");
        if (octaveInt.getSizeLength() > 2) {
            saveDataVectorized(writer, octaveInt);
        } else {
            saveData2d(writer, octaveInt);
        }
    }

    private void saveData2d(final Writer writer, 
			    final OctaveInt octaveInt) 
	throws IOException {

        final int[] data = octaveInt.getData();
        final int nrows = octaveInt.getSize(1);
        final int ncols = octaveInt.getSizeLength() > 1 
	    ? octaveInt.getSize(2) : 1;
        writer.write(NROWS + nrows + "\n");
        writer.write(NCOLUMNS + ncols + "\n");
        for (int row = 0; row < nrows; row++) {
            for (int col = 0; col < ncols; col++) {
                writer.write(" " + data[row + col * nrows]);
            }
            writer.write('\n');
        }
    }

    private void saveDataVectorized(final Writer writer, 
				    final OctaveInt octaveMatrix) 
	throws IOException {

        final int[] data = octaveMatrix.getData();
        writer.write(NDIMS + octaveMatrix.getSizeLength() + "\n");
        for (int idx = 1; idx <= octaveMatrix.getSizeLength(); idx++) {
            writer.write(" " + octaveMatrix.getSize(idx));
        }
        for (final int iNum : data) {
            writer.write("\n " + iNum);
        }
        writer.write("\n");
    }

}
