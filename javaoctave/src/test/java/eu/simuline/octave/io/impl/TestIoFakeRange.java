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
import eu.simuline.octave.type.OctaveFake;
import eu.simuline.octave.type.OctaveObject;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test read/write of {@link OctaveFake}
 */
public class TestIoFakeRange {

    /** */
    @Test public void testReadWrite() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("x = 1:10;");
        OctaveObject x = octave.get("x");
        octave.put("c", x);
        octave.eval("assert(c, x);");

        octave.close();
    }

}
