package dk.ange.octave.examples;

import junit.framework.TestCase;
import dk.ange.octave.OctaveEngine;
import dk.ange.octave.OctaveEngineFactory;
import dk.ange.octave.type.OctaveMatrix;

/**
 * http://kenai.com/projects/javaoctave/pages/SimpleExampleOfSolvingAnODE
 */
public class OdeExampleTest extends TestCase {

    /** Test */
    public void test() {
        // Begin web text
        OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("x = 0:0.01:1;");
        octave.eval("t = lsode('x**2+t**2', 0, x);");
        OctaveMatrix t = octave.get("t");
        octave.close();
        double[] result = t.getData();
        // End web text
        assertEquals(101, result.length);
    }

}
