package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.lottery.UserDayLottery;
import com.enjoyf.platform.service.lottery.UserLotteryLog;
import com.enjoyf.platform.service.point.UserConsumeLog;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public interface UserLotteryLogAccessor {

    public UserLotteryLog insert(UserLotteryLog lotteryLog, Connection conn) throws DbException;

    public List<UserLotteryLog> queryByUser(long lotteryId, String uno, Date from, Date to, Pagination pagination, Connection conn) throws DbException;

    public List<UserLotteryLog> queryByUser(long lotteryId, String uno, Connection conn) throws DbException;

    public UserLotteryLog getByUser(long lotteryId, String uno, Connection conn) throws DbException;

    public int queryRandomNum(long lotteryId, int maxNum, Connection conn) throws DbException;

    public List<UserLotteryLog> queryByLastestByLotteryId(long lotteryId, int size, Connection conn) throws DbException;

    public List<UserLotteryLog> query(QueryExpress queryExpress, Connection conn) throws DbException;
}
