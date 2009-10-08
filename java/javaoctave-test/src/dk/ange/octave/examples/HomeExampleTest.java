package dk.ange.octave.examples;

import junit.framework.TestCase;
import dk.ange.octave.OctaveEngine;
import dk.ange.octave.OctaveEngineFactory;
import dk.ange.octave.type.OctaveMatrix;

/**
 * http://kenai.com/projects/javaoctave/pages/Home
 */
public class HomeExampleTest extends TestCase {

    /** Test */
    public void test() {
        // Begin web text
        OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("x = 0:0.01:1;");
        octave.eval("t = lsode('x**2+t**2', 0, x);");
        OctaveMatrix t = octave.get("t");
        octave.close();
        // End web text
        assertEquals(101, t.rows());
    }

}
