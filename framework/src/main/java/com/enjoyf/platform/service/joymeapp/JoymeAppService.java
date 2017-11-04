package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.mcms.bean.DedeArchives;
import com.enjoyf.mcms.bean.DedeArctype;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTV;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.ShakeItem;
import com.enjoyf.platform.service.joymeapp.config.ShakeType;
import com.enjoyf.platform.service.joymeapp.gameclient.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:29
 * Description:
 */
public interface JoymeAppService extends EventReceiver {


    @RPC
    @Override
    boolean receiveEvent(Event event) throws ServiceException;


    @RPC
    public Map<Integer, Archive> queryArchiveMapByIds(Set<Integer> archiveIdSet) throws ServiceException;

    @RPC
    public Archive getArchiveById(Integer archiveId) throws Exception;

    @RPC
    public DedeArctype getqueryDedeArctype(Integer archiveTypeId) throws Exception;

    @RPC
    public Map<Integer, DedeArctype> queryDedeArctypeMapByTypeId(Set<Integer> archiveTypeIds) throws Exception;

    //clientLine

    @RPC
    public ClientLine getClientLine(QueryExpress queryExpress) throws ServiceException;

    ///clientLineItem
    @RPC
    public ClientLine getClientLineByCode(String code) throws ServiceException;

    @RPC
    public List<ClientLine> queryClientLineList(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public List<ClientLine> queryClientLine(ClientLineType lineType, Integer platform) throws ServiceException;

    @RPC
    public PageRows<ClientLine> queryClientLineByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    //todo remove
    @RPC
    public boolean modifyClientLine(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    @RPC
    public boolean modifyClientLineByCache(UpdateExpress updateExpress, Map<String, Object> param) throws ServiceException;

    //  为了着迷玩霸2.0.3热门页接口新加 ,解决今日推荐jt,ji，more字段更新不及时反映到接口中的问题
    @RPC
    public boolean removeClientLineFromCache(String code) throws ServiceException;

    @RPC
    public ClientLine createClientLine(ClientLine clientLine) throws ServiceException;

    //todo remove
    @RPC
    public ClientLineItem getClientLineItem(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public ClientLineItem getClientLineItemByCache(String lineCode, Long itemId) throws ServiceException;

    //todo remove
    @RPC
    public List<ClientLineItem> queryClientLineItemList(String lineCode) throws ServiceException;

    @RPC
    public List<ClientLineItem> queryClientLineItem(String lineCode, QuerySort querySort, Pagination pagination) throws ServiceException;

    @RPC
    public List<ClientLineItem> queryClientLineItemListForWanBaHotPageTodayRec(String lineCode) throws ServiceException;

    @RPC
    public List<ClientLineItem> queryClientLineItemListForHotPage(String lineCode) throws ServiceException;

    //根据code查询ClientLineCache中的自定义缓存
    @RPC
    public List queryClientLineCustomCache(String customCode, String platform) throws ServiceException;

    //根据code 删除ClientLineCache中的自定义缓存
    @RPC
    public boolean removeClientLineCustomCache(String customCode, String platform) throws ServiceException;

    @RPC
    public List<ClientLineItem> queryClientLineItemByQueryExpress(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<ClientLineItem> queryClientLineItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyClientLineItem(UpdateExpress updateExpress, long itemId) throws ServiceException;

    @RPC
    public ClientLineItem createClientLineItem(ClientLineItem clientLineitem) throws ServiceException;

    /**
     * 通过linecode查询item flag只有线上正式环境测试环境使用，为空会用缓存进行查询
     *
     * @param lineCode
     * @param flag
     * @param pagination
     * @return
     * @throws ServiceException
     */
    @RPC
    public PageRows<ClientLineItem> queryItemsByLineCode(String lineCode, String flag, Pagination pagination) throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RPC
    public List<ActivityTopMenu> queryClientTopMenuCache(Integer platform, Integer categroy, String flag, String appKey) throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RPC
    public ClientLineFlag createClientLineFlag(ClientLineFlag flag) throws ServiceException;

    @RPC
    public ClientLineFlag getClientLineFlag(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<ClientLineFlag> queryClientLineFlagByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyClientLineFlag(UpdateExpress updateExpress, Long flagId) throws ServiceException;

    @RPC
    public boolean modifyDedeArchivePubdateById(DedeArchives dedeArchives) throws Exception;

    @RPC
    public List<SocialShare> querySocialShare(QueryExpress queryExpress, String appkey, int platform, SocialShareType socialShareType, long activityid) throws ServiceException;

    @RPC
    public SocialShare createSocialShare(SocialShare socialShare, String appkey, int platform, SocialShareType socialShareType, long activityid) throws ServiceException;

    @RPC
    public boolean modifySocialShare(QueryExpress queryExpress, UpdateExpress updateExpress, String appkey, int platform, SocialShareType socialShareType, long activityid) throws ServiceException;

    @RPC
    public PageRows<SocialShare> querySocialShareByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public SocialShare getSocialShare(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public MobileGameArticle createMobileGameArticle(MobileGameArticle mobileGameArticle) throws ServiceException;

    @RPC
    public MobileGameArticle getMobileGameArticle(QueryExpress queryExpresse) throws ServiceException;

    @RPC
    public List<MobileGameArticle> queryMobileGameArticleByList(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<MobileGameArticle> queryMobileGameArticleByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyMobileGameArticle(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

//    @RPC
//    public PageRows<SocialProfileRecommend> querySocialProfileRecommendByCache(SocialProfileRecommendType recommendType, Pagination pagination) throws ServiceException;

    @RPC
    public AnimeIndex getAnimeIndex(BasicDBObject express) throws ServiceException;

    @RPC
    public List<AnimeIndex> queryAnimeIndex(MongoQueryExpress mongoQueryExpress) throws ServiceException;

    @RPC
    public boolean modifyAnimeIndex(BasicDBObject queryObject, BasicDBObject updateObjec) throws ServiceException;

    @RPC
    public AnimeIndex createAnimeIndex(AnimeIndex animeIndex) throws ServiceException;

    @RPC
    public PageRows<AnimeIndex> queryAnimeIndexByPage(MongoQueryExpress mongoQueryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public AnimeSpecial getAnimeSpecial(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public AnimeSpecial createAnimeSpecial(AnimeSpecial animeSpecial) throws ServiceException;

    @RPC
    public PageRows<AnimeSpecial> queryAnimeSpecialByPage(QueryExpress queyrExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyAnimeSpecial(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;


    @RPC
    public AnimeTag createAnimeTag(AnimeTag animeTag) throws ServiceException;

    @RPC
    public AnimeTag getAnimeTag(Long tagid, QueryExpress queryExpress) throws ServiceException;

    @RPC
    public List<AnimeTag> queryAnimeTag(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<AnimeTag> queryAnimeTagByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyAnimeTag(Long tagid, QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

    ////
    @RPC
    public List<AnimeSpecialItem> queryAnimeSpecialItem(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public AnimeSpecialItem getAnimeSpecialItem(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public AnimeSpecialItem createAnimeSpecialItem(AnimeSpecialItem animeSpecial) throws ServiceException;

    @RPC
    public PageRows<AnimeSpecialItem> queryAnimeSpecialItemByPage(QueryExpress queyrExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyAnimeSpecialItem(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

    ////////
    @RPC
    public AnimeTV createAnimeTV(AnimeTV animeTV) throws ServiceException;

    @RPC
    public AnimeTV getAnimeTV(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public List<AnimeTV> queryAnimeTV(QueryExpress queryExpress, Long tagid, String flag) throws ServiceException;

    @RPC
    public PageRows<AnimeTV> queryAnimeTVByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyAnimeTV(QueryExpress queryExpress, UpdateExpress updateExpress, List<Long> tags) throws ServiceException;

    @RPC
    public TagDedearchives createTagDedearchives(TagDedearchives tagDedearchives) throws ServiceException;

    @RPC
    public TagDedearchives getTagDedearchives(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public List<TagDedearchives> queryTagDedearchives(QueryExpress queryExpress) throws ServiceException;

    @RPC
    public PageRows<TagDedearchives> queryTagDedearchivesByPage(Boolean isTools, Long tagid, Integer platform, QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyTagDedearchives(Long tagid, String dede_archives_id, QueryExpress queryExpress, UpdateExpress updateExpress, ArchiveRelationType archiveRelationType) throws ServiceException;

    ////////
    @RPC
    public PageRows<TagDedearchiveCheat> queryTagDedearchiveCheat(QueryExpress queryExpress, Pagination pagination) throws ServiceException;


    @RPC
    public TagDedearchiveCheat createTagDedearchiveCheat(TagDedearchiveCheat cheat) throws ServiceException;

    @RPC
    public TagDedearchiveCheat getTagDedearchiveCheat(Long dede_archives_id) throws ServiceException;

    @RPC
    public Map<Long, TagDedearchiveCheat> getMapTagDedearchiveCheat(Set<Long> dedeIds) throws ServiceException;

    @RPC
    public boolean modifyTagDedearchiveCheat(Long dede_archives_id, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public JoymeWiki getJoymeWiki(String wikiKey, JoymeWikiContextPath contextPath) throws ServiceException;

    @RPC
    public PageRows<JoymeWiki> queryJoymeWikiByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC
    public boolean modifyJoymeWiki(String wikiKey, JoymeWikiContextPath contextPath, UpdateExpress updateExpress) throws ServiceException;

    @RPC
    public Map<String, JoymeWiki> queryJoymeWikiByWikiKeySet(Set<String> wikiKeySet, JoymeWikiContextPath contextPath) throws ServiceException;

    @RPC
    public void putGameIdByWeight(String code, String directId) throws ServiceException;

    @RPC
    public Set<String> getGameIdByWeight(String code) throws ServiceException;

    @RPC
    public boolean removeGameIdByWeight(String lineCode) throws ServiceException;

    @RPC
    public List<Integer> queryPostNum(QueryExpress queryExpress) throws ServiceException;

    /**
     * 按设备防刷
     * key的格式
     * 年月日_设备ID_礼包ID或者任务id  如：20151022_0A718FE3-F07E-4FBC-8800-14A447C7414C_10000
     *
     * @param key
     * @param type 1=存  2=取
     * @return
     * @throws ServiceException
     */
    @RPC
    public String validateInfo(String key, String type) throws ServiceException;

    /**
     * 临时获取图片宽高方案 玩霸2.3去除
     *
     * @param commentId
     * @param pics
     * @return
     * @throws ServiceException
     */
    @Deprecated
    @RPC
    public List<ImgDTO> picInfoListCache(String commentId, List<String> pics) throws ServiceException;

    @RPC
    public PageRows<Long> queryTagDedeArchivesByDistinct(QueryExpress queryExpress, Pagination page) throws ServiceException;

    @RPC
    public void addTagDedearchivesTimerTask(Long tagid, Set<String> idSet, Date publishDate) throws ServiceException;

    @RPC
    public PageRows<TagDedearchives> queryTagDedearchivesTimerList(Long tagid, Pagination pagination) throws ServiceException;

    @RPC
    public boolean delTagDedearchivesTimerTaskCache(Long tagid, Set<String> idSet, Date publishDate) throws ServiceException;

    @RPC
    public TagDedearchives getTagDedearchivesByCache(long gameId, ArchiveRelationType gameRelation, String archiveId) throws ServiceException;

    @RPC
    public List<TagDedearchives> queryTagDedearchivesByCache(long gameId, ArchiveRelationType gameRelation, List<String> archiveIds) throws ServiceException;

    @RPC
    public Map<Long, AnimeTag> queryAnimetags(Set<Long> tagIds) throws ServiceException;
}
