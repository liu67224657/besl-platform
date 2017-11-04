package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午2:15
 * To change this template use File | Settings | File Templates.
 */
public interface UserPointAccessor {

    /**
     * 添加 userPoint
     * @param userPoint
     * @param conn
     * @return
     * @throws DbException
     */
    public UserPoint insert(UserPoint userPoint, Connection conn) throws DbException;

    /**
     * 查询 by key
     * @param queryExpress
     * @param conn
     * @return
     * @throws DbException
     */
    public UserPoint get(QueryExpress queryExpress, Connection conn) throws  DbException;

    /**
     * 分页查询
     * @param queryExpress
     * @param pagination
     * @param conn
     * @return
     * @throws DbException
     */
    public List<UserPoint> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    /**
     * 修改 暂时没用到
     * @param updateExpress
     * @param queryExpress
     * @param conn
     * @return
     * @throws DbException
     */
    public int update(UpdateExpress updateExpress,QueryExpress queryExpress,Connection conn) throws DbException;
}
