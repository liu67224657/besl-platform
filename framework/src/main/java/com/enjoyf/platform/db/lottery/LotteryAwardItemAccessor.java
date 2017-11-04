package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.LotteryAwardItem;
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
public interface LotteryAwardItemAccessor {

    public int batchInsert(List<LotteryAwardItem> lotteryAwardItemList, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, long lotteryAwardItemId, Connection conn) throws DbException;

    public List<LotteryAwardItem> query(long lotteryAwardId, Pagination pagination, Connection conn) throws DbException;

    public List<LotteryAwardItem> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public LotteryAwardItem get(long lotteryAwardId, ValidStatus lotteryStatus, Connection conn) throws DbException;

    public LotteryAwardItem get(long lotteryAwardItemId, Connection conn) throws DbException;


    public LotteryAwardItem insert(LotteryAwardItem item, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
