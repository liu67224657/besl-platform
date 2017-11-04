package com.enjoyf.platform.webapps.common;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-11-1
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public class CommonMail implements Serializable {
    private static Map<String, CommonMail> map = new HashMap<String, CommonMail>();

    private static final CommonMail QQ_COM = new CommonMail("@qq.com", "http://mail.qq.com");
    private static final CommonMail ONE_SIX_THREE_COM = new CommonMail("@163.com", "http://mail.163.com");
    private static final CommonMail ONE_TWO_SIX_COM = new CommonMail("@126.com", "http://mail.126.com");
    private static final CommonMail SINA_COM = new CommonMail("@sina.com", "http://mail.sina.com");
    private static final CommonMail YAHOO_COM_CN = new CommonMail("@yahoo.com.cn", "http://mail.yahoo.com");
    private static final CommonMail YAHOO_CN = new CommonMail("@yahoo.cn", "http://mail.yahoo.com");
    private static final CommonMail VIP_QQ_COM = new CommonMail("@vip.qq.com", "http://mail.vip.qq.com");
    private static final CommonMail SOHU_COM = new CommonMail("@sohu.com", "http://mail.sohu.com");
    private static final CommonMail TOM_COM = new CommonMail("@tom.com", "http://mail.tom.com");
    private static final CommonMail YAHOO_COM = new CommonMail("@yahoo.com", "http://mail.yahoo.com");
    private static final CommonMail LIVE_CN = new CommonMail("@live.cn", "http://mail.live.com");
    private static final CommonMail TWO_ONECN_COM = new CommonMail("@21cn.com", "http://mail.21cn.com");
    private static final CommonMail HOTMAIL_COM = new CommonMail("@hotmail.com", "http://hotmail.com");
    private static final CommonMail GAMEIL_COM = new CommonMail("@gmail.com", "http://gmail.com");
    private static final CommonMail YEAH_NET = new CommonMail("@yeah.net", "http://www.yeah.net");


    private String code;
    private String server;

    private CommonMail(String c, String server) {
        code = c.toLowerCase();
        this.server = server;
        map.put(code.toLowerCase(), this);
    }

    public String getCode() {
        return code;
    }

    public String getServer() {
        return server;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof CommonMail) {
            return code.equalsIgnoreCase(((CommonMail) obj).getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public static CommonMail getByMail(String email) {
        int atIdx = email.lastIndexOf('@');

        if (atIdx == -1) {
            return null;
        }
        String code = email.substring(atIdx);
        return getByCode(code);
    }

    public static CommonMail getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }
        return map.get(c.toLowerCase());
    }

    public static Collection getAll() {
        return map.values();
    }
}
