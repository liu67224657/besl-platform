package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.Pagination;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public interface StatService extends EventReceiver {
    //report the stat result item, it's reported by other buz server.
    @RPC
    public boolean reportStat(StatItem item) throws ServiceException;

    //report the stat result items, it's reported by other buz server.
    @RPC
    public boolean reportStatBatch(List<StatItem> items) throws ServiceException;

    //get stat itme.
    @RPC
    public StatItem getStatItem(StatDomain domain, StatSection section, StatDateType dateType, Date statDate) throws ServiceException;

    //query the statitme in time scope.
    @RPC
    public List<StatItem> queryStatItems(StatDomain domain, StatSection section, StatDateType dateType, Date from, Date to) throws ServiceException;

    //query the selected domain's section items in a certain stat date.
    @RPC
    public Map<StatSection, StatItem> queryStatItemsByDomain(StatDomain domain, StatDateType dateType, Date statDate) throws ServiceException;

    //query the selected domain's section items in a certain stat date.
    @RPC
    public PageStatSectionItems queryPageStatSectionItemsByDomain(StatDomain domain, StatDateType dateType, Date statDate, Pagination p) throws ServiceException;

    //query the selected domain's section items in a certain period stat date.
    @RPC
    public Map<Date, Map<StatSection, StatItem>> queryStatItemsByDomainPeriod(StatDomain domain, StatDateType dateType, Date startDate, Date endDate) throws ServiceException;

    @RPC
    public boolean receiveEvent(Event event) throws ServiceException;

    @RPC
    public List<StatItem> queryStatItemsByQuery(String advertiseId, StatDomainDefault domain, StatSectionDefault section, StatDateType dateType, Date from, Date to) throws ServiceException;


}
