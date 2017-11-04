package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertiseProject;
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
class AbstractAdvertiseProjectAccessor extends AbstractSequenceBaseTableAccessor<AdvertiseProject> implements AdvertiseProjectAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractAdvertiseProjectAccessor.class);

    //
    private static final String KEY_SEQUENCE_NAME = "SEQ_ADVERTISE_ID";
    protected static final String KEY_TABLE_NAME = "ADVERTISE_PROJECT";

    @Override
    public AdvertiseProject insert(AdvertiseProject entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            entry.setProjectId(AdvertiseIdGenerator.generateProjectId(getSeqNo(KEY_SEQUENCE_NAME, conn)));

            pstmt = conn.prepareStatement(getInsertSql());

            //PROJECTID, PROJECTNAME, PROJECTDESC, STARTDATE, ENDDATE, STATSTARTDATE, STATENDDATE, VALIDSTATUS,
            //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
            pstmt.setString(1, entry.getProjectId());

            pstmt.setString(2, entry.getProjectName());
            pstmt.setString(3, entry.getProjectDesc());

            pstmt.setTimestamp(4, entry.getStartDate() != null ? new Timestamp(entry.getStartDate().getTime()) : null);
            pstmt.setTimestamp(5, entry.getEndDate() != null ? new Timestamp(entry.getEndDate().getTime()) : null);

            pstmt.setTimestamp(6, entry.getStatStartDate() != null ? new Timestamp(entry.getStatStartDate().getTime()) : null);
            pstmt.setTimestamp(7, entry.getStatEndDate() != null ? new Timestamp(entry.getStatEndDate().getTime()) : null);

            pstmt.setString(8, entry.getValidStatus() != null ? entry.getValidStatus().getCode() : null);

            pstmt.setString(9, entry.getCreateUserid());
            pstmt.setTimestamp(10, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(11, entry.getCreateIp());

            pstmt.setString(12, entry.getUpdateUserid());
            pstmt.setTimestamp(13, entry.getUpdateDate() != null ? new Timestamp(entry.getUpdateDate().getTime()) : null);
            pstmt.setString(14, entry.getUpdateIp());

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
    public AdvertiseProject get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected AdvertiseProject rsToObject(ResultSet rs) throws SQLException {
        AdvertiseProject entry = new AdvertiseProject();

        //PROJECTID, PROJECTNAME, PROJECTDESC, STARTDATE, ENDDATE, STATSTARTDATE, STATENDDATE, VALIDSTATUS,
        //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
        entry.setProjectId(rs.getString("PROJECTID"));

        entry.setProjectName(rs.getString("PROJECTNAME"));
        entry.setProjectDesc(rs.getString("PROJECTDESC"));

        entry.setStartDate(rs.getTimestamp("STARTDATE") != null ? new Date(rs.getTimestamp("STARTDATE").getTime()) : null);
        entry.setEndDate(rs.getTimestamp("ENDDATE") != null ? new Date(rs.getTimestamp("ENDDATE").getTime()) : null);
        entry.setStatStartDate(rs.getTimestamp("STATSTARTDATE") != null ? new Date(rs.getTimestamp("STATSTARTDATE").getTime()) : null);
        entry.setStatEndDate(rs.getTimestamp("STATENDDATE") != null ? new Date(rs.getTimestamp("STATENDDATE").getTime()) : null);

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
    public List<AdvertiseProject> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<AdvertiseProject> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        //PROJECTID, PROJECTNAME, PROJECTDESC, STARTDATE, ENDDATE, STATSTARTDATE, STATENDDATE, VALIDSTATUS,
        //CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (PROJECTID, PROJECTNAME, PROJECTDESC, STARTDATE, ENDDATE, STATSTARTDATE, STATENDDATE, VALIDSTATUS, CREATEUSERID, CREATEDATE, CREATEIP, UPDATEUSERID, UPDATEDATE, UPDATEIP) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT SCRIPT:" + insertSql);
        }

        return insertSql;
    }
}
