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
 * Classes related with executing octave. 
 * The central class is {@link eu.simuline.octave.exec.OctaveExec} 
 * executing octave. 
 * Class {@link eu.simuline.octave.exec.OctaveExecuteReader} 
 * reads the output of the octave process, 
 * with end of stream given by a "spacer". 
 * <p>
 * The basic interfaces are {@link eu.simuline.octave.exec.ReadFunctor} 
 * and {@link eu.simuline.octave.exec.WriteFunctor} 
 * reading from a reader and writing to a writer, respectively. 
 * The sole implementations <em>in this package</em> are 
 * {@link eu.simuline.octave.exec.ReaderWriteFunctor} 
 * which is a WriteFunctor writing to a writer 
 * what is read from a wrapped reader 
 * and {@link eu.simuline.octave.exec.WriterReadFunctor} 
 * which is a WriteFunctor reading from a reader 
 * and writing to a wrapped writer. 
 * <p>
 * Note that <code>eu.simuline.octave.io.DataReadFunctor</code> 
 * is the other implementation of {@link eu.simuline.octave.exec.ReadFunctor} 
 * and <code>eu.simuline.octave.io.DataWriteFunctor</code> 
 * is the other implementation of {@link eu.simuline.octave.exec.WriteFunctor} 
 * the two reading and writing octave objects. 
 * <p>
 * The classes 
 * {@link eu.simuline.octave.exec.OctaveWriterCallable} and 
 * {@link eu.simuline.octave.exec.OctaveReaderCallable} are required in 
 * {@link eu.simuline.octave.exec.OctaveExec#evalRW(WriteFunctor, ReadFunctor)} 
 * only. 
 * The first writes a command to octave, 
 * whereas the second one reads the result back. 
 */
package eu.simuline.octave.exec;

//import dk.ange.octave.exec.OctaveExec;
//import dk.ange.octave.exec.OctaveExecuteReader;
