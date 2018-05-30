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

//import        eu.simuline.testhelpers.Actions;

import java.util.Map;
import java.util.TreeMap;

import eu.simuline.octave.OctaveEngine;
import eu.simuline.octave.OctaveEngineFactory;
import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.type.OctaveInt;
import eu.simuline.octave.type.OctaveObject;

import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;

//import eu.simuline.testhelpers.Actions;
//import eu.simuline.testhelpers.Accessor;
//import static eu.simuline.testhelpers.Assert.assertEquals;
//import static eu.simuline.testhelpers.Assert.assertArraysEquals;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.fail;


import org.junit.Ignore;
import org.junit.Test;
// import org.junit.Before;
// import org.junit.runner.RunWith;
// import org.junit.runners.Suite;
// import org.junit.runners.Suite.SuiteClasses;


/**
 * Test read/write of {@link OctaveInt}
 */
//@RunWith(Suite.class)
//@SuiteClasses({TestIoOctaveInt.class})
public class TestIoOctaveInt {

    @Ignore @Test public void testScalarToText() {
        final OctaveObject integer42 = new OctaveInt(new int[] {42}, 1, 1);
        assertEquals("# name: ans\n" + // 
		     "# type: int32 scalar\n" + // 
		     "42\n", 
		     OctaveIO.toText(integer42));
        final OctaveObject integer43 = new OctaveInt(new int[] {43}, 1, 1);
        assertEquals("# name: integer43\n" +
		     "# type: int32 scalar\n" +
		     "43\n", 
		     OctaveIO.toText("integer43", integer43));
    }


    /**
     * Test
     */
    @Ignore @Test public void test3dToText() {
        final OctaveInt matrix = new OctaveInt(3, 4, 2);
        assertEquals("# name: matrix3d\n" + //
		     "# type: int32 matrix\n" + //
		     "# ndims: 3\n" + //
		     " 3 4 2\n" + //
		     " 0\n 0\n 0\n" + //
		     " 0\n 0\n 0\n" + //
		     " 0\n 0\n 0\n" + //
		     " 0\n 0\n 0\n" + //
		     " 0\n 0\n 0\n" + //
		     " 0\n 0\n 0\n" + //
		     " 0\n 0\n 0\n" + //
		     " 0\n 0\n 0\n", 
		     OctaveIO.toText("matrix3d", matrix));
        matrix.set(42, new int[] {1, 3, 2});
        assertEquals("# name: matrix3d\n" + //
		     "# type: int32 matrix\n" + //
		     "# ndims: 3\n" + //
		     " 3 4 2\n" + //
		     " 0\n 0\n 0\n" + // matrix(x,x,1)
		     " 0\n 0\n 0\n" + //
		     " 0\n 0\n 0\n" + //
		     " 0\n 0\n 0\n" + //

		     " 0\n 0\n 0\n" + // matrix(x,x,2)
		     " 0\n 0\n 0\n" + //
		     " 42\n 0\n 0\n" + // matrix(1,3,2)
		     " 0\n 0\n 0\n", 
		     OctaveIO.toText("matrix3d", matrix));
    }

    /**
     * Test
     */
    @Ignore @Test public void testEmpty3dToText() {
        final OctaveInt matrix = new OctaveInt(0, 0, 0);
        assertEquals("# name: matrix3d\n" + 
		     "# type: int32 matrix\n" + 
		     "# ndims: 3\n 0 0 0\n", 
		     OctaveIO.toText("matrix3d", matrix));
    }


    /**
     * @throws Exception
     */
    @Ignore @Test public void testEmpty2dToText() throws Exception {
        final OctaveInt matrix = new OctaveInt(0, 0);
        assertEquals(0, matrix.size(1));
        assertEquals(0, matrix.size(2));
        assertEquals("# name: matrix\n" + //
		     "# type: int32 matrix\n" + //
		     "# rows: 0\n" + //
		     "# columns: 0\n", 
		     OctaveIO.toText("matrix", matrix));
    }

    /**
     * @throws Exception
     */
    @Ignore @Test public void test2dToText() throws Exception {
        final int[] numbers = { 1, 2, 3, 4, 5, 6 };
        final OctaveInt matrix = new OctaveInt(numbers, 2, 3);
        assertEquals(2, matrix.size(1));
        assertEquals(3, matrix.size(2));
        assertEquals("# name: mymatrix\n" + //
		     "# type: int32 matrix\n" + //
		     "# rows: 2\n" + //
		     "# columns: 3\n" + //
		     " 1 3 5\n" + //
		     " 2 4 6\n", 
		     OctaveIO.toText("mymatrix", matrix));
    }


