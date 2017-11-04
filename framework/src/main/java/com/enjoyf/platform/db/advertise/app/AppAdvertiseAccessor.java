package com.enjoyf.platform.db.advertise.app;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.app.AppAdvertise;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  2014/6/9 15:10
 * Description:
 */
public interface AppAdvertiseAccessor {

    public AppAdvertise insert(AppAdvertise appAdvertise, Connection conn)throws DbException;

    public List<AppAdvertise> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public AppAdvertise get(long appAdvertiseId, Connection conn)throws DbException;
    
    public List<AppAdvertise> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    //update
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
