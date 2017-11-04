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
public class ProfileSettingField extends AbstractObjectField {
    //
    public static final ProfileSettingField ALLOWCONTENTREPLYTYPE = new ProfileSettingField("ALLOWCONTENTREPLYTYPE", ObjectFieldDBType.STRING, true, true);

    public static final ProfileSettingField ALLOWLETTERTYPE = new ProfileSettingField("ALLOWLETTERTYPE", ObjectFieldDBType.STRING, true, true);
    public static final ProfileSettingField LETTERHEAD = new ProfileSettingField("LETTERHEAD", ObjectFieldDBType.STRING, true, false);

    public static final ProfileSettingField ALLOWSUBMIT = new ProfileSettingField("ALLOWSUBMIT", ObjectFieldDBType.STRING, true, false);
    public static final ProfileSettingField ALLOWSUBMITTYPE = new ProfileSettingField("ALLOWSUBMITTYPE", ObjectFieldDBType.INT, true, false);

    public static final ProfileSettingField ALLOWTAG = new ProfileSettingField("ALLOWTAG", ObjectFieldDBType.STRING, true, false);
    public static final ProfileSettingField HINTMYFANS = new ProfileSettingField("HINTMYFANS", ObjectFieldDBType.STRING, true, false);
    public static final ProfileSettingField HINTMYFEEDBACK = new ProfileSettingField("HINTMYFEEDBACK", ObjectFieldDBType.STRING, true, false);

    public static final ProfileSettingField HINTMYLETTER = new ProfileSettingField("HINTMYLETTER", ObjectFieldDBType.STRING, true, false);
    public static final ProfileSettingField HINTMYNOTICE = new ProfileSettingField("HINTMYNOTICE", ObjectFieldDBType.STRING, true, false);
    public static final ProfileSettingField HINTATME = new ProfileSettingField("HINTATME", ObjectFieldDBType.STRING, true, false);

    public static final ProfileSettingField PAGETITLENUM = new ProfileSettingField("PAGETITLENUM", ObjectFieldDBType.INT, true, false);

    public static final ProfileSettingField SYNCSET = new ProfileSettingField("EXTSTR01", ObjectFieldDBType.STRING, true, false);
    public static final ProfileSettingField ATSET = new ProfileSettingField("EXTSTR02", ObjectFieldDBType.STRING, true, false);
    public static final ProfileSettingField ALLOWEXPSCHOOL = new ProfileSettingField("EXTSTR03", ObjectFieldDBType.STRING, true, false);
    public static final ProfileSettingField ALLOWEXPCOMP = new ProfileSettingField("EXTSTR04", ObjectFieldDBType.STRING, true, false);

    //社交端--赞
    public static final ProfileSettingField ALLOW_AGREE_SOCIAL = new ProfileSettingField("EXTSTR05", ObjectFieldDBType.STRING, true, false);
    //社交端--评论
    public static final ProfileSettingField ALLOW_REPLY_SOCIAL = new ProfileSettingField("EXTSTR06", ObjectFieldDBType.STRING, true, false);
    //社交端--关注
    public static final ProfileSettingField ALLOW_FOCUS_SOCIAL = new ProfileSettingField("EXTSTR07", ObjectFieldDBType.STRING, true, false);
    //社交端--提示音
    public static final ProfileSettingField ALLOW_SOUND_SOCIAL = new ProfileSettingField("EXTSTR08", ObjectFieldDBType.STRING, true, false);

    //
    //
    public ProfileSettingField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileSettingField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
