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
import eu.simuline.octave.type.Octave;
import eu.simuline.octave.type.OctaveCell;
import eu.simuline.octave.type.OctaveDouble;
import eu.simuline.octave.type.OctaveString;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test read/write of {@link OctaveCell}
 */
public class TestIoOctaveCell {

    /**
     */
    @Test public void testConstructor() {
        final OctaveCell cell = new OctaveCell(0, 0);
        assertEquals(0, cell.getSize(1));
        assertEquals(0, cell.getSize(2));
        assertEquals("# name: ans\n" + //
		     "# type: cell\n" + //
		     "# rows: 0\n" + //
		     "# columns: 0\n", 
		     OctaveIO.toText("ans", cell));
    }

    /**
     */
    @Test public void testConstructorValue() {
        final OctaveCell cell = new OctaveCell(0, 0);
        cell.set(Octave.scalar(42), 1, 1);
        assertEquals(1, cell.getSize(1));
        assertEquals(1, cell.getSize(2));
        assertEquals("# name: mycell2\n" + 
		     "# type: cell\n" + 
		     "# rows: 1\n" + 
		     "# columns: 1\n" + 
		     "# name: <cell-element>\n" + 
		     "# type: scalar\n" + 
		     "42.0\n\n", 
		     OctaveIO.toText("mycell2", cell));
    }

    /**
     */
    @Test public void testConstructorIntInt() {
        final OctaveCell cell = new OctaveCell(2, 2);
        assertEquals(2, cell.getSize(1));
        assertEquals(2, cell.getSize(2));
        assertEquals("# name: mycell22\n" + 
		     "# type: cell\n" + 
		     "# rows: 2\n" + 
		     "# columns: 2\n" + //

		     "# name: <cell-element>\n" + 
		     "# type: matrix\n" + 
		     "# rows: 0\n" + 
		     "# columns: 0\n" + //

		     "# name: <cell-element>\n" + 
		     "# type: matrix\n" + 
		     "# rows: 0\n" + 
		     "# columns: 0\n" + //
		     "\n" + //
		     "# name: <cell-element>\n" + 
		     "# type: matrix\n" + 
		     "# rows: 0\n" + 
		     "# columns: 0\n" + //

		     "# name: <cell-element>\n" + 
		     "# type: matrix\n" + 
		     "# rows: 0\n" + 
		     "# columns: 0\n" + //
		     "\n",
		     OctaveIO.toText("mycell22", cell));
    }

    /**
     */
    @Test public void testSetIntInt() {
        final OctaveCell cell = new OctaveCell(0, 0);
        assertEquals(0, cell.getSize(1));
        assertEquals(0, cell.getSize(2));
        cell.set(Octave.scalar(42), 3, 4);
        assertEquals(3, cell.getSize(1));
        assertEquals(4, cell.getSize(2));
        assertEquals("# name: mycell\n# type: cell\n" + 
		     "# rows: 3\n# columns: 4\n" + //
		     "# name: <cell-element>\n# type: matrix\n" + 
		     "# rows: 0\n# columns: 0\n" + //
		     "# name: <cell-element>\n# type: matrix\n" + 
		     "# rows: 0\n# columns: 0\n" + //
		     "# name: <cell-element>\n# type: matrix\n" + 
		     "# rows: 0\n# columns: 0\n" + //
		     "\n" + //
		     "# name: <cell-element>\n# type: matrix\n" + 
		     "# rows: 0\n# columns: 0\n" + //
		     "# name: <cell-element>\n# type: matrix\n" + 
		     "# rows: 0\n# columns: 0\n" + //
		     "# name: <cell-element>\n# type: matrix\n" + 
		     "# rows: 0\n# columns: 0\n" + //
		     "\n" + //
		     "# name: <cell-element>\n# type: matrix\n" + 
		     "# rows: 0\n# columns: 0\n" + //
		     "# name: <cell-element>\n# type: matrix\n" + 
		     "# rows: 0\n# columns: 0\n" + //
		     "# name: <cell-element>\n# type: matrix\n" + 
		     "# rows: 0\n# columns: 0\n" + //
		     "\n" + //
		     "# name: <cell-element>\n# type: matrix\n" + 
		     "# rows: 0\n# columns: 0\n" + //
		     "# name: <cell-element>\n# type: matrix\n" + 
		     "# rows: 0\n# columns: 0\n" + //
		     "# name: <cell-element>\n# type: scalar\n42.0\n" + //
		     "\n", 
		     OctaveIO.toText("mycell", cell));
    }

    /**
     * @throws Exception
     */
    @Test public void testOctaveConnection() throws Exception {
        final OctaveCell cell = new OctaveCell(0, 0);
        cell.set(Octave.scalar(42), 1, 1);
        final OctaveCell cell2 = new OctaveCell(0, 0);
        cell2.set(new OctaveString("mystring"), 1, 1);
        cell.set(cell2, 3, 2);

        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.put("mycell", cell);
        final OctaveCell mycell_copy = octave.get(OctaveCell.class, "mycell");
        assertEquals(cell, mycell_copy);
        octave.close();
    }

