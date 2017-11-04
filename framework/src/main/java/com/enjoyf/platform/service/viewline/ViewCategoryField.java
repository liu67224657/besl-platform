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
public class ViewCategoryField extends AbstractObjectField {
    //
    public static final ViewCategoryField CATEGORYID = new ViewCategoryField("CATEGORYID", ObjectFieldDBType.INT, false, true);

    public static final ViewCategoryField CATEGORYCODE = new ViewCategoryField("CATEGORYCODE", ObjectFieldDBType.STRING, false, true);
    public static final ViewCategoryField LOCATIONCODE = new ViewCategoryField("LOCATIONCODE", ObjectFieldDBType.STRING, true, false);
    public static final ViewCategoryField CATEGORYDOMAIN = new ViewCategoryField("CATEGORYDOMAIN", ObjectFieldDBType.STRING, false, true);
    public static final ViewCategoryField CATEGORYASPECT = new ViewCategoryField("CATEGORYASPECT", ObjectFieldDBType.STRING, false, true);

    public static final ViewCategoryField CATEGORYNAME = new ViewCategoryField("CATEGORYNAME", ObjectFieldDBType.STRING, true, false);
    public static final ViewCategoryField CATEGORYDESC = new ViewCategoryField("CATEGORYDESC", ObjectFieldDBType.STRING, true, false);

    public static final ViewCategoryField SEOKEYWORD = new ViewCategoryField("SEOKEYWORD", ObjectFieldDBType.STRING, true, false);
    public static final ViewCategoryField SEODESC = new ViewCategoryField("SEODESC", ObjectFieldDBType.STRING, true, false);

    public static final ViewCategoryField PARENTCATEGORYID = new ViewCategoryField("PARENTCATEGORYID", ObjectFieldDBType.INT, true, false);

    public static final ViewCategoryField DISPLAYORDER = new ViewCategoryField("DISPLAYORDER", ObjectFieldDBType.INT, true, false);
    public static final ViewCategoryField DISPLAYSETTING = new ViewCategoryField("DISPLAYSETTING", ObjectFieldDBType.STRING, true, false);

    public static final ViewCategoryField VALIDSTATUS = new ViewCategoryField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final ViewCategoryField PUBLISHSTATUS = new ViewCategoryField("PUBLISHSTATUS", ObjectFieldDBType.STRING, true, false);

    public static final ViewCategoryField CREATEDATE = new ViewCategoryField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ViewCategoryField CREATEUSERID = new ViewCategoryField("CREATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final ViewCategoryField UPDATEDATE = new ViewCategoryField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ViewCategoryField UPDATEUSERID = new ViewCategoryField("UPDATEUSERID", ObjectFieldDBType.STRING, true, false);

    //
    public ViewCategoryField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ViewCategoryField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
