/*
 * Copyright 2009, 2012 Ange Optimization ApS
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
package eu.simuline.octave;


import static org.junit.Assert.assertTrue;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

//import org.junit.Ignore;
import org.junit.Test;

/**
 * Test getVersion
 */
public class TestVersion {

    /**
     * Test getVersion
     */
    @Test public void testVersion() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.getOctaveVersion();
    }

    /**
     * Test that the version is a version we know. 
     * If this test fails the fix will usually be 
     * to add the new version to the Set of known versions.
     */
    @Test public void testKnownVersion() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        assertTrue("Version '" + octave.getOctaveVersion() + "' is not known", 
        	octave.isOctaveVersionAllowed());
        assertEquals("Wrong octave version",
		   "5.2.0",
		   octave.getOctaveVersion());
    }
    
    // TBD: reactivate 
    // Currently, this does not work with failsafe plugin 
    @Test public void testOctaveJavaVersion() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.getOctaveInJavaVersion();
//        assertEquals("wrong version of octavejava", 
//        	"0.7-SNAPSHOT",
//        	octave.getOctaveInJavaVersion());
     }
    
    // TBD: reactivate 
    // Currently, this does not work with failsafe plugin 
    @Test public void testMeta() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        octave.getVendor();
//        assertEquals("Wrong vendor", 
//        	"1.0",
//        	octave.getVendor());
    }

    @Test public void testPackageInstalled() {
	final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
	Collection<String> names = octave.getNamesOfPackagesInstalled();
	assertTrue(names.contains("quaternion"));
    }
}
