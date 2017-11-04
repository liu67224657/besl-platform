package com.enjoyf.platform.service.notice;

import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Collection;
import java.util.Map;

/**
 * Created by ericliu on 16/3/12.
 */
public interface NoticeService extends EventReceiver {

    @RPC
    Map<String, AppNoticeSum> queryAppNoticeSum(String profileId, String appKey, Collection<NoticeType> types) throws ServiceException;

    @RPC
    boolean readNotice(String profileId, String appkey, NoticeType type) throws ServiceException;

    @RPC
    AppNoticeSum querySystemNoticeSum(String profileId, String version, String platfrom, String appKey) throws ServiceException;


    ///////////////////////////////
    @RPC
    PageRows<SystemNotice> querySystemNoitce(String appKey, String version, String platform, Pagination page) throws ServiceException;

    @RPC
    PageRows<SystemNotice> querySystemNoticeByProfile(String profileId, String appkey, String version, String platform, Pagination pagination) throws ServiceException;

    @RPC
    PageRows<SystemNotice> querySystemNoitceByPage(QueryExpress queryExpress, Pagination page) throws ServiceException;

    @RPC
    boolean readSystemNotice(String profileId, String appKey, String version, String platform) throws ServiceException;

    @RPC
    SystemNotice getSystemNoitce(Long systemNoticeId) throws ServiceException;

    @RPC
    SystemNotice createSystemNotice(SystemNotice systemNotice) throws ServiceException;

    @RPC
    boolean modifySystemNotice(Long systemNoticeId, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    boolean deleteSystemNotice(String appkey, String version, String platform, Long systemNoticeId) throws ServiceException;

    /////////////
    @RPC
    PageRows<UserNotice> queryUserNotice(String profileId, String appKey, NoticeType type, Pagination page) throws ServiceException;

    @RPC
    boolean deleteUserNotice(String profileId, String appKey, String noticeType, long noticeId) throws ServiceException;

    @RPC
    boolean deleteUserNoticeAllByType(String profileId, String appkey, NoticeType noticeType) throws ServiceException;

    @RPC
    UserNotice getUserNotice(Long noticeId) throws ServiceException;

    @RPC
    boolean deleteUserNoticeByDestId(String profileId, String destId) throws ServiceException;

    @RPC
    @Override
    boolean receiveEvent(Event event) throws ServiceException;
}
