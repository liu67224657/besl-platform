/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="taijunli@enjoyfound.com">taijunli</a>
 * Create time: 11-12-23 上午14:56
 * Description:
 */
public class StatsEditorItemField extends AbstractObjectField {
    //
    public static final StatsEditorItemField ITEMNO = new StatsEditorItemField("ITEMNO", ObjectFieldDBType.STRING, true, false);

    //
    public static final StatsEditorItemField ADMINUNO = new StatsEditorItemField("ADMINUNO", ObjectFieldDBType.INT, true, false);

    public static final StatsEditorItemField ITEMTYPE = new StatsEditorItemField("ITEMTYPE", ObjectFieldDBType.STRING, true, false);
    public static final StatsEditorItemField ITEMSUBTYPE = new StatsEditorItemField("ITEMSUBTYPE", ObjectFieldDBType.STRING, true, false);

    public static final StatsEditorItemField ITEMSRCNO = new StatsEditorItemField("ITEMSRCNO", ObjectFieldDBType.STRING, true, false);
    public static final StatsEditorItemField SOURCEID = new StatsEditorItemField("SOURCEID", ObjectFieldDBType.STRING, true, false);


    //
    public static final StatsEditorItemField VALIDSTATUS = new StatsEditorItemField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);

    public static final StatsEditorItemField CREATEDATE = new StatsEditorItemField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final StatsEditorItemField CREATEIP = new StatsEditorItemField("CREATEIP", ObjectFieldDBType.STRING, true, false);

    //
    public StatsEditorItemField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public StatsEditorItemField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
