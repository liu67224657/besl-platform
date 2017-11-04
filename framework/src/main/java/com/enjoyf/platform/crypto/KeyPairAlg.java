package com.enjoyf.platform.crypto;

/**
 * a list of currently supported algorithms
 */
public enum KeyPairAlg {
    DSA("DSA"),
    RSA("RSA"),
    DH("DH");

    private String code;

    private KeyPairAlg(String code) {
        this.code = code;
    }

    public static KeyPairAlg getByCode(String code) {
        KeyPairAlg alg = null;

        if (code != null) {
            try {
                alg = KeyPairAlg.valueOf(code);
            } catch (Exception e) {
                //
            }
        }

        return alg;
    }

    public String toString() {
        return code;
    }

    public String getCode() {
        return code;
    }
}
