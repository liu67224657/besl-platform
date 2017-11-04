package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.UidProfileIdx;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/19
 * Description:
 */
public interface UidProfileIdxAccessor {

    public int bachInsert(List<UidProfileIdx> idx, Connection conn) throws DbException;

    public UidProfileIdx insert(UidProfileIdx idx, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;


    public UidProfileIdx get(QueryExpress queryExpess, Connection conn) throws DbException;
}
