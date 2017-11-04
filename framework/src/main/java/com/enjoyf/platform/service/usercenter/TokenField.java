/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 11-12-23 上午14:56
 * Description:
 */
public class TokenField extends AbstractObjectField {

    //
    public static final TokenField TOKEN = new TokenField("token", ObjectFieldDBType.STRING, true, false);
    public static final TokenField TOKEN_TYPE = new TokenField("token_type", ObjectFieldDBType.STRING, true, false);
    public static final TokenField PROFILEAPPKEY = new TokenField("profilekey", ObjectFieldDBType.STRING, true, false);
    public static final TokenField TOKEN_EXPIRES = new TokenField("token_expires", ObjectFieldDBType.INT, true, false);
    public static final TokenField TOKEN_UNO = new TokenField("token_uno", ObjectFieldDBType.STRING, true, false);
    public static final TokenField TOKEN_UID = new TokenField("token_uid", ObjectFieldDBType.LONG, true, false);
    public static final TokenField REQUEST_PARAMETER = new TokenField("request_parameter", ObjectFieldDBType.STRING, true, false);

    //
    public TokenField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public TokenField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
