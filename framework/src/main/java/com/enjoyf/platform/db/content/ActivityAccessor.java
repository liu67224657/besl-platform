package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.Activity;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-12
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public interface ActivityAccessor {

    public Activity insert(Activity activity, Connection conn) throws DbException;

    public Activity get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Activity> select(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Activity> select(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int modify(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
