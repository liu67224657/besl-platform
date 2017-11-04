package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.UserPrivacy;
import com.enjoyf.platform.service.usercenter.UserPrivacyFunction;
import com.enjoyf.platform.service.usercenter.UserPrivacyPrivacyAlarm;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public abstract class AbstractUserPrivacyAccessor extends AbstractBaseTableAccessor<UserPrivacy> implements UserPrivacyAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUserPrivacyAccessor.class);

    private static final String KEY_TABLE_NAME = "user_privacy";

    @Override
    public UserPrivacy insert(UserPrivacy userPrivacy, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, userPrivacy.getProfileId());
            pstmt.setString(2, userPrivacy.getAlarmSetting().toJson());
            pstmt.setString(3, userPrivacy.getFunctionSetting().toJson());
            pstmt.setTimestamp(4, new Timestamp(userPrivacy.getCreatetime().getTime()));
            pstmt.setString(5, userPrivacy.getCreateip());
            pstmt.setTimestamp(6, new Timestamp(userPrivacy.getUpdatetime().getTime()));
            pstmt.setString(7, userPrivacy.getUpdateip());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return userPrivacy;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profile_id,alarm_setting,function_setting,createtime,createip,updatetime,updateip) VALUES (?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("UserPrivacy insert sql" + sql);
        }
        return sql;
    }

    @Override
    public UserPrivacy get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
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
    protected UserPrivacy rsToObject(ResultSet rs) throws SQLException {

        UserPrivacy returnObject = new UserPrivacy();

        returnObject.setProfileId(rs.getString("profile_id"));
        returnObject.setAlarmSetting(UserPrivacyPrivacyAlarm.getByJson(rs.getString("alarm_setting")));
        returnObject.setFunctionSetting(UserPrivacyFunction.getByJson(rs.getString("function_setting")));
        returnObject.setCreatetime(rs.getTimestamp("createtime"));
        returnObject.setCreateip(rs.getString("createip"));
        returnObject.setUpdatetime(rs.getTimestamp("updatetime"));
        returnObject.setUpdateip(rs.getString("updateip"));


        return returnObject;
    }
}