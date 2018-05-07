package eu.simuline.octave.io.impl;

import java.io.BufferedReader;

import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.io.spi.OctaveDataReader;
import eu.simuline.octave.type.Octave;
import eu.simuline.octave.type.OctaveInt;

public abstract class OctaveIntegerReader extends OctaveDataReader{
	
	//Clear issue here. 
	//Even though int can be supported, UINT, as usual, messes up the initial pardigm
	@Override
	public String octaveType() {
		// TODO Auto-generated method stub
		//Given the existing octave types, the valid sets should be:
		//
		return null; //Must always be overriden
	}
	
	@Override
	public OctaveInt read(BufferedReader reader) {
        String line;
        line = OctaveIO.readerReadLine(reader);
        final int value = parseInt(line);
        return Octave.scalarInt(value);
	}
	
	protected static int parseInt(final String string) {
		return Integer.parseInt(string);
	}
}

