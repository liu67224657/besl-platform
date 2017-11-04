package com.enjoyf.platform.crypto;

public class DESCryptoMgr extends CryptoMgr {
    public DESCryptoMgr() {
        alg = DES_ALG;
    }


    // it used to be a int type, so only 4 bytes
    // are used for backward compatibility
    public void init(long key) {
        byte[] b = new byte[8];
        b[0] = (byte) (key & 0xff);
        b[1] = (byte) (key >> 8 & 0xff);
        b[2] = (byte) (key >> 16 & 0xff);
        b[3] = (byte) (key >> 24 & 0xff);
        b[4] = (byte) (b[0] + b[1]);
        b[5] = (byte) (b[0] + b[1] + b[2]);
        b[6] = (byte) (b[0] - b[1]);
        b[7] = (byte) (b[0] - b[4]);

        init(b);
    }

}
