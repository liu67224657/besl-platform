package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class WikiGameresField extends AbstractObjectField {
    public static final WikiGameresField ID = new WikiGameresField("id", ObjectFieldDBType.LONG);
    public static final WikiGameresField GAMEID = new WikiGameresField("game_id", ObjectFieldDBType.LONG);
    public static final WikiGameresField GAMENAME = new WikiGameresField("game_name", ObjectFieldDBType.STRING);
    public static final WikiGameresField HEADPIC = new WikiGameresField("head_pic", ObjectFieldDBType.STRING);
    public static final WikiGameresField VALIDSTATUS = new WikiGameresField("valid_status", ObjectFieldDBType.STRING);
    public static final WikiGameresField RECOMMEND = new WikiGameresField("recommend", ObjectFieldDBType.INT);
    public static final WikiGameresField DISPLAYORDER = new WikiGameresField("display_order", ObjectFieldDBType.INT);
    public static final WikiGameresField CREATETIME = new WikiGameresField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final WikiGameresField UPDATETIME = new WikiGameresField("update_time", ObjectFieldDBType.TIMESTAMP);
    public static final WikiGameresField UPDATEUSER = new WikiGameresField("update_user", ObjectFieldDBType.STRING);
    public static final WikiGameresField FOCUS_SUM = new WikiGameresField("focus_sum", ObjectFieldDBType.INT);


    public WikiGameresField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}