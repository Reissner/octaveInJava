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

import eu.simuline.octave.type.OctaveInt;

import java.io.BufferedReader;

/**
 * This is deactivated 
 * reading an {@link OctaveInt} from a {@link BufferedReader}. 
 * Note that java class {@link OctaveInt} 
 * is registered with {@link Int32MatrixWriter} 
 * and thus related with {@link Int32MatrixReader}. 
 */
public final class Uint8MatrixReader 
    extends AbstractPrimitiveMatrixReader<OctaveInt, int[]> {

    @Override
    public String octaveType() {
        return "uint8 matrix";
    }

    OctaveInt createOctaveValue(int[] size) {
	return new OctaveInt(size);
    }
}
