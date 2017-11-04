package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.lottery.LotteryAward;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-24
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public interface LotteryAwardAccessor {

    public LotteryAward insert(LotteryAward lotteryAward, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, long lotteryAwardId, Connection conn) throws DbException;

    public List<LotteryAward> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<LotteryAward> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public LotteryAward getById(long lotteryAwardId, Connection conn) throws DbException;

    public LotteryAward getByRate(long lotteryId, int randomNum, Connection conn) throws DbException;

    public LotteryAward getByLevel(int awardLevel, long lotteryId, Connection conn) throws DbException;

    public LotteryAward getByRestAmount(long lotteryId, Connection conn) throws DbException;

}
