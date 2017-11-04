/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.*;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:48
 * Description: 负责一些公共内容的服务，比方说IP，城市等。
 */
public interface MiscService {
    /////////////////////////////////////////////////////
    //region apis
    @RPC
    public List<Region> getAllRegions() throws ServiceException;

    /////////////////////////////////////////////////////
    //reg code apis
    @RPC
    public RegCode getRegCode(String regCode) throws ServiceException;

    @RPC
    public boolean updateApplyInfo(String regCode, String applyEmail, String applyReason) throws ServiceException;

    @RPC
    public boolean useRegCode(String regCode, String useUno, String useUserid) throws ServiceException;

    /////////////////////////////////////////////////////
    //reg code apply
    @RPC
    public RegCodeApply applyRegCode(RegCodeApply apply) throws ServiceException;

    @RPC
    public RegCodeApply getRegCodeApply(String email) throws ServiceException;

    @RPC
    public boolean appendApplyRegCode(String email, String regCode) throws ServiceException;


    //feedback apis
    @RPC
    public Feedback postFeedback(Feedback feedback) throws ServiceException;

    //增加一个IPForbidden
    @RPC
    public IpForbidden createIpForbidden(IpForbidden ipForbidden) throws ServiceException;

    //获得一个单个的IPForbidden实例
    @RPC
    public IpForbidden getIpForbidden(QueryExpress queryExpress) throws ServiceException;

    //查询一组IPForbidden实例，用于页面展示
    @RPC
    public PageRows<IpForbidden> queryIpForbiddens(QueryExpress queryExpress, Pagination paginatgion) throws ServiceException;

    //查询一组IPForbidden实例
    @RPC
    public List<IpForbidden> queryIpForbiddensNoPagination(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public boolean modifyIpForbidden(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public PageRows<GamePropDb> queryGamePropDb(QueryExpress queryExpress, String gamePropCode, Pagination pagination) throws ServiceException;

    @RPC
    public GamePropDb getGamePropDb(QueryExpress getExpress, String gamePropCode) throws ServiceException;

    /**
     * 通过查询条件，得到该DB下所有符合条件的数据根据keyId归类，每一个Map对应一个keyid的所有属性
     *
     * @return List<Map<String,GamePropDb>> 每条数据对应一个keyid下所有的属性
     * @throws ServiceException
     */
    @RPC
    public Map<Long, Map<String, Object>> queryGamePropDbByParam(List<List<GamePropDbQueryParam>> paramList, String gamePropCode) throws ServiceException;


    /**
     * 发布一条操作命令
     *
     * @param joymeOperate
     * @return
     * @throws ServiceException
     */
    @RPC
    public JoymeOperate createOperate(JoymeOperate joymeOperate) throws ServiceException;

    /**
     * 找到该机器运行的最后一个该类型的操作，
     * 在从joymeoperate中查找该类型的log还有多少没执行的
     *
     * @param serviceId
     * @param operateType
     * @return
     * @throws ServiceException
     */
    @RPC
    public List<JoymeOperate> doOperate(String serviceId, JoymeOperateType operateType) throws ServiceException;

    /**
     * 执行后写入operateLog
     *
     * @param operateLog
     * @return
     * @throws ServiceException
     */
    @RPC
    public JoymeOperateLog createOperateLog(JoymeOperateLog operateLog) throws ServiceException;


    @RPC
    public IndexCache createIndexCache(IndexCache indexCache) throws ServiceException;

    @RPC
    public boolean modifyIndexCache(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    @RPC
    public List<IndexCache> queryIndexCacheList(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public IndexCache getIndexCache(QueryExpress queryExpress) throws ServiceException;

    /**
     * 如果为空返回当前最大值的html，如果不为空，返回的indexCacheId和参数一样说明不需要更新
     *
     * @param indexCacheId
     * @param cacheType
     * @return
     * @throws ServiceException
     */
    @RPC
    public IndexCache getRecentIndexCacheById(Long indexCacheId, IndexCacheType cacheType) throws ServiceException;

    @RPC
    public SMSLog createSMS(SMSLog smsLog) throws ServiceException;

    /**
     * @param phone   手机号
     * @param content 内容
     * @param type    类型
     * @param code    短信的业务code
     * @return
     * @throws ServiceException
     */
    @RPC
    public boolean sendSMS(String phone, String content, String type, String code) throws ServiceException;

    @RPC
    public boolean checkPhoneLimit(String phone) throws ServiceException;

    @RPC
    public String getAccessTokenCache(String appId, String appSecret) throws ServiceException;

    @RPC
    public InterFlowAccount createInterFlowAccount(InterFlowAccount interflowAccount) throws ServiceException;

    @RPC
    public List<InterFlowAccount> queryInterFlowAccount(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<InterFlowAccount> queryInterFlowAccountByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyInterFlowAccount(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    @RPC
    public InterFlowAccount getInterFlowAccount(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public String saveAdvertiseInfo(String key, String value) throws ServiceException;

    @RPC
    public String getAdvertiseInfo(String key) throws ServiceException;

    @RPC
    public boolean saveRedisMiscValue(String key, String value, int timoutSec) throws ServiceException;

    @RPC
    public String getRedisMiscValue(String key) throws ServiceException;

    @RPC
    public boolean removeRedisMiscValue(String key) throws ServiceException;


    //TODO
    @RPC
    public RefreshCMSTiming createRefreshCMSTiming(RefreshCMSTiming refreshCMSTiming) throws ServiceException;

    @RPC
    public List<RefreshCMSTiming> queryRefreshCMSTiming(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<RefreshCMSTiming> queryRefreshCMSTimingByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyRefreshCMSTiming(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    @RPC
    public RefreshCMSTiming getRefreshCMSTiming(QueryExpress queryExpress) throws ServiceException;


    @RPC
    public String getTicketCache(String appid, String secret) throws ServiceException;

    @RPC
    public UserLogin getUserLogin(String openid) throws ServiceException;

    @RPC
    public boolean deleteUserLogin(String openid) throws ServiceException;

    @RPC
    List<String> getRedisListKey(String key) throws ServiceException;

    @RPC
    boolean updateRedisListKey(String key, Collection<String> keys) throws ServiceException;


    @RPC
    Map<String, String> hgetAll(String key) throws ServiceException;

    @RPC
    Long hset(String key, String field, String value) throws ServiceException;

    @RPC
    String hget(String key, String field) throws ServiceException;

    @RPC
    Long hdel(String key, String field) throws ServiceException;

}
