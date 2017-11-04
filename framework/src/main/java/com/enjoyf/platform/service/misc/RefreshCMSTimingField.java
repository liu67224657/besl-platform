package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.tools.PrivilegeResourceField;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2015/7/28.
 */
public class RefreshCMSTimingField extends AbstractObjectField {

    //
    public static final PrivilegeResourceField TIME_ID = new PrivilegeResourceField("time_id", ObjectFieldDBType.LONG, true, false);
    public static final PrivilegeResourceField CMS_ID = new PrivilegeResourceField("cms_id", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeResourceField CMS_NAME = new PrivilegeResourceField("cms_name", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeResourceField RELEASE_TYPE = new PrivilegeResourceField("release_type", ObjectFieldDBType.INT, true, false);
    public static final PrivilegeResourceField RELEASE_TIME = new PrivilegeResourceField("release_time", ObjectFieldDBType.LONG, true, false);
    public static final PrivilegeResourceField REMOVE_STATUS = new PrivilegeResourceField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeResourceField MODIFY_USER = new PrivilegeResourceField("modify_user", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeResourceField MODIFY_TIME = new PrivilegeResourceField("modify_time", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public RefreshCMSTimingField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public RefreshCMSTimingField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}

