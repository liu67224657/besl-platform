package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.event.user.UserEventEntry;
import com.enjoyf.platform.service.event.user.UserEventType;
import com.enjoyf.platform.service.event.user.UserEventTypePrefix;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class AbstractUserEventAccessor implements UserEventAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUserEventAccessor.class);

    private static final String KEY_SEQUENCE_NAME = "SEQ_USER_EVENT_ID";
    private static final String KEY_TABLE_NAME_PREFIX = "USER_EVENT_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";
    private static String KEY_UNDERLINE = "_";

    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    //the player event entry.
    public UserEventEntry insert(UserEventEntry entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            entry.setEventId(getSeqNo(conn));

            pstmt = conn.prepareStatement(getInsertSql(entry));

            //
            pstmt.setLong(1, entry.getEventId());

            pstmt.setString(2, Strings.nullToEmpty(entry.getSrcUno()));
            pstmt.setString(3, Strings.nullToEmpty(entry.getDestUno()));

            pstmt.setString(4, entry.getEventType().getCode());

            pstmt.setLong(5, entry.getCount());

            pstmt.setString(6, entry.getDescription());
            pstmt.setString(7, entry.getMeta() != null ? entry.getMeta().deconstitute() : null);

            pstmt.setTimestamp(8, new Timestamp(entry.getEventDate().getTime()));
            pstmt.setString(9, entry.getEventIp());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert UserEventEntry, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    //stats
    public long userEventTypeSum(UserEventType eventType, Date from, Date to, Connection conn) throws DbException {
        long sum = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Date> tableDates = DateUtil.getMonthList(from, to);

        try {
            for (Date date : tableDates) {
                String sql = "SELECT COUNT(1) FROM " + getTableName(eventType.getPrefix(), date) + " WHERE EVENTTYPE = ? AND EVENTDATE >= ? AND EVENTDATE < ?";

                pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, eventType.getCode());
                pstmt.setTimestamp(2, new Timestamp(from.getTime()));
                pstmt.setTimestamp(3, new Timestamp(to.getTime()));

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    sum += rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            GAlerter.lab("On userEventTypeSum UserEventEntry, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return sum;
    }

    @Override
    public long userEventTypeDistinct(UserEventType eventType, Date from, Date to, Connection conn) throws DbException {
        long sum = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Date> tableDates = DateUtil.getMonthList(from, to);

        try {
            for (Date date : tableDates) {
                String sql = "SELECT COUNT(DISTINCT SRCUNO) FROM " + getTableName(eventType.getPrefix(), date) + " WHERE EVENTTYPE = ? AND EVENTDATE >= ? AND EVENTDATE < ?";

                pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, eventType.getCode());
                pstmt.setTimestamp(2, new Timestamp(from.getTime()));
                pstmt.setTimestamp(3, new Timestamp(to.getTime()));

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    sum += rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            GAlerter.lab("On userEventTypeSum UserEventEntry, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return sum;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getTableName(UserEventTypePrefix prefix, Date d) throws DbException {
        return KEY_TABLE_NAME_PREFIX + prefix.getPrefix() + KEY_UNDERLINE + TableUtil.getTableDateSuffix(d, KEY_TABLE_SUFFIX_FMT);
    }

    private String getInsertSql(UserEvent entry) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(entry.getEventType().getPrefix(), entry.getEventDate()) + " (EVENTID, SRCUNO, DESTUNO, EVENTTYPE, EVENTCOUNT, DESCRIPTION, META, EVENTDATE, EVENTIP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("UserEvent INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
