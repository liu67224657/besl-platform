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
public class ProfileSumField extends AbstractObjectField {
    //
    public static final ProfileSumField FOCUSSUM = new ProfileSumField("FOCUSSUM", ObjectFieldDBType.INT, true, false);
    public static final ProfileSumField FANSSUM = new ProfileSumField("FANSSUM", ObjectFieldDBType.INT, true, false);

    public static final ProfileSumField BLOGSUM = new ProfileSumField("BLOGSUM", ObjectFieldDBType.INT, true, false);
    public static final ProfileSumField FORWARDSUM = new ProfileSumField("FORWARDSUM", ObjectFieldDBType.INT, true, false);
    public static final ProfileSumField FAVORSUM = new ProfileSumField("FAVORSUM", ObjectFieldDBType.INT, true, false);

    public static final ProfileSumField LASTCONTENTID = new ProfileSumField("EXTSTR01", ObjectFieldDBType.STRING, true, false);
    public static final ProfileSumField LASTREPLYID = new ProfileSumField("EXTSTR02", ObjectFieldDBType.STRING, true, false);

     public static final ProfileSumField SOCIALBLOGSUM = new ProfileSumField("SOCIALBLOGSUM", ObjectFieldDBType.INT, true, false);
     public static final ProfileSumField SOCIALPLAYSUM = new ProfileSumField("SOCIALPLAYSUM", ObjectFieldDBType.INT, true, false);


    public static final ProfileSumField SOCIALFOCUSSUM = new ProfileSumField("SOCIALFOCUSSUM", ObjectFieldDBType.INT, true, false);
     public static final ProfileSumField SOCIALFANSSUM = new ProfileSumField("SOCIALFANSSUM", ObjectFieldDBType.INT, true, false);
    //social message sum
    public static final ProfileSumField SOCIALAGREEMSGSUM = new ProfileSumField("EXTNUM01", ObjectFieldDBType.INT, true, false);
    public static final ProfileSumField SOCIALREPLYMSGSUM = new ProfileSumField("EXTNUM02", ObjectFieldDBType.INT, true, false);
    public static final ProfileSumField SOCIALNOTICEMSGSUM = new ProfileSumField("EXTNUM03", ObjectFieldDBType.INT, true, false);
    //
    public ProfileSumField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileSumField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
