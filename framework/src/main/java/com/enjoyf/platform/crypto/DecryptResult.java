package com.enjoyf.platform.crypto;

public class DecryptResult {
    /**
     * Indicates a "normal" decryption.
     */
    public static final int NORMAL = 0;
    /**
     * Indicates that we needed to use "old" decryption.
     */
    public static final int OLD = 1;

    byte[] barray;
    int type;

    public DecryptResult(byte[] barray) {
        this(barray, NORMAL);
    }

    public DecryptResult(byte[] barray, int type) {
        this.barray = barray;
        this.type = type;
    }

    public byte[] getArray() {
        return barray;
    }

    public boolean isOld() {
        return type == OLD;
    }
}
