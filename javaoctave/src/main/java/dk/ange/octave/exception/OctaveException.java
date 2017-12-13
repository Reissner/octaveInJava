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

import dk.ange.octave.exec.OctaveExec; // for javadoc only 

/**
 * Base exception class for the JavaOctave project. 
 * RuntimeException which handles the destroyed marker. 
 * Special methods: {@link #setDestroyed(boolean)} and {@link #isDestroyed()}. 
 * Note that derived classes shall implement all constructors ****. 
 */
public abstract class OctaveException extends RuntimeException {

    /**
     * Set to true on exceptions thrown from an Octave object 
     * when the reason for the exception is that the object has 
     * been asked to destroy its octave process. 
     */
    private boolean destroyed = false;

    /**
     * Constructor. 
     *
     * @see Throwable
     */
    public OctaveException() {
        // Do nothing
    }

    /**
     * Constructor. 
     *
     * @param message
     * @see Throwable
     */
    public OctaveException(final String message) {
        super(message);
    }

    /**
     * Constructor. 
     *
     * @param cause
     * @see Throwable
     */
    public OctaveException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructor 
     * required by {@link  OctaveExec#reInstException(OctaveException)} 
     *
     * @param message
     * @param cause
     * @see Throwable
     */
    public OctaveException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @return destroyed
     */
    public final boolean isDestroyed() {
        return this.destroyed;
    }

    /**
     * @param destroyed
     */
    public final void setDestroyed(final boolean destroyed) {
        this.destroyed = destroyed;
    }

}
