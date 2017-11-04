package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.AdvertiseUserPublishRelation;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class AbstractAdvertiseUserPublishRelationAccessor extends AbstractSequenceBaseTableAccessor<AdvertiseUserPublishRelation> implements AdvertiseUserPublishRelationAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractAdvertiseUserPublishRelationAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "ADVERTISE_USER_PUBLISH_RELATION";

    @Override
    public AdvertiseUserPublishRelation insert(AdvertiseUserPublishRelation entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //UNO, PUBLISHID, LOCATIONCODE, CREATEDATE, CREATEIP
            pstmt.setString(1, entry.getUno());

            pstmt.setString(2, entry.getPublishId());
            pstmt.setString(3, entry.getLocationCode());

            pstmt.setTimestamp(4, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(5, entry.getCreateIp());

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
    public AdvertiseUserPublishRelation get(QueryExpress getExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, getExpress, conn);
    }

    @Override
    protected AdvertiseUserPublishRelation rsToObject(ResultSet rs) throws SQLException {
        AdvertiseUserPublishRelation entry = new AdvertiseUserPublishRelation();

        //UNO, PUBLISHID, LOCATIONCODE, CREATEDATE, CREATEIP
        entry.setUno(rs.getString("UNO"));

        entry.setPublishId(rs.getString("PUBLISHID"));
        entry.setLocationCode(rs.getString("LOCATIONCODE"));


        entry.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);
        entry.setCreateIp(rs.getString("CREATEIP"));

        return entry;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        //UNO, PUBLISHID, LOCATIONCODE, CREATEDATE, CREATEIP
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (UNO, PUBLISHID, LOCATIONCODE, CREATEDATE, CREATEIP) "
                + "VALUES (?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT SCRIPT:" + insertSql);
        }

        return insertSql;
    }
}
