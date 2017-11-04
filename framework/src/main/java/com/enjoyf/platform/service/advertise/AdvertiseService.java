/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.advertise;

import com.enjoyf.platform.service.advertise.app.*;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-6-25 下午5:51
 * Description:
 */
public interface AdvertiseService extends EventReceiver {
    ////////////////////////////////////////////////////
    //the agent apis
    ////////////////////////////////////////////////////

    //create the agent.
    @RPC
    public AdvertiseAgent createAgent(AdvertiseAgent agent) throws ServiceException;

    //get the agent
    @RPC
    public AdvertiseAgent getAgent(String agentId) throws ServiceException;

    //update the agent
    @RPC
    public boolean modifyAgent(UpdateExpress updateExpress, String agentId) throws ServiceException;

    //query the agents
    @RPC
    public PageRows<AdvertiseAgent> queryPageAgents(QueryExpress queryExpress, Pagination page) throws ServiceException;

    @RPC
    public List<AdvertiseAgent> queryAgents(QueryExpress queryExpress) throws ServiceException;

    ////////////////////////////////////////////////////
    //the project apis
    ////////////////////////////////////////////////////
    //create the project.
    @RPC
    public AdvertiseProject createProject(AdvertiseProject project) throws ServiceException;

    //get the project
    @RPC
    public AdvertiseProject getProject(String projectId) throws ServiceException;

    //update the project
    @RPC
    public boolean modifyProject(UpdateExpress updateExpress, String projectId) throws ServiceException;

    //query the projects
    @RPC
    public PageRows<AdvertiseProject> queryPageProjects(QueryExpress queryExpress, Pagination page) throws ServiceException;

    @RPC
    public List<AdvertiseProject> queryProjects(QueryExpress queryExpress) throws ServiceException;

    ////////////////////////////////////////////////////
    //the publish apis
    ////////////////////////////////////////////////////

    //create the publish.
    @RPC
    public AdvertisePublish createPublish(AdvertisePublish publish) throws ServiceException;

    //get the publish
    @RPC
    public AdvertisePublish getPublish(String publishId) throws ServiceException;

    //update the publish
    @RPC
    public boolean modifyPublish(UpdateExpress updateExpress, String publishId) throws ServiceException;

    //query the publishs
    @RPC
    public PageRows<AdvertisePublish> queryPagePublishs(QueryExpress queryExpress, Pagination page) throws ServiceException;

    //添加综合查询——广告位信息（广告商信息+广告项目信息）
    @RPC
    public PageRows<AdvertisePublish> queryPagePublishsByState(QueryExpress queryExpress, Pagination page, boolean state) throws ServiceException;

    @RPC
    public List<AdvertisePublish> queryPublishs(QueryExpress queryExpress) throws ServiceException;


    ////////////////////////////////////////////////////
    //the publish location apis
    ////////////////////////////////////////////////////

    //create the publish location.
    @RPC
    public AdvertisePublishLocation createPublishLocation(AdvertisePublishLocation location) throws ServiceException;

    //get the publish location
    @RPC
    public AdvertisePublishLocation getPublishLocation(String publishId, String locationCode) throws ServiceException;

    //update the publish location
    @RPC
    public boolean modifyPublishLocation(UpdateExpress updateExpress, String publishId, String locationCode) throws ServiceException;

    //query the publish locations
    @RPC
    public PageRows<AdvertisePublishLocation> queryPagePublishLocations(QueryExpress queryExpress, Pagination page) throws ServiceException;

    @RPC
    public List<AdvertisePublishLocation> queryPublishLocations(QueryExpress queryExpress) throws ServiceException;

    //recieve the player event
    @RPC
    public boolean receiveEvent(Event event) throws ServiceException;

    //////////////////////////////////////////////////
    //AdvertiseAppUrl method
    /////////////////////////////////////////////////

    @RPC
    public AdvertiseAppUrl insertAppUrl(AdvertiseAppUrl advertiseAppUrl) throws ServiceException;

    //前台调用 从缓存取得
    @RPC
    public AdvertiseAppUrl getAppUrlByCode(String code) throws ServiceException;

    //修改时使用
    @RPC
    public AdvertiseAppUrl getAppUrl(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public boolean modifyAppUrl(UpdateExpress updateExpress, QueryExpress queryExpress, String code) throws ServiceException;

    @RPC
    public List<AdvertiseAppUrl> listAppUrl(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<AdvertiseAppUrl> pageAppUrl(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    //////////////////////////////////////////////////
    //AdvertiseAppUrl method
    /////////////////////////////////////////////////
    @RPC
    public AppAdvertise createAppAdvertise(AppAdvertise appAdvertise) throws ServiceException;
    
    @RPC
    public boolean modifyAppAdvertise(UpdateExpress updateExpress,long appAdvertiseId) throws ServiceException;

    @RPC
    public PageRows<AppAdvertise> queryAppAdvertise(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public AppAdvertise getAppAdvertise(long appAdvertiseId) throws ServiceException;

    @RPC
    public AppAdvertisePublish createAppAdertisePublish(AppAdvertisePublish publish) throws ServiceException;

     @RPC
    public AppAdvertisePublish getAppAdvertisePublish(long publishId) throws ServiceException;

    @RPC
    public boolean modifyAppAdvertisePublish(UpdateExpress updateExpress,long publishId) throws ServiceException;

    @RPC
    public PageRows<AppAdvertisePublish> queryAppAdvertisePublish(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public List<AppAdvertiseInfo> queryPublishingAdvertise(String appKey,AppAdvertisePublishType type,Date now)throws ServiceException;

    @RPC
    public List<AppAdvertisePublish> queryAppAdvertisePublishByCache(String appId, AppAdvertisePublishType publishType, String channel) throws ServiceException;

    @RPC 
    public Map<Long,AppAdvertise> queryAppAdvertiseByIdSet(Set<Long> advertiseIdSet, int platform) throws ServiceException;

    @RPC
    public AppAdvertisePv createAppAdvertisePvMongo(AppAdvertisePv appAdvertisePv) throws ServiceException;

    @RPC
    public AdvertiseAppUrlClickInfo reportAppUrlClick(AdvertiseAppUrlClickInfo info)throws ServiceException;

    /**
     * 返回的存在的deviceId
     * @param appKey
     * @param deviceId
     * @return
     * @throws ServiceException
     */
    @RPC
    Set<String> checkActivityDevice(String appKey, Set<String> deviceId) throws ServiceException;
}
