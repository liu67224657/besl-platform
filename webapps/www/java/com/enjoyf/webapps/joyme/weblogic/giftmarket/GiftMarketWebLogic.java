package com.enjoyf.webapps.joyme.weblogic.giftmarket;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityCategoryType;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameCategoryType;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDbStatus;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.search.SearchGiftCriteria;
import com.enjoyf.platform.service.search.SearchGiftResultEntry;
import com.enjoyf.platform.service.search.SearchServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.TextJsonItem;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDetailDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityMygiftDTO;
import com.enjoyf.webapps.joyme.dto.point.GoodsSeckillDTO;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-22
 * Time: 下午1:48
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "giftMarketWebLogic")
public class GiftMarketWebLogic {
    //获得微信用户信息
    //    private static final String APPID = "wx758f0b2d30620771";  服务号
    private static final String APPID = "wxedaaf0b0315d44e7";   //订阅号
    //SECRET
//    private static final String SECRET = "b58cd348c7f5908055e5e691eed45c39";  //服务号
    private static final String SECRET = "afde3c1cd927e508f663b92e6a084b6b ";   //订阅号

    //领号礼包详情页  dto
    public ActivityDetailDTO getActivityGiftDetailDTO(Long activityId) throws ServiceException {
        ActivityGoods activity = PointServiceSngl.get().getActivityGoods(activityId);

        if (activity == null) {
            return null;
        }
        long gameDbId = activity.getGameDbId();
        GameDB gameDB = null;
        if (gameDbId != 0) {
           BasicDBObject basicDBObject= new BasicDBObject();
            basicDBObject.put("_id", gameDbId);
            basicDBObject.put("validstatus", GameDbStatus.VALID.getCode());
            gameDB = GameResourceServiceSngl.get().getGameDB(basicDBObject, false);
        }
        // List<UserExchangeLog> listUserExchangeLog = PointServiceSngl.get().queryUserExchangeByQueryExpress(new QueryExpress().add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_GOODS_ID, exchangegoods.getGoodsId())));
        ActivityDetailDTO activityDetailDTO = new ActivityDetailDTO();
        activityDetailDTO.setAid(activityId);
        activityDetailDTO.setTitle(activity.getActivitySubject());
        activityDetailDTO.setGipic(URLUtils.getJoymeDnUrl(activity.getActivityPicUrl()));
        activityDetailDTO.setGid(activity.getActivityGoodsId());
        activityDetailDTO.setCn(activity.getGoodsAmount());
        activityDetailDTO.setRn(activity.getGoodsResetAmount());
        activityDetailDTO.setStartTime(activity.getStartTime());
        activityDetailDTO.setEndTime(activity.getEndTime());
        activityDetailDTO.setDesc(activity.getActivityDesc());
        activityDetailDTO.setShareId(0l);
        activityDetailDTO.setWeixinExclusive(activity.getChannelType().getCode());
        activityDetailDTO.setReserveType(activity.getReserveType());
        activityDetailDTO.setBgPic(URLUtils.getJoymeDnUrl(activity.getBgPic()));
        activityDetailDTO.setGameDbId(activity.getGameDbId());
        activityDetailDTO.setExchangeTimeType(activity.getTimeType().getCode());
        activityDetailDTO.setPoint(activity.getGoodsPoint());
        activityDetailDTO.setPlatform(activity.getPlatform().getCode());

        activityDetailDTO.setGameIcon(gameDB == null ? "" : URLUtils.getJoymeDnUrl(gameDB.getGameIcon()));
        activityDetailDTO.setGameTitie(gameDB == null ? "" : gameDB.getGameName());
        List<TextJsonItem> textJsonItemList = new ArrayList<TextJsonItem>();
        if (!CollectionUtil.isEmpty(activity.getTextJsonItemsList())) {
            for (TextJsonItem jsonItem : activity.getTextJsonItemsList()) {
                if (jsonItem.getType() == TextJsonItem.IMAGE_TYPE) {
                    jsonItem.setItem(URLUtils.getJoymeDnUrl(jsonItem.getItem()));
                }
                textJsonItemList.add(jsonItem);
            }
        }
        activityDetailDTO.setTextJsonItemsList(textJsonItemList);
        activityDetailDTO.setGameType(ActivityCategoryType.getByValue(0));
        if (gameDB != null && !CollectionUtil.isEmpty(gameDB.getCategoryTypeSet())) {
            List<String> categoryList = new ArrayList<String>();
            for (GameCategoryType categoryType : gameDB.getCategoryTypeSet()) {
                categoryList.add(categoryType.getValue());
            }
            activityDetailDTO.setCategoryList(categoryList);
        }

        activityDetailDTO.setGameDeveloper(gameDB == null ? "" : gameDB.getGameDeveloper());
        activityDetailDTO.setQrUrl("");
        activityDetailDTO.setWikiUrl(gameDB == null ? "" : gameDB.getWikiUrl());
        activityDetailDTO.setIosUrl(gameDB == null ? "" : gameDB.getIosDownload());
        activityDetailDTO.setAndroidUrl(gameDB == null ? "" : gameDB.getAndroidDownload());
        return activityDetailDTO;
    }


