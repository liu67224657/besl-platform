/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.GamePropDb;
import com.enjoyf.platform.service.misc.GamePropDbQueryParam;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public interface GamePropDbAccessor {

    public long getSeqNo(Connection connection) throws DbException;

    public GamePropDb insert(GamePropDb gamePropDb,String gamePropCode,Connection connection)throws DbException;

    //insert a feedback
    public List<GamePropDb> query(QueryExpress queryExpress, Pagination pagination, String gamePropCode, Connection conn) throws DbException;

    public List<GamePropDb> query(QueryExpress queryExpress,String gamePropCode, Connection conn) throws DbException;

    public List<GamePropDb> getByParamList(List<GamePropDbQueryParam> paramList, String gamePropCode, Connection conn) throws DbException;

    public GamePropDb get(QueryExpress getExpress, String gamePropCode, Connection conn) throws DbException;

    public void insertBatch(String gamePropCode, List<GamePropDb> gamePropDbList, Connection conn)throws DbException;
}
