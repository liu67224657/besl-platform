package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class WanbaItemField extends AbstractObjectField {
    public static final WanbaItemField ID = new WanbaItemField("item_id", ObjectFieldDBType.STRING);
    public static final WanbaItemField LINEKEY = new WanbaItemField("line_key", ObjectFieldDBType.STRING);
    public static final WanbaItemField OWNPROFILEID = new WanbaItemField("own_profile_id", ObjectFieldDBType.STRING);
    public static final WanbaItemField ITEMTYPE = new WanbaItemField("item_type", ObjectFieldDBType.INT);
    public static final WanbaItemField DESTID = new WanbaItemField("dest_id", ObjectFieldDBType.STRING);
    public static final WanbaItemField DESTPROFILEID = new WanbaItemField("dest_profile_id", ObjectFieldDBType.STRING);
    public static final WanbaItemField SCORE = new WanbaItemField("score", ObjectFieldDBType.DOUBLE);
    public static final WanbaItemField ITEMDOMAIN = new WanbaItemField("item_domain", ObjectFieldDBType.INT);
    public static final WanbaItemField CREATETIME = new WanbaItemField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final WanbaItemField VALIDSTATUS = new WanbaItemField("valid_status", ObjectFieldDBType.STRING);


    public WanbaItemField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}