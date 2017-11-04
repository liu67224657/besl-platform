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
public class StatViewBounceField extends AbstractMongoObjectField{

    public static final StatViewBounceField VB_ID = new StatViewBounceField("_id", ObjectFieldDBType.LONG);
    public static final StatViewBounceField VB_ADVERTISEID = new StatViewBounceField("advertise_id", ObjectFieldDBType.STRING);
    public static final StatViewBounceField VB_SESSIONID = new StatViewBounceField("sessionid", ObjectFieldDBType.STRING);
    public static final StatViewBounceField VB_URL = new StatViewBounceField("url", ObjectFieldDBType.STRING);
    public static final StatViewBounceField VB_STAT_DATE = new StatViewBounceField("stat_date", ObjectFieldDBType.STRING);

    public StatViewBounceField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
