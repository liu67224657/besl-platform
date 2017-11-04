package com.enjoyf.platform.service.billing;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-13
 * Time: 下午12:36
 * To change this template use File | Settings | File Templates.
 */
public class DepositLogField extends AbstractObjectField {
    public static final DepositLogField PLATFORM = new DepositLogField("platform", ObjectFieldDBType.INT);
    public static final DepositLogField APPCHANNEL = new DepositLogField("appchannel", ObjectFieldDBType.STRING);
    public static final DepositLogField ZONEKEY = new DepositLogField("zonekey", ObjectFieldDBType.STRING);
    public static final DepositLogField APPKEY = new DepositLogField("appkey", ObjectFieldDBType.STRING);
    public static final DepositLogField LOGID = new DepositLogField("deposit_log_id", ObjectFieldDBType.STRING);
    public static final DepositLogField SYNC_STATUS = new DepositLogField("syncstatus", ObjectFieldDBType.INT);
    public static final DepositLogField STATUS = new DepositLogField("status", ObjectFieldDBType.INT);
    public static final DepositLogField ERRORMSG = new DepositLogField("errormsg", ObjectFieldDBType.STRING);
    public static final DepositLogField DEPOSITTIME = new DepositLogField("deposittime", ObjectFieldDBType.TIMESTAMP);
    public static final DepositLogField CHANNEL = new DepositLogField("third_channel", ObjectFieldDBType.STRING);
    public static final DepositLogField SYNCTIMES = new DepositLogField("synctimes", ObjectFieldDBType.INT);

    public static final DepositLogField ORDERID = new DepositLogField("oriderid", ObjectFieldDBType.STRING);
    public static final DepositLogField THIRDORDERID = new DepositLogField("thirdorderid", ObjectFieldDBType.STRING);

    public DepositLogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public DepositLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
