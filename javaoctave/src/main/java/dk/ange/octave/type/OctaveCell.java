/*
 * Copyright 2007, 2008 Ange Optimization ApS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * @author Kim Hansen
 */
package dk.ange.octave.type;

import dk.ange.octave.exception.OctaveClassCastException;
import dk.ange.octave.type.cast.Cast;
import dk.ange.octave.type.matrix.AbstractGenericMatrix;
import dk.ange.octave.type.matrix.GenericMatrix;

/**
 * Nd cells. 
 */
public final class OctaveCell 
    extends GenericMatrix<OctaveObject> implements OctaveObject {

    /**
     * For some reason, the default value is not stored as such, 
     * but as a <code>null</code> value. 
     * Thus both in methods {@link #set(OctaveObject, int[])} 
     * and in {@link #get(int[])}, the value must be reconstructed. 
     * **** The reason for that, i cannot figure out. 
     * Instead I would pressume, that <code>null</code> 
     * is translated into <code>[]</code>, 
     * as elsewhere in octave's java-interface. 
     */
    private static final OctaveObject DEFAULT_VALUE = new OctaveDouble(0, 0);

    /**
     * Warn about usage of old constructor. 
     * 
     * @deprecated use: new OctaveCell(0, 0)
     */
    @Deprecated
    public OctaveCell() {
        super();
    }

    /**
     * @param size
     */
    public OctaveCell(final int... size) {
        super(size);
    }

    private OctaveCell(final AbstractGenericMatrix<OctaveObject[]> o) {
        super(o);
    }

    @Override
    @SuppressWarnings("PMD.AvoidThrowingNullPointerException")
    public void set(OctaveObject value, int... pos) {
        if (value == null) {
	    // **** I would pressume that one should put [] instead. 
            throw new NullPointerException("Cannot put null into OctaveCell");
        }
        if (DEFAULT_VALUE.equals(value)) {
            super.set(null, pos);
        } else {
            super.set(value, pos);
        }
    }

    @Override
    public OctaveObject get(int... pos) {
        OctaveObject get = super.get(pos);
         if (get == null) {
            get = DEFAULT_VALUE;
        }
	return get.shallowCopy();

    }

    /**
     * @param <T>
     * @param pos
     * @param castClass
     *            Class to cast to
     * @return shallow copy of value for this key.
     * @throws OctaveClassCastException
     *             if the object can not be cast to a castClass
     */
    public <T extends OctaveObject> T get(final Class<T> castClass,
					  final int... pos) {
        return Cast.cast(castClass, get(pos));
    }

    @Override
    public OctaveCell shallowCopy() {
        return new OctaveCell(this);
    }

}
