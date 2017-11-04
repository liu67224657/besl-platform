package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.event.Activity;
import com.enjoyf.platform.service.event.ActivityAwardLog;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-5
 * Time: 下午6:57
 * To change this template use File | Settings | File Templates.
 */
public class ActivityHandler {

    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private ActivityAccessor activityAccessor;

    private ActivityAwardLogAccessor activityAwardLogAccessor;

    public ActivityHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource


        DataSourceManager.get().append(dataSourceName, props);

        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        activityAccessor =TableAccessorFactory.get().factoryAccessor(ActivityAccessor.class, dataBaseType);

        activityAwardLogAccessor =TableAccessorFactory.get().factoryAccessor(ActivityAwardLogAccessor.class, dataBaseType);
    }

    public Activity insertActivity(Activity activity)throws DbException{
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return activityAccessor.insert(activity, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Activity getActivity(QueryExpress queryExpress)throws DbException{
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return activityAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Activity> queryActivity(QueryExpress queryExpress,Pagination page)throws DbException{
        Connection conn = null;

        try {
            PageRows<Activity> rows=new PageRows<Activity>();

            conn = DbConnFactory.factory(dataSourceName);

            rows.setRows(activityAccessor.query(queryExpress, page, conn));
            rows.setPage(page);

            return rows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyActivity(UpdateExpress updateExpress,QueryExpress queryExpress)throws DbException{
        Connection conn = null;

        try {
            PageRows<Activity> rows=new PageRows<Activity>();

            conn = DbConnFactory.factory(dataSourceName);
            return activityAccessor.update(updateExpress,queryExpress,conn)>0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public ActivityAwardLog insertAwardLog(ActivityAwardLog log) throws DbException{
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityAwardLogAccessor.insert(log,conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ActivityAwardLog> queryAwardLog(QueryExpress queryExpress)throws DbException{
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityAwardLogAccessor.query(queryExpress,conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

}
