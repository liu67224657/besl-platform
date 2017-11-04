package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-4-6
 * Time: 上午11:12
 * To change this template use File | Settings | File Templates.
 */
public class IndexCacheField extends AbstractObjectField {
    public static final JoymeOperateField INDEX_CACHE_ID = new JoymeOperateField("index_cache_id", ObjectFieldDBType.LONG, false, true);
    public static final JoymeOperateField SERVER_ID = new JoymeOperateField("server_id", ObjectFieldDBType.STRING, false, true);
    public static final JoymeOperateField HTML = new JoymeOperateField("html", ObjectFieldDBType.STRING, false, true);
    public static final JoymeOperateField VERSION = new JoymeOperateField("version", ObjectFieldDBType.STRING, false, true);
    public static final JoymeOperateField REMOVER_STATUS = new JoymeOperateField("remove_status", ObjectFieldDBType.STRING, false, true);
    public static final JoymeOperateField TYPE = new JoymeOperateField("type", ObjectFieldDBType.INT, false, true);

    //Constructors
    public IndexCacheField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public IndexCacheField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

}
