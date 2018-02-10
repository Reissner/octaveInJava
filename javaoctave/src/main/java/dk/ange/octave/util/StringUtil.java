/*
 * Copyright 2007, 2008 Ange Optimization ApS
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
package dk.ange.octave.util;

import java.util.Map;
import java.util.HashMap;

/**
 * Class for holding static utility functions for string handling: quoting. 
 * 
 * @author Kim Hansen
 */
public final class StringUtil {

    private static final Map<Character, String> CHAR2STR;

    static {
	CHAR2STR = new HashMap<Character, String>();
	CHAR2STR.put('"',  "\\\"");
	CHAR2STR.put('\\', "\\\\");
	CHAR2STR.put('\n',  "\\n");
	CHAR2STR.put('\r',  "\\r");
	CHAR2STR.put('\f',  "\\f");
	CHAR2STR.put('\b',  "\\b");
	CHAR2STR.put('\t',  "\\t");
    } // static 

    private StringUtil() {
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static void appendChar(StringBuffer buf, char c) {
	String str = CHAR2STR.get(c);
	if (str != null) {
	    buf.append(str);
	    return;
	}
	if (c < 0x20) { // NOPMD 
	    buf.append("\\u00");
	    int x = c / 0x10;
	    buf.append((char) (x < 0xA ? x + '0' : x - 0xA + 'A'));
	    x = c & 0xF;
	    buf.append((char) (x < 0xA ? x + '0' : x - 0xA + 'A'));
	} else {
	    buf.append(c);
	}
    }

    /**
     * Quotes string as Java Language string literal. 
     * Returns the string "<code>null</code>" (with length 4) 
     * if <code>str</code> is <code>null</code>.
     * 
     * Code taken from http://freemarker.sourceforge.net/
     * 
     * @param str
     * @return the string encoded and quoted
     */
    public static String jQuote(final String str) {
        if (str == null) {
            return "null";
        }
        final int len = str.length();
        final StringBuffer buf = new StringBuffer(len + 4);
        buf.append('"');
        for (int i = 0; i < len; i++) {
	    appendChar(buf, str.charAt(i));
        } // for each characters
        buf.append('"');
        return buf.toString();
    }

    /**
     * Quotes the first <code>len</code> characters of <code>cbuf</code> 
     * as Java Language string literal. 
     * Returns string <code>null</code> if <code>s</code> is <code>null</code>. 
     * 
     * @param cbuf
     *    the buffer
     * @param len
     *    How much of the buffer to quote
     * @return the string encoded and quoted: 
     *    Starts and ends with <code>"</code>
     */
    public static String jQuote(final char[] cbuf, final int len) {
        final StringBuffer buf = new StringBuffer(len + 4);
        buf.append('"');
        for (int i = 0; i < len; i++) {
	    appendChar(buf, cbuf[i]);
        } // for each characters
        buf.append('"');
        return buf.toString();
    }

}
