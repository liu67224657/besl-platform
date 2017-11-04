package com.enjoyf.platform.crypto;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.enjoyf.platform.util.HexUtil;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * This class is not thread safe, you can use it multiple times in a single thread only, or cache it in a thread local.
 * @author wuqiang
 */
public class Digester {

	private MessageDigest mdInstance = null;

    public Digester() {
    	this("SHA");
    }

    public Digester(String algorithm) {
    	try {
    		mdInstance = MessageDigest.getInstance(algorithm);
    	} catch (NoSuchAlgorithmException e) {
    		GAlerter.lab("cannot create digester ", e);
    	}
    }

    public String encode(final String origin) {
        return HexUtil.toHex(mdInstance.digest(origin.getBytes()));
    }

}
