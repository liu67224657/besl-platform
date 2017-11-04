package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.*;

import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-20 下午2:49
 * Description:
 */
public class GameOrderedAccessorMongo extends AbstractSequenceBaseMongoDbAccessor<GameOrdered> {

    private String COLLECTION_NAME = "game_ordered";
    private String SEQ_NAME = "SEQ_GAME_ORDERED_ID";

    public GameOrdered get(BasicDBObject basicDBObject, DB db) throws DbException {
        GameOrdered gameOrdered = null;
        DBObject dbObject = db.getCollection(COLLECTION_NAME).findOne(basicDBObject);
        if (dbObject != null) {
            gameOrdered = dbObjToEntry(dbObject);
        }
        return gameOrdered;
    }

    @Override
    public GameOrdered insert(GameOrdered gameOrdered, DB db) throws DbException {
        long curValue = getSeqNo(SEQ_NAME, db);
        gameOrdered.setGameOrderedId(curValue);
        WriteResult writeResult = db.getCollection(COLLECTION_NAME).insert(entryToDBObject(gameOrdered));
        if (writeResult.getN() > 0) {
            return gameOrdered;
        }
        return gameOrdered;
    }

    @Override
    protected GameOrdered dbObjToEntry(DBObject dbObject) {
        GameOrdered gameOrdered = new GameOrdered();
        gameOrdered.setGameOrderedId((Long) dbObject.get(GameOrderedField.ID.getColumn()));
        gameOrdered.setGameId((Long) dbObject.get(GameOrderedField.GAMEID.getColumn()));
        gameOrdered.setName((String)dbObject.get(GameOrderedField.NAME.getColumn()));
        gameOrdered.setWeiXin((String)dbObject.get(GameOrderedField.WEIXIN.getColumn()));
        gameOrdered.setQq((String)dbObject.get(GameOrderedField.QQ.getColumn()));
        gameOrdered.setMobile((String)dbObject.get(GameOrderedField.MOBILE.getColumn()));
        return gameOrdered;
    }

    @Override
    protected BasicDBObject entryToDBObject(GameOrdered gameOrdered) {
        BasicDBObject dbObject = new BasicDBObject();
        //基本
        dbObject.put(GameOrderedField.ID.getColumn(), gameOrdered.getGameOrderedId());
        dbObject.put(GameOrderedField.GAMEID.getColumn(), gameOrdered.getGameId());
        dbObject.put(GameOrderedField.NAME.getColumn(), gameOrdered.getName());
        dbObject.put(GameOrderedField.WEIXIN.getColumn(), gameOrdered.getWeiXin());
        dbObject.put(GameOrderedField.QQ.getColumn(), gameOrdered.getQq());
        dbObject.put(GameOrderedField.MOBILE.getColumn(), gameOrdered.getMobile());
        return dbObject;
    }
}
