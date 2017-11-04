/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.advertise.app;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.conn.MongoSourceManager;
import com.enjoyf.platform.service.advertise.app.AppAdvertise;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublish;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePv;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.mongodb.DB;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class AppAdvertiseHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private AppAdvertiseAccessor advertiseAccessor;
    private AppAdvertisePublishAccessor publishAccessor;

    private AppAdvertisePvAccessorMongo appAdvertisePvAccessorMongo;

    private static final String DB_GAME_DB = "advertise";

    public AppAdvertiseHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        advertiseAccessor = TableAccessorFactory.get().factoryAccessor(AppAdvertiseAccessor.class, dataBaseType);
        publishAccessor = TableAccessorFactory.get().factoryAccessor(AppAdvertisePublishAccessor.class, dataBaseType);


        MongoSourceManager.get().append(dsn, props);
        appAdvertisePvAccessorMongo = new AppAdvertisePvAccessorMongo();
    }


    public AppAdvertise insertAppAdvertise(AppAdvertise appAdvertise) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return advertiseAccessor.insert(appAdvertise, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppAdvertise getAppAdvertise(long appAdvertiseId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return advertiseAccessor.get(appAdvertiseId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppAdvertisePublish insertAppAdvertisePublish(AppAdvertisePublish publish) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return publishAccessor.insert(publish, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AppAdvertisePublish> queryAdvertisePublish(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return publishAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AppAdvertise> queryAdvertise(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return advertiseAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AppAdvertise> pageQueryAdvertise(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<AppAdvertise> pageRows = new PageRows<AppAdvertise>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            pageRows.setRows(advertiseAccessor.query(queryExpress, pagination, conn));
            pageRows.setPage(pagination);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return pageRows;
    }

    public boolean updateAdvertise(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return advertiseAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AppAdvertisePublish> pageQueryAppAdvertisePublish(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<AppAdvertisePublish> pageRows = new PageRows<AppAdvertisePublish>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            pageRows.setRows(publishAccessor.query(queryExpress, pagination, conn));
            pageRows.setPage(pagination);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return pageRows;
    }

    public AppAdvertisePublish getAppAdvertisePublish(long publishId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return publishAccessor.get(publishId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAppAdvertisePublish(UpdateExpress updateExpress, QueryExpress queryExpress, long publishId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return publishAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AppAdvertisePv insert(AppAdvertisePv appAdvertisePv) throws DbException {
        DB db = DbConnFactory.mongoDbFactory(dataSourceName, DB_GAME_DB);
        return appAdvertisePvAccessorMongo.insert(appAdvertisePv, db);
    }
}
