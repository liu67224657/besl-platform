package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ask.Answer;
import com.enjoyf.platform.service.ask.AskBody;
import com.enjoyf.platform.service.ask.AskVoice;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractAnswerAccessor extends AbstractBaseTableAccessor<Answer> implements AnswerAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAnswerAccessor.class);

    private static final String KEY_TABLE_NAME = "answer";

    @Override
    public Answer insert(Answer answer, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, answer.getAnswerProfileId());
            pstmt.setLong(2, answer.getQuestionId());
            pstmt.setString(3, answer.getBody().toJson());
            pstmt.setTimestamp(4, new Timestamp(answer.getAnswerTime().getTime()));
            pstmt.setBoolean(5, answer.getIsAccept());
            pstmt.setString(6, answer.getRichText());

            if (answer.getAskVoice() != null) {
                pstmt.setString(7, answer.getAskVoice().toJson());
            } else {
                pstmt.setNull(7, Types.VARCHAR);
            }
            pstmt.setInt(8, answer.getRemoveStatus().getCode());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                answer.setAnswerId(rs.getLong(1));
            }

        } catch (SQLException e) {
            logger.error("On insert answer, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return answer;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(answer_profile_id,question_id,body,answer_time,is_accept,richtext,askvoice,remove_status) VALUES (?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("Answer insert sql" + sql);
        }
        return sql;
    }

    @Override
    public Answer get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<Answer> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Answer> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected Answer rsToObject(ResultSet rs) throws SQLException {
        Answer returnObject = new Answer();
        returnObject.setAnswerId(rs.getLong("answer_id"));
        returnObject.setAnswerProfileId(rs.getString("answer_profile_id"));
        returnObject.setQuestionId(rs.getLong("question_id"));
        returnObject.setBody(AskBody.toObject(rs.getString("body")));
        returnObject.setRichText(rs.getString("richtext"));
        returnObject.setAnswerTime(new Date(rs.getTimestamp("answer_time").getTime()));
        returnObject.setIsAccept(rs.getBoolean("is_accept"));
        returnObject.setAskVoice(AskVoice.toObject(rs.getString("askvoice")));
        returnObject.setRemoveStatus(IntValidStatus.getByCode(rs.getInt("remove_status")));
        return returnObject;
    }
}