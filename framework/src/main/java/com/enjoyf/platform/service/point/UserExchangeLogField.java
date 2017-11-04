package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public class UserExchangeLogField extends AbstractObjectField {
    public static final UserExchangeLogField USER_EXCHANGE_LOG_ID = new UserExchangeLogField("user_exchange_log_id", ObjectFieldDBType.LONG, true, true);
    public static final UserExchangeLogField USER_NO = new UserExchangeLogField("user_no", ObjectFieldDBType.STRING, false, false);

    public static final UserExchangeLogField EXCHANGE_GOODS_ID = new UserExchangeLogField("exchange_goods_id", ObjectFieldDBType.LONG, false, false);
    public static final UserExchangeLogField EXCHANGE_GOODS_NAME = new UserExchangeLogField("exchange_goods_name", ObjectFieldDBType.STRING, false, false);
    public static final UserExchangeLogField EXCHANGE_GOODS_PIC = new UserExchangeLogField("exchange_goods_pic", ObjectFieldDBType.STRING, false, false);
    public static final UserExchangeLogField EXCHANGE_GOODS_DESC = new UserExchangeLogField("exchange_goods_desc", ObjectFieldDBType.STRING, false, false);
    public static final UserExchangeLogField EXCHANGE_GOODS_TYPE = new UserExchangeLogField("exchange_goods_type", ObjectFieldDBType.INT, false, false);

    public static final UserExchangeLogField GOODS_ITEM_ID = new UserExchangeLogField("goods_item_id", ObjectFieldDBType.LONG, false, false);
    public static final UserExchangeLogField SN_NAME1 = new UserExchangeLogField("sn_name1", ObjectFieldDBType.STRING, false, false);
    public static final UserExchangeLogField SN_VALUE1 = new UserExchangeLogField("sn_value1", ObjectFieldDBType.STRING, false, false);
    public static final UserExchangeLogField SN_NAME2 = new UserExchangeLogField("sn_name2", ObjectFieldDBType.STRING, false, false);
    public static final UserExchangeLogField SN_VALUE2 = new UserExchangeLogField("sn_value2", ObjectFieldDBType.STRING, false, false);

    public static final UserExchangeLogField EXCHANGE_TYPE = new UserExchangeLogField("exchange_type", ObjectFieldDBType.STRING, false, false);

    public static final UserExchangeLogField EXCHANGE_DATE = new UserExchangeLogField("exchange_date", ObjectFieldDBType.DATE, true, false);
    public static final UserExchangeLogField EXCHANGE_TIME = new UserExchangeLogField("exchange_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final UserExchangeLogField EXCHANGE_IP = new UserExchangeLogField("exchange_ip", ObjectFieldDBType.STRING, true, false);
    public static final UserExchangeLogField USER_EXCHANGE_DOMAIN = new UserExchangeLogField("exchange_domain", ObjectFieldDBType.STRING, true, false);

    public static final UserExchangeLogField SMS_COUNT = new UserExchangeLogField("sms_count", ObjectFieldDBType.INT, true, false);

    public static final UserExchangeLogField PROFILEID = new UserExchangeLogField("profileid", ObjectFieldDBType.STRING, true, false);
    public static final UserExchangeLogField EXCHANGE_POINT = new UserExchangeLogField("exchange_point", ObjectFieldDBType.INT, true, false);
    public static final UserExchangeLogField APPKEY = new UserExchangeLogField("appkey", ObjectFieldDBType.STRING, true, false);


    public UserExchangeLogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public UserExchangeLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
