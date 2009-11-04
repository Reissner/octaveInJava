/*
 * Copyright 2008, 2009 Ange Optimization ApS
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
package dk.ange.octave.exception;

import dk.ange.octave.type.OctaveType;

/**
 * Exception thrown when a cast fails inside the Octave object
 */
public class OctaveClassCastException extends OctaveRecoverableException {

    private final OctaveType octaveType;

    /**
     * @param cause 
     * @param octaveType 
     */
    public OctaveClassCastException(ClassCastException cause, OctaveType octaveType) {
        super(cause);
        this.octaveType =octaveType;
    }

    /**
     * @return the octaveType
     */
    public OctaveType getOctaveType() {
        return octaveType;
    }

}
