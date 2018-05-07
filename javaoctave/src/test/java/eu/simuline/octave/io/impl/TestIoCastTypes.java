package eu.simuline.octave.io.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import eu.simuline.octave.OctaveEngine;
import eu.simuline.octave.OctaveEngineFactory;
import eu.simuline.octave.exception.OctaveEvalException;
import eu.simuline.octave.exception.OctaveParseException;
import eu.simuline.octave.type.OctaveBoolean;
import eu.simuline.octave.type.OctaveDouble;
import eu.simuline.octave.type.OctaveInt;

/**
 * Test read/write of {@link OctaveObject}
 * Evaluate a Strings casting an assignment and ensure 'get' works.
 */
public class TestIoCastTypes {
	
	@Test
	public void castToReal() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
		//Scalar casts:
        octave.eval("a = single(true)");
        assertEquals(1.0, octave.get(OctaveDouble.class, "a").get(1), 0.0);
        octave.eval("b = single(5)");
        assertEquals(5.0, octave.get(OctaveDouble.class, "b").get(1), 0.0);
        octave.eval("c = single(5.5)");
        assertEquals(5.5, octave.get(OctaveDouble.class, "c").get(1), 0.0);
        octave.eval("d = double(true)");
        assertEquals(1.0, octave.get(OctaveDouble.class, "d").get(1), 0.0);
        octave.eval("e = double(5)");
        assertEquals(5.0, octave.get(OctaveDouble.class, "e").get(1, 1), 0.0);
        octave.eval("f = double(5.5)");
        assertEquals(5.5, octave.get(OctaveDouble.class, "f").get(1, 1), 0.0);
        
