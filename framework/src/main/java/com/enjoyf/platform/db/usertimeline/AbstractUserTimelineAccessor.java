package com.enjoyf.platform.db.usertimeline;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.timeline.TimeLineActionType;
import com.enjoyf.platform.service.timeline.UserTimeline;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public abstract class AbstractUserTimelineAccessor extends AbstractBaseTableAccessor<UserTimeline> implements UserTimelineAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUserTimelineAccessor.class);

    private static final String KEY_TABLE_NAME = "user_timeline";

    @Override
    public UserTimeline insert(UserTimeline userTimeline, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, userTimeline.getProfileId());
            pstmt.setString(2, userTimeline.getDomain());
            pstmt.setString(3, userTimeline.getType());
            pstmt.setString(4, userTimeline.getDestProfileid());
            pstmt.setObject(5, userTimeline.getDestId());
            pstmt.setString(6, userTimeline.getExtendBody());
            pstmt.setString(7, userTimeline.getActionType().getCode());
            pstmt.setString(8, userTimeline.getLinekey());
            pstmt.setObject(9, userTimeline.getCreateTime());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userTimeline.setItemId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return userTimeline;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profile_id,domain,type,dest_profileid,dest_id,extend_body,action_type,linekey,create_time) VALUES (?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("UserTimeline insert sql" + sql);
        }
        return sql;
    }

    @Override
    public UserTimeline get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<UserTimeline> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<UserTimeline> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected UserTimeline rsToObject(ResultSet rs) throws SQLException {

        UserTimeline returnObject = new UserTimeline();

        returnObject.setItemId(rs.getLong("item_id"));
        returnObject.setProfileId(rs.getString("profile_id"));
        returnObject.setDomain(rs.getString("domain"));
        returnObject.setType(rs.getString("type"));
        returnObject.setDestProfileid(rs.getString("dest_profileid"));
        returnObject.setDestId(rs.getLong("dest_id"));
        returnObject.setExtendBody(rs.getString("extend_body"));
        returnObject.setActionType(TimeLineActionType.getByCode(rs.getString("action_type")));
        returnObject.setLinekey(rs.getString("linekey"));
        returnObject.setCreateTime(rs.getTimestamp("create_time"));


        return returnObject;
    }
}