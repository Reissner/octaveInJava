/*
 * Copyright 2012 Ange Optimization ApS
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

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import eu.simuline.octave.exception.OctaveParseException;
import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.io.spi.OctaveDataReader;
import eu.simuline.octave.type.OctaveObject;
import eu.simuline.octave.type.OctaveStruct;

/**
 * The reader for the octave type "scalar struct" 
 * (which is an encoding similar to "struct" 
 * introduced in octave 3.6, optimized to the 1x1 struct) 
 * reading an {@link OctaveObject} from a {@link BufferedReader}.
 */
public final class ScalarStructReader extends OctaveDataReader {
    private static final String NAME = "# name: ";
    private static final String LENGTH = "# length: ";
    private static final String N_DIMS2 = "# ndims: 2";
    private static final String V_DIMS2 = " 1 1";

    @Override
    public String octaveType() {
        return "scalar struct";
    }

    @Override
    public OctaveStruct read(final BufferedReader reader) {
	// **** this i cannot see in Writer 
        // # ndims: 2
        String line = OctaveIO.readerReadLine(reader);
        if (!N_DIMS2.equals(line)) {
            throw new OctaveParseException
		("JavaOctave does not support matrix structs, read=" + line);
        }

        // 1 1
        line = OctaveIO.readerReadLine(reader);
        if (!V_DIMS2.equals(line)) {
            throw new OctaveParseException
		("JavaOctave does not support matrix structs, read=" + line);
        }

        // # length: 4
        line = OctaveIO.readerReadLine(reader);
        if (line == null || !line.startsWith(LENGTH)) {
            throw new OctaveParseException
		("Expected <" + LENGTH + "> got <" + line + ">. ");
       }
        final int length = Integer.parseInt(line.substring(LENGTH.length()));
	// only used during conversion

        final Map<String, OctaveObject> data =
	    new HashMap<String, OctaveObject>();

        for (int i = 0; i < length; i++) {
            // # name: elemmatrix
	    // Work around differences in number of line feeds 
	    // in octave 3.4 and 3.6
	    // keep reading until line is non-empty
            do {
                line = OctaveIO.readerReadLine(reader);
            } while ("".equals(line));
            if (!line.startsWith(NAME)) {
                throw new OctaveParseException
		    ("Expected <" + NAME + "> got <" + line + ">. ");
            }
            final String subname = line.substring(NAME.length());




            // data...
            final OctaveObject value = OctaveIO.read(reader);
            data.put(subname, value);
        } // for

        return new OctaveStruct(data);
    }

}
