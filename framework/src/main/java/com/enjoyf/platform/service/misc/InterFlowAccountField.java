package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-28
 * Time: 下午3:08
 * To change this template use File | Settings | File Templates.
 */
public class InterFlowAccountField extends AbstractObjectField {

    //interflow_id,interflowname,interflowaccount,lorduser,interflowtype,dutyuser,usernumber,level,manufacturer,
    //gamename,gamecategory,gametype,platform,theme,publisharea,createdate,createuserid,modifydate,modifyuserid,removestatus
    public static final InterFlowAccountField INTERFLOW_ID = new InterFlowAccountField("interflow_id", ObjectFieldDBType.STRING, true, true);
    public static final InterFlowAccountField INTERFLOW_NAME = new InterFlowAccountField("interflowname", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField INTERFLOW_ACCOUNT = new InterFlowAccountField("interflowaccount", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField LORD_USER = new InterFlowAccountField("lorduser", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField INTERFLOW_TYPE = new InterFlowAccountField("interflowtype", ObjectFieldDBType.INT, true, false);
    public static final InterFlowAccountField DUTY_USER = new InterFlowAccountField("dutyuser", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField USER_NUMBER = new InterFlowAccountField("usernumber", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField LEVEL = new InterFlowAccountField("level", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField MANUFACTURER = new InterFlowAccountField("manufacturer", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField GAME_NAME = new InterFlowAccountField("gamename", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField GAME_CATEGORY = new InterFlowAccountField("gamecategory", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField GAME_TYPE = new InterFlowAccountField("gametype", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField PLATFORM = new InterFlowAccountField("platform", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField THEME = new InterFlowAccountField("theme", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField PUBLISH_AREA = new InterFlowAccountField("publisharea", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField CREATE_DATE = new InterFlowAccountField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final InterFlowAccountField CREATE_USERID = new InterFlowAccountField("createuserid", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField MODIFY_DATE = new InterFlowAccountField("modifydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final InterFlowAccountField MODIFY_USERID = new InterFlowAccountField("modifyuserid", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField REMOVE_STATUS = new InterFlowAccountField("removestatus", ObjectFieldDBType.STRING, true, false);
    public static final InterFlowAccountField LAST_POST = new InterFlowAccountField("lastpost", ObjectFieldDBType.STRING, true, false);

    public InterFlowAccountField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public InterFlowAccountField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
