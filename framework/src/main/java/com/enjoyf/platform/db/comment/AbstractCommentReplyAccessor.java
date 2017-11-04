package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentReply;
import com.enjoyf.platform.service.comment.ReplyBody;
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
 * User: zhitaoshi
 * Date: 14-11-14
 * Time: 上午11:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCommentReplyAccessor extends AbstractBaseTableAccessor<CommentReply> implements CommentReplyAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCommentReplyAccessor.class);
    private static final String KEY_TABLE_NAME = "comment_reply";

    @Override
    public CommentReply insert(CommentReply commentReply, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            //reply_uno,comment_id,parent_id,parent_uno,root_id," +
            //                "root_uno,sub_reply_sum,agree_sum,disagree_sum,reply_body,create_time,create_ip," +
            //                "remove_status,floor_num,total_rows,display_order,domain
            pstmt.setString(1, commentReply.getReplyUno());
            pstmt.setString(2, commentReply.getCommentId() == null ? "" : commentReply.getCommentId());
            pstmt.setLong(3, commentReply.getParentId());
            pstmt.setString(4, commentReply.getParentUno() == null ? "" : commentReply.getParentUno());
            pstmt.setLong(5, commentReply.getRootId());
            pstmt.setString(6, commentReply.getRootUno() == null ? "" : commentReply.getRootUno());
            pstmt.setInt(7, commentReply.getSubReplySum());
            pstmt.setInt(8, commentReply.getAgreeSum());
            pstmt.setInt(9, commentReply.getDisagreeSum());
            pstmt.setString(10, ReplyBody.toJson(commentReply.getBody()));
            pstmt.setTimestamp(11, new Timestamp(commentReply.getCreateTime() == null ? System.currentTimeMillis() : commentReply.getCreateTime().getTime()));
            pstmt.setString(12, commentReply.getCreateIp() == null ? "" : commentReply.getCreateIp());
            pstmt.setString(13, commentReply.getRemoveStatus() == null ? ActStatus.UNACT.getCode() : commentReply.getRemoveStatus().getCode());
            pstmt.setInt(14, commentReply.getFloorNum());
            pstmt.setInt(15, commentReply.getTotalRows());
            pstmt.setLong(16, commentReply.getDisplayOrder());
            pstmt.setInt(17, commentReply.getDomain() == null ? CommentDomain.DEFAULT.getCode() : commentReply.getDomain().getCode());
            pstmt.setString(18, commentReply.getReplyProfileId() == null ? "" : commentReply.getReplyProfileId());
            pstmt.setString(19, commentReply.getReplyProfileKey() == null ? "" : commentReply.getReplyProfileKey());
            pstmt.setString(20, commentReply.getParentProfileId() == null ? "" : commentReply.getParentProfileId());
            pstmt.setString(21, commentReply.getParentProfileKey() == null ? "" : commentReply.getParentProfileKey());
            pstmt.setString(22, commentReply.getRootProfileId() == null ? "" : commentReply.getRootProfileId());
            pstmt.setString(23, commentReply.getRootProfileKey() == null ? "" : commentReply.getRootProfileKey());
            pstmt.setInt(24, commentReply.getReplyAgreeSum());
            pstmt.setInt(25,commentReply.getCustomerStatus());
            pstmt.setString(26, commentReply.getSubKey() == null ? "" : commentReply.getSubKey());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                commentReply.setReplyId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("AbstractCommentReplyAccessor insert occur Exception.e", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return commentReply;
    }

    @Override
    public List<CommentReply> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<CommentReply> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
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
    public CommentReply get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int remove(String commentId, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM " + KEY_TABLE_NAME + " WHERE comment_id=?";
        if (logger.isDebugEnabled()) {
            logger.debug("The get sql:" + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, commentId);

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On get, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public int count(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.queryRowSize(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected CommentReply rsToObject(ResultSet rs) throws SQLException {
        CommentReply reply = new CommentReply();
        reply.setReplyId(rs.getLong("reply_id"));
        reply.setReplyUno(rs.getString("reply_uno"));
        reply.setCommentId(rs.getString("comment_id"));
        reply.setParentId(rs.getLong("parent_id"));
        reply.setParentUno(rs.getString("parent_uno"));
        reply.setRootId(rs.getLong("root_id"));
        reply.setRootUno(rs.getString("root_uno"));
        reply.setSubReplySum(rs.getInt("sub_reply_sum"));
        reply.setAgreeSum(rs.getInt("agree_sum"));
        reply.setDisagreeSum(rs.getInt("disagree_sum"));
        reply.setBody(ReplyBody.parse(rs.getString("reply_body")));
        reply.setCreateTime(rs.getTimestamp("create_time"));
        reply.setCreateIp(rs.getString("create_ip"));
        reply.setModifyTime(rs.getTimestamp("modify_time"));
        reply.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        reply.setFloorNum(rs.getInt("floor_num"));
        reply.setTotalRows(rs.getInt("total_rows"));
        reply.setDisplayOrder(rs.getLong("display_order"));
        reply.setDomain(CommentDomain.getByCode(rs.getInt("domain")));
        reply.setReplyProfileId(rs.getString("reply_profileid"));
        reply.setReplyProfileKey(rs.getString("reply_profilekey"));
        reply.setParentProfileId(rs.getString("parent_profileid"));
        reply.setParentProfileKey(rs.getString("parent_profilekey"));
        reply.setRootProfileId(rs.getString("root_profileid"));
        reply.setRootProfileKey(rs.getString("root_profilekey"));
        reply.setReplyAgreeSum(rs.getInt("reply_agree_sum"));
        reply.setCustomerStatus(rs.getInt("customer_status"));
        reply.setSubKey(rs.getString("subkey"));
        return reply;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(reply_uno,comment_id,parent_id,parent_uno,root_id," +
                "root_uno,sub_reply_sum,agree_sum,disagree_sum,reply_body,create_time,create_ip," +
                "remove_status,floor_num,total_rows,display_order,domain," +
                "reply_profileid,reply_profilekey,parent_profileid,parent_profilekey,root_profileid," +
                "root_profilekey,reply_agree_sum,customer_status,subkey) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractCommentReplyAccessor insert CommentReply sql:" + sql);
        }
        return sql;
    }
}
