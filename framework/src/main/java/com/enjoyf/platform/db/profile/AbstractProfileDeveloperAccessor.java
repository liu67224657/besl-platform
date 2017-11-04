package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.profile.DeveloperLocation;
import com.enjoyf.platform.service.profile.DeveloperCategory;
import com.enjoyf.platform.service.profile.ProfileDeveloper;
import com.enjoyf.platform.service.profile.VerifyStatus;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Author: zhaoxin
 * Date: 11-8-26
 * Time: 下午4:31
 * Desc:
 */
public abstract class AbstractProfileDeveloperAccessor extends AbstractBaseTableAccessor<ProfileDeveloper> implements ProfileDeveloperAccessor {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    private static final String KEY_TABLE_NAME = "profile_developer";

    @Override
    public ProfileDeveloper insert(ProfileDeveloper profileDeveloper, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            pstmt.setString(1, profileDeveloper.getUno());
            pstmt.setString(2, profileDeveloper.getContacts());
            pstmt.setString(3, profileDeveloper.getVerifyDesc());
            pstmt.setString(4, profileDeveloper.getEmail());
            pstmt.setString(5, profileDeveloper.getQq());
            pstmt.setString(6, profileDeveloper.getPhone());
            pstmt.setString(7, profileDeveloper.getCompany());
            pstmt.setString(8, profileDeveloper.getLicensePic());
            pstmt.setString(9, JsonBinder.buildNormalBinder().toJson(profileDeveloper.getLocation()));
            pstmt.setInt(10, profileDeveloper.getCategory().getCode());
            pstmt.setString(11, profileDeveloper.getVerifyStatus().getCode());
            pstmt.setTimestamp(12, new Timestamp(profileDeveloper.getCreateDate() == null ? System.currentTimeMillis() : profileDeveloper.getCreateDate().getTime()));
            pstmt.setString(13, profileDeveloper.getCreateIp());
            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ProfileBlog, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return profileDeveloper;
    }

    @Override
    public ProfileDeveloper get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ProfileDeveloper> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ProfileDeveloper> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    protected ProfileDeveloper rsToObject(ResultSet rs) throws SQLException {
        ProfileDeveloper profileDeveloper = new ProfileDeveloper();

        profileDeveloper.setUno(rs.getString("uno"));
        profileDeveloper.setContacts(rs.getString("contacts"));
        profileDeveloper.setVerifyDesc(rs.getString("verify_desc"));
        profileDeveloper.setEmail(rs.getString("email"));
        profileDeveloper.setQq(rs.getString("qq"));
        profileDeveloper.setPhone(rs.getString("phone"));
        profileDeveloper.setCompany(rs.getString("company"));
        profileDeveloper.setLicensePic(rs.getString("license_pic"));
        profileDeveloper.setLocation(DeveloperLocation.fromJson(rs.getString("location")));
        profileDeveloper.setCategory(DeveloperCategory.getByCode(rs.getInt("category")));
        profileDeveloper.setVerifyStatus(VerifyStatus.getByCode(rs.getString("status")));
        profileDeveloper.setCreateDate(rs.getTimestamp("create_date"));
        profileDeveloper.setCreateIp(rs.getString("create_ip"));
        profileDeveloper.setModifyDate(rs.getTimestamp("modify_date"));
        profileDeveloper.setModifyIp(rs.getString("modify_ip"));
        profileDeveloper.setVerifyDate(rs.getTimestamp("verify_date"));
        profileDeveloper.setVerifyIp(rs.getString("verify_ip"));
        profileDeveloper.setVerifyReason(rs.getString("reason"));
        return profileDeveloper;
    }

    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME  + " (uno, contacts, verify_desc, email, " +
                "qq, phone, company, license_pic, location, category, status, create_date, " +
                "create_ip)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileDeveloper INSERT Script:" + insertSql);
        }
        return insertSql;
    }

}
