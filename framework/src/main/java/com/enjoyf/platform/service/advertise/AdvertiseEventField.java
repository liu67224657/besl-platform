/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.advertise;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description: the class is used to update or query.
 */
public class AdvertiseEventField extends AbstractObjectField {
    //EVENTID, PUBLISHID, LOCATIONCODE, EVENTTYPE,
    //UNO, SESSIONID, GLOBALID, EVENTCOUNT, EVENTDESC, EVENTDATE, EVENTIP
    public static final AdvertiseEventField EVENTID = new AdvertiseEventField("EVENTID", ObjectFieldDBType.LONG, false, true);

    public static final AdvertiseEventField PUBLISHID = new AdvertiseEventField("PUBLISHID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseEventField LOCATIONCODE = new AdvertiseEventField("LOCATIONCODE", ObjectFieldDBType.STRING, true, false);

    public static final AdvertiseEventField EVENTTYPE = new AdvertiseEventField("EVENTTYPE", ObjectFieldDBType.STRING, true, false);

    public static final AdvertisePublishLocationField UNO = new AdvertisePublishLocationField("UNO", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField SESSIONID = new AdvertisePublishLocationField("SESSIONID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField GLOBALID = new AdvertisePublishLocationField("GLOBALID", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final AdvertiseEventField EVENTCOUNT = new AdvertiseEventField("EVENTCOUNT", ObjectFieldDBType.LONG, false, true);
    public static final AdvertiseEventField EVENTDESC = new AdvertiseEventField("EVENTDESC", ObjectFieldDBType.STRING, true, false);

    public static final AdvertiseEventField EVENTIP = new AdvertiseEventField("EVENTIP", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseEventField EVENTDATE = new AdvertiseEventField("EVENTDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public AdvertiseEventField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AdvertiseEventField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
