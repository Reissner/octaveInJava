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

/**
 * Scalar that inherits from 1x1 matrix
 */
public class OctaveScalar extends OctaveDouble {

    /**
     * @param value
     */
    public OctaveScalar(final double value) {
        super(1, 1);
        this.data[0] = value;
    }

    /**
     * @return Returns the value of this object
     */
    public double getDouble() {
        return data[0];
    }

    @Override
    public OctaveScalar shallowCopy() {
        return new OctaveScalar(data[0]);
    }

    /**
     * Sets value
     * 
     * @param value
     */
    public void set(final double value) {
        this.data[0] = value;
    }

    @Override
    public String toString() {
        final String retValue = "OctaveScalar(" //
                + this.data[0] //
                + ")";
        return retValue;
    }

}
