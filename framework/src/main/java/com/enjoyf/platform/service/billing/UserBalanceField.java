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
public class UserBalanceField extends AbstractObjectField {
    public static final UserBalanceField BALANCE_ID = new UserBalanceField("balance_id", ObjectFieldDBType.STRING);
    public static final UserBalanceField AMOUNT = new UserBalanceField("amount", ObjectFieldDBType.INT);

    public UserBalanceField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public UserBalanceField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
