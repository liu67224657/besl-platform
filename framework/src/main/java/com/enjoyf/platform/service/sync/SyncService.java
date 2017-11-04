package com.enjoyf.platform.service.sync;

import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.List;
import java.util.Set;

/**
 * <p/>
 * Description:单例类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public interface SyncService extends EventReceiver {

    @RPC
    public ShareBaseInfo createShareInfo(ShareBaseInfo shareInfo) throws ServiceException;

    @RPC
    public PageRows<ShareBaseInfo> queryShareInfoByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;


    @RPC
    public List<ShareBaseInfo> queryShareInfo(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public ShareBaseInfo getShareInfo(QueryExpress queryExpress) throws ServiceException;

    /**
     * first get by cache
     *
     * @param shareId
     * @return
     * @throws ServiceException
     */
    @RPC
    public ShareBaseInfo getShareInfoById(long shareId) throws ServiceException;

    @RPC
    public boolean modifyShareInfo(UpdateExpress updateExpress, QueryExpress queryExpress, long shareId) throws ServiceException;

    /////////////////////////////////////////////////////////////////////////////////
    @RPC
    public ShareTopic createShareTopic(ShareTopic shareTopic) throws ServiceException;

    @RPC
    public PageRows<ShareTopic> queryShareTopicByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public List<ShareTopic> queryShareTopic(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public boolean modifyShareTopic(UpdateExpress updateExpress, QueryExpress queryExpress, long shareBaseInfoId) throws ServiceException;

    //////////////////////////////
    @RPC
    public ShareBody createShareBody(ShareBody shareBody) throws ServiceException;

    @RPC
    public List<ShareBody> queryShareBody(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<ShareBody> queryShareBodyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyShareBody(UpdateExpress updateExpress, QueryExpress queryExpress, long shareId) throws ServiceException;

    @RPC
    public ShareInfo choiceShareInfo(long shareId) throws ServiceException;

    //////////////////////////////

    @RPC
    public boolean syncShareInfo(SyncContent syncContent, TokenInfo tokenInfo, LoginDomain loginDomain, String profileId, long shareId) throws ServiceException;

    @RPC
    @Override
    boolean receiveEvent(Event event) throws ServiceException;
}
