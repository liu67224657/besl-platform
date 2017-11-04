/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;


import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.RegCodeApply;
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
class AbstractRegCodeApplyAccessor implements RegCodeApplyAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractRegCodeApplyAccessor.class);

    //
    private static final String KEY_TABLE_NAME = "MISC_REG_CODE_APPLY";


    protected RegCodeApply rsToObject(ResultSet rs) throws SQLException {
        RegCodeApply entry = new RegCodeApply();

        //USEREMAIL, INTRODUCE, APPLYDATE, APPLYIP, REGCODE
        entry.setUserEmail(rs.getString("USEREMAIL"));
        entry.setIntroduce(rs.getString("INTRODUCE"));
        entry.setApplyDate(rs.getTimestamp("APPLYDATE"));
        entry.setApplyIp(rs.getString("APPLYIP"));

        //
        entry.setRegCode(rs.getString("REGCODE"));

        return entry;
    }

    @Override
    public RegCodeApply insert(RegCodeApply apply, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("INSERT INTO " + KEY_TABLE_NAME +
                    " (USEREMAIL, INTRODUCE, APPLYDATE, APPLYIP, REGCODE) VALUES (?, ?, ?, ?, ?)");

            //USEREMAIL, INTRODUCE, APPLYDATE, APPLYIP, REGCODE
            pstmt.setString(1, apply.getUserEmail());
            pstmt.setString(2, apply.getIntroduce());
            pstmt.setTimestamp(3, apply.getApplyDate() != null ? new Timestamp(apply.getApplyDate().getTime()) : null);
            pstmt.setString(4, apply.getApplyIp());

            pstmt.setString(5, apply.getRegCode());

            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return apply;
    }

    @Override
    public RegCodeApply getByEmail(String email, Connection conn) throws DbException {
        RegCodeApply returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + KEY_TABLE_NAME + " WHERE USEREMAIL = ?");

            pstmt.setString(1, email);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getByEmail, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public boolean update(String email, RegCodeApply apply, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            //USEREMAIL, INTRODUCE, APPLYDATE, APPLYIP, REGCODE
            pstmt = conn.prepareStatement("UPDATE " + KEY_TABLE_NAME + " SET INTRODUCE = ?, APPLYDATE = ?, APPLYIP = ?, REGCODE = ? WHERE USEREMAIL = ?");

            pstmt.setString(1, apply.getIntroduce());
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(3, apply.getApplyIp());
            pstmt.setString(4, "");

            pstmt.setString(5, email);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean updateRegCode(String email, String regCode, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            //USEREMAIL, INTRODUCE, APPLYDATE, APPLYIP, REGCODE
            pstmt = conn.prepareStatement("UPDATE " + KEY_TABLE_NAME + " SET REGCODE = ? WHERE USEREMAIL = ?");

            pstmt.setString(1, regCode);

            pstmt.setString(2, email);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On updateRegCode, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }
}
