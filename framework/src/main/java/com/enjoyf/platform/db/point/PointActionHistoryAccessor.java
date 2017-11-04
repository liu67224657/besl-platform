package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public interface PointActionHistoryAccessor {

    /**
     * 添加 PointActionHistory
     * @param pointActionHistory
     * @param conn
     * @return
     * @throws DbException
     */
    public PointActionHistory insert(PointActionHistory pointActionHistory, Connection conn) throws DbException;

    /**
     * 查寻 通过主键key得到相应的 PointActionHistory
     * @param queryExpress
     * @param conn
     * @return
     * @throws DbException
     */
    public PointActionHistory get(QueryExpress queryExpress, Connection conn) throws  DbException;

    /**
     * 分页查询
     * @param queryExpress
     * @param pagination
     * @param conn
     * @return
     * @throws DbException
     */
    public List<PointActionHistory> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<PointActionHistory> query(QueryExpress queryExpress, Connection conn) throws DbException;


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
