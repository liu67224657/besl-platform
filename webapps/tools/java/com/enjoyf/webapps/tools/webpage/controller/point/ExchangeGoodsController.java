package com.enjoyf.webapps.tools.webpage.controller.point;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.IntRemoveStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.WikiGameresField;
import com.enjoyf.platform.service.content.ActivityField;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.content.MobileExclusive;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.text.ProcessItemsUtil;
import com.enjoyf.platform.text.TextJsonItem;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.PinYinUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-8-20
 * Time: 下午5:42
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/point/exchangegoods")
public class ExchangeGoodsController extends ToolsBaseController {
    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "errorMsg", required = false) String errorMsg,
                             @RequestParam(value = "activitytype", required = false) String type,
                             @RequestParam(value = "subject", required = false) String subject,
                             @RequestParam(value = "shoptypes", required = false) String shoptypes) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("errorMsg", errorMsg);
        try {

            int curPage = (pageStartIndex / pageSize) + 1;

            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(type)) {
                queryExpress.add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, Integer.parseInt(type)));
            }
            if (!StringUtil.isEmpty(subject)) {
                queryExpress.add(QueryCriterions.like(ActivityGoodsField.ACTIVITY_SUBJECT, "%" + subject + "%"));
            }
            if (!StringUtil.isEmpty(shoptypes)) {
                queryExpress.add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, Integer.parseInt(shoptypes)));
            }
            queryExpress.add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
