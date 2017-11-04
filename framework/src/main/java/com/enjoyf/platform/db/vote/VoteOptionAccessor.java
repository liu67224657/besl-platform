package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.vote.VoteOption;
import com.enjoyf.platform.service.vote.VoteRecordSet;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-20
 * Time: 上午13:06
 * To change this template use File | Settings | File Templates.
 */
interface VoteOptionAccessor {
    //insert
    public VoteOption insert(VoteOption voteOption, Connection conn) throws DbException;

    //insertBatch
    public Set<VoteOption> insertBatch(String subjectId, Set<VoteOption> voteOptions, Connection conn) throws DbException;

    //increaseBatch
    public boolean increaseBatch(String subjectId, VoteRecordSet recordSet, ObjectField field, Integer value, Connection conn) throws DbException;

    //get by id
    public VoteOption getVoteOption(String subjectId, QueryExpress queryExpress, Connection conn) throws DbException;

    //query
    public List<VoteOption> queryOptions(String subjectId, QueryExpress queryExpress, Connection conn) throws DbException;

    //update
    public int updateVoteOption(String subjectId, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
