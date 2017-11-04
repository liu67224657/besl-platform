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
public class AdvertiseAppUrlField extends AbstractObjectField {

    public static final AdvertiseAppUrlField CLIENTURLID = new AdvertiseAppUrlField("CLIENTURLID", ObjectFieldDBType.LONG, false, true);

    public static final AdvertiseAppUrlField CODE = new AdvertiseAppUrlField("CODE", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseAppUrlField CODE_NAME = new AdvertiseAppUrlField("CODE_NAME", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseAppUrlField IOSURL = new AdvertiseAppUrlField("IOSURL", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseAppUrlField ANDROIDURL = new AdvertiseAppUrlField("ANDROIDURL", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseAppUrlField DEFAULTURL = new AdvertiseAppUrlField("DEFAULTURL", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseAppUrlField REMOVESTATUS = new AdvertiseAppUrlField("REMOVESTATUS", ObjectFieldDBType.STRING, true, false);

    public static final AdvertisePublishLocationField CREATEIP = new AdvertisePublishLocationField("CREATEIP", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField CREATEID = new AdvertisePublishLocationField("CREATEID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertisePublishLocationField CREATETIME = new AdvertisePublishLocationField("CREATETIME", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final AdvertiseAppUrlField LASTMODIFYIP = new AdvertiseAppUrlField("LASTMODIFYIP", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseAppUrlField LASTMODIFYID = new AdvertiseAppUrlField("LASTMODIFYID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseAppUrlField LASTMODIFYTIME = new AdvertiseAppUrlField("LASTMODIFYTIME", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public AdvertiseAppUrlField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AdvertiseAppUrlField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
