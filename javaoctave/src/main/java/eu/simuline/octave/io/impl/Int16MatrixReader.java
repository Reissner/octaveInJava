package eu.simuline.octave.io.impl;

import java.io.BufferedReader;

import eu.simuline.octave.io.OctaveIO;
import eu.simuline.octave.io.spi.OctaveDataReader;
import eu.simuline.octave.type.Octave;
import eu.simuline.octave.type.OctaveDouble;
import eu.simuline.octave.type.OctaveInt;

public class Int16MatrixReader extends IntegerMatrixReader{
	@Override
	public String octaveType() {
		return "int16 matrix"; //Correct when 
	}
	
	@Override
	public OctaveInt read(BufferedReader reader) {
		return super.read(reader);
	}

	@Override
	protected OctaveInt readVectorizedMatrix(BufferedReader reader, String ndimsLine) {
		// TODO Auto-generated method stub
		return super.readVectorizedMatrix(reader, ndimsLine);
	}

	@Override
	protected OctaveInt read2dmatrix(BufferedReader reader, String rowsLine) {
		// TODO Auto-generated method stub
		return super.read2dmatrix(reader, rowsLine);
	}
	
}
