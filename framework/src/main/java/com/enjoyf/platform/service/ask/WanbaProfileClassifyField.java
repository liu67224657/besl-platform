package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by zhimingli on 2016/11/15 0015.
 */
public class WanbaProfileClassifyField  extends AbstractObjectField {
    public static final WanbaProfileClassifyField CLASSIFY_ID = new WanbaProfileClassifyField("classify_id", ObjectFieldDBType.STRING);
    public static final WanbaProfileClassifyField PROFILEID = new WanbaProfileClassifyField("profileid", ObjectFieldDBType.STRING);
    public static final WanbaProfileClassifyField CREATE_TIME = new WanbaProfileClassifyField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final WanbaProfileClassifyField CLASSIFY_TYPE = new WanbaProfileClassifyField("classify_type", ObjectFieldDBType.INT);
    public static final WanbaProfileClassifyField REMOVE_STATUS = new WanbaProfileClassifyField("remove_status", ObjectFieldDBType.INT);
    public static final WanbaProfileClassifyField EXT = new WanbaProfileClassifyField("ext", ObjectFieldDBType.STRING);



    public WanbaProfileClassifyField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
