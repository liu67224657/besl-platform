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
public class StatsEditorField extends AbstractObjectField {
    //ADMINUNO, EDITORNAME, EDITORDESC, VALIDSTATUS, CREATEDATE, CREATEIP
    public static final StatsEditorField ADMINUNO = new StatsEditorField("ADMINUNO", ObjectFieldDBType.INT, true, false);

    public static final StatsEditorField EDITORNAME = new StatsEditorField("EDITORNAME", ObjectFieldDBType.STRING, true, false);
    public static final StatsEditorField EDITORDESC = new StatsEditorField("EDITORDESC", ObjectFieldDBType.STRING, true, false);

    //
    public static final StatsEditorField VALIDSTATUS = new StatsEditorField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);

    //
    public static final StatsEditorField CREATEDATE = new StatsEditorField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final StatsEditorField CREATEIP = new StatsEditorField("CREATEIP", ObjectFieldDBType.STRING, true, false);

    //
    public StatsEditorField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public StatsEditorField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
