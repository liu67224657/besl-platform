package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityActionType;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityObjectType;
import com.enjoyf.platform.service.usercenter.activityuser.ActivityUser;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/10
 * Description:
 */
public abstract class AbstractActivityUserAccessor extends AbstractBaseTableAccessor<ActivityUser> implements ActivityUserAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractActivityUserAccessor.class);

    private static final String KEY_TABLE_NAME = "activity_user";

    @Override
    public ActivityUser insert(ActivityUser user, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            //activity_userid,appkey,subkey,profileid,uno,action_type,object_type,object_id,action_time
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, user.getActivityUserId());
            pstmt.setString(2, user.getAppkey());
            pstmt.setString(3, user.getSubkey());
            pstmt.setString(4, user.getProfileId());
            pstmt.setString(5, user.getUno());
            pstmt.setInt(6, user.getActionType().getCode());
            pstmt.setInt(7, user.getObjectType().getCode());
            pstmt.setString(8, user.getObjectId());
            pstmt.setTimestamp(9, new Timestamp(user.getActionTime() == null ? System.currentTimeMillis() : user.getActionTime().getTime()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return user;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(activity_user_id,appkey,subkey,profileid,uno,action_type,object_type,object_id,action_time) VALUES(?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("ActivityUser insert sql" + sql);
        }
        return sql;
    }

    @Override
    public ActivityUser get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<ActivityUser> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ActivityUser> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }


    @Override
    protected ActivityUser rsToObject(ResultSet rs) throws SQLException {

        //activity_userid,appkey,subkey,profileid,uno,action_type,object_type,object_id,action_time
        ActivityUser activityUser = new ActivityUser();

        activityUser.setActivityUserId(rs.getString("activity_user_id"));
        activityUser.setActionTime(new Date(rs.getTimestamp("action_time").getTime()));
        activityUser.setActionType(ActivityActionType.getByCode(rs.getInt("action_type")));
        activityUser.setAppkey(rs.getString("appkey"));
        activityUser.setSubkey(rs.getString("subkey"));
        activityUser.setObjectId(rs.getString("object_id"));
        activityUser.setObjectType(ActivityObjectType.getByCode(rs.getInt("object_type")));
        activityUser.setProfileId(rs.getString("profileid"));
        activityUser.setUno(rs.getString("uno"));

        return activityUser;
    }
}
