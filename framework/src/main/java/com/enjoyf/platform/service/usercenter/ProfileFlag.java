package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ProfileFlag implements Serializable {

    public static final int FLAG_EXPLORE = 0;  //虚拟用户

    public static final int FLAG_CLIENTID = 1;

    public static final int FLAG_SINA = 2;

    public static final int FLAG_QQ = 3;

    public static final int FLAG_WEIXIN = 4; //订阅号

    public static final int FLAG_QWEIBO = 5;

    public static final int FLAG_EMAIL = 6;//邮箱登录

    public static final int FLAG_RENREN = 7;

    public static final int FLAG_WXLOGIN = 8;  //客户端

    public static final int FLAG_NICK_HASCOMPLETE = 9;

    public static final int FLAG_YOUKU = 10;

    public static final int FLAG_WXSERV = 11; //服务号

    public static final int FLAG_MOBILE = 12;



    private int value;


    private static Map<LoginDomain, Integer> domain2Flag = new HashMap<LoginDomain, Integer>();

    static {
        domain2Flag.put(LoginDomain.EXPLORE, FLAG_EXPLORE);
        domain2Flag.put(LoginDomain.CLIENT, FLAG_CLIENTID);
        domain2Flag.put(LoginDomain.WEIXIN, FLAG_WEIXIN);
        domain2Flag.put(LoginDomain.QQ, FLAG_QQ);
        domain2Flag.put(LoginDomain.QWEIBO, FLAG_QWEIBO);
        domain2Flag.put(LoginDomain.SINAWEIBO, FLAG_SINA);

        domain2Flag.put(LoginDomain.MOBILE, FLAG_MOBILE);
        domain2Flag.put(LoginDomain.EMAIL, FLAG_EMAIL);

        domain2Flag.put(LoginDomain.RENREN, FLAG_RENREN);
        domain2Flag.put(LoginDomain.WXLOGIN, FLAG_WXLOGIN);
        domain2Flag.put(LoginDomain.WXSERVICE, FLAG_WXSERV);
        domain2Flag.put(LoginDomain.YOUKU, FLAG_YOUKU);


      }

    public ProfileFlag() {
    }

    public ProfileFlag(int v) {
        value = v;
    }

    public ProfileFlag has(int v) {
        if (v > 0) {
            value += (1 << v);
        }
        return this;
    }

    public ProfileFlag reduce(int v) {
        value -= (1 << v);

        return this;
    }

    public int getValue() {
        return value;
    }

    public boolean hasFlag(int length) {
        return (value & (1 << length)) > 0;
    }

    public boolean equalFlag(int length) {
        return value == (1 << length);
    }

    public boolean hasPassword() {
        return hasFlag(FLAG_EMAIL);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public static int getFlagByLoginDomain(LoginDomain loginDomain) throws ServiceException {
        if (domain2Flag.get(loginDomain) == null) {
            throw UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN;
        }

        return domain2Flag.get(loginDomain) == null ? -1 : domain2Flag.get(loginDomain);
    }


    public Set<LoginDomain> getLoginDomain() {
        Set<LoginDomain> loginDomainSet = new HashSet<LoginDomain>();

        if (this.hasFlag(FLAG_QQ)) {
            loginDomainSet.add(LoginDomain.QQ);
        }
        if (this.hasFlag(FLAG_QWEIBO)) {
            loginDomainSet.add(LoginDomain.QWEIBO);
        }
        if (this.hasFlag(FLAG_SINA)) {
            loginDomainSet.add(LoginDomain.SINAWEIBO);
        }
        if (this.hasFlag(FLAG_RENREN)) {
            loginDomainSet.add(LoginDomain.RENREN);
        }
        if (this.hasFlag(FLAG_YOUKU)) {
            loginDomainSet.add(LoginDomain.YOUKU);
        }
        if (this.hasFlag(FLAG_WXLOGIN)) {
            loginDomainSet.add(LoginDomain.WXLOGIN);
        }
        if (this.hasFlag(FLAG_WXSERV)) {
            loginDomainSet.add(LoginDomain.WXSERVICE);
        }
        if (this.hasFlag(FLAG_MOBILE)) {
            loginDomainSet.add(LoginDomain.MOBILE);
        }

        return loginDomainSet;
    }

}
