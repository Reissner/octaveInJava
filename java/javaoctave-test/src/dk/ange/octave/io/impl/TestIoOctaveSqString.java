/*
 * Copyright 2007, 2008, 2009 Ange Optimization ApS
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
package dk.ange.octave.io.impl;

import junit.framework.TestCase;
import dk.ange.octave.OctaveEngine;
import dk.ange.octave.OctaveEngineFactory;
import dk.ange.octave.exception.OctaveParseException;
import dk.ange.octave.exception.OctaveRecoverableException;
import dk.ange.octave.type.OctaveObject;
import dk.ange.octave.type.OctaveScalar;
import dk.ange.octave.type.OctaveString;

/**
 * Test reading of sq_string (can not be written)
 */
public class TestIoOctaveSqString extends TestCase {

    /** Test */
    public void testEquals() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("st='tekst';");
        final OctaveObject s2 = octave.get("st");
        final OctaveObject s1 = new OctaveString("tekst");
        assertEquals(s1, s2);
        octave.close();
    }

    /**
     * Test that getting a string containing \ will throw a parse exception but that octave still will work after that
     */
    public void testUnimplementedEscapeChar() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.put("x", new OctaveScalar(1));
        octave.eval("st='\\\\';");
        assertEquals(1, octave.get("x", OctaveScalar.class).getDouble(), 0);
        try {
            octave.get("st");
            fail();
        } catch (final OctaveParseException e) {
            assertTrue(OctaveRecoverableException.class.isInstance(e));
        }
        assertEquals(1, octave.get("x", OctaveScalar.class).getDouble(), 0);
        octave.close();
    }

}
