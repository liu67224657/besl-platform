package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.ImageContentSet;
import com.enjoyf.platform.service.vote.VoteDomain;
import com.enjoyf.platform.service.vote.VoteSubject;
import com.enjoyf.platform.service.vote.VoteVisible;
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
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-20
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
class AbstractVoteSubjectAccessor extends AbstractBaseTableAccessor<VoteSubject> implements VoteSubjectAccessor {

    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractVoteSubjectAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "VOTE_SUBJECT";

    @Override
    public VoteSubject insert(VoteSubject voteSubject, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //SUBJECTID, SUBJECT, DIRECTION, IMAGE, CHOICENUM, EXPIREDTIME, VISIBLE
            //VOTEDOMAIN, VOTENUM, CREATEUSERNO, CREATETIME, CREATEIP

            pstmt.setString(1, voteSubject.getSubjectId());
            pstmt.setString(2, voteSubject.getSubject());
            pstmt.setString(3, voteSubject.getDirection());

            pstmt.setString(4, voteSubject.getImageSet() != null ? voteSubject.getImageSet().toJsonStr() : null);

            pstmt.setInt(5, voteSubject.getChoiceNum());
            pstmt.setTimestamp(6, voteSubject.getExpiredDate() != null ? new Timestamp(voteSubject.getExpiredDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(7, voteSubject.getVoteVisible().getCode());
            pstmt.setString(8, voteSubject.getVoteDomain().getCode());

            pstmt.setInt(9, voteSubject.getVoteNum());

            pstmt.setString(10, voteSubject.getCreateUno());
            pstmt.setTimestamp(11, voteSubject.getCreateDate() != null ? new Timestamp(voteSubject.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(12, voteSubject.getCreateIp());
            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert VoteSubject, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return voteSubject;
    }

    @Override
    public boolean increase(String subjectId, ObjectField field, Integer value, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + KEY_TABLE_NAME +
                " SET " + field.getColumn() + " = " + field.getColumn() + " + ? WHERE SUBJECTID = ? AND " + field.getColumn() + " >= 0";
        if (logger.isDebugEnabled()) {
            logger.debug("VoteSubject VOTENUM increase sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, value != null ? value : 1);
            pstmt.setString(2, subjectId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            GAlerter.lab("On  VoteSubject increase VOTENUM a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public VoteSubject get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<VoteSubject> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<VoteSubject> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    ///////////////////////////////////////////////////////

    ///private methods.
    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME
                + " (SUBJECTID, SUBJECT, DIRECTION, IMAGE, CHOICENUM, EXPIREDTIME, VISIBLE, " +
                "VOTEDOMAIN, VOTENUM, CREATEUSERNO, CREATETIME, CREATEIP)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("VoteSubject INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    protected VoteSubject rsToObject(ResultSet rs) throws SQLException {
        VoteSubject entry = new VoteSubject();

        //SUBJECTID, SUBJECT, DIRECTION, IMAGE, CHOICENUM, EXPIREDTIME, VISIBLE
        //VOTEDOMAIN, VOTENUM, CREATEUSERNO, CREATETIME, CREATEIP

        entry.setSubjectId(rs.getString("SUBJECTID"));

        entry.setSubject(rs.getString("SUBJECT"));
        entry.setDirection(rs.getString("DIRECTION"));
        entry.setImageSet(ImageContentSet.parse(rs.getString("IMAGE")));

        entry.setChoiceNum(rs.getInt("CHOICENUM"));
        entry.setExpiredDate(rs.getTimestamp("EXPIREDTIME") != null ? new Date(rs.getTimestamp("EXPIREDTIME").getTime()) : null);
        entry.setVoteVisible(VoteVisible.getByCode(rs.getString("VISIBLE")));
        entry.setVoteDomain(VoteDomain.getByCode(rs.getString("VOTEDOMAIN")));

        entry.setVoteNum(rs.getInt("VOTENUM"));

        entry.setCreateUno(rs.getString("CREATEUSERNO"));
        entry.setCreateDate(rs.getTimestamp("CREATETIME") != null ? new Date(rs.getTimestamp("CREATETIME").getTime()) : null);
        entry.setCreateIp(rs.getString("CREATEIP"));

        return entry;
    }

}
