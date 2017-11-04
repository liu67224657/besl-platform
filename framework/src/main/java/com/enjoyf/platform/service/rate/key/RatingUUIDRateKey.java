package com.enjoyf.platform.service.rate.key;

import java.util.UUID;

import com.enjoyf.platform.service.rate.RateKeyDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @author Daniel
 */
@SuppressWarnings("serial")
public class RatingUUIDRateKey extends StringRateKey {
	
	public RatingUUIDRateKey(RateKeyDomain domain, UUID userId) {
		super(domain, userId.toString());
	}

	@Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}
	
}
