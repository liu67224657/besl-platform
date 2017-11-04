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
public class ProfileBlogField extends AbstractObjectField {
    //
    public static final ProfileBlogField UNO = new ProfileBlogField("UNO", ObjectFieldDBType.STRING, true, true);
    public static final ProfileBlogField SCREENNAME = new ProfileBlogField("SCREENNAME", ObjectFieldDBType.STRING, true, true);
    public static final ProfileBlogField DOMAINNAME = new ProfileBlogField("DOMAINNAME", ObjectFieldDBType.STRING, true, true);

    public static final ProfileBlogField HEADICON = new ProfileBlogField("HEADICON", ObjectFieldDBType.STRING, true, false);
    public static final ProfileBlogField DESCRIPTION = new ProfileBlogField("BLOGDESCRIPTION", ObjectFieldDBType.STRING, true, false);

    public static final ProfileBlogField TEMPLATEID = new ProfileBlogField("TEMPLATEID", ObjectFieldDBType.STRING, true, false);
    public static final ProfileBlogField SELFSETSTATUS = new ProfileBlogField("SELFSETSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final ProfileBlogField SELFSETDATA = new ProfileBlogField("SELFSETDATA", ObjectFieldDBType.STRING, true, false);

    public static final ProfileBlogField UPDATEDATE = new ProfileBlogField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ProfileBlogField MOREHEADICONS = new ProfileBlogField("MOREHEADICONS", ObjectFieldDBType.STRING, true, false);

    public static final ProfileBlogField AUDITSTATUS = new ProfileBlogField("AUDITSTATUS", ObjectFieldDBType.INT, true, false); //审核标记字段
    public static final ProfileBlogField AUDITDATE = new ProfileBlogField("AUDITDATE", ObjectFieldDBType.TIMESTAMP, true, false);//审核时间
    public static final ProfileBlogField AUDITUSERID = new ProfileBlogField("AUDITUSERID", ObjectFieldDBType.STRING, true, false);//审核人

    public static final ProfileBlogField ACTIVESTATUS = new ProfileBlogField("ACTIVESTATUS", ObjectFieldDBType.STRING, true, false);
    public static final ProfileBlogField INACTIVETILLDATE = new ProfileBlogField("INACTIVETILLDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final ProfileBlogField PROFILEDOMAIN = new ProfileBlogField("PROFILEDOMAIN", ObjectFieldDBType.STRING, true, false);

    public static final ProfileBlogField CREATEDATE = new ProfileBlogField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);//创建时间
    public static final ProfileBlogField USERID = new ProfileBlogField("USERID", ObjectFieldDBType.LONG, true, false);//创建时间


    public static final ProfileBlogField PHONENUM = new ProfileBlogField("PHONENUM", ObjectFieldDBType.STRING, true, false);
    public static final ProfileBlogField PHONEVERIFYSTATUS = new ProfileBlogField("PHONEVERIFYSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final ProfileBlogField PHONEBINDDATE = new ProfileBlogField("PHONEBINDDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final ProfileBlogField PLAYINGGAMES = new ProfileBlogField("PLAYINGGAMES", ObjectFieldDBType.STRING, true, false);
    public static final ProfileBlogField BACKGROUNDPIC = new ProfileBlogField("BACKGROUNDPIC", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public ProfileBlogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileBlogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
