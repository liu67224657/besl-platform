package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.comment.CommentForbid;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Diao
 * Date: 14-12-31
 * Time: 下午5:24
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCommentForbidAccessor extends AbstractBaseTableAccessor<CommentForbid> implements CommentForbidAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCommentForbidAccessor.class);
    private static final String KEY_TABLE_NAME = "comment_forbid";

    @Override
    public CommentForbid insert(CommentForbid commentForbid, Connection conn) throws DbException {
        PreparedStatement pstmt = null;


        try {
            pstmt = conn.prepareStatement(getInsertSql());

            pstmt.setString(1, commentForbid.getProfileid());
            pstmt.setTimestamp(2, new Timestamp(commentForbid.getStartTime() == null ? System.currentTimeMillis() : commentForbid.getStartTime().getTime()));
            pstmt.setLong(3, commentForbid.getLength());

            pstmt.executeUpdate();


        } catch (SQLException e) {
            GAlerter.lab("On insert commentForbid, occur SQLException.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return commentForbid;
    }

    @Override
    public CommentForbid get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<CommentForbid> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    public List<CommentForbid> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {

        return super.query(KEY_TABLE_NAME, queryExpress, page, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected CommentForbid rsToObject(ResultSet rs) throws SQLException {
        CommentForbid commentForbid = new CommentForbid();
        commentForbid.setProfileid(rs.getString("profileid"));
        commentForbid.setStartTime(rs.getTimestamp("start_time"));
        commentForbid.setLength(rs.getLong("length"));


        return commentForbid;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profileid,start_time,length) VALUES(?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractCommentHistory insertSql:" + sql);
        }
        return sql;
    }


}
