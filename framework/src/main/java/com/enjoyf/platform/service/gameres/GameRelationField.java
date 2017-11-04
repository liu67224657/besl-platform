/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="erickliu@enjoyfound.com">ericliu</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class GameRelationField extends AbstractObjectField {
    //
    public static final GameRelationField RELATIONID= new GameRelationField("RELATIONID", ObjectFieldDBType.LONG, true, true);
    public static final GameRelationField RESOURCEID = new GameRelationField("RESOURCEID", ObjectFieldDBType.LONG, true, false);
    public static final GameRelationField SORTNUM = new GameRelationField("SORTNUM", ObjectFieldDBType.INT, true, false);
    public static final GameRelationField RELATIONTYPE = new GameRelationField("RELATIONTYPE", ObjectFieldDBType.STRING, false, false);
    public static final GameRelationField RELATIONVALUE = new GameRelationField("RELATIONVALUE", ObjectFieldDBType.STRING, true, false);
    public static final GameRelationField VALIDSTATUS = new GameRelationField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final GameRelationField RELATIONNAME = new GameRelationField("RELATIONNAME", ObjectFieldDBType.STRING, true, false);



    //
    public GameRelationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GameRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