    /**
     * @throws Exception
     */
    @Ignore @Test public void test2dToTextB() throws Exception {
        final OctaveInt matrix = new OctaveInt(2, 3);
        assertEquals(2, matrix.size(1));
        assertEquals(3, matrix.size(2));
        assertEquals("# name: matrix\n" + //
		     "# type: int32 matrix\n" + //
		     "# rows: 2\n" + //
		     "# columns: 3\n" + //
		     " 0 0 0\n" + //
		     " 0 0 0\n", 
		     OctaveIO.toText("matrix", matrix));
        matrix.set(42, 1, 2);
        assertEquals("# name: myother\n" + //
		     "# type: int32 matrix\n" + //
		     "# rows: 2\n" + //
		     "# columns: 3\n" + //
		     " 0 42 0\n" + //
		     " 0 0 0\n", 
		     OctaveIO.toText("myother", matrix));
        matrix.set(2, 2, 1);
        assertEquals("# name: myother\n" + //
		     "# type: int32 matrix\n" + //
		     "# rows: 2\n" + //
		     "# columns: 3\n" + //
		     " 0 42 0\n" + //
		     " 2 0 0\n", 
		     OctaveIO.toText("myother", matrix));
        matrix.set(4, 2, 2);
        assertEquals("# name: myother\n" + //
		     "# type: int32 matrix\n" + //
		     "# rows: 2\n" + //
		     "# columns: 3\n" + //
		     " 0 42 0\n" + //
		     " 2 4 0\n", 
		     OctaveIO.toText("myother", matrix));
    }



    /**
     * @throws Exception
     */
    @Test public void testOctaveIntScalar() throws Exception {
System.out.println("testOctaveIntScalar()...");
	
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        //final OctaveObject i1 = new OctaveInt(42, 1, 1);
	final OctaveInt i1 = new OctaveInt(1, 1);
	i1.set(42, 1, 1);
        octave.put("i", i1);
        final OctaveInt i2 = (OctaveInt)octave.get("i");	
        assertEquals(i1, i2);
        octave.close();
System.out.println("...testOctaveIntScalar()");
    }


    /**
     * @throws Exception
     */
    @Ignore @Test public void testOctaveGet() throws Exception {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("m=int32([1 2;3 4]);");
        final OctaveInt m = octave.get(OctaveInt.class, "m");
        assertEquals("# name: m\n" + //
		     "# type: int32 matrix\n" + //
		     "# rows: 2\n" + //
		     "# columns: 2\n" + //
		     " 1 2\n" + //
		     " 3 4\n",  //
		     OctaveIO.toText("m", m));
        octave.close();
    }



    /**
     * @throws Exception
     */
    @Test public void testOctaveSetExecGet() throws Exception {
System.out.println("testOctaveSetExecGet()...");
        final int[] numbers = { 1, 2, 3, 4, 5, 6 };//
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        final OctaveInt in = new OctaveInt(numbers, 2, 3);//2, 3
        octave.put("in", in);
        octave.eval("out=in;");
        final OctaveInt out = octave.get(OctaveInt.class, "out");
        assertEquals(OctaveIO.toText(in), OctaveIO.toText(out));
        octave.eval("slicerow=in(2,:); slicecol=in(:,2);");
        OctaveInt slicerow = octave.get(OctaveInt.class, "slicerow");
        OctaveInt slicecol = octave.get(OctaveInt.class, "slicecol");
        assertEquals(2, slicerow.get(1, 1));
        assertEquals(4, slicerow.get(1, 2));
        assertEquals(6, slicerow.get(1, 3));
        assertEquals(3, slicecol.get(1, 1));
        assertEquals(4, slicecol.get(2, 1));
        octave.close();
System.out.println("...testOctaveSetExecGet()");
    }