//            PageRows<ExchangeGoods> pageRows = PointServiceSngl.get().queryExchangeGoodsByPage(new QueryExpress().add(QuerySort.add(GoodsField.CREATEDATE, QuerySortOrder.DESC)), pagination);
            PageRows<ActivityGoods> pageRows = PointServiceSngl.get().queryActivityGoodsByPage(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("activityType", type);
            mapMessage.put("subject", subject);
            mapMessage.put("nowdate", new Date());
            mapMessage.put("shopTypes", GoodsActionType.getAll());
            mapMessage.put("shop_types", shoptypes);
//            List<ShareBaseInfo> baseInfoList = SyncServiceSngl.get().queryShareInfo(new QueryExpress().add(QueryCriterions.eq(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode())));
//            mapMessage.put("baseInfoList", baseInfoList);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/point/exchangegoodslist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "activitytype", required = false) Integer activitType,
                                   @RequestParam(value = "seckilltype", required = false) Integer seckillType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("activitType", activitType);
        mapMessage.put("seckillType", seckillType);
        mapMessage.put("shopTypes", GoodsActionType.getAll());
        mapMessage.put("consumetimestypecollection", ConsumeTimesType.getAll());
        return new ModelAndView("/point/createexchangegoods", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "activitytype", required = false) Integer activityType,
                               @RequestParam(value = "activitysubject", required = false) String activitySubject,
                               @RequestParam(value = "picurl1", required = false) String picurl1,
                               @RequestParam(value = "picurl3", required = false) String picurl3,
                               @RequestParam(value = "jsondesc", required = false) String jsonDesc,
                               @RequestParam(value = "subdesc", required = false) String subDesc,
                               @RequestParam(value = "starttime", required = false) String startTime,
                               @RequestParam(value = "endtime", required = false) String endTime,
                               @RequestParam(value = "shoptype", required = false) Integer shopType,
                               @RequestParam(value = "activitygoodtype", required = false) Integer activityGoodType,
                               @RequestParam(value = "gamedbid", required = false) String gameDbId,
                               @RequestParam(value = "channltype", required = false) Integer channlType,
                               @RequestParam(value = "hotactivity", required = false) Integer hotActivity,
                               @RequestParam(value = "reserve", required = false) Integer reserve,
                               @RequestParam(value = "goodsamount", required = false) Integer goodsAmount,
                               @RequestParam(value = "goodsconsumepoint", required = false) String point,
                               @RequestParam(value = "timetype", required = false) Integer timeType,
                               @RequestParam(value = "displaytype", required = false) Integer displayType,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "yktitle", required = false) String yktitle,
                               @RequestParam(value = "ykdesc", required = false) String ykdesc,
                               @RequestParam(value = "picurl2", required = false) String picurl2,
                               @RequestParam(value = "ipadpic", required = false) String ipadpic,
                               @RequestParam(value = "price", required = false) String price,
                               @RequestParam(value = "subscript", required = false) String subscript,
                               @RequestParam(value = "subcolor", required = false) String subcolor,
                               @RequestParam(value = "seckilltype", required = false) String seckillType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ActivityGoods activityGoods = new ActivityGoods();
        try {
            activityGoods.setActivityType(ActivityType.getByCode(activityType));
            activityGoods.setFirstLetter(PinYinUtil.getFirstWordLetter(activitySubject));
            activityGoods.setGameDbId(StringUtil.isEmpty(gameDbId) ? 0l : Long.parseLong(gameDbId));
            if (activityGoods.getGameDbId() > 0l) {
                BasicDBObject queryDBObject = new BasicDBObject();
                queryDBObject.put(GameDBField.ID.getColumn(), activityGoods.getGameDbId());
                BasicDBObject updateDBObject = new BasicDBObject();
                updateDBObject.append("$inc", new BasicDBObject().append(GameDBField.GIFTSUM.getColumn(), 1));
                GameResourceServiceSngl.get().updateGameDB(queryDBObject, updateDBObject);
            }
            activityGoods.setActivitySubject(activitySubject);
            activityGoods.setActivityPicUrl(picurl1);
            activityGoods.setStartTime(sim.parse(startTime));
            activityGoods.setBgPic(picurl3);
            activityGoods.setEndTime(sim.parse(endTime));
            activityGoods.setActivityDesc(jsonDesc);
            activityGoods.setTextJsonItemsList(ProcessItemsUtil.praseRichTextToItems(jsonDesc));
            activityGoods.setSubDesc(subDesc);
            activityGoods.setGoodsAmount(goodsAmount);
            activityGoods.setGoodsPoint(StringUtil.isEmpty(point) ? 0 : Integer.parseInt(point));
            activityGoods.setReserveType(reserve);
            activityGoods.setTimeType(ConsumeTimesType.getByCode(timeType));
            activityGoods.setDisplayType(displayType);
            activityGoods.setActStatus(ActStatus.UNACT);
            activityGoods.setCreateIp(getIp());
            activityGoods.setCreateUserId(getCurrentUser().getUserid());
            activityGoods.setCreateTime(new Date());
            activityGoods.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            activityGoods.setPlatform(StringUtil.isEmpty(platform) ? AppPlatform.CLIENT : AppPlatform.getByCode(Integer.parseInt(platform)));

            if (ActivityType.getByCode(activityType).equals(ActivityType.EXCHANGE_GOODS)) {
                activityGoods.setChannelType(MobileExclusive.getByCode(channlType));
                activityGoods.setActivitygoodsType(GoodsType.VIRTUAL);
                activityGoods.setGoodsResetAmount(0);
                activityGoods.setHotActivity(ChooseType.getByCode(hotActivity));
            } else {
                activityGoods.setHotActivity(ChooseType.NO);
                activityGoods.setGoodsActionType(GoodsActionType.getByCode(shopType));
                activityGoods.setActivitygoodsType(GoodsType.getByCode(activityGoodType));
                if (GoodsType.GOODS.getCode() == activityGoodType) {
                    activityGoods.setGoodsResetAmount(goodsAmount);
                }
            }
            activityGoods.setSeckilltype(StringUtil.isEmpty(seckillType) ? ChooseType.NO : ChooseType.getByCode(Integer.parseInt(seckillType)));
            SecKill secKill = new SecKill();
            secKill.setTitle(yktitle);
            secKill.setDesc(ykdesc);
            secKill.setColor(subcolor);
            secKill.setSupscript(subscript);
            secKill.setPrice(price);
            activityGoods.setSecKill(secKill);
            activityGoods = PointServiceSngl.get().createActivityGoods(activityGoods);
            if (activityGoods.getActivityGoodsId() != 0) {
                //调用PHP方法
                remotePhp(activityGoods);
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.CREATE_ACTICVITY_EXCHANGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("create ActivitGoods:" + activityGoods);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/point/exchangegoods/list", mapMessage);
        } catch (ParseException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/point/exchangegoods/list", mapMessage);
        }
        return new ModelAndView("forward:/point/exchangegoods/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "goodsid", required = true) Long goodsId,
                                   @RequestParam(value = "qatype", required = false) String qaType,
                                   @RequestParam(value = "qsubject", required = false) String qSubject,
                                   @RequestParam(value = "qstype", required = false) String qsType,
                                   @RequestParam(value = "pageStartIndex", required = false, defaultValue = "0") Integer pageStartIndex) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("qaType", qaType);
        mapMessage.put("qSubject", qSubject);
        mapMessage.put("qsType", qsType);
        mapMessage.put("pageStartIndex", pageStartIndex);

        mapMessage.put("shopTypes", GoodsActionType.getAll());
        mapMessage.put("consumetimestypecollection", ConsumeTimesType.getAll());
        try {
            ActivityGoods goods = PointServiceSngl.get().getActivityGoods(goodsId);
            if (goods == null) {
                mapMessage = putErrorMessage(mapMessage, "goods.goodsId.empty");
                return new ModelAndView("forward:/point/exchangegoods/list", mapMessage);
            }
            mapMessage.put("activitType", goods.getActivityType().getCode());
            mapMessage.put("activityGoods", goods);
            mapMessage.put("activityGoodsid", goodsId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("forward:/point/exchangegoods/list", mapMessage);
        }
        return new ModelAndView("/point/modifyexchangegoods", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "activitygoodsid", required = true) Long goodsId,
                               @RequestParam(value = "activitytype", required = false) Integer activityType,
                               @RequestParam(value = "activitysubject", required = false) String activitySubject,
                               @RequestParam(value = "picurl1", required = false) String picurl1,
                               @RequestParam(value = "picurl3", required = false) String picurl3,
                               @RequestParam(value = "jsondesc", required = false) String jsonDesc,
                               @RequestParam(value = "subdesc", required = false) String subDesc,
                               @RequestParam(value = "starttime", required = false) String startTime,
                               @RequestParam(value = "endtime", required = false) String endTime,
                               @RequestParam(value = "shoptype", required = false) Integer shopType,
                               @RequestParam(value = "activitygoodtype", required = false) Integer activityGoodType,
                               @RequestParam(value = "gamedbid", required = false) String gameDbId,
                               @RequestParam(value = "channltype", required = false) Integer channlType,
                               @RequestParam(value = "hotactivity", required = false) Integer hotActivity,
                               @RequestParam(value = "reserve", required = false) Integer reserve,
                               @RequestParam(value = "goodsamount", required = false) Integer goodsAmount,
                               @RequestParam(value = "goodsrestamount", required = false) Integer goodsRestAmount,
                               @RequestParam(value = "goodsconsumepoint", required = false) String point,
                               @RequestParam(value = "timetype", required = false) Integer timeType,
                               @RequestParam(value = "displaytype", required = false) Integer displayType,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "yktitle", required = false) String yktitle,
                               @RequestParam(value = "price", required = false) String price,
                               @RequestParam(value = "ykdesc", required = false) String ykdesc,
                               @RequestParam(value = "picurl2", required = false) String picurl2,
                               @RequestParam(value = "ipadpic", required = false) String ipadpic,
                               @RequestParam(value = "subscript", required = false) String subscript,
                               @RequestParam(value = "subcolor", required = false) String subcolor,
                               @RequestParam(value = "seckilltype", required = false) String seckillType,
                               @RequestParam(value = "oldfirstletter", required = false) String oldLetter,


                               @RequestParam(value = "qatype", required = false) String qaType,
                               @RequestParam(value = "qsubject", required = false) String qSubject,
                               @RequestParam(value = "qstype", required = false) String qsType,
                               @RequestParam(value = "pageStartIndex", required = false, defaultValue = "0") Integer pageStartIndex) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ActivityGoodsField.FIRST_LETTER, PinYinUtil.getFirstWordLetter(activitySubject));
            updateExpress.set(ActivityGoodsField.ACTIVITY_SUBJECT, activitySubject);
            updateExpress.set(ActivityGoodsField.ACTIVITY_PICURL, picurl1);
            updateExpress.set(ActivityGoodsField.START_TIME, sim.parse(startTime));
            updateExpress.set(ActivityGoodsField.END_TIME, sim.parse(endTime));
            updateExpress.set(ActivityGoodsField.BG_PIC, picurl3);
            updateExpress.set(ActivityGoodsField.ACTIVITY_DESC, jsonDesc);
            updateExpress.set(ActivityGoodsField.ACTIVITY_DESC_JSON, TextJsonItem.toJson(ProcessItemsUtil.praseRichTextToItems(jsonDesc)));
            updateExpress.set(ActivityGoodsField.SUB_DESC, subDesc);
            updateExpress.set(ActivityGoodsField.GOODS_AMOUNT, goodsAmount);
            updateExpress.set(ActivityGoodsField.GOODS_POINT, StringUtil.isEmpty(point) ? 0 : Integer.parseInt(point));
            updateExpress.set(ActivityGoodsField.RESERVE_TYPE, reserve);
            updateExpress.set(ActivityGoodsField.TIME_TYPE, timeType);
            updateExpress.set(ActivityGoodsField.DISPLAY_TYPE, displayType);
            updateExpress.set(ActivityGoodsField.GAME_DB_ID, StringUtil.isEmpty(gameDbId) ? 0 : Long.parseLong(gameDbId));
            updateExpress.set(ActivityGoodsField.PLATFORM, StringUtil.isEmpty(platform) ? AppPlatform.CLIENT.getCode() : Integer.parseInt(platform));

            if (ActivityType.getByCode(activityType).equals(ActivityType.EXCHANGE_GOODS)) {
                updateExpress.set(ActivityGoodsField.CHANNEL_TYPE, channlType);
                updateExpress.set(ActivityGoodsField.HOT_ACTIVITY, hotActivity);

            } else {
                updateExpress.set(ActivityGoodsField.GOODS_RESETAMOUNT, goodsRestAmount);
                updateExpress.set(ActivityGoodsField.GOODS_ACTION_TYPE, shopType);
            }
            SecKill secKill = new SecKill();
            secKill.setTitle(yktitle);
            secKill.setDesc(ykdesc);
            secKill.setColor(subcolor);
            secKill.setSupscript(subscript);
            secKill.setPrice(price);
            updateExpress.set(ActivityGoodsField.SECKILL_INFO, secKill.toJson());
            updateExpress.set(ActivityGoodsField.SECKILL_TYPE, StringUtil.isEmpty(seckillType) ? 0 : Integer.parseInt(seckillType));

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("oldLetter", oldLetter);
            paramMap.put("newLetter", PinYinUtil.getFirstWordLetter(activitySubject));
            boolean bool = PointServiceSngl.get().modifyActivityGoods(goodsId, updateExpress, paramMap);
            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_EXCHANGE_GOODS);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("modify ActivityGoods id:" + goodsId);
                addLog(log);

                ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(goodsId);
                remotePhp(activityGoods);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/point/exchangegoods/list?errorMsg=system.error&pager.offset=" + pageStartIndex + "&activitytype=" + qaType + "&subject=" + qSubject + "&shoptypes=" + qsType);
        } catch (ParseException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/point/exchangegoods/list?errorMsg=system.error&pager.offset=" + pageStartIndex + "&activitytype=" + qaType + "&subject=" + qSubject + "&shoptypes=" + qsType);
        }
        return new ModelAndView("redirect:/point/exchangegoods/list?pager.offset=" + pageStartIndex + "&activitytype=" + qaType + "&subject=" + qSubject + "&shoptypes=" + qsType);
    }


    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "goodsid", required = true) Long goodsId,
                                @RequestParam(value = "firstletter", required = false) String firstLetter,
                                @RequestParam(value = "qatype", required = false) String qaType,
                                @RequestParam(value = "qsubject", required = false) String qSubject,
                                @RequestParam(value = "qstype", required = false) String qsType,
                                @RequestParam(value = "pageStartIndex", required = false, defaultValue = "0") Integer pageStartIndex) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode());
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("newLetter", firstLetter);
            boolean bool = PointServiceSngl.get().modifyActivityGoods(goodsId, updateExpress, paramMap);
            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_ACTICVITY_EXCHANGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("ActivityGoods id:" + goodsId);
                addLog(log);
                ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(goodsId);
                remotePhp(activityGoods);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/point/exchangegoods/list?errorMsg=system.error&pager.offset=" + pageStartIndex + "&activitytype=" + qaType + "&subject=" + qSubject + "&shoptypes=" + qsType);
        }
        return new ModelAndView("redirect:/point/exchangegoods/list?pager.offset=" + pageStartIndex + "&activitytype=" + qaType + "&subject=" + qSubject + "&shoptypes=" + qsType);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "goodsid", required = true) Long goodsId,
                               @RequestParam(value = "firstletter", required = false) String firstLetter,
                               @RequestParam(value = "qatype", required = false) String qaType,
                               @RequestParam(value = "qsubject", required = false) String qSubject,
                               @RequestParam(value = "qstype", required = false) String qsType,
                               @RequestParam(value = "pageStartIndex", required = false, defaultValue = "0") Integer pageStartIndex) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ActivityGoodsField.REMOVE_STATUS, ActStatus.UNACT.getCode());
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("oldLetter", firstLetter);
            Boolean bool = PointServiceSngl.get().modifyActivityGoods(goodsId, updateExpress, paramMap);
            if (bool) {
                //删除商品  删除批次
                List<GoodsSeckill> list = PointServiceSngl.get().queryGoodsSeckill(new QueryExpress()
                        .add(QueryCriterions.eq(GoodsSeckillField.GOODS_ID, String.valueOf(goodsId)))
                        .add(QueryCriterions.eq(GoodsSeckillField.REMOVE_STATUS, IntRemoveStatus.USED.getCode())));
                for (GoodsSeckill goodsSeckill : list) {
                    PointServiceSngl.get().modifyGoodsSeckill(goodsSeckill.getSeckillId(), new UpdateExpress().set(GoodsSeckillField.REMOVE_STATUS, IntRemoveStatus.REMOVE.getCode()));
                }
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_ACTICVITY_EXCHANGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("ActivityGoods id:" + goodsId);
                addLog(log);
                ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(goodsId);
                remotePhp(activityGoods);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/point/exchangegoods/list?errorMsg=system.error&pager.offset=" + pageStartIndex + "&activitytype=" + qaType + "&subject=" + qSubject + "&shoptypes=" + qsType);
        }
        return new ModelAndView("redirect:/point/exchangegoods/list?pager.offset=" + pageStartIndex + "&activitytype=" + qaType + "&subject=" + qSubject + "&shoptypes=" + qsType);
    }

    @RequestMapping(value = "/sort")
    public ModelAndView sort(@RequestParam(value = "goodsid", required = true) Long goodsId,
                             @RequestParam(value = "displayorder", required = false) String displayorder,
                             @RequestParam(value = "subject", required = false) String subject,
                             @RequestParam(value = "pageStartIndex", required = false) Integer pageStartIndex,
                             @RequestParam(value = "shoptypes", required = false) String shopType,
                             @RequestParam(value = "activitytype", required = false) String activityType,
                             @RequestParam(value = "firstletter", required = false) String firstLetter
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ActivityGoodsField.DISPLAY_ORDER, Integer.parseInt(displayorder));

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("oldLetter", firstLetter);
            paramMap.put("newLetter", firstLetter);
            Boolean bool = PointServiceSngl.get().modifyActivityGoods(goodsId, updateExpress, paramMap);
            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.RECOVER_ACTICVITY_EXCHANGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("ActivityGoods id:" + goodsId);
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/point/exchangegoods/list?errorMsg=system.error&pager.offset=" + pageStartIndex + "&activitytype=" + activityType + "&subject=" + subject + "&shoptypes=" + shopType);
        }
        return new ModelAndView("redirect:/point/exchangegoods/list?pager.offset=" + pageStartIndex + "&activitytype=" + activityType + "&subject=" + subject + "&shoptypes=" + shopType);
    }


    private final String TYPE = "1";//1=微信搜索热词列表

    @RequestMapping(value = "/exclusive")
    public ModelAndView exclusive(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                  @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                                  @RequestParam(value = "type", required = false) String type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String code = "pc_exclusive";
            String codeName = "PC独家礼包列表";
            if (TYPE.equals(type)) {
                code = "weixin_hot_search_list";
                codeName = "微信搜索热词推荐";
            }
            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLineByCode(code);
            if (clientLine == null) {
                clientLine = new ClientLine();
                clientLine.setLineName(codeName);
                clientLine.setCode(code);
                clientLine.setCreateDate(new Date());
                clientLine.setCreateUserid(getCurrentUser().getUserid());
                clientLine.setPlatform(0);
                clientLine.setValidStatus(ValidStatus.VALID);
                if (TYPE.equals(type)) {
                    clientLine.setLineType(ClientLineType.WEIXIN_HOT_SEARCH_LIST);
                    clientLine.setItemType(ClientItemType.WEIXIN_HOT_SEARCH_LIST);
                } else {
                    clientLine.setLineType(ClientLineType.PC_EXCLUSIVE);
                    clientLine.setItemType(ClientItemType.PC_EXCLUSIVE);
                }

                clientLine = JoymeAppServiceSngl.get().createClientLine(clientLine);
            }
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()))
                    .add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));
            PageRows<ClientLineItem> pageRows = JoymeAppServiceSngl.get().queryClientLineItemByPage(queryExpress, pagination);
            mapMessage.put("lineId", clientLine.getLineId());
            mapMessage.put("code", clientLine.getCode());
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("type", type);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/point/item/itemlist", mapMessage);
    }

    @RequestMapping(value = "/addgiftpage")
    public ModelAndView addGiftPage(@RequestParam(value = "lineid", required = false) String lineId,
                                    @RequestParam(value = "type", required = false) String type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lineId", lineId);
        mapMessage.put("type", type);
        return new ModelAndView("/point/item/createpage", mapMessage);
    }

    @RequestMapping(value = "/addgift")
    public ModelAndView addGift(@RequestParam(value = "lineid", required = false) String lineId,
                                @RequestParam(value = "aid", required = false) String aid,
                                @RequestParam(value = "searcname", required = false) String searcName,
                                @RequestParam(value = "type", required = false) String type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ClientLineItem clientLineItem = new ClientLineItem();
            if (TYPE.equals(type)) {
                clientLineItem.setTitle(searcName);
                clientLineItem.setLineId(Long.parseLong(lineId));
                clientLineItem.setItemType(ClientItemType.WEIXIN_HOT_SEARCH_LIST);
                clientLineItem.setItemCreateDate(new Date());
                clientLineItem.setValidStatus(ValidStatus.INVALID);
                clientLineItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                clientLineItem.setItemDomain(ClientItemDomain.DEFAULT);
            } else {
                ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(Long.parseLong(aid.trim()));
                if (activityGoods == null || activityGoods.getActivityType().equals(ActivityType.GOODS)) {
                    mapMessage.put("errorMap", "礼包ID有误请重新填写");
                    return new ModelAndView("forward:/point/exchangegoods/addgiftpage?lineid=" + lineId, mapMessage);
                }
                clientLineItem.setTitle(activityGoods.getActivitySubject());
                clientLineItem.setPicUrl(activityGoods.getActivityPicUrl());
                clientLineItem.setDirectId(String.valueOf(activityGoods.getActivityGoodsId()));
                clientLineItem.setLineId(Long.parseLong(lineId));
                clientLineItem.setItemType(ClientItemType.PC_EXCLUSIVE);
                clientLineItem.setItemCreateDate(new Date());
                clientLineItem.setValidStatus(ValidStatus.INVALID);
                clientLineItem.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                clientLineItem.setItemDomain(ClientItemDomain.DEFAULT);
            }
            JoymeAppServiceSngl.get().createClientLineItem(clientLineItem);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/exchangegoods/exclusive?type=" + type, mapMessage);
    }

    @RequestMapping(value = "/addgiftdelete")
    public ModelAndView deleteLine(@RequestParam(value = "itemid", required = false) Long itemId,
                                   @RequestParam(value = "valid", required = false) String valid,
                                   @RequestParam(value = "type", required = false) String type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.getByCode(valid).getCode());
        try {
            JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, itemId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/exchangegoods/exclusive?type=" + type, mapMessage);
    }

    @RequestMapping(value = "/modifygiftpage")
    public ModelAndView modifyGiftPage(@RequestParam(value = "itemId", required = false) String itemId,
                                       @RequestParam(value = "type", required = false) String type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            ClientLineItem clientLineItem = JoymeAppServiceSngl.get().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, Long.parseLong(itemId))));
            mapMessage.put("item", clientLineItem);
            mapMessage.put("itemId", itemId);
            mapMessage.put("type", type);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/point/item/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modifygift")
    public ModelAndView modifygift(@RequestParam(value = "itemid", required = false) String itemid,
                                   @RequestParam(value = "aid", required = false) String aid,
                                   @RequestParam(value = "displayorder", required = false) String displayOrder,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "searchname", required = false) String searcName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            if (!StringUtil.isEmpty(displayOrder)) {
                UpdateExpress updateExpress = new UpdateExpress().set(ClientLineItemField.DISPLAY_ORDER, Integer.parseInt(displayOrder));
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, Long.parseLong(itemid));
                return new ModelAndView("redirect:/point/exchangegoods/exclusive?type=" + type, mapMessage);
            }
            if (TYPE.equals(type)) {
                UpdateExpress updateExpress = new UpdateExpress()
                        .set(ClientLineItemField.TITLE, searcName);
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, Long.parseLong(itemid));
            } else {
                ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(Long.parseLong(aid));
                if (activityGoods == null || activityGoods.getActivityType().equals(ActivityType.GOODS)) {
                    mapMessage.put("errorMap", "礼包ID有误请重新填写");
                    return new ModelAndView("forward:/point/exchangegoods/modifygiftpage?itemId=" + itemid, mapMessage);
                }

                UpdateExpress updateExpress = new UpdateExpress().set(ClientLineItemField.DIRECT_ID, aid)
                        .set(ClientLineItemField.TITLE, activityGoods.getActivitySubject())
                        .set(ClientLineItemField.PIC_URL, activityGoods.getActivityPicUrl());
                JoymeAppServiceSngl.get().modifyClientLineItem(updateExpress, Long.parseLong(itemid));
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/point/exchangegoods/exclusive?type=" + type, mapMessage);

    }

    public boolean remotePhp(ActivityGoods activityGoods) {
        try {
            if (activityGoods.getGameDbId() <= 0) {
                return false;
            }
            HttpClientManager httpClientManager = new HttpClientManager();
            String domain = WebappConfig.get().getDomain();
            if (domain.equals("joyme.dev") || domain.equals("joyme.test")) {
                domain = "joyme.alpha";
            }
            HttpResult result = httpClientManager.post("http://channel." + domain + "?c=source&a=savedata", new HttpParameter[]{
                    new HttpParameter("aid", activityGoods.getActivityGoodsId()),
                    new HttpParameter("gid", activityGoods.getGameDbId()),
                    new HttpParameter("pubdate", activityGoods.getStartTime().getTime()),
                    new HttpParameter("extra", new Gson().toJson(activityGoods)),
                    new HttpParameter("source", "2")
            }, null);
            JSONObject jsonObject = JSONObject.fromObject(result.getResult());
            if (result.getReponseCode() != 200) {
                return false;
            }
            return jsonObject.getInt("rs") == 1;
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occured error.e:", e);
            return false;
        }
    }


}
