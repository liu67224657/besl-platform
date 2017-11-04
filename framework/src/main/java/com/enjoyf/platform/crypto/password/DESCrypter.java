package com.enjoyf.platform.crypto.password;

import java.text.DecimalFormat;

import com.enjoyf.platform.crypto.PasswdUtil;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class DESCrypter implements PasswordCrypter {

    public DESCrypter() {
    }

    public String generateKey(String seed) {
        return null;
    }

    public String encrypt(String orig, String key) {
        return PasswdUtil.encrypt(orig, new DecimalFormat("00000000").format(key));
    }

    public String decrypt(String encrypt, String key) {
        return PasswdUtil.decrypt(encrypt, new DecimalFormat("00000000").format(key));
    }
}
