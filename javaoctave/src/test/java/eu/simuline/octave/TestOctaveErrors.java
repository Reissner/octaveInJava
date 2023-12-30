/*
 * Copyright 2007, 2008, 2009, 2010 Ange Optimization ApS
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
package eu.simuline.octave;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import eu.simuline.octave.exception.OctaveEvalException;
import eu.simuline.octave.exception.OctaveIOException;
import eu.simuline.octave.exception.OctaveNonrecoverableException;
import eu.simuline.octave.exception.OctaveParseException;
import eu.simuline.octave.exception.OctaveRecoverableException;
import eu.simuline.octave.type.Octave;
import eu.simuline.octave.type.OctaveDouble;
import eu.simuline.octave.type.OctaveString;

/**
 * Test
 */
public class TestOctaveErrors {

    /**
     * Test that error() in octave breaks the engine
     */
    @Test public void testError() {
        final StringWriter stdout = new StringWriter();
        final StringWriter stderr = new StringWriter();
        final OctaveEngineFactory octaveEngineFactory = 
	    new OctaveEngineFactory();
        octaveEngineFactory.setErrorWriter(stderr);
        final OctaveEngine octave = octaveEngineFactory.getScriptEngine();
        octave.setWriter(stdout);
        try {
            octave.unsafeEval("error('test usage of error');");
            fail("error in octave should cause execute() " + 
		 "to throw an exception");
        } catch (final OctaveIOException e) {
            assertTrue(OctaveNonrecoverableException.class.isInstance(e));
            assertTrue(!e.isDestroyed());
        }
        assertEquals("", stdout.toString());
        /*
         * 2008-05-08, Kim: Does this still fail? I haven't seen this error for a long time
         * 
         * 2010-01-10, Kim: I this this error is fixed. It used to fail because output to stderr from octave sometimes
         * wasn't flushed to Java.
         */
        assertEquals("error: test usage of error\n", stderr.toString());
        try {
            octave.put("x", Octave.scalar(42));
            fail("put should fail when the engine is broken");
        } catch (final OctaveIOException e) {
            assertTrue(OctaveNonrecoverableException.class.isInstance(e));
            assertTrue(!e.isDestroyed());
        }
        try {
            octave.close();
            fail("close should fail when the engine is broken");
        } catch (final OctaveIOException e) {
            assertTrue(OctaveNonrecoverableException.class.isInstance(e));
            assertTrue(!e.isDestroyed());
        }
        octave.destroy();
        try {
            octave.close();
            fail("close should fail when the engine is destroyed");
        } catch (final OctaveIOException e) {
            assertTrue(OctaveNonrecoverableException.class.isInstance(e));
            assertTrue(e.isDestroyed());
        }
    }

    /**
     * Test that error() in try/catch does not break the engine
     */
    @Test public void testEvalWithTryCatch() {
        final StringWriter stdout = new StringWriter();
        final StringWriter stderr = new StringWriter();
        final OctaveEngineFactory octaveEngineFactory = new OctaveEngineFactory();
        octaveEngineFactory.setErrorWriter(stderr);
        final OctaveEngine octave = octaveEngineFactory.getScriptEngine();
        octave.setWriter(stdout);
        octave.eval("" //
                + "try\n" //
                + "  error('test usage of error');\n" //
                + "catch\n" //
                + "  javaoctave_asdf_lasterr = lasterr();\n" //
                + "end_try_catch\n" //
                + "");
        assertEquals("", stdout.toString());
        assertEquals("", stderr.toString());
        final OctaveString lastError = octave.get(OctaveString.class, 
						  "javaoctave_asdf_lasterr");
        octave.eval("clear javaoctave_asdf_lasterr");
        assertTrue(lastError.getString().contains("test usage of error"));
        octave.put("x", Octave.scalar(42));
        octave.close();
        octave.destroy();
        try {
            octave.close();
            fail("close should fail when the engine is destroyed");
        } catch (final OctaveIOException e) {
            assertTrue(OctaveNonrecoverableException.class.isInstance(e));
            assertTrue(e.isDestroyed());
        }
    }

