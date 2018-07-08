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

import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.io.spi.OctaveDataReader;
import eu.simuline.octave.type.matrix.AbstractGenericMatrix;

import java.io.BufferedReader;

/**
 * Common Reader class for scalars of primitive java types: 
 * Boolean, Double, Integer.... 
 *
 * @param <T>
 *    the type to be read in which has to extend {@link AbstractGenericMatrix}. 
 */
abstract class AbstractPrimitiveScalarReader
    <T extends AbstractGenericMatrix<?>> 
    extends OctaveDataReader {

    @Override
    public final T read(final BufferedReader reader) {
        final String line = OctaveIO.readerReadLine(reader);
        final T ret = createOctaveScalar();
	ret.setPlain(line, 0);
        return ret;
    }

    abstract T createOctaveScalar();
}
