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
public class ViewLineField extends AbstractObjectField {
    //
    public static final ViewLineField LINEID = new ViewLineField("LINEID", ObjectFieldDBType.INT, false, true);

    public static final ViewLineField CATEGORYID = new ViewLineField("CATEGORYID", ObjectFieldDBType.INT, false, true);

    public static final ViewLineField CATEGORYASPECT = new ViewLineField("CATEGORYASPECT", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineField LINENAME = new ViewLineField("LINENAME", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineField LINEDESC = new ViewLineField("LINEDESC", ObjectFieldDBType.STRING, true, false);

    public static final ViewLineField SEOKEYWORD = new ViewLineField("SEOKEYWORD", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineField SEODESC = new ViewLineField("SEODESC", ObjectFieldDBType.STRING, true, false);

    public static final ViewLineField LOCATIONCODE = new ViewLineField("LOCATIONCODE", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineField ITEMTYPE = new ViewLineField("ITEMTYPE", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineField ITEMMINCOUNT = new ViewLineField("ITEMMINCOUNT", ObjectFieldDBType.INT, true, false);
    public static final ViewLineField DISPLAYORDER = new ViewLineField("DISPLAYORDER", ObjectFieldDBType.INT, true, false);

    public static final ViewLineField AUTOFILLTYPE = new ViewLineField("AUTOFILLTYPE", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineField AUTOFILLRULE = new ViewLineField("AUTOFILLRULE", ObjectFieldDBType.STRING, true, false);


    public static final ViewLineField CREATEDATE = new ViewLineField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ViewLineField CREATEUSERID = new ViewLineField("CREATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineField UPDATEDATE = new ViewLineField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ViewLineField UPDATEUSERID = new ViewLineField("UPDATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineField VALIDSTATUS = new ViewLineField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);

    //
    public ViewLineField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ViewLineField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
