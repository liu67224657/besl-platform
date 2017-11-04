package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.vote.VoteOption;
import com.enjoyf.platform.service.vote.VoteOptionType;
import com.enjoyf.platform.service.vote.VoteRecord;
import com.enjoyf.platform.service.vote.VoteRecordSet;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-20
 * Time: 上午16:23
 * To change this template use File | Settings | File Templates.
 */
class AbstractVoteOptionAccessor extends AbstractSequenceBaseTableAccessor<VoteOption> implements VoteOptionAccessor {

    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractVoteOptionAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "VOTE_OPTION_";
    private static final String KEY_SEQUENCE_NAME = "SEQ_VOTE_OPTION_ID";
    private static final int TABLE_NUM = 100;

    @Override
    public VoteOption insert(VoteOption voteOption, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {

            pstmt = conn.prepareStatement(getInsertSql(voteOption.getSubjectID()));

            //OPTIONID,DESCRIPTION,OPTIONTYPE,SUBJECTID,OPTIONNUM
            voteOption.setOptionId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt.setLong(1, voteOption.getOptionId());
            pstmt.setString(2, voteOption.getDescription());
            pstmt.setString(3, voteOption.getVoteOptionType().getCode());

            pstmt.setString(4, voteOption.getSubjectID());
            pstmt.setInt(5, voteOption.getOptionNum());
            //
            pstmt.executeUpdate();

        } catch (SQLException e) {
            GAlerter.lab("On insertVoteRecord, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return voteOption;
    }

    @Override
    public Set<VoteOption> insertBatch(String subjectId, Set<VoteOption> voteOptions, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            if (CollectionUtil.isEmpty(voteOptions)) {
                return null;
            }

            pstmt = conn.prepareStatement(getInsertSql(subjectId));

            //OPTIONID,DESCRIPTION,OPTIONTYPE,SUBJECTID,OPTIONNUM
            for (VoteOption option : voteOptions) {
                option.setOptionId(getSeqNo(KEY_SEQUENCE_NAME, conn));
                pstmt.setLong(1, option.getOptionId());
                pstmt.setString(2, option.getDescription());
                pstmt.setString(3, option.getVoteOptionType().getCode());
                pstmt.setString(4, option.getSubjectID());
                pstmt.setInt(5, option.getOptionNum());

                pstmt.addBatch();
            }

            //
            pstmt.executeBatch();

        } catch (SQLException e) {
            GAlerter.lab("On insertVoteOption, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return voteOptions;
    }

    @Override
    public boolean increaseBatch(String subjectID, VoteRecordSet recordSet, ObjectField field, Integer value, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            String sql = "UPDATE " + getTableName(subjectID) +
                    " SET " + field.getColumn() + " = " + field.getColumn() + " + ? WHERE OPTIONID = ? AND " + field.getColumn() + " >= 0";
            if (logger.isDebugEnabled()) {
                logger.debug("VoteOption increase sql:" + sql);
            }

            pstmt = conn.prepareStatement(sql);

            for (VoteRecord voteRecord : recordSet.getRecords()) {
                pstmt.setInt(1, value != null ? value : 1);
                pstmt.setLong(2, voteRecord.getOptionId());

                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            GAlerter.lab("On  VoteOption increase a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return true;
    }

    @Override
    public VoteOption getVoteOption(String subjectId, QueryExpress queryExpress, Connection conn) throws DbException {
        return get(getTableName(subjectId), queryExpress, conn);
    }

    @Override
    public List<VoteOption> queryOptions(String subjectId, QueryExpress queryExpress, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public int updateVoteOption(String subjectId, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(getTableName(subjectId), updateExpress, queryExpress, conn);
    }

    ///////////////////////////////////////////////////////
    protected String getTableName(String subjectId) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(subjectId.hashCode(), TABLE_NUM);
    }

    ///private methods.
    private String getInsertSql(String subjectId) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(subjectId)
                + " (OPTIONID,DESCRIPTION,OPTIONTYPE,SUBJECTID,OPTIONNUM)"
                + " VALUES (?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("VOTEOPTION INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected VoteOption rsToObject(ResultSet rs) throws SQLException {
        VoteOption entity = new VoteOption();

        //OPTIONID,DESCRIPTION,OPTIONTYPE,SUBJECTID,OPTIONNUM

        entity.setOptionId(rs.getLong("OPTIONID"));
        entity.setDescription(rs.getString("DESCRIPTION"));
        entity.setVoteOptionType(VoteOptionType.getByCode(rs.getString("OPTIONTYPE")));
        entity.setSubjectID(rs.getString("SUBJECTID"));
        entity.setOptionNum(rs.getInt("OPTIONNUM"));

        return entity;
    }

}
