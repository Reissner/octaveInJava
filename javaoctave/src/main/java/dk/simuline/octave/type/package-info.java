/**
 * Contains all octave types. 
 * The base interface for all types is {@link dk.ange.octave.type.OctaveObject} 
 * defining a single method only: 
 * {@link dk.ange.octave.type.OctaveObject#shallowCopy()}. 
 *
 * Classes implementing {@link dk.ange.octave.type.OctaveObject}: 
 * <ul>
 * <li>
 * {@link dk.ange.octave.type.OctaveBoolean} extending 
 * {@link dk.ange.octave.type.matrix.BooleanMatrix}, 
 * <li>
 * {@link dk.ange.octave.type.OctaveCell} extending 
 * {@link dk.ange.octave.type.matrix.GenericMatrix}, 
 * <li>
 * {@link dk.ange.octave.type.OctaveDouble} extending 
 * {@link dk.ange.octave.type.matrix.DoubleMatrix}, 
 * <li>
 * {@link dk.ange.octave.type.OctaveInt} extending 
 * {@link dk.ange.octave.type.matrix.IntMatrix}, 
 * <li>
 * Without base class, i.e. based on object: 
 * {@link dk.ange.octave.type.OctaveSparseBoolean}, 
 * {@link dk.ange.octave.type.OctaveString}, 
 * {@link dk.ange.octave.type.OctaveStruct}, 
 * {@link dk.ange.octave.type.OctaveFunctionHandle}, 
 * {@link dk.ange.octave.type.OctaveComplex}, 
 * and special case {@link dk.ange.octave.type.OctaveFake}. 
 * </ul>
 * Special is {@link dk.ange.octave.type.Octave} 
 * the use of which is not clear to me... 
 * Provides a single static method 
 * {@link dk.ange.octave.type.Octave#scalar(double)}. 
 */
package dk.ange.octave.type;
