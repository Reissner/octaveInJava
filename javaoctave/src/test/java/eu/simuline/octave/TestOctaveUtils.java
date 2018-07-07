package eu.simuline.octave;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Test {@link OctaveUtils}
 */
public class TestOctaveUtils {

    /**
     * Test that the list of variables is empty. 
     */
    @Test public void testListVarsEmpty() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        final Collection<String> collection = OctaveUtils.listVars(octave);
        assertEquals(collection.toString(), 0, collection.size());
        octave.close();
    }

    /**
     * Test that the list of variables has 1 or 2 entries. 
     * **** better: test concrete collection of variables. 
     */
    @Test public void testListVarsOneTwo() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();

        octave.eval("my_var = 42;");
        final Collection<String> collection1 = OctaveUtils.listVars(octave);
        assertEquals(collection1.toString(), 1, collection1.size());

        octave.eval("1 + 2;");
        octave.eval("my_other_var = 42;");
        final Collection<String> collection2 = OctaveUtils.listVars(octave);
        assertEquals(collection2.toString(), 2, collection2.size());

        octave.close();
    }

    public static void main(String[] args) {
    }

}
