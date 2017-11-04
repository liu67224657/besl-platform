package com.enjoyf.platform.service.rate.key;

import com.enjoyf.platform.service.rate.RateKeyDomain;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class LoginWrongPwdRateKey extends StringRateKey {

    public LoginWrongPwdRateKey(String loginName) {
        super(RateKeyDomain.LOGIN_WRONG_PWD, loginName);
    }
}
