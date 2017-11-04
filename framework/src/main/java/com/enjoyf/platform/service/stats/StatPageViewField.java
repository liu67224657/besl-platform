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
public class StatPageViewField extends AbstractMongoObjectField{

    public static final StatPageViewField PV_ID = new StatPageViewField("_id", ObjectFieldDBType.LONG);
    public static final StatPageViewField PV_ADVERTISEID = new StatPageViewField("advertise_id", ObjectFieldDBType.STRING);
    public static final StatPageViewField PV_SESSIONID = new StatPageViewField("sessionid", ObjectFieldDBType.STRING);
    public static final StatPageViewField PV_URL = new StatPageViewField("url", ObjectFieldDBType.STRING);
    public static final StatPageViewField PV_STAT_DATE = new StatPageViewField("stat_date", ObjectFieldDBType.STRING);

    public StatPageViewField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
