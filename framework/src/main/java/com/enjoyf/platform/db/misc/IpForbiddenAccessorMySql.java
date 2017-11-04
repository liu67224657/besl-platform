package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.example.Example;
import com.enjoyf.platform.service.misc.IpForbidden;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-4-10
 * Time: 下午6:34
 * To change this template use File | Settings | File Templates.
 */
public class IpForbiddenAccessorMySql extends AbstractIpForbiddenAccessor {
    @Override
    public List<IpForbidden> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, page, conn);
    }

    @Override
    public List<IpForbidden> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, conn);
    }
}
