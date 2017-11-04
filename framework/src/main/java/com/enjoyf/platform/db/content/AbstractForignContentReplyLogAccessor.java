package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.ForignContentReplyLog;
import com.enjoyf.platform.service.content.ForignContentReplyLogType;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-15 下午4:50
 * Description:
 */
public abstract class AbstractForignContentReplyLogAccessor extends AbstractBaseTableAccessor<ForignContentReplyLog> implements ForignContentReplyLogAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractForignContentReplyLogAccessor.class);

    private static final String TABLE_NAME = "forign_content_reply_log";

    @Override
    public ForignContentReplyLog insert(ForignContentReplyLog replyLog, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            //log_id,reply_id,content_id, agree_uno,create_date,create_ip
            pstmt.setLong(1, replyLog.getReplyId());
            pstmt.setLong(2, replyLog.getContentId());
            pstmt.setString(3, replyLog.getUno());
            pstmt.setInt(4, replyLog.getLogType().getCode());
            pstmt.setTimestamp(5, new Timestamp(replyLog.getCreateDate() == null ? System.currentTimeMillis() : replyLog.getCreateDate().getTime()));
            pstmt.setString(6, replyLog.getCreateIp());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                replyLog.setLogId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return replyLog;
    }

    @Override
    protected ForignContentReplyLog rsToObject(ResultSet rs) throws SQLException {
        ForignContentReplyLog replyLog = new ForignContentReplyLog();
        replyLog.setLogId(rs.getLong("log_id"));
        replyLog.setReplyId(rs.getLong("reply_id"));
        replyLog.setUno(rs.getString("uno"));
        replyLog.setContentId(rs.getLong("content_id"));
        replyLog.setLogType(ForignContentReplyLogType.getByCode(rs.getInt("log_type")));
        replyLog.setCreateDate(rs.getTimestamp("create_date"));
        replyLog.setCreateIp(rs.getString("create_ip"));
        return replyLog;
    }

    @Override
    public ForignContentReplyLog get(QueryExpress getExpress, Connection connection) throws DbException {
        return super.get(TABLE_NAME, getExpress, connection);
    }

    @Override
    public List<ForignContentReplyLog> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    public String getInsertSql() {
        String sql = "INSERT INTO "+TABLE_NAME+" (reply_id,content_id,uno,log_type,create_date,create_ip) VALUES (?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug(" the insert sql:" + sql);
        }
        return sql;
    }

}
