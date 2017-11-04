/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.NextPageRows;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:51
 * Description:
 */
public interface TimeLineService extends EventReceiver {
    //////////////////////////////////////////////
    //the timeline apis
    //////////////////////////////////////////////
    //insert timeline item, in a queue.
    @RPC(partitionHashing = 0)
    public boolean insertTimeLine(String ownUno, TimeLineItem item) throws ServiceException;

    //query whose time line the uno, the timeline domain, before time and size.
    @RPC(partitionHashing = 0)
    public List<TimeLineItem> queryTimeLinesBefore(String ownUno, TimeLineDomain domain, Long before, Integer size) throws ServiceException;

    //query whose time line the uno, the timeline domain, before time and size.
    @RPC(partitionHashing = 0)
    public List<TimeLineItem> queryTimeLinesAfter(String ownUno, TimeLineDomain domain, Long after, Integer size) throws ServiceException;

    //query whose time line the uno and the timeline type.
    @RPC(partitionHashing = 0)
    public PageRows<TimeLineItem> queryTimeLines(String ownUno, TimeLineDomain domain, Pagination page) throws ServiceException;

    //query whose time line the uno and the timeline type and  org.
    @RPC(partitionHashing = 0)
    public PageRows<TimeLineItem> queryTimeLinesOrg(String ownUno, TimeLineDomain domain, Pagination page) throws ServiceException;

    //query whose time line the uno and the timeline type and  focus.
    @RPC(partitionHashing = 0)
    public PageRows<TimeLineItem> queryTimeLinesOnlyFocus(String ownUno, TimeLineDomain domain, Pagination page) throws ServiceException;

    @RPC(partitionHashing = 0)
    public PageRows<TimeLineItem> queryTimeLinesByFilterType(String ownUno, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page) throws ServiceException;

    @RPC(partitionHashing = 0)
    public PageRows<TimeLineItem> queryTimeLinesByFilterTypeRelationId(String ownUno, String relationId, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page) throws ServiceException;

    //update the time info status. remove, etc.
    @RPC(partitionHashing = 0)
    public boolean removeTimeLine(String ownUno, TimeLineDomain domain, String directId) throws ServiceException;

    //////////////////////////////////////////////
    //the event server apis
    //////////////////////////////////////////////
    //recieve the player event
    @RPC(partitionHashing = 0)
    public boolean receiveEvent(Event event) throws ServiceException;


    @RPC(partitionHashing = 0)
    public SocialTimeLineItem insertSocialTimeLineItem(SocialTimeLineDomain socialTimeLineDomain, String ownUno, SocialTimeLineItem socialTimeLineItem) throws ServiceException;

    @RPC(partitionHashing = 0)
    public PageRows<SocialTimeLineItem> querySocialTimeLineItemList(SocialTimeLineDomain socialTimeLineDomain, String ownUno, Pagination page) throws ServiceException;

    @RPC(partitionHashing = 0)
    public SocialTimeLineItem getSocialTimeLineItem(String ownUno, SocialTimeLineDomain socialTimeLineDomain, Long sid) throws ServiceException;

    @RPC(partitionHashing = 0)
    NextPageRows<SocialTimeLineItem> querySocialTimeLineItemNextList(SocialTimeLineDomain home, String uno, NextPagination nextPagination) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean modifySocialTimeLineItem(SocialTimeLineDomain socialTimeLineDomain, String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    @RPC(partitionHashing = 0)
    public PageRows<String> queryUGCWikiUserDynamicCache(SocialTimeLineDomain socialTimeLineDomain, SocialTimeLineItemType socialTimeLineItemType, String profileId, Pagination page);

    @RPC(partitionHashing = 0)
    public boolean removeAllUGCWikiUserDynamicCache(SocialTimeLineDomain socialTimeLineDomain, SocialTimeLineItemType socialTimeLineItemType, String profileId, SocialTimeLineItem socialTimeLineItem);

    //build the user timeline.
    @RPC
    public UserTimeline buildUserTimeline(UserTimeline timeline) throws ServiceException;

    //
    @RPC
    public PageRows<UserTimeline> queryUserTimeline(String profileid, String domain, String type, Pagination page) throws ServiceException;

    //get the relation with someone
    @RPC
    public UserTimeline getUserTimeline(String profileid,String type,String domain,String actionType) throws ServiceException;

    @RPC
    public boolean delUserTimeline(UserTimeline userTimeline)throws ServiceException;
}
