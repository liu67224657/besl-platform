package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import com.enjoyf.platform.util.sql.mongodb.AbstractMongoObjectField;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-22 下午12:35
 * Description:
 */
public class GameDBChannelField extends AbstractMongoObjectField {

    public static final GameDBChannelField CHANNEL_ID = new GameDBChannelField("_id", ObjectFieldDBType.LONG);
    public static final GameDBChannelField GAME_DB_CHANNEL_NAME = new GameDBChannelField("channel_name", ObjectFieldDBType.STRING);
    public static final GameDBChannelField GAME_DB_CHANNEL_CODE = new GameDBChannelField("channel_code", ObjectFieldDBType.STRING);

    public GameDBChannelField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
