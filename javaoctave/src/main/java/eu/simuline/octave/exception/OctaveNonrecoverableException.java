/*
 * Copyright 2008 Ange Optimization ApS
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
package eu.simuline.octave.exception;

/**
 * An exception in the Octave engine that did break the octave process, 
 * any action on the Octave object now will cause a
 * new OctaveNonrecoverableException.
 */
public abstract class OctaveNonrecoverableException extends OctaveException {

    /**
     * Constructor. 
     *
     * @see Throwable
     */
    public OctaveNonrecoverableException() {
        // Do nothing
    }

    /**
     * Constructor. 
     *
     * @param message
     * @see Throwable
     */
    public OctaveNonrecoverableException(final String message) {
        super(message);
    }

    /**
     * Constructor. 
     *
     * @param cause
     * @see Throwable
     */
    public OctaveNonrecoverableException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructor. 
     *
     * @param message
     * @param cause
     * @see Throwable
     */
    public OctaveNonrecoverableException(final String message, 
					 final Throwable cause) {
        super(message, cause);
    }

}
