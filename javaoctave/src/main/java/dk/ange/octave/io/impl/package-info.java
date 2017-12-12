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

/**
 * Provides the implementations of the services 
 * specified in {@link dk.ange.octave.io.spi}; 
 * one for each octave type given in octave via <code>typeinfo</code>. 
 * The reader classes are in 
 * <code>META-INF/services/dk.ange.octave.io.OctaveDataReader</code> 
 * whereas the writer classes are in 
 * <code>META-INF/services/dk.ange.octave.io.OctaveDataWriter</code>. 

 * Consequently, the classes are 
 * <ul>

 * <li>
 * {@link CellReader}, {@link CellWriter} for reading/writing cells 
 * <li>
 * ScalarReader for reading (double) scalars, whereas ScalarWriter is missing 
 * <li>
 * ComplexScalarReader for reading complex scalars with double components, 
 * whereas ComplexScalarWriter is missing 
 * <li>
 * Both, (real?) diagonal matrix reader/writer are missing 
 * likewise for complex diagonal matrices 
 * <li>
 * ComplexMatrixReader for reading complex matrices with double components, 
 * whereas ComplexScalarWriter is missing 

 * <li>
 * FakeRangeReader ***** and FakeWriter... maybe for ranges. 
 * Seemingly, FakeRangeReader seems not fully functional. 
 * see {@link dk.ange.octave.type.OctaveFake}

 * <li>
 * BooleanReader, CellWriter for reading/writing cells 



 * </ul>
 */
package dk.ange.octave.io.impl;
