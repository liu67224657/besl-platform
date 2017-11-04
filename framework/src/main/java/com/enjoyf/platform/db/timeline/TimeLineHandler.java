/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.timeline;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.usertimeline.UserTimelineAccessor;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.timeline.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.sql.*;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class TimeLineHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private TimeLineItemAccessor timeLineItemAccessor;
    private TimeLineDetailAccessor itemDetailAccessor;


    private SocialTimeLineItemAccessor socialTimeLineItemAccessor;
    private UserTimelineAccessor userTimelineAccessor;

    public TimeLineHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        timeLineItemAccessor = TimeLineAccessorFactory.factoryTimeLineItemAccessor(dataBaseType);
        itemDetailAccessor = TimeLineAccessorFactory.factoryItemDetailAccessor(dataBaseType);

        socialTimeLineItemAccessor = TableAccessorFactory.get().factoryAccessor(SocialTimeLineItemAccessor.class, dataBaseType);
        userTimelineAccessor = TableAccessorFactory.get().factoryAccessor(UserTimelineAccessor.class, dataBaseType);
    }

    /////////////////////////////////////////////////////
    //the timelien apis
    /////////////////////////////////////////////////////
    public TimeLineItem insertTimeLine(TimeLineItem item) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return timeLineItemAccessor.insert(item, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<TimeLineItem> queryTimeLines(String ownUno, TimeLineDomain domain, Pagination page) throws DbException {
        PageRows<TimeLineItem> returnValue = new PageRows<TimeLineItem>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(timeLineItemAccessor.query(ownUno, domain, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public PageRows<TimeLineItem> queryTimeLinesOnlyFocus(String ownUno, TimeLineDomain domain, Pagination page) throws DbException {
        PageRows<TimeLineItem> returnValue = new PageRows<TimeLineItem>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(timeLineItemAccessor.queryOnlyFocus(ownUno, domain, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public List<TimeLineItem> queryTimelinesBefore(String ownUno, TimeLineDomain domain, Long before, Integer size) throws DbException {
        List<TimeLineItem> returnValue = null;

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = timeLineItemAccessor.queryBefore(ownUno, domain, before, size, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public List<TimeLineItem> queryTimelinesAfter(String ownUno, TimeLineDomain domain, Long after, Integer size) throws DbException {
        List<TimeLineItem> returnValue = null;

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = timeLineItemAccessor.queryAfter(ownUno, domain, after, size, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public PageRows<TimeLineItem> queryTimeLinesOrg(String ownUno, TimeLineDomain domain, Pagination page) throws DbException {
        PageRows<TimeLineItem> returnValue = new PageRows<TimeLineItem>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(timeLineItemAccessor.queryOrg(ownUno, domain, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }


    public PageRows<TimeLineItem> queryTimeLinesByFilterType(String ownUno, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page) throws DbException {
        PageRows<TimeLineItem> returnValue = new PageRows<TimeLineItem>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            returnValue.setRows(timeLineItemAccessor.query(ownUno, domain, timeLineFilterType, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public PageRows<TimeLineItem> queryTimeLinesByFilterTypeRelationId(String ownUno, String directId, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page) throws DbException {
        PageRows<TimeLineItem> returnValue = new PageRows<TimeLineItem>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            returnValue.setRows(timeLineItemAccessor.queryRelationID(ownUno, directId, domain, timeLineFilterType, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public boolean removeTimeLine(String ownUno, TimeLineDomain domain, String directId) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return timeLineItemAccessor.remove(ownUno, domain, directId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateTimeLineStatus(String ownUno, TimeLineDomain domain, String directId, ActStatus removeStatus) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return timeLineItemAccessor.updateStatus(ownUno, domain, directId, removeStatus, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean removeTimeLineByDirectUno(String ownUno, TimeLineDomain domain, String directUno) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return timeLineItemAccessor.removeByDirectUno(ownUno, domain, directUno, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public TimeLineItem getItem(String ownUno, TimeLineDomain domain, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return timeLineItemAccessor.getByExpress(ownUno, domain, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateTimeLineItem(String ownUno, TimeLineDomain domain, UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return timeLineItemAccessor.update(ownUno, domain, updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean insertItemDetail(TimeLineItemDetail timeLineItemDetail) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return itemDetailAccessor.insert(timeLineItemDetail, conn) != null;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<TimeLineItemDetail> queryItemDetail(String ownUno, QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<TimeLineItemDetail> pageRows = new PageRows<TimeLineItemDetail>();
        try {
            conn = DbConnFactory.factory(dataSourceName);


            pageRows.setRows(itemDetailAccessor.queryBySize(ownUno, queryExpress, pagination, conn));
            pageRows.setPage(pagination);

            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateItemDetail(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return itemDetailAccessor.updateDetail(ownUno, updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean delteItemDetail(String ownUno, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return itemDetailAccessor.deleteDetail(ownUno, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    //////////////////SocialTimeLineItem
    public SocialTimeLineItem insertSocialTimeLineItem(SocialTimeLineDomain socialTimeLineDomain, String ownUno, SocialTimeLineItem socialTimeLineItem) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialTimeLineItemAccessor.insertSocialTimeLineItem(socialTimeLineDomain, ownUno, socialTimeLineItem, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public SocialTimeLineItem getSocialTimeLineDomainBySId(SocialTimeLineDomain socialTimeLineDomain, String ownUno, long contentId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialTimeLineItemAccessor.getBySId(socialTimeLineDomain, ownUno, contentId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateSocialTimeLineDomain(SocialTimeLineDomain socialTimeLineDomain, String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialTimeLineItemAccessor.update(socialTimeLineDomain, ownUno, updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<SocialTimeLineItem> querySocialTimeLineDomain(SocialTimeLineDomain socialTimeLineDomain, String ownUno, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return socialTimeLineItemAccessor.query(socialTimeLineDomain, ownUno, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<SocialTimeLineItem> querySocialTimeLineDomainPageRows(SocialTimeLineDomain socialTimeLineDomain, String ownUno, QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<SocialTimeLineItem> pageRows = new PageRows<SocialTimeLineItem>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            pageRows.setRows(socialTimeLineItemAccessor.queryByPageRows(socialTimeLineDomain, ownUno, queryExpress, pagination, conn));
            pageRows.setPage(pagination);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return pageRows;
    }

    public NextPageRows<SocialTimeLineItem> querySocialTimeLineItemNextList(SocialTimeLineDomain domain, String uno, NextPagination nextPagination) throws DbException {
        Connection conn = null;
        NextPageRows<SocialTimeLineItem> nextPageRows = new NextPageRows<SocialTimeLineItem>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<SocialTimeLineItem> list = socialTimeLineItemAccessor.queryNextRows(domain, uno, nextPagination, conn);
            nextPageRows.setRows(list);
            nextPageRows.setPage(nextPagination);
            return nextPageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserTimeline buildUserTimeline(UserTimeline timeline) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userTimelineAccessor.insert(timeline, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<UserTimeline> queryUserTimeline(String profileid, String domain, String type, Pagination page) throws DbException {
        PageRows<UserTimeline> returnValue = new PageRows<UserTimeline>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(UserTimelineField.PROFILEID, profileid));
            if (!StringUtil.isEmpty(type)) {
                queryExpress.add(QueryCriterions.eq(UserTimelineField.TYPE, type));
            }
            if (!StringUtil.isEmpty(domain)) {
                queryExpress.add(QueryCriterions.eq(UserTimelineField.DOMAIN, domain));
            }
            queryExpress.add(QuerySort.add(UserTimelineField.CREATETIME, QuerySortOrder.DESC));
            returnValue.setRows(userTimelineAccessor.query(queryExpress, page, conn));
            returnValue.setPage(page);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public UserTimeline getUserTimeline(String profileid, String type, String domain, String actionType) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(UserTimelineField.PROFILEID, profileid))
                    .add(QueryCriterions.eq(UserTimelineField.TYPE, type));
            if (!StringUtil.isEmpty(actionType))
                queryExpress.add(QueryCriterions.eq(UserTimelineField.ACTIONTYPE, actionType));
            if (!StringUtil.isEmpty(domain))
                queryExpress.add(QueryCriterions.eq(UserTimelineField.DOMAIN, domain));
            return userTimelineAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean delUserTimeline(String profileid, Long destId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(UserTimelineField.PROFILEID, profileid))
                    .add(QueryCriterions.eq(UserTimelineField.DESTID, destId));
            return userTimelineAccessor.delete(queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean delUserTimelineById(Long itemId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(UserTimelineField.ITEMID, itemId));
            return userTimelineAccessor.delete(queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserTimeline getUserTimeLineById(Long itemid) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(UserTimelineField.ITEMID, itemid));
            return userTimelineAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