    /**
     * Test that shows that try/catch does prevent a syntax error 
     * from breaking the engine
     */
    @Test public void testSyntaxErrorInTryCatch() {
        final StringWriter stdout = new StringWriter();
        final StringWriter stderr = new StringWriter();
        final OctaveEngineFactory octaveEngineFactory = new OctaveEngineFactory();
        octaveEngineFactory.setErrorWriter(stderr);
        final OctaveEngine octave = octaveEngineFactory.getScriptEngine();
        octave.setWriter(stdout);
        try {
            octave.unsafeEval("" //
                    + "try\n" //
                    + "  x = linspace(0,6.3,10*);\n" //
                    + "catch\n" //
                    + "  javaoctave_asdf_lasterr = lasterr()\n" //
                    + "end_try_catch\n" //
                    + "");
            fail();
        } catch (final OctaveIOException e) {
            assertTrue(OctaveNonrecoverableException.class.isInstance(e));
            assertTrue(!e.isDestroyed());
        }
        assertEquals("", stdout.toString());
        assertTrue(stderr.toString().contains("syntax error"));
        try {
            octave.put("x", Octave.scalar(42));
            fail("put should fail when the engine is broken");
        } catch (final OctaveIOException e) {
            assertTrue(OctaveNonrecoverableException.class.isInstance(e));
            assertTrue(!e.isDestroyed());
        }
        try {
            octave.close();
            fail("close should fail when the engine is broken");
        } catch (final OctaveIOException e) {
            assertTrue(OctaveNonrecoverableException.class.isInstance(e));
            assertTrue(!e.isDestroyed());
        }
        octave.destroy();
        try {
            octave.close();
            fail("close should fail when the engine is destroyed");
        } catch (final OctaveIOException e) {
            assertTrue(OctaveNonrecoverableException.class.isInstance(e));
            assertTrue(e.isDestroyed());
        }
    }

    /**
     * Test that syntax error in eval() does not break the engine
     */
    @Test public void testSyntaxErrorInEval() {
        final StringWriter stdout = new StringWriter();
        final StringWriter stderr = new StringWriter();
        final OctaveEngineFactory octaveEngineFactory = new OctaveEngineFactory();
        octaveEngineFactory.setErrorWriter(stderr);
        final OctaveEngine octave = octaveEngineFactory.getScriptEngine();
        octave.setWriter(stdout);
        octave.put("javaoctave_asdf_eval", new OctaveString("x = linspace(0,6.3,10*);"));
        octave.eval("eval(javaoctave_asdf_eval, 'javaoctave_asdf_lasterr = lasterr();')");
        assertEquals("", stdout.toString());
        assertEquals("", stderr.toString());
        final OctaveString lastError = octave.get(OctaveString.class, "javaoctave_asdf_lasterr");
        octave.eval("clear javaoctave_asdf_eval javaoctave_asdf_lasterr");
        assertTrue(lastError.getString().contains("syntax error"));
        octave.put("x", Octave.scalar(42));
        octave.close();
        octave.destroy();
        try {
            octave.close();
            fail("close should fail when the engine is destroyed");
        } catch (final OctaveIOException e) {
            assertTrue(OctaveNonrecoverableException.class.isInstance(e));
            assertTrue(e.isDestroyed());
        }
    }

    /**
     * Test that syntax error in safeEval() does not break the engine
     */
    @Test public void testSyntaxErrorInSafeEval() {
        final StringWriter stdout = new StringWriter();
        final StringWriter stderr = new StringWriter();
        final OctaveEngineFactory octaveEngineFactory = new OctaveEngineFactory();
        octaveEngineFactory.setErrorWriter(stderr);
        final OctaveEngine octave = octaveEngineFactory.getScriptEngine();
        octave.setWriter(stdout);
        try {
            octave.eval("x = linspace(0,6.3,10*);");
        } catch (final OctaveEvalException e) {
            assertTrue(OctaveRecoverableException.class.isInstance(e));
            assertTrue(!e.isDestroyed());
            assertTrue(e.getMessage().contains("syntax error"));
        }
        assertEquals("", stdout.toString());
        assertEquals("", stderr.toString());
        octave.put("x", Octave.scalar(42));
        octave.close();
        octave.destroy();
        try {
            octave.close();
            fail("close should fail when the engine is destroyed");
        } catch (final OctaveIOException e) {
            assertTrue(OctaveNonrecoverableException.class.isInstance(e));
            assertTrue(e.isDestroyed());
        }
    }

    /** Test */
    @Test public void testOk() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("ok=1;");
        octave.close();
    }

    /**
     * Test that when an unknown type is read the OctaveParseException is thrown and the system will still work
     */
    @Test public void testParseException() {
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
