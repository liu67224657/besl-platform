package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.AbstractSequenceBaseMongoDbAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.vote.WikiVote;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-20
 * Time: 下午4:04
 * To change this template use File | Settings | File Templates.
 */
public class WikiVoteAccessorMongo extends AbstractSequenceBaseMongoDbAccessor<WikiVote> {
    private static final String KEY_COLLECTION_NAME = "wiki_vote";

    @Override
    protected BasicDBObject entryToDBObject(WikiVote entry) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("_id", entry.getArticleId());
        dbObject.put("url", entry.getUrl());
        dbObject.put("name", entry.getName());
        dbObject.put("nostr", entry.getNoStr());
        dbObject.put("pic", entry.getPic());
        dbObject.put("keywords", entry.getKeyWords());
        dbObject.put("votessum", entry.getVotesSum());
        dbObject.put("createtime", entry.getCreateDate() == null ? System.currentTimeMillis() : entry.getCreateDate().getTime());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dbObject.put("createdate", df.format(entry.getCreateDate() == null ? new Date(System.currentTimeMillis()) : entry.getCreateDate()));
        dbObject.put("modifytime", entry.getModifyDate() == null ? null : entry.getModifyDate().getTime());
        dbObject.put("modifydate", null);
        dbObject.put("modifyuserid", entry.getModifyUserId() == null ? "" : entry.getModifyUserId());
        dbObject.put("removestatus", entry.getRemoveStatus() == null ? ActStatus.UNACT.getCode() : entry.getRemoveStatus().getCode());

        return dbObject;
    }

    @Override
    protected WikiVote dbObjToEntry(DBObject dbObject) {
        WikiVote wikiVote = new WikiVote();
        wikiVote.setArticleId((Long) dbObject.get("_id"));
        wikiVote.setKeyWords((String) dbObject.get("keywords"));
        wikiVote.setName((String) dbObject.get("name"));
        wikiVote.setNoStr((String) dbObject.get("nostr"));
        wikiVote.setUrl((String) dbObject.get("url"));
        wikiVote.setPic((String) dbObject.get("pic"));
        wikiVote.setVotesSum(dbObject.get("votessum") == null ? 0 : (Integer) dbObject.get("votessum"));
        wikiVote.setCreateDate(dbObject.get("createtime") == null ? null : new Date((Long) dbObject.get("createtime")));
        wikiVote.setModifyDate(dbObject.get("modifytime") == null ? null : new Date((Long) dbObject.get("modifytime")));
        wikiVote.setModifyUserId((String) dbObject.get("modifyuserid"));
        wikiVote.setRemoveStatus(ActStatus.getByCode((String) dbObject.get("removestatus")));
        return wikiVote;
    }

    @Override
    public WikiVote insert(WikiVote wikiVote, DB db) throws DbException {
        WriteResult writeResult = db.getCollection(KEY_COLLECTION_NAME).insert(entryToDBObject(wikiVote));
        if (writeResult.getN() > 0) {
            return wikiVote;
        }
        return null;
    }

    public WikiVote getWikiVote(MongoQueryExpress mongoQueryExpress, DB db) throws DbException {
        return super.get(mongoQueryExpress, KEY_COLLECTION_NAME, db);
    }

    public boolean update(BasicDBObject query, BasicDBObject update, DB db) {
        return super.update(query, update, KEY_COLLECTION_NAME, db);
    }

    public List<WikiVote> queryByPage(MongoQueryExpress mongoQueryExpress, Pagination pagination, DB db) throws DbException {
        return super.query(mongoQueryExpress, pagination, KEY_COLLECTION_NAME, db);
    }
}
