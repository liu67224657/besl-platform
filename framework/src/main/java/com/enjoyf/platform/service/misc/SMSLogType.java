/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.misc;

import com.google.common.base.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class SMSLogType {
    private static Map<String, SMSLogType> map = new HashMap<String, SMSLogType>();

    public static final SMSLogType VERIFY_CODE = new SMSLogType("verifyCode");   //发送验证码

    public static final SMSLogType GIFT_CODE = new SMSLogType("giftCode"); //发送礼包兑换码

    public static final SMSLogType SOCIAL_CLIENT_REGISTER = new SMSLogType("socialClientRegister");//社交端 注册

    private String code;

    public SMSLogType(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "SMSLogType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof SMSLogType)) {
            return false;
        }

        return code.equalsIgnoreCase(((SMSLogType) obj).getCode());
    }

    public static SMSLogType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<SMSLogType> getAll() {
        return map.values();
    }
}
