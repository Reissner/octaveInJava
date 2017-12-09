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

import java.io.IOException;
import java.io.Writer;

import dk.ange.octave.io.spi.OctaveDataWriter;
import dk.ange.octave.type.OctaveDouble;

/**
 * The writer of OctaveMatrix. 
 */
public final class MatrixWriter extends OctaveDataWriter<OctaveDouble> {

    @Override
    public Class<OctaveDouble> javaType() {
        return OctaveDouble.class;
    }

    @Override
    public void write(final Writer writer, 
		      final OctaveDouble octaveMatrix) throws IOException {
        if (octaveMatrix.getSizeLength() <= 2) {
            if (octaveMatrix.getSizeLength() == 2 && 
		octaveMatrix.size(1) == 1 && 
		octaveMatrix.size(2) == 1) {

                writer.write("# type: scalar\n");
                writer.write(octaveMatrix.get(1, 1) + "\n");
            } else {
                writer.write("# type: matrix\n");
                saveData2d(writer, octaveMatrix);
            }
        } else {
            writer.write("# type: matrix\n");
            saveDataVectorized(writer, octaveMatrix);
        }
    }

    private void saveData2d(final Writer writer, 
			    final OctaveDouble octaveMatrix) 
	throws IOException {

        final double[] data = octaveMatrix.getData();
        final int nrows = octaveMatrix.getSize(1);
	final int ncols = octaveMatrix.getSizeLength() > 1 
	    ? octaveMatrix.getSize(2) : 1;
        writer.write("# rows: " + nrows + "\n# columns: " + ncols + "\n");
        for (int row = 0; row < nrows; row++) {
            for (int col = 0; col < ncols; col++) {
                writer.write(" " + data[row + col * nrows]);
            }
            writer.write('\n');
        }
    }

    private void saveDataVectorized(final Writer writer,
				    final OctaveDouble octaveMatrix) 
	throws IOException {

        final double[] data = octaveMatrix.getData();
        writer.write("# ndims: " + octaveMatrix.getSizeLength() + "\n");
        for (int idx = 1; idx <= octaveMatrix.getSizeLength(); idx++) {
            writer.write(" " + octaveMatrix.getSize(idx));
        }
        for (final double d : data) {
            writer.write("\n " + d);
        }
        writer.write("\n");
    }

}
