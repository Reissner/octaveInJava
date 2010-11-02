package dk.ange.octave.type.cast;

import dk.ange.octave.type.OctaveComplex;
import dk.ange.octave.type.OctaveDouble;

/**
 * Cast OctaveDouble to OctaveComplex
 */
public class DoubleToComplexCaster implements Caster<OctaveDouble, OctaveComplex> {

    public OctaveComplex cast(final OctaveDouble from) {
        return new OctaveComplex(from.getData(), from.getSize());
    }

    public Class<OctaveDouble> from() {
        return OctaveDouble.class;
    }

    public Class<OctaveComplex> to() {
        return OctaveComplex.class;
    }

}
