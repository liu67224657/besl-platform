package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.conn.MongoSourceManager;
import com.enjoyf.platform.db.joymeapp.AppErrorLogAccessorMongodb;
import com.enjoyf.platform.db.joymeapp.AppInstallLogAccessorMongodb;
import com.enjoyf.platform.db.joymeapp.AppPageViewLogAccessorMongodb;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.event.user.UserEventEntry;
import com.enjoyf.platform.util.FiveProps;
import com.mongodb.DB;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class EventMongoHandler {
    private String dataSourceName;


    private static final String DB_EVENT_DB = "event";

    private PageViewEventAccessorMongodb pvEventAccessorMongodb;

    private UserEventAccessorMongodb userEventAccessorMongodb;

    public EventMongoHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn;
        MongoSourceManager.get().append(dsn, props);
        pvEventAccessorMongodb = new PageViewEventAccessorMongodb();
        userEventAccessorMongodb = new UserEventAccessorMongodb();
    }

    public PageViewEvent insertPageViewEvent(PageViewEvent pageViewEvent) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_EVENT_DB);
        return pvEventAccessorMongodb.insert(pageViewEvent, db);
    }

    public UserEventEntry insertUserEventEntry(UserEventEntry event) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_EVENT_DB);
        return userEventAccessorMongodb.insert(event, db);
    }

}
