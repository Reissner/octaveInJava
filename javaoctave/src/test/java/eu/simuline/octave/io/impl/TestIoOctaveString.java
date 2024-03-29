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

import java.io.BufferedReader;
import java.io.StringReader;

import eu.simuline.octave.OctaveEngine;
import eu.simuline.octave.OctaveEngineFactory;
import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.type.OctaveObject;
import eu.simuline.octave.type.OctaveString;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test read/write of {@link OctaveString}
 */
public class TestIoOctaveString {

    /**
     * Test format of generated octave data
     */
    @Test public void testToString() {
        final OctaveObject string = new OctaveString("tekst");
        assertEquals("# name: ans\n" + 
		     "# type: string\n" + 
		     "# elements: 1\n" + 
		     "# length: 5\ntekst\n", 
		     OctaveIO.toText(string));
    }

    /**
     * Test format of generated octave data
     */
    @Test public void testToOctave() {
        final OctaveObject string = new OctaveString("mytekst");
        assertEquals("# name: tre\n" + 
		     "# type: string\n" + 
		     "# elements: 1\n" + 
		     "# length: 7\nmytekst\n", 
		     OctaveIO.toText("tre", string));
    }

    /**
     * Test that string put into octave is the same as we get back
     */
    @Test public void testOctaveRoundtrip() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        roundtrip(octave, new OctaveString("sample text"));
        roundtrip(octave, new OctaveString(""));
        roundtrip(octave, new OctaveString("a\nb"));
        roundtrip(octave, new OctaveString("a\tb"));
        octave.close();
    }

    private static void roundtrip(final OctaveEngine octave, 
				  final OctaveObject octaveObject) {
        final String key = "octave_string";
        octave.put(key, octaveObject);
        final OctaveObject octaveObject2 = octave.get(key);
        assertEquals(octaveObject, octaveObject2);
    }

    /**
     * Test that string read from octave is what we expect
     */
    @Test public void testOctaveRead() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        read(octave, "sample text", "sample text");
        read(octave, "", "");
        read(octave, "a\\nb", "a\nb");
        read(octave, "a\\tb", "a\tb");
        octave.close();
    }

    private static void read(final OctaveEngine octave, 
			     final String input, 
			     final String expected) {
        final String key = "octave_string";
        octave.eval(key + " = \"" + input + "\";");
        final OctaveString output = octave.get(OctaveString.class, key);
        assertEquals(expected, output.getString());
    }

    /**
     * @throws Exception
     */
    @Test public void testWriteRead() throws Exception {
        final OctaveString string = new OctaveString("TEST");

        final String text = OctaveIO.toText(string);
        final BufferedReader bufferedReader = 
	    new BufferedReader(new StringReader(text));

        assertEquals("# name: ans", bufferedReader.readLine());
        assertEquals(string, OctaveIO.read(bufferedReader));
        assertEquals(-1, bufferedReader.read()); // Check end of file
    }

}
