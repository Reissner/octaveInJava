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
package eu.simuline.octave;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import eu.simuline.octave.exception.OctaveException;
import eu.simuline.octave.exception.OctaveIOException;
import eu.simuline.octave.type.Octave;
import eu.simuline.octave.type.OctaveDouble;
import eu.simuline.octave.type.OctaveObject;

/**
 * Tests eu.simuline.octave.Octave.*
 */
public class TestOctave {

    /**
     * @param args
     */
    public static void main(final String[] args) {
	org.junit.runner.JUnitCore.runClasses(TestOctave.class);
    }

    // Tests:

    /*
     * Octave() and Octave(Writer, Writer) is tested in setUp()
     */

    /*
     * close() is tested in tearDown()
     */

    /**
     * Test method for set(String,double), getScalar(), execute(String)
     * 
     * @throws Exception
     */
    @Test public void testExecute() throws Exception {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();

        final OctaveObject Y = Octave.scalar(2);
        octave.put("y", Y);
        final OctaveObject X = Octave.scalar(42);
        octave.put("x", X);
        final OctaveObject Z = Octave.scalar(4);
        octave.put("z", Z);

        OctaveDouble x = octave.get(OctaveDouble.class, "x");
        assertEquals(42.0, x.get(1, 1), 0.0);

        octave.eval("x = x + 10;");
        x = octave.get(OctaveDouble.class, "x");
        assertEquals(52.0, x.get(1, 1), 0.0);
        octave.close();
    }

    /**
     * Test method for reader=exec(reader)
     * 
     * @throws Exception
     */
    @Test public void testExec() throws Exception {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.put("x", Octave.scalar(42));
        octave.unsafeEval(new StringReader("x=x+10;"));
        final OctaveDouble octaveScalar = octave.get(OctaveDouble.class, "x");
        final double x = octaveScalar.get(1, 1);
        assertEquals(52.0, x, 0.0);
        octave.close();
    }

    /**
     * @throws Exception
     */
    // does something like 
    // Running eu.simuline.octave.TestOctave
    // 2017-11-28 23:52:09,016 [main-javaoctave-ReaderWriterPipeThread] 
    // ERROR eu.simuline.octave.util.ReaderWriterPipeThread 
    // (ReaderWriterPipeThread.java:81) - Error when reading from reader
    // java.io.IOException: Stream closed
    // at java.io.BufferedInputStream.getBufIfOpen(BufferedInputStream.java:170)
    // at java.io.BufferedInputStream.read1(BufferedInputStream.java:283)
    // at java.io.BufferedInputStream.read(BufferedInputStream.java:345)
    // at sun.nio.cs.StreamDecoder.readBytes(StreamDecoder.java:284)
    // at sun.nio.cs.StreamDecoder.implRead(StreamDecoder.java:326)
    // at sun.nio.cs.StreamDecoder.read(StreamDecoder.java:178)
    // at java.io.InputStreamReader.read(InputStreamReader.java:184)
    // at java.io.Reader.read(Reader.java:140)
    // at eu.simuline.octave.util.ReaderWriterPipeThread
    //                                      .run(ReaderWriterPipeThread.java:79)
    @Test public void testDestroy() throws Exception {
	final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
	octave.eval("sigterm_dumps_octave_core(0);");
	Thread thread = new DestroyThread(octave);

	// destroys itself in 1000 ms 
	thread.start();
	try {
	    // octave pauses for 10 sec and so thread is destroyed during pause 
	    octave.eval("pause(10);");
	    fail("Exception expected. ");
	} catch (final OctaveException oe) {
	    assertTrue(oe.isDestroyed());
	    System.out.println("Stacktrace above is intended: is by logging. ");
	    System.out.println("Message 'Uncaught:...' also. ");
	} catch (final Throwable th) {
	    assertTrue(false);
	}
    }

    /**
     * Helper for TestOctave
     * 
     * @author Kim Hansen
     */
    private static class DestroyThread extends Thread {
        private final OctaveEngine octave;

        private DestroyThread(final OctaveEngine octave) {
            this.octave = octave;
        }

        @Override
        public void run() {
            try {
                sleep(1000);
                octave.destroy();
            } catch (final Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Test advanced Constructor to Octave()
     * 
     * @throws Exception
     */
    @Test public void testConstructor() throws Exception {
        final OctaveEngineFactory octaveEngineFactory = 
	    new OctaveEngineFactory();
        octaveEngineFactory.setOctaveProgramFile(new File("octave"));
        final OctaveEngine octave = octaveEngineFactory.getScriptEngine();
        octave.setWriter(null);
        octave.eval("disp('testConstructor');");
        octave.close();
    }

    /**
     * Test if files are closed by the Octave object
     * 
     * @throws Exception
     */
    @Test public void testFileClose() throws Exception {
        final Writer stdin  = new DontCloseWriter("stdin");
        final Writer stdout = new DontCloseWriter("stdout");
        final Writer stderr = new DontCloseWriter("stderr");
        final OctaveEngineFactory octaveEngineFactory = 
	    new OctaveEngineFactory();
        octaveEngineFactory.setErrorWriter(stderr);
        octaveEngineFactory.setOctaveInputLog(stdin);
        final OctaveEngine octave = octaveEngineFactory.getScriptEngine();
        octave.setWriter(stdout);
        octave.eval("disp('testFileClose');");
        octave.close();

        final OctaveEngine octave2 = octaveEngineFactory.getScriptEngine();
        octave.setWriter(stdout);
        try {
            octave2.unsafeEval("error('testFileClose2');");
            fail();
        } catch (final OctaveException e) {
            assertTrue(e instanceof OctaveIOException);
        }
    }

    private static class DontCloseWriter extends Writer {
        private final String name;

        private DontCloseWriter(final String name) {
            this.name = name;
        }

        @Override
        public void write(final char[] cbuf, final int off, final int len) {
            // Don't do anything
        }

        @Override
        public void flush() {
            // Don't do anything
        }

        @Override
        public void close() throws IOException {
            throw new IOException("DontCloseWriter '" + name + "' closed.");
        }
    }

    /**
     * Test
     */
    @Test public void testOutputWithoutNewline() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        final StringWriter result = new StringWriter();
        octave.setWriter(result);
        octave.eval("printf('testOutputWithoutNewline1');");
        assertEquals("testOutputWithoutNewline1", result.toString());
        result.getBuffer().setLength(0);
        octave.eval("disp('testOutputWithoutNewline2');");
        assertEquals("testOutputWithoutNewline2\n", result.toString());
        result.getBuffer().setLength(0);
        octave.eval("printf('testOutput\\nWithoutNewline3');");
        assertEquals("testOutput\nWithoutNewline3", result.toString());
        result.getBuffer().setLength(0);
        octave.eval("disp('testOutput\\nWithoutNewline4');");
        assertEquals("testOutput\\nWithoutNewline4\n", result.toString());
        result.getBuffer().setLength(0);
        octave.eval("disp(\"testOutput\\nWithoutNewline5\");");
        assertEquals("testOutput\nWithoutNewline5\n", result.toString());
        result.getBuffer().setLength(0);
        octave.eval("'testOutputWithoutNewline6'");
        assertEquals("ans = testOutputWithoutNewline6\n", result.toString());
        octave.close();
    }

}
