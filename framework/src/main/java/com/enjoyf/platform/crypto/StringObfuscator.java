//

// (C) 2009 Fivewh platform platform.com

//


package com.enjoyf.platform.crypto;


import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;


/**
 * String Obfuscator
 * <p/>
 * <p/>
 * <p/>
 * This is a simple class to obfuscate strings. It is only tested to work with the US-ASCII
 * <p/>
 * character set, and only with strings that do not explicitly contain the binary 0 character.
 * <p/>
 * THESE FUNCTIONS WILL NOT WORK IF THE STRING CONTAINS CHARACTER CODES >127 OR <=0 !! <p>
 * <p/>
 * <p/>
 * <p/>
 * The obfuscated output is (much) longer than the input and is encoded to be URL-safe.
 * <p/>
 * It is recommended that this class be used only for short strings.<p>
 * <p/>
 * <p/>
 * <p/>
 * This obfuscator intentionally encodes some redundant information into the output string
 * <p/>
 * in order that the decode() function will likely fail if the encoded string is corrupt or
 * <p/>
 * has been tampered with.<p>
 * <p/>
 * <p/>
 * <p/>
 * *** WARNING! ***
 * <p/>
 * The obfuscation method described here is used to communicate with stand-along game clients
 * <p/>
 * (platform downloadable games). DO NOT CHANGE THE ENCODE/DECODE LOGIC UNLESS IT IS KNOWN THAT ALL
 * <p/>
 * EXISTING GAME CLIENTS CAN BE UPDATED TO ACCOMODATE THE CHANGE. Otherwise, you will break
 * <p/>
 * functionality in games that communicate with the server. If in doubt, please copy to a new
 * <p/>
 * class or refactor in a way that preserves the existing encoding method.<p>
 * <p/>
 * <p/>
 * <p/>
 * For more information, contact Gil Alvarez, Jeff Price or Frank LaRosa.
 */

public class StringObfuscator {


    private static final char HEX_PREFIX = '.';


    Random rnd = new Random();


    /**
     * String Obfuscator
     *
     * @param source Any string that doesn't contain character codes <=0 or >127.
     * @return An obfuscated version. The result is guaranteed to be longer than the input,
     *         <p/>
     *         and is also guaranteed to be URL-safe, containing only letters, digits, and the '.' character.
     * @throws UnsupportedEncodingException Thrown if the string can't be decoded to UTF-8, or
     *                                      <p/>
     *                                      if the decoded version contains any bytes outside the range 1 to 127.
     */

    public String encode(String source) throws UnsupportedEncodingException

    {

        if ((source == null) || (source.length() == 0)) {
            return source;
        }


        int origLength = source.length();

        while ((source.length() % 3) != 0) {

            source += '\0';

        }


        byte[] srcBytes = source.getBytes("UTF-8");

        byte[] dstBytes = new byte[srcBytes.length + (srcBytes.length / 3)];

        int i = 0;

        int x = 0;

        while (i < srcBytes.length) {

            if ((srcBytes[i] < 0) || ((srcBytes[i] == 0) && (i < origLength))) {

                throw new UnsupportedEncodingException("Character code is out of range. Legal range is 1 to 127.");

            }

            dstBytes[x++] = srcBytes[i++];

            if ((i % 3) == 0) {

                byte b = (byte) ((dstBytes[x - 3] & 48) + (dstBytes[x - 2] & 12) + (dstBytes[x - 1] & 3));

                dstBytes[x - 3] = (byte) ((dstBytes[x - 3] & 207) + ((dstBytes[x - 2] & 12) << 2));

                dstBytes[x - 2] = (byte) ((dstBytes[x - 2] & 243) + ((dstBytes[x - 1] & 3) << 2));

                dstBytes[x - 1] = (byte) ((dstBytes[x - 1] & 252) + rnd.nextInt(4));

                dstBytes[x++] = b;

            }

        }


        for (i = 0; i < dstBytes.length; i++) {

            dstBytes[i] = (byte)

                    (((dstBytes[i] >>> 2) & 12) + ((dstBytes[i] << 2) & 48) + (dstBytes[i] & 195));

        }


        StringBuffer result = new StringBuffer();

        for (i = 0; i < dstBytes.length; i++) {

            byte b = dstBytes[i];

            if (((b >= 48) && (b <= 57)) || ((b >= 65) && (b <= 90)) || ((b >= 97) && (b <= 122))) {

                result.append(new String(dstBytes, i, 1, "UTF-8"));

            } else {

                result.append(HEX_PREFIX);

                String hex = Integer.toHexString(b);

                if (hex.length() < 2) {
                    result.append("0");
                }

                result.append(hex);

            }

        }


        return result.toString();

    }


