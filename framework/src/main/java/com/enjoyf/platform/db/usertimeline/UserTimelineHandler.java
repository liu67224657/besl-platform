package com.enjoyf.platform.db.usertimeline;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.timeline.UserTimeline;
import com.enjoyf.platform.service.timeline.UserTimelineField;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="wengangsai@straff.joyme.com">saiwengang</a>
 * Create time: 14/12/10
 * Description:
 */
public class UserTimelineHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private UserTimelineAccessor userTimelineAccessor;

    public UserTimelineHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the data source
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        userTimelineAccessor = TableAccessorFactory.get().factoryAccessor(UserTimelineAccessor.class, dataBaseType);
    }


    public UserTimeline buildTimeline(UserTimeline timeline) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userTimelineAccessor.insert(timeline, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<UserTimeline> queryUserTimeline(String profileid, String domain, String type, Pagination page)throws DbException  {
        PageRows<UserTimeline> returnValue = new PageRows<UserTimeline>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress= new QueryExpress()
                    .add(QueryCriterions.eq(UserTimelineField.PROFILEID, profileid))
                    .add(QueryCriterions.eq(UserTimelineField.TYPE, type));
            if (!StringUtil.isEmpty(type))
                queryExpress.add(QueryCriterions.eq(UserTimelineField.TYPE, type));
            if (!StringUtil.isEmpty(domain))
                queryExpress.add(QueryCriterions.eq(UserTimelineField.DOMAIN, domain));
            returnValue.setRows(userTimelineAccessor.query(queryExpress, conn));
            returnValue.setPage(page);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public List<UserTimeline> queryUserTimeline(QueryExpress queryExpress)throws DbException  {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userTimelineAccessor.query(queryExpress,conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserTimeline getUserTimeline(String profileid, String type, String domain, String actionType) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress= new QueryExpress()
                    .add(QueryCriterions.eq(UserTimelineField.PROFILEID, profileid))
                    .add(QueryCriterions.eq(UserTimelineField.TYPE, type));
            if (!StringUtil.isEmpty(actionType))
                queryExpress.add(QueryCriterions.eq(UserTimelineField.ACTIONTYPE, actionType));
            if (!StringUtil.isEmpty(domain))
                queryExpress.add(QueryCriterions.eq(UserTimelineField.DOMAIN, domain));
            return userTimelineAccessor.get(queryExpress,conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean delUserTimeline(String profileid,Long destId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress= new QueryExpress()
                    .add(QueryCriterions.eq(UserTimelineField.PROFILEID, profileid))
                    .add(QueryCriterions.eq(UserTimelineField.DESTID, destId));
            return userTimelineAccessor.delete(queryExpress,conn)>0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    public boolean delUserTimelineById(Long itemId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress= new QueryExpress()
                    .add(QueryCriterions.eq(UserTimelineField.ITEMID, itemId));
            return userTimelineAccessor.delete(queryExpress,conn)>0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
    public UserTimeline getUserTimeLineById(Long itemid)  throws DbException{
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress= new QueryExpress()
                    .add(QueryCriterions.eq(UserTimelineField.ITEMID, itemid));
            return userTimelineAccessor.get(queryExpress,conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
