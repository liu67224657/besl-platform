package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.RefreshCMSTiming;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by zhimingli on 2015/7/28.
 */
public interface RefreshCMSTimingReleaseAccessor {

    public RefreshCMSTiming insert(RefreshCMSTiming refreshCMSTiming, Connection conn) throws DbException;

    public List<RefreshCMSTiming> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<RefreshCMSTiming> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public RefreshCMSTiming get(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;
}
