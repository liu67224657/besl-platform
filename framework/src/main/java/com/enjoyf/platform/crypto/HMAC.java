package com.enjoyf.platform.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.enjoyf.platform.util.HexUtil;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @author Daniel
 */
public class HMAC {
	
	public static final String HMAC_MD5 = "HmacMD5";
	public static final String HMAC_SHA1 = "HmacSHA1";
	
	private Mac mac;

	public HMAC(String secretKey) {
		this(secretKey, HMAC_SHA1);
	}
	
	public HMAC(String secretKey, String algorithm) {
		if (!HMAC_SHA1.equals(algorithm) && !HMAC_MD5.equals(algorithm)) {
			throw new IllegalArgumentException("Not supported algorithm:" + algorithm);
		}
		
		SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(), algorithm);
		try {
			this.mac = Mac.getInstance(algorithm);
			this.mac.init(secret);
		} catch (NoSuchAlgorithmException e) {
			GAlerter.lab("Extremely severe problem, cannot create HMAC");
		} catch (InvalidKeyException e) {
			GAlerter.lab("Extremely severe problem, cannot create HMAC");
		}
	}
	
	public String toHex(String input) {
		byte[] result = this.mac.doFinal(input.getBytes());
		return HexUtil.toHex(result);
	}

}
