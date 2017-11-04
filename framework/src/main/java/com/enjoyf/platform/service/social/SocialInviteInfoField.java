package com.enjoyf.platform.service.social;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-21
 * Time: 上午7:39
 * To change this template use File | Settings | File Templates.
 */
public class SocialInviteInfoField extends AbstractObjectField {

    public static final SocialInviteInfoField INVITEID = new SocialInviteInfoField("INVITEID", ObjectFieldDBType.LONG, false, true);
    public static final SocialInviteInfoField SRCUNO = new SocialInviteInfoField("SRCUNO", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteInfoField INVITEDOMAIN = new SocialInviteInfoField("INVITEDOMAIN", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteInfoField DESTID = new SocialInviteInfoField("DESTID", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteInfoField CREATEDATE = new SocialInviteInfoField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, false, false);
    public static final SocialInviteInfoField CREATEIP = new SocialInviteInfoField("CREATEIP", ObjectFieldDBType.STRING, false, false);


    //
    public SocialInviteInfoField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialInviteInfoField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
