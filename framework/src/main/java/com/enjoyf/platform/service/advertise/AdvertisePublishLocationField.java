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
public class AdvertisePublishLocationField extends AbstractObjectField {
    //PLID, LOCATIONCODE, LOCATIONNAME, LOCATIONDESC, PUBLISHID, VALIDSTATUS,
    //CREATEUSERID, CREATEDATE, CREATEIP
    public static final AdvertisePublishLocationField PLID = new AdvertisePublishLocationField("PLID", ObjectFieldDBType.STRING, false, true);

    public static final AdvertisePublishLocationField LOCATIONCODE = new AdvertisePublishLocationField("LOCATIONCODE", ObjectFieldDBType.STRING, true, false);

    public static final AdvertisePublishLocationField LOCATIONNAME = new AdvertisePublishLocationField("LOCATIONNAME", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField LOCATIONDESC = new AdvertisePublishLocationField("LOCATIONDESC", ObjectFieldDBType.STRING, false, true);

    public static final AdvertisePublishLocationField PUBLISHID = new AdvertisePublishLocationField("PUBLISHID", ObjectFieldDBType.STRING, false, true);

    public static final AdvertisePublishLocationField VALIDSTATUS = new AdvertisePublishLocationField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);

    public static final AdvertisePublishLocationField CREATEUSERID = new AdvertisePublishLocationField("CREATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField CREATEIP = new AdvertisePublishLocationField("CREATEIP", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField CREATEDATE = new AdvertisePublishLocationField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public AdvertisePublishLocationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AdvertisePublishLocationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
