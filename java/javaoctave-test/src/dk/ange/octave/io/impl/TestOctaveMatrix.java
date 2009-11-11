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
import java.util.TreeMap;

import junit.framework.TestCase;
import dk.ange.octave.OctaveEngine;
import dk.ange.octave.OctaveEngineFactory;
import dk.ange.octave.io.OctaveIO;
import dk.ange.octave.type.OctaveMatrix;
import dk.ange.octave.type.OctaveScalar;
import dk.ange.octave.type.OctaveObject;

/**
 * Test read/write of OctaveMatrix
 */
public class TestOctaveMatrix extends TestCase {

    /**
     * Test
     */
    public void testConstructorIntIntInt() {
        final OctaveMatrix matrix = new OctaveMatrix(3, 4, 2);
        assertEquals("" //
                + "# name: matrix3d\n" //
                + "# type: matrix\n" //
                + "# ndims: 3\n" //
                + " 3 4 2\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + "\n", OctaveIO.toText(matrix, "matrix3d"));
        matrix.set(42.0, 1, 3, 2);
        assertEquals("" //
                + "# name: matrix3d\n" //
                + "# type: matrix\n" //
                + "# ndims: 3\n" //
                + " 3 4 2\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + " 42.0\n 0.0\n 0.0\n" //
                + " 0.0\n 0.0\n 0.0\n" //
                + "\n", OctaveIO.toText(matrix, "matrix3d"));
    }

    /**
     * Test
     */
    public void testConstructor1() {
        final OctaveMatrix matrix = new OctaveMatrix(0, 0, 0);
        assertEquals("# name: matrix3d\n# type: matrix\n# ndims: 3\n 0 0 0\n\n", OctaveIO.toText(matrix, "matrix3d"));
    }

    /**
     * @throws Exception
     */
    public void testConstructor2() throws Exception {
        final OctaveMatrix matrix = new OctaveMatrix(0, 0);
        assertEquals(0, matrix.size(1));
        assertEquals(0, matrix.size(2));
        assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 0\n" + //
                "# columns: 0\n" + //
                "\n" //
        , OctaveIO.toText(matrix, "matrix"));
    }

    /**
     * @throws Exception
     */
    public void testConstructorMatrix() throws Exception {
        final double[] numbers = { 1, 2, 3, 4, 5, 6 };
        final OctaveMatrix matrix = new OctaveMatrix(numbers, 2, 3);
        assertEquals(2, matrix.size(1));
        assertEquals(3, matrix.size(2));
        assertEquals("" + //
                "# name: mymatrix\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 3\n" + //
                " 1.0 3.0 5.0\n" + //
                " 2.0 4.0 6.0\n\n" //
        , OctaveIO.toText(matrix, "mymatrix"));
    }

