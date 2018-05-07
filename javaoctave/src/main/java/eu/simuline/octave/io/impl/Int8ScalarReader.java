package eu.simuline.octave.io.impl;

import java.io.BufferedReader;

import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.io.spi.OctaveDataReader;
import eu.simuline.octave.type.Octave;
import eu.simuline.octave.type.OctaveInt;

public class Int8ScalarReader extends OctaveIntegerReader { 

	@Override
	public String octaveType() {
		return "int8 scalar"; //Correct when 
	}
	
	@Override
	public OctaveInt read(BufferedReader reader) {
		return super.read(reader);
	}
	
}
