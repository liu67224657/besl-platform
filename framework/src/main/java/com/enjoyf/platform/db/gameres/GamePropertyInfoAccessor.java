package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.GamePropertyInfo;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-11-20
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
public interface GamePropertyInfoAccessor {

    public List<GamePropertyInfo> batchInsert(List<GamePropertyInfo> infoList, Connection conn) throws DbException;

    public List<GamePropertyInfo> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GamePropertyInfo> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
