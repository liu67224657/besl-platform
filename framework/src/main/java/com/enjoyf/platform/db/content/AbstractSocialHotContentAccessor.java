package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.social.SocialHotContent;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
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
 * Date: 14-4-14
 * Time: 下午4:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialHotContentAccessor extends AbstractBaseTableAccessor<SocialHotContent> implements SocialHotContentAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractForignContentAccessor.class);

    private static final String TABLE_NAME = "social_content_hot";

    @Override
    public SocialHotContent insert(SocialHotContent socialHotContent, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            pstmt.setLong(1, socialHotContent.getContentId());
            pstmt.setString(2, socialHotContent.getUno());
            pstmt.setInt(3, socialHotContent.getDisplayOrder());
            pstmt.setTimestamp(4, new Timestamp(socialHotContent.getCreateDate() == null ? System.currentTimeMillis() : socialHotContent.getCreateDate().getTime()));
            if (socialHotContent.getCreateIp() == null) {
                pstmt.setNull(5, Types.VARCHAR);
            } else {
                pstmt.setString(5, socialHotContent.getCreateIp());
            }
            pstmt.setString(6, socialHotContent.getRemoveStatus().getCode());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return socialHotContent;
    }

    @Override
    public SocialHotContent get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + TABLE_NAME + "(content_id,uno,display_order,create_date,create_ip,act_status)VALUES(?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("the insert sql script:" + sql);
        }
        return sql;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, connection);
    }

    @Override
    public int getMaxValue(Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        int maxValue = 0;
        String sql = "SELECT display_order FROM " + TABLE_NAME + " ORDER BY display_order DESC LIMIT 0,1 FOR UPDATE";
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                maxValue = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return maxValue;
    }

    @Override
    public List<SocialHotContent> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialHotContent> query(NextPagination pagination, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<SocialHotContent> list = new LinkedList<SocialHotContent>();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE act_status='n' AND display_order" + ((pagination.isNext() ? "<" : ">")) + "? ORDER BY display_order DESC" + "  LIMIT 0, ?";
        if (pagination.getStartId() <= 0l) {
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE act_status='n' ORDER BY display_order DESC  LIMIT 0, ?";
        }

        String minSql = "SELECT display_order FROM " + TABLE_NAME + " WHERE act_status='n' ORDER BY display_order ASC LIMIT 0,1";
        int minOrder = 0;
        try {
            pstmt = conn.prepareStatement(sql);
            if (pagination.getStartId() <= 0) {
                pstmt.setInt(1, pagination.getPageSize());
            } else {
                pstmt.setInt(1, (int) pagination.getStartId());
                pstmt.setInt(2, pagination.getPageSize());
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(rsToObject(rs));
            }

            if (!CollectionUtil.isEmpty(list)) {
                pagination.setNextId(list.get(list.size() - 1).getDisplayOrder());
            }

            pstmt = conn.prepareStatement(minSql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                minOrder = rs.getInt("display_order");
            }

            pagination.setQueryRowsNum(list.size());

            if ((list.get(list.size() - 1).getDisplayOrder()) == minOrder) {
                pagination.setLast(true);
            } else {
                pagination.setLast(false);
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
    public List<SocialHotContent> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected SocialHotContent rsToObject(ResultSet rs) throws SQLException {
        SocialHotContent socialHotContent = new SocialHotContent();
        socialHotContent.setContentId(rs.getLong("content_id"));
        socialHotContent.setDisplayOrder(rs.getInt("display_order"));
        socialHotContent.setUno(rs.getString("uno"));
        socialHotContent.setCreateDate(rs.getTimestamp("create_date"));
        socialHotContent.setCreateIp(rs.getString("create_ip"));
        socialHotContent.setModifyDate(rs.getTimestamp("modify_date"));
        socialHotContent.setModifyIp(rs.getString("modify_ip"));
        socialHotContent.setRemoveStatus(ActStatus.getByCode(rs.getString("act_status")));
        return socialHotContent;
    }
}
