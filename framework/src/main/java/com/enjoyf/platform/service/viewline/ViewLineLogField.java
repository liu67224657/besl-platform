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
public class ViewLineLogField extends AbstractObjectField {
    //
    public static final ViewLineLogField LOGID = new ViewLineLogField("LOGID", ObjectFieldDBType.LONG, false, true);
    public static final ViewLineLogField SRCID = new ViewLineLogField("SRCID", ObjectFieldDBType.INT, false, false);
    public static final ViewLineLogField SUBDOMAIN = new ViewLineLogField("SUBDOMAIN", ObjectFieldDBType.STRING, false, false);
    public static final ViewLineLogField LOGNAME = new ViewLineLogField("LOGNAME", ObjectFieldDBType.STRING, false, false);
    public static final ViewLineLogField LOGCONTENT = new ViewLineLogField("LOGCONTENT", ObjectFieldDBType.STRING, false, false);
    public static final ViewLineLogField CREATEDATE = new ViewLineLogField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, false, false);
    public static final ViewLineLogField CREATEUNO = new ViewLineLogField("CREATEUNO", ObjectFieldDBType.STRING, false, false);
    public static final ViewLineLogField CREATEIP = new ViewLineLogField("CREATEIP", ObjectFieldDBType.STRING, false, false);
    public static final ViewLineLogField CREATEUSERID = new ViewLineLogField("CREATEUSERID", ObjectFieldDBType.STRING, false, false);


    //
    public ViewLineLogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ViewLineLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
