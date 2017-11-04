package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.example.Example;
import com.enjoyf.platform.service.vote.VoteSubject;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-20
 * Time: 上午10:13
 * To change this template use File | Settings | File Templates.
 */
public class VoteSubjectAccessorMySql extends AbstractVoteSubjectAccessor {
    @Override
    public List<VoteSubject> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, page, conn);
    }

    @Override
    public List<VoteSubject> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, range, conn);
    }
}
