
package com.enjoyf.platform.db.notice;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.notice.UserNotice;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractUserNoticeAccessor extends AbstractBaseTableAccessor<UserNotice> implements UserNoticeAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUserNoticeAccessor.class);

    private static final String KEY_TABLE_NAME = "user_notice";

    @Override
    public UserNotice insert(UserNotice userNotice, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, userNotice.getProfileId());
            pstmt.setString(2, userNotice.getAppkey());
            pstmt.setString(3, userNotice.getNoticeType());
            pstmt.setString(4, userNotice.getBody());
            pstmt.setTimestamp(5, new Timestamp(userNotice.getCreateTime().getTime()));
            pstmt.setString(6, userNotice.getDestId());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userNotice.setUserNoticeId(rs.getLong(1));
            }

        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return userNotice;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profile_id,appkey,notice_type,body,create_time,dest_id) VALUES (?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("UserNotice insert sql" + sql);
        }
        return sql;
    }

    @Override
    public UserNotice get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<UserNotice> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<UserNotice> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected UserNotice rsToObject(ResultSet rs) throws SQLException {

        UserNotice returnObject = new UserNotice();

        returnObject.setUserNoticeId(rs.getLong("user_notice_id"));
        returnObject.setProfileId(rs.getString("profile_id"));
        returnObject.setAppkey(rs.getString("appkey"));
        returnObject.setNoticeType(rs.getString("notice_type"));
        returnObject.setBody(rs.getString("body"));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setDestId(rs.getString("dest_id"));


        return returnObject;
    }
}