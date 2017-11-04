package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-3 下午3:53
 * Description:
 */
public class JoymeOperateLogField extends AbstractObjectField {
    public static final JoymeOperateLogField OPERATE_LOG_ID = new JoymeOperateLogField("operate_log_id", ObjectFieldDBType.LONG, false, true);
    public static final JoymeOperateLogField OPERATE_TYPE = new JoymeOperateLogField("operate_type", ObjectFieldDBType.INT, false, true);
    public static final JoymeOperateLogField OPERATE_SERVER_ID = new JoymeOperateLogField("operate_server_id", ObjectFieldDBType.STRING, false, true);
    public static final JoymeOperateLogField OPERATE_ID = new JoymeOperateLogField("operate_id", ObjectFieldDBType.LONG, false, true);
    public static final JoymeOperateLogField OPERATE_TIME = new JoymeOperateLogField("operate_time", ObjectFieldDBType.TIMESTAMP, false, true);

    //
    public JoymeOperateLogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public JoymeOperateLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
