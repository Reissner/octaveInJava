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
package eu.simuline.octave.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Test {@link OctaveLong}
 */
public class TestOctaveLong {

    /**
     * Test equals
     */
    @Test public void testEquals() {
        final OctaveLong s1a = longScalar(1);
        final OctaveLong s1b = longScalar(1);
        final OctaveLong s1c = longScalar(0);
        s1c.set(1, 1, 1);

        assertEquals(s1a, s1b);
        assertEquals(s1a, s1c);
        assertEquals(s1b, s1c);
        assertNotSame(s1a, s1b);
        assertNotSame(s1a, s1c);
        assertNotSame(s1b, s1c);

        final OctaveLong s0 = longScalar(0);
        final OctaveLong s2 = longScalar(2);

        assertTrue(!s1a.equals(s0));
        assertTrue(!s1a.equals(s2));
    }

    private static OctaveLong longScalar(final long i) {
        return new OctaveLong(new long[] { i }, 1, 1);
    }

    /**
     * Simple test of set and get
     */
    @Test public void testGetAndSet() {
        final OctaveLong matrix = new OctaveLong(3, 6, 5, 4);
        assertEquals(0, matrix.get(2, 5, 2, 3));
        matrix.set(42, 2, 5, 2, 3);
        assertEquals(42, matrix.get(2, 5, 2, 3));
    }

    /** */
    @Test public void testGrowth() {
        final OctaveLong matrix = new OctaveLong(3, 3, 3, 3);
	// test set and get without resize 
        matrix.set(42, 2, 2, 2, 2);
        matrix.set( 1, 3, 2, 2, 2);
        matrix.set( 2, 2, 3, 2, 2);
        matrix.set( 3, 2, 2, 3, 2);
        matrix.set( 4, 2, 2, 2, 3);
        assertEquals(42, matrix.get(2, 2, 2, 2));
        assertEquals( 1, matrix.get(3, 2, 2, 2));
        assertEquals( 2, matrix.get(2, 3, 2, 2));
        assertEquals( 3, matrix.get(2, 2, 3, 2));
        assertEquals( 4, matrix.get(2, 2, 2, 3));

	// test set and get with resize 
        matrix.set(314, 4, 5, 7, 6);
        // assertEquals(42, matrix.get(2, 2, 2, 2));
        // assertEquals( 1, matrix.get(3, 2, 2, 2));
        // assertEquals( 2, matrix.get(2, 3, 2, 2));
        // assertEquals( 3, matrix.get(2, 2, 3, 2));
        // assertEquals( 4, matrix.get(2, 2, 2, 3));
        // assertEquals(Math.PI, matrix.get(4, 5, 7, 6));

	final OctaveLong matrixB = new OctaveLong(4, 5, 7, 6);
        matrixB.set(42, 2, 2, 2, 2);
        matrixB.set( 1, 3, 2, 2, 2);
        matrixB.set( 2, 2, 3, 2, 2);
        matrixB.set( 3, 2, 2, 3, 2);
        matrixB.set( 4, 2, 2, 2, 3);
        matrixB.set(314, 4, 5, 7, 6);
 	assertEquals(matrixB, matrix);
    }



}
