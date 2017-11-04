package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import com.enjoyf.platform.util.sql.mongodb.AbstractMongoObjectField;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-3-21
 * Time: 上午10:56
 * To change this template use File | Settings | File Templates.
 */
public class StatUserViewField extends AbstractMongoObjectField{

    public static final StatUserViewField UV_ID = new StatUserViewField("_id", ObjectFieldDBType.LONG);
    public static final StatUserViewField UV_ADVERTISEID = new StatUserViewField("advertise_id", ObjectFieldDBType.STRING);
    public static final StatUserViewField UV_SESSIONID = new StatUserViewField("sessionid", ObjectFieldDBType.STRING);
    public static final StatUserViewField UV_URL = new StatUserViewField("url", ObjectFieldDBType.STRING);
    public static final StatUserViewField UV_STAT_DATE = new StatUserViewField("stat_date", ObjectFieldDBType.STRING);
    public static final StatUserViewField UV_IP = new StatUserViewField("ip", ObjectFieldDBType.STRING);

    public StatUserViewField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
