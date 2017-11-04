/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.usercenter;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class TokenType implements Serializable {
    private static Map<Integer, TokenType> map = new HashMap<Integer, TokenType>();

    public static final TokenType DEFAULT = new TokenType(0);
    public static final TokenType ONCE = new TokenType(1);
    public static final TokenType TIME_LIMIT = new TokenType(2);


    //
    private Integer code;

    private TokenType(Integer c) {
        code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "TokenType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof TokenType)) {
            return false;
        }

        return code==(((TokenType) obj).getCode());
    }

    public static TokenType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<TokenType> getAll() {
        return map.values();
    }
}
