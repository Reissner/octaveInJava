/*
 * Copyright 2010 Ange Optimization ApS
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

import java.io.BufferedReader;

/**
 * The reader for the octave type (scalar) "bool" 
 * reading an {@link OctaveBoolean} from a {@link BufferedReader}. 
 */
public final class BooleanSingleReader 
    extends AbstractPrimitiveScalarReader<OctaveBoolean> {

    @Override
    public String octaveType() {
        return "bool";
    }

    OctaveBoolean createOctaveScalar() {
	return new OctaveBoolean(1, 1);
    }
}
