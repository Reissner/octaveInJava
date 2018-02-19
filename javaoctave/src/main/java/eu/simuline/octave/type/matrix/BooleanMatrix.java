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
package eu.simuline.octave.type.matrix;

import java.util.Arrays;

/**
 * General matrix with boolean values. 
 */
public class BooleanMatrix extends AbstractGenericMatrix<boolean[]> {

    /**
     * @param size
     */
    protected BooleanMatrix(final int... size) {
        super(size);
    }

    /**
     * @param data
     * @param size
     */
    protected BooleanMatrix(final boolean[] data, final int... size) {
        super(data, size);
    }

    protected final boolean[] newD(final int size) {
        return new boolean[size];
    }

    protected final int dataLength() {
        return this.data.length;
    }

    protected final void dataFillInit(final int fromIndex, final int toIndex) {
        Arrays.fill(this.data, fromIndex, toIndex, false);
    }

    protected final boolean dataEquals(final int usedLength,
				       final boolean[] otherData) {
        for (int i = 0; i < usedLength; i++) {
            if (this.data[i] != otherData[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Set the value. 
     * 
     * @param value
     * @param pos
     */
    public final void set(final boolean value, final int... pos) {
        resizeUp(pos);
        this.data[pos2ind(pos)] = value;
    }

    /**
     * Get the value. 
     * 
     * @param pos
     * @return value at pos
     */
    public final boolean get(final int... pos) {
        return this.data[pos2ind(pos)];
    }

}
