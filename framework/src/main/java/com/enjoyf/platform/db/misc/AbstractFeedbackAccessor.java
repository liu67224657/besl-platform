/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;


import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.misc.Feedback;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AbstractFeedbackAccessor implements FeedbackAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractFeedbackAccessor.class);

    //
    private static final String KEY_TABLE_NAME = "MISC_FEEDBACK";
    private static final String KEY_SEQUENCE_NAME = "SEQ_MISC_FEEDBACK_ID";


    @Override
    public Feedback insert(Feedback entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            entry.setFeedbackId(getSeqNo(conn));

            pstmt = conn.prepareStatement(getInsertSql());

            //FEEDBACKID, UNO, FEEDBACKSUBJECT, FEEDBACKBODY, FEEDBACKDATE, FEEDBACKIP
            pstmt.setLong(1, entry.getFeedbackId());

            pstmt.setString(2, entry.getUno());
            pstmt.setString(3, entry.getFeedbackSubject());

            pstmt.setString(4, entry.getFeedbackBody());

            pstmt.setTimestamp(5, entry.getFeedbackDate() != null ? new Timestamp(entry.getFeedbackDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(6, entry.getFeedbackIp());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert Feedback, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }


    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (FEEDBACKID, UNO, FEEDBACKSUBJECT, FEEDBACKBODY, FEEDBACKDATE, FEEDBACKIP) VALUES (?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("Feedback INSERT Script:" + insertSql);
        }


        return insertSql;
    }
}
