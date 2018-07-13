
package eu.simuline.octave.io.impl;

import eu.simuline.octave.io.spi.OctaveDataWriter;
import eu.simuline.octave.type.matrix.AbstractGenericMatrix;

import java.io.IOException;
import java.io.Writer;

/**
 * Common Writer class for primitive java types: Boolean, Double, Integer.... 
 *
 * @param <T>
 *    the type to be written out 
 *    which has to extend {@link AbstractGenericMatrix}. 
 */
abstract class AbstractPrimitiveMatrixWriter
    <T extends AbstractGenericMatrix<?,?>> 
    extends OctaveDataWriter<T> {

    protected static final String NDIMS    = "# ndims: ";
    protected static final String NROWS    = "# rows: ";
    protected static final String NCOLUMNS = "# columns: ";

    // prevents instantiation (as does keyword abstract); for pmd only 
    protected AbstractPrimitiveMatrixWriter() {
    }

    // **** indicates that it would be a good idea 
    // to merge reader and writer 
    // and to have one OctaveObject for each octave type. 
    protected abstract String octaveMatrixType();

    // **** indicates that it would be a good idea 
    // to merge reader and writer 
    // and to have one OctaveObject for each octave type. 
    protected abstract String octaveScalarType();

    // is used for integer types only, for logical and floating point, 
    // it is overwritten 
    public void write(final Writer writer, 
		      final T octaveMatrix) throws IOException {
	if (octaveMatrix.getSizeLength() == 2 && 
	    octaveMatrix.getSize(1) == 1 && 
	    octaveMatrix.getSize(2) == 1) {

	    writer.write("# type: " + octaveScalarType() + "\n");
	    writer.write(octaveMatrix.getPlainString(0) + "\n");
	} else {
	    writer.write("# type: " + octaveMatrixType() + "\n");
	    // **** note: unlike for floating types and bool, 
	    // for integer types, 
	    // there is no special case for 2 dimensions, i.e. matrices 
	    // using saveData2d(writer, octaveMatrix);
	    saveDataVectorized(writer, octaveMatrix);
	}
    }

    protected void saveDataVectorized(final Writer writer, 
				      final T octaveMatrix) 
	throws IOException {

        writer.write(NDIMS + octaveMatrix.getSizeLength() + "\n");
        for (int idx = 1; idx <= octaveMatrix.getSizeLength(); idx++) {
            writer.write(" " + octaveMatrix.getSize(idx));
        }

	int len = octaveMatrix.dataSize();
        for (int idx = 0; idx < len; idx++) {
            writer.write("\n " + octaveMatrix.getPlainString(idx));
	}

        writer.write("\n");
    }

}
