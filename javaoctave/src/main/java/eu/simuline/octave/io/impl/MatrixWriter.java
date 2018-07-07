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
package eu.simuline.octave.io.impl;

import eu.simuline.octave.type.OctaveDouble;

import java.io.IOException;
import java.io.Writer;

/**
 * The writer for the octave types 
 * "matrix" (of double) and "scalar" (of double) 
 * writing an {@link OctaveDouble} to a {@link Writer}. 
 */
public final class MatrixWriter 
    extends AbstractLogicalFloatingPointWriter<OctaveDouble> {

    @Override
    public Class<OctaveDouble> javaType() {
        return OctaveDouble.class;
    }

    @Override
    protected String octaveMatrixType() {
        return "matrix";
    }

    @Override
    protected String octaveScalarType() {
        return "scalar";
    }
}
