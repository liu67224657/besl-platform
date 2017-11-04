package com.enjoyf.platform.crypto;

/**
 * The concrete class that handle encryption/decryption
 * using "DESede" algorithm.
 */
public class DESedeCryptoMgr extends CryptoMgr {
    public DESedeCryptoMgr() {
        alg = DES_EDE_ALG;
    }

    /**
     * support long integer as key
     */
    public void init(long key) {
        byte[] b = new byte[24];
        b[0] = (byte) (key & 0xff);
        b[1] = (byte) (key >> 8 & 0xff);
        b[2] = (byte) (key >> 16 & 0xff);
        b[3] = (byte) (key >> 24 & 0xff);
        b[4] = (byte) (key >> 32 & 0xff);
        b[5] = (byte) (key >> 40 & 0xff);
        b[6] = (byte) (key >> 48 & 0xff);
        b[7] = (byte) (key >> 52 & 0xff);

        for (int i = 8; i < b.length; i++) {
            b[i] = (byte) (key >> ((i * 4) % 64) ^ b[i - 8]);
        }

        init(b);
    }

}
