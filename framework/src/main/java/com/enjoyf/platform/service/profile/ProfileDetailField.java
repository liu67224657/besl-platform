/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class ProfileDetailField extends AbstractObjectField {
    //
    public static final ProfileDetailField UNO = new ProfileDetailField("UNO", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDetailField REALNAME = new ProfileDetailField("REALNAME", ObjectFieldDBType.STRING, true, true);

    public static final ProfileDetailField SEX = new ProfileDetailField("SEX", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDetailField BIRTHDAY = new ProfileDetailField("BIRTHDAY", ObjectFieldDBType.STRING, true, false);

    public static final ProfileDetailField PROVINCEID = new ProfileDetailField("PROVINCEID", ObjectFieldDBType.INT, true, false);
    public static final ProfileDetailField CITYID = new ProfileDetailField("CITYID", ObjectFieldDBType.INT, true, false);

    public static final ProfileDetailField QQ = new ProfileDetailField("QQ", ObjectFieldDBType.STRING, true, false);
    public static final ProfileDetailField MSN = new ProfileDetailField("MSN", ObjectFieldDBType.STRING, true, false);
    public static final ProfileDetailField INTEREST = new ProfileDetailField("INTEREST", ObjectFieldDBType.STRING, true, false);

    public static final ProfileDetailField COMPLETESTATUS = new ProfileDetailField("COMPLETESTATUS", ObjectFieldDBType.STRING, true, false);

    public static final ProfileDetailField VERIFYSTATUS = new ProfileDetailField("VERIFYSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final ProfileDetailField VERIFYDESC = new ProfileDetailField("VERIFYDESC", ObjectFieldDBType.STRING, true, false);


    public static final ProfileBlogField UPDATEDATE = new ProfileBlogField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public ProfileDetailField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileDetailField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
