package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 下午5:18
 * To change this template use File | Settings | File Templates.
 */
public class NewReleaseTagField extends AbstractObjectField{

    public static final NewReleaseTagField NEW_RELEASE_TAG_ID = new NewReleaseTagField("new_game_tag_id", ObjectFieldDBType.LONG, true, true);
    public static final NewReleaseTagField TAG_NAME = new NewReleaseTagField("tag_name", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseTagField IS_HOT = new NewReleaseTagField("is_hot", ObjectFieldDBType.BOOLEAN, true, false);
    public static final NewReleaseTagField IS_TOP = new NewReleaseTagField("is_top", ObjectFieldDBType.BOOLEAN, true, false);
    public static final NewReleaseTagField CREATE_DATE = new NewReleaseTagField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final NewReleaseTagField CREATE_IP = new NewReleaseTagField("create_ip", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseTagField CREATE_USERID = new NewReleaseTagField("create_userid", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseTagField LAST_MODIFY_DATE = new NewReleaseTagField("last_modify_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final NewReleaseTagField LAST_MODIFY_IP = new NewReleaseTagField("last_modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseTagField LAST_MODIFY_USERID = new NewReleaseTagField("last_modify_userid", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseTagField VALIDSTATUS = new NewReleaseTagField("validstatus", ObjectFieldDBType.STRING, true, false);

    public NewReleaseTagField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public NewReleaseTagField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
