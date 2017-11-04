package com.enjoyf.platform.db.stats;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.conn.MongoSourceManager;
import com.enjoyf.platform.service.stats.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.DB;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-3-21
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
public class StatMongoDBHandler {

    private static final String DB_ADVERTISE_DB = "page_view";

    private DataBaseType dataBaseType;
    private String dataSourceName;

    private AdvertisePageViewAccessorMongo statPageViewAccessorMongo;
    private AdvertiseUserViewAccessorMongo statUserViewAccessorMongo;
    private AdvertiseBounceRateAccessorMongo statBounceRateAccessorMongo;
    private AdvertiseViewTimeAccessorMongo statViewTimeAccessorMongo;
    private AdvertiseViewDepthAccessorMongo statViewDepthAccessorMongo;


    public StatMongoDBHandler(String dsn, FiveProps fiveProps) throws DbException {
        this.dataSourceName = dsn;
        MongoSourceManager.get().append(dsn, fiveProps);

        statPageViewAccessorMongo = new AdvertisePageViewAccessorMongo();
        statUserViewAccessorMongo = new AdvertiseUserViewAccessorMongo();
        statBounceRateAccessorMongo = new AdvertiseBounceRateAccessorMongo();
        statViewTimeAccessorMongo = new AdvertiseViewTimeAccessorMongo();
        statViewDepthAccessorMongo = new AdvertiseViewDepthAccessorMongo();
    }

    public int uvCount(MongoQueryExpress queryExpress, Date date) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_ADVERTISE_DB);
        return statUserViewAccessorMongo.uvCount(queryExpress, date, db);
    }

    public List<StatPageView> queryAdvertisePV(MongoQueryExpress queryExpress, Date statDate, Date endDate) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_ADVERTISE_DB);
        return statPageViewAccessorMongo.queryAdvertisePV(queryExpress, statDate, endDate, db);
    }

    public List<StatViewBounce> queryAdvertiseViewBounce(MongoQueryExpress queryExpress, Date statDate, Date endDate) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_ADVERTISE_DB);
        return statBounceRateAccessorMongo.queryBounceRate(queryExpress, statDate, endDate, db);
    }

    public PageRows<StatUserView> queryAdvertiseUVPage(MongoQueryExpress queryExpress, Pagination pagination, Date statDate, Date endDate) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_ADVERTISE_DB);
        PageRows<StatUserView> pageRows = new PageRows<StatUserView>();
        List<StatUserView> list = statUserViewAccessorMongo.queryAdvertiseUVPage(queryExpress, pagination, statDate, endDate, db);
        pageRows.setPage(pagination);
        pageRows.setRows(list);
        return pageRows;
    }

    public StatUserView getAdvertiseUV(MongoQueryExpress query, Date date2, Date endDate2) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_ADVERTISE_DB);
        return statUserViewAccessorMongo.getAdvertiseUV(query, date2, db);
    }

    public List<StatViewTime> queryAdvertiseViewTime(MongoQueryExpress queryExpress, Date statDate, Date endDate) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_ADVERTISE_DB);
        return statViewTimeAccessorMongo.queryAdvertiseViewTime(queryExpress, statDate, endDate, db);
    }

    public List<StatViewDepth> queryAdvertiseCrawDepth(MongoQueryExpress queryExpress, Date statDate, Date endDate) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_ADVERTISE_DB);
        return statViewDepthAccessorMongo.queryAdvertiseCrawDepth(queryExpress, statDate, endDate, db);
    }

    public List<StatUserView> queryAdvertiseUV(MongoQueryExpress queryExpress, Date statDate, Date endDate) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_ADVERTISE_DB);
        return statUserViewAccessorMongo.queryAdvertiseUV(queryExpress, statDate, endDate, db);
    }
}
