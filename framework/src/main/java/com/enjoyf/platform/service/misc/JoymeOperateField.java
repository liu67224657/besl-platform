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
public class JoymeOperateField extends AbstractObjectField {
    public static final JoymeOperateField OPERATE_ID = new JoymeOperateField("operate_id", ObjectFieldDBType.LONG, false, true);
    public static final JoymeOperateField OPERATE_TYPE = new JoymeOperateField("operate_type", ObjectFieldDBType.INT, false, true);
    public static final JoymeOperateField OPERATE_CONTENT = new JoymeOperateField("operate_content", ObjectFieldDBType.STRING, false, true);
    public static final JoymeOperateField CREATE_SERVER_ID = new JoymeOperateField("create_server_id", ObjectFieldDBType.STRING, false, true);
    public static final JoymeOperateField CREATE_TIME = new JoymeOperateField("create_time", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final JoymeOperateField CREATE_USERID = new JoymeOperateField("create_userid", ObjectFieldDBType.STRING, false, true);

    //
    public JoymeOperateField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public JoymeOperateField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
