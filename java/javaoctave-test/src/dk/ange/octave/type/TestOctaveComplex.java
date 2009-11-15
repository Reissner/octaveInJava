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
package dk.ange.octave.type;

import dk.ange.octave.type.cast.Cast;
import junit.framework.TestCase;

/**
 * Test OctaveComplex
 */
public class TestOctaveComplex extends TestCase {

    /**
     * Test that an OctaveDouble can be viewed as an OctaveComplex
     */
    public void testCast() {
        Cast.cast(OctaveComplex.class, new OctaveDouble(1, 1));
    }

}
