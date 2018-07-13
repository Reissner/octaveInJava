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

import eu.simuline.octave.type.OctaveLong;

import java.io.Writer;
 
/**
 * The writer for the octave type "int32 matrix" 
 * writing an {@link OctaveLong} to a {@link Writer}. 
 */
public final class Int64MatrixWriter 
    extends AbstractPrimitiveMatrixWriter<OctaveLong> {

    @Override
    public Class<OctaveLong> javaType() {
        return OctaveLong.class;
    }

    @Override
    protected String octaveMatrixType() {
        return "int64 matrix";
    }

    @Override
    protected String octaveScalarType() {
        return "int64 scalar";
    }

}
