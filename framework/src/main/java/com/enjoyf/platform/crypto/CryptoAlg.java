package com.enjoyf.platform.crypto;

/**
 * This is a java-enum class as defined in "Effective Java" Item 21.
 * Only static instances should exist.
 * <p/>
 * As we support new algorithms, we add more static final instances to this class.
 */

public class CryptoAlg {
    public static final CryptoAlg DES = new CryptoAlg("DES");

    public static final CryptoAlg DES_EDE = new CryptoAlg("DESede");

    private String alg;        // algorithm name

    private CryptoAlg(String alg) {
        this.alg = alg;
    }

    public String getName() {
        return alg;
    }
}
