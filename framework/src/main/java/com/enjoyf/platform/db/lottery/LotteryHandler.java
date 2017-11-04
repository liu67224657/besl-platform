package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.*;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.lottery.LotteryAddress;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-24
 * Time: 下午1:02
 * To change this template use File | Settings | File Templates.
 */
public class LotteryHandler {


    private DataBaseType dataBaseType;
    private String dataSourceName;

    private LotteryAccessor lotteryAccessor;
    private LotteryAwardAccessor lotteryAwardAccessor;
    private LotteryAwardItemAccessor lotteryAwardItemAccessor;
    private UserLotteryLogAccessor userLotteryLogAccessor;
    private UserDayLotteryAccessor userDayLotteryAccessor;

    private LotteryAddressAccessor lotteryAddressAccessor;
//    private TicketAccessor ticketAccessor;
//    private TicketAwardAccessor ticketAwardAccessor;

    public LotteryHandler(String dsn, FiveProps fiveProps) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the datasource
        DataSourceManager.get().append(dataSourceName, fiveProps);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        lotteryAccessor = TableAccessorFactory.get().factoryAccessor(LotteryAccessor.class, dataBaseType);
        lotteryAwardAccessor = TableAccessorFactory.get().factoryAccessor(LotteryAwardAccessor.class, dataBaseType);
        lotteryAwardItemAccessor = TableAccessorFactory.get().factoryAccessor(LotteryAwardItemAccessor.class, dataBaseType);
        userLotteryLogAccessor = TableAccessorFactory.get().factoryAccessor(UserLotteryLogAccessor.class, dataBaseType);
        userDayLotteryAccessor = TableAccessorFactory.get().factoryAccessor(UserDayLotteryAccessor.class, dataBaseType);

//        ticketAccessor = TableAccessorFactory.get().factoryAccessor(TicketAccessor.class, dataBaseType);
        lotteryAddressAccessor = TableAccessorFactory.get().factoryAccessor(LotteryAddressAccessor.class, dataBaseType);
    }


    public Lottery insertLottery(Lottery lottery) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAccessor.insert(lottery, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<Lottery> queryLottery(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<Lottery> queryLotteryByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<Lottery> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<Lottery> list = lotteryAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<Lottery>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Lottery getLottery(long lotteryId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAccessor.get(lotteryId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateLottery(UpdateExpress updateExpress, long lotteryId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAccessor.update(updateExpress, lotteryId, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public LotteryAward insertLotteryAward(LotteryAward lotteryAward) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAwardAccessor.insert(lotteryAward, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<LotteryAward> queryLotteryAward(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<LotteryAward> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<LotteryAward> list = lotteryAwardAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<LotteryAward>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public LotteryAward getById(long lotteryAwardId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAwardAccessor.getById(lotteryAwardId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public LotteryAward getByRate(long lotteryId, int randomNum) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAwardAccessor.getByRate(lotteryId, randomNum, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public LotteryAward getByLevel(int awardLevel, long lotteryId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAwardAccessor.getByLevel(awardLevel, lotteryId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean updateLotteryAward(UpdateExpress updateExpress, long lotteryAwardId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAwardAccessor.update(updateExpress, lotteryAwardId, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<LotteryAward> queryLotteryAward(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAwardAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public LotteryAwardItem insertLotteryAwardItem(LotteryAwardItem item) throws ServiceException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            item=lotteryAwardItemAccessor.insert(item, conn);

            return item;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int batchInsertLotteryAwardItem(List<LotteryAwardItem> lotteryAwardItemList) throws ServiceException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            int returnInt = lotteryAwardItemAccessor.batchInsert(lotteryAwardItemList, conn);

            conn.commit();
            return returnInt;
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
            GAlerter.lab("On commit batchInsertAwardItem occured SQLException. ", e);
            throw new DbException(e);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<LotteryAwardItem> queryLotteryAwardItem(long lotteryAwardId, Pagination pagination) throws DbException {
        PageRows<LotteryAwardItem> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<LotteryAwardItem> list = lotteryAwardItemAccessor.query(lotteryAwardId, pagination, conn);
            pageRows = new PageRows<LotteryAwardItem>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<LotteryAwardItem> queryLotteryAwardItem(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAwardItemAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public LotteryAwardItem getLotteryAwardItem(long lotteryAwardItemId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAwardItemAccessor.get(lotteryAwardItemId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateLotteryAwardItemById(UpdateExpress updateExpress, long lotteryAwardItemId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAwardItemAccessor.update(updateExpress, lotteryAwardItemId, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public UserLotteryLog insertUserLotteryLog(UserLotteryLog userLotteryLog) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userLotteryLogAccessor.insert(userLotteryLog, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<UserLotteryLog> queryUserLotteryLogByUser(long lotteryId, String uno, Date from, Date to, Pagination pagination) throws DbException {
        PageRows<UserLotteryLog> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<UserLotteryLog> list = userLotteryLogAccessor.queryByUser(lotteryId, uno, from, to, pagination, conn);
            pageRows = new PageRows<UserLotteryLog>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public List<UserLotteryLog> queryUserLotteryLog(long lotteryId, String uno) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<UserLotteryLog> list = userLotteryLogAccessor.queryByUser(lotteryId, uno, conn);
            return list;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserLotteryLog getUserLotteryLog(long lotteryId, String uno) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userLotteryLogAccessor.getByUser(lotteryId, uno, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<UserLotteryLog> queryLastestLotteryLog(long lotteryId, int size) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userLotteryLogAccessor.queryByLastestByLotteryId(lotteryId, size, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public PageRows<UserDayLottery> queryUserDayLottery(long lotteryId, Date date, Pagination pagination) throws DbException {
        PageRows<UserDayLottery> pageRows = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            List<UserDayLottery> list = userDayLotteryAccessor.query(lotteryId, date, pagination, conn);
            pageRows = new PageRows<UserDayLottery>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserDayLottery getUserDayLottery(long lotteryId, String uno, Date date) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userDayLotteryAccessor.get(lotteryId, uno, date, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserDayLottery insertUserDayLottery(UserDayLottery userDayLottery) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userDayLotteryAccessor.insert(userDayLottery, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateUserDayLottery(UpdateExpress updateExpress, long lotteryId, String uno, Date createDate) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userDayLotteryAccessor.update(updateExpress, lotteryId, uno, createDate, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AwardEntry userLotteryAwardEntry(String uno, String screenName, String ip, Date lotteryDate, int randomNum, long lotteryId, LotteryType lotteryType, AwardEntry awardEntry) throws ServiceException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            Lottery lottery = lotteryAccessor.get(lotteryId, conn);
            if (lottery == null) {
                throw new ServiceException(LotteryServiceException.LOTTERY_NOT_EXISTS, "lottery not exists.lotteryId: " + lotteryId);
            }


            if (LotteryType.LOTTERY_TYPE_MUST_REAWARD.equals(lotteryType)) {
                awardEntry = getAwardEntryByMustReward(awardEntry, lotteryId, randomNum, conn); //百分百中奖
            } else {
                awardEntry = getAwardEntryByCommon(awardEntry, lotteryId, randomNum, conn);//普通抽奖
            }

            //没有奖品
            if (awardEntry.getAward() == null && awardEntry.getAwardItem() == null) {
                UserDayLottery userDayLottery = userDayLotteryAccessor.get(lotteryId, uno, lotteryDate, conn);
                if (userDayLottery == null) {
                    userDayLottery = new UserDayLottery();
                    userDayLottery.setLotteryId(lotteryId);
                    userDayLottery.setLotteryTimes(1);
                    userDayLottery.setLotteryDate(lotteryDate);
                    userDayLottery.setUserNo(uno);

                    userDayLottery = userDayLotteryAccessor.insert(userDayLottery, conn);
                    if (userDayLottery == null) {
                        throw new ServiceException(LotteryServiceException.USER_DAY_LOTTERY_INSERT_FAILD, "user day lottery insert failed.lotteryId: " + lotteryId);
                    }
                } else {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.increase(UserDayLotteryField.LOTTERY_TIMES, 1);
                    boolean bool = userDayLotteryAccessor.update(updateExpress, lotteryId, uno, lotteryDate, conn) > 0;
                    if (!bool) {
                        throw new ServiceException(LotteryServiceException.USER_DAY_LOTTERY_INSERT_FAILD, "user day lottery insert failed.lotteryId: " + lotteryId);
                    }
                }
                awardEntry.setDayLottery(userDayLottery);

                UserLotteryLog userLotteryLog = new UserLotteryLog();
                userLotteryLog.setUno(uno);
                userLotteryLog.setScreenName(screenName);
                userLotteryLog.setLotteryId(lotteryId);
                userLotteryLog.setLotteryDate(lotteryDate);
                userLotteryLog.setLotteryIp(ip);

                userLotteryLog = userLotteryLogAccessor.insert(userLotteryLog, conn);
                if (userLotteryLog == null) {
                    throw new ServiceException(LotteryServiceException.USER_LOTTERY_LOG_INSERT_FAILD, "user lottery log insert failed.lotteryId: " + lotteryId);
                }
                conn.commit();
                return awardEntry;
            }

            //虚拟奖品  但是  没有子奖品
            if (awardEntry.getAward() != null && awardEntry.getAward().getLotteryAwardType().equals(LotteryAwardType.VIRTUAL) && awardEntry.getAwardItem() == null) {
                UserDayLottery userDayLottery = userDayLotteryAccessor.get(lotteryId, uno, lotteryDate, conn);
                if (userDayLottery == null) {
                    userDayLottery = new UserDayLottery();
                    userDayLottery.setLotteryId(lotteryId);
                    userDayLottery.setLotteryTimes(1);
                    userDayLottery.setLotteryDate(lotteryDate);
                    userDayLottery.setUserNo(uno);

                    userDayLottery = userDayLotteryAccessor.insert(userDayLottery, conn);
                    if (userDayLottery == null) {
                        throw new ServiceException(LotteryServiceException.USER_DAY_LOTTERY_INSERT_FAILD, "user day lottery insert failed.lotteryId: " + lotteryId);
                    }
                } else {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.increase(UserDayLotteryField.LOTTERY_TIMES, 1);
                    boolean bool = userDayLotteryAccessor.update(updateExpress, lotteryId, uno, lotteryDate, conn) > 0;
                    if (!bool) {
                        throw new ServiceException(LotteryServiceException.USER_DAY_LOTTERY_INSERT_FAILD, "user day lottery insert failed.lotteryId: " + lotteryId);
                    }
                }
                awardEntry.setDayLottery(userDayLottery);

                UserLotteryLog userLotteryLog = new UserLotteryLog();
                userLotteryLog.setUno(uno);
                userLotteryLog.setScreenName(screenName);
                userLotteryLog.setLotteryId(lotteryId);
                userLotteryLog.setLotteryDate(lotteryDate);
                userLotteryLog.setLotteryIp(ip);

                userLotteryLog = userLotteryLogAccessor.insert(userLotteryLog, conn);
                if (userLotteryLog == null) {
                    throw new ServiceException(LotteryServiceException.USER_LOTTERY_LOG_INSERT_FAILD, "user lottery log insert failed.lotteryId: " + lotteryId);
                }
                conn.commit();
                return awardEntry;
            }

            UserDayLottery userDayLottery = new UserDayLottery();
            userDayLottery.setLotteryId(lotteryId);
            userDayLottery.setLotteryTimes(1);
            userDayLottery.setLotteryDate(lotteryDate);
            userDayLottery.setUserNo(uno);

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.increase(UserDayLotteryField.LOTTERY_TIMES, 1);
            if (userDayLotteryAccessor.update(updateExpress, lotteryId, uno, lotteryDate, conn) <= 0) {

                userDayLottery = userDayLotteryAccessor.insert(userDayLottery, conn);
                if (userDayLottery == null) {
                    throw new ServiceException(LotteryServiceException.USER_DAY_LOTTERY_INSERT_FAILD, "user day lottery insert failed.lotteryId: " + lotteryId);
                } else {
                    awardEntry.setDayLottery(userDayLottery);
                }
            } else {
                awardEntry.setDayLottery(userDayLottery);
            }

            //奖品剩余数量 -1  如果失败 throw
            if (lotteryAwardAccessor.update(new UpdateExpress().increase(LotteryAwardField.LOTTERY_AWARD_REST_AMOUNT, -1), awardEntry.getAward().getLotteryAwardId(), conn) == 0) {
                throw new ServiceException(LotteryServiceException.LOTTERY_AWARD_UPDATE_FAILED, "lottery award update failed.lotteryId: " + lotteryId);
            } else {
                awardEntry.setIncreaseAwardRest(-1);
            }

            //虚拟奖品下  子奖品  抽奖状态修改为  已抽中
            if (LotteryAwardType.VIRTUAL.equals(awardEntry.getAward().getLotteryAwardType())) {
                boolean lotteryAwardItemSuccess = lotteryAwardItemAccessor.update(new UpdateExpress()
                        .set(LotteryAwardItemField.LOTTERY_STATUS, ValidStatus.INVALID.getCode())
                        .set(LotteryAwardItemField.LOTTERY_DATE, lotteryDate)
                        .set(LotteryAwardItemField.OWN_UNO, uno), awardEntry.getAwardItem().getLotteryAwardItemId(), conn) > 0;
                //修改失败
                if (!lotteryAwardItemSuccess) {
                    throw new ServiceException(LotteryServiceException.LOTTERY_AWARD_ITEM_UPDATE_FAILED, "lottery award item update failed.lotteryId: " + lotteryId);
                } else {
                    awardEntry.setUpdateAwardItemStatus(ValidStatus.INVALID.getCode());
                }
            }

            UserLotteryLog userLotteryLog = new UserLotteryLog();
            userLotteryLog.setUno(uno);
            userLotteryLog.setScreenName(screenName);
            userLotteryLog.setLotteryId(lotteryId);
            userLotteryLog.setLotteryDate(lotteryDate);
            userLotteryLog.setLotteryIp(ip);
            userLotteryLog.setLotteryAwardId(awardEntry.getAward().getLotteryAwardId());
            userLotteryLog.setLotteryAwardName(awardEntry.getAward().getLotteryAwardName());
            userLotteryLog.setLotteryAwardDesc(awardEntry.getAward().getLotteryAwardDesc());
            userLotteryLog.setLotteryAwardPic(awardEntry.getAward().getLotteryAwardPic());
            userLotteryLog.setLotteryAwardLevel(awardEntry.getAward().getLotteryAwardLevel());
            if (awardEntry.getAward().getLotteryAwardType().equals(LotteryAwardType.VIRTUAL)) {
                userLotteryLog.setLotteryAwardItemId(awardEntry.getAwardItem().getLotteryAwardItemId());
                userLotteryLog.setName1(awardEntry.getAwardItem().getName1());
                userLotteryLog.setValue1(awardEntry.getAwardItem().getValue1());
                userLotteryLog.setName2(awardEntry.getAwardItem().getName2());
                userLotteryLog.setValue2(awardEntry.getAwardItem().getValue2());
            }

            userLotteryLog = userLotteryLogAccessor.insert(userLotteryLog, conn);
            if (userLotteryLog == null) {
                throw new ServiceException(LotteryServiceException.USER_LOTTERY_LOG_INSERT_FAILD, "user lottery log insert failed.lotteryId: " + lotteryId);
            }

            conn.commit();
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
            resetAwardEntry(conn, awardEntry);
            try {
                conn.commit();
            } catch (SQLException e1) {
                GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e);
                throw new DbException(e1);
            }
            throw new DbException(e);
        } catch (ServiceException e) {
            DataBaseUtil.rollbackConnection(conn);
            resetAwardEntry(conn, awardEntry);
            try {
                conn.commit();
            } catch (SQLException e1) {
                GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e);
                throw new DbException(e1);
            }
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return awardEntry;
    }

    private void resetAwardEntry(Connection conn, AwardEntry awardEntry) {
        if (awardEntry.getAward() == null && awardEntry.getAwardItem() == null) {
            if (awardEntry.getDayLottery() != null) {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(UserDayLotteryField.LOTTERY_TIMES, -1);
                try {
                    userDayLotteryAccessor.update(updateExpress, awardEntry.getDayLottery().getLotteryId(), awardEntry.getDayLottery().getUserNo(), awardEntry.getDayLottery().getLotteryDate(), conn);
                } catch (DbException e) {
                    GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e);
                }
            }
        } else if (awardEntry.getAward() != null && awardEntry.getAwardItem() == null) {
            if (awardEntry.getDayLottery() != null) {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(UserDayLotteryField.LOTTERY_TIMES, -1);
                try {
                    userDayLotteryAccessor.update(updateExpress, awardEntry.getDayLottery().getLotteryId(), awardEntry.getDayLottery().getUserNo(), awardEntry.getDayLottery().getLotteryDate(), conn);
                } catch (DbException e) {
                    GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e);
                }
            }
        } else if (awardEntry.getAward() != null && awardEntry.getAwardItem() != null) {
            if (awardEntry.getDayLottery() != null) {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(UserDayLotteryField.LOTTERY_TIMES, -1);
                try {
                    userDayLotteryAccessor.update(updateExpress, awardEntry.getDayLottery().getLotteryId(), awardEntry.getDayLottery().getUserNo(), awardEntry.getDayLottery().getLotteryDate(), conn);
                } catch (DbException e) {
                    GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e);
                }
            }
            if (awardEntry.getIncreaseAwardRest() == -1) {
                try {
                    lotteryAwardAccessor.update(new UpdateExpress().increase(LotteryAwardField.LOTTERY_AWARD_REST_AMOUNT, 1), awardEntry.getAward().getLotteryAwardId(), conn);
                } catch (DbException e) {
                    GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e);
                }
            }
            if (awardEntry.getUpdateAwardItemStatus().equals(ValidStatus.INVALID.getCode())) {
                try {
                    lotteryAwardItemAccessor.update(new UpdateExpress()
                            .set(LotteryAwardItemField.LOTTERY_STATUS, ValidStatus.INVALID.getCode())
                            .set(LotteryAwardItemField.LOTTERY_DATE, awardEntry.getAwardItem().getLotteryDate())
                            .set(LotteryAwardItemField.OWN_UNO, awardEntry.getAwardItem().getOwnUserNo()), awardEntry.getAwardItem().getLotteryAwardItemId(), conn);
                } catch (DbException e) {
                    GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e);
                }
            }
        }

    }
//
//    public Ticket insertTicket(Ticket ticket) throws DbException {
//        Connection conn = null;
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//            return ticketAccessor.insert(ticket, conn);
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
//
//    public boolean modifyTicket(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
//        Connection conn = null;
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//            return ticketAccessor.update(updateExpress, queryExpress, conn) > 0;
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
//
//    public List<Ticket> queryTicket(QueryExpress queryExpress) throws DbException {
//        Connection conn = null;
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//            return ticketAccessor.query(queryExpress, conn);
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
//
//
//    public PageRows<Ticket> queryTicket(QueryExpress queryExpress, Pagination pagination) throws DbException {
//        Connection conn = null;
//        PageRows<Ticket> returnObj = new PageRows<Ticket>();
//
//        try {
//
//            conn = DbConnFactory.factory(dataSourceName);
//            List<Ticket> list = ticketAccessor.query(queryExpress, pagination, conn);
//
//            returnObj.setRows(list);
//            returnObj.setPage(pagination);
//
//            return returnObj;
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
//
//    public Ticket getTicket(QueryExpress queryExpress) throws DbException {
//        Connection conn = null;
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//
//            return ticketAccessor.get(queryExpress, conn);
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
//
//    public TicketAward createTicketAward(TicketAward ticketAward) throws DbException {
//        Connection conn = null;
//        try {
//
//            conn = DbConnFactory.factory(dataSourceName);
//
//            return ticketAwardAccessor.insert(ticketAward, conn);
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
//
//    public List<TicketAward> queryTicketAward(QueryExpress queryExpress) throws DbException {
//        Connection conn = null;
//
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//
//            return ticketAwardAccessor.query(queryExpress, conn);
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
//
//    public PageRows<TicketAward> queryTicketAward(QueryExpress queryExpress, Pagination pagination) throws DbException {
//        Connection conn = null;
//        PageRows<TicketAward> returnObj = new PageRows<TicketAward>();
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//            List<TicketAward> list = ticketAwardAccessor.query(queryExpress, pagination, conn);
//            returnObj.setRows(list);
//            returnObj.setPage(pagination);
//
//            return returnObj;
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
//
//    public TicketAward getTicketAward(QueryExpress queryExpress) throws DbException {
//        Connection conn = null;
//        try {
//
//            conn = DbConnFactory.factory(dataSourceName);
//            return ticketAwardAccessor.get(queryExpress, conn);
//
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }
//
//    public boolean modifyTicketAward(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
//        Connection conn = null;
//
//        try {
//            conn = DbConnFactory.factory(dataSourceName);
//            return ticketAwardAccessor.update(updateExpress, queryExpress, conn) > 0;
//        } finally {
//            DataBaseUtil.closeConnection(conn);
//        }
//    }


    private AwardEntry getAwardEntryByMustReward(AwardEntry awardEntry, long lotteryId, int randomNum, Connection conn) throws DbException {
        LotteryAward lotteryAward = lotteryAwardAccessor.getByRate(lotteryId, randomNum, conn);

        if (lotteryAward == null || lotteryAward.getLotteryAwardRestAmount() <= 0) {
            lotteryAward = lotteryAwardAccessor.getByRestAmount(lotteryAward.getLotteryId(), conn);
        }

        if (lotteryAward == null) {
            return awardEntry;
        }

        if (lotteryAward.getLotteryAwardType().equals(LotteryAwardType.GOODS) || lotteryAward.getLotteryAwardType().equals(LotteryAwardType.POINT)) {
            awardEntry = new AwardEntry(null, lotteryAward);
            return awardEntry;
        }

        LotteryAwardItem awardItem = lotteryAwardItemAccessor.get(lotteryAward.getLotteryAwardId(), ValidStatus.VALID, conn);
        if (awardItem == null) {
            return awardEntry;
        }
        awardEntry = new AwardEntry(awardItem, lotteryAward);
        return awardEntry;
    }

    private AwardEntry getAwardEntryByCommon(AwardEntry awardEntry, long lotteryId, int randomNum, Connection conn) throws DbException {
        LotteryAward lotteryAward = lotteryAwardAccessor.getByRate(lotteryId, randomNum, conn);
        if (lotteryAward == null || lotteryAward.getLotteryAwardRestAmount() <= 0) {
            return awardEntry;
        }
        if (lotteryAward.getLotteryAwardType().equals(LotteryAwardType.GOODS) || lotteryAward.getLotteryAwardType().equals(LotteryAwardType.POINT)) {
            awardEntry = new AwardEntry(null, lotteryAward);
        } else {
            LotteryAwardItem awardItem = lotteryAwardItemAccessor.get(lotteryAward.getLotteryAwardId(), ValidStatus.VALID, conn);
            //item 为空
            if (awardItem != null) {
                awardEntry = new AwardEntry(awardItem, lotteryAward);
            }
        }
        return awardEntry;
    }

    public UserLotteryLog userLotteryAward(String uno, String screenName, String ip, Date lotteryDate, int randomNum, long lotteryId, LotteryType lotteryType) throws ServiceException {
        Connection conn = null;
        UserLotteryLog returnLog = null;
        AwardEntry awardEntry = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            Lottery lottery = lotteryAccessor.get(lotteryId, conn);
            if (lottery == null) {
                throw new ServiceException(LotteryServiceException.LOTTERY_NOT_EXISTS, "lottery not exists.lotteryId: " + lotteryId);
            }

            if (LotteryType.LOTTERY_TYPE_MUST_REAWARD.equals(lotteryType)) {
                awardEntry = getAwardEntryByMustReward(awardEntry, lotteryId, randomNum, conn); //百分百中奖
            } else {
                awardEntry = getAwardEntryByCommon(awardEntry, lotteryId, randomNum, conn);//普通抽奖
            }

            //没有奖品
            if (awardEntry.getAward() == null && awardEntry.getAwardItem() == null) {
                UserDayLottery userDayLottery = new UserDayLottery();
                userDayLottery.setLotteryId(lotteryId);
                userDayLottery.setLotteryTimes(1);
                userDayLottery.setLotteryDate(lotteryDate);
                userDayLottery.setUserNo(uno);

                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(UserDayLotteryField.LOTTERY_TIMES, 1);
                if (userDayLotteryAccessor.update(updateExpress, lotteryId, uno, lotteryDate, conn) <= 0) {
                    userDayLottery = userDayLotteryAccessor.insert(userDayLottery, conn);
                    if (userDayLottery == null) {
                        throw new ServiceException(LotteryServiceException.USER_DAY_LOTTERY_INSERT_FAILD, "user day lottery insert failed.lotteryId: " + awardEntry.getAward().getLotteryId());
                    } else {
                        awardEntry.setDayLottery(userDayLottery);
                    }
                } else {
                    awardEntry.setDayLottery(userDayLottery);
                }

                UserLotteryLog userLotteryLog = new UserLotteryLog();
                userLotteryLog.setUno(uno);
                userLotteryLog.setScreenName(screenName);
                userLotteryLog.setLotteryId(lotteryId);
                userLotteryLog.setLotteryDate(lotteryDate);
                userLotteryLog.setLotteryIp(ip);

                returnLog = userLotteryLogAccessor.insert(userLotteryLog, conn);
                if (returnLog == null) {
                    throw new ServiceException(LotteryServiceException.USER_LOTTERY_LOG_INSERT_FAILD, "user lottery log insert failed.lotteryId: " + lotteryId);
                } else {
                    throw new ServiceException(LotteryServiceException.LOTTERY_AWARD_OUTOF_REST_AMMOUNT, "lottery has end.lotteryId: " + lotteryId);
                }
            }

            //虚拟奖品  但是  没有子奖品
            if (awardEntry.getAward() != null && awardEntry.getAward().getLotteryAwardType().equals(LotteryAwardType.VIRTUAL) && awardEntry.getAwardItem() == null) {
                UserDayLottery userDayLottery = new UserDayLottery();
                userDayLottery.setLotteryId(lotteryId);
                userDayLottery.setLotteryTimes(1);
                userDayLottery.setLotteryDate(lotteryDate);
                userDayLottery.setUserNo(uno);

                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(UserDayLotteryField.LOTTERY_TIMES, 1);
                if (userDayLotteryAccessor.update(updateExpress, lotteryId, uno, lotteryDate, conn) <= 0) {
                    userDayLottery = userDayLotteryAccessor.insert(userDayLottery, conn);
                    if (userDayLottery == null) {
                        throw new ServiceException(LotteryServiceException.USER_DAY_LOTTERY_INSERT_FAILD, "user day lottery insert failed.lotteryId: " + awardEntry.getAward().getLotteryId());
                    } else {
                        awardEntry.setDayLottery(userDayLottery);
                    }
                } else {
                    awardEntry.setDayLottery(userDayLottery);
                }

                UserLotteryLog userLotteryLog = new UserLotteryLog();
                userLotteryLog.setUno(uno);
                userLotteryLog.setScreenName(screenName);
                userLotteryLog.setLotteryId(lotteryId);
                userLotteryLog.setLotteryDate(lotteryDate);
                userLotteryLog.setLotteryIp(ip);

                returnLog = userLotteryLogAccessor.insert(userLotteryLog, conn);
                if (returnLog == null) {
                    throw new ServiceException(LotteryServiceException.USER_LOTTERY_LOG_INSERT_FAILD, "user lottery log insert failed.lotteryId: " + lotteryId);
                } else {
                    throw new ServiceException(LotteryServiceException.LOTTERY_AWARD_OUTOF_REST_AMMOUNT, "lottery has end.lotteryId: " + lotteryId);
                }
            }

            UserDayLottery userDayLottery = new UserDayLottery();
            userDayLottery.setLotteryId(lotteryId);
            userDayLottery.setLotteryTimes(1);
            userDayLottery.setLotteryDate(lotteryDate);
            userDayLottery.setUserNo(uno);

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.increase(UserDayLotteryField.LOTTERY_TIMES, 1);
            if (userDayLotteryAccessor.update(updateExpress, lotteryId, uno, lotteryDate, conn) <= 0) {

                userDayLottery = userDayLotteryAccessor.insert(userDayLottery, conn);
                if (userDayLottery == null) {
                    throw new ServiceException(LotteryServiceException.USER_DAY_LOTTERY_INSERT_FAILD, "user day lottery insert failed.lotteryId: " + awardEntry.getAward().getLotteryId());
                } else {
                    awardEntry.setDayLottery(userDayLottery);
                }
            } else {
                awardEntry.setDayLottery(userDayLottery);
            }

            //奖品剩余数量 -1  如果失败 throw
            if (lotteryAwardAccessor.update(new UpdateExpress().increase(LotteryAwardField.LOTTERY_AWARD_REST_AMOUNT, -1), awardEntry.getAward().getLotteryAwardId(), conn) == 0) {
                throw new ServiceException(LotteryServiceException.LOTTERY_AWARD_UPDATE_FAILED, "lottery award update failed.lotteryId: " + lotteryId);
            } else {
                awardEntry.setIncreaseAwardRest(-1);
            }

            //虚拟奖品下  子奖品  抽奖状态修改为  已抽中
            if (LotteryAwardType.VIRTUAL.equals(awardEntry.getAward().getLotteryAwardType())) {
                boolean lotteryAwardItemSuccess = lotteryAwardItemAccessor.update(new UpdateExpress()
                        .set(LotteryAwardItemField.LOTTERY_STATUS, ValidStatus.INVALID.getCode())
                        .set(LotteryAwardItemField.LOTTERY_DATE, lotteryDate)
                        .set(LotteryAwardItemField.OWN_UNO, uno), awardEntry.getAwardItem().getLotteryAwardItemId(), conn) > 0;
                //修改失败
                if (!lotteryAwardItemSuccess) {
                    throw new ServiceException(LotteryServiceException.LOTTERY_AWARD_ITEM_UPDATE_FAILED, "lottery award item update failed.lotteryId: " + lotteryId);
                } else {
                    awardEntry.setUpdateAwardItemStatus(ValidStatus.INVALID.getCode());
                }
            }

            UserLotteryLog userLotteryLog = new UserLotteryLog();
            userLotteryLog.setUno(uno);
            userLotteryLog.setScreenName(screenName);
            userLotteryLog.setLotteryId(lotteryId);
            userLotteryLog.setLotteryDate(lotteryDate);
            userLotteryLog.setLotteryIp(ip);
            userLotteryLog.setLotteryAwardId(awardEntry.getAward().getLotteryAwardId());
            userLotteryLog.setLotteryAwardName(awardEntry.getAward().getLotteryAwardName());
            userLotteryLog.setLotteryAwardDesc(awardEntry.getAward().getLotteryAwardDesc());
            userLotteryLog.setLotteryAwardPic(awardEntry.getAward().getLotteryAwardPic());
            userLotteryLog.setLotteryAwardLevel(awardEntry.getAward().getLotteryAwardLevel());
            if (awardEntry.getAward().getLotteryAwardType().equals(LotteryAwardType.VIRTUAL)) {
                userLotteryLog.setLotteryAwardItemId(awardEntry.getAwardItem().getLotteryAwardItemId());
                userLotteryLog.setName1(awardEntry.getAwardItem().getName1());
                userLotteryLog.setValue1(awardEntry.getAwardItem().getValue1());
                userLotteryLog.setName2(awardEntry.getAwardItem().getName2());
                userLotteryLog.setValue2(awardEntry.getAwardItem().getValue2());
            }

            returnLog = userLotteryLogAccessor.insert(userLotteryLog, conn);
            if (returnLog == null) {
                throw new ServiceException(LotteryServiceException.USER_LOTTERY_LOG_INSERT_FAILD, "user lottery log insert failed.lotteryId: " + lotteryId);
            }

            conn.commit();
        } catch (SQLException e) {
            DataBaseUtil.rollbackConnection(conn);
            resetAwardEntry(conn, awardEntry);
            try {
                conn.commit();
            } catch (SQLException e1) {
                GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e);
                throw new DbException(e1);
            }
            throw new DbException(e);
        } catch (ServiceException e) {
            DataBaseUtil.rollbackConnection(conn);
            resetAwardEntry(conn, awardEntry);
            try {
                conn.commit();
            } catch (SQLException e1) {
                GAlerter.lab(this.getClass().getName() + " occoured SQLException.e:", e);
                throw new DbException(e1);
            }
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return returnLog;
    }

    public List<UserLotteryLog> queryUserLotteryLogByQuery(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return userLotteryLogAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public LotteryAddress insertLotteryAddress(LotteryAddress lotteryAddress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAddressAccessor.insert(lotteryAddress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public LotteryAddress getLotteryAddress(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAddressAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyLotteryAddress(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAddressAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyLotteryAwardItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return lotteryAwardItemAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

}
