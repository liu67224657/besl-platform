/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.sequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.props.EnvConfig;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */

/**
 * this class is used by jdbc and hibernate. used to get a new sequence id.
 */
public class TableSequenceFetcher {
    private static Map<String, SequenceValue> sequenceMap = new HashMap<String, SequenceValue>();

    // the table name which store all the sequence of the database.
    private static final String SEQUENCE_TABLE_NAME = "SEQUENCES_TABLE";

    private static TableSequenceFetcher instance = new TableSequenceFetcher();

    private TableSequenceFetcher() {
    }

    public static synchronized TableSequenceFetcher get() {
        return instance;
    }

    public long generate(String sequenceName, Connection conn) throws TableSequenceException {
        SequenceValue sequenceValue = null;
        long returnValue = 0;

        // get the sequence from map or initialize it.
        synchronized (sequenceMap) {
            sequenceValue = sequenceMap.get(sequenceName);

            if (sequenceValue == null) {
                sequenceValue = new SequenceValue(sequenceName);

                sequenceMap.put(sequenceValue.getSequenceName(), sequenceValue);
            }
        }

        // get the next value from the cache.
        synchronized (sequenceValue) {
            // if the cache has next value, just get one from the cache.
            if (sequenceValue.hasNext()) {
                returnValue = sequenceValue.getNextValue();
            } else {
                // if the cache hasn't next value, get more from db.
                long dbValue = 0;
                try {
                    dbValue = fetchNextValue(sequenceName, EnvConfig.get().getSequenceFetchStep(), conn);
                }
                catch (SQLException e) {
                    throw new TableSequenceException("Generate sequence value failed, the sequence name is " + sequenceName + " " + e);
                }

                // set the db value to cache.
                sequenceValue.setCurValue(dbValue);
                sequenceValue.setMaxValue(dbValue + EnvConfig.get().getSequenceFetchStep() - 1);

                returnValue = sequenceValue.getNextValue();
            }
        }

        return returnValue;
    }

    private long fetchNextValue(String sequenceName, int step, Connection conn) throws SQLException {
        String selectSql = "SELECT CURVALUE FROM " + SEQUENCE_TABLE_NAME + " WHERE SEQUENCENAME = ?";
        String updateSql = "UPDATE " + SEQUENCE_TABLE_NAME + " SET CURVALUE = CURVALUE + ? WHERE SEQUENCENAME = ? AND CURVALUE = ?";

        long curValue = -1;

        boolean success = false;
        int tryTimes = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            while (!success && (tryTimes < EnvConfig.get().getSequenceFetchTryTimes())) {
                // select the current value from table.
                pstmt = conn.prepareStatement(selectSql);
                pstmt.setString(1, sequenceName.trim());

                rs = pstmt.executeQuery();
                if (rs.next()) {
                    curValue = rs.getLong(1);
                } else {
                    throw new SQLException("The sequence is not exist in sequence table, sequence:" + sequenceName);
                }

                // update the sequence value.
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setLong(1, step);
                pstmt.setString(2, sequenceName);
                pstmt.setLong(3, curValue);

                if (pstmt.executeUpdate() > 0) {
                    success = true;
                } else {
                    curValue = -1;
                }

                // try times increase.
                tryTimes++;
            }
        }
        finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        if (curValue < 0) {
            throw new SQLException("Fetch sequence value from table failed, curValue < 0,the sequence name is " + sequenceName);
        }

        return curValue;
    }

}
