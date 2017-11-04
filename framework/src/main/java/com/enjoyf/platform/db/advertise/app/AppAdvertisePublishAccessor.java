package com.enjoyf.platform.db.advertise.app;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublish;
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
public interface AppAdvertisePublishAccessor {

    public AppAdvertisePublish insert(AppAdvertisePublish publish,Connection conn)throws DbException;

    public List<AppAdvertisePublish> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<AppAdvertisePublish> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public AppAdvertisePublish get(long publishId, Connection conn) throws DbException ;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn)throws DbException;
}
