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
public class AdvertiseProjectField extends AbstractObjectField {
    //PROJECTID, PROJECTNAME, PROJECTDESC, STARTDATE, ENDDATE, STATSTARTDATE, STATENDDATE, VALIDSTATUS,
    //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
    public static final AdvertiseProjectField PROJECTID = new AdvertiseProjectField("PROJECTID", ObjectFieldDBType.STRING, false, true);

    public static final AdvertiseProjectField PROJECTNAME = new AdvertiseProjectField("PROJECTNAME", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseProjectField PROJECTDESC = new AdvertiseProjectField("PROJECTDESC", ObjectFieldDBType.STRING, true, false);

    public static final AdvertiseProjectField STARTDATE = new AdvertiseProjectField("STARTDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AdvertiseProjectField ENDDATE = new AdvertiseProjectField("ENDDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AdvertiseProjectField STATSTARTDATE = new AdvertiseProjectField("STATSTARTDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AdvertiseProjectField STATENDDATE = new AdvertiseProjectField("STATENDDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final AdvertiseProjectField VALIDSTATUS = new AdvertiseProjectField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);

    public static final AdvertisePublishLocationField CREATEUSERID = new AdvertisePublishLocationField("CREATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField CREATEIP = new AdvertisePublishLocationField("CREATEIP", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField CREATEDATE = new AdvertisePublishLocationField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final AdvertiseProjectField UPDATEUSERID = new AdvertiseProjectField("UPDATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseProjectField UPDATEIP = new AdvertiseProjectField("UPDATEIP", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseProjectField UPDATEDATE = new AdvertiseProjectField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public AdvertiseProjectField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AdvertiseProjectField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
