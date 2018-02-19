/**
 * Package collecting various utility classes. 
 * <ul>
 * <li>
 * Some deal with readers/writers 
 * {@link eu.simuline.octave.util.TeeWriter} 
 * {@link eu.simuline.octave.util.NoCloseWriter} 
 * are special writers, 
 * {@link eu.simuline.octave.util.IOUtils} is some utility class. 
 * In conjunctions with reader/writer, 
 * there is a thread copying reader to writer, 
 * {@link eu.simuline.octave.util.ReaderWriterPipeThread} 
 * <li>
 * some deal with strings 
 * {@link eu.simuline.octave.util.StringUtil}
 * <li>
 * Finally there is a thread factory to create a named thread from a runnable. 
 * </ul>
 */
package eu.simuline.octave.util;
