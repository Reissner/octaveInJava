/**
 * Contains all octave types. 
 * The base interface for all types is {@link OctaveObject} 
 * defining a single method only: {@link OctaveObject#shallowCopy()}. 
 *
 * Classes implementing {@link OctaveObject}: 
 * <ul>
 * <li>
 * {@link OctaveBoolean} extending 
 * {@link dk.ange.octave.type.matrix.BooleanMatrix}, 
 * <li>
 * {@link OctaveCell} extending 
 * {@link dk.ange.octave.type.matrix.GenericMatrix}, 
 * <li>
 * {@link OctaveDouble} extending 
 * {@link dk.ange.octave.type.matrix.DoubleMatrix}, 
 * <li>
 * {@link OctaveInt} extending 
 * {@link dk.ange.octave.type.matrix.IntMatrix}, 
 * <li>
 * Without base class, i.e. based on object: 
 * {@link OctaveSparseBoolean}, {@link OctaveString}, 
 * {@link OctaveStruct}, {@link OctaveFunctionHandle}, {@link OctaveComplex}, 
 * and special case {@link OctaveFake}. 
 * </ul>
 * Special is {@link Octave} the use of which is not clear to me... 
 * Provides a single static method {@link Octave# scalar(double)}. 
 */
package dk.ange.octave.type;
