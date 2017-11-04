package com.enjoyf.platform.service.sync;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-4
 * Time: 下午7:22
 * To change this template use File | Settings | File Templates.
 */
public class ShareBaseInfoField extends AbstractObjectField {
    public static final ShareBaseInfoField SHAREID = new ShareBaseInfoField("share_id", ObjectFieldDBType.LONG, true, true);
    public static final ShareBaseInfoField SHARESOURCEKEY = new ShareBaseInfoField("share_key", ObjectFieldDBType.STRING, false, true);
    public static final ShareBaseInfoField SHARESOURCE = new ShareBaseInfoField("share_source_url", ObjectFieldDBType.STRING, false, true);
    public static final ShareBaseInfoField SHAREDISPLAY_STYLE = new ShareBaseInfoField("share_display_style", ObjectFieldDBType.STRING, false, true);
    public static final ShareBaseInfoField SHAREDIRECT_ID = new ShareBaseInfoField("share_direct_id", ObjectFieldDBType.STRING, false, true);

    public static final ShareBaseInfoField EXPIREDATE = new ShareBaseInfoField("expire_date", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final ShareBaseInfoField SHARETYPE = new ShareBaseInfoField("share_type", ObjectFieldDBType.INT, false, true);
    public static final ShareBaseInfoField CREATEUSERID = new ShareBaseInfoField("createuserid", ObjectFieldDBType.STRING, false, true);
    public static final ShareBaseInfoField CREATEDATE = new ShareBaseInfoField("createdate", ObjectFieldDBType.STRING, false, true);
    public static final ShareBaseInfoField CREATEIP = new ShareBaseInfoField("createip", ObjectFieldDBType.STRING, false, true);


    public static final ShareBaseInfoField CREATEUSERIP = new ShareBaseInfoField("createuserip", ObjectFieldDBType.STRING, false, true);
    public static final ShareBaseInfoField REMOVESTATUS = new ShareBaseInfoField("removestatus", ObjectFieldDBType.STRING, false, true);
    public static final ShareBaseInfoField SHAREREWARDTYPE = new ShareBaseInfoField("share_reward_type", ObjectFieldDBType.INT, false, true);
    public static final ShareBaseInfoField SHAREREWARDPOINT = new ShareBaseInfoField("share_revward_point", ObjectFieldDBType.INT, false, true);
    public static final ShareBaseInfoField SHAREREWARDID = new ShareBaseInfoField("share_reward_id", ObjectFieldDBType.LONG, false, true);


    public static final ShareBaseInfoField LASTMODIFYUSERID = new ShareBaseInfoField("modifyuserid", ObjectFieldDBType.STRING, false, true);
    public static final ShareBaseInfoField LASTMODIFYDATE = new ShareBaseInfoField("modifydate", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final ShareBaseInfoField LASTMODIFYIP = new ShareBaseInfoField("modifyip", ObjectFieldDBType.STRING, false, true);


    public ShareBaseInfoField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ShareBaseInfoField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
