package com.enjoyf.platform.service.search;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;

/**
 * Created by IntelliJ IDEA.
 * User: zhaoxin
 * Date: 11-8-25
 * Time: 上午8:12
 * To change this template use File | Settings | File Templates.
 */
public class SearchServiceImpl implements SearchService {
    ReqProcessor reqProcessor;

    public SearchServiceImpl(ServiceConfigNaming cfg) {
        reqProcessor = cfg.getReqProcessor();
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(event);
        Request req = new Request(SearchConstants.RECIEVE_EVENT, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public PageRows<SearchGiftResultEntry> searchGiftByText(SearchGiftCriteria criteria, Pagination page) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(criteria);
        wp.writeSerializable(page);

        Request req = new Request(SearchConstants.SEARCH_GIFT, wp);

        RPacket rp = reqProcessor.process(req);
        return (PageRows<SearchGiftResultEntry>) rp.readSerializable();
    }



}
