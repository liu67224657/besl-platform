package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import com.enjoyf.platform.util.sql.mongodb.AbstractMongoObjectField;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-22 下午12:35
 * Description:
 */
public class GameOrderedField extends AbstractMongoObjectField {
    //基本
    public static final GameOrderedField ID = new GameOrderedField("_id", ObjectFieldDBType.LONG);
    public static final GameOrderedField GAMEID = new GameOrderedField("gameid", ObjectFieldDBType.LONG);
    public static final GameOrderedField NAME = new GameOrderedField("name", ObjectFieldDBType.STRING);
    public static final GameOrderedField WEIXIN = new GameOrderedField("weixin", ObjectFieldDBType.STRING);
    public static final GameOrderedField QQ = new GameOrderedField("qq", ObjectFieldDBType.STRING);
    public static final GameOrderedField MOBILE = new GameOrderedField("mobile", ObjectFieldDBType.STRING);
    public GameOrderedField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
