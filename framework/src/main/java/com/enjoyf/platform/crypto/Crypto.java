//  Copyright (C) 2009 Fivewh platform mochi.com
package com.enjoyf.platform.crypto;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.enjoyf.platform.util.HexUtil;

public class Crypto {
    protected Key key;
    protected String algorithm;
    protected String feedbackMode;
    protected String paddingScheme;
    protected String transformation;
    protected String encoding;
    protected byte[] iv;
    protected boolean xor;

    protected AlgorithmParameterSpec params;

    protected int blockSize = 8;
    protected byte paddingValue = ' ';

    protected Cipher encrypter;
    protected Cipher decrypter;

    /**
     * Old configuration as of Oct-1-2004
     */
    public static class V1 extends Crypto {
        public V1() {
            super("DES", "ECB", "PKCS5Padding", null, null, false);
        }
    }

    /**
     * Oracle DB compatible configuration
     */
    public static class V2 extends Crypto {
        public V2() {
            super("DES", "CBC", "NoPadding", "UTF-8", "0123456789ABCDEF", true);
        }
    }

    public Crypto(String algorithm, String feedbackMode, String paddingScheme, String encoding, String iv, boolean xor) {
        this.algorithm = algorithm;
        this.feedbackMode = feedbackMode;
        this.paddingScheme = paddingScheme;
        this.transformation = algorithm + '/' + feedbackMode + '/' + paddingScheme;
        this.encoding = encoding;

        if (iv != null) {
            this.xor = xor;
            this.iv = HexUtil.fromHex(iv);
            params = new IvParameterSpec(this.iv);
        } else {
            this.xor = false;
        }
    }

    public void init(byte[] key) throws GeneralSecurityException {
        this.key = new SecretKeySpec(key, algorithm);

        encrypter = Cipher.getInstance(transformation);
        decrypter = Cipher.getInstance(transformation);
        if (params != null) {
            encrypter.init(Cipher.ENCRYPT_MODE, this.key, params);
            decrypter.init(Cipher.DECRYPT_MODE, this.key, params);
        } else {
            encrypter.init(Cipher.ENCRYPT_MODE, this.key);
            decrypter.init(Cipher.DECRYPT_MODE, this.key);
        }
    }

    public byte[] encrypt(byte[] value) {
        int padding = blockSize - (value.length % blockSize);
        byte[] result = value;
        result = new byte[value.length + padding];
        System.arraycopy(value, 0, result, 0, value.length);
        for (int i = value.length; i < result.length; i++) {
            result[i] = paddingValue;
        }
        if (xor) {
            for (int i = 0; i < 8; i++) {
                result[i] ^= iv[i];
            }
        }
        try {
            result = encrypter.doFinal(result);
        }
        catch (GeneralSecurityException e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    public String encrypt(String value) {
        byte[] result = null;
        if (encoding != null) {
            try {
                result = value.getBytes(encoding);
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            result = value.getBytes();
        }
        result = encrypt(result);
        return HexUtil.toHex(result);
    }

    public byte[] decrypt(byte[] value) {
        byte[] result = value;
        try {
            result = decrypter.doFinal(result);
        }
        catch (GeneralSecurityException e) {
            throw new RuntimeException(e.getMessage() + " : " + HexUtil.toHex(value));
        }
        if (xor) {
            for (int i = 0; i < 8; i++) {
                result[i] ^= iv[i];
            }
        }
        for (int i = result.length - 1; i >= 0; i--) {
            if (result[i] != paddingValue) {
                if (i != result.length - 1) {
                    byte[] temp = new byte[i + 1];
                    System.arraycopy(result, 0, temp, 0, i + 1);
                    result = temp;
                }
                break;
            }
        }
        return result;
    }

    public String decrypt(String value) {
        byte[] bytes = HexUtil.fromHex(value);
        bytes = decrypt(bytes);
        if (encoding != null) {
            try {
                value = new String(bytes, encoding);
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            value = new String(bytes);
        }
        return value;
    }
}
