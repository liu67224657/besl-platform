package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.event.ActivityAwardLog;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-6
 * Time: 上午9:13
 * To change this template use File | Settings | File Templates.
 */
public interface ActivityAwardLogAccessor {

    public ActivityAwardLog insert(ActivityAwardLog activity, Connection conn) throws DbException;

    public List<ActivityAwardLog> query(QueryExpress queryExpress,Connection conn) throws DbException;
}
