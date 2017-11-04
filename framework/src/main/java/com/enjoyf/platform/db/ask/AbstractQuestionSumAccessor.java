package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ask.QuestionSum;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public abstract class AbstractQuestionSumAccessor extends AbstractBaseTableAccessor<QuestionSum> implements QuestionSumAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractQuestionSumAccessor.class);

    private static final String KEY_TABLE_NAME = "question_sum";

    @Override
    public QuestionSum insert(QuestionSum questionSum, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setLong(1, questionSum.getQuestionId());
            pstmt.setInt(2, questionSum.getViewSum());
            pstmt.setInt(3, questionSum.getAnsewerSum());
            pstmt.setInt(4, questionSum.getFollowSum());
            pstmt.setTimestamp(5,new Timestamp(questionSum.getQuestionCreateTime().getTime()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return questionSum;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(question_id,view_sum,ansewer_sum,follow_sum,question_create_time) VALUES (?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("WanbaQuestionSum insert sql" + sql);
        }
        return sql;
    }

    @Override
    public QuestionSum get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<QuestionSum> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<QuestionSum> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected QuestionSum rsToObject(ResultSet rs) throws SQLException {
        QuestionSum returnObject = new QuestionSum();
        returnObject.setQuestionId(rs.getLong("question_id"));
        returnObject.setViewSum(rs.getInt("view_sum"));
        returnObject.setAnsewerSum(rs.getInt("ansewer_sum"));
        returnObject.setFollowSum(rs.getInt("follow_sum"));
        returnObject.setQuestionCreateTime(rs.getTimestamp("question_create_time"));

        return returnObject;
    }
}