    /**
     * @throws Exception
     */
    public void testConstructorIntInt() throws Exception {
        final OctaveMatrix matrix = new OctaveMatrix(2, 3);
        assertEquals(2, matrix.size(1));
        assertEquals(3, matrix.size(2));
        assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 3\n" + //
                " 0.0 0.0 0.0\n" + //
                " 0.0 0.0 0.0\n\n" //
        , OctaveIO.toText(matrix, "matrix"));
        matrix.set(42, 1, 2);
        assertEquals("" + //
                "# name: myother\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 3\n" + //
                " 0.0 42.0 0.0\n" + //
                " 0.0 0.0 0.0\n\n" //
        , OctaveIO.toText(matrix, "myother"));
        matrix.set(2, 2, 1);
        assertEquals("" + //
                "# name: myother\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 3\n" + //
                " 0.0 42.0 0.0\n" + //
                " 2.0 0.0 0.0\n\n" //
        , OctaveIO.toText(matrix, "myother"));
        matrix.set(4.0, 2, 2);
        assertEquals("" + //
                "# name: myother\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 3\n" + //
                " 0.0 42.0 0.0\n" + //
                " 2.0 4.0 0.0\n\n" //
        , OctaveIO.toText(matrix, "myother"));
    }

    /**
     * @throws Exception
     */
    public void testGrowth() throws Exception {
        final OctaveMatrix matrix = new OctaveMatrix(0, 0);
        assertEquals(0, matrix.size(1));
        assertEquals(0, matrix.size(2));
        assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 0\n" + //
                "# columns: 0\n\n" //
        , OctaveIO.toText(matrix, "matrix"));
        matrix.set(1, 1, 1);
        assertEquals(1, matrix.size(1));
        assertEquals(1, matrix.size(2));
        assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 1\n" + //
                "# columns: 1\n" + //
                " 1.0\n\n" //
        , OctaveIO.toText(matrix, "matrix"));
        matrix.set(3, 3, 1);
        assertEquals(3, matrix.size(1));
        assertEquals(1, matrix.size(2));
        assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 3\n" + //
                "# columns: 1\n" + //
                " 1.0\n" + //
                " 0.0\n" + //
                " 3.0\n\n" //
        , OctaveIO.toText(matrix, "matrix"));

        final OctaveMatrix matrix2 = new OctaveMatrix(0, 0);
        matrix2.set(3.0, 1, 3);
        assertEquals("" + //
                "# name: matrix\n" + //
                "# type: matrix\n" + //
                "# rows: 1\n" + //
                "# columns: 3\n" + //
                " 0.0 0.0 3.0\n" + //
                "\n" //
        , OctaveIO.toText(matrix2, "matrix"));
    }

    /**
     * @throws Exception
     */
    public void testOctaveGet() throws Exception {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("m=[1 2;3 4];");
        final OctaveMatrix m = octave.get("m");
        assertEquals("" + //
                "# name: m\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 2\n" + //
                " 1.0 2.0\n" + //
                " 3.0 4.0\n" + //
                "\n" //
        , OctaveIO.toText(m, "m"));
        octave.close();
    }

    /**
     * @throws Exception
     */
    public void testOctaveSetExecGet() throws Exception {
        final double[] numbers = { 1, 2, 3, 4, 5, 6 };
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        final OctaveMatrix in = new OctaveMatrix(numbers, 2, 3);
        octave.put("in", in);
        octave.eval("out=in;");
        final OctaveMatrix out = octave.get("out");
        assertEquals(OctaveIO.toText(in), OctaveIO.toText(out));
        octave.eval("slicerow=in(2,:); slicecol=in(:,2);");
        final OctaveMatrix slicerow = octave.get("slicerow");
        final OctaveMatrix slicecol = octave.get("slicecol");
        assertEquals(2.0, slicerow.get(1, 1));
        assertEquals(4.0, slicerow.get(1, 2));
        assertEquals(6.0, slicerow.get(1, 3));
        assertEquals(3.0, slicecol.get(1, 1));
        assertEquals(4.0, slicecol.get(2, 1));
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
        final OctaveScalar okOne = new OctaveScalar(1);
        OctaveScalar ok;

        octave.eval("xnan=[NaN 0];");
        ok = octave.get("ok");
        assertEquals(okOne, ok);
        final OctaveMatrix xnan = octave.get("xnan");
        assertEquals(Double.NaN, xnan.get(1, 1));
        assertEquals(Double.valueOf(0), xnan.get(1, 2));
        ok = octave.get("ok");
        assertEquals(okOne, ok);

        octave.eval("xinf=[Inf -Inf];");
        ok = octave.get("ok");
        assertEquals(okOne, ok);
        final OctaveMatrix xinf = octave.get("xinf");
        assertEquals(Double.POSITIVE_INFINITY, xinf.get(1, 1));
        assertEquals(Double.NEGATIVE_INFINITY, xinf.get(1, 2));
        ok = octave.get("ok");
        assertEquals(okOne, ok);

        octave.close();
        stderr.close();
        assertEquals("", stderr.toString()); // No warning when saving matrix with NaN/Inf
    }

    /**
     * Test
     */
    public void test3dMatrix() {
        final OctaveMatrix matrix3d = new OctaveMatrix(3, 4, 2);
        matrix3d.set(42.0, 1, 3, 2);
        matrix3d.set(-1.0, 3, 1, 1);
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.put("matrix3d", matrix3d);
        octave.eval("x1 = matrix3d(:,:,1);");
        octave.eval("x2 = matrix3d(:,:,2);");
        octave.eval("x3 = matrix3d(:,3,:);");
        octave.eval("x4 = matrix3d(3,:,:);");
        final OctaveMatrix x1 = octave.get("x1");
        final OctaveMatrix x2 = octave.get("x2");
        final OctaveMatrix x3 = octave.get("x3");
        final OctaveMatrix x4 = octave.get("x4");
        octave.close();
        assertEquals(0.0, x1.get(1, 3));
        assertEquals(-1.0, x1.get(3, 1));
        assertEquals(42.0, x2.get(1, 3));
        assertEquals(0.0, x2.get(3, 1));
        assertEquals(42.0, x3.get(1, 1, 2));
        assertEquals(-1.0, x4.get(1, 1, 1));
    }

    /** Test */
    public void testNdMatrix() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        final TreeMap<String, OctaveObject> vars = new TreeMap<String, OctaveObject>();
        final double[] bigdata = new double[2 * 3 * 4];
        for (int idx = 0; idx < bigdata.length; idx++) {
            bigdata[idx] = idx + 1.0;
        }
        final double[] data2d = { 1.0, 2.0, 3.0, 5.0, 8.0, 13.0 };
        final double[] datascalar = { 42.0 };
        vars.put("bigmatrix", new OctaveMatrix(bigdata, 1, 2, 3, 4));
        vars.put("matrix2d", new OctaveMatrix(data2d, 2, 3));
        vars.put("matrixscalar", new OctaveMatrix(datascalar, 1, 1));
        vars.put("matrixzero", new OctaveMatrix(0, 0, 0, 0));
        vars.put("matrixzero2d", new OctaveMatrix(0, 0));
        octave.putAll(vars);
        final OctaveMatrix matrixzero = octave.get("matrixzero");
        final OctaveMatrix matrix2d = octave.get("matrix2d");
        final OctaveMatrix bigmatrix = octave.get("bigmatrix");
        final OctaveMatrix matrixzero2d = octave.get("matrixzero2d");
        final OctaveMatrix matrixscalar = octave.get("matrixscalar");
        assertEquals(matrixzero, vars.get("matrixzero"));
        assertEquals(matrixzero2d, vars.get("matrixzero2d"));
        assertEquals(matrixscalar, vars.get("matrixscalar"));
        assertEquals(matrix2d, vars.get("matrix2d"));
        assertEquals(bigmatrix, vars.get("bigmatrix"));
        octave.close();

        assertEquals("" + //
                "# name: matrixzero2d\n" + //
                "# type: matrix\n" + //
                "# rows: 0\n" + //
                "# columns: 0\n\n" //
        , OctaveIO.toText(matrixzero2d, "matrixzero2d"));

        assertEquals("" + //
                "# name: matrixzero\n" + //
                "# type: matrix\n" + //
                "# ndims: 4\n" + //
                " 0 0 0 0\n\n" // 
        , OctaveIO.toText(matrixzero, "matrixzero"));

        assertEquals("" + //
                "# name: matrixscalar\n" + //
                "# type: matrix\n" + //
                "# rows: 1\n" + //
                "# columns: 1\n" + //
                " 42.0\n\n" //
        , OctaveIO.toText(matrixscalar, "matrixscalar"));

        assertEquals("" + //
                "# name: matrix2d\n" + //
                "# type: matrix\n" + //
                "# rows: 2\n" + //
                "# columns: 3\n" + //
                " 1.0 3.0 8.0\n" + //
                " 2.0 5.0 13.0\n\n" //
        , OctaveIO.toText(matrix2d, "matrix2d"));

        assertEquals("" + //
                "# name: bigmatrix\n" + //
                "# type: matrix\n" + //
                "# ndims: 4\n" + //
                " 1 2 3 4\n" + //
                " 1.0\n" + //
                " 2.0\n" + //
                " 3.0\n" + //
                " 4.0\n" + //
                " 5.0\n" + //
                " 6.0\n" + //
                " 7.0\n" + //
                " 8.0\n" + //
                " 9.0\n" + //
                " 10.0\n" + //
                " 11.0\n" + //
                " 12.0\n" + //
                " 13.0\n" + //
                " 14.0\n" + //
                " 15.0\n" + //
                " 16.0\n" + //
                " 17.0\n" + //
                " 18.0\n" + //
                " 19.0\n" + //
                " 20.0\n" + //
                " 21.0\n" + //
                " 22.0\n" + //
                " 23.0\n" + //
                " 24.0\n\n" //
        , OctaveIO.toText(bigmatrix, "bigmatrix"));
    }

}
