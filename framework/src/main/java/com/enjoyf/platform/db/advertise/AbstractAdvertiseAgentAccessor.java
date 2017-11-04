package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertiseAgent;
import com.enjoyf.platform.util.Pagination;
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
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class AbstractAdvertiseAgentAccessor extends AbstractSequenceBaseTableAccessor<AdvertiseAgent> implements AdvertiseAgentAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractAdvertiseAgentAccessor.class);

    //
    private static final String KEY_SEQUENCE_NAME = "SEQ_ADVERTISE_ID";
    protected static final String KEY_TABLE_NAME = "ADVERTISE_AGENT";

    @Override
    public AdvertiseAgent insert(AdvertiseAgent entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            entry.setAgentId(AdvertiseIdGenerator.generateAgentId(getSeqNo(KEY_SEQUENCE_NAME, conn)));

            pstmt = conn.prepareStatement(getInsertSql());

            //AGENTID, AGENTNAME, AGENTDESC, VALIDSTATUS,
            //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
            pstmt.setString(1, entry.getAgentId());

            pstmt.setString(2, entry.getAgentName());
            pstmt.setString(3, entry.getAgentDesc());

            pstmt.setString(4, entry.getValidStatus() != null ? entry.getValidStatus().getCode() : null);

            pstmt.setString(5, entry.getCreateUserid());
            pstmt.setTimestamp(6, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(7, entry.getCreateIp());

            pstmt.setString(8, entry.getUpdateUserid());
            pstmt.setTimestamp(9, entry.getUpdateDate() != null ? new Timestamp(entry.getUpdateDate().getTime()) : null);
            pstmt.setString(10, entry.getUpdateIp());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public AdvertiseAgent get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected AdvertiseAgent rsToObject(ResultSet rs) throws SQLException {
        AdvertiseAgent entry = new AdvertiseAgent();

        //AGENTID, AGENTNAME, AGENTDESC, VALIDSTATUS,
        //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
        entry.setAgentId(rs.getString("AGENTID"));

        entry.setAgentName(rs.getString("AGENTNAME"));
        entry.setAgentDesc(rs.getString("AGENTDESC"));

        entry.setValidStatus(ValidStatus.getByCode(rs.getString("VALIDSTATUS")));

        entry.setCreateUserid(rs.getString("CREATEUSERID"));
        entry.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);
        entry.setCreateIp(rs.getString("CREATEIP"));

        entry.setUpdateUserid(rs.getString("UPDATEUSERID"));
        entry.setUpdateDate(rs.getTimestamp("UPDATEDATE") != null ? new Date(rs.getTimestamp("UPDATEDATE").getTime()) : null);
        entry.setUpdateIp(rs.getString("UPDATEIP"));

        return entry;
    }

    @Override
    public List<AdvertiseAgent> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<AdvertiseAgent> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        //AGENTID, AGENTNAME, AGENTDESC, VALIDSTATUS,
        //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (AGENTID, AGENTNAME, AGENTDESC, VALIDSTATUS, CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT SCRIPT:" + insertSql);
        }

        return insertSql;
    }
}
