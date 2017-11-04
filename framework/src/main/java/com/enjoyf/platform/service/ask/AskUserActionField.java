package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class AskUserActionField extends AbstractObjectField {
    public static final AskUserActionField ASKUSERACTIONID = new AskUserActionField("ask_user_action_id", ObjectFieldDBType.STRING);
    public static final AskUserActionField PROFILEID = new AskUserActionField("profile_id", ObjectFieldDBType.STRING);
    public static final AskUserActionField ITEMTYPE = new AskUserActionField("item_type", ObjectFieldDBType.INT);
    public static final AskUserActionField DESTID = new AskUserActionField("dest_id", ObjectFieldDBType.LONG);
    public static final AskUserActionField ACTIONTYPE = new AskUserActionField("action_type", ObjectFieldDBType.INT);
    public static final AskUserActionField CREATETIME = new AskUserActionField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final AskUserActionField ACTIONVALUE = new AskUserActionField("action_value", ObjectFieldDBType.STRING);


    public AskUserActionField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}