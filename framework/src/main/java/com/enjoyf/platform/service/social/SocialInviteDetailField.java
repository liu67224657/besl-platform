package com.enjoyf.platform.service.social;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-21
 * Time: 上午7:39
 * To change this template use File | Settings | File Templates.
 */
public class SocialInviteDetailField extends AbstractObjectField {

    public static final SocialInviteDetailField INVITEID = new SocialInviteDetailField("INVITEID", ObjectFieldDBType.LONG, false, false);
    public static final SocialInviteDetailField GID = new SocialInviteDetailField("GID", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteDetailField INVITEUNO = new SocialInviteDetailField("INVITEUNO", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteDetailField INVITEDOMAIN = new SocialInviteDetailField("INVITEDOMAIN", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteDetailField DIRECTUNO = new SocialInviteDetailField("DIRECTUNO", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteDetailField CREATEDATE = new SocialInviteDetailField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, false, false);
    public static final SocialInviteDetailField CREATEIP = new SocialInviteDetailField("CREATEIP", ObjectFieldDBType.STRING, false, false);


    //
    public SocialInviteDetailField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialInviteDetailField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
