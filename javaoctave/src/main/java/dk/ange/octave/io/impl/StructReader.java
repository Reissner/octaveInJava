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

/**
 * The reader of struct. 
 */
public final class StructReader extends OctaveDataReader {
    private static final String LENGTH = "# length: ";
    private static final String CELL = "# type: cell";
    private static final String NAME = "# name: ";

    private static final CellReader CELL_READER = new CellReader();

    @Override
    public String octaveType() {
        return "struct";
    }

    @Override
    public OctaveStruct read(final BufferedReader reader) {
        String line;

        line = OctaveIO.readerReadLine(reader);
        // In octave 3.6 dimension of the scalar is also written now
        if (line != null && line.startsWith("# ndims:")) {
            if (!line.equals("# ndims: 2")) {
                throw new OctaveParseException
		    ("JavaOctave does not support matrix structs, read '" + 
		     line + "'");
            }
            line = OctaveIO.readerReadLine(reader);
            if (!line.equals(" 1 1")) {
                throw new OctaveParseException
		    ("JavaOctave does not support matrix structs, read '" + 
		     line + "'");
            }
            line = OctaveIO.readerReadLine(reader);
        }

        // # length: 4
        if (line == null || !line.startsWith(LENGTH)) {
            throw new OctaveParseException
		("Expected '" + LENGTH + "' got '" + line + "'");
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
		    ("Expected '" + NAME + "' got '" + line + "'");
            }
            final String subname = line.substring(NAME.length());

            line = OctaveIO.readerReadLine(reader);
            if (!line.equals(CELL)) {
                throw new OctaveParseException
		    ("Expected '" + CELL + "' got '" + line + "'");
            }

            final OctaveCell cell = CELL_READER.read(reader);
            if (cell.size(1) == 1 && cell.size(2) == 1) {
                final OctaveObject value = cell.get(1, 1);
                data.put(subname, value);
            } else {
                throw new OctaveParseException
		    ("JavaOctave does not support matrix structs, size="
                        + cell.getSize(1) + " " + cell.getSize(2) + "...");
            }
        }

        return new OctaveStruct(data);
    }

}
