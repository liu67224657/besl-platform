/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.social;


import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.social.InviteImportInfo;
import com.enjoyf.platform.service.social.InviteRelationType;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
 abstract class  AbstractInviteImportInfoAccessor implements InviteImportInfoAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractInviteImportInfoAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "SOCIAL_INVITE_";

    private static final int TABLE_NUM = 100;
    private static final String KEY_SEQUENCE_NAME = "SEQ_SOCIAL_INVITE_IMPORT_ID";

    @Override
    public InviteImportInfo insert(InviteImportInfo inviteImportInfo, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            //INVITEID,UNO,DESTMAIL,DESTUNO,INVITESTATUS,INVATERELATIONTYPE
            pstmt = conn.prepareStatement(getInsertSql(inviteImportInfo.getSrcUno()));

            inviteImportInfo.setInviteId(getSeqNo(conn));
            pstmt.setLong(1, inviteImportInfo.getInviteId());
            pstmt.setString(2, inviteImportInfo.getSrcUno());
            pstmt.setString(3, inviteImportInfo.getDestEmail());
            pstmt.setString(4, inviteImportInfo.getDestUno());
            pstmt.setString(5, inviteImportInfo.getInvateStatus().getCode());
            pstmt.setString(6, inviteImportInfo.getInviteRelationType().getCode());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return inviteImportInfo;
    }

    @Override
    public InviteImportInfo getInvite(String srcUno, String inviteEmail, Connection conn) throws DbException {
        InviteImportInfo returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(srcUno) + " WHERE SRCUNO=? AND DESTMAIL=?";
        if (logger.isDebugEnabled()) {
            logger.debug("getInvite sql script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, srcUno);
            pstmt.setString(2, inviteEmail);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }

        } catch (SQLException e) {
            GAlerter.lab("On getInvite, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
            DataBaseUtil.closeResultSet(rs);
        }

        return returnValue;
    }

    @Override
    public boolean updateInviteStatus(String srcUno, String destUno, String inviteEmail, ActStatus status, Connection conn) throws DbException {

        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getTableName(srcUno) + "SET DESTUNO=?,DESTMAIL=?,UPDATEDATE=?,INVITESTATUS=? WHERE SRCUNO=? AND DESTMAIL=?";
        if (logger.isDebugEnabled()) {
            logger.debug("updateInviteStatus sql script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, destUno);
            pstmt.setString(2, inviteEmail);
            pstmt.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
            pstmt.setString(4, status.getCode());

            pstmt.setString(5, srcUno);
            pstmt.setString(6, inviteEmail);

            return pstmt.executeUpdate() > 0 ? true : false;

        } catch (SQLException e) {
            GAlerter.lab("On getInvite, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public List<InviteImportInfo> selectByDateField(String srcUno, ActStatus inviteStatus, Date startDate, Date endDate, Connection conn) throws DbException {
        List<InviteImportInfo> returnValue = new ArrayList<InviteImportInfo>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(srcUno) + " WHERE SRCUNO=? AND INVITESTATUS=?"+getWhereByDataField(startDate,endDate);
        if (logger.isDebugEnabled()) {
            logger.debug("getInvite sql script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, srcUno);
            pstmt.setString(2, inviteStatus.getCode());
            if(startDate!=null){
              pstmt.setTimestamp(3, new Timestamp(startDate.getTime()));
            }
            if(startDate!=null){
              pstmt.setTimestamp(4, new Timestamp(endDate.getTime()));
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }

        } catch (SQLException e) {
            GAlerter.lab("On getInvite, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
            DataBaseUtil.closeResultSet(rs);
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

    private InviteImportInfo rsToObject(ResultSet rs) throws SQLException {
        //INVITEID,SRCUNO,DESTMAIL,DESTUNO,INVITESTATUS,INVATERELATIONTYPE
        InviteImportInfo returnValue = new InviteImportInfo();
        returnValue.setInviteId(rs.getLong("INVITEID"));
        returnValue.setSrcUno(rs.getString("SRCUNO"));
        returnValue.setDestEmail(rs.getString("DESTMAIL"));
        returnValue.setDestEmail(rs.getString("DESTUNO"));
        returnValue.setInvateStatus(ActStatus.getByCode(rs.getString("INVITESTATUS")));
        returnValue.setInviteRelationType(InviteRelationType.getByCode(rs.getString("INVATERELATIONTYPE")));
        returnValue.setCreateDate(new Date(rs.getTimestamp("CREATEDATE").getTime()));
        if (rs.getTimestamp("UPDATEDATE") != null) {
            returnValue.setLastModifyDate(new Date(rs.getTimestamp("UPDATEDATE").getTime()));
        }
        return returnValue;
    }

    private String getInsertSql(String uno) {
        String insert = "INSERT INTO " + getTableName(uno) + " (INVITEID,SRCUNO,DESTMAIL,DESTUNO,INVITESTATUS,INVATERELATIONTYPE) VALUES(?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert sql script:" + insert);
        }
        return insert;
    }

    private String getWhereByDataField(Date startDate, Date endDate) {
        StringBuffer sb = new StringBuffer();
        if (startDate != null) {
            sb.append(" AND UPDATEDATE>=?");
        }
        if (endDate != null) {
            sb.append(" AND UPDATEDATE<?");
        }
        String whereStr = sb.toString();
        if (logger.isDebugEnabled()) {
            logger.debug("insert sql script:" + whereStr);
        }
        return whereStr;
    }

    protected String getTableName(String uno) {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }
}
