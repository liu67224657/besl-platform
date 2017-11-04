package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.event.Activity;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-5
 * Time: 下午5:26
 * To change this template use File | Settings | File Templates.
 */
public interface ActivityAccessor {

    public Activity insert(Activity activity, Connection conn) throws DbException;

    public Activity get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Activity> query(QueryExpress queryExpress,Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

}
