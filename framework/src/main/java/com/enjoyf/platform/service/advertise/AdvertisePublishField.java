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
public class AdvertisePublishField extends AbstractObjectField {
    //PUBLISHID, PUBLISHNAME, PUBLISHDESC, PROJECTID, AGENTID, VALIDSTATUS,
    //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
    public static final AdvertisePublishField PUBLISHID = new AdvertisePublishField("PUBLISHID", ObjectFieldDBType.STRING, false, true);

    public static final AdvertisePublishField PUBLISHNAME = new AdvertisePublishField("PUBLISHNAME", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishField PUBLISHDESC = new AdvertisePublishField("PUBLISHDESC", ObjectFieldDBType.STRING, true, false);

    public static final AdvertisePublishField PROJECTID = new AdvertisePublishField("PROJECTID", ObjectFieldDBType.STRING, false, true);
    public static final AdvertisePublishField AGENTID = new AdvertisePublishField("AGENTID", ObjectFieldDBType.STRING, false, true);

    public static final AdvertisePublishField REDIRECTURL = new AdvertisePublishField("REDIRECTURL", ObjectFieldDBType.STRING, false, true);

    public static final AdvertisePublishField VALIDSTATUS = new AdvertisePublishField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);

    public static final AdvertisePublishLocationField CREATEUSERID = new AdvertisePublishLocationField("CREATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField CREATEIP = new AdvertisePublishLocationField("CREATEIP", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField CREATEDATE = new AdvertisePublishLocationField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final AdvertisePublishField UPDATEUSERID = new AdvertisePublishField("UPDATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishField UPDATEIP = new AdvertisePublishField("UPDATEIP", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishField UPDATEDATE = new AdvertisePublishField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AdvertisePublishField STATENDDATE = new AdvertisePublishField("STATENDDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public AdvertisePublishField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AdvertisePublishField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
