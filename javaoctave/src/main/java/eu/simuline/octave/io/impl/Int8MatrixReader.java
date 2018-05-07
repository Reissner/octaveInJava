package eu.simuline.octave.io.impl;

import java.io.BufferedReader;

import eu.simuline.octave.type.OctaveInt;

public class Int8MatrixReader extends IntegerMatrixReader {

	@Override
	public String octaveType() {
		return "int8 matrix"; //Correct when 
	}
	
	@Override
	public OctaveInt read(BufferedReader reader) {
		return super.read(reader);
	}

	@Override
	protected OctaveInt readVectorizedMatrix(BufferedReader reader, String ndimsLine) {
		return super.readVectorizedMatrix(reader, ndimsLine);
	}

	@Override
	protected OctaveInt read2dmatrix(BufferedReader reader, String rowsLine) {
		return super.read2dmatrix(reader, rowsLine);
	}
}
