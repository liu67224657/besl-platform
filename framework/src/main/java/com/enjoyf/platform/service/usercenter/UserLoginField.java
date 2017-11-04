/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 11-12-23 上午14:56
 * Description:
 */
public class UserLoginField extends AbstractObjectField {

    //
    public static final UserLoginField LOGIN_ID = new UserLoginField("login_id", ObjectFieldDBType.STRING, true, false);
    public static final UserLoginField LOGIN_KEY = new UserLoginField("login_key", ObjectFieldDBType.STRING, true, false);
    public static final UserLoginField LOGIN_PASSWORD = new UserLoginField("login_password", ObjectFieldDBType.STRING, true, false);
    public static final UserLoginField LOGIN_NAME = new UserLoginField("login_name", ObjectFieldDBType.INT, true, false);
    public static final UserLoginField LOGIN_DOMAIN = new UserLoginField("login_domain", ObjectFieldDBType.STRING, true, false);
    public static final UserLoginField UNO = new UserLoginField("uno", ObjectFieldDBType.STRING, true, false);
    public static final UserLoginField CREATETIME = new UserLoginField("createtime", ObjectFieldDBType.LONG, true, false);
    public static final UserLoginField CREATEIP = new UserLoginField("createip", ObjectFieldDBType.LONG, true, false);
    public static final UserLoginField TOKEN_INFO = new UserLoginField("token_info", ObjectFieldDBType.STRING, true, false);
    public static final UserLoginField PASSWDTIME = new UserLoginField("passwdtime", ObjectFieldDBType.STRING, true, false);
    public static final ObjectField UPDATETIME = new UserLoginField("updatetime", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ObjectField UPDATEIP = new UserLoginField("updateip", ObjectFieldDBType.STRING, true, false);
    public static final ObjectField AUTHCODE = new UserLoginField("auth_code", ObjectFieldDBType.STRING, true, false);
    public static final ObjectField AUTHTIME = new UserLoginField("auth_time", ObjectFieldDBType.TIMESTAMP, true, false);


    //
    public UserLoginField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public UserLoginField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
