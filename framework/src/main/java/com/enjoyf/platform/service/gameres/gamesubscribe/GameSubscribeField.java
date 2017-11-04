package com.enjoyf.platform.service.gameres.gamesubscribe;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-22 下午12:35
 * Description:
 */
public class GameSubscribeField extends AbstractObjectField {


    public static final GameSubscribeField ID = new GameSubscribeField("id", ObjectFieldDBType.LONG);
    public static final GameSubscribeField UNO = new GameSubscribeField("uno", ObjectFieldDBType.STRING);
    public static final GameSubscribeField GAME_DB_ID = new GameSubscribeField("game_id", ObjectFieldDBType.STRING);
    public static final GameSubscribeField PLATFORM = new GameSubscribeField("platform", ObjectFieldDBType.INT);
    public static final GameSubscribeField CREATE_TIME = new GameSubscribeField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final GameSubscribeField VALIDSTATUS = new GameSubscribeField("validstatus", ObjectFieldDBType.STRING);

    public GameSubscribeField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
          super(column, type, modify, uniquene);
      }


    public GameSubscribeField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
