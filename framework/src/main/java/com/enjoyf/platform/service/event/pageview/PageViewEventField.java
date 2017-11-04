/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.pageview;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description: the class is used to update or query.
 */
public class PageViewEventField extends AbstractObjectField {
    //
    public static final PageViewEventField LOCATIONURL = new PageViewEventField("LOCATIONURL", ObjectFieldDBType.STRING, true, false);
    public static final PageViewEventField GLOBALID = new PageViewEventField("GLOBALID", ObjectFieldDBType.STRING, true, false);
    public static final PageViewEventField REFER = new PageViewEventField("REFER", ObjectFieldDBType.STRING, true, false);

    public static final PageViewEventField EVENTDATE = new PageViewEventField("EVENTDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public PageViewEventField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PageViewEventField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
