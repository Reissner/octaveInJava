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
package eu.simuline.octave.examples;

import eu.simuline.octave.OctaveEngine;
import eu.simuline.octave.OctaveEngineFactory;
import eu.simuline.octave.type.OctaveDouble;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * http://kenai.com/projects/javaoctave/pages/SimpleExampleOfJavaOctaveUsage
 */
public class SimpleExampleTest {

    /** Test */
    @Test public void test() {
        // Begin web text
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        final OctaveDouble a = new OctaveDouble(new double[] {1, 2, 3, 4 }, 
						2, 2);
        octave.put("a", a);
        final String func = "" //
                + "function res = my_func(a)\n" //
                + " res = 2 * a;\n" //
                + "endfunction\n" //
                + "";
        octave.eval(func);
        octave.eval("b = my_func(a);");
        final OctaveDouble b = octave.get(OctaveDouble.class, "b");
        octave.close();
        // End web text
        assertEquals(8.0, b.get(2, 2), 0.0);
    }

}
