/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.service.example;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class ExampleServiceBeslImpl implements ExampleService {
    //
    private ReqProcessor reqProcessor = null;

    public ExampleServiceBeslImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("ExampleServiceBeslImpl.ctor: ServiceConfig is null!");
        }

        reqProcessor = scfg.getReqProcessor();
    }

    @Override
    public Example create(Example entry) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(entry);

        Request req = new Request(ExampleConstants.CREATE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (Example) rPacket.readSerializable();
    }

    @Override
    public Example get(QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ExampleConstants.GET, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (Example) rPacket.readSerializable();
    }

    @Override
    public PageRows<Example> query(QueryExpress queryExpress, Pagination page) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(page);

        Request req = new Request(ExampleConstants.QUERY_BY_PAGE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (PageRows<Example>) rPacket.readSerializable();
    }

    @Override
    public RangeRows<Example> query(QueryExpress queryExpress, Rangination range) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(queryExpress);
        wPacket.writeSerializable(range);

        Request req = new Request(ExampleConstants.QUERY_BY_RANGE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (RangeRows<Example>) rPacket.readSerializable();
    }

    @Override
    public boolean modify(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(updateExpress);
        wPacket.writeSerializable(queryExpress);

        Request req = new Request(ExampleConstants.MODIFY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }
}
