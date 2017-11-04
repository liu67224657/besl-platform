package com.enjoyf.platform.service.billing;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-6
 * Time: 下午6:01
 * To change this template use File | Settings | File Templates.
 */
public interface BillingService extends EventReceiver {

    public DepositLog createDepositLog(DepositLog log) throws ServiceException;


    public boolean modifyDepositLog(IntValidStatus syncStatus, String errorMsg, String logId) throws ServiceException;


    public PageRows<DepositLog> queryDepositLogQueryExpressPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public boolean receiveEvent(Event event) throws ServiceException;


    /**
     * 根据sql语句查数据
     * @param sql
     * @return
     * @throws ServiceException
     */

    public String queryBySql(String sql)throws ServiceException;

    public boolean checkReceipt(String receipt) throws ServiceException;

    public boolean setReceipt(String receipt) throws ServiceException;
}
