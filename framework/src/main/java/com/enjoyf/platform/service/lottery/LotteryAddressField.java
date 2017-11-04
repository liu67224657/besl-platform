/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 11-12-23 上午14:56
 * Description:
 */
public class LotteryAddressField extends AbstractObjectField {

    //
    public static final LotteryAddressField PROFILEID = new LotteryAddressField("profileid", ObjectFieldDBType.STRING, false, true);
    public static final LotteryAddressField ADDRESS = new LotteryAddressField("address", ObjectFieldDBType.STRING, true, false);
    public static final LotteryAddressField UPDATETIME = new LotteryAddressField("updatetime", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final LotteryAddressField UPDATEIP = new LotteryAddressField("updateip", ObjectFieldDBType.STRING, true, false);

    //
    public LotteryAddressField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public LotteryAddressField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
