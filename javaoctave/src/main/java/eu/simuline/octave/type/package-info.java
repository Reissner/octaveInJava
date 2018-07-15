/**
 * Contains all octave types which are supported. 
 * The base interface for all types 
 * is {@link eu.simuline.octave.type.OctaveObject} 
 * defining a single method only: 
 * {@link eu.simuline.octave.type.OctaveObject#shallowCopy()}. 
 *
 * Classes implementing {@link eu.simuline.octave.type.OctaveObject}: 
 * <ul>
 * <li>
 * {@link eu.simuline.octave.type.OctaveBoolean} extending 
 * {@link eu.simuline.octave.type.matrix.AbstractGenericMatrix}, 
 * <li>
 * {@link eu.simuline.octave.type.OctaveDouble} extending 
 * {@link eu.simuline.octave.type.matrix.AbstractGenericMatrix}, 
 * <li>
 * {@link eu.simuline.octave.type.OctaveInt} extending 
 * {@link eu.simuline.octave.type.matrix.AbstractGenericMatrix}, 
 * <li>
 * {@link eu.simuline.octave.type.OctaveCell} extending 
 * {@link eu.simuline.octave.type.matrix.GenericMatrix}, 
 * <li>
 * Without base class, i.e. based on object: 
 * {@link eu.simuline.octave.type.OctaveSparseBoolean}, 
 * {@link eu.simuline.octave.type.OctaveString}, 
 * {@link eu.simuline.octave.type.OctaveStruct}, 
 * {@link eu.simuline.octave.type.OctaveFunctionHandle}, 
 * {@link eu.simuline.octave.type.OctaveComplex}, 
 * and special case {@link eu.simuline.octave.type.OctaveFake}. 
 * </ul>
 * Special is {@link eu.simuline.octave.type.Octave} 
 * the use of which is not clear to me... 
 * Provides a single static method 
 * {@link eu.simuline.octave.type.Octave#scalar(double)}. 
 */
package eu.simuline.octave.type;
