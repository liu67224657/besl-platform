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
public class StatViewDepthField extends AbstractMongoObjectField{

    public static final StatViewDepthField VD_ID = new StatViewDepthField("_id", ObjectFieldDBType.LONG);
    public static final StatViewDepthField VD_ADVERTISEID = new StatViewDepthField("advertise_id", ObjectFieldDBType.STRING);
    public static final StatViewDepthField VD_SESSIONID = new StatViewDepthField("sessionid", ObjectFieldDBType.STRING);
    public static final StatViewDepthField VD_STAT_DATE = new StatViewDepthField("stat_date", ObjectFieldDBType.STRING);
    public static final StatViewDepthField VD_IP = new StatViewDepthField("ip", ObjectFieldDBType.STRING);
    public static final StatViewDepthField VD_COUNT = new StatViewDepthField("count", ObjectFieldDBType.INT);

    public StatViewDepthField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
