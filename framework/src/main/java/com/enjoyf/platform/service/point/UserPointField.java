package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 上午11:41
 * To change this template use File | Settings | File Templates.
 */
public class UserPointField extends AbstractObjectField {

    public static final UserPointField USERPOINTID = new UserPointField("user_point_id", ObjectFieldDBType.LONG, true, true);
    public static final UserPointField USERNO = new UserPointField("user_no", ObjectFieldDBType.STRING, true, false);
    public static final UserPointField USERPOINT = new UserPointField("user_point", ObjectFieldDBType.INT, true, false);
    public static final UserPointField CONSUME_AMOUNT = new UserPointField("consume_amount", ObjectFieldDBType.INT, true, false);
    public static final UserPointField CONSUME_EXCHANGE = new UserPointField("consume_exchange", ObjectFieldDBType.INT, true, false);
    public static final UserPointField EXT_INT1 = new UserPointField("extint1", ObjectFieldDBType.INT, true, false);
    public static final UserPointField EXT_INT2 = new UserPointField("extint2", ObjectFieldDBType.INT, true, false);
    public static final UserPointField PROFILEID = new UserPointField("profileid", ObjectFieldDBType.STRING, true, false);
    public static final UserPointField POINTKEY = new UserPointField("pointkey", ObjectFieldDBType.STRING, true, false);
    public static final UserPointField PRESTIGE = new UserPointField("prestige", ObjectFieldDBType.INT, true, false);

    public UserPointField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public UserPointField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
