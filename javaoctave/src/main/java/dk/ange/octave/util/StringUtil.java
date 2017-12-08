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

/**
 * Class for holding static utility functions for string handling: quoting. 
 * 
 * @author Kim Hansen
 */
public final class StringUtil {

    private StringUtil() {
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
    @SuppressWarnings("checkstyle:magicnumber")
    public static String jQuote(final String str) {
        if (str == null) {
            return "null";
        }
        final int ln = str.length();
        final StringBuffer b = new StringBuffer(ln + 4);
        b.append('"');
        for (int i = 0; i < ln; i++) {
            final char c = str.charAt(i);
            if (c == '"') {
                b.append("\\\"");
            } else if (c == '\\') {
                b.append("\\\\");
            } else if (c < 0x20) {
                if (c == '\n') {
                    b.append("\\n");
                } else if (c == '\r') {
                    b.append("\\r");
                } else if (c == '\f') {
                    b.append("\\f");
                } else if (c == '\b') {
                    b.append("\\b");
                } else if (c == '\t') {
                    b.append("\\t");
                } else {
                    b.append("\\u00");
                    int x = c / 0x10;
                    b.append((char) (x < 0xA ? x + '0' : x - 0xA + 'A'));
                    x = c & 0xF;
                    b.append((char) (x < 0xA ? x + '0' : x - 0xA + 'A'));
                }
            } else {
                b.append(c);
            }
        } // for each characters
        b.append('"');
        return b.toString();
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
    @SuppressWarnings("checkstyle:magicnumber")
    // **** this is a copy of the above code 
    public static String jQuote(final char[] cbuf, final int len) {
        final StringBuffer b = new StringBuffer(len + 4);
        b.append('"');
        for (int i = 0; i < len; i++) {
            final char c = cbuf[i];
            if (c == '"') {
                b.append("\\\"");
            } else if (c == '\\') {
                b.append("\\\\");
            } else if (c < 0x20) {
                if (c == '\n') {
                    b.append("\\n");
                } else if (c == '\r') {
                    b.append("\\r");
                } else if (c == '\f') {
                    b.append("\\f");
                } else if (c == '\b') {
                    b.append("\\b");
                } else if (c == '\t') {
                    b.append("\\t");
                } else {
                    b.append("\\u00");
                    int x = c / 0x10;
                    b.append((char) (x < 0xA ? x + '0' : x - 0xA + 'A'));
                    x = c & 0xF;
                    b.append((char) (x < 0xA ? x + '0' : x - 0xA + 'A'));
                }
            } else {
                b.append(c);
            }
        } // for each characters
        b.append('"');
        return b.toString();
    }

}
