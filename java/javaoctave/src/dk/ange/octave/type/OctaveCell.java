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

import dk.ange.octave.type.matrix.GenericMatrix;

/**
 * Nd cells
 */
public class OctaveCell extends GenericMatrix<OctaveObject> implements OctaveObject {

    private static final OctaveObject DEFAULT_VALUE = new OctaveMatrix(0, 0);

    /**
     * Warn about usage of old constructor
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

    /**
     * @param data
     * @param size
     */
    private OctaveCell(final Object[] data, final int... size) {
        super(data, size);
    }

    @Override
    public void set(final OctaveObject value, final int... pos) {
        if (value == null) {
            throw new NullPointerException("Can not put null into OctaveCell");
        }
        if (DEFAULT_VALUE.equals(value)) {
            super.set(null, pos);
        } else {
            super.set(value, pos);
        }
    }

    @Override
    public OctaveObject get(final int... pos) {
        final OctaveObject get = super.get(pos);
        if (get == null) {
            return DEFAULT_VALUE.shallowCopy();
        } else {
            return get.shallowCopy();
        }
    }

    @Override
    public OctaveObject shallowCopy() {
        return new OctaveCell(data, size);
    }

}
