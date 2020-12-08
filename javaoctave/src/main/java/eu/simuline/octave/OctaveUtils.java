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
 * @deprecated
 */
public final class OctaveUtils {


    private OctaveUtils() {
        throw new UnsupportedOperationException("Do not instantiate");
    }

    /**
     * Returns a collection of variables defined 
     * excluding variables like {@link OctaveEngine#NARGIN} and {@link OctaveEngine#ANS} 
     * but also those that are most likely to be created by this software. TBD: clarification 
     * 
     * @param octave
     *    some octave engine. 
     * @return collection of variables
     * @deprecated
     * use {@link OctaveEngine#getVarNames()} instead. 
     */
    public static Collection<String> listVars(final OctaveEngine octave) {
	// Justification: 3.0 are the earliest versions. 
	// all later ones don't use '-v' any more 
	String script = octave.getOctaveVersion().startsWith("3.0.") ? "ans=whos -v()" : "ans=whos()";
	octave.eval(script);
	// TBD: clarify: if we use who instead of whos, this can be simplified.  
	octave.eval("{ans.name}");
	OctaveCell cell = octave.get(OctaveCell.class, "ans");
	// TBD: this can be unified with OctaveEngine.getNamesOfPackagesInstalled()
	int len = cell.dataSize();
	Collection<String> collection = new HashSet<String>();
	//cell.getSize(i)
	for (int idx = 0; idx < len; idx++) {
	    collection.add(cell.get(OctaveString.class, 1, idx+1).getString());
	}
	collection.removeIf(p -> "__nargin__".equals(p));
	collection.removeIf(p -> "ans".equals(p));
	// TBD: eliminate magic literal 
	Pattern pattern = Pattern.compile("javaoctave_[0-9a-f]{12}_eval");
	collection.removeIf(p -> pattern.matcher(p).matches());
	return collection;
    }

}
