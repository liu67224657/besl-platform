package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.ProfileMobile;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/23
 * Description:
 */
public class AbstractProfileMobileAccessor extends AbstractBaseTableAccessor<ProfileMobile> implements ProfileMobileAccessor {

    private static final String KEY_TABLE_NAME = "user_profile_mobile";
    private static Logger logger = LoggerFactory.getLogger(AbstractProfileMobileAccessor.class);

    @Override
    public ProfileMobile insert(ProfileMobile profileMobile, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {

            profileMobile.setProfileMobileId(UserCenterUtil.getProfileMobileId(profileMobile.getMobile(), profileMobile.getProfileKey()));
            //profile_mobileid,profile_id,uno,uid,profilekey,mobile,createtime
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, profileMobile.getProfileMobileId());
            pstmt.setString(2, profileMobile.getProfileId());
            pstmt.setString(3, profileMobile.getUno());
            pstmt.setLong(4, profileMobile.getUid());
            pstmt.setString(5, profileMobile.getProfileKey());
            pstmt.setString(6, profileMobile.getMobile());
            pstmt.setTimestamp(7, new Timestamp(profileMobile.getCreateTime() == null ? System.currentTimeMillis() : profileMobile.getCreateTime().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return profileMobile;
    }

    @Override
    protected ProfileMobile rsToObject(ResultSet rs) throws SQLException {
        ProfileMobile profileMobile = new ProfileMobile();

        profileMobile.setProfileMobileId(rs.getString("profile_mobile_id"));
        profileMobile.setMobile(rs.getString("mobile"));
        profileMobile.setCreateTime(rs.getTimestamp("create_time"));
        profileMobile.setProfileId(rs.getString("profile_id"));
        profileMobile.setProfileKey(rs.getString("profilekey"));
        profileMobile.setUid(rs.getLong("uid"));
        profileMobile.setUno(rs.getString("uno"));
        return profileMobile;
    }

    @Override
    public ProfileMobile get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ProfileMobile> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profile_mobile_id,profile_id,uno,uid,profilekey,mobile,create_time) VALUES(?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("profile insert sql" + sql);
        }
        return sql;
    }
}
