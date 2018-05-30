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
 * specified in {@link eu.simuline.octave.io.spi}; 
 * one for each octave type given in octave via <code>typeinfo</code>. 
 * The reader classes are in 
 * <code>META-INF/services/eu.simuline.octave.io.OctaveDataReader</code> 
 * whereas the writer classes are in 
 * <code>META-INF/services/eu.simuline.octave.io.OctaveDataWriter</code>. 

 * Consequently, the classes are given by the following table 
 * <table>
 * <thead><tr align='left'>
 *   <th>index</th>
 *   <th>octave class</th>
 *   <th>java class</th>
 *   <th>reader</th>
 *   <th>writer</th>
 * </tr></thead>
 * <tfoot align='left'>

 *   <tr>
 *   <th>1</th>
 *   <th>&lt;unknown type></th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>2</th>
 *   <th>"cell"</th>
 *   <th>{@link eu.simuline.octave.type.OctaveCell}</th>
 *   <th>{@link eu.simuline.octave.io.impl.CellReader}</th>
 *   <th>{@link eu.simuline.octave.io.impl.CellWriter}</th>
 *   </tr>

 *   <tr>
 *   <th>3</th>
 *   <th>"scalar"</th>
 *   <th>see 5</th>
 *   <th>{@link eu.simuline.octave.io.impl.ScalarReader}</th>
 *   <th>see 5</th>
 *   </tr>

 *   <tr>
 *   <th>4</th>
 *   <th>"complex scalar"</th>
 *   <th>see 7</th>
 *   <th>{@link eu.simuline.octave.io.impl.ComplexScalarReader}</th>
 *   <th>see 7</th>
 *   </tr>

 *   <tr>
 *   <th>5</th>
 *   <th>"matrix"</th>
 *   <th>{@link eu.simuline.octave.type.OctaveDouble}</th>
 *   <th>{@link eu.simuline.octave.io.impl.MatrixReader}</th>
 *   <th>{@link eu.simuline.octave.io.impl.MatrixWriter}</th>
 *   </tr>

 *   <tr>
 *   <th>6</th>
 *   <th>"diagonal matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>7</th>
 *   <th>"complex matrix"</th>
 *   <th>{@link eu.simuline.octave.type.OctaveComplex}</th>
 *   <th>{@link eu.simuline.octave.io.impl.ComplexMatrixReader}</th>
 *   <th>???</th>
 *   </tr>

 *   <tr>
 *   <th>8</th>
 *   <th>"complex diagonal matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>9</th>
 *   <th>"range"</th>
 *   <th>OctaveFake</th>
 *   <th>FakeRangeReader</th>
 *   <th>FakeWriter</th>
 *   </tr>

 *   <tr>
 *   <th>10</th>
 *   <th>"bool"</th>
 *   <th>---</th>
 *   <th>{@link eu.simuline.octave.io.impl.BooleanSingleReader}</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>11</th>
 *   <th>"bool matrix"</th>
 *   <th>{@link eu.simuline.octave.type.OctaveBoolean}</th>
 *   <th>{@link eu.simuline.octave.io.impl.BooleanReader}</th>
 *   <th>{@link eu.simuline.octave.io.impl.BooleanWriter}</th> 
 *   </tr>

 *   <tr>
 *   <th>12</th>
 *   <th>"string"</th>
 *   <th>{@link eu.simuline.octave.type.OctaveString}</th>
 *   <th>{@link eu.simuline.octave.io.impl.OctaveStringReader}</th>
 *   <th>{@link eu.simuline.octave.io.impl.OctaveStringWriter}/---</th>
 *   </tr>

 *   <tr>
 *   <th>13</th>
 *   <th>"sq_string"</th>
 *   <th>see 12</th>
 *   <th>{@link eu.simuline.octave.io.impl.OctaveSqStringReader}</th>
 *   <th>{@link eu.simuline.octave.io.impl.OctaveStringWriter}/---</th>
 *   </tr>

 *   <tr>
 *   <th>14</th>
 *   <th>"int8 scalar"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>15</th>
 *   <th>"int16 scalar"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>16</th>
 *   <th>"int32 scalar"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>17</th>
 *   <th>"int64 scalar"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>18</th>
 *   <th>"uint8 scalar"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>19</th>
 *   <th>"uint16 scalar"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>20</th>
 *   <th>"uint32 scalar"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>21</th>
 *   <th>"uint64 scalar"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>



 *   <tr>
 *   <th>22</th>
 *   <th>"int8 matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>23</th>
 *   <th>"int16 matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>24</th>
 *   <th>"int32 matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>25</th>
 *   <th>"int64 matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>26</th>
 *   <th>"uint8 matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>26</th>
 *   <th>"uint8 matrix" Uint??</th>
 *   <th>{@link eu.simuline.octave.type.OctaveInt}</th>
 *   <th>{@link eu.simuline.octave.io.impl.Uint8MatrixReader}</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>27</th>
 *   <th>"uint16 matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>28</th>
 *   <th>"uint32 matrix"</th>
 *   <th>{@link eu.simuline.octave.type.OctaveInt}</th>
 *   <th>{@link eu.simuline.octave.io.impl.Int32MatrixReader}</th>
 *   <th>{@link eu.simuline.octave.io.impl.Int32MatrixWriter}</th>
 *   </tr>

 *   <tr>
 *   <th>29</th>
 *   <th>"uint64 matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>




 *   <tr>
 *   <th>30</th>
 *   <th>"sparse bool matrix"</th>
 *   <th>{@link eu.simuline.octave.type.OctaveSparseBoolean}</th>
 *   <th>{@link eu.simuline.octave.io.impl.SparseBooleanReader}</th>
 *   <th>{@link eu.simuline.octave.io.impl.SparseBooleanWriter}</th>
 *   </tr>

 *   <tr>
 *   <th>31</th>
 *   <th>"sparse matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>32</th>
 *   <th>"sparse complex matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>33</th>
 *   <th>"struct"</th>
 *   <th>{@link eu.simuline.octave.type.OctaveStruct}</th>
 *   <th>{@link eu.simuline.octave.io.impl.StructReader}</th>
 *   <th>{@link eu.simuline.octave.io.impl.StructWriter}</th>
 *   </tr>

 *   <tr>
 *   <th>34</th>
 *   <th>"scalar struct"</th>
 *   <th>{@link eu.simuline.octave.type.OctaveStruct}</th>
 *   <th>{@link eu.simuline.octave.io.impl.ScalarStructReader}</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>35</th>
 *   <th>"class"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>36</th>
 *   <th>"cs-list"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>37</th>
 *   <th>"magic-colon"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>38</th>
 *   <th>"built-in function"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>39</th>
 *   <th>"user-defined function"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>40</th>
 *   <th>"dynamically-linked function"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>41</th>
 *   <th>"function handle"</th>
 *   <th>{@link eu.simuline.octave.type.OctaveFunctionHandle}</th>
 *   <th>????</th>
 *   <th>{@link eu.simuline.octave.io.impl.FunctionHandleWriter}</th>
 *   </tr>

 *   <tr>
 *   <th>42</th>
 *   <th>"inline function" (which will be deprecated)</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>


 *   <tr>
 *   <th>43</th>
 *   <th>"float scalar"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>44</th>
 *   <th>"float complex scalar"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>45</th>
 *   <th>"float matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>46</th>
 *   <th>"float diagonal matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>47</th>
 *   <th>"float complex matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>48</th>
 *   <th>"float complex diagonal matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>




 
 *   <tr>
 *   <th>49</th>
 *   <th>"permutation matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>50</th>
 *   <th>"null_matrix"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>51</th>
 *   <th>"null_string"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>52</th>
 *   <th>"null_sq-string"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>53</th>
 *   <th>"lazy_index"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>54</th>
 *   <th>"onCleanup"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>55</th>
 *   <th>"octave_java"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>

 *   <tr>
 *   <th>56</th>
 *   <th>"object"</th>
 *   <th>---</th>
 *   <th>---</th>
 *   <th>---</th>
 *   </tr>



 * </tfoot>
 * </table>
 * does not take fake into account (see below). 
 * does not take integer types into account (see below). 

 * <p>
 * There three categories of basic types in octave: 
 * boolean, floating point and integer. 
 * <ul>
 * <li>
 * <code>bool</code> is the boolean (scalar) type, 
 * <li>
 * <code>double</code> and <code>float</code> 
 * are the floating point (scalar) types, 
 * <li>
 * <code>int8</code>, <code>int16</code>, <code>int32</code> 
 * and <code>int64</code>, are the signed (scalar) integer types and 
 * <code>uint8</code>, <code>uint16</code>, <code>uint32</code> 
 * and <code>uint64</code>, are the according unsigned (scalar) integer types. 
 * </ul>
 * <p>
 * Principally, all these types may be vector valued, matrix valued 
 * or even in higher dimensions. 
 * To express this, they are endowed with the suffix <code>matrix</code> 
 * even for higher dimensions 
 * and are thus called <code>bool matrix</code>, <code>float matrix</code>, 
 * <code>int8 matrix</code>, <code>int16 matrix</code>, 
 * <code>int32 matrix</code> and <code>int64 matrix</code> 
 * and accordingly for the unsigned integers. 
 * Since double is the standard type occurring most frequently,
 * what should be called <code>double matrix</code> 
 * is just called <code>matrix</code>. 
 * <p>
 * Likewise, if endowed with the suffix <code>scalar</code>, 
 * only scalars, i.e. 1x1 matrices are allowed. 
 * This type is to spare memory, i.e. the format information. 
 * likewise, 
 * <code>double scalar</code> is just called <code>scalar</code> and 
 * <code>bool scalar</code> is just called <code>bool</code>, 
 * which seems strange but comes from the fact, 
 * that bools mostly come as scalars. 
 * The other names are regular, 
 * as <code>int8 scalar</code>, <code>int16 scalar</code>, 
 * <code>int32 scalar</code> and <code>int64 scalar</code>. 
 * Although strutcs are not atomic but as the name says structures, 
 * they can in general be array valued. 
 * If not, it is a <code>scalar struct</code>, 
 * in the general case it is just a <code>struct</code> 
 * Note that this is the only case where the text <code>scalar </code> 
 * is prefixed (since <code>scalar</code> itself 
 * is short for <code>double scalar</code>). 
 * <p>
 * For the moment, we do not treat complex types. 
 * <p>
 * Floating matrix types may be diagonal. 
 * Thus we have <code>double diagonal matrix</code> 
 * called just <code>diagonal matrix</code>, 
 * <code>float diagonal matrix</code>. 
 * <p>
 * Floating matrix types and also boolean matrices may be sparse. 
 * Thus we have <code>double sparse matrix</code> 
 * called just <code>sparse matrix</code>, 
 * but no <code>float sparse matrix</code>. 




 * <ul>
 * <li>
 * {@link eu.simuline.octave.io.impl.BooleanReader}/
 * {@link eu.simuline.octave.io.impl.BooleanWriter} 
 * for reading/writing octave (double) "bool matrix"s 
 * represented by {@link eu.simuline.octave.type.OctaveBoolean}. 
 * <p>
 * There is also a {@link eu.simuline.octave.io.impl.BooleanSingleReader} 
 * for reading octave (scalar) "bool"s 
 * whereas no BooleanSingleWriter is provided. 



 * <li>
 * {@link eu.simuline.octave.io.impl.CellReader}/ 
 * {@link eu.simuline.octave.io.impl.CellWriter} 
 * for reading/writing octave "cell"s 
 * represented by {@link eu.simuline.octave.type.OctaveCell}. 
 * <li>
 * {@link eu.simuline.octave.io.impl.MatrixReader}/ 
 * {@link eu.simuline.octave.io.impl.MatrixWriter} 
 * for reading/writing octave (double) "matrix"s 
 * represented by {@link eu.simuline.octave.type.OctaveDouble}. 
 * <p>
 * There is also a {@link eu.simuline.octave.io.impl.ScalarReader} 
 * for reading  octave (double) "scalar"s 
 * whereas no <code>ScalarWriter</code> is provided. 




 * <li>
 * {@link eu.simuline.octave.io.impl.ComplexMatrixReader}/ 
 * {@link eu.simuline.octave.io.impl.ComplexScalarReader} 
 * for reading octave (double) "complex matrix"s and "complex scalar"s 
 * both represented by {@link eu.simuline.octave.type.OctaveComplex}. 
 * <p>
 * Both <code>ComplexMatrixWriter</code> and <code>ComplexScalarWriter</code> 
 * are missing. 

 * <li>
 * {@link eu.simuline.octave.io.impl.FakeRangeReader} ***** and 
 * {@link eu.simuline.octave.io.impl.FakeWriter}... maybe for ranges. 
 * Seemingly, {@link eu.simuline.octave.io.impl.FakeRangeReader} 
 * is not fully functional. 
 * Both read/write {@link eu.simuline.octave.type.OctaveFake}; 
 * package {@link eu.simuline.octave.type} does not contain a class 
 * representing range explicitly. 

 * <li>
 * {@link eu.simuline.octave.io.impl.FunctionHandleWriter} 
 * for writing octave "function handle"s 
 * represented by {@link eu.simuline.octave.type.OctaveFunctionHandle}. 
 * CAUTION: there is no according reader. 
 * <p>
 * There is no support for octave types 
 * "built-in function", "user-defined function", "dynamically-linked function", 
 * "inline function" (which will be deprecated). 

 * <li>
 * {@link eu.simuline.octave.io.impl.OctaveStringReader}/ 
 * {@link eu.simuline.octave.io.impl.OctaveStringWriter} 
 * for reading/writing octave "string"s 
 * represented by {@link eu.simuline.octave.type.OctaveString}. 
 * CAUTION: 
 * There is also an {@link eu.simuline.octave.io.impl.OctaveSqStringReader}, 
 * which is not fully functional: 
 * It reads like {@link eu.simuline.octave.io.impl.OctaveStringReader} 
 * and performs an additional check to ensure that the octave "sq_string" 
 * can be in fact represented by an octave "string". 
 * Thus {@link eu.simuline.octave.io.impl.OctaveStringReader} 
 * and  {@link eu.simuline.octave.io.impl.OctaveSqStringReader} 
 * share the same writer {@link eu.simuline.octave.io.impl.OctaveStringWriter}. 

 * <li>
 * {@link eu.simuline.octave.io.impl.StructReader}/ 
 * {@link eu.simuline.octave.io.impl.StructWriter} 
 * for reading/writing octave "struct"s 
 * represented by {@link eu.simuline.octave.type.OctaveStruct}. 
 * <p>
 * There is also a {@link eu.simuline.octave.io.impl.ScalarStructReader} 
 * for reading octave "scalar struct"s (which is optimized for 1x1 structs) 
 * also into {@link eu.simuline.octave.type.OctaveStruct}. 
 * There is no according <code>ScalarStructWriter</code>. 


 * <li>
 * {@link eu.simuline.octave.io.impl.SparseBooleanReader}/ 
 * {@link eu.simuline.octave.io.impl.SparseBooleanWriter} 
 * for reading/writing octave "sparse bool matrix"s 
 * represented by {@link eu.simuline.octave.type.OctaveSparseBoolean}. 
 * <p>
 * Note that there is no treatment for the octave types "sparse matrix" 
 * representing doubles and "sparse complex matrix". 

 * <li>
 * {@link eu.simuline.octave.io.impl.Uint8MatrixReader} 
 * for readingoctave (double) "uint8 matrix"s 
 * represented by {@link eu.simuline.octave.type.OctaveInt}. 
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
package eu.simuline.octave.io.impl;
