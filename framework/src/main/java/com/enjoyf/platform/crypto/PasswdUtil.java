package com.enjoyf.platform.crypto;

import com.enjoyf.platform.util.HexUtil;
import com.enjoyf.platform.util.log.GAlerter;

// This class decrypts the password

public class PasswdUtil {
    public static String encrypt(String passwd, byte[] key) {
        String cipherOut = null;
        CryptoMgr cryptoMgr;

        try {
            cryptoMgr = CryptoMgr.getInstance(CryptoAlg.DES, key);
            byte[] cipherText = cryptoMgr.encrypt(passwd.getBytes());
            cipherOut = HexUtil.toHex(cipherText);
        } catch (CryptException ce) {
            GAlerter.lab("Passwd Util: Exception while encryption password" + ce);
        }

        return cipherOut;
    }

    public static String encrypt(String passwd, String key) {
        return encrypt(passwd, key.getBytes());
    }

    public static String decrypt(String cipherInput, byte[] key) {
        String decryptOut = null;
        CryptoMgr cryptoMgr;

        try {
            cryptoMgr = CryptoMgr.getInstance(CryptoAlg.DES, key);
            decryptOut = new String(cryptoMgr.decrypt(HexUtil.fromHex(cipherInput)));
        } catch (CryptException ce) {
            GAlerter.lab("Password Util: Exception while decrypting password" + ce);
        }

        return decryptOut;
    }

    public static String decrypt(String cipherInput, String key) {
        return decrypt(cipherInput, key.getBytes());
    }


    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("The parameters are missed.");
            System.exit(1);
        }

        String orgPwd = args[0];
        String pwdKey = args[1];

        if (pwdKey.length() != 8) {
            System.out.println("The original deskey must be 8.");
            //System.exit(1);
        }

        String encyptedPwd = PasswdUtil.encrypt(orgPwd, pwdKey);

        System.out.println("The original password:" + orgPwd);
        System.out.println("The original deskey:" + pwdKey);
        System.out.println("The encrypted password:" + encyptedPwd);

        System.out.println("The decrypted password:" + PasswdUtil.decrypt(encyptedPwd, pwdKey));
    }
}
