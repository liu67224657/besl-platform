package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.vote.VoteDomain;
import com.enjoyf.platform.service.vote.VoteSubjectRecord;
import com.enjoyf.platform.util.CollectionUtil;
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
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-20
 * Time: 上午16:24
 * To change this template use File | Settings | File Templates.
 */
class AbstractVoteSubjectRecordAccessor extends AbstractSequenceBaseTableAccessor<VoteSubjectRecord> implements VoteSubjectRecordAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractVoteSubjectRecordAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "VOTE_SUBJECT_RECORD_";
    private static final String KEY_SEQUENCE_NAME = "SEQ_VOTE_SUBJECT_RECORD_ID";
    private static final int TABLE_NUM = 100;

    @Override
    public VoteSubjectRecord insert(VoteSubjectRecord subjectRecord, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {

            pstmt = conn.prepareStatement(getInsertSql(subjectRecord.getSubjectId()));

            //SUBJECTRECORDID, SUBJECTID, VOTEUNO, VOTEDOMAIN, VOTEOPTION, VOTEOPTIONVALUE, VOTEDATE, VOTEIP
            subjectRecord.setSubjectRecordId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt.setLong(1, subjectRecord.getSubjectRecordId());
            pstmt.setString(2, subjectRecord.getSubjectId());

            pstmt.setString(3, subjectRecord.getVoteUno());
            pstmt.setString(4, subjectRecord.getVoteDomain().getCode());

            pstmt.setLong(5, subjectRecord.getVoteOption());
            pstmt.setString(6, subjectRecord.getVoteOptionValue());

            pstmt.setTimestamp(7, subjectRecord.getVoteDate() != null ? new Timestamp(subjectRecord.getVoteDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(8, subjectRecord.getVoteIp());

            //
            pstmt.executeUpdate();

        } catch (SQLException e) {
            GAlerter.lab("On insert VoteSubjectRecord, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return subjectRecord;
    }

    @Override
    public boolean insertBatch(String subjectId, Set<VoteSubjectRecord> subjectRecordSet, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            if (CollectionUtil.isEmpty(subjectRecordSet)) {
                return false;
            }
            pstmt = conn.prepareStatement(getInsertSql(subjectId));

            //OPTIONID,DESCRIPTION,OPTIONTYPE,SUBJECTID,OPTIONNUM
            for (VoteSubjectRecord subjectRecord : subjectRecordSet) {

                subjectRecord.setSubjectRecordId(getSeqNo(KEY_SEQUENCE_NAME, conn));

                pstmt.setLong(1, subjectRecord.getSubjectRecordId());
                pstmt.setString(2, subjectRecord.getSubjectId());

                pstmt.setString(3, subjectRecord.getVoteUno());
                pstmt.setString(4, subjectRecord.getVoteDomain().getCode());

                pstmt.setLong(5, subjectRecord.getVoteOption());
                pstmt.setString(6, subjectRecord.getVoteOptionValue());

                pstmt.setTimestamp(7, subjectRecord.getVoteDate() != null ? new Timestamp(subjectRecord.getVoteDate().getTime()) : new Timestamp(System.currentTimeMillis()));
                pstmt.setString(8, subjectRecord.getVoteIp());

                pstmt.addBatch();
            }

            //
            pstmt.executeBatch();

        } catch (SQLException e) {
            GAlerter.lab("On VoteSubjectRecord, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return true;
    }

    @Override
    public VoteSubjectRecord getSubjectRecord(String subjectId, QueryExpress queryExpress, Connection conn) throws DbException {
        return get(getTableName(subjectId), queryExpress, conn);
    }

    @Override
    public List<VoteSubjectRecord> query(String ownUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<VoteSubjectRecord> query(String ownUno, QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<VoteSubjectRecord> queryByExpress(String ownUno, QueryExpress queryExpress, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public int updateVoteSubjectRecord(String subjectId, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(getTableName(subjectId), updateExpress, queryExpress, conn);
    }

    ///private methods.
    private String getInsertSql(String subjectId) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(subjectId)
                + " (SUBJECTRECORDID, SUBJECTID, VOTEUNO, VOTEDOMAIN, VOTEOPTION, VOTEOPTIONVALUE, VOTEDATE, VOTEIP)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("VoteSubjectRecord INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    protected VoteSubjectRecord rsToObject(ResultSet rs) throws SQLException {
        VoteSubjectRecord entry = new VoteSubjectRecord();

        // SUBJECTRECORDID, SUBJECTID, VOTEUNO, VOTEDOMAIN, VOTEOPTION, VOTEOPTIONVALUE, VOTEDATE, VOTEIP
        entry.setSubjectRecordId(rs.getLong("SUBJECTRECORDID"));
        entry.setSubjectId(rs.getString("SUBJECTID"));

        entry.setVoteUno(rs.getString("VOTEUNO"));
        entry.setVoteDomain(VoteDomain.getByCode(rs.getString("VOTEDOMAIN")));

        entry.setVoteOption(rs.getLong("VOTEOPTION"));
        entry.setVoteOptionValue(rs.getString("VOTEOPTIONVALUE"));

        entry.setVoteDate(rs.getTimestamp("VOTEDATE") != null ? new Date(rs.getTimestamp("VOTEDATE").getTime()) : null);
        entry.setVoteIp(rs.getString("VOTEIP"));

        return entry;
    }

    protected String getTableName(String subjectId) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(subjectId.hashCode(), TABLE_NUM);
    }

}
