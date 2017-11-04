/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Yin Pengyi
 */

/**
 * Wrapper for Character Set used for encoding. Enumertes the supported character set
 * mapped to the Locale.
 * To add a new character set  refer http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html
 * to get the canonical names.
 */
public class CharSetEncoding implements Serializable {
    private String charsetString;
    private static final HashMap m_map = new HashMap();

    private static final String LATIN_1_ENC = "ISO-8859-1";
    private static final String LATIN_2_ENC = "ISO-8859-2";
    private static final String LATIN_7_ENC = "ISO-8859-7";
    private static final String BIG5_ENC = "big5";
    private static final String UTF8_ENC = "UTF-8";
    private static final String SJIS_ENC = "SJIS";
    private static final String KSC5601_ENC = "KSC5601";
    private static final String TIS620_ENC = "TIS620";
    private static final String GBK_ENC = "GBK";

    public String toString() {
        return charsetString;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o instanceof CharSetEncoding) {
            return o.toString().equals(this.toString());
        }
        return false;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

}