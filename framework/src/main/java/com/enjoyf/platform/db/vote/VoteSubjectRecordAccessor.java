package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.vote.VoteSubjectRecord;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-20
 * Time: 上午16:24
 * To change this template use File | Settings | File Templates.
 */
interface VoteSubjectRecordAccessor {

    //insert
    public VoteSubjectRecord insert(VoteSubjectRecord subjectRecord, Connection conn) throws DbException;

    //insertBatch
    public boolean insertBatch(String subjectId, Set<VoteSubjectRecord> subjectRecordSet, Connection conn) throws DbException;

    //get by id
    public VoteSubjectRecord getSubjectRecord(String subjectId, QueryExpress queryExpress, Connection conn) throws DbException;

    //query
    public List<VoteSubjectRecord> query(String subjectId, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<VoteSubjectRecord> query(String subjectId, QueryExpress queryExpress, Rangination range, Connection conn) throws DbException;

    public List<VoteSubjectRecord> queryByExpress(String subjectId, QueryExpress queryExpress, Connection conn) throws DbException;

    //update
    public int updateVoteSubjectRecord(String subjectId, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
