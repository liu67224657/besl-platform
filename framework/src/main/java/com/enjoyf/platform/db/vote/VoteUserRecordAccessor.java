package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.vote.VoteOption;
import com.enjoyf.platform.service.vote.VoteUserRecord;
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
interface VoteUserRecordAccessor{

    //insert
    public VoteUserRecord insert(VoteUserRecord voteUserRecord, Connection conn) throws DbException;

    //get by id
    public VoteUserRecord getVoteUserRecord(String voteUno, QueryExpress queryExpress, Connection conn) throws DbException;

    //query
    public List<VoteUserRecord> query(String voteUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<VoteUserRecord> query(String voteUno, QueryExpress queryExpress, Rangination range, Connection conn) throws DbException;
    
    public List<VoteUserRecord> queryByExpress(String voteUno, QueryExpress queryExpress, Connection conn) throws DbException;

    //update
    public int updateVoteUserRecord(String voteUno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
