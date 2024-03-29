/*
 * Copyright 2009 Ange Optimization ApS
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
package eu.simuline.octave.type;

//import net.sourceforge.cobertura.coveragedata.HasBeenInstrumented;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test OctaveBoolean
 */
public class TestOctaveBoolean {

    /**
     * Test 
     */
    @Test public void testGetAndSet() {
        final OctaveBoolean matrix = new OctaveBoolean(3, 6, 5, 4);
        matrix.set(true, 2, 5, 2, 3);
        for (int row = 1; row <= 3; row++) {
            for (int column = 1; column <= 6; column++) {
                for (int depth = 1; depth <= 5; depth++) {
                    for (int coffee = 1; coffee <= 4; coffee++) {
                        if (row == 2 && column == 5 
			    && depth == 2 && coffee == 3) {
			    // true for exactly the entry explicitly set above 
                            assertTrue( matrix.get(row, column, depth, coffee));
                        } else {
                            assertTrue(!matrix.get(row, column, depth, coffee));
                        }
                    }
                }
            }
        }
        try {
            matrix.get(2, 3, 1, 0);
            fail("Attempt to get with a position " + 
		 "that includes a 0 should fail");
        } catch (final IndexOutOfBoundsException e) {
            // ok
        }
        try {
            matrix.get(2, 3, 10, 3);
            fail("Attempt to get with a position " + 
		 "that exceeds range should fail");
        } catch (final IndexOutOfBoundsException e) {
            // ok
        }
        try {
            matrix.get(2, 3, 2, 3, 4);
            fail("Attempt to get with a position " + 
		 "that exceeds dimensions should fail");
        } catch (final IndexOutOfBoundsException e) {
            // ok
        }

    }

    /**
     */
    @Test public void testSizeConstructor() {
        final OctaveBoolean matrix = new OctaveBoolean(3, 6, 5, 4);
        assertEquals(4, matrix.getSizeLength());
        assertEquals(3, matrix.getSize(1));
        assertEquals(6, matrix.getSize(2));
        assertEquals(5, matrix.getSize(3));
        assertEquals(4, matrix.getSize(4));

        try {
            new OctaveBoolean(1);
            fail("OctaveMatrix should not support one dimensional matrices");
        } catch (final IllegalArgumentException e) {
            // OK
        }
    }

    /** Test */
    @Test public void testMakeCopy() {
        final boolean[] data = new boolean[2 * 3 * 4];
        for (int idx = 0; idx < data.length; idx++) {
            data[idx] = idx % 2 == 0;
        }
        final OctaveBoolean matrix = new OctaveBoolean(data, 2, 3, 4)
	    .shallowCopy();
        boolean b = true;
        for (int depth = 1; depth <= 4; depth++) {
            for (int column = 1; column <= 3; column++) {
                for (int row = 1; row <= 2; row++) {
                    assertEquals(matrix.get(row, column, depth), b);
                    b = !b;
                }
            }
        }

    }

    /**
     * matrixzero doesn't work because of bug in octave
     */
    @Test public void testGrowth() {
        final OctaveBoolean matrix = new OctaveBoolean(3, 3, 3, 3);
	// test set and get without resize 
        matrix.set(true, 2, 2, 2, 2);
        matrix.set(true, 3, 2, 2, 2);
        matrix.set(true, 2, 3, 2, 2);
        matrix.set(true, 2, 2, 3, 2);
        matrix.set(true, 2, 2, 2, 3);
        assertEquals(true, matrix.get(2, 2, 2, 2));
        assertEquals(true, matrix.get(3, 2, 2, 2));
        assertEquals(true, matrix.get(2, 3, 2, 2));
        assertEquals(true, matrix.get(2, 2, 3, 2));
        assertEquals(true, matrix.get(2, 2, 2, 3));

	// test set and get with resize 
        matrix.set(true, 4, 5, 7, 6);
        // assertEquals(true, matrix.get(2, 2, 2, 2));
        // assertEquals(true, matrix.get(3, 2, 2, 2));
        // assertEquals(true, matrix.get(2, 3, 2, 2));
        // assertEquals(true, matrix.get(2, 2, 3, 2));
        // assertEquals(true, matrix.get(2, 2, 2, 3));
        // assertEquals(true, matrix.get(4, 5, 7, 6));


	final OctaveBoolean matrixB = new OctaveBoolean(4, 5, 7, 6);
        matrixB.set(true, 2, 2, 2, 2);
        matrixB.set(true, 3, 2, 2, 2);
        matrixB.set(true, 2, 3, 2, 2);
        matrixB.set(true, 2, 2, 3, 2);
        matrixB.set(true, 2, 2, 2, 3);
        matrixB.set(true, 4, 5, 7, 6);
	assertEquals(matrixB, matrix);
    }

    /** */
    @Test public void testResize() {
        final OctaveBoolean matrix = new OctaveBoolean(0, 4);
        assertEquals(2, matrix.getSizeLength());
        assertEquals(0, matrix.getSize(1));
        assertEquals(4, matrix.getSize(2));
        // assertEquals(0, matrix.getData().length); is 0
	assertTrue(matrix.dataSize() >= 0);

        matrix.set(true, 1, 1);
        assertEquals(true, matrix.get(1, 1));
        assertEquals(2, matrix.getSizeLength());
        assertEquals(1, matrix.getSize(1));
        assertEquals(4, matrix.getSize(2));
        // assertEquals(4, matrix.getData().length); is 8
        assertTrue(matrix.dataSize() >= 4);
    }

    /** Test Performance of Resize */
    @Test public void testPerformance() {
        OctaveBoolean matrix = new OctaveBoolean(30, 0);
        final long allowedTime;
//        if (matrix instanceof HasBeenInstrumented) {
            allowedTime = 1800;
        // } else {
        //     allowedTime = 300;
        // }
        long t = System.currentTimeMillis();
        // 4125 was the number of containers in a real job.
        for (int pos = 1; pos <= 4125; ++pos) {
            matrix.set(true, 1, pos);
            matrix.set(true, 30, pos);
        }
        long timeused = System.currentTimeMillis() - t;
        if (timeused > allowedTime) {
            fail("Performance test didn't finish in " + allowedTime + 
		 "ms (used " + timeused + "ms)");
        }

        matrix = new OctaveBoolean(0, 30);
        t = System.currentTimeMillis();
        // 1000 is just some random number
        for (int pos = 1; pos <= 1000; ++pos) {
            matrix.set(true, pos, 1);
            matrix.set(true, pos, 30);
        }
        timeused = System.currentTimeMillis() - t;
        if (timeused > allowedTime) {
            fail("Performance test didn't finish in " + allowedTime + 
		 "ms (used " + timeused + "ms)");
        }
    }

}
