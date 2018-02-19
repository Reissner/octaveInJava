/*
 * Copyright 2010 Ange Optimization ApS
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

import eu.simuline.octave.OctaveEngine;
import eu.simuline.octave.OctaveEngineFactory;
import eu.simuline.octave.type.OctaveInt;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test read/write of {@link OctaveInt}
 */
public class TestIoOctaveInt {

    /**
     * Test that data read from Octave is what we expect it to be
     */
    @Test public void testRead() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();

        final OctaveInt x = new OctaveInt(0, 0);
        octave.eval("x = uint8(zeros(0,0));");
        assertEquals(x, octave.get("x"));

        x.set(1, 1, 1);
        octave.eval("x(1, 1) = 1;");
        // assertEquals(x, octave.get("x")); Unknown octave type, type='uint8 scalar'

        x.set(2, 3, 1);
        octave.eval("x(3, 1) = 2;");
        assertEquals(x, octave.get("x"));

        x.set(3, 2, 2);
        octave.eval("x(2, 2) = 3;");
        assertEquals(x, octave.get("x"));

        final OctaveInt x3 = new OctaveInt(0, 0, 0);
        x3.set(222, 2, 2, 2);
        octave.eval("x3 = uint8(zeros(0,0,0));");
        octave.eval("x3(2, 2, 2) = 222;");
        assertEquals(x3, octave.get("x3"));

        octave.close();
    }

}
