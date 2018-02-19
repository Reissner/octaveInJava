package eu.simuline.octave.type.cast;

import eu.simuline.octave.type.OctaveComplex;
import eu.simuline.octave.type.OctaveDouble;

/**
 * Cast OctaveDouble to OctaveComplex. 
 */
public final class DoubleToComplexCaster 
    implements Caster<OctaveDouble, OctaveComplex> {

    @Override
    public OctaveComplex cast(final OctaveDouble from) {
        return new OctaveComplex(from);
    }

    @Override
    public Class<OctaveDouble> from() {
        return OctaveDouble.class;
    }

    @Override
    public Class<OctaveComplex> to() {
        return OctaveComplex.class;
    }

}
