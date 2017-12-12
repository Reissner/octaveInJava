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
 * {@link dk.ange.octave.io.impl.BooleanReader}/
 * {@link dk.ange.octave.io.impl.BooleanWriter} 
 * for reading/writing octave (double) "bool matrix"s 
 * represented by {@link dk.ange.octave.type.OctaveBoolean}. 
 * <p>
 * There is also a {@link dk.ange.octave.io.impl.BooleanSingleReader} 
 * for reading octave (scalar) "bool"s 
 * whereas no BooleanSingleWriter is provided. 



 * <li>
 * {@link dk.ange.octave.io.impl.CellReader}/ 
 * {@link dk.ange.octave.io.impl.CellWriter} 
 * for reading/writing octave "cell"s 
 * represented by {@link dk.ange.octave.type.OctaveCell}. 
 * <li>
 * {@link dk.ange.octave.io.impl.MatrixReader}/ 
 * {@link dk.ange.octave.io.impl.MatrixWriter} 
 * for reading/writing octave (double) "matrix"s 
 * represented by {@link dk.ange.octave.type.OctaveDouble}. 
 * <p>
 * There is also a {@link dk.ange.octave.io.impl.ScalarReader} 
 * for reading  octave (double) "scalar"s 
 * whereas no <code>ScalarWriter</code> is provided. 




 * <li>
 * {@link dk.ange.octave.io.impl.ComplexMatrixReader}/ 
 * {@link dk.ange.octave.io.impl.ComplexScalarReader} 
 * for reading octave (double) "complex matrix"s and "complex scalar"s 
 * both represented by {@link dk.ange.octave.type.OctaveComplex}. 
 * <p>
 * Both <code>ComplexMatrixWriter</code> and <code>ComplexScalarWriter</code> 
 * are missing. 

 * <li>
 * {@link dk.ange.octave.io.impl.FakeRangeReader} ***** and 
 * {@link dk.ange.octave.io.impl.FakeWriter}... maybe for ranges. 
 * Seemingly, {@link dk.ange.octave.io.impl.FakeRangeReader} 
 * is not fully functional. 
 * Both read/write {@link dk.ange.octave.type.OctaveFake}; 
 * package {@link dk.ange.octave.type} does not contain a class 
 * representing range explicitly. 

 * <li>
 * {@link dk.ange.octave.io.impl.FunctionHandleWriter} 
 * for writing octave "function handle"s 
 * represented by {@link dk.ange.octave.type.OctaveFunctionHandle}. 
 * CAUTION: there is no according reader. 
 * <p>
 * There is no support for octave types 
 * "built-in function", "user-defined function", "dynamically-linked function", 
 * "inline function" (which will be deprecated). 

 * <li>
 * {@link dk.ange.octave.io.impl.OctaveStringReader}/ 
 * {@link dk.ange.octave.io.impl.OctaveStringWriter} 
 * for reading/writing octave "string"s 
 * represented by {@link dk.ange.octave.type.OctaveString}. 
 * CAUTION: 
 * There is also an {@link dk.ange.octave.io.impl.OctaveSqStringReader}, 
 * which is not fully functional: 
 * It reads like {@link dk.ange.octave.io.impl.OctaveStringReader} 
 * and performs an additional check to ensure that the octave "sq_string" 
 * can be in fact represented by an octave "string". 
 * Thus {@link dk.ange.octave.io.impl.OctaveStringReader} 
 * and  {@link dk.ange.octave.io.impl.OctaveSqStringReader} 
 * share the same writer {@link dk.ange.octave.io.impl.OctaveStringWriter}. 

 * <li>
 * {@link dk.ange.octave.io.impl.StructReader}/ 
 * {@link dk.ange.octave.io.impl.StructWriter} 
 * for reading/writing octave "struct"s 
 * represented by {@link dk.ange.octave.type.OctaveStruct}. 
 * <p>
 * There is also a {@link dk.ange.octave.io.impl.ScalarStructReader} 
 * for reading octave "scalar struct"s (which is optimized for 1x1 structs) 
 * also into {@link dk.ange.octave.type.OctaveStruct}. 
 * There is no according <code>ScalarStructWriter</code>. 


 * <li>
 * {@link dk.ange.octave.io.impl.SparseBooleanReader}/ 
 * {@link dk.ange.octave.io.impl.SparseBooleanWriter} 
 * for reading/writing octave (double) "sparse bool matrix"s 
 * represented by {@link dk.ange.octave.type.OctaveSparseBoolean}. 
 * <p>
 * Note that there is no treatment for the octave types "sparse matrix" 
 * representing doubles and "sparse complex matrix". 

 * <li>
 * {@link dk.ange.octave.io.impl.Uint8MatrixReader} 
 * for readingoctave (double) "uint8 matrix"s 
 * represented by {@link dk.ange.octave.type.OctaveInt}. 
 * There is no according writer. 
 * Also there are no reader/writer for other octave integer types: 
 * "uint8 scalar", and in both variants, scalar and matrix, 
 * and signed/unsigned "int16", "int32", "int64". 
 * Note that java has no type corresponding with "uint64". 





 * <li>
 * Both, (real) "diagonal matrix" and "complex diagonal matrix" 
 * reader/writer are missing. 
 * <li>
 * There is no support for octave type "octave_java"


 * <li>
 * There is no support for octave types "class", "cs-list", "magic-colon", 
 * "lazy_index" "onCleanup", "object". 
 * <li>
 * There is no support for octave "float" types: 
 * neither scalar, nor matrix, neither real nor complex, 
 * and not for diagonal matrices. 
 * <li>
 * There is no support for octave "permutation matrix". 
 * <li>
 * There is no support for octave 
 * "null_matrix", "null_string" nor "null_sq_string". 
 * <li>
 * Of course, there cannot be a reader/writer for octave's unknown type. 

 * </ul>
 */
package dk.ange.octave.io.impl;

import dk.ange.octave.io.impl.BooleanReader;
import dk.ange.octave.io.impl.BooleanWriter;
import dk.ange.octave.io.impl.*;
