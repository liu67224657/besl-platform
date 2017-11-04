package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.point.UserConsumeLog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.apache.openjpa.util.QueryException;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public interface UserConsumeLogAccessor {

    public UserConsumeLog insert(UserConsumeLog consumeLog, Connection conn) throws DbException;

    public List<UserConsumeLog> queryByUser(String profileId, Date from, Date to, Pagination pagination, Connection conn) throws DbException;

    public List<UserConsumeLog> queryByUserGoodsIdConsumeTime(String uno, long goodsId, Date consumeTime, Connection conn) throws DbException;


    public List<UserConsumeLog> queryUserConsumeLog(QueryExpress queryExpress, Pagination pagination, GoodsActionType type, Connection conn) throws DbException;

    public List<UserConsumeLog> queryByUserGoodsId(String uno, long goodsId, String consumeOrder, Connection conn) throws DbException;

    public List<UserConsumeLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
