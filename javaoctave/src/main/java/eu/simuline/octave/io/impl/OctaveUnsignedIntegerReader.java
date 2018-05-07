package eu.simuline.octave.io.impl;

import java.io.BufferedReader;

import eu.simuline.octave.exception.OctaveException;
import eu.simuline.octave.exception.OctaveParseException;
import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.io.spi.OctaveDataReader;
import eu.simuline.octave.type.Octave;
import eu.simuline.octave.type.OctaveInt;

public abstract class OctaveUnsignedIntegerReader extends OctaveDataReader{
	
	@Override
	public String octaveType() {
		// TODO Auto-generated method stub
		//Given the existing octave types, the valid sets should be:
		//
		return null; //Must always be overriden
	}
	
	@Override
	public OctaveInt read(BufferedReader reader) throws OctaveException {
        String line;
        line = OctaveIO.readerReadLine(reader);
    	//TODO: First check that value can be represented by int -- or change to BigInteger
        final int value = parseInt(line);
        //it will never be the case that octave returns a negative after cast uint.
        return Octave.scalarInt(value);
	}
	
	protected static int parseInt(final String string) {
		return Integer.parseInt(string);
	}
}

