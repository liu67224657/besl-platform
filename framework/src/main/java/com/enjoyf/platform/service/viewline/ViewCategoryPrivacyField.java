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
public class ViewCategoryPrivacyField extends AbstractObjectField {
    //
    public static final ViewCategoryPrivacyField CATEGORYID = new ViewCategoryPrivacyField("CATEGORYID", ObjectFieldDBType.INT, false, true);

    public static final ViewCategoryPrivacyField PRIVACYLEVEL = new ViewCategoryPrivacyField("PRIVACYLEVEL", ObjectFieldDBType.STRING, false, true);
    public static final ViewCategoryPrivacyField UNO = new ViewCategoryPrivacyField("UNO", ObjectFieldDBType.STRING, false, true);

    public static final ViewCategoryPrivacyField CREATEDATE = new ViewCategoryPrivacyField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ViewCategoryPrivacyField CREATEUSERID = new ViewCategoryPrivacyField("CREATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final ViewCategoryPrivacyField UPDATEDATE = new ViewCategoryPrivacyField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ViewCategoryPrivacyField UPDATEUSERID = new ViewCategoryPrivacyField("UPDATEUSERID", ObjectFieldDBType.STRING, true, false);

    //
    public ViewCategoryPrivacyField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ViewCategoryPrivacyField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
