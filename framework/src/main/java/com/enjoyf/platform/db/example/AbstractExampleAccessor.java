package com.enjoyf.platform.db.example;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.example.Example;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
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
class AbstractExampleAccessor extends AbstractSequenceBaseTableAccessor<Example> implements ExampleAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractExampleAccessor.class);

    //
    private static final String KEY_SEQUENCE_NAME = "SEQ_EXAMPLE_ID";
    protected static final String KEY_TABLE_NAME = "EXAMPLE";

    @Override
    public Example insert(Example entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            entry.setExampleId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql());

            //EXAMPLEID, EXAMPLENAME, EXAMPLEDISCRIPTION, CLICKTIMES, VALIDSTATUS, LASTCLICKDATE
            pstmt.setLong(1, entry.getExampleId());

            pstmt.setString(2, entry.getExampleName());
            pstmt.setString(3, entry.getExampleDiscription());

            pstmt.setInt(4, entry.getClickTimes());
            pstmt.setString(5, entry.getValidStatus() != null ? entry.getValidStatus().getCode() : null);

            pstmt.setTimestamp(6, entry.getLastClickDate() != null ? new Timestamp(entry.getLastClickDate().getTime()) : null);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert Example, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public Example get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected Example rsToObject(ResultSet rs) throws SQLException {
        Example entry = new Example();

        //EXAMPLEID, EXAMPLENAME, EXAMPLEDISCRIPTION, CLICKTIMES, VALIDSTATUS, LASTCLICKDATE
        entry.setExampleId(rs.getLong("EXAMPLEID"));

        entry.setExampleName(rs.getString("EXAMPLENAME"));
        entry.setExampleDiscription(rs.getString("EXAMPLEDISCRIPTION"));

        entry.setClickTimes(rs.getInt("CLICKTIMES"));

        entry.setValidStatus(ValidStatus.getByCode(rs.getString("VALIDSTATUS")));

        entry.setLastClickDate(rs.getTimestamp("LASTCLICKDATE") != null ? new Date(rs.getTimestamp("LASTCLICKDATE").getTime()) : null);

        return entry;
    }

    @Override
    public List<Example> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Example> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (EXAMPLEID, EXAMPLENAME, EXAMPLEDISCRIPTION, CLICKTIMES, VALIDSTATUS, LASTCLICKDATE) VALUES (?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("Example INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
