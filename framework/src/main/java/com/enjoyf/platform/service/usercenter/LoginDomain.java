/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.regex.RegexUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 *
 * <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:登录方式
 */
public class LoginDomain implements Serializable {
    private static Map<String, LoginDomain> map = new HashMap<String, LoginDomain>();

    //default account domain..
    public static final LoginDomain EXPLORE = new LoginDomain("explore");
    public static final LoginDomain EMAIL = new LoginDomain("email");
    public static final LoginDomain CLIENT = new LoginDomain("client");
    public static final LoginDomain SINAWEIBO = new LoginDomain("sinaweibo");
    public static final LoginDomain QWEIBO = new LoginDomain("qweibo");
    public static final LoginDomain QQ = new LoginDomain("qq");
    public static final LoginDomain WEIXIN = new LoginDomain("weixin"); //订阅号
    public static final LoginDomain RENREN = new LoginDomain("renren");
    public static final LoginDomain MOBILE = new LoginDomain("mobile");
    public static final LoginDomain WXLOGIN = new LoginDomain("wxlogin"); //微信登陆

    public static final LoginDomain WXSERVICE = new LoginDomain("wxserv"); //微信登陆
    public static final LoginDomain YOUKU = new LoginDomain("youku"); //优酷登陆

    //
    private String code;

    public LoginDomain(String c) {
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
        return "LoginDomain: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        return !((obj == null) || !(obj instanceof LoginDomain)) && code.equalsIgnoreCase(((LoginDomain) obj).getCode());
    }

    public static LoginDomain getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<LoginDomain> getAll() {
        return map.values();
    }


    /**
     * 通过账户获取logindomain
     * @param loginKey 登录名称
     * @return 目前支持email,mobile如果不符合规则返回null
     */
    public static LoginDomain getLoginDomainByLoginKey(String loginKey) {
        Matcher matcher = RegexUtil.EMAIL_PATTERN.matcher(loginKey);
        boolean isEmail = matcher.matches();
        if (isEmail) {
            return LoginDomain.EMAIL;
        }

        matcher = RegexUtil.MOBILE_PATTERN.matcher(loginKey);
        boolean isMobile = matcher.matches();
        if (isMobile) {
            return LoginDomain.MOBILE;
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(LoginDomain.getLoginDomainByLoginKey("15901019849"));
        System.out.println(LoginDomain.getLoginDomainByLoginKey("liu67224657@qq.com"));
        System.out.println(LoginDomain.getLoginDomainByLoginKey("sdfsdafds"));
    }

}
