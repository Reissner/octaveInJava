package dk.ange.octave.examples;

import junit.framework.TestCase;
import dk.ange.octave.OctaveEngine;
import dk.ange.octave.OctaveEngineFactory;
import dk.ange.octave.type.OctaveMatrix;

/**
 * http://kenai.com/projects/javaoctave/pages/SimpleExampleOfJavaOctaveUsage
 */
public class SimpleExampleTest extends TestCase {

    /** Test */
    public void test() {
        // Begin web text
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        final OctaveMatrix a = new OctaveMatrix(new double[] { 1, 2, 3, 4 }, 2, 2);
        octave.put("a", a);
        final String func = "" //
                + "function res = my_func(a)\n" //
                + " res = 2 * a;\n" //
                + "endfunction\n" //
                + "";
        octave.eval(func);
        octave.eval("b = my_func(a);");
        final OctaveMatrix b = octave.get("b");
        octave.close();
        // End web text
        assertEquals(8.0, b.get(2,2));
    }

}
