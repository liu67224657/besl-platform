package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public class UserConsumeLogField extends AbstractObjectField {
    public static final UserConsumeLogField USER_CONSUME_LOG_ID = new UserConsumeLogField("user_consume_log_id", ObjectFieldDBType.LONG, true, true);
    public static final UserConsumeLogField UNO = new UserConsumeLogField("user_no", ObjectFieldDBType.STRING, true, false);

    public static final UserConsumeLogField GOODS_ID = new UserConsumeLogField("goods_id", ObjectFieldDBType.LONG, true, false);
    public static final UserConsumeLogField GOODS_NAME = new UserConsumeLogField("goods_name", ObjectFieldDBType.STRING, true, false);
    public static final UserConsumeLogField GOODS_PIC = new UserConsumeLogField("goods_pic", ObjectFieldDBType.STRING, true, false);
    public static final UserConsumeLogField GOODS_DESC = new UserConsumeLogField("goods_desc", ObjectFieldDBType.STRING, true, false);
    public static final UserConsumeLogField GOODS_ITEM_ID = new UserConsumeLogField("goods_item_id", ObjectFieldDBType.LONG, true, false);
    public static final UserConsumeLogField GOODS_TYPE = new UserConsumeLogField("goods_type", ObjectFieldDBType.INT, true, false);

    public static final UserConsumeLogField CONSUME_AMOUNT = new UserConsumeLogField("consume_amount", ObjectFieldDBType.INT, true, false);
    public static final UserConsumeLogField CONSUME_TYPE = new UserConsumeLogField("consume_type", ObjectFieldDBType.STRING, true, false);

    public static final UserConsumeLogField CONSUME_DATE = new UserConsumeLogField("consume_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final UserConsumeLogField CONSUME_TIME = new UserConsumeLogField("consume_time", ObjectFieldDBType.DATE, true, false);
    public static final UserConsumeLogField CONSUME_IP = new UserConsumeLogField("consume_ip", ObjectFieldDBType.STRING, true, false);

    public static final UserConsumeLogField PROFILEID = new UserConsumeLogField("profileid", ObjectFieldDBType.STRING, true, false);
    public static final UserConsumeLogField APPKEY = new UserConsumeLogField("appkey", ObjectFieldDBType.STRING, true, false);
    public static final UserConsumeLogField GOODS_ACTION_TYPE = new UserConsumeLogField("goods_action_type", ObjectFieldDBType.INT, true, false);
    public static final UserConsumeLogField VALID_STATUS = new UserConsumeLogField("valid_status", ObjectFieldDBType.STRING, true, false);
    public static final UserConsumeLogField CONSUMEORDER = new UserConsumeLogField("consume_order", ObjectFieldDBType.LONG, true, false);


    public UserConsumeLogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public UserConsumeLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
