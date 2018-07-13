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
 * @author Ernst Reissner
 */
package eu.simuline.octave.io.impl;

import eu.simuline.octave.type.OctaveLong;

import java.io.BufferedReader;

/**
 * The reader for the octave type "int64 matrix" 
 * reading an {@link OctaveLong} from a {@link BufferedReader}. 
 */
public final class Int64MatrixReader 
    extends AbstractPrimitiveMatrixReader<OctaveLong> {

    @Override
    public String octaveType() {
        return "int64 matrix";
    }

    OctaveLong createOctaveValue(int[] size) {
	return new OctaveLong(size);
    }
}