    /**
     * Test
     */
    @Ignore @Test public void testOctave3dMatrix() {
        final OctaveInt matrix3d = new OctaveInt(3, 4, 2);
        matrix3d.set(42, 1, 3, 2);
        matrix3d.set(-1, 3, 1, 1);
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.put("matrix3d", matrix3d);
        octave.eval("x1 = matrix3d(:,:,1);");
        octave.eval("x2 = matrix3d(:,:,2);");
        octave.eval("x3 = matrix3d(:,3,:);");
        octave.eval("x4 = matrix3d(3,:,:);");
        final OctaveInt x1 = octave.get(OctaveInt.class, "x1");
        final OctaveInt x2 = octave.get(OctaveInt.class, "x2");
        final OctaveInt x3 = octave.get(OctaveInt.class, "x3");
        final OctaveInt x4 = octave.get(OctaveInt.class, "x4");
        octave.close();
        assertEquals( 0, x1.get(1, 3));
        assertEquals(-1, x1.get(3, 1));
        assertEquals(42, x2.get(1, 3));
        assertEquals( 0, x2.get(3, 1));
        assertEquals(42, x3.get(1, 1, 2));
        assertEquals(-1, x4.get(1, 1, 1));
    }

    /** Test */
    @Ignore @Test public void testOctaveNdMatrix() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        final Map<String, OctaveObject> vars = 
	    new TreeMap<String, OctaveObject>();
        final int[] bigdata = new int[2 * 3 * 4];
        for (int idx = 0; idx < bigdata.length; idx++) {
            bigdata[idx] = idx + 1;
        }
        final int[] data2d = {1, 2, 3, 5, 8, 13};
        final int[] datascalar = {42};
        vars.put("bigmatrix",    new OctaveInt(bigdata, 1, 2, 3, 4));
        vars.put("matrix2d",     new OctaveInt(data2d, 2, 3));
        vars.put("matrixscalar", new OctaveInt(datascalar, 1, 1));
        vars.put("matrixzero",   new OctaveInt(0, 0, 0, 0));
        vars.put("matrixzero2d", new OctaveInt(0, 0));
        octave.putAll(vars);
        OctaveInt matrixzero   = octave.get(OctaveInt.class, "matrixzero");
        OctaveInt matrix2d     = octave.get(OctaveInt.class, "matrix2d");
        OctaveInt bigmatrix    = octave.get(OctaveInt.class, "bigmatrix");
        OctaveInt matrixzero2d = octave.get(OctaveInt.class, "matrixzero2d");
        OctaveInt matrixscalar = octave.get(OctaveInt.class, "matrixscalar");
        assertEquals(matrixzero,   vars.get("matrixzero"));
        assertEquals(matrixzero2d, vars.get("matrixzero2d"));
        assertEquals(matrixscalar, vars.get("matrixscalar"));
        assertEquals(matrix2d,     vars.get("matrix2d"));
        assertEquals(bigmatrix,    vars.get("bigmatrix"));
        octave.close();

        assertEquals("# name: matrixzero2d\n" + //
		     "# type: matrix\n" + //
		     "# rows: 0\n" + //
		     "# columns: 0\n", //
		     OctaveIO.toText("matrixzero2d", matrixzero2d));

        assertEquals("# name: matrixzero\n" + //
		     "# type: matrix\n" + //
		     "# ndims: 4\n" + //
		     " 0 0 0 0\n",  // 
		     OctaveIO.toText("matrixzero", matrixzero));

        assertEquals("# name: matrixscalar\n" + //
		     "# type: scalar\n" + //
		     "42\n",  //
		     OctaveIO.toText("matrixscalar", matrixscalar));

        assertEquals("# name: matrix2d\n" + //
		     "# type: matrix\n" + //
		     "# rows: 2\n" + //
		     "# columns: 3\n" + //
		     " 1 3 8\n" + //
		     " 2 5 13\n",  //
		     OctaveIO.toText("matrix2d", matrix2d));

        assertEquals("# name: bigmatrix\n" + //
		     "# type: matrix\n" + //
		     "# ndims: 4\n" + //
		     " 1 2 3 4\n" + //
		     " 1\n" + //
		     " 2\n" + //
		     " 3\n" + //
		     " 4\n" + //
		     " 5\n" + //
		     " 6\n" + //
		     " 7\n" + //
		     " 8\n" + //
		     " 9\n" + //
		     " 10\n" + //
		     " 11\n" + //
		     " 12\n" + //
		     " 13\n" + //
		     " 14\n" + //
		     " 15\n" + //
		     " 16\n" + //
		     " 17\n" + //
		     " 18\n" + //
		     " 19\n" + //
		     " 20\n" + //
		     " 21\n" + //
		     " 22\n" + //
		     " 23\n" + //
		     " 24\n",  //
		     OctaveIO.toText("bigmatrix", bigmatrix));
    }


    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String[] args) {
	//Actions.runFromMain();
    }
}
