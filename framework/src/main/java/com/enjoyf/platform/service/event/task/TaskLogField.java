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
public class TaskLogField extends AbstractObjectField {

    public static final TaskLogField LOG_ID = new TaskLogField("log_id", ObjectFieldDBType.STRING, true, true);
    public static final TaskLogField PROFILE_ID = new TaskLogField("profileid", ObjectFieldDBType.STRING, true, false);
    public static final TaskLogField TASK_ID = new TaskLogField("task_id", ObjectFieldDBType.STRING, true, false);
    public static final TaskLogField APPKEY = new TaskLogField("appkey", ObjectFieldDBType.STRING, true, false);
    public static final TaskLogField PLATFORM = new TaskLogField("platform", ObjectFieldDBType.INT, true, false);
    public static final TaskLogField OVER_DATE = new TaskLogField("over_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final TaskLogField OVER_IP = new TaskLogField("over_ip", ObjectFieldDBType.STRING, true, false);
    public static final TaskLogField OVER_STATUS = new TaskLogField("over_status", ObjectFieldDBType.STRING, true, false);
    public static final TaskLogField OVER_TIMES = new TaskLogField("over_times", ObjectFieldDBType.INT, true, false);
    public static final TaskLogField TASK_TYPE = new TaskLogField("task_type", ObjectFieldDBType.INT, true, false);


    public TaskLogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public TaskLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
