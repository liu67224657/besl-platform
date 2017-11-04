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
public class AdvertiseAgentField extends AbstractObjectField {
    //AGENTID, AGENTNAME, AGENTDESC, VALIDSTATUS,
    //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
    public static final AdvertiseAgentField AGENTID = new AdvertiseAgentField("AGENTID", ObjectFieldDBType.STRING, false, true);

    public static final AdvertiseAgentField AGENTNAME = new AdvertiseAgentField("AGENTNAME", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseAgentField AGENTDESC = new AdvertiseAgentField("AGENTDESC", ObjectFieldDBType.STRING, true, false);

    public static final AdvertiseAgentField VALIDSTATUS = new AdvertiseAgentField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);

    public static final AdvertisePublishLocationField CREATEUSERID = new AdvertisePublishLocationField("CREATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField CREATEIP = new AdvertisePublishLocationField("CREATEIP", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField CREATEDATE = new AdvertisePublishLocationField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final AdvertiseAgentField UPDATEUSERID = new AdvertiseAgentField("UPDATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseAgentField UPDATEIP = new AdvertiseAgentField("UPDATEIP", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseAgentField UPDATEDATE = new AdvertiseAgentField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public AdvertiseAgentField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AdvertiseAgentField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
