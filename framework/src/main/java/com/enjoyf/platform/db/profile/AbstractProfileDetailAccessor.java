/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.profile;


import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.profile.ProfileDetail;
import com.enjoyf.platform.service.profile.ProfileDetailField;
import com.enjoyf.platform.service.profile.VerifyType;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
abstract class AbstractProfileDetailAccessor extends AbstractBaseTableAccessor<ProfileDetail> implements ProfileDetailAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractProfileDetailAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "PROFILE_DETAIL_";
    private static final int TABLE_NUM = 100;

    @Override
    public ProfileDetail insert(ProfileDetail profileDetail, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(profileDetail.getUno()));

            //UNO, REALNAME, SEX, BIRTHDAY, PROVINCEID, CITYID, MSN, QQ, INTEREST, COMPLETESTATUS
            pstmt.setString(1, profileDetail.getUno());
            pstmt.setString(2, profileDetail.getRealName());
            pstmt.setString(3, profileDetail.getSex());
            pstmt.setString(4, profileDetail.getBirthday());
            pstmt.setInt(5, profileDetail.getProvinceId());
            pstmt.setInt(6, profileDetail.getCityId());
            pstmt.setString(7, profileDetail.getMsn());
            pstmt.setString(8, profileDetail.getQq());
            pstmt.setString(9, profileDetail.interestToString());
            pstmt.setString(10, profileDetail.getCompleteStatus().getCode());
            pstmt.setString(11, profileDetail.getVerifyType().getCode());
            pstmt.setString(12, profileDetail.getVerifyDesc());

            pstmt.setTimestamp(13, profileDetail.getUpdateDate() != null ? new Timestamp(profileDetail.getUpdateDate().getTime()) : new Timestamp(System.currentTimeMillis()));

            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ProfileDetail, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return profileDetail;
    }

    @Override
    public boolean update(ProfileDetail profileDetail, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + getTableName(profileDetail.getUno()) + " SET REALNAME = ?, SEX = ?, BIRTHDAY = ?, PROVINCEID = ?, CITYID = ?, MSN = ?, QQ = ?, INTEREST = ?, COMPLETESTATUS = ?, VERIFYSTATUS = ?, VERIFYDESC = ? , UPDATEDATE = ? WHERE UNO = ?");

            pstmt.setString(1, profileDetail.getRealName());
            pstmt.setString(2, profileDetail.getSex());
            pstmt.setString(3, profileDetail.getBirthday());
            pstmt.setInt(4, profileDetail.getProvinceId());
            pstmt.setInt(5, profileDetail.getCityId());
            pstmt.setString(6, profileDetail.getMsn());
            pstmt.setString(7, profileDetail.getQq());
            pstmt.setString(8, profileDetail.interestToString());
            pstmt.setString(9, profileDetail.getCompleteStatus().getCode());
            pstmt.setString(10, profileDetail.getVerifyType().getCode());
            pstmt.setString(11, profileDetail.getVerifyDesc());

            pstmt.setTimestamp(8, profileDetail.getUpdateDate() != null ? new Timestamp(profileDetail.getUpdateDate().getTime()) : new Timestamp(System.currentTimeMillis()));

            pstmt.setString(12, profileDetail.getUno());
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update profileDetail, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean update(String uno, Map<ObjectField, Object> keyValues, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getUpdateScript(uno, keyValues));

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, keyValues);
            pstmt.setString(index, uno);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On update, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean update(UpdateExpress updateExpress, String uno, Connection conn) throws DbException {
        return super.update(getTableName(uno), updateExpress, new QueryExpress().add(QueryCriterions.eq(ProfileDetailField.UNO, uno)), conn) > 0;
    }


    @Override
    public ProfileDetail get(String uno, Connection conn) throws DbException {
        ProfileDetail returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(uno) + " WHERE UNO = ?");

            pstmt.setString(1, uno);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On select an ProfileDetail, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<ProfileDetail> queryList(String uno, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(getTableName(uno), queryExpress, conn);
    }


    ///private methods.
    private String getInsertSql(String uno) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(uno)
                + " (UNO, REALNAME, SEX, BIRTHDAY, PROVINCEID, CITYID, MSN, QQ, INTEREST, COMPLETESTATUS, VERIFYSTATUS, VERIFYDESC, UPDATEDATE)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileDetail INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected String getTableName(String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }

    protected ProfileDetail rsToObject(ResultSet rs) throws SQLException {
        ProfileDetail entry = new ProfileDetail();

        entry.setUno(rs.getString("UNO"));

        entry.setRealName(rs.getString("REALNAME"));

        entry.setBirthday(rs.getString("BIRTHDAY"));
        entry.setSex(rs.getString("SEX"));

        entry.setProvinceId(rs.getInt("PROVINCEID"));
        entry.setCityId(rs.getInt("CITYID"));

        entry.setMsn(rs.getString("MSN"));
        entry.setQq(rs.getString("QQ"));

        entry.addInterest(rs.getString("INTEREST"));

        entry.setCompleteStatus(ActStatus.getByCode(rs.getString("COMPLETESTATUS")));
        entry.setVerifyType(VerifyType.getByCode(rs.getString("VERIFYSTATUS")));
        entry.setVerifyDesc(rs.getString("VERIFYDESC"));

        return entry;
    }

    protected String getUpdateScript(String uno, Map<ObjectField, Object> keyValues) throws DbException {
        StringBuffer returnValue = new StringBuffer();

        returnValue.append("UPDATE ").append(getTableName(uno));

        if (keyValues != null && keyValues.size() > 0) {
            returnValue.append(" SET ").append(ObjectFieldUtil.generateMapSetClause(keyValues));
        }

        returnValue.append(" WHERE UNO = ?");

        return returnValue.toString();
    }
}
