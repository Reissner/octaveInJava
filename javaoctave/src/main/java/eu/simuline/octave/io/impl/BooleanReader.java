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

import eu.simuline.octave.type.OctaveBoolean;

import java.io.BufferedReader;

/**
 * The reader for the octave type "bool matrix" (matrix with boolean entries) 
 * reading an {@link OctaveBoolean} from a {@link BufferedReader}. 
 * **** the class name should be BoolMatrixReader **** 
 */
public final class BooleanReader 
    extends AbstractPrimitiveMatrixReader<OctaveBoolean, boolean[]> {

    @Override
    public String octaveType() {
        return "bool matrix";
    }

    OctaveBoolean createOctaveValue(int[] size) {
	return new OctaveBoolean(size);
    }
}
