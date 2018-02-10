/**
 * Package collecting various utility classes. 
 * <ul>
 * <li>
 * Some deal with readers/writers 
 * {@link dk.ange.octave.util.TeeWriter} 
 * {@link dk.ange.octave.util.NoCloseWriter} 
 * are special writers, 
 * {@link dk.ange.octave.util.IOUtils} is some utility class. 
 * In conjunctions with reader/writer, 
 * there is a thread copying reader to writer, 
 * {@link dk.ange.octave.util.ReaderWriterPipeThread} 
 * <li>
 * some deal with strings 
 * {@link dk.ange.octave.util.StringUtil}
 * <li>
 * Finally there is a thread factory to create a named thread from a runnable. 
 * </ul>
 */
package dk.ange.octave.util;
