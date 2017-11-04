package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.advertise.AdvertiseEvent;
import com.enjoyf.platform.util.log.GAlerter;
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
class AbstractAdvertiseEventAccessor extends AbstractSequenceBaseTableAccessor<AdvertiseEvent> implements AdvertiseEventAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractAdvertiseEventAccessor.class);

    //
    private static final String KEY_SEQUENCE_NAME = "SEQ_ADVERTISE_EVENT_ID";
    protected static final String PREFIX_KEY_TABLE_NAME = "ADVERTISE_EVENT_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    @Override
    public AdvertiseEvent insert(AdvertiseEvent entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        if (entry.getEventDate() == null) {
            entry.setEventDate(new Date());
        }

        try {
            //
            entry.setEventId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            //
            pstmt = conn.prepareStatement(getInsertSql(entry.getEventDate()));

            //EVENTID, PUBLISHID, LOCATIONCODE, EVENTTYPE,
            //UNO, SESSIONID, GLOBALID, EVENTCOUNT, EVENTDESC, EVENTDATE, EVENTIP
            pstmt.setLong(1, entry.getEventId());

            pstmt.setString(2, entry.getPublishId());
            pstmt.setString(3, entry.getLocationCode());

            pstmt.setString(4, entry.getEventType().getCode());

            pstmt.setString(5, entry.getUno());
            pstmt.setString(6, entry.getSessionId());
            pstmt.setString(7, entry.getGlobalId());

            pstmt.setLong(8, entry.getEventCount());
            pstmt.setString(9, entry.getEventDesc());

            pstmt.setTimestamp(10, new Timestamp(entry.getEventDate().getTime()));
            pstmt.setString(11, entry.getEventIp());

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
    protected AdvertiseEvent rsToObject(ResultSet rs) throws SQLException {
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql(Date d) throws DbException {
        //EVENTID, PUBLISHID, LOCATIONCODE, EVENTTYPE,
        //UNO, SESSIONID, GLOBALID, EVENTCOUNT, EVENTDESC, EVENTDATE, EVENTIP
        String insertSql = "INSERT INTO " + getTableName(d) + " (EVENTID, PUBLISHID, LOCATIONCODE, EVENTTYPE, UNO, SESSIONID, GLOBALID, EVENTCOUNT, EVENTDESC, EVENTDATE, EVENTIP) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT SCRIPT:" + insertSql);
        }

        return insertSql;
    }

    private String getTableName(Date d) {
        return PREFIX_KEY_TABLE_NAME + TableUtil.getTableDateSuffix(d, KEY_TABLE_SUFFIX_FMT);
    }
}
