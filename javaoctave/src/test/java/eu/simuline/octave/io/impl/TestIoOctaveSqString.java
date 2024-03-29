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
package eu.simuline.octave.io.impl;

import eu.simuline.octave.OctaveEngine;
import eu.simuline.octave.OctaveEngineFactory;
import eu.simuline.octave.exception.OctaveParseException;
import eu.simuline.octave.exception.OctaveRecoverableException;
import eu.simuline.octave.type.OctaveString;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test reading of sq_string (can not be written)
 */
public class TestIoOctaveSqString {

    /**
     * Test that string read from octave is what we expect
     */
    @Test public void testOctaveRead() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        read(octave, "sample text", "sample text");
        read(octave, "", "");
        try { // not implemented yet, but does not break the engine
            read(octave, "a\\nb", "a\nb");
            fail();
        } catch (final OctaveParseException e) {
            assertEquals("Handling of escape char (\\) not done, line='a\\nb'",
			 e.getMessage());
            assertTrue(OctaveRecoverableException.class.isInstance(e));
        }
        // read(octave, "a\\tb", "a\tb"); same as \n example
        octave.close();
    }

    private static void read(final OctaveEngine octave, 
			     final String input, 
			     final String expected) {
        final String key = "octave_string";
        octave.eval(key + " = '" + input + "';");
        final OctaveString output = octave.get(OctaveString.class, key);
        assertEquals(expected, output.getString());
    }

}
