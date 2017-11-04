/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="erickliu@enjoyfound.com">ericliu</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class GamePrivacyField extends AbstractObjectField {
    //RESOURCEID,UNO,PRIVCAYLEVEL,RESOURCEDOMAIN,CREATEDATE,CREATEUSERID,UPDATEDATE,UPDATEUSERID
    public static final GamePrivacyField RESOURCEID = new GamePrivacyField("RESOURCEID", ObjectFieldDBType.LONG, false, true);
    public static final GamePrivacyField UNO = new GamePrivacyField("UNO", ObjectFieldDBType.STRING, false, true);
    public static final GamePrivacyField PRIVCAYLEVEL = new GamePrivacyField("PRIVCAYLEVEL", ObjectFieldDBType.STRING, false, true);
    public static final GamePrivacyField RESOURCEDOMAIN = new GamePrivacyField("RESOURCEDOMAIN", ObjectFieldDBType.STRING, true, true);

    public static final GamePrivacyField CREATEDATE = new GamePrivacyField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final GamePrivacyField CREATEUSERID = new GamePrivacyField("CREATEUSERID", ObjectFieldDBType.STRING, true, true);
    public static final GamePrivacyField UPDATEDATE = new GamePrivacyField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final GamePrivacyField UPDATEUSERID = new GamePrivacyField("UPDATEUSERID", ObjectFieldDBType.STRING, true, true);

   //扩展字段
    public static final GamePrivacyField GROUPID = new GamePrivacyField("GROUPID", ObjectFieldDBType.STRING, true, true);


    //
    public GamePrivacyField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GamePrivacyField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
