package com.enjoyf.platform.crypto.cert;

/**
 * This class handles the reading of private keys.
 * It can read both PEM and DER style keys.
 *
 */


import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;


public class PrivateKeyFactory {


    /**
     * Reads a DER private key.
     *
     * @param name
     * @param provider
     * @return
     * @throws Exception
     */
    private static Key p_getDerKey(String name, String provider)
            throws Exception {
        InputStream in = name.getClass().getResourceAsStream(name);
        int size = in.available();
        byte[] encodedKey = new byte[size];
        in.read(encodedKey);

        PKCS8EncodedKeySpec kspec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory kfac = KeyFactory.getInstance("RSA", provider);
        return kfac.generatePrivate(kspec);
    }


    /**
     * Reads and returns a private key. The name is specified
     * as per the classpath the file is in. The file can be
     * a PEM or DER format private key. This method uses
     * the extension to determine how to read the file. the default
     * format read is DER (if the extension does not provide sufficient details).
     *
     * @param name
     * @return
     */
    public static Key getKey(String name, String provider)
            throws Exception {
        if (name.toLowerCase().endsWith((".pem"))) {
            return p_getDerKey(name, provider);
        } else {
            return p_getDerKey(name, provider);
        }
    }
}
