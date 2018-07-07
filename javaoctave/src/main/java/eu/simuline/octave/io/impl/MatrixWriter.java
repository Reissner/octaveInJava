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
package eu.simuline.octave.io.impl;

import eu.simuline.octave.type.OctaveDouble;

import java.io.IOException;
import java.io.Writer;

/**
 * The writer for the octave types 
 * "matrix" (of double) and "scalar" (of double) 
 * writing an {@link OctaveDouble} to a {@link Writer}. 
 */
public final class MatrixWriter 
    extends AbstractLogicalFloatingPointWriter<OctaveDouble> {

    @Override
    public Class<OctaveDouble> javaType() {
        return OctaveDouble.class;
    }

    @Override
    protected String octaveMatrixType() {
        return "matrix";
    }

    @Override
    protected String octaveScalarType() {
        return "scalar";
    }




    @Override
    public void write(final Writer writer, 
		      final OctaveDouble octaveMatrix) throws IOException {
        if (octaveMatrix.getSizeLength() > 2) {
	    writer.write("# type: " + octaveMatrixType() + "\n");
            saveDataVectorized(writer, octaveMatrix);
	} else {
            if (octaveMatrix.getSizeLength() == 2 && 
		octaveMatrix.size(1) == 1 && 
		octaveMatrix.size(2) == 1) {

                writer.write("# type: " + octaveScalarType() + "\n");
		writer.write(octaveMatrix.getPlainString(0) + "\n");
            } else {
                writer.write("# type: " + octaveMatrixType() + "\n");
                saveData2d(writer, octaveMatrix);
            }
        }
    }
}
