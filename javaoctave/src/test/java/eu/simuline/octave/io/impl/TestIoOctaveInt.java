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
import eu.simuline.octave.type.OctaveDouble;
import eu.simuline.octave.type.OctaveInt;
import eu.simuline.octave.type.OctaveObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.TreeMap;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test read/write of {@link OctaveInt}
 */
public class TestIoOctaveInt {

    /**
     * Test that data read from Octave is what we expect it to be
     */
    @Test public void testReadScalar() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();

        final OctaveInt x = new OctaveInt(0, 0);
        octave.eval("x = uint8(zeros(0,0));");
        assertEquals(x, octave.get("x"));

        x.set(1, 1, 1);
        octave.eval("x(1, 1) = 1;");
        assertEquals(x, octave.get("x"));
        

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
        final OctaveInt a  = new OctaveInt(1,1);
        final OctaveInt b  = new OctaveInt(1,1);
        final OctaveInt c  = new OctaveInt(1,1);
        a.set(3, 1, 1);
        b.set(4, 1, 1);
        c.set(4, 1, 1);
        
        octave.eval("a  = int8(3.2)");
        octave.eval("b  = int16(3.5)");
        octave.eval("c  = int32(3.6)");
        assertEquals(a, octave.get("a"));
        assertEquals(b, octave.get("b"));
        assertEquals(c, octave.get("c"));
        
        octave.eval("d  = uint8(3.2)");
        octave.eval("e  = uint16(3.5)");
        octave.eval("f  = uint32(3.6)");
        OctaveInt d = octave.get(OctaveInt.class, "d");
        OctaveInt g = octave.get(OctaveInt.class, "e");
        OctaveInt f = octave.get(OctaveInt.class, "f");
        assertEquals(d.get(1,1), 3);
        assertEquals(g.get(1,1), 4);
        assertEquals(f.get(1,1), 4);
        octave.close();
    }
    

    /** Test */
    @Test
    public void testOctaveNdMatrix() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        final TreeMap<String, OctaveObject> vars = 
	    new TreeMap<String, OctaveObject>();
        final double[] bigdata = new double[2 * 3 * 4];
        for (int idx = 0; idx < bigdata.length; idx++) {
            bigdata[idx] = idx + 1.0;
        }
        final double[] data2d = {1.0, 2.0, 3.0, 5.0, 8.0, 13.0};
        final double[] datascalar = {42.0};
        vars.put("bigmatrix",    new OctaveDouble(bigdata, 1, 2, 3, 4));
        vars.put("matrix2d",     new OctaveDouble(data2d, 2, 3));
        vars.put("matrixscalar", new OctaveDouble(datascalar, 1, 1));
        vars.put("matrixzero",   new OctaveDouble(0, 0, 0, 0));
        vars.put("matrixzero2d", new OctaveDouble(0, 0));
        octave.putAll(vars);
        OctaveDouble matrixzero = octave.get(OctaveDouble.class, "matrixzero");
        OctaveDouble matrix2d = octave.get(OctaveDouble.class, "matrix2d");
        OctaveDouble bigmatrix = octave.get(OctaveDouble.class, "bigmatrix");
        OctaveDouble matrixzero2d = octave.get(OctaveDouble.class, "matrixzero2d");
        OctaveDouble matrixscalar = octave.get(OctaveDouble.class, "matrixscalar");
        assertEquals(matrixzero,   vars.get("matrixzero"));
        assertEquals(matrixzero2d, vars.get("matrixzero2d"));
        assertEquals(matrixscalar, vars.get("matrixscalar"));
        assertEquals(matrix2d,     vars.get("matrix2d"));
        assertEquals(bigmatrix,    vars.get("bigmatrix"));
        octave.close();
    }
}
