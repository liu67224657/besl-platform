package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
public class CityField extends AbstractObjectField {

    public static final CityField CITY_ID = new CityField("city_id", ObjectFieldDBType.LONG, true, true);
    public static final CityField CITY_NAME = new CityField("city_name", ObjectFieldDBType.STRING, true, false);
    public static final CityField IS_PRESET = new CityField("is_preset", ObjectFieldDBType.BOOLEAN, true, false);
    public static final CityField CREATE_DATE = new CityField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final CityField CREATE_IP = new CityField("create_ip", ObjectFieldDBType.STRING, true, false);
    public static final CityField CREATE_USERID = new CityField("create_userid", ObjectFieldDBType.STRING, true, false);
    public static final CityField LAST_MODIFY_DATE = new CityField("last_modify_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final CityField LAST_MODIFY_IP = new CityField("last_modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final CityField LAST_MODIFY_USERID = new CityField("last_modify_userid", ObjectFieldDBType.STRING, true, false);
    public static final CityField VALIDSTATUS = new CityField("validstatus", ObjectFieldDBType.STRING, true, false);

    public CityField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public CityField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
