package com.enjoyf.platform.service.billing;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-6
 * Time: 下午6:01
 * To change this template use File | Settings | File Templates.
 */
public class BillingServiceImpl implements BillingService {

    private ReqProcessor reqProcessor = null;

    public BillingServiceImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("BillingServiceImpl.ctor: ServiceConfig is null!");
        }

        reqProcessor = scfg.getReqProcessor();
    }

    @Override
    public DepositLog createDepositLog(DepositLog log) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(log);

        Request req = new Request(BillingConstants.DEPOSIT_LOG_CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (DepositLog) rPacket.readSerializable();
    }

    @Override
    public boolean modifyDepositLog(IntValidStatus syncStatus, String errorMsg, String logId) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(syncStatus);
        wPacket.writeStringUTF(errorMsg);
        wPacket.writeStringUTF(logId);

        Request req = new Request(BillingConstants.DEPOSIT_SYNC_MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public PageRows<DepositLog> queryDepositLogQueryExpressPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(pagination);

        Request req = new Request(BillingConstants.QUERY_DEPOSITLOG_LIST, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (PageRows<DepositLog>) rPacket.readSerializable();
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        return false;
    }


    /**
     * 根据sql语句查数据
     *
     * @param sql
     * @return
     * @throws com.enjoyf.platform.service.service.ServiceException
     */
    @Override
    public String queryBySql(String sql) throws ServiceException {

        WPacket wPacket = new WPacket();

        wPacket.writeStringUTF(sql);

        Request req = new Request(BillingConstants.QUERY_BY_SQL, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readStringUTF();
    }

    @Override
    public boolean checkReceipt(String receipt) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeStringUTF(receipt);

        Request req = new Request(BillingConstants.CHECK_RECEIPT, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public boolean setReceipt(String receipt) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeStringUTF(receipt);

        Request req = new Request(BillingConstants.SET_RECEIPT, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }
}
