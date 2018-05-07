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

import java.io.BufferedReader;

import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.io.spi.OctaveDataReader;
import eu.simuline.octave.type.Octave;
import eu.simuline.octave.type.OctaveDouble;

/**
 * The reader for the octave type "scalar" (double) 
 * reading an {@link OctaveDouble} from a {@link BufferedReader}. 
 */
public class DoubleScalarReader extends RealScalarReader {

    @Override
    public String octaveType() {
        return "scalar";
    }

    @Override
    public OctaveDouble read(final BufferedReader reader) {
        return super.read(reader);
    }

    /**
     * This is almost the same as Double.parseDouble(), 
     * but it handles a few more versions of infinity. 
     *
     * @param string
     * @return The parsed Double
     */
    public static double parseDouble(final String string) {
    	return RealScalarReader.parseDouble(string);
    }
}
