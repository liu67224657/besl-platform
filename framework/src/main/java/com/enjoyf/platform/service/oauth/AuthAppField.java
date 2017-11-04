/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class AuthAppField extends AbstractObjectField {
    //
    public static final AuthAppField APPID = new AuthAppField("APPID", ObjectFieldDBType.STRING, true, true);
    public static final AuthAppField APPKEY = new AuthAppField("APPKEY", ObjectFieldDBType.STRING, true, false);

    public static final AuthAppField APPTYPE = new AuthAppField("APPTYPE", ObjectFieldDBType.STRING, true, false);
    public static final AuthAppField PLATOFRM = new AuthAppField("PLATFORM", ObjectFieldDBType.INT, true, false);
    public static final AuthAppField APPDETAIL = new AuthAppField("APPDETAIL", ObjectFieldDBType.STRING, true, false);
    public static final AuthAppField VALIDSTATUS = new AuthAppField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);

    public static final AuthAppField CREATEDATE = new AuthAppField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AuthAppField CREATEIP = new AuthAppField("CREATEIP", ObjectFieldDBType.STRING, true, false);

    public static final AuthAppField AUDITSTATUS = new AuthAppField("AUDITSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final AuthAppField AUDITDATE = new AuthAppField("AUDITDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final AuthAppField AUDITIP = new AuthAppField("AUDITIP", ObjectFieldDBType.STRING, true, false);
    public static final AuthAppField APPNAME = new AuthAppField("APPNAME", ObjectFieldDBType.STRING, true, false);
    public static final AuthAppField PROFILEKEY = new AuthAppField("PROFILEKEY", ObjectFieldDBType.STRING, true, false);
    public static final AuthAppField DISPLAYMY = new AuthAppField("DISPLAYMY", ObjectFieldDBType.INT, true, false);
    public static final AuthAppField DISPLAYRED = new AuthAppField("DISPLAYRED", ObjectFieldDBType.INT, true, false);
    public static final AuthAppField MODIFYDATE = new AuthAppField("MODIFYDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AuthAppField APPSECRET = new AuthAppField("APPSECRET", ObjectFieldDBType.STRING, true, false);
    public static final AuthAppField DEPOSITCALLBACK = new AuthAppField("DEPOSITCALLBACK", ObjectFieldDBType.STRING, true, false);
    public static final AuthAppField GAMEKEY = new AuthAppField("gamekey", ObjectFieldDBType.STRING, true, false);

    //
    public AuthAppField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AuthAppField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
