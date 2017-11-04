package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ask.AnswerSum;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

public interface AnswerSumAccessor {

    public AnswerSum insert(AnswerSum answerSum, Connection conn) throws DbException;

    public AnswerSum get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AnswerSum> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AnswerSum> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
