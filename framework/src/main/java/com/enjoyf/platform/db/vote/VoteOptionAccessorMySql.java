package com.enjoyf.platform.db.vote;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.vote.VoteOption;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-20
 * Time: 上午16:24
 * To change this template use File | Settings | File Templates.
 */
public class VoteOptionAccessorMySql extends AbstractVoteOptionAccessor {
    @Override
    public List<VoteOption> queryOptions(String subjectId, QueryExpress queryExpress, Connection conn) throws DbException {
        return query(getTableName(subjectId), queryExpress, conn);
    }
}