    /**
     * @throws Exception
     */
    @Test public void testSameInOctave() throws Exception {
        final OctaveCell cell = new OctaveCell(2, 3);
        for (int r = 1; r <= 2; ++r) {
            for (int c = 1; c <= 3; ++c) {
                cell.set(Octave.scalar(r + 0.1 * c), r, c);
            }
        }
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.put("cell_java", cell);
        // Assert it is the same in Octave and Java
        octave.eval("cell_octave=cell(); " + 
		    "for r=1:2 for c=1:3 cell_octave{r,c}=r+0.1*c; end end");
        octave.eval("assert(cell_octave, cell_java);");
        // Assert that the returned value is the same as the original
        assertEquals(cell, octave.get("cell_java"));
        octave.close();
    }

    /**
     */
    @Test public void testMatrixInCell() {
        final OctaveDouble octaveMatrix = new OctaveDouble(2, 3);
        octaveMatrix.set(42, 1, 1);
        final OctaveCell cell = new OctaveCell(2, 2);
        cell.set(Octave.scalar(42), 1, 1);
        cell.set(octaveMatrix, 1, 2);
        assertEquals(2, cell.getSize(1));
        assertEquals(2, cell.getSize(2));
        assertEquals("# name: mycell2\n" + //
		     "# type: cell\n" + //
		     "# rows: 2\n" + //
		     "# columns: 2\n" + //

		     "# name: <cell-element>\n" + //
		     "# type: scalar\n" + //
		     "42.0\n" + //

		     "# name: <cell-element>\n" + //
		     "# type: matrix\n" + //
		     "# rows: 0\n" + //
		     "# columns: 0\n" + //

		     "\n" + //

		     "# name: <cell-element>\n" + //
		     "# type: matrix\n" + //
		     "# rows: 2\n" + //
		     "# columns: 3\n" + //
		     " 42.0 0.0 0.0\n" + //
		     " 0.0 0.0 0.0\n" + //

		     "# name: <cell-element>\n" + //
		     "# type: matrix\n" + //
		     "# rows: 0\n" + //
		     "# columns: 0\n" + //

		     "\n",
		     OctaveIO.toText("mycell2", cell));
    }

    /** */
    @Test public void testCell12() {
        final OctaveCell cell = new OctaveCell(0, 0);
        cell.set(Octave.scalar(42), 1, 2);
        assertEquals(1, cell.getSize(1));
        assertEquals(2, cell.getSize(2));
        assertEquals("# name: cell12\n" + //
		     "# type: cell\n" + //
		     "# rows: 1\n" + //
		     "# columns: 2\n" + //

		     "# name: <cell-element>\n" + //
		     "# type: matrix\n" + //
		     "# rows: 0\n" + //
		     "# columns: 0\n" + //

		     "\n" + //
		     "# name: <cell-element>\n" + //
		     "# type: scalar\n" + //
		     "42.0\n" + //

		     "\n", 
		     OctaveIO.toText("cell12", cell));
    }

    /** */
    @Test public void testCell22() {
        final OctaveCell cell = new OctaveCell(0, 0);
        cell.set(Octave.scalar(12), 1, 2);
        cell.set(Octave.scalar(21), 2, 1);
        assertEquals(2, cell.getSize(1));
        assertEquals(2, cell.getSize(2));
        assertEquals("# name: cell22\n" + //
		     "# type: cell\n" + //
		     "# rows: 2\n" + //
		     "# columns: 2\n" + //

		     "# name: <cell-element>\n" + //
		     "# type: matrix\n" + //
		     "# rows: 0\n" + //
		     "# columns: 0\n" + //

		     "# name: <cell-element>\n" + //
		     "# type: scalar\n" + //
		     "21.0\n" + //

		     "\n" + //

		     "# name: <cell-element>\n" + //
		     "# type: scalar\n" + //
		     "12.0\n" + //

		     "# name: <cell-element>\n" + //
		     "# type: matrix\n" + //
		     "# rows: 0\n" + //
		     "# columns: 0\n" + //

		     "\n", 
		     OctaveIO.toText("cell22", cell));
    }

    /**
     * @throws Exception
     */
    @Test public void testWriteRead() throws Exception {
        final OctaveCell cell = new OctaveCell(0, 0);
        cell.set(Octave.scalar(12), 1, 2);
        cell.set(Octave.scalar(21), 2, 1);
        assertEquals(2, cell.getSize(1));
        assertEquals(2, cell.getSize(2));

        final String text = OctaveIO.toText(cell);
        final BufferedReader bufferedReader = 
	    new BufferedReader(new StringReader(text));

        assertEquals("# name: ans", bufferedReader.readLine());
        assertEquals(cell, OctaveIO.read(bufferedReader));
        assertEquals(-1, bufferedReader.read()); // Check end of file
    }

}
