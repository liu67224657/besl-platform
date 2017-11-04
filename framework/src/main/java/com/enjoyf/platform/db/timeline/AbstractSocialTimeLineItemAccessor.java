package com.enjoyf.platform.db.timeline;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.timeline.SocialTimeLineDomain;
import com.enjoyf.platform.service.timeline.SocialTimeLineItem;
import com.enjoyf.platform.service.timeline.SocialTimeLineItemField;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-15
 * Time: 上午9:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialTimeLineItemAccessor extends AbstractBaseTableAccessor<SocialTimeLineItem> implements SocialTimeLineItemAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractSocialTimeLineItemAccessor.class);

    private static final String TABLE_NAME = "social_timeline_";

    private static final int TABLE_NUM = 10;

    protected String getTableName(SocialTimeLineDomain domain, String ownUno) throws DbException {
        return TABLE_NAME + domain.getCode().toLowerCase() + "_" + TableUtil.getTableNumSuffix(ownUno.hashCode(), TABLE_NUM);
    }


    @Override
    public SocialTimeLineItem insert(SocialTimeLineItem socialTimeLineItem, Connection conn) throws DbException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected SocialTimeLineItem rsToObject(ResultSet rs) throws SQLException {
        SocialTimeLineItem socialTimeLineItem = new SocialTimeLineItem();
        socialTimeLineItem.setSid(rs.getLong("sid"));
        socialTimeLineItem.setProfileId(rs.getString("own_uno"));
        socialTimeLineItem.setDomain(SocialTimeLineDomain.getByCode(rs.getString("domain")));
        socialTimeLineItem.setDirectId(rs.getString("content_id"));
        socialTimeLineItem.setDirectProfileId(rs.getString("content_uno"));
        socialTimeLineItem.setCreateTime(rs.getTimestamp("create_time"));
        socialTimeLineItem.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        socialTimeLineItem.setRemoveTime(rs.getTimestamp("remove_time"));
        return socialTimeLineItem;
    }

    private String getInsertSql(SocialTimeLineDomain socialTimeLineDomain, String ownUno) throws DbException {
        String sql = "INSERT INTO " + getTableName(socialTimeLineDomain, ownUno) + "(own_uno,domain,content_id,content_uno,create_time,remove_status,remove_time)VALUES(?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("the insert sql script:" + sql);
        }
        return sql;
    }

    @Override
    public SocialTimeLineItem insertSocialTimeLineItem(SocialTimeLineDomain socialTimeLineDomain, String ownUno, SocialTimeLineItem item, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(socialTimeLineDomain, ownUno), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, item.getProfileId());
            pstmt.setString(2, item.getDomain().getCode());
            pstmt.setString(3, item.getDirectId());
            pstmt.setString(4, item.getDirectProfileId());

            pstmt.setTimestamp(5, new Timestamp(item.getCreateTime() == null ? System.currentTimeMillis() : item.getCreateTime().getTime()));
            pstmt.setString(6, item.getRemoveStatus().getCode());
            pstmt.setTimestamp(7, new Timestamp(item.getRemoveTime() == null ? System.currentTimeMillis() : item.getRemoveTime().getTime()));

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                item.setSid(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return item;
    }

    @Override
    public SocialTimeLineItem getBySId(SocialTimeLineDomain socialTimeLineDomain, String ownUno, long contentId, Connection connection) throws DbException {
        return super.get(getTableName(socialTimeLineDomain, ownUno), new QueryExpress().add(QueryCriterions.eq(SocialTimeLineItemField.SID, contentId)), connection);
    }

    @Override
    public int update(SocialTimeLineDomain socialTimeLineDomain, String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException {
        return super.update(getTableName(socialTimeLineDomain, ownUno), updateExpress, queryExpress, connection);
    }

    @Override
    public List<SocialTimeLineItem> query(SocialTimeLineDomain socialTimeLineDomain, String ownUno, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(getTableName(socialTimeLineDomain, ownUno), queryExpress, conn);
    }

    @Override
    public List<SocialTimeLineItem> queryByPageRows(SocialTimeLineDomain socialTimeLineDomain, String ownUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return super.query(getTableName(socialTimeLineDomain, ownUno), queryExpress, page, conn);
    }

    @Override
    public List<SocialTimeLineItem> queryNextRows(SocialTimeLineDomain domain, String uno, NextPagination nextPagination, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<SocialTimeLineItem> list = new LinkedList<SocialTimeLineItem>();

        String sql = "SELECT * FROM " + getTableName(domain, uno) + " WHERE remove_status='n' AND own_uno=? AND domain=? AND content_id" + ((nextPagination.isNext() ? "<" : ">")) + "? ORDER BY content_id DESC LIMIT 0, ?";
        if (nextPagination.getStartId() <= 0l) {
            sql = "SELECT * FROM " + getTableName(domain, uno) + " WHERE remove_status='n'  AND own_uno=? AND domain=? ORDER BY content_id DESC LIMIT 0, ?";
        }

        try {
            pstmt = conn.prepareStatement(sql);
            if (nextPagination.getStartId() <= 0) {
                pstmt.setString(1, uno);
                pstmt.setString(2, domain.getCode());
                pstmt.setInt(3, nextPagination.getPageSize());
            } else {
                pstmt.setString(1, uno);
                pstmt.setString(2, domain.getCode());
                pstmt.setInt(3, (int) nextPagination.getStartId());
                pstmt.setInt(4, nextPagination.getPageSize());
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(rsToObject(rs));
            }

//            if (!CollectionUtil.isEmpty(list)) {
//                nextPagination.setNextId(list.get(list.size() - 1).getContentId());
//            }
//            nextPagination.setQueryRowsNum(list.size());


            //判断是否是最后一页
            String lastContentid = " select content_id from " + getTableName(domain, uno) + " where own_uno= ? and remove_status='n' order by content_id asc limit 0,1";
            pstmt = conn.prepareStatement(lastContentid);
            pstmt.setString(1, uno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lastContentid = rs.getString("content_id");
                if (lastContentid.equals(nextPagination.getNextId()+"")) {
                    nextPagination.setLast(true);
                }else {
                    nextPagination.setLast(false);
                }
            }

        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return list;
    }
}
