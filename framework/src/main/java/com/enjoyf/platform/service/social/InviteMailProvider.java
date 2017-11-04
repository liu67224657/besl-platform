/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.social;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@enjoyfound.com">Eric Liu</a>
 * Create time: 11-8-17 下午1:23
 * Description:邀请邮箱导入的供应商
 */
public class InviteMailProvider implements Serializable {
    private static Map<String, InviteMailProvider> map = new HashMap<String, InviteMailProvider>();

    public static final InviteMailProvider MAIL_SOHU = new InviteMailProvider("sohu","@sohu.com");
    public static final InviteMailProvider MAIL_126 = new InviteMailProvider("126","@126.com");
    public static final InviteMailProvider MAIL_GMAIL = new InviteMailProvider("gmail","@gmail.com");

    public static final InviteMailProvider MAIL_MSN = new InviteMailProvider("msn","@hotmail.com");

    private String code;
    private String suffixMail;

    public InviteMailProvider(String c, String suffixMail) {
        code = c.toLowerCase();
        this.suffixMail=suffixMail;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public String getSuffixMail() {
        return suffixMail;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "InviteMailProvider: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof InviteMailProvider)) {
            return false;
        }

        return code.equalsIgnoreCase(((InviteMailProvider) obj).getCode());
    }

    public static InviteMailProvider getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<InviteMailProvider> getAll() {
        return map.values();
    }
}
