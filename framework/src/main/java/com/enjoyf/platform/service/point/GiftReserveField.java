package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class GiftReserveField extends AbstractObjectField {

    public static final GiftReserveField GIFT_RESERVE_ID = new GiftReserveField("gift_reserve_id", ObjectFieldDBType.LONG, true, true);
    public static final GiftReserveField UNO = new GiftReserveField("uno", ObjectFieldDBType.STRING, true, false);
    public static final GiftReserveField PROFILEID = new GiftReserveField("profileid", ObjectFieldDBType.STRING, true, false);
    public static final GiftReserveField AID = new GiftReserveField("aid", ObjectFieldDBType.LONG, true, false);
    public static final GiftReserveField GIFT_NAME = new GiftReserveField("gift_name", ObjectFieldDBType.STRING, true, false);
    public static final GiftReserveField VALID_STATUS = new GiftReserveField("valid_status", ObjectFieldDBType.STRING, true, false);
    public static final GiftReserveField CREATE_TIME = new GiftReserveField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GiftReserveField APPKEY = new GiftReserveField("appkey", ObjectFieldDBType.STRING, true, false);

    public GiftReserveField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GiftReserveField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
