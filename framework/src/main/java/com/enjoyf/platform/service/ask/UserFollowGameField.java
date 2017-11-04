package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class UserFollowGameField extends AbstractObjectField {
    public static final UserFollowGameField ID = new UserFollowGameField("id", ObjectFieldDBType.LONG);
    public static final UserFollowGameField GAMEID = new UserFollowGameField("game_id", ObjectFieldDBType.LONG);
    public static final UserFollowGameField PROFILEID = new UserFollowGameField("profile_id", ObjectFieldDBType.STRING);
    public static final UserFollowGameField CREATETIME = new UserFollowGameField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final UserFollowGameField MODIFYTIME = new UserFollowGameField("modify_time", ObjectFieldDBType.TIMESTAMP);
    public static final UserFollowGameField VALID_STATUS = new UserFollowGameField("valid_status", ObjectFieldDBType.STRING);


    public UserFollowGameField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}