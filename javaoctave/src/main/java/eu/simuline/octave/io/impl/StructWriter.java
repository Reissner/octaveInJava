/*
 * Copyright 2008, 2009 Ange Optimization ApS
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

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.io.spi.OctaveDataWriter;
import eu.simuline.octave.type.OctaveObject;
import eu.simuline.octave.type.OctaveStruct;

// import static eu.simuline.octave.io.impl.CellWriter.NROWS;
// import static eu.simuline.octave.io.impl.CellWriter.NCOLUMNS;

/**
 * The writer for the octave type "struct" 
 * writing an {@link OctaveStruct} to a {@link Writer}. 
 *
 * The format is 
 * <pre>
 * # type: struct\n
 * # length: ...\n
 * // comment: the following is in a loop of given length 
 * </pre>
 */
public final class StructWriter extends OctaveDataWriter<OctaveStruct> {
    static final String LENGTH = "# length: ";
    static final String NAME   = "# name: ";

    static final String TYPE_CELL = "# type: cell";

    @Override
    public Class<OctaveStruct> javaType() {
        return OctaveStruct.class;
    }

    @Override
    public void write(final Writer writer,
		      final OctaveStruct octaveStruct) throws IOException {
        writer.write("# type: struct\n");
        final Map<String, OctaveObject> data = octaveStruct.getData();
        writer.write(LENGTH + data.size() + "\n");
        for (final Map.Entry<String, OctaveObject> entry : data.entrySet()) {
            final String subname = entry.getKey();
            final OctaveObject value = entry.getValue();
            writer.write(NAME + subname + "\n");

	    // **** what follows seems to correspond with CellReader 
            writer.write(TYPE_CELL + "\n");
//            writer.write("# type: cell\n");
            writer.write("# rows: " + 1 + "\n");
            writer.write("# columns: " + 1 + "\n");
            OctaveIO.write(writer, "<cell-element>", value);
            writer.write("\n");
        }
    }

}
