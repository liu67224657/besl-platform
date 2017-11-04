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
public class ActivityField extends AbstractObjectField {
    //
    public static final ActivityField ACTIVITY_ID = new ActivityField("activityid", ObjectFieldDBType.LONG, false, true);
    public static final ActivityField RESTAMOUNT = new ActivityField("award_restamount", ObjectFieldDBType.INT, false, true);



    public ActivityField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ActivityField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
