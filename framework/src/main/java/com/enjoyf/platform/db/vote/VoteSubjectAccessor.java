package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.vote.VoteSubject;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-20
 * Time: 上午10:03
 * To change this template use File | Settings | File Templates.
 */
interface VoteSubjectAccessor {

    //insert
    public VoteSubject insert(VoteSubject voteSubject, Connection conn) throws DbException;

    //increase num
    public boolean increase(String subjectId, ObjectField field, Integer value, Connection conn) throws DbException;

    //get by id
    public VoteSubject get(QueryExpress queryExpress, Connection conn) throws DbException;

    //query
    public List<VoteSubject> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<VoteSubject> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException;

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
