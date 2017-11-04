package com.enjoyf.platform.crypto;

/**
 * Base64 encoder for Right Now Web integration
 * <p/>
 * Use this encoder to communicate with RNW.
 */
public class RNWBase64Encoder extends Base64Encoder {

    private static final char RNW_FILLCHAR = '*';

    private static final String RNW_CHARMAP =
            // 00000000001111111111222222
            // 01234567890123456789012345
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

                    // 22223333333333444444444455
                    // 67890123456789012345678901
                    + "abcdefghijklmnopqrstuvwxyz"

                    // 555555556666
                    // 234567890123
                    + "0123456789_-";

    public RNWBase64Encoder() {
        super(RNW_FILLCHAR, RNW_CHARMAP);
    }


    public static void main(String[] args) {
        System.out.println(new RNWBase64Encoder().decode(args[0]));
    }

}
