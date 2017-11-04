package com.enjoyf.platform.db.sync;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.sync.ShareBody;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.service.sync.ShareTopic;
import com.enjoyf.platform.service.sync.ShareUserLog;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-4
 * Time: 下午3:59
 * To change this template use File | Settings | File Templates.
 */
public class SyncHandler {

    private DataBaseType dataBaseType;
    private String dataSourceName;

    private ShareBaseInfoAccessor shareInfoAccessor;

    private ShareTopicAccessor shareTopicAccessor;
    private ShareBodyAccessor shareBodyAccessor;

    private ShareUserLogAccessor shareUserLogAccessor;

    //
    public SyncHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        shareInfoAccessor = TableAccessorFactory.get().factoryAccessor(ShareBaseInfoAccessor.class, dataBaseType);
        shareTopicAccessor = TableAccessorFactory.get().factoryAccessor(ShareTopicAccessor.class, dataBaseType);
        shareBodyAccessor = TableAccessorFactory.get().factoryAccessor(ShareBodyAccessor.class, dataBaseType);
        shareUserLogAccessor = TableAccessorFactory.get().factoryAccessor(ShareUserLogAccessor.class, dataBaseType);
    }

    public ShareBaseInfo insertShareInfo(ShareBaseInfo shareInfo) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shareInfoAccessor.insert(shareInfo, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ShareBaseInfo> queryShareInfo(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<ShareBaseInfo> returnObj = null;

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            //call query method
            List<ShareBaseInfo> list = shareInfoAccessor.query(queryExpress, pagination, conn);

            //new return Pagerows
            returnObj = new PageRows<ShareBaseInfo>();
            returnObj.setRows(list);
            returnObj.setPage(pagination);

            return returnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ShareBaseInfo> queryShareInfo(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shareInfoAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public ShareBody insertShareBody(ShareBody shareBody) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shareBodyAccessor.insert(shareBody, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ShareBody> queryShareBodyByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<ShareBody> returnObj = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ShareBody> list = shareBodyAccessor.query(queryExpress, pagination, conn);
            returnObj = new PageRows<ShareBody>();
            returnObj.setRows(list);
            returnObj.setPage(pagination);
            return returnObj;


        } finally {
            DataBaseUtil.closeConnection(conn);
        }


    }

    public ShareTopic insertShareTopic(ShareTopic shareTopic) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shareTopicAccessor.insert(shareTopic, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<ShareTopic> queryShareTopic(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<ShareTopic> returnObj = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ShareTopic> list = shareTopicAccessor.query(queryExpress, pagination, conn);
            returnObj = new PageRows<ShareTopic>();
            returnObj.setRows(list);
            returnObj.setPage(pagination);
            return returnObj;


        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ShareTopic> queryShareTopic(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return shareTopicAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateShareTopic(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return shareTopicAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean updateShareInfo(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shareInfoAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateShareBody(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return shareBodyAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public List<ShareBody> queryShareBody(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return shareBodyAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ShareBaseInfo getShareInfo(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return shareInfoAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public ShareUserLog insertShareUserLog(ShareUserLog log) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return shareUserLogAccessor.insert(log, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


}
