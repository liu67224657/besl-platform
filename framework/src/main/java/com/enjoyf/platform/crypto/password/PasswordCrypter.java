package com.enjoyf.platform.crypto.password;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public interface PasswordCrypter {
    /**
     * generateKey
     */
    public String generateKey(String seed);

    /**
     * encrypt
     */
    public String encrypt(String orig, String key);

    /**
     * decrypt
     */
    public String decrypt(String encrypt, String key);
}
