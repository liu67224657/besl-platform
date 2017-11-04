/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;


import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.UseStatus;
import com.enjoyf.platform.service.misc.RegCode;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AbstractRegCodeAccessor implements RegCodeAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractRegCodeAccessor.class);

    //
    private static final String KEY_TABLE_NAME = "MISC_REG_CODE";

    protected RegCode rsToObject(ResultSet rs) throws SQLException {
        RegCode entry = new RegCode();

        //REGCODE, USESTATUS, APPLYEMAIL, APPLYREASON, APPLYDATE, CONFIRMDATE, USEUNO, USEUSERID, USEDATE，
        entry.setRegCode(rs.getString("REGCODE"));
        entry.setUseStatus(UseStatus.getByCode(rs.getString("USESTATUS")));

        entry.setApplyEmail(rs.getString("APPLYEMAIL"));
        entry.setApplyReason(rs.getString("APPLYREASON"));
        entry.setApplyDate(rs.getTimestamp("APPLYDATE"));
        entry.setConfirmDate(rs.getTimestamp("CONFIRMDATE"));

        entry.setUseUno(rs.getString("USEUNO"));
        entry.setUseUserid(rs.getString("USEUSERID"));

        entry.setUseDate(rs.getTimestamp("USEDATE"));

        return entry;
    }

    @Override
    public RegCode get(String regCode, Connection conn) throws DbException {
        RegCode returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + KEY_TABLE_NAME + " WHERE REGCODE = ?");

            pstmt.setString(1, regCode);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On get, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public boolean updateUseInfo(String regCode, String useUno, String useUserid, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            //REGCODE, USESTATUS, APPLYEMAIL, APPLYREASON, APPLYDATE, CONFIRMDATE, USEUNO, USEUSERID, USEDATE
            pstmt = conn.prepareStatement("UPDATE " + KEY_TABLE_NAME + " SET USESTATUS = ?, USEUNO = ?, USEUSERID = ?, USEDATE = ? WHERE REGCODE = ?");

            pstmt.setString(1, UseStatus.USED.getCode());
            pstmt.setString(2, useUno);
            pstmt.setString(3, useUserid);
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            pstmt.setString(5, regCode);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update notice info, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean updateApplyInfo(String regCode, String applyEmail, String applyReason, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            //REGCODE, USESTATUS, APPLYEMAIL, APPLYREASON, APPLYDATE, CONFIRMDATE, USEUNO, USEUSERID, USEDATE
            pstmt = conn.prepareStatement("UPDATE " + KEY_TABLE_NAME + " SET USESTATUS = ?, APPLYEMAIL = ?, APPLYREASON = ?, APPLYDATE = ? WHERE REGCODE = ?");

            pstmt.setString(1, UseStatus.UNUSE.getCode());
            pstmt.setString(2, applyEmail);
            pstmt.setString(3, applyReason);
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            pstmt.setString(5, regCode);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update notice info, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }
}
