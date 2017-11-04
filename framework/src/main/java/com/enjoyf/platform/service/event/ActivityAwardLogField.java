/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description: the class is used to update or query.
 */
public class ActivityAwardLogField extends AbstractObjectField {
    //
    public static final ActivityAwardLogField ACTIVITY_ID = new ActivityAwardLogField("activityid", ObjectFieldDBType.LONG, false, true);
    public static final ActivityAwardLogField UNO = new ActivityAwardLogField("uno", ObjectFieldDBType.STRING, false, true);
    public static final ActivityAwardLogField AWARD_DATE = new ActivityAwardLogField("award_date", ObjectFieldDBType.DATE, false, true);



    public ActivityAwardLogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ActivityAwardLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
