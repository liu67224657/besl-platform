package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-17
 * Time: 下午2:59
 * To change this template use File | Settings | File Templates.
 */
public class TaskGroupField extends AbstractObjectField {


    //task_group_id,task_group_name,appkey,task_count,task_award, platform,create_time,create_userid,remove_status
    public static final TaskGroupField TASK_GROUP_ID = new TaskGroupField("task_group_id", ObjectFieldDBType.STRING, false, true);
    public static final TaskGroupField TASK_GROUP_NAME = new TaskGroupField("task_group_name", ObjectFieldDBType.STRING, true, false);
    public static final TaskGroupField APPKEY = new TaskGroupField("appkey", ObjectFieldDBType.STRING);
    public static final TaskGroupField TYPE = new TaskGroupField("type", ObjectFieldDBType.STRING);
    public static final TaskGroupField TASK_COUNT = new TaskGroupField("task_count", ObjectFieldDBType.INT);
    public static final TaskGroupField SHOW_TYPE = new TaskGroupField("showtype", ObjectFieldDBType.INT);
    public static final TaskGroupField TASK_AWARD = new TaskGroupField("task_award", ObjectFieldDBType.STRING);
    public static final TaskGroupField DISPLAY_ORDER = new TaskGroupField("display_order", ObjectFieldDBType.INT, true, false);
    public static final TaskGroupField PLATFORM = new TaskGroupField("platform", ObjectFieldDBType.INT);
    public static final TaskGroupField REMOVE_STATUS = new TaskGroupField("remove_status", ObjectFieldDBType.STRING);

    public static final TaskGroupField MODIFY_TIME = new TaskGroupField("modify_time", ObjectFieldDBType.TIMESTAMP);
    public static final TaskGroupField MODIFY_USERID = new TaskGroupField("modify_userid", ObjectFieldDBType.STRING);

    public TaskGroupField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public TaskGroupField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
