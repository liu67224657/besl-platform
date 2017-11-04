package com.enjoyf.platform.service.joymeapp.gameclient;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli
 * Date: 2015/01/07
 * Time: 18:39
 */
public class TagDedearchiveCheatFiled extends AbstractObjectField {

    public static final TagDedearchiveCheatFiled DEDE_ARCHIVES_ID = new TagDedearchiveCheatFiled("dede_archives_id", ObjectFieldDBType.LONG, true, false);
    public static final TagDedearchiveCheatFiled READ_NUM = new TagDedearchiveCheatFiled("read_num", ObjectFieldDBType.INT, true, false);
    public static final TagDedearchiveCheatFiled AGREE_NUM = new TagDedearchiveCheatFiled("agree_num", ObjectFieldDBType.INT, true, false);

    public static final TagDedearchiveCheatFiled AGREE_PERCENT = new TagDedearchiveCheatFiled("agree_percent", ObjectFieldDBType.DOUBLE, true, false);
    public static final TagDedearchiveCheatFiled CHEATING_TIME = new TagDedearchiveCheatFiled("cheating_time", ObjectFieldDBType.DATE, true, false);

    public static final TagDedearchiveCheatFiled REAL_READ_NUM = new TagDedearchiveCheatFiled("real_read_num", ObjectFieldDBType.INT, true, false);
    public static final TagDedearchiveCheatFiled REAL_AGREE_NUM = new TagDedearchiveCheatFiled("real_agree_num", ObjectFieldDBType.INT, true, false);
    public static final TagDedearchiveCheatFiled ARCHIVE_TYPE = new TagDedearchiveCheatFiled("archive_type", ObjectFieldDBType.INT, true, false);


    public TagDedearchiveCheatFiled(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public TagDedearchiveCheatFiled(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
