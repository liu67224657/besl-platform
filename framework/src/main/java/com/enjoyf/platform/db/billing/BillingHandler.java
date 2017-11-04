package com.enjoyf.platform.db.billing;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.billing.*;
import com.enjoyf.platform.service.joymeapp.AppContentVersionInfo;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericLiu
 * Date: 12-1-10
 * Time: 下午5:52
 * To change this template use File | Settings | File Templates.
 */
public class BillingHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private DepositLogAccessor depositLogAccessor;
    private UserBalanceAccessor userBalanceAccessor;

    public BillingHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);

        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();


        depositLogAccessor = TableAccessorFactory.get().factoryAccessor(DepositLogAccessor.class, dataBaseType);
        userBalanceAccessor = TableAccessorFactory.get().factoryAccessor(UserBalanceAccessor.class, dataBaseType);
    }


    public DepositLog insertDepositLog(DepositLog log) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            //订单状态是成功，同时同步状态成功 用事务
            if (IntValidStatus.VALID.equals(log.getStatus()) && IntValidStatus.VALID.equals(log.getSyncStatus())) {
                try {
                    conn.setAutoCommit(false);
                    log = depositLogAccessor.insert(log, conn);

                    String balanceId = BillingUtil.getBalanceId(log.getProfileId(), log.getAppKey(), log.getZoneKey());
                    int result = userBalanceAccessor.update(new UpdateExpress().increase(UserBalanceField.AMOUNT, log.getAmount()), new QueryExpress()
                            .add(QueryCriterions.eq(UserBalanceField.BALANCE_ID, balanceId))
                            , conn);
                    if (result <= 0) {
                        UserBalance balance = new UserBalance();
                        balance.setAppKey(log.getAppKey());
                        balance.setProfileId(log.getProfileId());
                        balance.setZoneKey(log.getZoneKey());
                        balance.setAmount(log.getAmount());
                        userBalanceAccessor.insert(balance, conn);
                    }
                    conn.commit();
                } catch (SQLException e) {
                    GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
                    DataBaseUtil.rollbackConnection(conn);
                }

                return log;
            } else {
                return depositLogAccessor.insert(log, conn);
            }
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyDepositLog(UpdateExpress updateExpress, String logId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return depositLogAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(DepositLogField.LOGID, logId)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<DepositLog> queryDepositLogQueryExpress(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<DepositLog> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<DepositLog> list = depositLogAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<DepositLog>();

            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyBalance(int amount, String appKey, String profileId, String zoneKey) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            int result = userBalanceAccessor.update(new UpdateExpress().increase(UserBalanceField.AMOUNT, amount), new QueryExpress()
                    .add(QueryCriterions.eq(UserBalanceField.BALANCE_ID, BillingUtil.getBalanceId(profileId, appKey, zoneKey)))
                    , conn);
            if (result <= 0) {
                UserBalance balance = new UserBalance();
                balance.setAppKey(appKey);
                balance.setProfileId(profileId);
                balance.setZoneKey(zoneKey);
                balance.setAmount(amount);
                userBalanceAccessor.insert(balance, conn);
                return true;
            }
            return result > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public DepositLog getDepositLog(String logId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return depositLogAccessor.get(new QueryExpress().add(QueryCriterions.eq(DepositLogField.LOGID, logId)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public String queryStatBySql(String sql) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return depositLogAccessor.queryBySql(sql, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
