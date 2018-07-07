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

import java.io.IOException;
import java.io.Writer;

import eu.simuline.octave.type.OctaveInt;

/**
 * The writer for the octave type "int32 matrix" 
 * writing an {@link OctaveInt} to a {@link Writer}. 
 */
public final class Int32MatrixWriter 
    extends AbstractPrimitiveMatrixWriter<OctaveInt> {

    @Override
    public Class<OctaveInt> javaType() {
        return OctaveInt.class;
    }

    @Override
    protected String octaveMatrixType() {
        return "int32 matrix";
    }

    @Override
    protected String octaveScalarType() {
        return "int32 scalar";
    }

    @Override
    public void write(final Writer writer, 
		      final OctaveInt octaveMatrix) throws IOException {
	if (octaveMatrix.getSizeLength() == 2 && 
	    octaveMatrix.size(1) == 1 && 
	    octaveMatrix.size(2) == 1) {

	    writer.write("# type: " + octaveScalarType() + "\n");
	    writer.write(octaveMatrix.getPlainString(0) + "\n");
	} else {
	    writer.write("# type: " + octaveMatrixType() + "\n");
	    // **** note: unlike for floating types and bool, 
	    // there is no special case for 2 dimensions, i.e. matrices 
	    // using saveData2d(writer, octaveMatrix);
	    saveDataVectorized(writer, octaveMatrix);
	}
    }


    // never used because for integer types format does not support this. 
    // **** existence of this method signifies odd design 
    // private void saveData2d(final Writer writer, 
    // 			    final OctaveInt octaveMatrix) 
    // 	throws IOException {
    // 	throw new IllegalStateException("Not supported by integer types");
    // }

    private void saveDataVectorized(final Writer writer, 
				    final OctaveInt octaveMatrix) 
	throws IOException {

        writer.write(NDIMS + octaveMatrix.getSizeLength() + "\n");
        for (int idx = 1; idx <= octaveMatrix.getSizeLength(); idx++) {
            writer.write(" " + octaveMatrix.getSize(idx));
        }

	int len = octaveMatrix.dataSize();
        for (int idx = 0; idx < len; idx++) {
            writer.write("\n " + octaveMatrix.getPlainString(idx));
	}

        writer.write("\n");
    }

}
