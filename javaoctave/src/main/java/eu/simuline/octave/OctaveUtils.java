package eu.simuline.octave;

import java.util.Collection;
import java.util.HashSet;

import java.util.regex.Pattern;

import eu.simuline.octave.type.OctaveCell;
import eu.simuline.octave.type.OctaveString;

/**
 * Small utility functions that can be used with JavaOctave.
 * 
 * Holder class for static functions.
 */
public final class OctaveUtils {

    // TBC: in which version does this occur? 
    // seemingly not in 5.2.0. 
    // Just nargin and that only within functions. 
    // but in that context also nargout would be needed. 
    // Note also that it is nargin not __nargin__. 
    // In particular this variable is not returned by whos. 
    /**
     * A variable name not to be listed by {@link #listVars(OctaveEngine)}. 
     */
    private static final String NARGIN = "__nargin__";

    private OctaveUtils() {
        throw new UnsupportedOperationException("Do not instantiate");
    }

    /**
     * Returns a collection of variables defined 
     * excluding variables like {@link #NARGIN} and {@link OctaveEngine#ANS} 
     * but also those that are most likely to be created by this software. TBD: clarification 
     * 
     * @param octave
     *    some octave engine. 
     * @return collection of variables
     */
    public static Collection<String> listVars(final OctaveEngine octave) {
	// Justification: 3.0 are the earliest versions. 
	// all later ones don't use '-v' any more 
	String script = octave.getOctaveVersion().startsWith("3.0.") ? "ans=whos -v()" : "ans=whos()";
	octave.eval(script);
	// TBD: clarify: if we use who instead of whos, this can be simplified.  
	octave.eval("{ans.name}");
	OctaveCell cell = octave.get(OctaveCell.class, OctaveEngine.ANS);
	// TBD: this can be unified with OctaveEngine.getNamesOfPackagesInstalled()
	int len = cell.dataSize();
	Collection<String> collection = new HashSet<String>();
	//cell.getSize(i)
	for (int idx = 0; idx < len; idx++) {
	    collection.add(cell.get(OctaveString.class, 1, idx+1).getString());
	}
	collection.removeIf(p -> NARGIN.equals(p));
	collection.removeIf(p -> OctaveEngine.ANS.equals(p));
	// TBD: eliminate magic literal 
	Pattern pattern = Pattern.compile("javaoctave_[0-9a-f]{12}_eval");
	collection.removeIf(p -> pattern.matcher(p).matches());
	return collection;
    }

}
