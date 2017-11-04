package com.enjoyf.platform.crypto;

import java.io.UnsupportedEncodingException;


/**
 * Base64 Encoder.
 * This conforms to RFC2045 Part 6.8, when used with the default character map and fill character.
 */
public class Base64Encoder {
    private static final char DEFAULT_FILLCHAR = '=';

    private static final String DEFAULT_CHARMAP =
            // 00000000001111111111222222
            // 01234567890123456789012345
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

                    // 22223333333333444444444455
                    // 67890123456789012345678901
                    + "abcdefghijklmnopqrstuvwxyz"

                    // 555555556666
                    // 234567890123
                    + "0123456789+/";

    private final char mFillChar;
    private final String mCharMap;


    /**
     * Construct a standard encoder that conforms to RFC 2045
     */
    public Base64Encoder() {
        this(DEFAULT_FILLCHAR, DEFAULT_CHARMAP);
    }


    /**
     * Construct an encoder that uses a custom character set.
     */
    public Base64Encoder(char fillChar, String charMap) {
        if ((charMap == null) || (charMap.length() != 64)) {
            throw new IllegalArgumentException("Character map must be exactly 64 characters");
        }
        if (charMap.indexOf(fillChar) >= 0) {
            throw new IllegalArgumentException("Character map cannot contain the fill character");
        }
        mFillChar = fillChar;
        mCharMap = charMap;
    }


    private byte[] getBinaryBytes(String str) {
        try {
            return str.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            return str.getBytes();
        }

    }

    public String encode(String stringData) {
        return this.encode(this.getBinaryBytes(stringData));
    }

    public String encode(byte[] data) {
        int c;
        int len = data.length;
        StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
        for (int i = 0; i < len; ++i) {
            c = (data[i] >> 2) & 0x3f;
            ret.append(mCharMap.charAt(c));
            c = (data[i] << 4) & 0x3f;
            if (++i < len) {
                c |= (data[i] >> 4) & 0x0f;
            }

            ret.append(mCharMap.charAt(c));
            if (i < len) {
                c = (data[i] << 2) & 0x3f;
                if (++i < len) {
                    c |= (data[i] >> 6) & 0x03;
                }

                ret.append(mCharMap.charAt(c));
            } else {
                ++i;
                ret.append(mFillChar);
            }

            if (i < len) {
                c = data[i] & 0x3f;
                ret.append(mCharMap.charAt(c));
            } else {
                ret.append(mFillChar);
            }
        }

        return ret.toString();
    }

    public String decode(String stringData) {
        byte[] data = getBinaryBytes(stringData);
        int op;
        int op1;
        int len = data.length;
        StringBuffer ret = new StringBuffer((len * 3) / 4);

        for (int i = 0; i < len; ++i) {
            op = mCharMap.indexOf(data[i]);
            ++i;
            op1 = mCharMap.indexOf(data[i]);
            op = ((op << 2) | ((op1 >> 4) & 0x3));
            ret.append((char) op);
            if (++i < len) {
                op = data[i];
                if (mFillChar == op) {
                    break;
                }

                op = mCharMap.indexOf((char) op);
                op1 = ((op1 << 4) & 0xf0) | ((op >> 2) & 0xf);
                ret.append((char) op1);
            }

            if (++i < len) {
                op1 = data[i];
                if (mFillChar == op1) {
                    break;
                }

                op1 = mCharMap.indexOf((char) op1);
                op = ((op << 6) & 0xc0) | op1;
                ret.append((char) op);
            }
        }

        return ret.toString();
    }

}
