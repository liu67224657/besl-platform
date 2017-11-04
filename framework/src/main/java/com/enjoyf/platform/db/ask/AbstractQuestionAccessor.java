package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractQuestionAccessor extends AbstractBaseTableAccessor<Question> implements QuestionAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractQuestionAccessor.class);

    private static final String KEY_TABLE_NAME = "question";

    @Override
    public Question insert(Question question, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            //game_id,ask_profile_id,type,title,body,create_time,
            // question_point,time_limit,accept_answer_id,invite_profile_id,
            // remove_status,first_answer_id,richtext,askvoice,question_status
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, question.getGameId());
            pstmt.setString(2, question.getAskProfileId());
            pstmt.setInt(3, question.getType().getCode());
            pstmt.setString(4, question.getTitle());
            pstmt.setString(5, question.getBody().toJson());
            pstmt.setTimestamp(6, new Timestamp(question.getCreateTime().getTime()));
            pstmt.setInt(7, question.getQuestionPoint());
            pstmt.setLong(8, question.getTimeLimit());
            pstmt.setLong(9, question.getAcceptAnswerId());
            pstmt.setString(10, question.getInviteProfileId());
            pstmt.setInt(11, question.getRemoveStatus().getCode());
            pstmt.setLong(12, question.getFirstAnswerId());
            pstmt.setString(13, question.getRichText());
            if (question.getAskVoice() != null) {
                pstmt.setString(14, question.getAskVoice().toJson());
            } else {
                pstmt.setNull(14, Types.VARCHAR);
            }
            pstmt.setInt(15,question.getQuestionStatus().getCode());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                question.setQuestionId(rs.getLong(1));
            }

        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return question;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(game_id,ask_profile_id,type,title,body,create_time,question_point,time_limit,accept_answer_id,invite_profile_id,remove_status,first_answer_id,richtext,askvoice,question_status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("Question insert sql" + sql);
        }
        return sql;
    }

    @Override
    public Question get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<Question> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Question> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected Question rsToObject(ResultSet rs) throws SQLException {

        Question returnObject = new Question();

        returnObject.setQuestionId(rs.getLong("question_id"));
        returnObject.setGameId(rs.getLong("game_id"));
        returnObject.setAskProfileId(rs.getString("ask_profile_id"));
        returnObject.setType(QuestionType.getByCode(rs.getInt("type")));
        returnObject.setTitle(rs.getString("title"));
        returnObject.setBody(AskBody.toObject(rs.getString("body")));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setQuestionPoint(rs.getInt("question_point"));
        returnObject.setTimeLimit(rs.getLong("time_limit"));
        returnObject.setAcceptAnswerId(rs.getLong("accept_answer_id"));
        returnObject.setInviteProfileId(rs.getString("invite_profile_id"));
        returnObject.setRemoveStatus(IntValidStatus.getByCode(rs.getInt("remove_status")));
        returnObject.setFirstAnswerId(rs.getLong("first_answer_id"));
        returnObject.setRichText(rs.getString("richtext"));
        returnObject.setAskVoice(AskVoice.toObject(rs.getString("askvoice")));
        returnObject.setQuestionStatus(QuestionStatus.getByCode(rs.getInt("question_status")));
        returnObject.setReactivated(rs.getLong("reactivated"));
        return returnObject;
    }
}