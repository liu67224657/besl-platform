package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.message.PushMessage;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-2
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class PushMsgAccessorMySql extends AbstractPushMsgAccessor {
    @Override
    public List<PushMessage> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, page, conn);
    }

    @Override
    public List<PushMessage> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, range, conn);
    }
}
