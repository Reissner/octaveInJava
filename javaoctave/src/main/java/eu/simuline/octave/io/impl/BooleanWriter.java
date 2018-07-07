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

import eu.simuline.octave.type.OctaveBoolean;
import eu.simuline.octave.util.StringUtil;

import java.io.IOException;
import java.io.Writer;

/**
 * The writer for the octave type "bool matrix" (matrix with boolean entries) 
 * and "bool", which is short for "bool scalar", 
 * writing an {@link OctaveBoolean} to a {@link Writer}. 
 */
public final class BooleanWriter 
    extends AbstractLogicalFloatingPointWriter<OctaveBoolean> {

    @Override
    public Class<OctaveBoolean> javaType() {
        return OctaveBoolean.class;
    }

    @Override
    protected String octaveMatrixType() {
        return "bool matrix";
    }

    @Override
    protected String octaveScalarType() {
        return "bool";
    }
}