    /**
     * String de-Obfuscator
     *
     * @param source A string resulting from a call to the encode() method.
     * @return The original deobfuscated string.
     * @throws IllegalArgumentException Thrown if the input is not in the proper obfuscation format.
     *                                  <p/>
     *                                  This exception will likely be thrown if the input is any string that did not come from encode().
     */

    public String decode(String source) throws UnsupportedEncodingException, IllegalArgumentException

    {

        if ((source == null) || (source.length() == 0)) {
            return source;
        }


        ByteArrayOutputStream bytes = new ByteArrayOutputStream(source.length());

        int i = 0;

        try {

            while (i < source.length()) {

                if (source.charAt(i) == HEX_PREFIX) {

                    byte b = (byte) Integer.parseInt(source.substring(i + 1, i + 3), 16);

                    bytes.write(b);

                    i += 3;

                } else {

                    byte[] b = source.substring(i, i + 1).getBytes("UTF-8");

                    bytes.write(b[0]);

                    i++;

                }

            }

        }

        catch (StringIndexOutOfBoundsException e) {

            throw new IllegalArgumentException();

        }

        catch (NumberFormatException e) {

            throw new IllegalArgumentException();

        }


        byte[] srcBytes = bytes.toByteArray();

        if ((srcBytes.length % 4) != 0) {

            throw new IllegalArgumentException();

        }


        for (i = 0; i < srcBytes.length; i++) {

            srcBytes[i] = (byte)

                    (((srcBytes[i] << 2) & 48) + ((srcBytes[i] >>> 2) & 12) + (srcBytes[i] & 195));

        }


        byte[] dstBytes = new byte[(srcBytes.length / 4) * 3];

        i = 0;

        int x = 0;

        while (i < srcBytes.length) {

            byte b = srcBytes[i + 3];

            if (((srcBytes[i] >>> 4) & 3) != ((b >>> 2) & 3)) {

                throw new IllegalArgumentException();

            }

            if (((srcBytes[i + 1] >>> 2) & 3) != (b & 3)) {

                throw new IllegalArgumentException();

            }

            dstBytes[x] = (byte) ((srcBytes[i] & 207) + (b & 48));

            dstBytes[x + 1] = (byte) ((srcBytes[i + 1] & 243) + (b & 12));

            dstBytes[x + 2] = (byte) ((srcBytes[i + 2] & 252) + (b & 3));

            x += 3;

            i += 4;

        }


        x = dstBytes.length - 1;

        while ((x > 0) && (dstBytes[x] == 0)) {

            x--;

        }


        return new String(dstBytes, 0, x + 1, "UTF-8");

    }

    // You can run this from the command line to obfuscate a single string

    public static void main(String[] args) {

        StringObfuscator obfs = new StringObfuscator();


        try {

            String source = args[0];

            System.out.println(source + " (" + source.length() + ")");


            String encoded = obfs.encode(args[0]);

            System.out.println(encoded + " (" + encoded.length() + ")");


            String decoded = obfs.decode(encoded);

            System.out.println(decoded + " (" + decoded.length() + ")");


            if (args.length > 1) {

                System.out.println(obfs.decode(args[1]));

            }

        }


        catch (Exception e) {

            e.printStackTrace();

        }


    }


}

