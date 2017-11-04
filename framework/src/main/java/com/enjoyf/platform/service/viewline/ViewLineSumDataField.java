/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto:taijunli@staff.joyme.com>Li TaiJun</a>
 * Create time: 12-2-9 下午17:10
 * Description: the class is used to update or query.
 */
public class ViewLineSumDataField extends AbstractObjectField {
    //SRCID, SUMDOMAIN, SUMTYPECODE, SUMNUM01, SUMNUM02, SUMNUM03, SUMNUM04, SUMNUM05, SUMNUM06, SUMNUM07, SUMNUM08
    public static final ViewLineSumDataField SRCID = new ViewLineSumDataField("SRCID", ObjectFieldDBType.INT, false, true);

    public static final ViewLineSumDataField SUMDOMAIN = new ViewLineSumDataField("SUMDOMAIN", ObjectFieldDBType.STRING, false, true);
    public static final ViewLineSumDataField SUMTYPECODE = new ViewLineSumDataField("SUMTYPECODE", ObjectFieldDBType.STRING, true, false);

    public static final ViewLineSumDataField VIEWTIMES = new ViewLineSumDataField("SUMNUM01", ObjectFieldDBType.INT, true, false);
    public static final ViewLineSumDataField POSTTIMES = new ViewLineSumDataField("SUMNUM02", ObjectFieldDBType.INT, true, false);
    public static final ViewLineSumDataField REPLYTIMES = new ViewLineSumDataField("SUMNUM03", ObjectFieldDBType.INT, true, false);
    public static final ViewLineSumDataField FAVORTIMES = new ViewLineSumDataField("SUMNUM04", ObjectFieldDBType.INT, true, false);
    public static final ViewLineSumDataField FORWARDTIMES = new ViewLineSumDataField("SUMNUM05", ObjectFieldDBType.INT, true, false);

    //
    public ViewLineSumDataField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ViewLineSumDataField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
