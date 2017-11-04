package com.enjoyf.platform.service.rate.key;

import com.enjoyf.platform.service.rate.RateKeyDomain;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class RegisterIpRateKey extends StringRateKey {

    public RegisterIpRateKey(String ip) {
        super(RateKeyDomain.REGISTER_IP, ip);
    }
    
    @Override
	public String toString() {
		return ReflectUtil.fieldsToString(this);
	}

}
