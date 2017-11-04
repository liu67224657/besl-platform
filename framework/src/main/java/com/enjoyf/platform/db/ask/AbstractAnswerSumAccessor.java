package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ask.AnswerSum;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractAnswerSumAccessor extends AbstractBaseTableAccessor<AnswerSum> implements AnswerSumAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAnswerSumAccessor.class);

    private static final String KEY_TABLE_NAME = "answer_sum";

    @Override
    public AnswerSum insert(AnswerSum answerSum, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setLong(1, answerSum.getAnswerId());
            pstmt.setInt(2, answerSum.getViewSum());
            pstmt.setInt(3, answerSum.getReplySum());
            pstmt.setInt(4, answerSum.getAgreeSum());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return answerSum;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(answer_id,view_sum,reply_sum,agree_sum) VALUES (?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AnswerSum insert sql" + sql);
        }
        return sql;
    }

    @Override
    public AnswerSum get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<AnswerSum> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AnswerSum> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected AnswerSum rsToObject(ResultSet rs) throws SQLException {

        AnswerSum returnObject = new AnswerSum();

        returnObject.setAnswerId(rs.getLong("answer_id"));
        returnObject.setViewSum(rs.getInt("view_sum"));
        returnObject.setReplySum(rs.getInt("reply_sum"));
        returnObject.setAgreeSum(rs.getInt("agree_sum"));


        return returnObject;
    }
}