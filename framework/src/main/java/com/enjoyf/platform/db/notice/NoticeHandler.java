package com.enjoyf.platform.db.notice;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.notice.SystemNotice;
import com.enjoyf.platform.service.notice.SystemNoticeField;
import com.enjoyf.platform.service.notice.UserNotice;
import com.enjoyf.platform.service.notice.UserNoticeField;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by ericliu on 16/3/19.
 */
public class NoticeHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private UserNoticeAccessor userNoticeAccessor;
    private SystemNoticeAccessor systemNoticeAccessor;

    public NoticeHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();


        userNoticeAccessor = TableAccessorFactory.get().factoryAccessor(UserNoticeAccessor.class, dataBaseType);
        systemNoticeAccessor = TableAccessorFactory.get().factoryAccessor(SystemNoticeAccessor.class, dataBaseType);
    }

    public UserNotice insertUserNotice(UserNotice userNotice) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userNoticeAccessor.insert(userNotice, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserNotice getUserNotice(long userNoticeId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userNoticeAccessor.get(new QueryExpress().add(QueryCriterions.eq(UserNoticeField.USER_NOTICE_ID, userNoticeId)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleteUserNotice(long userNoticeId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userNoticeAccessor.delete(new QueryExpress().add(QueryCriterions.eq(UserNoticeField.USER_NOTICE_ID, userNoticeId)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserNotice getUserNotice(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userNoticeAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    ////
    public SystemNotice insertSystemNotice(SystemNotice systemNotice) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return systemNoticeAccessor.insert(systemNotice, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SystemNotice getSystemNotice(long systemNoticeId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return systemNoticeAccessor.get(new QueryExpress().add(QueryCriterions.eq(SystemNoticeField.SYSTEMNOTICEID, systemNoticeId)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleteSystemNotice(long systemNoticeId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return systemNoticeAccessor.delete(new QueryExpress().add(QueryCriterions.eq(SystemNoticeField.SYSTEMNOTICEID, systemNoticeId)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifySystemNotice(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return systemNoticeAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SystemNotice> querySystemNoitcePage(QueryExpress queryExpress, Pagination page) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SystemNotice> systemNoticeList = systemNoticeAccessor.query(queryExpress, page, conn);

            PageRows<SystemNotice> pageRows = new PageRows<SystemNotice>();
            pageRows.setPage(page);
            pageRows.setRows(systemNoticeList);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

}
