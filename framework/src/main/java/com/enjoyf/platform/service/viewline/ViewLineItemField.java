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
public class ViewLineItemField extends AbstractObjectField {
    //
    public static final ViewLineItemField ITEMID = new ViewLineItemField("ITEMID", ObjectFieldDBType.LONG, false, true);

    public static final ViewLineItemField LINEID = new ViewLineItemField("LINEID", ObjectFieldDBType.INT, true, false);
    public static final ViewLineField CATEGORYID = new ViewLineField("CATEGORYID", ObjectFieldDBType.INT, false, true);

    public static final ViewLineField CATEGORYASPECT = new ViewLineField("CATEGORYASPECT", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineItemField DISPLAYINFO = new ViewLineItemField("DISPLAYINFO", ObjectFieldDBType.STRING, true, false);

    public static final ViewLineItemField ITEMDESC = new ViewLineItemField("ITEMDESC", ObjectFieldDBType.STRING, true, false);

    public static final ViewLineItemField DIRECTUNO = new ViewLineItemField("DIRECTUNO", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineItemField DIRECTID = new ViewLineItemField("DIRECTID", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineItemField PARENTUNO = new ViewLineItemField("PARENTUNO", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineItemField PARENTID = new ViewLineItemField("PARENTID", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineItemField RELATIONUNO = new ViewLineItemField("RELATIONUNO", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineItemField RELATIONID = new ViewLineItemField("RELATIONID", ObjectFieldDBType.STRING, true, false);

    public static final ViewLineItemField ITEMCREATEDATE = new ViewLineItemField("ITEMCREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final ViewLineItemField DISPLAYORDER = new ViewLineItemField("DISPLAYORDER", ObjectFieldDBType.INT, true, false);
    public static final ViewLineItemField DISPLAYTYPE = new ViewLineItemField("DISPLAYTYPE", ObjectFieldDBType.INT, true, false);

    public static final ViewLineItemField VALIDSTATUS = new ViewLineItemField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);

    public static final ViewLineItemField CREATEDATE = new ViewLineItemField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ViewLineItemField CREATEUNO = new ViewLineItemField("CREATEUNO", ObjectFieldDBType.STRING, true, false);

    public static final ViewLineItemField FEILTER1 = new ViewLineItemField("FEILTER1", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineItemField FEILTER2 = new ViewLineItemField("FEILTER2", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineItemField FEILTER3 = new ViewLineItemField("FEILTER3", ObjectFieldDBType.STRING, true, false);
    public static final ViewLineItemField FEILTER4 = new ViewLineItemField("FEILTER4", ObjectFieldDBType.STRING, true, false);


    //
    public ViewLineItemField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ViewLineItemField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
