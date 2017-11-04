package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertisePublish;
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
class AbstractAdvertisePublishAccessor extends AbstractSequenceBaseTableAccessor<AdvertisePublish> implements AdvertisePublishAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractAdvertisePublishAccessor.class);

    //
    private static final String KEY_SEQUENCE_NAME = "SEQ_ADVERTISE_ID";
    protected static final String KEY_TABLE_NAME = "ADVERTISE_PUBLISH";

    @Override
    public AdvertisePublish insert(AdvertisePublish entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            entry.setPublishId(AdvertiseIdGenerator.generatePublishId(entry.getProjectId(), entry.getAgentId(), getSeqNo(KEY_SEQUENCE_NAME, conn)));

            pstmt = conn.prepareStatement(getInsertSql());

            //PUBLISHID, PUBLISHNAME, PUBLISHDESC, PROJECTID, AGENTID, REDIRECTURL, VALIDSTATUS,
            //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
            pstmt.setString(1, entry.getPublishId());

            pstmt.setString(2, entry.getPublishName());
            pstmt.setString(3, entry.getPublishDesc());

            pstmt.setString(4, entry.getProjectId());
            pstmt.setString(5, entry.getAgentId());

            pstmt.setString(6, entry.getRedirectUrl());

            pstmt.setString(7, entry.getValidStatus() != null ? entry.getValidStatus().getCode() : null);

            pstmt.setString(8, entry.getCreateUserid());
            pstmt.setTimestamp(9, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(10, entry.getCreateIp());

            pstmt.setString(11, entry.getUpdateUserid());
            pstmt.setTimestamp(12, entry.getUpdateDate() != null ? new Timestamp(entry.getUpdateDate().getTime()) : null);
            pstmt.setString(13, entry.getUpdateIp());
            pstmt.setTimestamp(14, entry.getStatEndDate() != null ? new Timestamp(entry.getStatEndDate().getTime()) : new Timestamp(System.currentTimeMillis()));

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
    public AdvertisePublish get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected AdvertisePublish rsToObject(ResultSet rs) throws SQLException {
        AdvertisePublish entry = new AdvertisePublish();

        //PUBLISHID, PUBLISHNAME, PUBLISHDESC, PROJECTID, AGENTID, REDIRECTURL, VALIDSTATUS,
        //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
        entry.setPublishId(rs.getString("PUBLISHID"));

        entry.setPublishName(rs.getString("PUBLISHNAME"));
        entry.setPublishDesc(rs.getString("PUBLISHDESC"));

        entry.setProjectId(rs.getString("PROJECTID"));
        entry.setAgentId(rs.getString("AGENTID"));

        entry.setRedirectUrl(rs.getString("REDIRECTURL"));

        entry.setValidStatus(ValidStatus.getByCode(rs.getString("VALIDSTATUS")));

        entry.setCreateUserid(rs.getString("CREATEUSERID"));
        entry.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);
        entry.setCreateIp(rs.getString("CREATEIP"));

        entry.setUpdateUserid(rs.getString("UPDATEUSERID"));
        entry.setUpdateDate(rs.getTimestamp("UPDATEDATE") != null ? new Date(rs.getTimestamp("UPDATEDATE").getTime()) : null);
        entry.setUpdateIp(rs.getString("UPDATEIP"));
        entry.setStatEndDate(rs.getTimestamp("STATENDDATE") != null ? new Date(rs.getTimestamp("STATENDDATE").getTime()) : null);

        return entry;
    }

    @Override
    public List<AdvertisePublish> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<AdvertisePublish> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        //PUBLISHID, PUBLISHNAME, PUBLISHDESC, PROJECTID, AGENTID, REDIRECTURL, VALIDSTATUS,
        //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (PUBLISHID, PUBLISHNAME, PUBLISHDESC, PROJECTID, AGENTID, REDIRECTURL, VALIDSTATUS, CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP,STATENDDATE) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT SCRIPT:" + insertSql);
        }

        return insertSql;
    }
}
