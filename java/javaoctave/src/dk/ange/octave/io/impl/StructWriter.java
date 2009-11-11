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
package dk.ange.octave.io.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import dk.ange.octave.io.OctaveIO;
import dk.ange.octave.io.spi.OctaveDataWriter;
import dk.ange.octave.type.OctaveStruct;
import dk.ange.octave.type.OctaveObject;

/**
 * The writer of OctaveStruct
 */
public final class StructWriter extends OctaveDataWriter {

    @Override
    public Class<? extends OctaveObject> javaType() {
        return OctaveStruct.class;
    }

    @Override
    public void write(final Writer writer, final OctaveObject octaveType) throws IOException {
        final OctaveStruct octaveStruct = (OctaveStruct) octaveType;
        final Map<String, OctaveObject> data = octaveStruct.getData();
        writer.write("# type: struct\n# length: " + data.size() + "\n");
        for (final Map.Entry<String, OctaveObject> entry : data.entrySet()) {
            final String subname = entry.getKey();
            final OctaveObject value = entry.getValue();
            writer.write("# name: " + subname + "\n# type: cell\n# rows: 1\n# columns: 1\n");
            OctaveIO.write(writer, "<cell-element>", value);
        }
    }

}
