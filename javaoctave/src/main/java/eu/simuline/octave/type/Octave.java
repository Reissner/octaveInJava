/*
 * Copyright 2009 Ange Optimization ApS
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
package eu.simuline.octave.type;

/**
 * Place holder for factory methods. 
 */
public final class Octave {

    /**
     * Constant true of corresponding with octave bool. 
     */
    private final static OctaveBoolean TRUE = 
	new OctaveBoolean(new boolean[] {true}, 1, 1);

    /**
     * Constant false of corresponding with octave bool. 
     */
    private final static OctaveBoolean FALSE = 
	new OctaveBoolean(new boolean[] {false}, 1, 1);

    private Octave() {
        throw new UnsupportedOperationException("Do not instantiate");
    }

    /**
     * Returns java counterparts of octave scalars. 
     *
     * @param d
     * @return New OctaveDouble with a single value
     */
    // used by user and in tests only 
    public static OctaveDouble scalar(final double d) {
        final OctaveDouble od = new OctaveDouble(1, 1);
        od.set(d, 1, 1);
        return od;
    }

    /**
     * Returns java counterparts of octave bools. 
     *
     * @param b
     * @return New OctaveBoolean with a single value
     */
    // used by user only 
    public static OctaveBoolean bool(final boolean b) {
        return b ? TRUE : FALSE;
    }

}
