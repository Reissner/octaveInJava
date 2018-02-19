
package dk.ange.octave.io.impl;

import dk.ange.octave.io.spi.OctaveDataWriter;
import dk.ange.octave.type.OctaveObject;


/**
 * Common Writer class for primitive java types: Boolean, Double, Integer.... 
 *
 * @param <T>
 *    the type to be read in. 
 */
abstract class AbstractPrimitiveMatrixWriter<T extends OctaveObject> 
    extends OctaveDataWriter<T> {

    protected static final String NDIMS    = "# ndims: ";
    protected static final String NROWS    = "# rows: ";
    protected static final String NCOLUMNS = "# columns: ";

}
