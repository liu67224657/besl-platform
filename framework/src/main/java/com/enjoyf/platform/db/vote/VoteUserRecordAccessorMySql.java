package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.vote.VoteUserRecord;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-20
 * Time: 上午16:24
 * To change this template use File | Settings | File Templates.
 */
public class VoteUserRecordAccessorMySql extends AbstractVoteUserRecordAccessor {
    @Override
    public List<VoteUserRecord> query(String voteUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(getTableName(voteUno), queryExpress, page, conn);
    }

    @Override
    public List<VoteUserRecord> query(String voteUno, QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return query(getTableName(voteUno), queryExpress, range, conn);
    }

    @Override
    public List<VoteUserRecord> queryByExpress(String voteUno, QueryExpress queryExpress, Connection conn) throws DbException {
        return query(getTableName(voteUno), queryExpress, conn);
    }
}
