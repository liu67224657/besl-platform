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
public class AdvertiseUserPublishRelationField extends AbstractObjectField {
    //UNO, PUBLISHID, LOCATIONCODE, CREATEDATE, CREATEIP
    public static final AdvertiseUserPublishRelationField UNO = new AdvertiseUserPublishRelationField("UNO", ObjectFieldDBType.STRING, false, true);

    public static final AdvertiseUserPublishRelationField PUBLISHID = new AdvertiseUserPublishRelationField("PUBLISHID", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseUserPublishRelationField LOCATIONCODE = new AdvertiseUserPublishRelationField("LOCATIONCODE", ObjectFieldDBType.STRING, true, false);

    public static final AdvertiseUserPublishRelationField CREATEIP = new AdvertiseUserPublishRelationField("CREATEIP", ObjectFieldDBType.STRING, true, false);
    public static final AdvertiseUserPublishRelationField CREATEDATE = new AdvertiseUserPublishRelationField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public AdvertiseUserPublishRelationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AdvertiseUserPublishRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
