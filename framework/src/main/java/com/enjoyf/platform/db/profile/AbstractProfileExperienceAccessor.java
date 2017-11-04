package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.profile.ProfileExperience;
import com.enjoyf.platform.service.profile.ProfileExperienceType;
import com.enjoyf.platform.service.tools.AuditStatus;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-16
 * Time: 下午2:40
 * To change this template use File | Settings | File Templates.
 */
public class AbstractProfileExperienceAccessor implements ProfileExperienceAccessor {

    private final Logger logger = LoggerFactory.getLogger(AbstractProfileExperienceAccessor.class);
    //
    private static final String KEY_TABLE_NAME_PREFIX = "PROFILE_EXPERIENCE_";
    private static final String KEY_SEQUENCE_NAME = "SEQ_PROFILE_EXPERIENCE_ID";
    private static final int TABLE_NUM = 100;

    @Override
    public List<ProfileExperience> insert(List<ProfileExperience> list, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        List<ProfileExperience> rtList = new ArrayList<ProfileExperience>();
        try {
            for (ProfileExperience entity : list) {
                entity.setExpid(getSeqNo(conn));
                pstmt = conn.prepareStatement(getInsertSql(entity.getUno()));

                //EXPID, UNO, EXPNAME, EXPTYPE, METAINFO, EXP_STARTDATE, EXP_ENDDATE, CREATEDATE, UPDATEDATE, AUDITSTATUS, AUDITDATE
                pstmt.setLong(1, entity.getExpid());
                pstmt.setString(2, entity.getUno());

                pstmt.setString(3, entity.getExpName());
                pstmt.setString(4, entity.getExperienceType().getCode());
                pstmt.setString(5, entity.getMetaInfo());

                pstmt.setTimestamp(6, entity.getExpStartDate() != null ? new Timestamp(entity.getExpStartDate().getTime()) : new Timestamp(System.currentTimeMillis()));
                pstmt.setTimestamp(7, entity.getExpEndDate() != null ? new Timestamp(entity.getExpEndDate().getTime()) : new Timestamp(System.currentTimeMillis()));

                pstmt.setTimestamp(8, entity.getCreateDate() != null ? new Timestamp(entity.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
                pstmt.setTimestamp(9, entity.getUpdateDate() != null ? new Timestamp(entity.getUpdateDate().getTime()) : new Timestamp(System.currentTimeMillis()));

                pstmt.setInt(10, entity.getAuditStatus().getValue());
                pstmt.setTimestamp(11, entity.getAuditDate() != null ? new Timestamp(entity.getAuditDate().getTime()) : null);

                pstmt.executeUpdate();
                rtList.add(entity);
            }

        } catch (SQLException e) {
            GAlerter.lab("On insert ProfileExperience, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return rtList;
    }

    @Override
    public boolean remove(String uno, ProfileExperienceType experienceType, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM " + getTableName(uno) + " WHERE UNO = ? AND EXPTYPE = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("ProfileExperience remove sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, uno);
            pstmt.setString(2, experienceType.getCode());
            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On remove, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public List<ProfileExperience> queryProfileExperienceByUno(String uno, Connection conn) throws DbException {
        List<ProfileExperience> returnValue = new ArrayList<ProfileExperience>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(uno) + " WHERE UNO = ? ORDER BY EXPID ASC");
            pstmt.setString(1, uno);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryProfileExperienceByUno, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    protected String getTableName(String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }

    private String getInsertSql(String uno) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(uno) +
                " (EXPID, UNO, EXPNAME, EXPTYPE, METAINFO, EXP_STARTDATE, EXP_ENDDATE, CREATEDATE, UPDATEDATE, AUDITSTATUS, AUDITDATE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ProfileExperience INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected ProfileExperience rsToObject(ResultSet rs) throws SQLException {
        ProfileExperience entity = new ProfileExperience();

        entity.setExpid(rs.getLong("EXPID"));
        entity.setUno(rs.getString("UNO"));

        entity.setExpName(rs.getString("EXPNAME"));
        entity.setExperienceType(new ProfileExperienceType(rs.getString("EXPTYPE")));

        entity.setMetaInfo(rs.getString("METAINFO"));
        entity.setExpStartDate(rs.getTimestamp("EXP_STARTDATE"));
        entity.setExpEndDate(rs.getTimestamp("EXP_ENDDATE"));

        entity.setCreateDate(rs.getTimestamp("CREATEDATE"));
        entity.setUpdateDate(rs.getTimestamp("UPDATEDATE"));

        entity.setAuditStatus(AuditStatus.getByValue(rs.getInt("AUDITSTATUS")));
        entity.setAuditDate(rs.getTimestamp("AUDITDATE"));

        return entity;
    }
}
