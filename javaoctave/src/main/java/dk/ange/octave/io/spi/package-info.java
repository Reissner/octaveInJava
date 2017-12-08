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
 * The {@link dk.ange.octave.io.spi} package 
 * contains service provider interfaces 
 * for reading and writing various kinds of 
 * {@link dk.ange.octave.type.OctaveObject}s. 
 * The idea is described in 
 * <a href="https://docs.oracle.com/javase/tutorial/ext/basics/spi.html"> 
 * the oracle tutorial</a> and 
 * this package is inspired and partially based 
 * on the package {@link javax.imageio.spi}. 
 * <p>
 * The two abstract classes provided are 
 * {@link dk.ange.octave.io.spi.OctaveDataReader} and 
 * {@link dk.ange.octave.io.spi.OctaveDataWriter}. 
 */
package dk.ange.octave.io.spi;
