package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ask.CollectType;
import com.enjoyf.platform.service.ask.UserCollect;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractUserCollectAccessor extends AbstractBaseTableAccessor<UserCollect> implements UserCollectAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUserCollectAccessor.class);

    private static final String KEY_TABLE_NAME = "user_collect";

    @Override
    public UserCollect insert(UserCollect userCollect, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, userCollect.getProfileId());
            pstmt.setInt(2, userCollect.getCollectType().getCode());
            pstmt.setTimestamp(3, new Timestamp(userCollect.getCreateDate().getTime()));
            pstmt.setString(4, userCollect.getAppkey());
            pstmt.setLong(5, userCollect.getContentId());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userCollect.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return userCollect;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profile_id,collect_type,create_time,appkey,content_id) VALUES (?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("UserCollect insert sql" + sql);
        }
        return sql;
    }

    @Override
    public UserCollect get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<UserCollect> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<UserCollect> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected UserCollect rsToObject(ResultSet rs) throws SQLException {
        UserCollect returnObject = new UserCollect();

        returnObject.setId(rs.getLong("id"));
        returnObject.setProfileId(rs.getString("profile_id"));
        returnObject.setCollectType(CollectType.getByCode(rs.getInt("collect_type")));
        returnObject.setCreateDate(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setAppkey(rs.getString("appkey"));
        returnObject.setContentId(rs.getLong("content_id"));

        return returnObject;
    }
}