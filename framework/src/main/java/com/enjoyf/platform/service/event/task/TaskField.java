package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-17
 * Time: 下午2:59
 * To change this template use File | Settings | File Templates.
 */
public class TaskField extends AbstractObjectField {

    public static final TaskField TASK_ID = new TaskField("task_id", ObjectFieldDBType.STRING);
    public static final TaskField APPKEY = new TaskField("appkey", ObjectFieldDBType.STRING);
    public static final TaskField PLATFORM = new TaskField("platform", ObjectFieldDBType.INT);
    public static final TaskField TASK_GROUP_ID = new TaskField("task_group_id", ObjectFieldDBType.STRING);
    public static final TaskField DIRECT_ID = new TaskField("direct_id", ObjectFieldDBType.STRING);
    public static final TaskField TASK_TYPE = new TaskField("task_type", ObjectFieldDBType.INT);
    public static final TaskField TASK_NAME = new TaskField("task_name", ObjectFieldDBType.STRING);
    public static final TaskField TASK_DESC = new TaskField("task_desc", ObjectFieldDBType.STRING);
    public static final TaskField TASK_PIC = new TaskField("task_pic", ObjectFieldDBType.STRING);
    public static final TaskField TASK_AWARD = new TaskField("task_award", ObjectFieldDBType.STRING);
    public static final TaskField OVER_TIMES = new TaskField("over_times", ObjectFieldDBType.INT);
    public static final TaskField CREATE_TIME = new TaskField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final TaskField CREATE_USERID = new TaskField("create_userid", ObjectFieldDBType.STRING);
    public static final TaskField MODIFY_TIME = new TaskField("modify_time", ObjectFieldDBType.TIMESTAMP);
    public static final TaskField MODIFY_USERID = new TaskField("modify_userid", ObjectFieldDBType.STRING);
    public static final TaskField DISPLAY_ORDER = new TaskField("display_order", ObjectFieldDBType.INT);
    public static final TaskField REMOVE_STATUS = new TaskField("remove_status", ObjectFieldDBType.STRING);

    public static final TaskField REDIRECT_TYPE = new TaskField("redirect_type", ObjectFieldDBType.INT);
    public static final TaskField REDIRECT_URI = new TaskField("redirect_uri", ObjectFieldDBType.STRING);
    public static final TaskField AUTO_SENDAWARD = new TaskField("auto_sendaward", ObjectFieldDBType.BOOLEAN);
    public static final ObjectField TASK_ACTION = new TaskField("task_action", ObjectFieldDBType.INT);
    public static final ObjectField TASK_HOUR = new TaskField("task_hour", ObjectFieldDBType.INT);
    public static final ObjectField TASK_VERIFY_ID = new TaskField("task_verify_id", ObjectFieldDBType.INT);
    public static final ObjectField TASK_JSONINFO = new TaskField("task_jsoninfo", ObjectFieldDBType.STRING);

    public static final ObjectField BEGIN_TIME = new TaskField("begin_time", ObjectFieldDBType.TIMESTAMP);
    public static final ObjectField END_TIME = new TaskField("end_time", ObjectFieldDBType.TIMESTAMP);

    public TaskField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public TaskField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
