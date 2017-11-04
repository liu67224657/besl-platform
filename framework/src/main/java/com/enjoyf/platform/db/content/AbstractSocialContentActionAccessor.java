package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.social.SocialContentAction;
import com.enjoyf.platform.service.content.social.SocialContentActionType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-15 下午1:57
 * Description:
 */
public abstract class AbstractSocialContentActionAccessor extends AbstractBaseTableAccessor<SocialContentAction> implements SocialContentActionAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialContentReplyAccessor.class);

    private static final String TABLE_NAME = "social_content_action";

    @Override
    public SocialContentAction insert(SocialContentAction action, Connection conn) throws DbException {


        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, action.getUno());
            pstmt.setLong(2, action.getContentId());
            pstmt.setString(3, action.getContentUno());

            pstmt.setInt(4, action.getType().getCode());

            pstmt.setTimestamp(5, new Timestamp(action.getCreateTime() == null ? System.currentTimeMillis() : action.getCreateTime().getTime()));
            pstmt.setString(6, action.getCreateIp());

            pstmt.setString(7, action.getRemoveStatus().getCode());

            pstmt.setFloat(8, action.getLon());
            pstmt.setFloat(9, action.getLat());

            pstmt.execute();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                action.setActionId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return action;
    }

    //
    private String getInsertSql() {
        String sql = "INSERT INTO " + TABLE_NAME + " (action_uno,content_id,content_uno,action_type,create_time,create_ip,remove_status,lat,lon) VALUES (?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug(" the insert sql:" + sql);
        }

        return sql;
    }

    @Override
    public List<SocialContentAction> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public SocialContentAction get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialContentAction> query(long contentId, SocialContentActionType type, NextPagination nextPagination, Connection conn) throws DbException {

        List<SocialContentAction> returnValue = new ArrayList<SocialContentAction>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
       // String sql = "SELECT * FROM " + TABLE_NAME + " WHERE content_id=? AND action_type= ? AND remove_status=? AND action_id" + (nextPagination.isNext() ? "< ? ORDER BY create_time DESC" : "> ? ORDER BY create_time ASC") + "  LIMIT 0, ?";
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE content_id=? AND action_type= ? AND remove_status=? AND create_time<(SELECT create_time FROM social_content_action WHERE action_id=? ) ORDER BY create_time DESC  LIMIT 0, ?";
        if (nextPagination.getStartId() <= 0l) {
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE content_id=? AND action_type= ? AND remove_status=? ORDER BY create_time DESC  LIMIT 0, ?";
        }

        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            if (nextPagination.getStartId() <= 0l) {
                pstmt.setLong(1, contentId);
                pstmt.setInt(2, type.getCode());
                pstmt.setString(3, ActStatus.UNACT.getCode());
                pstmt.setInt(4, nextPagination.getPageSize());
            } else {
                pstmt.setLong(1, contentId);
                pstmt.setInt(2, type.getCode());
                pstmt.setString(3, ActStatus.UNACT.getCode());
                pstmt.setLong(4, nextPagination.getStartId());
                pstmt.setInt(5, nextPagination.getPageSize());
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                SocialContentAction action = rsToObject(rs);
                returnValue.add(action);
            }

            if (!CollectionUtil.isEmpty(returnValue)) {
                nextPagination.setNextId(returnValue.get(returnValue.size() - 1).getActionId());
            }

            //判断是否是最后一页
            String lastContentid = " select action_id from " + TABLE_NAME + " WHERE content_id=? AND action_type= ? AND remove_status=? ORDER BY create_time ASC  LIMIT 0, 1";
            pstmt = conn.prepareStatement(lastContentid);
            pstmt.setLong(1, contentId);
            pstmt.setInt(2, type.getCode());
            pstmt.setString(3, ActStatus.UNACT.getCode());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lastContentid = rs.getString("action_id");
                if (lastContentid.equals(nextPagination.getNextId() + "")) {
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
        return returnValue;


    }

    @Override
    protected SocialContentAction rsToObject(ResultSet rs) throws SQLException {
        SocialContentAction action = new SocialContentAction();
        action.setActionId(rs.getLong("action_id"));
        action.setUno(rs.getString("action_uno"));
        action.setContentId(rs.getLong("content_id"));
        action.setContentUno(rs.getString("content_uno"));
        action.setCreateIp(rs.getString("create_ip"));
        action.setCreateTime(rs.getTimestamp("create_time"));
        action.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        action.setType(SocialContentActionType.getByCode(rs.getInt("action_type")));
        action.setLon(rs.getFloat("lon"));
        action.setLat(rs.getFloat("lat"));

        return action;
    }

}
