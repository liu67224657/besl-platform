package com.enjoyf.platform.service.ask.wiki;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class AdvertiseField extends AbstractObjectField {
    public static final AdvertiseField ID = new AdvertiseField("id", ObjectFieldDBType.LONG);
    public static final AdvertiseField APPKEY = new AdvertiseField("appkey", ObjectFieldDBType.STRING);
    public static final AdvertiseField TITLE = new AdvertiseField("title", ObjectFieldDBType.STRING);
    public static final AdvertiseField DESCRIPTION = new AdvertiseField("description", ObjectFieldDBType.STRING);
    public static final AdvertiseField DOMAIN = new AdvertiseField("domain", ObjectFieldDBType.INT);
    public static final AdvertiseField TARGET = new AdvertiseField("target", ObjectFieldDBType.STRING);
    public static final AdvertiseField PIC = new AdvertiseField("pic", ObjectFieldDBType.STRING);
    public static final AdvertiseField EXTEND = new AdvertiseField("extend", ObjectFieldDBType.STRING);
    public static final AdvertiseField REMOVE_STATUS = new AdvertiseField("remove_status", ObjectFieldDBType.STRING);
    public static final AdvertiseField DISPLAY_ORDER = new AdvertiseField("display_order", ObjectFieldDBType.LONG);
    public static final AdvertiseField CREATE_DATE = new AdvertiseField("create_date", ObjectFieldDBType.TIMESTAMP);
    public static final AdvertiseField PLATFORM = new AdvertiseField("platform", ObjectFieldDBType.INT);
    public static final AdvertiseField TYPE = new AdvertiseField("type", ObjectFieldDBType.INT);
    public static final AdvertiseField START_TIME = new AdvertiseField("start_time", ObjectFieldDBType.TIMESTAMP);
    public static final AdvertiseField END_TIME = new AdvertiseField("end_time", ObjectFieldDBType.TIMESTAMP);
    public AdvertiseField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}