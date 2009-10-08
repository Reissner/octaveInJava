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
        OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("warning off"); // not web text: needed to silence warnings from lsode
        octave.put("fun", new OctaveString("sqrt(1-t**2)"));
        octave.put("t1", new OctaveScalar(0));
        octave.put("t2", new OctaveScalar(1));
        octave.eval("result = lsode(fun, 0, [t1 t2])(2);");
        OctaveMatrix result = octave.get("result");
        octave.close();
        double integral = result.get(1);
        assertEquals(Math.PI / 4, integral, 1e-5);
        // End web text
    }

}
