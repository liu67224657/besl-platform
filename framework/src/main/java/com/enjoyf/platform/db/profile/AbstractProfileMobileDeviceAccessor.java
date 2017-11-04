package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.profile.ProfileMobileDevice;
import com.enjoyf.platform.service.profile.ProfileMobileDeviceClientType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-2
 * Time: 上午11:38
 * To change this template use File | Settings | File Templates.
 */
public class AbstractProfileMobileDeviceAccessor extends AbstractBaseTableAccessor<ProfileMobileDevice> implements ProfileMobileDeviceAccessor {

    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractProfileMobileDeviceAccessor.class);

    protected static final String KEY_TABLE_NAME = "PROFILE_MOBILE_DEVICE";

    @Override
    public ProfileMobileDevice insert(ProfileMobileDevice entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {

            pstmt = conn.prepareStatement(getInsertSql());

            //MDSERIAL MDCLIENTTYPE MDHDTYPE MDOSVERSION UNO PUSHMSGID PUSHSTATUS VALIDSTATUS CREATEDATE

            pstmt.setString(1, entry.getMdSerial());

            pstmt.setString(2, entry.getMdClientType().getCode());

            pstmt.setString(3, entry.getMdHdType());

            pstmt.setString(4, entry.getMdOsVersion());

            pstmt.setString(5, entry.getUno());

            pstmt.setLong(6, entry.getPushMsgId());

            pstmt.setString(7, entry.getPushStatus().getCode());

            pstmt.setString(8, entry.getValidStatus().getCode());

            pstmt.setTimestamp(9, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : null);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert PROFILE_MOBILE_DEVICE, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public ProfileMobileDevice get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ProfileMobileDevice> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ProfileMobileDevice> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected ProfileMobileDevice rsToObject(ResultSet rs) throws SQLException {
        ProfileMobileDevice entry = new ProfileMobileDevice();

        //PMDID MDSERIAL MDCLIENTTYPE MDHDTYPE MDOSVERSION UNO PUSHMSGID PUSHSTATUS CREATEDATE 

        entry.setPmdId(rs.getLong("PMDID"));

        entry.setMdSerial(rs.getString("MDSERIAL"));

        entry.setMdClientType(ProfileMobileDeviceClientType.getByCode(rs.getString("MDCLIENTTYPE")));

        entry.setMdHdType(rs.getString("MDHDTYPE"));

        entry.setMdOsVersion(rs.getString("MDOSVERSION"));

        entry.setUno(rs.getString("UNO"));

        entry.setPushMsgId(rs.getLong("PUSHMSGID"));

        entry.setPushStatus(ActStatus.getByCode(rs.getString("PUSHSTATUS")));

        entry.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);

        return entry;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (MDSERIAL, MDCLIENTTYPE, MDHDTYPE, MDOSVERSION, UNO, PUSHMSGID, PUSHSTATUS, VALIDSTATUS, CREATEDATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("PROFILE_MOBILE_DEVICE INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
