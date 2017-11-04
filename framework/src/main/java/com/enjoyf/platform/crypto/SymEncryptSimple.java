package com.enjoyf.platform.crypto;

/**
 * A class that performs cheezy encryption, primarily for testing things.
 */
public class SymEncryptSimple extends SymEncrypt {
    /**
     * Encrypt the clear text.
     */
    public byte[] encrypt(byte[] clearText) {
        byte[] btemp = new byte[clearText.length];
        for (int i = 0; i < clearText.length; i++) {
            btemp[i] = (byte) (clearText[i] + 1);
        }

        return btemp;
    }

    /**
     * Decrypts cipher text.
     */
    public DecryptResult decrypt(byte[] cipherText) {
        byte[] btemp = new byte[cipherText.length];
        for (int i = 0; i < cipherText.length; i++) {
            btemp[i] = (byte) (cipherText[i] - 1);
        }

        return new DecryptResult(btemp);
    }
}
