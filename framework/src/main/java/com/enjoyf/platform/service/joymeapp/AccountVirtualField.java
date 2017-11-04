package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-20
 * Time: 下午2:18
 * To change this template use File | Settings | File Templates.
 */
public class AccountVirtualField extends AbstractObjectField {
    public static final AccountVirtualField VIRTUAL_ID = new AccountVirtualField("virtual_id", ObjectFieldDBType.LONG, true, false);
    public static final AccountVirtualField UNO = new AccountVirtualField("uno", ObjectFieldDBType.STRING, true, false);
     public static final AccountVirtualField SCREENNAME = new AccountVirtualField("screenname", ObjectFieldDBType.STRING, true, false);
    public static final AccountVirtualField CREATE_USER = new AccountVirtualField("create_user", ObjectFieldDBType.STRING, true, false);
    public static final AccountVirtualField CREATE_TIME = new AccountVirtualField("create_time", ObjectFieldDBType.DATE, true, false);
    public static final AccountVirtualField REMOVE_STATUS = new AccountVirtualField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final AccountVirtualField VIRTUAL_TYPE = new AccountVirtualField("virtual_type", ObjectFieldDBType.INT, true, false);
	public static final AccountVirtualField HEADICON = new AccountVirtualField("headicon", ObjectFieldDBType.STRING, true, false);

    public AccountVirtualField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AccountVirtualField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
