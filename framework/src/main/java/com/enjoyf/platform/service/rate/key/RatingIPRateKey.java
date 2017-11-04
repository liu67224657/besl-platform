package com.enjoyf.platform.service.rate.key;

import com.enjoyf.platform.service.rate.RateKeyDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class RatingIPRateKey extends StringRateKey {
	
	public RatingIPRateKey(RateKeyDomain domain, String ip) {
		super(domain, ip);
	}

	@Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}
	
}
