package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.UserDayPoint;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-13
 * Time: 上午10:17
 * To change this template use File | Settings | File Templates.
 */
public interface UserDayPointAccessor {
    /**
     * 添加 每日积分记录
     * @param dayPoint
     * @param conn
     * @return
     * @throws DbException
     */
    public UserDayPoint insert(UserDayPoint dayPoint, Connection conn) throws DbException;

    /**
     * 查询 得到 满足条件的 一条记录
     * @param queryExpress
     * @param conn
     * @return
     * @throws DbException
     */
    public UserDayPoint get(QueryExpress queryExpress, Date date, Connection conn) throws  DbException;

    public List<UserDayPoint> query(QueryExpress queryExpress, Date date, Connection conn) throws  DbException;

    /**
     * 分页查询 多条记录
     * @param queryExpress
     * @param pagination
     * @param conn
     * @return
     * @throws DbException
     */
    public List<UserDayPoint> query(QueryExpress queryExpress, Date date, Pagination pagination, Connection conn) throws DbException;

    /**
     * 修改一条记录
     * @param updateExpress
     * @param queryExpress
     * @param conn
     * @return
     * @throws DbException
     */
    public int update(UpdateExpress updateExpress,QueryExpress queryExpress,Date date, Connection conn) throws DbException;
}