    public AllowExchangeStatus allowGetCode(ActivityGoods goods, String profileId) throws ServiceException {
        //一天很多次
        if (goods.getTimeType().equals(ConsumeTimesType.MANYTIMESADAY)) {
            return AllowExchangeStatus.ALLOW;
        } else if (goods.getTimeType().equals(ConsumeTimesType.ONETIMESADAY)) {  //一天一次
            return CollectionUtil.isEmpty(PointServiceSngl.get().queryUserExchangeByQueryExpress(new QueryExpress()
                    .add(QueryCriterions.eq(UserExchangeLogField.PROFILEID, profileId))
                    .add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_GOODS_ID, goods.getActivityGoodsId()))
                    .add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_DATE, new Date()))
                    .add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_TYPE, UserExchangeType.GET_CODE.getCode())))) ? AllowExchangeStatus.ALLOW : AllowExchangeStatus.HAS_EXCHANGED_BY_DAY;
        } else if (goods.getTimeType().equals(ConsumeTimesType.ONETIMESMANYDAY)) {   //永久一次
            return CollectionUtil.isEmpty(PointServiceSngl.get().queryUserExchangeByQueryExpress(new QueryExpress()
                    .add(QueryCriterions.eq(UserExchangeLogField.PROFILEID, profileId))
                    .add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_GOODS_ID, goods.getActivityGoodsId()))
                    .add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_TYPE, UserExchangeType.GET_CODE.getCode())))) ? AllowExchangeStatus.ALLOW : AllowExchangeStatus.HAS_EXCHANGED;
        }
