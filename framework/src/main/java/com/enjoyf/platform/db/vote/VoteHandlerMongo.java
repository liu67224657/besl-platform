package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.conn.MongoSourceManager;
import com.enjoyf.platform.service.vote.WikiVote;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-20
 * Time: 下午3:52
 * To change this template use File | Settings | File Templates.
 */
public class VoteHandlerMongo {

    private DataBaseType dataBaseType;
    private String dataSourceName;
    private static final String MONGO_DB_VOTE = "vote";

    private WikiVoteAccessorMongo wikiVoteAccessorMongo;

    public VoteHandlerMongo(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        MongoSourceManager.get().append(dsn, props);
        wikiVoteAccessorMongo = new WikiVoteAccessorMongo();
    }

    public WikiVote getWikiVote(MongoQueryExpress mongoQueryExpress) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, MONGO_DB_VOTE);
        return wikiVoteAccessorMongo.getWikiVote(mongoQueryExpress, db);
    }

    public WikiVote createWikiVote(WikiVote wikiVote) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, MONGO_DB_VOTE);
        return wikiVoteAccessorMongo.insert(wikiVote, db);
    }

    public boolean updateWikiVote(BasicDBObject query, BasicDBObject update) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, MONGO_DB_VOTE);
        return  wikiVoteAccessorMongo.update(query, update, db);
    }

    public List<WikiVote> queryWikiVoteByPage(MongoQueryExpress mongoQueryExpress, Pagination pagination) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, MONGO_DB_VOTE);
        return wikiVoteAccessorMongo.queryByPage(mongoQueryExpress, pagination, db);
    }
}
