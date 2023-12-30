package eu.simuline.octave.io;

import java.util.Map;

import eu.simuline.octave.exception.OctaveParseException;
import eu.simuline.octave.type.Octave;
import eu.simuline.octave.type.OctaveDouble;
import eu.simuline.octave.type.OctaveObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test {@link OctaveIO}
 */
public class TestOctaveIO {

    /**
     * Test that {@link OctaveIO#readWithName(String)} works 
     * and throws the expected on too much data.
     */
    @Test public void testReadWithName() {
        // Data
        final String varName = "x";
        final OctaveDouble varValue = Octave.scalar(42);
        final String input = "" + //
                "# name: x\n" + //
                "# type: scalar\n" + //
                "42\n" + //
                "";
        final String extra = "" + //
                "extra 1\n" + //
                "extra 2\n" + //
                "";
        // Test ok
        final Map<String, OctaveObject> map = OctaveIO.readWithName(input);
        assertTrue(map.containsKey(varName));
        assertEquals(varValue, map.get(varName));
        // Test error
        try {
            OctaveIO.readWithName(input + extra);
        } catch (final OctaveParseException e) {
            assertEquals("Too much data in input, " + 
			 "first extra line is 'extra 1'", e.getMessage());
        }
    }

}
