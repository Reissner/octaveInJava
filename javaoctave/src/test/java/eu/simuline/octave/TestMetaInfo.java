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

import java.io.File;
import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//import org.junit.Ignore;
import org.junit.Test;

import eu.simuline.octave.OctaveEngine.PackageDesc;

/**
 * Test meta info like version (both octave and javaoctave bridge), 
 * vendor, packages installed. 
 */
public class TestMetaInfo {

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

    @Test public void testPackageLoading() {
	final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
	Map<String, PackageDesc> name2pkg = octave.getPackagesInstalled();
	assertTrue(name2pkg.keySet().contains("quaternion"));
	assertTrue(!name2pkg.get("quaternion").isLoaded);
	octave.eval("pkg('load', 'quaternion')");
	name2pkg = octave.getPackagesInstalled();
	assertTrue( name2pkg.get("quaternion").isLoaded);
	octave.eval("pkg('unload', 'quaternion')");
	name2pkg = octave.getPackagesInstalled();
	assertTrue(!name2pkg.get("quaternion").isLoaded);
    }

    /**
     * Test that the list of variables is empty. 
     */
    @Test public void testListVarsEmpty() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
        final Collection<String> collection = octave.getVarNames();
        assertEquals(collection.toString(), 0, collection.size());
        octave.close();
    }

    /**
     * Test that the list of variables has 1 or 2 entries. 
     * **** better: test concrete collection of variables. 
     */
    @Test public void testListVarsOneTwo() {
        final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();

        octave.eval("my_var = 42;");
        final Collection<String> collection1 = octave.getVarNames();
        assertEquals(collection1.toString(), 1, collection1.size());

        octave.eval("1 + 2;");
        octave.eval("my_other_var = 42;");
        final Collection<String> collection2 = octave.getVarNames();
        assertEquals(collection2.toString(), 2, collection2.size());

        octave.close();
    }

    @Test public void testOnOS() {
	final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
	assertEquals(File.separator, octave.getFilesep());
    }

    /**
     * Tests {@link OctaveEngine#getInstHomeDir()}, {@link OctaveEngine#getOctaveVersion()} 
     * and {@link OctaveEngine#getFileForName(String)} at the same time.
     */
    @Test public void testBasedOnWhich() {
	final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();

 	String name = "pkg";
 	OctaveEngine.NameDesc desc = octave.getDescForName(name);
 	assertEquals(name, desc.name);
	assertEquals("function", desc.type);

	File fileCmp = new File(octave.getInstHomeDir(), "share");
	fileCmp = new File(fileCmp, "octave");
	fileCmp = new File(fileCmp, octave.getOctaveVersion());
	fileCmp = new File(fileCmp, "m");
	fileCmp = new File(fileCmp, "pkg");
	fileCmp = new File(fileCmp, "pkg.m");
	assertEquals(fileCmp, desc.file);

	name = "cos";
 	desc = octave.getDescForName(name);
 	assertEquals(name, desc.name);
	assertEquals("built-in function", desc.type);

	fileCmp = new File("libinterp", "corefcn");
	fileCmp = new File(fileCmp, "mappers.cc");
	assertEquals(fileCmp, desc.file);
    }

    /**
     * Tests on the java directory.
     */
    @Test public void testJavaDir() {
 	final OctaveEngine octave = new OctaveEngineFactory().getScriptEngine();
 	String name = "javaaddpath";
 	OctaveEngine.NameDesc desc = octave.getDescForName(name);
 	assertEquals(name, desc.name);
 	assertEquals("function", desc.type);
 	assertEquals(octave.getJavaHomeDir(), desc.file.getParentFile());
   }

}
