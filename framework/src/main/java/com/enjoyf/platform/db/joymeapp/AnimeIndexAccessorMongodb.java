package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AnimeIndex;
import com.enjoyf.platform.service.joymeapp.AnimeRedirectType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-9-15
 * Time: 下午8:12
 * To change this template use File | Settings | File Templates.
 */
public class AnimeIndexAccessorMongodb extends AbstractSequenceBaseMongoDbAccessor<AnimeIndex> {

    private String COLLECTION_NAME = "anime_index";


    private String SEQ_NAME = "SEQ_ANIME_INDEX_ID";

    public AnimeIndex insert(AnimeIndex animeIndex, DB db) throws DbException {
        long curValue = getSeqNo(SEQ_NAME, db);
        animeIndex.setAnimeIndexId(curValue);
        WriteResult writeResult = db.getCollection(COLLECTION_NAME).insert(entryToDBObject(animeIndex));
        if (writeResult.getN() > 0) {
            return animeIndex;
        }
        return null;
    }

    public List<AnimeIndex> query(MongoQueryExpress queryExpress, DB db) throws DbException {
        return super.query(queryExpress, COLLECTION_NAME, db);
    }

    public List<AnimeIndex> query(MongoQueryExpress queryExpress, Pagination pagination, DB db) throws DbException {
        return super.query(queryExpress, pagination, COLLECTION_NAME, db);
    }

    public AnimeIndex get(BasicDBObject basicDBObject, DB db) throws DbException {
        AnimeIndex animeIndex = null;
        DBObject dbObject = db.getCollection(COLLECTION_NAME).findOne(basicDBObject);
        if (dbObject != null) {
            animeIndex = dbObjToEntry(dbObject);
        }
        return animeIndex;
    }

    public boolean modify(BasicDBObject queryDBObject, BasicDBObject updateDBObject, DB db) {
        DBObject dbObject = db.getCollection(COLLECTION_NAME).findOne(queryDBObject);
        Set<String> updateSet = updateDBObject.keySet();
        for (String key : updateSet) {
            dbObject.put(key, updateDBObject.get(key));
        }
        int i = db.getCollection(COLLECTION_NAME).update(queryDBObject, dbObject).getN();
        return i > 0;
    }


    @Override
    protected AnimeIndex dbObjToEntry(DBObject dbObject) {
        AnimeIndex animeIndex = new AnimeIndex();
        animeIndex.setAnimeIndexId((Long) dbObject.get("_id"));
        animeIndex.setLine_name((String) dbObject.get("line_name"));
        animeIndex.setAppkey((String) dbObject.get("appkey"));
        animeIndex.setTitle((String) dbObject.get("title"));
        animeIndex.setCode((String) dbObject.get("code"));
        animeIndex.setCreateDate((Date) dbObject.get("create_date"));
        animeIndex.setCreateUser((String) dbObject.get("create_user"));
        animeIndex.setValidStatus((ValidStatus.getByCode((String) dbObject.get("valid_status"))));
        animeIndex.setAnimeRedirectType(AnimeRedirectType.getByCode((Integer) dbObject.get("anime_redirect")));
        animeIndex.setLinkUrl((String) dbObject.get("link_url"));
        animeIndex.setPic_url((String) dbObject.get("pic_url"));
        Object object = dbObject.get("platform");
        if (object != null) {
            animeIndex.setPlatform((Integer) object);
        }else{
            animeIndex.setPlatform(-1);
        }
        animeIndex.setSuperScript((String) dbObject.get("super_script"));
        animeIndex.setWikiPageNum((String) dbObject.get("wiki_page_num"));
        animeIndex.setDesc((String) dbObject.get("desc"));
        return animeIndex;
    }


    @Override
    protected BasicDBObject entryToDBObject(AnimeIndex entry) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("_id", entry.getAnimeIndexId());
        dbObject.put("appkey", entry.getAppkey());
        dbObject.put("line_name", entry.getLine_name());
        dbObject.put("title", entry.getTitle());
        dbObject.put("code", entry.getCode());
        dbObject.put("create_date", entry.getCreateDate());
        dbObject.put("create_user", entry.getCreateUser());
        dbObject.put("valid_status", entry.getValidStatus().getCode());
        dbObject.put("anime_redirect", entry.getAnimeRedirectType().getCode());
        dbObject.put("link_url", entry.getLinkUrl());
        dbObject.put("pic_url", entry.getPic_url());
        dbObject.put("platform", entry.getPlatform());
        dbObject.put("super_script", entry.getSuperScript());
        dbObject.put("wiki_page_num", entry.getWikiPageNum());
        dbObject.put("desc", entry.getDesc());
        return dbObject;
    }


}
