package com.enjoyf.platform.crypto;

public abstract class SymEncrypt {
    public abstract byte[] encrypt(byte[] clearText)
            throws CryptException;

    public abstract DecryptResult decrypt(byte[] cipherText)
            throws CryptException;
}
