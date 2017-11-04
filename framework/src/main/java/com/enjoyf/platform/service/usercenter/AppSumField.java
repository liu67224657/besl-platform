package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/9
 * Description:
 */
public class AppSumField extends AbstractObjectField {
    //
    public static final AppSumField APP_SUM_ID = new AppSumField("app_sum_id", ObjectFieldDBType.STRING);
    public static final AppSumField APPKEY = new AppSumField("appkey", ObjectFieldDBType.STRING);
    public static final AppSumField SUBKEY = new AppSumField("subkey", ObjectFieldDBType.STRING);
    public static final AppSumField ACTIVITY_USERSUM = new AppSumField("activity_usersum", ObjectFieldDBType.INT);
    public static final AppSumField ACTIVITY_LOGSUM = new AppSumField("activity_logsum", ObjectFieldDBType.INT);

    //
    public AppSumField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
