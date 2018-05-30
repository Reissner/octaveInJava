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
package eu.simuline.octave.io.impl;

import java.io.BufferedReader;

import eu.simuline.octave.exception.OctaveParseException;
import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.io.spi.OctaveDataReader;
import eu.simuline.octave.type.OctaveString;

/**
 * The reader of string/sq_string. 
 *
 * @see OctaveStringReader
 * @see OctaveSqStringReader
 */
public abstract class AbstractOctaveStringReader extends OctaveDataReader {


    private static final String H_ELEMENTS1 = "# elements: 1";
    private static final String H_ELEMENTS0 = "# elements: 0";

    private static final String LENGTH = "# length: ";

    /**
     * Overriding this method may add just handling of <code>\\</code> 
     * or delegate to this method. 
     */
    //@Override
    protected final OctaveString readImpl(final BufferedReader reader) {
        final String elements = OctaveIO.readerReadLine(reader);
        final StringBuilder builder = new StringBuilder();
	// NOTE: in java > 1.7 strings are allowed in switch 
	if (H_ELEMENTS1.equals(elements)) {
            final String lengthString = OctaveIO.readerReadLine(reader);
            if (!lengthString.startsWith(LENGTH)) {
                throw new OctaveParseException
		    ("Parse error in String, line='" + lengthString + "'");
            }
            final int length = Integer
		.parseInt(lengthString.substring(LENGTH.length()));

            boolean first = true;
            while (builder.length() < length) {
                if (!first) {
                    builder.append('\n');
                }
                builder.append(OctaveIO.readerReadLine(reader));
                first = false;
            }

            if (builder.length() != length) {
                throw new OctaveParseException
		    ("Unexpected length of string read. expected=" + 
		     length + ", actual=" + builder.length());
            }
        } else if (!H_ELEMENTS0.equals(elements)) {
            throw new OctaveParseException
		("Expected elements to be 0 or 1, '" + elements + "'");
        }
        return new OctaveString(builder.toString());
    }
}
