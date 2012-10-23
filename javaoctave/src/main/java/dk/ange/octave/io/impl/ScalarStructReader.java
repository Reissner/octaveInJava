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
package dk.ange.octave.io.impl;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import dk.ange.octave.exception.OctaveParseException;
import dk.ange.octave.io.OctaveIO;
import dk.ange.octave.io.spi.OctaveDataReader;
import dk.ange.octave.type.OctaveObject;
import dk.ange.octave.type.OctaveStruct;

/**
 * Read 'scalar struct', this is an encoding similar to 'struct' introduced in octave 3.6, it is optimized to the 1x1
 * struct.
 */
public final class ScalarStructReader extends OctaveDataReader {

    @Override
    public String octaveType() {
        return "scalar struct";
    }

    @Override
    public OctaveObject read(final BufferedReader reader) {
        String line;

        // # ndims: 2
        line = OctaveIO.readerReadLine(reader);
        if (!line.equals("# ndims: 2")) {
            throw new OctaveParseException("JavaOctave does not support matrix structs, read=" + line);
        }

        // 1 1
        line = OctaveIO.readerReadLine(reader);
        if (!line.equals(" 1 1")) {
            throw new OctaveParseException("JavaOctave does not support matrix structs, read=" + line);
        }

        // # length: 4
        line = OctaveIO.readerReadLine(reader);
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

            // data...
            final OctaveObject value = OctaveIO.read(reader);
            data.put(subname, value);
        }

        return new OctaveStruct(data);
    }

}
