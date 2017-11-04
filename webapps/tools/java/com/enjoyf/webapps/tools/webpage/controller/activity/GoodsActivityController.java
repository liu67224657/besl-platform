package com.enjoyf.webapps.tools.webpage.controller.activity;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertiseAppUrlField;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.point.ActivityHotRanksField;
import com.enjoyf.platform.service.point.Goods;
import com.enjoyf.platform.service.point.GoodsField;
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
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.activity.ActivityWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-2 下午3:22
 * Description:
 */
@Controller
@RequestMapping(value = "/activity/goods")
public class GoodsActivityController extends ToolsBaseController {
    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "goodscategory", required = false) Integer goodsCategoryValue,
                             @RequestParam(value = "goodsactiontype", required = false) String goodsActionType,
                             @RequestParam(value = "activityname", required = false) String activityName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            QueryExpress queryExpress = new QueryExpress();

            mapMessage.put("shopTypes", GoodsActionType.getAll());
            if (!StringUtil.isEmpty(goodsActionType)) {
                mapMessage.put("goodsActionType", goodsActionType);
                queryExpress.add(QueryCriterions.eq(ActivityField.GOODS_ACTION_TYPE, Integer.parseInt(goodsActionType)));

            }
            queryExpress.add(QueryCriterions.eq(ActivityField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()));
            queryExpress.add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
            if (goodsCategoryValue != null && goodsCategoryValue > 0) {
                mapMessage.put("goodsCategoryValue", goodsCategoryValue);
                mapMessage.put("goodsCategory", ActivityGoodsCategory.getByValue(goodsCategoryValue));
                queryExpress.add(QueryCriterions.bitwiseAnd(ActivityField.GOODS_CATEGORY, QueryCriterionRelation.GT, goodsCategoryValue, 0));
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

        return new ModelAndView("/activity/goods/activitylist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createpage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<Goods> goodsList = PointServiceSngl.get().queryGoods(new QueryExpress().add(QueryCriterions.eq(GoodsField.VALIDSTATUS, ValidStatus.VALID.getCode())));
            if (!CollectionUtil.isEmpty(goodsList)) {
                mapMessage.put("goodsList", goodsList);
            }
            mapMessage.put("shopTypes", GoodsActionType.getAll());
            List<ShareBaseInfo> baseInfoList = SyncServiceSngl.get().queryShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.SHARETYPE, ShareType.GIFTMARKET.getCode()))
                    .add(QueryCriterions.eq(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode())));

            mapMessage.put("baseInfoList", baseInfoList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/activity/goods/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "activitySubject", required = false) String activitySubject,
                               @RequestParam(value = "picurl1", required = false) String picUrl,
                               @RequestParam(value = "goodsid", required = false) Long goodsId,
                               @RequestParam(value = "jsonDesc", required = false) String jsonDesc,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "subdesc", required = false) String subDesc,
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
                               @RequestParam(value = "goodscategory", required = false) Integer goodsCategory,
                               @RequestParam(value = "goodsactiontype", required = false) Integer goodsActionType,
                               @RequestParam(value = "picurl3", required = false) String bgPic,
                               @RequestParam(value = "reserve", required = false) String reserve) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ActivityRelation relation = ContentServiceSngl.get().getActivityRelation(new QueryExpress().add(QueryCriterions.eq(ActivityRelationField.ACTIVITY_DIRECTID, goodsId)));
            if (relation != null) {
                mapMessage.put("errorMessage", "exchangegoods.has.exsited");
                return new ModelAndView("forward:/activity/goods/createpage", mapMessage);
            }
            Activity activity = new Activity();
            activity.setActivitySubject(activitySubject);
            activity.setActivityPicUrl(picUrl);
            activity.setTextJsonItemsList(ProcessItemsUtil.praseRichTextToItems(jsonDesc));
            activity.setActivityDesc(jsonDesc);
            activity.setSubDesc(subDesc);
            activity.setStartTime(starttime);
            activity.setActivityType(ActivityType.GOODS);
            activity.setEndTime(endtime);
            activity.setReserveType(Integer.parseInt(reserve));

            //TODO 这里先写个默认的
            activity.setWeixinExclusive(MobileExclusive.DEFAULT);

            if (!StringUtil.isEmpty(gameName)) {
                activity.setGameName(gameName);
            }
            if (!StringUtil.isEmpty(gameIcon)) {
                activity.setGameIconUrl(gameIcon);
            }
            if (!StringUtil.isEmpty(developer)) {
                activity.setGameProduct(developer);
            }
            if (!StringUtil.isEmpty(gameUrl)) {
                activity.setGameUrl(gameUrl);
            }
            if (!StringUtil.isEmpty(iosUrl)) {
                activity.setIosDownloadUrl(iosUrl);
            }
            if (iosSize != null && iosSize > 0) {
                activity.setIosSizeMB(iosSize);
            }
            if (androidSize != null && androidSize > 0) {
                activity.setAndroidSizeMB(androidSize);
            }
            if (!StringUtil.isEmpty(androidUrl)) {
                activity.setAndroidDownloadUrl(androidUrl);
            }
            activity.setBgPic(bgPic);
            activity.setRemoveStatus(ActStatus.UNACT);
            activity.setCreateIp(getIp());
            activity.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            activity.setCreateUserId(getCurrentUser().getUserid());
            activity.setCreateTime(new Date());
            if (category != null && category > 0) {
                activity.setCategory(new ActivityCategoryType().has(category));
            }
            activity.setShareId(shareId);
            activity.setGoodsCategory(ActivityGoodsCategory.getByValue(goodsCategory));
            activity.setGoodsActionType(GoodsActionType.getByCode(goodsActionType));
            activity = ContentServiceSngl.get().insertActivity(activity);

            if (activity.getActivityId() != 0) {
                ActivityRelation activityRelation = new ActivityRelation();
                activityRelation.setActivityId(activity.getActivityId());
                activityRelation.setActivityType(ActivityType.GOODS);
                activityRelation.setDirectId(goodsId);
                activityRelation.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                activityRelation.setCreateTime(new Date());
                activityRelation.setCreateIp(getIp());
                activityRelation.setCreateUserid(getCurrentUser().getUserid());
                ContentServiceSngl.get().insertActivityRelation(activityRelation);
                PointServiceSngl.get().modifyGoodsById(new UpdateExpress().set(GoodsField.VALIDSTATUS, ValidStatus.INVALID.getCode()), goodsId);

            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/activity/goods/list", mapMessage);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "activityId", required = true) Long activityId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (activityId == null) {
                return new ModelAndView("redirect:/activity/goods/list");
            }
            mapMessage.put("shopTypes", GoodsActionType.getAll());
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
        return new ModelAndView("activity/goods/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "activityId", required = false) Long activityId,
                               @RequestParam(value = "activitySubject", required = false) String activitySubject,
                               @RequestParam(value = "picurl1", required = false) String picUrl,
                               @RequestParam(value = "code", required = false) String code,
                               @RequestParam(value = "jsonDesc", required = false) String jsonDesc,
                               @RequestParam(value = "subdesc", required = false) String subDesc,
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
                               @RequestParam(value = "goodscategory", required = false) Integer goodsCategory,
                               @RequestParam(value = "goodsactiontype", required = false) Integer goodsActionType,
                               @RequestParam(value = "picurl3", required = false) String bgPic,
                               @RequestParam(value = "reserve", required = false) String reserve) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ActivityField.ACTIVITY_SUBJECT, activitySubject);
            updateExpress.set(ActivityField.ACTIVITY_PICURL, picUrl);
            updateExpress.set(ActivityField.ACTIVITY_DESCJSON, TextJsonItem.toJson(ProcessItemsUtil.praseRichTextToItems(jsonDesc)));
            updateExpress.set(ActivityField.ACTIVITY_DESC, jsonDesc);
            updateExpress.set(ActivityField.SUB_DESSSC, subDesc);
            updateExpress.set(ActivityField.START_TIME, starttime);
            updateExpress.set(ActivityField.END_TIME, endtime);
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
            updateExpress.set(ActivityField.GOODS_CATEGORY, goodsCategory);
            updateExpress.set(ActivityField.GOODS_ACTION_TYPE, goodsActionType);
            updateExpress.set(ActivityField.BG_PIC, bgPic);
            updateExpress.set(ActivityField.RESERVE_TYPE, Integer.parseInt(reserve));
            boolean val = ContentServiceSngl.get().modifyActivity(updateExpress, activityId);
            if (val) {
                AdvertiseServiceSngl.get().modifyAppUrl(new UpdateExpress().set(AdvertiseAppUrlField.IOSURL, iosUrl).set(AdvertiseAppUrlField.ANDROIDURL, androidUrl), new QueryExpress().add(QueryCriterions.eq(AdvertiseAppUrlField.CODE, code)), code);
            }
            if (val) {
                //
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_ACTICVITY);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("activityId id:" + activityId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/activity/goods/list", mapMessage);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "activityId", required = false) Long activityId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ActivityField.REMOVE_STATUS, ActStatus.ACTED.getCode());
            updateExpress.set(ActivityField.LASTMODIFYIP, getIp());
            updateExpress.set(ActivityField.LASTMODIFYTIME, new Date());
            updateExpress.set(ActivityField.LASTMODIFYUSERID, getCurrentUser().getUserid());
            Boolean bool = ContentServiceSngl.get().modifyActivity(updateExpress, activityId);
            PointServiceSngl.get().modifyActivityHotRanks(new UpdateExpress().set(ActivityHotRanksField.REMOVE_STATUS, ActStatus.ACTED.getCode()),
                    new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_ID, activityId))
                            .add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_TYPE, ActivityType.GOODS.getCode())));
            if (bool) {
                //
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_ACTICVITY);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("activityId id:" + activityId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/activity/goods/list", mapMessage);
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
            PointServiceSngl.get().modifyActivityHotRanks(new UpdateExpress().set(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()),
                    new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_ID, activityId))
                            .add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_TYPE, ActivityType.GOODS.getCode())));
            if (bool) {
                //
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_ACTICVITY);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("activityId id:" + activityId);
                addLog(log);

            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("redirect:/activity/goods/list", mapMessage);
    }

    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView getAppMenuSort(@PathVariable(value = "sort") String sort,
                                       @RequestParam(value = "activityid", required = true) Long activityId,
                                       @RequestParam(value = "goodscategory", required = true) Integer goodsCategoryValue) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("goodscategory", goodsCategoryValue);
        try {
            ActivityWebLogic.sortGoodsActivity(sort, activityId, goodsCategoryValue);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/activity/goods/list");
        }
        return new ModelAndView("forward:/activity/goods/list", mapMessage);
    }

}
