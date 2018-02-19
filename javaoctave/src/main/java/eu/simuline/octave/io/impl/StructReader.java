/*
 * Copyright 2008, 2009, 2012 Ange Optimization ApS
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
package dk.ange.octave.io.impl;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import dk.ange.octave.exception.OctaveParseException;
import dk.ange.octave.io.OctaveIO;
import dk.ange.octave.io.spi.OctaveDataReader;
import dk.ange.octave.type.OctaveCell;
import dk.ange.octave.type.OctaveObject;
import dk.ange.octave.type.OctaveStruct;

import static dk.ange.octave.io.impl.StructWriter.LENGTH;
import static dk.ange.octave.io.impl.StructWriter.NAME;
import static dk.ange.octave.io.impl.StructWriter.TYPE_CELL;

/**
 * The reader for the octave type "struct" 
 * reading an {@link OctaveStruct} from a {@link BufferedReader}. 
 * Reads the format written by {@link StructWriter}
 */
public final class StructReader extends OctaveDataReader {
    private static final String N_DIMS2 = "# ndims: 2";
    private static final String V_DIMS2 = " 1 1";

    private static final CellReader CELL_READER = new CellReader();

    @Override
    public String octaveType() {
        return "struct";
    }

    @Override
    @SuppressWarnings("PMD.NPathComplexity")
    public OctaveStruct read(final BufferedReader reader) {
        String line = OctaveIO.readerReadLine(reader);
        // In octave 3.6 dimension of the scalar is also written now 
	// **** this i cannot see in Writer 
        if (line != null && line.startsWith("# ndims:")) {
            if (!N_DIMS2.equals(line)) {
                throw new OctaveParseException
		    ("JavaOctave does not support matrix structs, read '" + 
		     line + "'");
            }

	    // 1 1
            line = OctaveIO.readerReadLine(reader);
            if (!V_DIMS2.equals(line)) {
                throw new OctaveParseException
		    ("JavaOctave does not support matrix structs, read '" + 
		     line + "'");
            }
            line = OctaveIO.readerReadLine(reader);
        }

        // # length: 4
//        String line = OctaveIO.readerReadLine(reader);
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
	    // in octave 3.4 and 3.6: 
	    // keep reading until line is non-empty
            do {
                line = OctaveIO.readerReadLine(reader);
            } while ("".equals(line));
            if (!line.startsWith(NAME)) {
                throw new OctaveParseException
		    ("Expected <" + NAME + "> got <" + line + ">. ");
            }
            final String subname = line.substring(NAME.length());




            line = OctaveIO.readerReadLine(reader);
            if (!line.equals(TYPE_CELL)) {
                throw new OctaveParseException
		    ("Expected '" + TYPE_CELL + "' got '" + line + "'");
            }

            final OctaveCell cell = CELL_READER.read(reader);
            if (cell.size(1) != 1 || cell.size(2) != 1) {
                throw new OctaveParseException
		    ("JavaOctave does not support matrix structs, size="
		     + cell.getSize(1) + " " + cell.getSize(2) + "...");
	    }

            // data...
	    final OctaveObject value = cell.get(1, 1);
	    data.put(subname, value);
        } // for 

        return new OctaveStruct(data);
    }

}
