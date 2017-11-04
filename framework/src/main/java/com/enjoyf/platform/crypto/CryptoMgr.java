package com.enjoyf.platform.crypto;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.crypto.provider.SunJCE;

/**
 * An abstraction of the "manager" class that handles all the
 * encryption/decryption.  This class is conformed to the existing
 * Sun's JCE architecture. The derived classes need to specify the algorithm,
 * and handle how to initialize itself with the given key.  Please see DESCryptoMgr
 * as an example of derived class.
 * <p/>
 * Usage:
 * <p/>
 * CryptoMgr mgr = CryptoMgr.getInstance(CryptoAlg.DES, 1234L);
 * byte[] cipherText = mgr.encrypt("This is a test".getBytes());
 * String decryptedText =  new String (mgr.decrypt(cipherText));
 */
public class CryptoMgr {
	
	private static final Logger logger = LoggerFactory.getLogger(CryptoMgr.class);
	
    public static final String DES_ALG = "DES";
    public static final String DES_EDE_ALG = "DESede";

    // algorithm which CryptoMgr instance is using.
    protected String alg;
    protected Cipher encryptor;
    protected Cipher decryptor;

    // key for
    protected SecretKey secretKey;

    static {
        Security.addProvider(new SunJCE());
    }

    protected CryptoMgr() {
        // disable constructor
    }

    /**
     * initialize the CryptoMgr with key
     *
     * @param key for encryption/decryption
     */
    public void init(byte[] key) {
        String alg = getAlgorithm();

        try {
            secretKey = new SecretKeySpec(key, alg);
            decryptor = Cipher.getInstance(alg);
            decryptor.init(Cipher.DECRYPT_MODE, secretKey);
            encryptor = Cipher.getInstance(alg);
            encryptor.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (Exception e) {
            logger.error("CryptoMgr.init error.", e);
            decryptor = null;
            encryptor = null;
        }
    }

    /**
     * initialize the CryptoMgr with key
     *
     * @param key for encryption/decryption
     */
    public void init(long key) {
        throw new UnsupportedOperationException(getAlgorithm() + " algorithm does not take long integer as a key");
    }


    /**
     * initialize the CryptoMgr with key
     *
     * @param key for encryption/decryption
     */
    public void init(Key key) {
        throw new UnsupportedOperationException(
                getAlgorithm() + " algorithm does not take Key class as a key");
    }

    /**
     * @param algorithm for encryption/decryption
     * @param key       for encryption/decryption
     * @return an instance of CryptoMgr initialized with the given algorithm and key
     */
    public static CryptoMgr getInstance(CryptoAlg algorithm, Key key) {
        CryptoMgr mgr = getInstance(algorithm);
        mgr.init(key);
        return mgr;
    }

    /**
     * @param algorithm for encryption/decryption
     * @param key       for encryption/decryption
     * @return an instance of CryptoMgr initialized with the given algorithm and key
     */
    public static CryptoMgr getInstance(CryptoAlg algorithm, long key) {
        CryptoMgr mgr = getInstance(algorithm);
        mgr.init(key);

        return mgr;
    }

    /**
     * return an instance of CryptoMgr initialized with the given algorithm and key
     *
     * @param algorithm for encryption/decryption
     * @param key       for encryption/decryption
     */
    public static CryptoMgr getInstance(CryptoAlg algorithm, byte[] key) {
        CryptoMgr mgr = getInstance(algorithm);
        mgr.init(key);

        return mgr;
    }

    /**
     * @param algorithm for encryption/decryption
     * @return an instance of CryptoMgr for the given algorithm
     */
    private static CryptoMgr getInstance(CryptoAlg algorithm) {
// 		CryptoMgr mgr =
// 			(CryptoMgr) Class.forName(CryptoMgr.class.getBoardName() + algorithm)
// 			.newInstance();

        if (algorithm == null) {
            throw new IllegalArgumentException("CryptoMgr.getInstance: missing algorithm");
        }

        if (algorithm.equals(CryptoAlg.DES)) {
            return new DESCryptoMgr();
        } else if (algorithm.equals(CryptoAlg.DES_EDE)) {
            return new DESedeCryptoMgr();
        }

        throw new IllegalArgumentException("CryptoMgr.getInstance: unsupported algorithm");
    }

    /**
     * @return the algorithm for encryption/decryption
     */
    public String getAlgorithm() {
        return alg;
    }

    /**
     * encrypt the clearText
     */
    public byte[] encrypt(byte[] clearText) throws CryptException {
        if (encryptor == null) {
            throw new CryptException(
                    "Did not instantiate the encryptor object correctly", null);
        }

        byte[] cipherText = null;

        try {
            cipherText = encryptor.doFinal(clearText);
        }
        catch (Exception e) {
            throw new CryptException("CrytoMgr.encrypt", e);
        }
        return cipherText;
    }

// 	public SealedObject encrypt(Object clearText) throws CryptException;

    /**
     * decrypt the cipherText
     */
    public byte[] decrypt(byte[] cipherText) throws CryptException {
        if (decryptor == null) {
            throw new CryptException(
                    "CryptoMgr: Did not instantiate the encryptor object correctly");
        }

        byte[] plain = null;
        try {
            plain = decryptor.doFinal(cipherText);
        }
        catch (Exception e) {
            throw new CryptException("SymEncryptJce.decrypt", e);
        }

        return plain;
    }
}

