package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-22 下午12:35
 * Description:
 */
public class GameDBRelationField extends AbstractObjectField {

    public static final GameDBRelationField RELATIONID = new GameDBRelationField("relationid", ObjectFieldDBType.LONG, false, true);
    public static final GameDBRelationField GAMEDBID = new GameDBRelationField("gamedbid", ObjectFieldDBType.LONG, false, true);
    public static final GameDBRelationField TITLE = new GameDBRelationField("title", ObjectFieldDBType.STRING, false, true);
    public static final GameDBRelationField TYPE = new GameDBRelationField("type", ObjectFieldDBType.INT, false, true);
    public static final GameDBRelationField URI = new GameDBRelationField("uri", ObjectFieldDBType.STRING, false, true);
    public static final GameDBRelationField VALIDSTATUS = new GameDBRelationField("validstatus", ObjectFieldDBType.INT, false, true);

    public static final GameDBRelationField MODIFYUSERID = new GameDBRelationField("modifyuserid", ObjectFieldDBType.STRING, false, true);
    public static final GameDBRelationField MODIFYIP = new GameDBRelationField("modifyip", ObjectFieldDBType.STRING, false, true);
    public static final GameDBRelationField DISPLAYORDER = new GameDBRelationField("displayorder", ObjectFieldDBType.INT, false, true);


    //
    public GameDBRelationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GameDBRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
