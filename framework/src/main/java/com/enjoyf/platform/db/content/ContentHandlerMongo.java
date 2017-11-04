package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.conn.MongoSourceManager;
import com.enjoyf.platform.service.content.social.SocialLog;
import com.enjoyf.platform.util.FiveProps;
import com.mongodb.DB;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-6-19
 * Time: 下午12:12
 * To change this template use File | Settings | File Templates.
 */
public class ContentHandlerMongo {

    private DataBaseType dataBaseType;
    private String dataSourceName;
    private static final String MONGO_DB_CONTENT = "content";

    private SocialLogAccessorMongo socialLogAccessorMongo;

    public ContentHandlerMongo(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        MongoSourceManager.get().append(dsn, props);
        socialLogAccessorMongo = new SocialLogAccessorMongo();
    }

    public SocialLog insertSocialLog(SocialLog socialLog) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, MONGO_DB_CONTENT);
        return socialLogAccessorMongo.insert(socialLog, db);
    }
}