        //Matrix casts
        octave.eval("g = single([true, false true; false false false])");
        assertEquals(0.0, octave.get(OctaveDouble.class, "g").get(2, 3), 0.0);
        octave.eval("h = single([5 1 4; 6 8 4])");
        assertEquals(4.0, octave.get(OctaveDouble.class, "h").get(2, 3), 0.0);
        octave.eval("i = single([5.5 3.5 12])");
        assertEquals(12.0, octave.get(OctaveDouble.class, "i").get(1, 3), 0.0);
        octave.eval("j = double([true; false; true; false])");
        assertEquals(1.0, octave.get(OctaveDouble.class, "j").get(3, 1), 0.0);
        octave.eval("k = double([5 4 3 1 2])");
        assertEquals(2.0, octave.get(OctaveDouble.class, "k").get(1, 5), 0.0);
        octave.eval("l = double([5.5 55])");
        assertEquals(55.0, octave.get(OctaveDouble.class, "l").get(1, 2), 0.0);
        octave.close();
	}
	
	@Test
	public void castToBoolean() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
		//Scalar casts:
        octave.eval("a = logical(true)");
        
        assertEquals(true, octave.get(OctaveBoolean.class, "a").get(1));
        octave.eval("b = logical(5)");
        assertEquals(true, octave.get(OctaveBoolean.class, "b").get(1));
        octave.eval("c = logical(0.0)");
        assertEquals(false, octave.get(OctaveBoolean.class, "c").get(1));
        
        //Matrix casts
        octave.eval("g = logical([true, false true; false false false])");
        assertEquals(false, octave.get(OctaveBoolean.class, "g").get(2, 3));
        octave.eval("h = logical([5 1 4; 6 8 4])");
        assertEquals(true, octave.get(OctaveBoolean.class, "h").get(2, 3));
        octave.eval("i = logical([5.5 3.5 12])");
        assertEquals(true, octave.get(OctaveBoolean.class, "i").get(1, 3));
        octave.close();
	}
	
	@Test
	public void castToInteger() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        //Scalar
        octave.eval("a = int8(true)");
        assertEquals(1, octave.get(OctaveInt.class, "a").get(1), 0.0);
        octave.eval("b = int8(1.0)");
        assertEquals(1, octave.get(OctaveInt.class, "b").get(1), 0.0);
        octave.eval("c = int8(1)");
        assertEquals(1, octave.get(OctaveInt.class, "c").get(1), 0.0);
        octave.eval("d = int16(true)");
        assertEquals(1, octave.get(OctaveInt.class, "d").get(1), 0.0);
        octave.eval("e = int16(1.0)");
        assertEquals(1, octave.get(OctaveInt.class, "e").get(1), 0.0);
        octave.eval("f = int16(1)");
        assertEquals(1, octave.get(OctaveInt.class, "f").get(1), 0.0);
        octave.eval("g = int32(true)");
        assertEquals(1, octave.get(OctaveInt.class, "g").get(1), 0.0);
        octave.eval("h = int32(1.0)");
        assertEquals(1, octave.get(OctaveInt.class, "h").get(1), 0.0);
        octave.eval("i = int32(1)");
        assertEquals(1, octave.get(OctaveInt.class, "i").get(1), 0.0);
        //Matrix
        octave.eval("a = int8([true false; true true])");
        assertEquals(1, octave.get(OctaveInt.class, "a").get(1), 0.0);
        octave.eval("b = int8([1.0 2.0])");
        assertEquals(1, octave.get(OctaveInt.class, "b").get(1), 0.0);
        octave.eval("c = int8([1 2])");
        assertEquals(1, octave.get(OctaveInt.class, "c").get(1), 0.0);
        octave.eval("d = int16([true false])");
        assertEquals(1, octave.get(OctaveInt.class, "d").get(1), 0.0);
        octave.eval("e = int16([1.0 2.5, 43 43.6])");
        assertEquals(1, octave.get(OctaveInt.class, "e").get(1), 0.0);
        octave.eval("f = int16([1; 2; 5; 4])");
        assertEquals(1, octave.get(OctaveInt.class, "f").get(1), 0.0);
        octave.eval("g = int32([true false; true true; false false])");
        assertEquals(1, octave.get(OctaveInt.class, "g").get(1), 0.0);
        octave.eval("h = int32([1.0 2.5 3.1 9.6; 2.4 3.4 4 5])");
        assertEquals(1, octave.get(OctaveInt.class, "h").get(1), 0.0);
        octave.eval("i = int32([1 2 3 4 5 6 7])");
        assertEquals(1, octave.get(OctaveInt.class, "i").get(1), 0.0);
        octave.close();
	}
	
	@Test
	public void castToUnsignedInteger() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        //Scalar
        octave.eval("a = uint8(true)");
        assertEquals(1, octave.get(OctaveInt.class, "a").get(1), 0.0);
        octave.eval("b = uint8(1.0)");
        assertEquals(1, octave.get(OctaveInt.class, "b").get(1), 0.0);
        octave.eval("c = uint8(1)");
        assertEquals(1, octave.get(OctaveInt.class, "c").get(1), 0.0);
        octave.eval("d = uint16(true)");
        assertEquals(1, octave.get(OctaveInt.class, "d").get(1), 0.0);
        octave.eval("e = uint16(1.0)");
        assertEquals(1, octave.get(OctaveInt.class, "e").get(1), 0.0);
        octave.eval("f = uint16(1)");
        assertEquals(1, octave.get(OctaveInt.class, "f").get(1), 0.0);
        octave.eval("g = uint32(true)");
        assertEquals(1, octave.get(OctaveInt.class, "g").get(1), 0.0);
        octave.eval("h = uint32(1.0)");
        assertEquals(1, octave.get(OctaveInt.class, "h").get(1), 0.0);
        octave.eval("i = uint32(1)");
        assertEquals(1, octave.get(OctaveInt.class, "i").get(1), 0.0);
        //Matrix
        octave.eval("a = uint8([true false; true true])");
        assertEquals(1, octave.get(OctaveInt.class, "a").get(1), 0.0);
        octave.eval("b = uint8([1.0 2.0])");
        assertEquals(1, octave.get(OctaveInt.class, "b").get(1), 0.0);
        octave.eval("c = uint8([1 2])");
        assertEquals(1, octave.get(OctaveInt.class, "c").get(1), 0.0);
        octave.eval("d = uint16([true false])");
        assertEquals(1, octave.get(OctaveInt.class, "d").get(1), 0.0);
        octave.eval("e = uint16([1.0 2.5, 43 43.6])");
        assertEquals(1, octave.get(OctaveInt.class, "e").get(1), 0.0);
        octave.eval("f = uint16([1; 2; 5; 4])");
        assertEquals(1, octave.get(OctaveInt.class, "f").get(1), 0.0);
        octave.eval("g = uint32([true false; true true; false false])");
        assertEquals(1, octave.get(OctaveInt.class, "g").get(1), 0.0);
        octave.eval("h = uint32([1.0 2.5 3.1 9.6; 2.4 3.4 4 5])");
        assertEquals(1, octave.get(OctaveInt.class, "h").get(1), 0.0);
        octave.eval("i = uint32([1 2 3 4 5 6 7])");
        assertEquals(1, octave.get(OctaveInt.class, "i").get(1), 0.0);
        octave.close();
	}
	
	//int64, uint64, uint32 (partial), float,
	@Test
	public void unsupportedCasts() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.eval("a = uint64(true)");
        try {
        	assertEquals(1, octave.get(OctaveInt.class, "a").get(1), 0.0);
        	fail();
        } catch (OctaveParseException e) {
        }
        octave.eval("b = int64(true)");
        try {
        	assertEquals(1, octave.get(OctaveInt.class, "b").get(1), 0.0);
        	fail();
        } catch (OctaveParseException e) {
        }
        octave.eval("c = uint32(2247483647)");
        try {
        	assertEquals(1, octave.get(OctaveInt.class, "c").get(1), 0.0);
        	fail();
        } catch (Throwable e) {
        }
        try {
            octave.eval("d = float(1000)");
        	fail();
        } catch (OctaveEvalException e) {
        }
        octave.close();
	}
}
