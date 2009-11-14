package dk.ange.octave.type;

/**
 * Place holder for factory methods
 */
public final class Octave {

    private Octave() {
        throw new UnsupportedOperationException("Do not instantiate");
    }

    /**
     * @param d
     * @return New OctaveDouble with a single value
     */
    public static OctaveDouble scalar(final double d) {
        final OctaveDouble od = new OctaveDouble(1, 1);
        od.set(d, 1, 1);
        return od;
    }

}
