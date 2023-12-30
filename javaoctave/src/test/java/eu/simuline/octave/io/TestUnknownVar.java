/*
 * Copyright 2007, 2008 Ange Optimization ApS
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
package eu.simuline.octave.io;

import eu.simuline.octave.OctaveEngine;
import eu.simuline.octave.OctaveEngineFactory;
import eu.simuline.octave.type.Octave;
import eu.simuline.octave.type.OctaveDouble;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Tests */
public class TestUnknownVar {

    /**
     * Test Octave.get() on unknown var
     */
    @Test public void testGetUnknownVar() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        final OctaveDouble x1 = octave.get(OctaveDouble.class, "x");
        assertNull(x1);
        final OctaveDouble x = Octave.scalar(42);
        octave.put("x", x);
        final OctaveDouble x2 = octave.get(OctaveDouble.class, "x");
        assertEquals(x, x2);
        octave.close();
    }

}
