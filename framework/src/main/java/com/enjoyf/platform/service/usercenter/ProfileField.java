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
public class ProfileField extends AbstractObjectField {

    //
    public static final ProfileField PROFILEID = new ProfileField("profile_id", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField UID = new ProfileField("uid", ObjectFieldDBType.LONG, true, false);
    public static final ProfileField NICK = new ProfileField("nick", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField DESCRIPTION = new ProfileField("description", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField ICON = new ProfileField("icon", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField ICONS = new ProfileField("icons", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField CREATETIME = new ProfileField("createtime", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ProfileField CREATEIP = new ProfileField("createip", ObjectFieldDBType.LONG, true, false);
    public static final ProfileField UNO = new ProfileField("uno", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField PROFILEKEY = new ProfileField("profilekey", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField FLAG = new ProfileField("flag", ObjectFieldDBType.INT, true, false);
    public static final ProfileField UPDATETIME = new ProfileField("updatetime", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ProfileField SEX = new ProfileField("sex", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField DOMAIN = new ProfileField("domain", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField CHECKNICK = new ProfileField("checknick", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField PROVINCEID = new ProfileField("provinceid", ObjectFieldDBType.INT, true, false);
    public static final ProfileField CITYID = new ProfileField("cityid", ObjectFieldDBType.INT, true, false);
    public static final ProfileField MOBILE = new ProfileField("mobile", ObjectFieldDBType.STRING, true, false);
    //signature,level,experience,backPic,hobby
    public static final ProfileField SIGNATURE = new ProfileField("signature", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField LEVEL = new ProfileField("level", ObjectFieldDBType.INT, true, false);
    public static final ProfileField EXPERIENCE = new ProfileField("experience", ObjectFieldDBType.INT, true, false);
    public static final ProfileField BACKPIC = new ProfileField("backpic", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField HOBBY = new ProfileField("hobby", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField BIRTHDAY = new ProfileField("birthday", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField REALNAME = new ProfileField("realname", ObjectFieldDBType.STRING, true, false);
    public static final ProfileField APPKEY = new ProfileField("appkey", ObjectFieldDBType.STRING, true, false);

    //
    public ProfileField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
