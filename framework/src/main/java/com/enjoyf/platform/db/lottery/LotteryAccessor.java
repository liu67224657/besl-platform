package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.lottery.Lottery;
import com.enjoyf.platform.service.point.Goods;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-24
 * Time: 下午1:07
 * To change this template use File | Settings | File Templates.
 */
public interface LotteryAccessor {

    public Lottery insert(Lottery lottery, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, long lotteryId, Connection conn) throws DbException;

    public List<Lottery> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<Lottery> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public Lottery get(long lotteryId, Connection conn) throws DbException;

}
