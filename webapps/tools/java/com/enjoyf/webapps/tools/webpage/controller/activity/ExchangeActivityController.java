package com.enjoyf.webapps.tools.webpage.controller.activity;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertiseAppUrlField;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBModifyTimeFieldJson;
import com.enjoyf.platform.service.point.ActivityHotRanksField;
import com.enjoyf.platform.service.point.ExchangeGoods;
import com.enjoyf.platform.service.point.ExchangeGoodsField;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.service.sync.ShareBaseInfoField;
import com.enjoyf.platform.service.sync.ShareType;
import com.enjoyf.platform.service.sync.SyncServiceSngl;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.text.ProcessItemsUtil;
import com.enjoyf.platform.text.TextJsonItem;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.PinYinUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.activity.ActivityWebLogic;
import com.enjoyf.webapps.tools.weblogic.gameclient.GameClientWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-2 下午3:22
 * Description:
 */
@Controller
@RequestMapping(value = "/activity/exchange")
public class ExchangeActivityController extends ToolsBaseController {
    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @Resource(name = "gameClientWebLogic")
    private GameClientWebLogic gameClientWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "platform", required = false) Integer platform,
                             @RequestParam(value = "cooperation", required = false) Integer cooperation,
                             @RequestParam(value = "activityname", required = false) String activityName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ActivityField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()));
            queryExpress.add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
            if (platform != null && platform > 0) {
                mapMessage.put("platform", ActivityPlatform.getByValue(platform));
                queryExpress.add(QueryCriterions.bitwiseAnd(ActivityField.ACTIVITY_PLATFORM, QueryCriterionRelation.GT, platform, 0));
            }
            if (cooperation != null && cooperation > 0) {
                mapMessage.put("cooperation", ActivityCooperation.getByValue(cooperation));
                queryExpress.add(QueryCriterions.bitwiseAnd(ActivityField.ACTIVITY_COOPERATION, QueryCriterionRelation.GT, cooperation, 0));
            }
            if (!StringUtil.isEmpty(activityName)) {
                queryExpress.add(QueryCriterions.like(ActivityField.ACTIVITY_SUBJECT, "%" + activityName + "%"));
            }
            PageRows<Activity> page = ContentServiceSngl.get().queryActivity(queryExpress, pagination);
            mapMessage.put("list", page.getRows());
            mapMessage.put("page", page.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/activity/exchange/activitylist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createpage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            PageRows<ExchangeGoods> pageRows = PointServiceSngl.get().queryExchangeGoodsByPage(new QueryExpress().add(QueryCriterions.eq(ExchangeGoodsField.VALIDSTATUS, ValidStatus.VALID.getCode())), new Pagination());
            mapMessage.put("goodsList", pageRows.getRows());
            List<ShareBaseInfo> baseInfoList = SyncServiceSngl.get().queryShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.SHARETYPE, ShareType.GIFTMARKET.getCode()))
                    .add(QueryCriterions.eq(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode())));
            mapMessage.put("baseInfoList", baseInfoList);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/activity/exchange/list", mapMessage);
        }
        return new ModelAndView("/activity/exchange/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "activitySubject", required = false) String activitySubject,
                               @RequestParam(value = "picurl1", required = false) String picUrl,
                               @RequestParam(value = "exchangegoodsid", required = false) Long exchangeGoodsId,
                               @RequestParam(value = "jsonDesc", required = false) String jsonDesc,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "starttime", required = false) Date starttime,
                               @RequestParam(value = "endtime", required = false) Date endtime,
                               @RequestParam(value = "gameName", required = false) String gameName,
                               @RequestParam(value = "picurl2", required = false) String gameIcon,
                               @RequestParam(value = "caregory", required = false) Integer category,
                               @RequestParam(value = "developer", required = false) String developer,
                               @RequestParam(value = "gameUrl", required = false) String gameUrl,
                               @RequestParam(value = "iosUrl", required = false) String iosUrl,
                               @RequestParam(value = "iosSize", required = false) Double iosSize,
                               @RequestParam(value = "androidUrl", required = false) String androidUrl,
                               @RequestParam(value = "androidSize", required = false) Double androidSize,
                               @RequestParam(value = "activitysid", required = false) Long shareId,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "cooperation", required = false) Integer cooperation,
                               @RequestParam(value = "gamedbid", required = false) String gamedbId,
                               @RequestParam(value = "weixinexclusive", required = false) String weixinExclusive,
                               @RequestParam(value = "hotactivity", required = false) String hotActivity,
                               @RequestParam(value = "picurl3", required = false) String bgPic,
                               @RequestParam(value = "reserve", required = false) String reserve,
                               @RequestParam(value = "subdesc", required = false) String subDesc) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int order = Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000);
        try {
            long gameDbIdLong = 0;
            try {
                if (!StringUtil.isEmpty(gamedbId) && !"0".equals(gamedbId)) {
                    gameDbIdLong = Long.parseLong(gamedbId.trim());
                    GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gameDbIdLong), false);
                    if (gameDB == null) {
                        mapMessage.put("errorMessage", "exchangegoods.has.gamedbid");
                        return new ModelAndView("forward:/activity/exchange/createpage", mapMessage);
                    }
                }
            } catch (NumberFormatException e) {
                mapMessage.put("errorMessage", "exchangegoods.has.gamedbid");
                return new ModelAndView("forward:/activity/exchange/createpage", mapMessage);
            }
            ActivityRelation relation = ContentServiceSngl.get().getActivityRelation(new QueryExpress().add(QueryCriterions.eq(ActivityRelationField.ACTIVITY_DIRECTID, exchangeGoodsId)));
            if (relation != null) {
                mapMessage.put("errorMessage", "exchangegoods.has.exsited");
                return new ModelAndView("forward:/activity/exchange/createpage", mapMessage);
            }
            Activity activity = new Activity();
            activity.setActivitySubject(activitySubject);
            activity.setGameDbId(gameDbIdLong);
            activity.setWeixinExclusive(MobileExclusive.getByCode(Integer.parseInt(weixinExclusive)));
            activity.setHotActivity(Integer.parseInt(hotActivity));
            activity.setActivityPicUrl(picUrl);
            activity.setTextJsonItemsList(ProcessItemsUtil.praseRichTextToItems(jsonDesc));
            activity.setActivityDesc(jsonDesc);
            activity.setStartTime(starttime);
            activity.setActivityType(ActivityType.EXCHANGE_GOODS);
            activity.setEndTime(endtime);
            activity.setGameName(gameName);
            activity.setGameIconUrl(gameIcon);
            activity.setGameProduct(developer);
            activity.setGameUrl(gameUrl);
            activity.setIosDownloadUrl(iosUrl);
            activity.setSubDesc(subDesc);
            activity.setReserveType(Integer.parseInt(reserve));
            activity.setBgPic(bgPic);
            if (iosSize != null) {
                activity.setIosSizeMB(iosSize);
            }
            activity.setAndroidDownloadUrl(androidUrl);
            if (androidSize != null) {
                activity.setAndroidSizeMB(androidSize);
            }

            activity.setRemoveStatus(ActStatus.UNACT);
            activity.setCreateIp(getIp());
            activity.setCreateUserId(getCurrentUser().getUserid());
            activity.setCreateTime(new Date());
            activity.setCategory(new ActivityCategoryType().has(category));
            activity.setDisplayOrder(order);
            activity.setShareId(shareId);
            activity.setActivityPlatform(ActivityPlatform.getByValue(platform));
            activity.setCooperation(ActivityCooperation.getByValue(cooperation));
            activity.setFirstLetter(PinYinUtil.getFirstWordLetter(gameName));

            activity = ContentServiceSngl.get().insertActivity(activity);
            if (activity == null) {
                return null;
            }
            if (activity.getActivityId() != 0) {
                ActivityRelation activityRelation = new ActivityRelation();
                activityRelation.setActivityId(activity.getActivityId());
                activityRelation.setActivityType(ActivityType.EXCHANGE_GOODS);
                activityRelation.setDirectId(exchangeGoodsId);
                activityRelation.setDisplayOrder(order);
                activityRelation.setCreateTime(new Date());
                activityRelation.setCreateIp(getIp());
                activityRelation.setCreateUserid(getCurrentUser().getUserid());
                ContentServiceSngl.get().insertActivityRelation(activityRelation);
                PointServiceSngl.get().modifyExchangeGoods(new UpdateExpress().set(ExchangeGoodsField.VALIDSTATUS, ValidStatus.INVALID.getCode()), new QueryExpress().add(QueryCriterions.eq(ExchangeGoodsField.GOODSID, exchangeGoodsId)));
            }
            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.CREATE_ACTICVITY_EXCHANGE);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpAfter("create activity:" + activity);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/activity/exchange/list", mapMessage);
        }

        return new ModelAndView("redirect:/activity/exchange/list", mapMessage);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "activityId", required = true) Long activityId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (activityId == null) {
                return new ModelAndView("redirect:/activity/exchange/list");
            }
            Activity activity = ContentServiceSngl.get().getActivityById(activityId);
            mapMessage.put("activity", activity);
            List<ShareBaseInfo> baseInfoList = SyncServiceSngl.get().queryShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.SHARETYPE, ShareType.GIFTMARKET.getCode()))
                    .add(QueryCriterions.eq(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode())));
            mapMessage.put("baseInfoList", baseInfoList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("activity/exchange/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "activityId", required = false) Long activityId,
                               @RequestParam(value = "activitySubject", required = false) String activitySubject,
                               @RequestParam(value = "picurl1", required = false) String picUrl,
                               @RequestParam(value = "code", required = false) String code,
                               @RequestParam(value = "jsonDesc", required = false) String jsonDesc,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "starttime", required = false) Date starttime,
                               @RequestParam(value = "endtime", required = false) Date endtime,
                               @RequestParam(value = "gameName", required = false) String gameName,
                               @RequestParam(value = "picurl2", required = false) String gameIcon,
                               @RequestParam(value = "caregory", required = false) Integer category,
                               @RequestParam(value = "developer", required = false) String developer,
                               @RequestParam(value = "gameUrl", required = false) String gameUrl,
                               @RequestParam(value = "iosUrl", required = false) String iosUrl,
                               @RequestParam(value = "iosSize", required = false) Double iosSize,
                               @RequestParam(value = "androidUrl", required = false) String androidUrl,
                               @RequestParam(value = "androidSize", required = false) Double androidSize,
                               @RequestParam(value = "activitysid", required = false) Long shareId,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "cooperation", required = false) Integer cooperation,
                               @RequestParam(value = "gamedbid", required = false) String gamedbId,
                               @RequestParam(value = "weixinexclusive", required = false) String weixinExclusive,
                               @RequestParam(value = "hotactivity", required = false) String hotActivity,
                               @RequestParam(value = "picurl3", required = false) String bgPic,
                               @RequestParam(value = "reserve", required = false) String reserve,
                               @RequestParam(value = "subdesc", required = false) String subDesc) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            long gameDbIdLong = 0;
            try {
                if (!StringUtil.isEmpty(gamedbId) && !"0".equals(gamedbId)) {
                    mapMessage.put("errorMessage", activityId);
                    gameDbIdLong = Long.parseLong(gamedbId.trim());
                    GameDB gameDB = GameResourceServiceSngl.get().getGameDB(new BasicDBObject().append("_id", gameDbIdLong), false);
                    if (gameDB == null) {
                        mapMessage.put("errorMessage", "exchangegoods.has.gamedbid");
                        return new ModelAndView("forward:/activity/exchange/modifypage", mapMessage);
                    }
                }
            } catch (NumberFormatException e) {
                mapMessage.put("errorMessage", "exchangegoods.has.gamedbid");
                return new ModelAndView("forward:/activity/exchange/modifypage", mapMessage);
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ActivityField.ACTIVITY_SUBJECT, activitySubject);
            updateExpress.set(ActivityField.ACTIVITY_PICURL, picUrl);
            updateExpress.set(ActivityField.ACTIVITY_DESCJSON, TextJsonItem.toJson(ProcessItemsUtil.praseRichTextToItems(jsonDesc)));
            updateExpress.set(ActivityField.ACTIVITY_DESC, jsonDesc);
            updateExpress.set(ActivityField.START_TIME, starttime);
            updateExpress.set(ActivityField.END_TIME, endtime);
            updateExpress.set(ActivityField.SUB_DESSSC, subDesc);
            updateExpress.set(ActivityField.ACTIVITY_GAMENAME, gameName);
            updateExpress.set(ActivityField.ACTIVITY_GAMEICON, gameIcon);
            updateExpress.set(ActivityField.ACTIVITY_GAMEDEVELOPER, developer);
            updateExpress.set(ActivityField.ACTIVITY_GAME_URL, gameUrl);
            updateExpress.set(ActivityField.ACTIVITY_IOSURL, iosUrl);
            updateExpress.set(ActivityField.ACTIVITY_IOSSIZEMB, iosSize);
            updateExpress.set(ActivityField.ACTIVITY_ANDROIDURL, androidUrl);
            updateExpress.set(ActivityField.ACTIVITY_ANDROIDSIZEMB, androidSize);
            updateExpress.set(ActivityField.LASTMODIFYIP, getIp());
            updateExpress.set(ActivityField.LASTMODIFYTIME, new Date());
            updateExpress.set(ActivityField.LASTMODIFYUSERID, getCurrentUser().getUserid());
            updateExpress.set(ActivityField.CATEGORY, category);
            updateExpress.set(ActivityField.ACTIVITY_SHARE_ID, shareId);
            updateExpress.set(ActivityField.ACTIVITY_PLATFORM, platform);
            updateExpress.set(ActivityField.ACTIVITY_COOPERATION, cooperation);
            updateExpress.set(ActivityField.FIRST_LETTER, PinYinUtil.getFirstWordLetter(gameName));
            updateExpress.set(ActivityField.GAME_DB_ID, gameDbIdLong);
            updateExpress.set(ActivityField.IS_EXCLUSIVE, Integer.parseInt(weixinExclusive));
            updateExpress.set(ActivityField.HOT_ACTIVITY, Integer.parseInt(hotActivity));
            updateExpress.set(ActivityField.RESERVE_TYPE, Integer.parseInt(reserve));
            updateExpress.set(ActivityField.BG_PIC, bgPic);
            boolean val = ContentServiceSngl.get().modifyActivity(updateExpress, activityId);
            if (val) {
                AdvertiseServiceSngl.get().modifyAppUrl(new UpdateExpress().set(AdvertiseAppUrlField.IOSURL, iosUrl).set(AdvertiseAppUrlField.ANDROIDURL, androidUrl), new QueryExpress().add(QueryCriterions.eq(AdvertiseAppUrlField.CODE, code)), code);
            }
            if (val) {
                //
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_ACTICVITY_EXCHANGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("activityId id:" + activityId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/activity/exchange/list", mapMessage);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "activityId", required = false) Long activityId,
                                @RequestParam(value = "gameid", required = false) Long gameid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ActivityField.REMOVE_STATUS, ActStatus.ACTED.getCode());
            updateExpress.set(ActivityField.LASTMODIFYIP, getIp());
            updateExpress.set(ActivityField.LASTMODIFYTIME, new Date());
            updateExpress.set(ActivityField.LASTMODIFYUSERID, getCurrentUser().getUserid());
            Boolean bool = ContentServiceSngl.get().modifyActivity(updateExpress, activityId);
            if (gameid != 0) {
                GameDBModifyTimeFieldJson json = new GameDBModifyTimeFieldJson();
                json.setGiftlastModifyTime(new Date().getTime());
                gameClientWebLogic.sendGameDBModifyTimeEvent(gameid, json);
            }

            PointServiceSngl.get().modifyActivityHotRanks(new UpdateExpress().set(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()),
                    new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_ID, activityId))
                            .add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode())));
            if (bool) {
                //
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_ACTICVITY_EXCHANGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("activityId id:" + activityId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/activity/exchange/list", mapMessage);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "activityId", required = false) Long activityId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ActivityField.REMOVE_STATUS, ActStatus.UNACT.getCode());
            updateExpress.set(ActivityField.LASTMODIFYIP, getIp());
            updateExpress.set(ActivityField.LASTMODIFYTIME, new Date());
            updateExpress.set(ActivityField.LASTMODIFYUSERID, getCurrentUser().getUserid());
            Boolean bool = ContentServiceSngl.get().modifyActivity(updateExpress, activityId);
            PointServiceSngl.get().modifyActivityHotRanks(new UpdateExpress().set(ActivityHotRanksField.REMOVE_STATUS, ActStatus.ACTED.getCode()),
                    new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_ID, activityId))
                            .add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode())));
            if (bool) {
                //
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_ACTICVITY_EXCHANGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("activityId id:" + activityId);
                addLog(log);

            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/activity/exchange/list", mapMessage);
    }

    @RequestMapping(value = "/sort/{sort}")
    @ResponseBody
    public String getAppMenuSort(@PathVariable(value = "sort") String sort,
                                 @RequestParam(value = "activityid", required = true) Long activityId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        try {
            Long returnActivityId = ActivityWebLogic.sortExchangeActivity(sort, activityId);
            if (returnActivityId == null) {
                resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                return binder.toJson(resultObjectMsg);
            }
            mapMessage.put("returnitemid", returnActivityId);
        } catch (Exception e) {
            resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
            resultObjectMsg.setMsg(i18nSource.getMessage("system.error", null, null));
            return binder.toJson(resultObjectMsg);
        }
        mapMessage.put("sort", sort);
        mapMessage.put("activityid", activityId);

        resultObjectMsg.setResult(mapMessage);
        return binder.toJson(resultObjectMsg);
    }

}
