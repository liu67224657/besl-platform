package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBChannel;
import com.enjoyf.platform.service.gameres.gamedb.GameDbChannelEntry;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-11-21
 * Time: 下午4:52
 * To change this template use File | Settings | File Templates.
 */
public class GameDBChannelAccessorMongo extends AbstractSequenceBaseMongoDbAccessor<GameDBChannel> {
    private String COLLECTION_NAME = "game_db_channel";
    private String SEQ_NAME = "SEQ_CHANNEL_ID";

    @Override
    public GameDBChannel insert(GameDBChannel gameDbChannel, DB db) throws DbException {
        long curValue = getSeqNo(SEQ_NAME, db);

        gameDbChannel.setChannelId(curValue);
        WriteResult writeResult = db.getCollection(COLLECTION_NAME).insert(entryToDBObject(gameDbChannel));

        if (writeResult.getN() > 0) {
            return gameDbChannel;
        }

        return null;
    }

    public List<GameDBChannel> query(MongoQueryExpress queryExpress, DB db) throws DbException {
        return super.query(queryExpress, COLLECTION_NAME, db);
    }

    public GameDBChannel get(MongoQueryExpress queryExpress, DB db) throws DbException {
        return super.get(queryExpress, COLLECTION_NAME, db);
    }

    public GameDBChannel get(BasicDBObject basicDBObject, DB db) throws DbException {
        GameDBChannel gameDbChannel = null;
        DBObject dbObject = db.getCollection(COLLECTION_NAME).findOne(basicDBObject);
        if (dbObject != null) {
            gameDbChannel = dbObjToEntry(dbObject);
        }
        return gameDbChannel;
    }

    @Override
    protected GameDBChannel dbObjToEntry(DBObject dbObject) {
        GameDBChannel entry = new GameDBChannel();
        entry.setChannelId(Long.parseLong((dbObject.get("_id").toString())));
        entry.setChannelName((String) dbObject.get("channel_name"));
        entry.setChannelCode((String) dbObject.get("channel_code"));
//        entry.getGameDBChannelInfo().setGameDbChannelId((Long) dbObject.get("gamedbchannelid"));
//        entry.getGameDBChannelInfo().setDownload((String) dbObject.get("download"));

        return entry;
    }

    @Override
    protected BasicDBObject entryToDBObject(GameDBChannel entry) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("_id", entry.getChannelId());
        dbObject.put("channel_name", entry.getChannelName());
        dbObject.put("channel_code", entry.getChannelCode());
//        dbObject.put("gamedbchannelid", entry.getGameDBChannelInfo().getGameDbChannelId());
//        dbObject.put("download", entry.getGameDBChannelInfo().getDownload());
        return dbObject;
    }
}
