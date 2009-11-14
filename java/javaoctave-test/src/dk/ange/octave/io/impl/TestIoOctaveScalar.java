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

import java.io.StringWriter;

import junit.framework.Assert;
import junit.framework.TestCase;
import dk.ange.octave.OctaveEngine;
import dk.ange.octave.OctaveEngineFactory;
import dk.ange.octave.io.OctaveIO;
import dk.ange.octave.type.Octave;
import dk.ange.octave.type.OctaveDouble;
import dk.ange.octave.type.OctaveObject;
import dk.ange.octave.type.OctaveStruct;

/**
 * Test read/write of {@link OctaveDouble}
 * 
 * FIXME merge with TestIoOctaveDouble
 */
public class TestIoOctaveScalar extends TestCase {

    /**
     * @throws Exception
     */
    public void testToString() throws Exception {
        final OctaveObject integer = Octave.scalar(42);
        Assert.assertEquals("# name: ans\n# type: scalar\n42.0\n\n", OctaveIO.toText(integer));
    }

    /**
     * @throws Exception
     */
    public void testToOctave() throws Exception {
        final OctaveObject integer = Octave.scalar(43);
        Assert.assertEquals("# name: tre\n# type: scalar\n43.0\n\n", OctaveIO.toText(integer, "tre"));
    }

    /**
     * @throws Exception
     */
    public void testOctaveConnection() throws Exception {
        final OctaveObject i1 = Octave.scalar(42);
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.put("i", i1);
        final OctaveDouble i2 = octave.get("i");
        Assert.assertEquals(i1, i2);
        octave.close();
    }

    /**
     * Test how the system handles save of Inf and NaN
     * 
     * @throws Exception
     */
    public void testSaveNanInf() throws Exception {
        final StringWriter stderr = new StringWriter();
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.setErrorWriter(stderr);

        octave.eval("ok=1;");
        final OctaveDouble okOne = Octave.scalar(1);
        OctaveDouble ok;

        octave.eval("xnan=NaN;");
        ok = octave.get("ok");
        assertEquals(okOne, ok);
        final OctaveDouble xnan = octave.get("xnan");
        assertEquals(Double.NaN, xnan.get(1, 1));
        ok = octave.get("ok");
        assertEquals(okOne, ok);

        octave.eval("xinf=Inf;");
        ok = octave.get("ok");
        assertEquals(okOne, ok);
        final OctaveDouble xinf = octave.get("xinf");
        assertEquals(Double.POSITIVE_INFINITY, xinf.get(1, 1));
        ok = octave.get("ok");
        assertEquals(okOne, ok);

        octave.eval("xninf=-Inf;");
        ok = octave.get("ok");
        assertEquals(okOne, ok);
        final OctaveDouble xninf = octave.get("xninf");
        assertEquals(Double.NEGATIVE_INFINITY, xninf.get(1, 1));
        ok = octave.get("ok");
        assertEquals(okOne, ok);

        octave.close();
        stderr.close();
        assertEquals("", stderr.toString());
    }

    /** Test that we can get and set globals */
    public void testGlobal() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();

        octave.eval("global x");
        octave.put("x", Octave.scalar(42.0));

        final OctaveDouble x = octave.get("x");
        assertEquals(42.0, x.get(1));

        octave.close();
    }

    /**
     * Test
     */
    public void testScalar() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("s.one=1;");
        octave.eval("s.two=[2 3];");

        final OctaveStruct cell = octave.get("s");

        final OctaveDouble two = cell.get("two", OctaveDouble.class);
        assertEquals(1, two.size(1));
        assertEquals(2, two.size(2));
        assertEquals(2d, two.get(1, 1));
        assertEquals(3d, two.get(1, 2));

        final OctaveDouble one = cell.get("one", OctaveDouble.class);
        assertEquals(1, one.size(1));
        assertEquals(1, one.size(2));
        assertEquals(1d, one.get(1, 1));

        octave.close();
    }

}
