package com.enjoyf.platform.webapps.common.security;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class DES {

    private final static String KEY = "ENJOYFOU";

    public static byte[] desEncrypt(byte[] plainText) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(KEY.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        byte data[] = plainText;
        byte encryptedData[] = cipher.doFinal(data);
        return encryptedData;
    }

    public static byte[] desDecrypt(byte[] encryptText) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(KEY.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        byte encryptedData[] = encryptText;
        byte decryptedData[] = cipher.doFinal(encryptedData);
        return decryptedData;
    }

    public static String encrypt(String input) throws Exception {
        return base64Encode(desEncrypt(input.getBytes()));
    }

    public static String urlEncrypt(String input) throws Exception {
        return
                java.net.URLEncoder.encode(base64Encode(desEncrypt(input.getBytes())), "UTF-8");
    }


    public static String urlDecrypt(String input) throws Exception {
        return new String(desDecrypt(base64Decode(java.net.URLDecoder.decode(input, "UTF-8"))));
    }

    public static String decrypt(String input) throws Exception {
        byte[] result = base64Decode(input);
        return new String(desDecrypt(result));
    }

    public static String base64Encode(byte[] s) {
        if (s == null)
            return null;
        Base64 b = new Base64();
        return new String(b.encode(s)).trim();
    }

    public static byte[] base64Decode(String s) throws IOException {
        if (s == null) {
            return null;
        }
        Base64 decoder = new Base64();
        byte[] b = decoder.decode(s);
        return b;
    }


    public static String md5(String s) {

        StringBuffer sf = new StringBuffer();
        byte[] b = null;
        try {
            b = java.security.MessageDigest.getInstance("MD5").digest(
                    s.getBytes("UTF8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < b.length; i++) {
            sf.append(byte2Hex(b[i]));
        }
        return sf.toString();
    }

    public static String byte2Hex(byte b) {
        return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF"
                .charAt(b & 0xf));
    }


    public static void main(String args[]) {
        try {
            // System.out.println("===>"+
            // DESCoder.decrypt("nYsZXLwijRy+FweKG8oOxoXkvm62ifdWW"));
            String a = "{\"synLoginStr\":\"<iframe border='0' src='http://192.168.20.32:8080/mp2/sso/setcookie.jsp?key=ZoKHRCQBYjICajzbEYPxlpvOo9fem92FJkJhN8zb48%252FRcBTqEq1nMWYFgL7SF4BzfsBu53F13MI%253D' style='display:none'><\\/iframe><iframe border='0' src='http://192.168.20.31:8888/ktvMusic/sso/setcookie.jsp?key=ZoKHRCQBYjICajzbEYPxlpvOo9fem92FJkJhN8zb48%252FRcBTqEq1nMWYFgL7SF4BzfsBu53F13MI%253D' style='display:none'><\\/iframe><iframe border='0' src='http://web.uonenet.com:8080/sso/setcookie.jsp?key=ZoKHRCQBYjICajzbEYPxlpvOo9fem92FJkJhN8zb48%252FRcBTqEq1nMWYFgL7SF4BzfsBu53F13MI%253D' style='display:none'><\\/iframe>\",\"key\":\"ZoKHRCQBYjICajzbEYPxlpvOo9fem92FJkJhN8zb48%2FRcBTqEq1nMWYFgL7SF4BzfsBu53F13MI%3D\"}";
            String b = DES.encrypt(a);
            System.out.println(b);
            System.out.println(DES.decrypt(b));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
