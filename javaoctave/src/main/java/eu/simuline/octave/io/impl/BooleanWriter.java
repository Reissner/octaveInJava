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
package eu.simuline.octave.io.impl;

import eu.simuline.octave.type.OctaveBoolean;
import eu.simuline.octave.util.StringUtil;

import java.io.IOException;
import java.io.Writer;

/**
 * The writer for the octave type "bool matrix" (matrix with boolean entries) 
 * and "bool", which is short for "bool scalar", 
 * writing an {@link OctaveBoolean} to a {@link Writer}. 
 */
public final class BooleanWriter 
    extends AbstractPrimitiveMatrixWriter<OctaveBoolean> {

    @Override
    public Class<OctaveBoolean> javaType() {
        return OctaveBoolean.class;
    }

    @Override
    public void write(final Writer writer, 
		      final OctaveBoolean octaveMatrix) throws IOException {
        if (octaveMatrix.getSizeLength() > 2) {
	    writer.write("# type: " + "bool matrix" + "\n");
            saveDataVectorized(writer, octaveMatrix);
        } else {
            if (octaveMatrix.getSizeLength() == 2 && 
		octaveMatrix.size(1) == 1 && 
		octaveMatrix.size(2) == 1) {

                writer.write("# type: " + "bool" + "\n");
		writer.write(StringUtil.toString(octaveMatrix.get(1, 1))
			     + "\n");
            } else {
		writer.write("# type: " + "bool matrix" + "\n");
		saveData2d(writer, octaveMatrix);
	    }
        }
    }

    private void saveData2d(final Writer writer, 
			    final OctaveBoolean octaveMatrix) 
	throws IOException {

        final boolean[] data = octaveMatrix.getData();
        final int nrows = octaveMatrix.getSize(1);
        final int ncols = octaveMatrix.getSizeLength() > 1 
	    ? octaveMatrix.getSize(2) : 1;
        writer.write(NROWS + nrows + "\n");
        writer.write(NCOLUMNS + ncols + "\n");
        for (int row = 0; row < nrows; row++) {
            for (int col = 0; col < ncols; col++) {
                writer.write(" " + StringUtil.toString(data[row + col * nrows]));
            }
            writer.write('\n');
        }
    }

    private void saveDataVectorized(final Writer writer, 
				    final OctaveBoolean octaveMatrix) 
	throws IOException {

        final boolean[] data = octaveMatrix.getData();
        writer.write(NDIMS + octaveMatrix.getSizeLength() + "\n");
        for (int idx = 1; idx <= octaveMatrix.getSizeLength(); idx++) {
            writer.write(" " + octaveMatrix.getSize(idx));
        }
        for (final boolean b : data) {
            writer.write("\n " + StringUtil.toString(b));
        }
        writer.write("\n");
    }

}
