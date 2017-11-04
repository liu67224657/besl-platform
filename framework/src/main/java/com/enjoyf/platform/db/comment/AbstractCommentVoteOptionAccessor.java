package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.CommentVoteOption;
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
public abstract class AbstractCommentVoteOptionAccessor extends AbstractBaseTableAccessor<CommentVoteOption> implements CommentVoteOptionAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCommentVoteOptionAccessor.class);
    private static final String KEY_TABLE_NAME = "comment_vote_option";

    @Override
    public CommentVoteOption insert(CommentVoteOption commentVoteOption, Connection conn) throws DbException {
        PreparedStatement pstmt = null;


        try {
            pstmt = conn.prepareStatement(getInsertSql());

            pstmt.setString(1, commentVoteOption.getVoteOptionId());
            pstmt.setString(2, commentVoteOption.getCommentId());
            pstmt.setString(3, commentVoteOption.getTitle());
            pstmt.setString(4, commentVoteOption.getPic() == null ? "" : commentVoteOption.getPic());
            pstmt.setLong(5, commentVoteOption.getOptionTotal());
            pstmt.setString(6, commentVoteOption.getRemoveStatus() == null ? ValidStatus.INVALID.getCode() : commentVoteOption.getRemoveStatus());
            pstmt.setLong(7, commentVoteOption.getDisplayOrder());
            pstmt.setTimestamp(8, new Timestamp(commentVoteOption.getCreateTime() == null ? System.currentTimeMillis() : commentVoteOption.getCreateTime().getTime()));
            pstmt.setString(9,commentVoteOption.getCreateUser()==null?"":commentVoteOption.getCreateUser());

            pstmt.executeUpdate();


        } catch (SQLException e) {
            GAlerter.lab("On insert CommentVoteOption, occur SQLException.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return commentVoteOption;
    }

    @Override
    public int countCommentVoteOption(QueryExpress queryExpress, Connection conn) throws DbException {

        return queryRowSize(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    public CommentVoteOption get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<CommentVoteOption> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    public List<CommentVoteOption> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {

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
    protected CommentVoteOption rsToObject(ResultSet rs) throws SQLException {
        CommentVoteOption commentVoteOption = new CommentVoteOption();
        commentVoteOption.setVoteOptionId(rs.getString("vote_option_id"));
        commentVoteOption.setCommentId(rs.getString("comment_id"));
        commentVoteOption.setTitle(rs.getString("title"));
        commentVoteOption.setPic(rs.getString("pic"));
        commentVoteOption.setOptionTotal(rs.getLong("option_total"));
        commentVoteOption.setRemoveStatus(rs.getString("remove_status"));
        commentVoteOption.setDisplayOrder(rs.getLong("display_order"));
        commentVoteOption.setCreateTime(rs.getTimestamp("create_time"));
        commentVoteOption.setCreateUser(rs.getString("create_user"));

        return commentVoteOption;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(vote_option_id,comment_id,title,pic,option_total,remove_status,display_order,create_time,create_user) VALUES(?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractCommentVoteOption insertSql:" + sql);
        }
        return sql;
    }


}
