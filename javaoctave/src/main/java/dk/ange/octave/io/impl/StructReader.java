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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dk.ange.octave.exception.OctaveParseException;
import dk.ange.octave.io.OctaveIO;
import dk.ange.octave.io.spi.OctaveDataReader;
import dk.ange.octave.type.OctaveCell;
import dk.ange.octave.type.OctaveObject;
import dk.ange.octave.type.OctaveStruct;

/**
 * The reader of struct
 */
public class StructReader extends OctaveDataReader {

    private final static CellReader cellReader = new CellReader();

    @Override
    public String octaveType() {
        return "struct";
    }

    @Override
    public OctaveStruct read(final BufferedReader reader) {
        String line;

        // In octave 3.6 dimension of the scalar is also written now
        line = OctaveIO.readerReadLine(reader);
        if (line != null && line.startsWith("# ndims:")) {
            if (!line.equals("# ndims: 2")) {
                throw new OctaveParseException("JavaOctave does not support matrix structs, read=" + line);
            }
            line = OctaveIO.readerReadLine(reader);
            if (!line.equals(" 1 1")) {
                throw new OctaveParseException("JavaOctave does not support matrix structs, read=" + line);
            }
            line = OctaveIO.readerReadLine(reader);
        }

        // # length: 4
        final String LENGTH = "# length: ";
        if (line == null || !line.startsWith(LENGTH)) {
            throw new OctaveParseException("Expected '" + LENGTH + "' got '" + line + "'");
        }
        final int length = Integer.valueOf(line.substring(LENGTH.length())); // only used during conversion

        final Map<String, OctaveObject> data = new HashMap<String, OctaveObject>();

        for (int i = 0; i < length; i++) {
            // # name: elemmatrix
            final String NAME = "# name: ";
            do { // Work around differences in number of line feeds in octave 3.4 and 3.6
                line = OctaveIO.readerReadLine(reader);
            } while ("".equals(line)); // keep reading until line is non-empty
            if (!line.startsWith(NAME)) {
                throw new OctaveParseException("Expected '" + NAME + "' got '" + line + "'");
            }
            final String subname = line.substring(NAME.length());

            final String CELL = "# type: cell";
            line = OctaveIO.readerReadLine(reader);
            if (!line.equals(CELL)) {
                throw new OctaveParseException("Expected '" + CELL + "' got '" + line + "'");
            }

            final OctaveCell cell = cellReader.read(reader);
            if (cell.size(1) == 1 && cell.size(2) == 1) {
                final OctaveObject value = cell.get(1, 1);
                data.put(subname, value);
            } else {
                throw new OctaveParseException("JavaOctave does not support matrix structs, size="
                        + Arrays.toString(cell.getSize()));
            }
        }

        return new OctaveStruct(data);
    }

}
