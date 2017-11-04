package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.lottery.UserDayLottery;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-24
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public interface UserDayLotteryAccessor {

    public UserDayLottery insert(UserDayLottery userDayLottery, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, long userDayLotteryId, Date date, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, long lotteryId, String uno, Date date, Connection conn) throws DbException;

    public List<UserDayLottery> query(long lotteryId, Date date, Pagination pagination, Connection conn) throws DbException;

    public List<UserDayLottery> query(long lotteryId, Date date, Connection conn) throws DbException;

    public UserDayLottery get(long userDayLotteryId, Date date, Connection conn) throws DbException;

    public UserDayLottery get(long lotteryId, String uno, Date date, Connection conn) throws DbException;

}
