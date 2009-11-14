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
package dk.ange.octave;

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;
import dk.ange.octave.exception.OctaveException;
import dk.ange.octave.exception.OctaveParseException;
import dk.ange.octave.exception.OctaveRecoverableException;
import dk.ange.octave.type.Octave;
import dk.ange.octave.type.OctaveDouble;

/**
 * Test
 */
public class TestOctaveErrors extends TestCase {

    /**
     * @throws IOException
     */
    public void testError() throws IOException {
        final StringWriter stdout = new StringWriter();
        final StringWriter stderr = new StringWriter();
        try {
            final OctaveEngineFactory octaveEngineFactory = new OctaveEngineFactory();
            octaveEngineFactory.setErrorWriter(stderr);
            final OctaveEngine octave = octaveEngineFactory.getScriptEngine();
            octave.setWriter(stdout);
            octave.eval("error('testError()');");
            fail("error in octave should cause execute() to throw an exception");
            octave.close();
        } catch (final OctaveException e) {
            // ok
        }
        stdout.close();
        stderr.close();
        assertEquals("", stdout.toString());
        // 2008-05-08: Does this still fail? I haven't seen this error for a long time, Kim
        assertEquals("This sometime fails, there is some timing problem that prevents all of stderr to get "
                + "from octave to Java when there is an error in octave.", "error: testError()\n", stderr.toString());
    }

    /** Test */
    public void testOk() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("ok=1;");
        octave.close();
    }

    /**
     * Test that when an unknown type is read the OctaveParseException is thrown and the system will still work
     */
    public void testParseException() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.put("x", Octave.scalar(1));
        octave.eval("y = uint16(42);");
        assertEquals(1, octave.get(OctaveDouble.class, "x").get(1, 1), 0);
        try {
            octave.get("y");
            fail();
        } catch (final OctaveParseException e) {
            assertTrue(OctaveRecoverableException.class.isInstance(e));
        }
        assertEquals(1, octave.get(OctaveDouble.class, "x").get(1, 1), 0);
        octave.close();
    }

}
