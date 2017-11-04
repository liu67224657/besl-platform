package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertisePublishLocation;
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
class AbstractAdvertisePublishLocationAccessor extends AbstractSequenceBaseTableAccessor<AdvertisePublishLocation> implements AdvertisePublishLocationAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractAdvertisePublishLocationAccessor.class);

    //
    private static final String KEY_SEQUENCE_NAME = "SEQ_ADVERTISE_ID";
    protected static final String KEY_TABLE_NAME = "ADVERTISE_PUBLISH_LOCATION";

    @Override
    public AdvertisePublishLocation insert(AdvertisePublishLocation entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            entry.setPlId(AdvertiseIdGenerator.generatePublishLocationId(getSeqNo(KEY_SEQUENCE_NAME, conn)));

            //PLID, LOCATIONCODE, LOCATIONNAME, LOCATIONDESC, PUBLISHID, VALIDSTATUS,
            //CREATEUSERID, CREATEDATE, CREATEIP
            pstmt.setString(1, entry.getPlId());

            pstmt.setString(2, entry.getLocationCode());
            pstmt.setString(3, entry.getLocationName());
            pstmt.setString(4, entry.getLocationDesc());

            pstmt.setString(5, entry.getPublishId());

            pstmt.setString(6, entry.getValidStatus() != null ? entry.getValidStatus().getCode() : null);

            pstmt.setString(7, entry.getCreateUserid());
            pstmt.setTimestamp(8, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(9, entry.getCreateIp());

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
    public AdvertisePublishLocation get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected AdvertisePublishLocation rsToObject(ResultSet rs) throws SQLException {
        AdvertisePublishLocation entry = new AdvertisePublishLocation();

        //PLID, LOCATIONCODE, LOCATIONNAME, LOCATIONDESC, PUBLISHID, VALIDSTATUS,
        //CREATEUSERID, CREATEDATE, CREATEIP
        entry.setPlId(rs.getString("PLID"));

        entry.setLocationCode(rs.getString("LOCATIONCODE"));
        entry.setLocationName(rs.getString("LOCATIONNAME"));
        entry.setLocationDesc(rs.getString("LOCATIONDESC"));

        entry.setPublishId(rs.getString("PUBLISHID"));

        entry.setValidStatus(ValidStatus.getByCode(rs.getString("VALIDSTATUS")));

        entry.setCreateUserid(rs.getString("CREATEUSERID"));
        entry.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);
        entry.setCreateIp(rs.getString("CREATEIP"));

        return entry;
    }

    @Override
    public List<AdvertisePublishLocation> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<AdvertisePublishLocation> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        //PLID, LOCATIONCODE, LOCATIONNAME, LOCATIONDESC, PUBLISHID, VALIDSTATUS,
        //CREATEUSERID, CREATEDATE, CREATEIP
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (PLID, LOCATIONCODE, LOCATIONNAME, LOCATIONDESC, PUBLISHID, VALIDSTATUS, CREATEUSERID, CREATEDATE, CREATEIP) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT SCRIPT:" + insertSql);
        }

        return insertSql;
    }
}
