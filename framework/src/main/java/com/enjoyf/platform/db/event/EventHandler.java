package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.service.event.pageview.PageViewLocation;
import com.enjoyf.platform.service.event.user.UserEventEntry;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class EventHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private UserEventAccessor userEventAccessor;
    private PageViewEventAccessor pvEventAccessor;
    private PageViewLocationAccessor pvLocationAccessor;

    //
    private PageViewEventStatsAccessor pvEventStatsAccessor;

    public EventHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);

        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        userEventAccessor = EventAccessorFactory.factoryUserEventAccessor(dataBaseType);
        pvEventAccessor = EventAccessorFactory.factoryPageViewEventAccessor(dataBaseType);
        pvLocationAccessor = EventAccessorFactory.factoryPageViewLocationAccessor(dataBaseType);
        pvEventStatsAccessor = EventAccessorFactory.factoryPageViewEventStatsAccessor(dataBaseType);
    }

    public UserEventEntry insertUserEvent(UserEventEntry event) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userEventAccessor.insert(event, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageViewEvent inertPageViewEvent(PageViewEvent event) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return pvEventAccessor.insert(event, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    //pageview location apis
    public PageViewLocation getPageViewLocationById(Integer locationId) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return pvLocationAccessor.getById(locationId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<PageViewLocation> queryAllPageViewLocations() throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return pvLocationAccessor.queryAll(conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //
    public long statsPageView(Date from, Date to, QueryExpress queryExpress) throws DbException {
        long returnValue = 0;

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            //loop to add the page view.
            List<Date> months = DateUtil.getMonthList(from, to);
            for (Date month : months) {
                returnValue += pvEventStatsAccessor.statsPageView(month, queryExpress, conn);
            }
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //
    public long statsUniqueUser(Date from, Date to, QueryExpress queryExpress) throws DbException {
        long returnValue = 0;

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            //loop to add the page view.
            List<Date> months = DateUtil.getMonthList(from, to);
            for (Date month : months) {
                returnValue += pvEventStatsAccessor.statsUniqueUser(month, queryExpress, conn);
            }
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }
}
