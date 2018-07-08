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

import eu.simuline.octave.type.OctaveInt;

import java.io.Writer;

/**
 * This is deactivated. 
 * writing an {@link OctaveInt} holding an uint8 to a {@link Writer}. 
 * Note that java class {@link OctaveInt} 
 * is registered with {@link Int32MatrixWriter}. 
 */
public final class Uint8MatrixWriter 
    extends AbstractPrimitiveMatrixWriter<OctaveInt> {

    @Override
    public Class<OctaveInt> javaType() {
        return OctaveInt.class;
    }

    @Override
    protected String octaveMatrixType() {
        return "uint8 matrix";
    }

    @Override
    protected String octaveScalarType() {
        return "uint8 scalar";
    }

}
