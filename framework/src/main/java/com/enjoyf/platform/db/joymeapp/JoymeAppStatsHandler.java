package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.conn.MongoSourceManager;
import com.enjoyf.platform.service.joymeapp.AnimeIndex;
import com.enjoyf.platform.service.joymeapp.AppErrorInfo;
import com.enjoyf.platform.service.joymeapp.AppInstallInfo;
import com.enjoyf.platform.service.joymeapp.AppPageViewInfo;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-9-15
 * Time: 下午8:06
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppStatsHandler {

    private String dataSourceName;

    private static final String DB_JOYMEAPP = "joymeapp";

    private AppInstallLogAccessorMongodb installStatsAccessorMongodb;
    private AppPageViewLogAccessorMongodb pageViewInfoAccessorMongodb;
    private AppErrorLogAccessorMongodb errorLogsStatsAccessorMongodb;
    private AnimeIndexAccessorMongodb animeIndexAccessorMongodb;

    public JoymeAppStatsHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn;

        MongoSourceManager.get().append(dsn, props);

        installStatsAccessorMongodb = new AppInstallLogAccessorMongodb();
        pageViewInfoAccessorMongodb = new AppPageViewLogAccessorMongodb();
        errorLogsStatsAccessorMongodb = new AppErrorLogAccessorMongodb();
        animeIndexAccessorMongodb = new AnimeIndexAccessorMongodb();
    }


    public AppInstallInfo insertInstallLog(AppInstallInfo appInstallInfo) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_JOYMEAPP);
        return installStatsAccessorMongodb.insert(appInstallInfo, db);
    }

    public void insertPvLog(AppPageViewInfo appPageViewInfo) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_JOYMEAPP);
        pageViewInfoAccessorMongodb.insert(appPageViewInfo, db);
    }

    public void insertErrorLog(AppErrorInfo appErrorInfo) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_JOYMEAPP);
        errorLogsStatsAccessorMongodb.insert(appErrorInfo, db);
    }

    public List<AppErrorInfo> queryErrorLog(BasicDBObject queryObject, Date date) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_JOYMEAPP);
        return errorLogsStatsAccessorMongodb.query(queryObject, date, db);
    }

    //AnimeIndex start
    public List<AnimeIndex> queryAnimeIndex(MongoQueryExpress express) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_JOYMEAPP);
        return animeIndexAccessorMongodb.query(express, db);
    }

    public AnimeIndex getAnimeIndex(BasicDBObject express) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_JOYMEAPP);
        return animeIndexAccessorMongodb.get(express, db);
    }

    public boolean modify(BasicDBObject queryObject, BasicDBObject updateObject) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_JOYMEAPP);
        return animeIndexAccessorMongodb.modify(queryObject, updateObject, db);
    }

    public AnimeIndex insertAnimeIndex(AnimeIndex animeIndex) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_JOYMEAPP);
        return animeIndexAccessorMongodb.insert(animeIndex, db);
    }

    public PageRows<AnimeIndex> queryAniIndexByPage(MongoQueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<AnimeIndex> pageRows = null;
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_JOYMEAPP);
        List<AnimeIndex> list = animeIndexAccessorMongodb.query(queryExpress, pagination, db);
        pageRows = new PageRows<AnimeIndex>();
        pageRows.setPage(pagination);
        pageRows.setRows(list);

        return pageRows;
    }


}
