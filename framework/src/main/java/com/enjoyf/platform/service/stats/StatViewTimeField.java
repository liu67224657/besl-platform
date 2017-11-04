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
public class StatViewTimeField extends AbstractMongoObjectField{

    public static final StatViewTimeField VT_ID = new StatViewTimeField("_id", ObjectFieldDBType.LONG);
    public static final StatViewTimeField VT_ADVERTISEID = new StatViewTimeField("advertise_id", ObjectFieldDBType.STRING);
    public static final StatViewTimeField VT_SESSIONID = new StatViewTimeField("sessionid", ObjectFieldDBType.STRING);
    public static final StatViewTimeField VT_URL = new StatViewTimeField("url", ObjectFieldDBType.STRING);
    public static final StatViewTimeField VT_STAT_DATE = new StatViewTimeField("stat_date", ObjectFieldDBType.STRING);

    public StatViewTimeField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
