package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.vote.VoteDomain;
import com.enjoyf.platform.service.vote.VoteRecordSet;
import com.enjoyf.platform.service.vote.VoteUserRecord;
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
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-20
 * Time: 上午16:24
 * To change this template use File | Settings | File Templates.
 */
class AbstractVoteUserRecordAccessor extends AbstractSequenceBaseTableAccessor<VoteUserRecord> implements VoteUserRecordAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractVoteUserRecordAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "VOTE_USER_RECORD_";
    private static final String KEY_SEQUENCE_NAME = "SEQ_VOTE_USER_RECORD_ID";
    private static final int TABLE_NUM = 100;

    @Override
    public VoteUserRecord insert(VoteUserRecord voteUserRecord, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {

            pstmt = conn.prepareStatement(getInsertSql(voteUserRecord.getVoteUno()));

            // USERRECORDID, SUBJECTID, VOTEUNO, VOTEDOMAIN, VOTERECORD, VOTEDATE, VOTEIP
            voteUserRecord.setUserRecordId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt.setLong(1, voteUserRecord.getUserRecordId());
            pstmt.setString(2, voteUserRecord.getSubjectId());

            pstmt.setString(3, voteUserRecord.getVoteUno());
            pstmt.setString(4, voteUserRecord.getVoteDomain().getCode());
            pstmt.setString(5, voteUserRecord.getRecordSet().toJsonStr());

            pstmt.setTimestamp(6, voteUserRecord.getVoteDate() != null ? new Timestamp(voteUserRecord.getVoteDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(7, voteUserRecord.getVoteIp());

            //
            pstmt.executeUpdate();

        } catch (SQLException e) {
            GAlerter.lab("On insert VoteUserRecord, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return voteUserRecord;
    }

    @Override
    public VoteUserRecord getVoteUserRecord(String voteUno, QueryExpress queryExpress, Connection conn) throws DbException {
        return get(getTableName(voteUno), queryExpress, conn);
    }

    @Override
    public List<VoteUserRecord> query(String voteUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<VoteUserRecord> query(String voteUno, QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }
    
    @Override
    public List<VoteUserRecord> queryByExpress(String voteUno, QueryExpress queryExpress, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public int updateVoteUserRecord(String voteUno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(getTableName(voteUno), updateExpress, queryExpress, conn);
    }

    ///private methods.
    private String getInsertSql(String voteUno) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(voteUno)
                + " (USERRECORDID, SUBJECTID, VOTEUNO, VOTEDOMAIN, VOTERECORD, VOTEDATE, VOTEIP)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("VoteUserRecord INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    protected VoteUserRecord rsToObject(ResultSet rs) throws SQLException {
        VoteUserRecord entry = new VoteUserRecord();

        // USERRECORDID, SUBJECTID, VOTEUNO, VOTEDOMAIN, VOTERECORD, VOTEDATE, VOTEIP
        entry.setUserRecordId(rs.getLong("USERRECORDID"));
        entry.setSubjectId(rs.getString("SUBJECTID"));

        entry.setVoteUno(rs.getString("VOTEUNO"));
        entry.setVoteDomain(VoteDomain.getByCode(rs.getString("VOTEDOMAIN")));

        entry.setRecordSet(VoteRecordSet.parse(rs.getString("VOTERECORD")));

        entry.setVoteDate(rs.getTimestamp("VOTEDATE") != null ? new Date(rs.getTimestamp("VOTEDATE").getTime()) : null);
        entry.setVoteIp(rs.getString("VOTEIP"));

        return entry;
    }

    protected String getTableName(String voteUno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(voteUno.hashCode(), TABLE_NUM);
    }

}
