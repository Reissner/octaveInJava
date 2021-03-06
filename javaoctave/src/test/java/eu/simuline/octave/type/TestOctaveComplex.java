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

import eu.simuline.octave.type.cast.Cast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test OctaveComplex
 */
public class TestOctaveComplex {

    /**
     * Test that an OctaveDouble can be viewed as an OctaveComplex
     */
    @Test public void testCast() {
        Cast.cast(OctaveComplex.class, new OctaveDouble(1, 1));
    }

    /**
     * Test OctaveComplex resizes correctly
     */
    @Test public void testResize() {
        final OctaveComplex complex = new OctaveComplex(1, 1);

        checkSize(complex, 1, 1);
        assertEquals(0.0, complex.getReal(1, 1), 0.0);
        assertEquals(0.0, complex.getImag(1, 1), 0.0);

        complex.setReal(22, 1, 2);
        checkSize(complex, 1, 2);
        assertEquals( 0.0, complex.getReal(1, 1), 0.0);
        assertEquals( 0.0, complex.getImag(1, 1), 0.0);
        assertEquals(22.0, complex.getReal(1, 2), 0.0);
        assertEquals( 0.0, complex.getImag(1, 2), 0.0);

        complex.setImag(33, 2, 1);
        checkSize(complex, 2, 2);
        assertEquals( 0.0, complex.getReal(1, 1), 0.0);
        assertEquals( 0.0, complex.getImag(1, 1), 0.0);
        assertEquals(22.0, complex.getReal(1, 2), 0.0);
        assertEquals( 0.0, complex.getImag(1, 2), 0.0);
        assertEquals( 0.0, complex.getReal(2, 1), 0.0);
        assertEquals(33.0, complex.getImag(2, 1), 0.0);
        assertEquals( 0.0, complex.getReal(2, 2), 0.0);
        assertEquals( 0.0, complex.getImag(2, 2), 0.0);

        // assertEquals(22.0, complex.getReal()[complex.pos2ind(1, 2)], 0.0);
        // assertEquals(33.0, complex.getImag()[complex.pos2ind(2, 1)], 0.0);
    }

    private void checkSize(final OctaveComplex complex, 
			   final int i, 
			   final int j) {
        assertEquals(i, complex.getSize(1));
        assertEquals(j, complex.getSize(2));
        complex.getReal(i, j);
        complex.getImag(i, j);
        failGet(complex, i + 1, j);
        failGet(complex, i, j + 1);
    }

    private void failGet(final OctaveComplex complex, 
			 final int i, 
			 final int j) {
        try {
            complex.getReal(i, j);
            fail();
        } catch (final IndexOutOfBoundsException e) {
            // Expect this
        }
        try {
            complex.getImag(i, j);
            fail();
        } catch (final IndexOutOfBoundsException e) {
            // Expect this
        }
    }

}
