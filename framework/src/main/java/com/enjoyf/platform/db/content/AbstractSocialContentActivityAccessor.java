package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.social.SocialContentActivity;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-12
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialContentActivityAccessor extends AbstractBaseTableAccessor<SocialContentActivity> implements SocialContentActivityAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialContentActivityAccessor.class);
    private static final String KEY_TABLE_NAME = "social_content_activity";

    @Override
    public SocialContentActivity insert(SocialContentActivity socialContentActivity, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setLong(1, socialContentActivity.getContentId());
            pstmt.setString(2, socialContentActivity.getContentUno());
            pstmt.setLong(3, socialContentActivity.getActivityId());
            pstmt.setString(4, socialContentActivity.getRemoveStatus() == null ? ValidStatus.VALID.getCode() : socialContentActivity.getRemoveStatus().getCode());
            pstmt.setTimestamp(5, new Timestamp(socialContentActivity.getCreateDate() == null ? System.currentTimeMillis() : socialContentActivity.getCreateDate().getTime()));
            pstmt.setString(6, socialContentActivity.getCreateIp());
            pstmt.setString(7, socialContentActivity.getCreateUserId());
            pstmt.setLong(8, socialContentActivity.getDisplayOrder());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                socialContentActivity.setContentActivityId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert SocialActivity, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return socialContentActivity;
    }

    @Override
    public SocialContentActivity get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialContentActivity> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialContentActivity> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public long getMinDisplayOrder(Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT display_order FROM " + KEY_TABLE_NAME + " WHERE display_order>=0 ORDER BY display_order ASC LIMIT 0,1 FOR UPDATE";
        if (logger.isDebugEnabled()) {
            logger.debug("The get sql:" + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On get, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return 0l;
    }

    @Override
    public List<SocialContentActivity> query(long activityId, NextPagination nextPagination, Connection conn) throws DbException {
        List<SocialContentActivity> list = new ArrayList<SocialContentActivity>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + KEY_TABLE_NAME + " WHERE activity_id=? AND remove_status=? AND display_order" + (nextPagination.isNext() ? ">" : "<") + "? ORDER BY display_order ASC" + "  LIMIT 0, ?";
        if (nextPagination.getStartId() <= 0l) {
            sql = "SELECT * FROM " + KEY_TABLE_NAME + " WHERE activity_id=? AND remove_status=? ORDER BY display_order ASC  LIMIT 0, ?";
        }
        String maxSql = "SELECT display_order FROM " + KEY_TABLE_NAME + " WHERE activity_id=? AND remove_status=? ORDER BY display_order DESC LIMIT 0,1";
        long maxOrder = 0l;
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            if (nextPagination.getStartId() <= 0l) {
                pstmt.setLong(1, activityId);
                pstmt.setString(2, ValidStatus.VALID.getCode());
                pstmt.setInt(3, nextPagination.getPageSize());
            } else {
                pstmt.setLong(1, activityId);
                pstmt.setString(2, ValidStatus.VALID.getCode());
                pstmt.setLong(3, nextPagination.getStartId());
                pstmt.setInt(4, nextPagination.getPageSize());
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rsToObject(rs));
            }

            if (!CollectionUtil.isEmpty(list)) {
                nextPagination.setNextId(list.get(list.size() - 1).getDisplayOrder());
            }

            pstmt = conn.prepareStatement(maxSql);
            pstmt.setLong(1, activityId);
            pstmt.setString(2, ValidStatus.VALID.getCode());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                maxOrder = rs.getLong("display_order");
            }

            nextPagination.setQueryRowsNum(list.size());

            if (!CollectionUtil.isEmpty(list)) {
                if ((list.get(list.size() - 1).getDisplayOrder()) == maxOrder) {
                    nextPagination.setLast(true);
                } else {
                    nextPagination.setLast(false);
                }
            } else {
                nextPagination.setLast(true);
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

    @Override
    protected SocialContentActivity rsToObject(ResultSet rs) throws SQLException {
        SocialContentActivity socialContentActivity = new SocialContentActivity();
        socialContentActivity.setContentActivityId(rs.getLong("sca_id"));
        socialContentActivity.setContentId(rs.getLong("content_id"));
        socialContentActivity.setContentUno(rs.getString("content_uno"));
        socialContentActivity.setActivityId(rs.getLong("activity_id"));
        socialContentActivity.setRemoveStatus(ValidStatus.getByCode(rs.getString("remove_status")));
        socialContentActivity.setCreateDate(rs.getTimestamp("create_date"));
        socialContentActivity.setCreateIp(rs.getString("create_ip"));
        socialContentActivity.setCreateUserId(rs.getString("create_userid"));
        socialContentActivity.setLastModifyDate(rs.getTimestamp("modify_date"));
        socialContentActivity.setLastModifyIp(rs.getString("modify_ip"));
        socialContentActivity.setLastModifyUserId(rs.getString("modify_userid"));
        socialContentActivity.setDisplayOrder(rs.getLong("display_order"));
        return socialContentActivity;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(content_id,content_uno,activity_id,remove_status,create_date,create_ip,create_userid,display_order) VALUES(?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("SocialContentActivity insert sql:" + sql);
        }
        return sql;
    }
}
