package eu.simuline.octave.io.impl;

import eu.simuline.octave.type.matrix.AbstractGenericMatrix;

import java.io.IOException;
import java.io.Writer;

/**
 * Describe class AbstractLogicalFloatingPointWriter here.
 *
 *
 * Created: Sun Jul  8 00:33:34 2018
 *
 * @author <a href="mailto:ernst.reissner@simuline.eu">Ernst Reissner</a>
 * @version 1.0
 */
abstract class AbstractLogicalFloatingPointWriter 
    <T extends AbstractGenericMatrix<?>> 
    extends AbstractPrimitiveMatrixWriter<T> {


    @Override
    public void write(final Writer writer, 
		      final T octaveMatrix) throws IOException {
        if (octaveMatrix.getSizeLength() > 2) {
	    writer.write("# type: " + octaveMatrixType() + "\n");
            saveDataVectorized(writer, octaveMatrix);
        } else {
            if (octaveMatrix.getSizeLength() == 2 && 
		octaveMatrix.size(1) == 1 && 
		octaveMatrix.size(2) == 1) {

                writer.write("# type: " + octaveScalarType() + "\n");
		writer.write(octaveMatrix.getPlainString(0) + "\n");
            } else {
		writer.write("# type: " + octaveMatrixType() + "\n");
		saveData2d(writer, octaveMatrix);
	    }
        }
    }


    protected void saveData2d(final Writer writer, 
			      final T octaveMatrix) 
	throws IOException {

        final int nrows = octaveMatrix.getSize(1);
        final int ncols = octaveMatrix.getSizeLength() > 1 
	    ? octaveMatrix.getSize(2) : 1;
        writer.write(NROWS + nrows + "\n");
        writer.write(NCOLUMNS + ncols + "\n");
        for (int row = 0; row < nrows; row++) {
            for (int col = 0; col < ncols; col++) {
               writer.write(" " + 
			    octaveMatrix.getPlainString(row + col * nrows));
            }
            writer.write('\n');
        }
    }

}
