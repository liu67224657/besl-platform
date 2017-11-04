package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class AbstractPageViewEventAccessor implements PageViewEventAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPageViewEventAccessor.class);

    private static final String KEY_SEQUENCE_NAME = "SEQ_PV_EVENT_ID";
    private static final String KEY_TABLE_NAME_PREFIX = "PV_EVENT_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    @Override
    public PageViewEvent insert(PageViewEvent entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            entry.setEventId(getSeqNo(conn));

            pstmt = conn.prepareStatement(getInsertSql(entry.getEventDate()));

            //EVENTID, LOCATIONURL, LOCATIONID, SESSIONID, GLOBALID, UNO, REFER, REFERID, OS,SCREEN,EXPLORERTYPE,EXPLORERVERSION,META,EVENTDATE,EVENTIP
            pstmt.setLong(1, entry.getEventId());

            pstmt.setString(2, entry.getLocationUrl());
            pstmt.setString(3, entry.getLocationId());

            pstmt.setString(4, entry.getSessionId());
            pstmt.setString(5, entry.getGlobalId());
            pstmt.setString(6, entry.getUno());

            pstmt.setString(7, entry.getRefer());
            pstmt.setString(8, entry.getReferId());

            pstmt.setString(9, entry.getOs());
            pstmt.setString(10, entry.getScreen());

            pstmt.setString(11, entry.getExplorerType());
            pstmt.setString(12, entry.getExplorerVersion());
            pstmt.setString(13, entry.getMeta().deconstitute());

            pstmt.setTimestamp(14, new Timestamp(entry.getEventDate().getTime()));
            pstmt.setString(15, entry.getEventIp());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert PageViewEvent, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private String getTableName(Date d) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableDateSuffix(d, KEY_TABLE_SUFFIX_FMT);
    }

    private String getInsertSql(Date d) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(d) + " (EVENTID, LOCATIONURL, LOCATIONID, SESSIONID, GLOBALID, UNO, REFER, REFERID, OS, SCREEN, EXPLORERTYPE, EXPLORERVERSION, META, EVENTDATE, EVENTIP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("PageViewEvent INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
