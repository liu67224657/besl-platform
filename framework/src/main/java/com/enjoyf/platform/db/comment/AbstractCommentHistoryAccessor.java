package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentHistory;
import com.enjoyf.platform.service.comment.CommentHistoryType;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-19
 * Time: 下午5:24
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCommentHistoryAccessor extends AbstractBaseTableAccessor<CommentHistory> implements CommentHistoryAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCommentHistoryAccessor.class);
    private static final String KEY_TABLE_NAME = "comment_history";

    @Override
    public CommentHistory insert(CommentHistory commentHistory, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            //profileid,object_id,action_times,action_date,action_type,action_ip,domain
            pstmt.setString(1, commentHistory.getProfileId() == null ? "" : commentHistory.getProfileId());
            pstmt.setString(2, commentHistory.getObjectId() == null ? "" : commentHistory.getObjectId());
            pstmt.setInt(3, commentHistory.getActionTimes());
            pstmt.setTimestamp(4, new Timestamp(commentHistory.getActionDate() == null ? System.currentTimeMillis() : commentHistory.getActionDate().getTime()));
            pstmt.setInt(5, commentHistory.getHistoryType() == null ? 0 : commentHistory.getHistoryType().getCode());
            pstmt.setString(6, commentHistory.getActionIp() == null ? "" : commentHistory.getActionIp());
            pstmt.setInt(7, commentHistory.getDomain() == null ? 0 : commentHistory.getDomain().getCode());
            pstmt.setString(8, commentHistory.getCommentId() == null ? "" : commentHistory.getCommentId());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                commentHistory.setHistoryId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert CommentBean, occur SQLException.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return commentHistory;
    }

    @Override
    public CommentHistory get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<CommentHistory> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected CommentHistory rsToObject(ResultSet rs) throws SQLException {
        CommentHistory history = new CommentHistory();
        history.setHistoryId(rs.getLong("history_id"));
        history.setObjectId(rs.getString("object_id"));
        history.setActionTimes(rs.getInt("action_times"));
        history.setActionDate(rs.getTimestamp("action_date"));
        history.setActionIp(rs.getString("action_ip"));
        history.setDomain(CommentDomain.getByCode(rs.getInt("domain")));
        history.setProfileId(rs.getString("profileid"));
        history.setHistoryType(CommentHistoryType.getByCode(rs.getInt("action_type")));
        history.setCommentId(rs.getString("comment_id"));
        return history;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profileid,object_id,action_times,action_date,action_type,action_ip,domain,comment_id) VALUES(?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractCommentHistory insertSql:" + sql);
        }
        return sql;
    }
}
