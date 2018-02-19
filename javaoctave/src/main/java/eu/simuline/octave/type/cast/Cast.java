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
package eu.simuline.octave.type.cast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.spi.ServiceRegistry;

import eu.simuline.octave.exception.OctaveClassCastException;
import eu.simuline.octave.exception.OctaveCastServiceException;
import eu.simuline.octave.type.OctaveObject;

/**
 * Helper class for the auto cast functionality. 
 * Currently, only a single caster is implemented: 
 * {@link DoubleToComplexCaster} but this can be dynamically extended. 
 */
public final class Cast {

    private static final int PRIME = 31;
 
    /**
     * Maps a class pair to an according caster. 
     */
    private static Map<ClassPair<?, ?>, Caster<?, ?>> casterMap;

    private Cast() {
        throw new UnsupportedOperationException("Do not instantiate");
    }

    @SuppressWarnings("unchecked")
    private static synchronized void initIfNecessary() {
        if (casterMap == null) {
            casterMap = new HashMap<ClassPair<?, ?>, Caster<?, ?>>();
            @SuppressWarnings("rawtypes")
            final Iterator<Caster> sp = ServiceRegistry
		.lookupProviders(Caster.class);
           while (sp.hasNext()) {
                register(sp.next());
            }
        }
    }

    private static <F, T> void register(final Caster<F, T> caster) {
        final ClassPair<F, T> cp = new ClassPair<F, T>(caster.from(),
						       caster.to());
	Caster<?, ?> overwritten = casterMap.put(cp, caster);
        if (overwritten != null) {
            throw new OctaveCastServiceException("casterMap.containsKey(cp)");
        }
    }

    /**
     * Cast and transform the object. 
     * 
     * @param <F>
     * @param <T>
     * @param toClass
     * @param from
     * @return The transformed object
     */
    public static <F extends OctaveObject, 
		   T extends OctaveObject> T cast(final Class<T> toClass, 
						  final F from) {
        if (from == null) {
            return null;
        }
        if (toClass.isInstance(from)) {
            return toClass.cast(from);
        }
        final ClassPair<F, T> cp = new ClassPair<F, T>(unsafeGetClass(from),
						       toClass);
        final Caster<F, T> caster = casterMapGet(cp);
        if (caster == null) {
            throw new OctaveClassCastException(null, from, toClass);
        }
        return caster.cast(from);
    }

    @SuppressWarnings("unchecked")
    private static <F> Class<F> unsafeGetClass(final F from) {
        return (Class<F>) from.getClass();
    }

    @SuppressWarnings("unchecked")
    private static <F extends OctaveObject, 
		    T extends OctaveObject> 
    Caster<F, T> casterMapGet(final ClassPair<F, T> cp) {
        initIfNecessary();
        if (!casterMap.containsKey(cp)) {
            return null;
        }
        final Caster<F, T> caster = (Caster<F, T>) casterMap.get(cp);
        if (!caster.from().equals(cp.from)) {
            throw new OctaveCastServiceException
		("!caster.from().equals(cp.from)");
        }
        if (!caster.to().equals(cp.to)) {
            throw new OctaveCastServiceException("!caster.to().equals(cp.to)");
        }
        return caster;
    }

    /**
     * Represents a pair of classes, 
     * essentially a cast from one to the other class. 
     *
     * @param <F> from class 
     * @param <T> to   class 
     */
    private static class ClassPair<F, T> {
        ClassPair(final Class<F> from, final Class<T> to) {
            this.from = from;
            this.to = to;
        }

        private final Class<F> from;

        private final Class<T> to;

        @Override
        public int hashCode() {
            int result = 1;
            result = PRIME * result + ((from == null) ? 0 : from.hashCode());
            result = PRIME * result + ((to   == null) ? 0 :   to.hashCode());
            return result;
        }

	@Override
	public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ClassPair<?, ?> other = (ClassPair<?, ?>) obj;
            if (from == null) {
                if (other.from != null) {
                    return false;
                }
            } else if (!from.equals(other.from)) {
                return false;
            }
            if (to == null) {
                if (other.to != null) {
                    return false;
                }
            } else if (!to.equals(other.to)) {
                return false;
            }
            return true;
        }
    }

}