//        else if (goods.getExchangeTimeType().equals(ConsumeTimesType.INTRAVAL)) {
//            List<UserExchangeLog> list = PointServiceSngl.get().queryUserExchangeByQueryExpress(new QueryExpress().add(QueryCriterions.eq(UserExchangeLogField.PROFILEID, profileId))
//                    .add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_GOODS_ID, goods.getGoodsId()))
//                    .add(QuerySort.add(UserExchangeLogField.EXCHANGE_TIME, QuerySortOrder.DESC)));
//            if (CollectionUtil.isEmpty(list)) {
//                return AllowExchangeStatus.ALLOW;
//            }
//            //上次领取时间
//            long time = list.get(0).getExhangeTime().getTime();
//            //时间间隔
//            long gtime = (long) goods.getExchangeIntrval();
//            //当前时间
//            long now = System.currentTimeMillis();
//            return now > (time + gtime) ? AllowExchangeStatus.ALLOW : AllowExchangeStatus.HAS_EXCHANGED_BY_INTRVAL;
//        }
        return AllowExchangeStatus.NO_ALLOW;
    }

    //积分兑换 是否允许 兑换 判断
    public AllowExchangeStatus allowExchangeGoods(ActivityGoods goods, String profileId) throws ServiceException {
        //一天很多次
        if (goods.getTimeType().equals(ConsumeTimesType.MANYTIMESADAY)) {
            return AllowExchangeStatus.ALLOW;
        } else if (goods.getTimeType().equals(ConsumeTimesType.ONETIMESADAY)) {
            return CollectionUtil.isEmpty(PointServiceSngl.get().queryConsumeLogByGoodsIdConsumeTime(profileId, goods.getActivityGoodsId(), new Date())) ? AllowExchangeStatus.ALLOW : AllowExchangeStatus.HAS_EXCHANGED_BY_DAY;
        } else if (goods.getTimeType().equals(ConsumeTimesType.ONETIMESMANYDAY)) {
            return CollectionUtil.isEmpty(PointServiceSngl.get().queryConsumeLogByGoodsId(profileId, goods.getActivityGoodsId(), "")) ? AllowExchangeStatus.ALLOW : AllowExchangeStatus.HAS_EXCHANGED;
        }

        return AllowExchangeStatus.NO_ALLOW;
    }

    //todo 优化改方法
    public PageRows<ActivityDTO> queryActivityDTOs(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        PageRows<ActivityGoods> activityPageRows = PointServiceSngl.get().queryActivityGoodsByPage(queryExpress, pagination);
        if (activityPageRows == null || CollectionUtil.isEmpty(activityPageRows.getRows())) {
            return null;
        }
        List<ActivityGoods> activityList = activityPageRows.getRows();
        List<ActivityDTO> activityDTOs = new LinkedList<ActivityDTO>();
        for (ActivityGoods activity : activityList) {
            ActivityDTO dto = new ActivityDTO();
            if (activity.getActivityType().equals(ActivityType.GOODS)) {
                dto = buildGoodsActivityDTO(activity);
            } else if (activity.getActivityType().equals(ActivityType.EXCHANGE_GOODS)) {
                dto = buildExchangeActivityDTO(activity);
            }
            if (dto != null) {
                activityDTOs.add(dto);
            }
        }
        PageRows<ActivityDTO> pageRows = new PageRows<ActivityDTO>();
        pageRows.setPage(activityPageRows.getPage());
        pageRows.setRows(activityDTOs);
        return pageRows;
    }

    public ActivityDTO buildActivityDTObyRelationType(ActivityGoods activity) throws ServiceException {
        ActivityDTO activityDTO = null;
        if (activity.getActivityType().equals(ActivityType.GOODS)) {
            activityDTO = buildGoodsActivityDTO(activity);
        } else if (activity.getActivityType().equals(ActivityType.EXCHANGE_GOODS)) {
            activityDTO = buildExchangeActivityDTO(activity);
        }
        return activityDTO;
    }

    public ActivityDTO buildExchangeActivityDTO(ActivityGoods activity) throws ServiceException {
        ActivityDTO dto = new ActivityDTO();
        dto.setGid(activity.getActivityGoodsId());
        dto.setDesc(activity.getSubDesc());
        dto.setGipic(URLUtils.getJoymeDnUrl(activity.getActivityPicUrl()));
        dto.setTitle(activity.getActivitySubject());
        dto.setCn(activity.getGoodsAmount());
        dto.setSn(activity.getGoodsResetAmount());
        //TODO 类
        if (activity.getDisplayType() == 3 || activity.getDisplayType() == 2) {
            dto.setHot(true);
        } else {
            dto.setHot(false);
        }
        if (activity.getDisplayType() == 1 || activity.getDisplayType() == 3) {
            dto.setNews(true);
        } else {
            dto.setNews(false);
        }
        dto.setRn(activity.getGoodsAmount() - activity.getGoodsResetAmount());
        dto.setPoint(activity.getGoodsPoint());
        dto.setGeid(activity.getActivityGoodsId());
        dto.setExDate(activity.getEndTime());
        dto.setWeixinExclusive(activity.getChannelType().getCode());
        dto.setReserveType(activity.getReserveType());
        dto.setHotActivity(activity.getHotActivity().getCode());
        dto.setPlatform(activity.getPlatform().getCode());
        return dto;
    }

    public ActivityDTO buildGoodsActivityDTO(ActivityGoods activity) throws ServiceException {
        ActivityDTO dto = new ActivityDTO();
        dto.setGeid(activity == null ? null : activity.getActivityGoodsId());
        dto.setTitle(activity.getActivitySubject());
        dto.setDesc(activity.getSubDesc());
        dto.setPoint(activity == null ? null : activity.getGoodsPoint());
        dto.setSn(activity == null ? null : activity.getGoodsResetAmount());
        dto.setCn(activity == null ? null : activity.getGoodsAmount());
        dto.setExDate(activity.getEndTime());
        dto.setSid(0l);
        dto.setGid(activity.getActivityGoodsId());
        dto.setGipic(URLUtils.getJoymeDnUrl(activity.getActivityPicUrl()));
        dto.setReserveType(activity.getReserveType());
        dto.setStartDate(activity.getStartTime());
        dto.setSeckillType(activity.getSeckilltype().getCode());
        dto.setYktitle(activity.getSecKill() == null ? "" : activity.getSecKill().getTitle());
        dto.setYkdesc(activity.getSecKill() == null ? "" : activity.getSecKill().getDesc());
        dto.setSupscript(activity.getSecKill() == null ? "" : activity.getSecKill().getSupscript());
        dto.setColor(activity.getSecKill() == null ? "" : activity.getSecKill().getColor());
        dto.setPrice(activity.getSecKill() == null ? "" : activity.getSecKill().getPrice());
        return dto;
    }

    public GoodsSeckillDTO buildGoodsSeckillDTO(GoodsSeckill goodsSeckill, ActivityGoods activityGoods) throws ServiceException {
        GoodsSeckillDTO goodsSeckillDTO = new GoodsSeckillDTO();
        goodsSeckillDTO.setSeckillId(goodsSeckill.getSeckillId());
        goodsSeckillDTO.setAfterTips(goodsSeckill.getSeckillTips().getAfterTips());
        goodsSeckillDTO.setBeforeTips(goodsSeckill.getSeckillTips().getBeforeTips());
        goodsSeckillDTO.setEndTime(goodsSeckill.getEndTime());
        goodsSeckillDTO.setGoodsId(goodsSeckill.getGoodsId());
        goodsSeckillDTO.setInTips(goodsSeckill.getSeckillTips().getInTips());
        goodsSeckillDTO.setStartTime(goodsSeckill.getStartTime());
        goodsSeckillDTO.setTotals(goodsSeckill.getSeckillTotal());
        goodsSeckillDTO.setRestsum(goodsSeckill.getSeckillSum());
        if (activityGoods != null) {
            ActivityDTO dto = buildGoodsActivityDTO(activityGoods);
            if (dto != null) {
                goodsSeckillDTO.setActivityDTO(dto);
            }
        }
        return goodsSeckillDTO;
    }

    public List<ActivityDTO> buildYoukuGiftmarket(List<ActivityGoods> list) throws ServiceException {
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        List<ActivityDTO> activityDTOs = new LinkedList<ActivityDTO>();

        for (ActivityGoods activityGoods : list) {
            if (activityGoods != null) {
                ActivityDTO dto = buildGoodsActivityDTO(activityGoods);
                if (dto != null) {
                    activityDTOs.add(dto);
                }
            }
        }
        return activityDTOs;
    }

    public ActivityDetailDTO getGoodsActivityDetailDTO(long activityId) throws ServiceException {
        ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(activityId);
        if (activityGoods == null) {
            return null;
        }
        long gameDbId = activityGoods.getGameDbId();
        GameDB gameDB = null;
        if (gameDbId != 0) {
            gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject("_id", gameDbId), false);
        }
        ActivityDetailDTO dto = new ActivityDetailDTO();
        dto.setAid(activityGoods.getActivityGoodsId());
        dto.setTitle(activityGoods.getActivitySubject());
        dto.setGipic(URLUtils.getJoymeDnUrl(activityGoods.getActivityPicUrl()));
        dto.setWikiUrl(gameDB == null ? "" : gameDB.getWikiUrl());
        dto.setEndTime(activityGoods.getEndTime());

        List<TextJsonItem> textJsonItemList = new ArrayList<TextJsonItem>();
        if (!CollectionUtil.isEmpty(activityGoods.getTextJsonItemsList())) {
            for (TextJsonItem jsonItem : activityGoods.getTextJsonItemsList()) {
                if (jsonItem.getType() == TextJsonItem.IMAGE_TYPE) {
                    jsonItem.setItem(URLUtils.getJoymeDnUrl(jsonItem.getItem()));
                }
                textJsonItemList.add(jsonItem);
            }
        }
        dto.setTextJsonItemsList(textJsonItemList);

        dto.setDesc(activityGoods.getActivityDesc());
        dto.setGameIcon(gameDB == null ? "" : URLUtils.getJoymeDnUrl(gameDB.getGameIcon()));
        dto.setGameTitie(gameDB == null ? "" : gameDB.getGameName());
        dto.setGameType(ActivityCategoryType.getByValue(0));
        if (gameDB != null && !CollectionUtil.isEmpty(gameDB.getCategoryTypeSet())) {
            List<String> categoryList = new ArrayList<String>();
            for (GameCategoryType categoryType : gameDB.getCategoryTypeSet()) {
                categoryList.add(categoryType.getValue());
            }
            dto.setCategoryList(categoryList);
        }
        dto.setGameDeveloper(gameDB == null ? null : gameDB.getGameDeveloper());
        dto.setGoodsType(activityGoods.getActivitygoodsType().getCode());
        dto.setIosUrl(gameDB == null ? "" : gameDB.getIosDownload());
        dto.setAndroidUrl(gameDB == null ? "" : gameDB.getAndroidDownload());
        dto.setQrUrl("");
        dto.setExchangeTimeType(activityGoods.getTimeType().getCode());
        dto.setGid(activityGoods.getActivityGoodsId());
        dto.setCn(activityGoods.getGoodsAmount());
        dto.setSn(activityGoods.getGoodsResetAmount());
        dto.setRn(activityGoods.getGoodsAmount() - activityGoods.getGoodsResetAmount());
        dto.setPoint(activityGoods.getGoodsPoint());
        dto.setBgPic(URLUtils.getJoymeDnUrl(activityGoods.getBgPic()));
        dto.setReserveType(activityGoods.getReserveType());
        dto.setExpire(activityGoods.getEndTime().getTime() - System.currentTimeMillis() > 0 ? false : true);
        dto.setStartTime(activityGoods.getStartTime());
        dto.setSeckillType(activityGoods.getSeckilltype().getCode());
        dto.setShareId(0);

        return dto;
    }

    public PageRows<ActivityMygiftDTO> queryActivityMygiftDTO(Pagination pagination, Date from, Date to, String profileId, String appkey) throws ServiceException {
        PageRows<UserExchangeLog> exchangeLogPageRows = PointServiceSngl.get().queryByUser(profileId, from, to, pagination, appkey);
        if (exchangeLogPageRows == null || CollectionUtil.isEmpty(exchangeLogPageRows.getRows())) {
            return null;
        }
        List<UserExchangeLog> logList = exchangeLogPageRows.getRows();
        List<ActivityMygiftDTO> activityMygiftDTOs = new LinkedList<ActivityMygiftDTO>();
        for (UserExchangeLog log : logList) {
            ActivityMygiftDTO dto = getActivityMygiftDTO(log);
            if (dto != null) {
                activityMygiftDTOs.add(dto);
            }
        }
        PageRows<ActivityMygiftDTO> pageRows = new PageRows<ActivityMygiftDTO>();
        pageRows.setPage(exchangeLogPageRows.getPage());
        pageRows.setRows(activityMygiftDTOs);
        return pageRows;
    }

    public ActivityMygiftDTO getActivityMygiftDTO(UserExchangeLog log) throws ServiceException {
//        ActivityRelation activityRelation = ContentServiceSngl.get().getActivityRelation(new QueryExpress().add(QueryCriterions.eq(ActivityRelationField.ACTIVITY_DIRECTID, log.getGoodsId())));
        ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(log.getGoodsId());
        ActivityMygiftDTO dto = new ActivityMygiftDTO();
        dto.setTitle(activityGoods.getActivitySubject());
        dto.setDesc(activityGoods.getActivityDesc());
        dto.setGid(log.getGoodsId());
        dto.setGipic(URLUtils.getJoymeDnUrl(activityGoods.getActivityPicUrl()));
        dto.setItemId(log.getGoodsItemId());
        dto.setEndTime(activityGoods.getEndTime());
        dto.setAid(activityGoods.getActivityGoodsId());
        dto.setItemName1(log.getSnName1());
        dto.setItemValue1(log.getSnValue1());
        dto.setLid(log.getUserExchangeLogId());
        Date date = activityGoods.getEndTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
        dto.setExpire(date.getTime() - System.currentTimeMillis() > 0 ? false : true);
        if (activityGoods.getActStatus().equals(ActStatus.ACTED)) {
            dto.setRemove(false);
        } else {
            dto.setRemove(true);
        }
        dto.setCn(activityGoods.getGoodsAmount());
        dto.setSn(activityGoods.getGoodsResetAmount());
        return dto;
    }

    public static void main(String agres[]) {


        try {
            SearchGiftCriteria criteria = new SearchGiftCriteria();
            Set<String> keySet = new HashSet<String>();
            keySet.add("手机");
            criteria.setKeys(keySet);
            Pagination pagination = new Pagination(20, 1, 20);
            PageRows<SearchGiftResultEntry> pageRows = SearchServiceSngl.get().searchGiftByText(criteria, pagination);
            for (SearchGiftResultEntry searchGiftResultEntry : pageRows.getRows()) {
                System.out.println(searchGiftResultEntry.getGtName());
                System.out.println(searchGiftResultEntry.getGtId());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public PageRows<ActivityDTO> searchActivity(String cond, Pagination pagination) throws ServiceException {
        PageRows<ActivityDTO> pageRows = null;
        List<ActivityDTO> list = null;
        //set serach criteria
        SearchGiftCriteria criteria = new SearchGiftCriteria();
        Set<String> keySet = new HashSet<String>();
        keySet.add(cond);
        criteria.setKeys(keySet);

        //
        PageRows<SearchGiftResultEntry> searchResultList = SearchServiceSngl.get().searchGiftByText(criteria, pagination);
        if (CollectionUtil.isEmpty(searchResultList.getRows())) {
            return pageRows;
        }
        Set<Long> setLong = new HashSet<Long>();
        for (SearchGiftResultEntry searchGiftResultEntry : searchResultList.getRows()) {
            try {
                setLong.add(Long.parseLong(searchGiftResultEntry.getGtId()));
            } catch (NumberFormatException e) {
                GAlerter.lab("SearchWebLogic pass number error.", e);
            }
        }
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.in(ActivityGoodsField.ACTIVITY_GOODS_ID, setLong.toArray()))
                .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
//                .add(QueryCriterions.in(ActivityGoodsField.CHANNEL_TYPE, new Object[]{MobileExclusive.DEFAULT.getCode(), MobileExclusive.WEIXIN.getCode()}))
                .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
        List<ActivityGoods> listActivity = PointServiceSngl.get().listActivityGoods(queryExpress);

        if (CollectionUtil.isEmpty(listActivity)) {
            return pageRows;
        }
        pageRows = new PageRows<ActivityDTO>();
        list = new LinkedList<ActivityDTO>();
        for (ActivityGoods activity : listActivity) {

            ActivityDTO dto = buildActivityDTObyRelationType(activity);
            list.add(dto);
        }

        pageRows.setPage(searchResultList.getPage());
        pageRows.setRows(list);
        return pageRows;
    }


    //获取用户积分
    public UserPoint getUserPoint(String appkey, com.enjoyf.platform.service.usercenter.Profile profile) throws ServiceException {

        appkey = AppUtil.getAppKey(appkey);

        PointKeyType pointKeyType = PointServiceSngl.get().getPointKeyType(appkey);
        String pointKey = "";
        if (pointKeyType == null) {
            pointKey = "default";
        } else {
            pointKey = pointKeyType.getValue();
        }

        UserPoint userPoint = PointServiceSngl.get().getUserPoint(new QueryExpress().add(QueryCriterions.eq(UserPointField.PROFILEID, profile.getProfileId()))
                .add(QueryCriterions.eq(UserPointField.POINTKEY, pointKey)));
        if (userPoint == null) {
            userPoint = new UserPoint();
            userPoint.setUserNo(profile.getUno());
            userPoint.setProfileId(profile.getProfileId());
            userPoint.setConsumeAmount(0);
            userPoint.setConsumeExchange(0);
            userPoint.setUserPoint(0);
            userPoint.setPointKey(pointKey);
        }
        return userPoint;
    }

    public List<ActivityDTO> queryCoinActivityListNoPage(String profileId, String appkey, int type) throws ServiceException {
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(UserConsumeLogField.PROFILEID, profileId))
                .add(QueryCriterions.eq(UserConsumeLogField.APPKEY, appkey));
        if (GoodsActionType.GIFT.getCode() == type) {
            long now = System.currentTimeMillis();
            long lastWeek = 60l * 60l * 24l * 1000 * 7;
            long selectDate = now - lastWeek;
            queryExpress.add(QueryCriterions.eq(UserConsumeLogField.GOODS_ACTION_TYPE, type))
                    .add(QueryCriterions.geq(UserConsumeLogField.CONSUME_TIME, new Date(selectDate)));
            List<UserConsumeLog> list = PointServiceSngl.get().queryConsumeLogList(queryExpress, GoodsActionType.GIFT);

            List<ActivityDTO> activityList = buildCoinActivity(list);
            return activityList;
        } else {
            queryExpress.add(QueryCriterions.eq(UserConsumeLogField.GOODS_ACTION_TYPE, type))
                    .add(QuerySort.add(UserConsumeLogField.CONSUME_DATE, QuerySortOrder.DESC));
            List<UserConsumeLog> list = PointServiceSngl.get().queryConsumeLogList(queryExpress, GoodsActionType.MIDOU);
            List<ActivityDTO> activityList = buildCoinActivity(list);
            return activityList;
        }
    }

    public PageRows<ActivityDTO> queryCoinActivityList(String profileId, String appkey, int type, Pagination pagination) throws ServiceException {
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(UserConsumeLogField.PROFILEID, profileId))
                .add(QueryCriterions.eq(UserConsumeLogField.APPKEY, appkey));
        if (GoodsActionType.GIFT.getCode() == type) {
            long now = System.currentTimeMillis();
            long lastWeek = 60l * 60l * 24l * 1000 * 7;
            long selectDate = now - lastWeek;
            queryExpress.add(QueryCriterions.eq(UserConsumeLogField.GOODS_ACTION_TYPE, type))
                    .add(QueryCriterions.geq(UserConsumeLogField.CONSUME_TIME, new Date(selectDate)));
            PageRows<UserConsumeLog> pageRows = PointServiceSngl.get().queryConsumeLogByPage(queryExpress, pagination, GoodsActionType.GIFT);
            if (CollectionUtil.isEmpty(pageRows.getRows())) {
                return null;
            }
            List<ActivityDTO> activityList = buildCoinActivity(pageRows.getRows());
            PageRows<ActivityDTO> returnActivity = new PageRows<ActivityDTO>();
            returnActivity.setPage(pageRows.getPage());
            returnActivity.setRows(activityList);
            return returnActivity;
        } else {
            queryExpress.add(QueryCriterions.eq(UserConsumeLogField.GOODS_ACTION_TYPE, type))
                    .add(QuerySort.add(UserConsumeLogField.CONSUME_DATE, QuerySortOrder.DESC));
            PageRows<UserConsumeLog> pageRows = PointServiceSngl.get().queryConsumeLogByPage(queryExpress, pagination, GoodsActionType.MIDOU);
            if (CollectionUtil.isEmpty(pageRows.getRows())) {
                return null;
            }
            List<ActivityDTO> activityList = buildCoinActivity(pageRows.getRows());
            PageRows<ActivityDTO> returnActivity = new PageRows<ActivityDTO>();
            returnActivity.setPage(pageRows.getPage());
            returnActivity.setRows(activityList);
            return returnActivity;
        }
    }

    private List<ActivityDTO> buildCoinActivity(List<UserConsumeLog> list) throws ServiceException {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<ActivityDTO> activityDTOList = new ArrayList<ActivityDTO>();
        Set<Long> idSet = new HashSet<Long>();
        for (UserConsumeLog userConsumeLog : list) {
            idSet.add(userConsumeLog.getGoodsId());
        }

        Map<Long, ActivityGoods> goodsMap = new HashMap<Long, ActivityGoods>();
        if (!CollectionUtil.isEmpty(idSet)) {
            List<ActivityGoods> activityList = PointServiceSngl.get().listActivityGoods(new QueryExpress().add(QueryCriterions.in(ActivityGoodsField.ACTIVITY_GOODS_ID, idSet.toArray())));
            if (!CollectionUtil.isEmpty(activityList)) {
                for (ActivityGoods activityGoods : activityList) {
                    if (activityGoods != null) {
                        goodsMap.put(activityGoods.getActivityGoodsId(), activityGoods);
                    }
                }
            }
        }
        for (UserConsumeLog log : list) {
            ActivityDTO activityDTO = buildGoodsActivityDTO(goodsMap.get(log.getGoodsId()));
            activityDTO.setConsumeOrder(log.getConsumeOrder());
            activityDTO.setExchangeTime(log.getConsumeTime());
            activityDTO.setPoint(log.getConsumeAmount());
            activityDTOList.add(activityDTO);
        }
        return activityDTOList;
    }
}
