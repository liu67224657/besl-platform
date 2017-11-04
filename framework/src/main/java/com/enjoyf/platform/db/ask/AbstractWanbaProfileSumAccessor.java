package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ask.WanbaProfileSum;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractWanbaProfileSumAccessor extends AbstractBaseTableAccessor<WanbaProfileSum> implements WanbaProfileSumAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWanbaProfileSumAccessor.class);

    private static final String KEY_TABLE_NAME = "wanba_profile_sum";

    @Override
    public WanbaProfileSum insert(WanbaProfileSum wanbaProfileSum, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, wanbaProfileSum.getProfileId());
            pstmt.setInt(2, wanbaProfileSum.getAnswerSum());
            pstmt.setInt(3, wanbaProfileSum.getAwardPoint());
            pstmt.setInt(4, wanbaProfileSum.getQuestionFollowSum());
            pstmt.setInt(5, wanbaProfileSum.getFavoriteSum());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return wanbaProfileSum;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profile_id,answer_sum,award_point,question_follow_sum,favorite_sum) VALUES (?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("WanbaProfileSum insert sql" + sql);
        }
        return sql;
    }

    @Override
    public WanbaProfileSum get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<WanbaProfileSum> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<WanbaProfileSum> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected WanbaProfileSum rsToObject(ResultSet rs) throws SQLException {

        WanbaProfileSum returnObject = new WanbaProfileSum();

        returnObject.setProfileId(rs.getString("profile_id"));
        returnObject.setAnswerSum(rs.getInt("answer_sum"));
        returnObject.setAwardPoint(rs.getInt("award_point"));
        returnObject.setQuestionFollowSum(rs.getInt("question_follow_sum"));
        returnObject.setFavoriteSum(rs.getInt("favorite_sum"));

        return returnObject;
    }
}