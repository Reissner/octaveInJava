package dk.ange.octave.type;

import dk.ange.octave.type.matrix.DoubleMatrix;

/**
 * FIXME todo
 * 
 * FIXME resize sync
 * 
 * FIXME should inherit from OctaveMatrix
 */
public class OctaveComplex implements OctaveObject {

    private final DoubleMatrix real;

    private final DoubleMatrix imag;

    /**
     * @param size
     */
    public OctaveComplex(final int... size) {
        real = new DoubleMatrix(size);
        imag = new DoubleMatrix(size);
    }

    /**
     * @param value
     * @param pos
     */
    public void setReal(final double value, final int... pos) {
        real.set(value, pos);
    }

    /**
     * @param pos
     * @return FIXME
     */
    public double getReal(final int... pos) {
        return real.get(pos);
    }

    /**
     * @param value
     * @param pos
     */
    public void setImag(final double value, final int... pos) {
        imag.set(value, pos);
    }

    /**
     * @param pos
     * @return FIXME
     */
    public double getImag(final int... pos) {
        return imag.get(pos);
    }

    @Override
    public OctaveObject shallowCopy() {
        // FIXME Not implemented yet
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
