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


package dk.ange.octave.io.impl;

import dk.ange.octave.io.spi.OctaveDataReader;

abstract class AbstractPrimitiveMatrixReader extends OctaveDataReader {
    protected static final String NDIMS = "# ndims: ";

    /**
     * @param ns
     * @return product of rs
     */
    // **** same as in AbstractGenericMatrix 
    protected static int product(final int... ns) {
        int p = 1;
        for (final int n : ns) {
            p *= n;
        }
        return p;
    }

}
