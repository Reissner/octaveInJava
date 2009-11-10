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

import junit.framework.Assert;
import junit.framework.TestCase;
import dk.ange.octave.OctaveEngine;
import dk.ange.octave.OctaveEngineFactory;
import dk.ange.octave.io.OctaveIO;
import dk.ange.octave.type.OctaveCell;
import dk.ange.octave.type.OctaveMatrix;
import dk.ange.octave.type.OctaveScalar;
import dk.ange.octave.type.OctaveString;

/**
 * Test
 */
public class TestOctaveCell extends TestCase {

    /**
     */
    public void testConstructor() {
        final OctaveCell cell = new OctaveCell(0, 0);
        Assert.assertEquals(0, cell.size(1));
        Assert.assertEquals(0, cell.size(2));
        Assert.assertEquals("# name: ans\n# type: cell\n# rows: 0\n# columns: 0\n", OctaveIO.toText(cell, "ans"));
    }

    /**
     */
    public void testConstructorValue() {
        final OctaveCell cell = new OctaveCell(0, 0);
        cell.set(new OctaveScalar(42), 1, 1);
        Assert.assertEquals(1, cell.size(1));
        Assert.assertEquals(1, cell.size(2));
        Assert.assertEquals("# name: mycell2\n# type: cell\n# rows: 1\n# columns: 1\n"
                + "# name: <cell-element>\n# type: scalar\n42.0\n\n\n" //
        , OctaveIO.toText(cell, "mycell2"));
    }

    /**
     */
    public void testConstructorIntInt() {
        final OctaveCell cell = new OctaveCell(2, 2);
        Assert.assertEquals(2, cell.size(1));
        Assert.assertEquals(2, cell.size(2));
        Assert.assertEquals("# name: mycell22\n# type: cell\n# rows: 2\n# columns: 2\n"
                + "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "\n" //
        , OctaveIO.toText(cell, "mycell22"));
    }

    /**
     */
    public void testSetIntInt() {
        final OctaveCell cell = new OctaveCell(0, 0);
        Assert.assertEquals(0, cell.size(1));
        Assert.assertEquals(0, cell.size(2));
        cell.set(new OctaveScalar(42), 3, 4);
        Assert.assertEquals(3, cell.size(1));
        Assert.assertEquals(4, cell.size(2));
        Assert.assertEquals("# name: mycell\n# type: cell\n# rows: 3\n# columns: 4\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "# name: <cell-element>\n# type: matrix\n# rows: 0\n# columns: 0\n\n" + //
                "# name: <cell-element>\n# type: scalar\n42.0\n\n\n" //
        , OctaveIO.toText(cell, "mycell"));
    }

    /**
     * @throws Exception
     */
    public void testOctaveConnection() throws Exception {
        final OctaveCell cell = new OctaveCell(0, 0);
        cell.set(new OctaveScalar(42), 1, 1);
        final OctaveCell cell2 = new OctaveCell(0, 0);
        cell2.set(new OctaveString("mystring"), 1, 1);
        cell.set(cell2, 3, 2);

        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.put("mycell", cell);
        final OctaveCell mycell_copy = octave.get("mycell");
        assertEquals(cell, mycell_copy);
        octave.close();
    }

    /**
     * @throws Exception
     */
    public void testSameInOctave() throws Exception {
        final OctaveCell cell = new OctaveCell(2, 3);
        for (int r = 1; r <= 2; ++r) {
            for (int c = 1; c <= 3; ++c) {
                cell.set(new OctaveScalar(r + 0.1 * c), r, c);
            }
        }
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.put("cell_java", cell);
        // Assert it is the same in Octave and Java
        octave.eval("cell_octave=cell(); for r=1:2 for c=1:3 cell_octave{r,c}=r+0.1*c; endfor endfor");
        octave.eval("assert(cell_octave, cell_java);");
        // Assert that the returned value is the same as the original
        assertEquals(cell, octave.get("cell_java"));
        octave.close();
    }

    /**
     */
    public void testMatrixInCell() {
        final OctaveMatrix octaveMatrix = new OctaveMatrix(2, 3);
        octaveMatrix.set(42, 1, 1);
        final OctaveCell cell = new OctaveCell(2, 2);
        cell.set(new OctaveScalar(42), 1, 1);
        cell.set(octaveMatrix, 1, 2);
        Assert.assertEquals(2, cell.size(1));
        Assert.assertEquals(2, cell.size(2));
        Assert.assertEquals("" //
                + "# name: mycell2\n" //
                + "# type: cell\n" //
                + "# rows: 2\n" //
                + "# columns: 2\n" //

                + "# name: <cell-element>\n" //
                + "# type: scalar\n" //
                + "42.0\n" //
                + "\n" //

                + "# name: <cell-element>\n" //
                + "# type: matrix\n" //
                + "# rows: 0\n" //
                + "# columns: 0\n" //
                + "\n" //
                + "\n" //

                + "# name: <cell-element>\n" //
                + "# type: matrix\n" //
                + "# rows: 2\n" //
                + "# columns: 3\n" //
                + " 42.0 0.0 0.0\n" //
                + " 0.0 0.0 0.0\n" //
                + "\n" //

                + "# name: <cell-element>\n" //
                + "# type: matrix\n" //
                + "# rows: 0\n" //
                + "# columns: 0\n" //
                + "\n" //
                + "\n" //
        , OctaveIO.toText(cell, "mycell2"));
    }

}
