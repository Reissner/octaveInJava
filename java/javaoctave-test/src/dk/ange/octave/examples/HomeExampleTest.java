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
package dk.ange.octave.examples;

import junit.framework.TestCase;
import dk.ange.octave.OctaveEngine;
import dk.ange.octave.OctaveEngineFactory;
import dk.ange.octave.type.OctaveMatrix;
import dk.ange.octave.type.OctaveScalar;
import dk.ange.octave.type.OctaveString;

/**
 * http://kenai.com/projects/javaoctave/pages/Home
 */
public class HomeExampleTest extends TestCase {

    /** Test */
    public void test() {
        // Begin web text
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("warning off"); // not web text: needed to silence warnings from lsode
        octave.put("fun", new OctaveString("sqrt(1-t**2)"));
        octave.put("t1", new OctaveScalar(0));
        octave.put("t2", new OctaveScalar(1));
        octave.eval("result = lsode(fun, 0, [t1 t2])(2);");
        final OctaveMatrix result = octave.get("result");
        octave.close();
        final double integral = result.get(1);
        assertEquals(Math.PI / 4, integral, 1e-5);
        // End web text
    }

}
