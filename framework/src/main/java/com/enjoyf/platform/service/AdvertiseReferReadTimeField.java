package com.enjoyf.platform.service;

import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import com.enjoyf.platform.util.sql.mongodb.AbstractMongoObjectField;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-3-21
 * Time: 上午10:56
 * To change this template use File | Settings | File Templates.
 */
public class AdvertiseReferReadTimeField extends AbstractMongoObjectField{

    public static final AdvertiseReferReadTimeField REFER_ID = new AdvertiseReferReadTimeField("_id", ObjectFieldDBType.LONG);
    public static final AdvertiseReferReadTimeField REFER_ADVERTISEID = new AdvertiseReferReadTimeField("advertise_id", ObjectFieldDBType.STRING);
    public static final AdvertiseReferReadTimeField REFER_SESSIONID = new AdvertiseReferReadTimeField("sessionid", ObjectFieldDBType.STRING);
    public static final AdvertiseReferReadTimeField REFER_URL = new AdvertiseReferReadTimeField("url", ObjectFieldDBType.STRING);
    public static final AdvertiseReferReadTimeField REFER_STAT_DATE = new AdvertiseReferReadTimeField("stat_date", ObjectFieldDBType.STRING);

    public AdvertiseReferReadTimeField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
