package com.enjoyf.platform.util;

/**
 * Utility class for converting byte arrays to hex strings and vice-versa.
 */
public class HexUtil {
    private static final char[] HEXCHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    // ascii map for hex value conversion
    private static final byte[] HEXVALUES = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,         //   0 -  19
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,         //  20 -  39
            0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0,         //  40 -  59
            0, 0, 0, 0, 0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0,     //  60 -  79
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 11, 12,         //  80 -  99
            13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,         // 100 - 119
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,         // 120 - 139
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,         // 140 - 159
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,         // 160 - 179
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,         // 180 - 199
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,         // 200 - 219
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,         // 220 - 239
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};                    // 240 - 255

    /**
     * Convert a byte array to a hex string.
     *
     * @param value byte aray
     * @return hex string
     */
    public static String toHex(byte[] value) {
        int len = value.length;
        StringBuffer buffer = new StringBuffer(len * 2);
        for (int i = 0; i < len; i++) {
            buffer.append(HEXCHARS[0xF & (value[i] >> 4)]);
            buffer.append(HEXCHARS[0xF & (value[i] >> 0)]);
        }
        return buffer.toString();
    }

    /**
     * Convert a hex string into a byte array.
     *
     * @param value hex string
     * @return byte array
     */
    public static byte[] fromHex(String value) {
        try {
            int len = value.length();
            char[] chars = new char[len];
            value.getChars(0, len, chars, 0);
            byte[] buffer = new byte[len / 2];
            len = buffer.length;
            int h, l;
            for (int i = 0; i < len; i++) {
                h = chars[i * 2];
                l = chars[i * 2 + 1];
                buffer[i] = (byte) ((HEXVALUES[h] << 4) | HEXVALUES[l]);
            }
            return buffer;
        }
        catch (Exception e) {
            throw new IllegalArgumentException("unable to convert input: " + value);
        }
    }

    /**
     * Convert an unsigned byte to an integer.
     */
    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }
}
