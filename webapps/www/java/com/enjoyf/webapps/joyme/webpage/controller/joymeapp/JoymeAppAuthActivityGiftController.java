/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;


import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.MobileExclusive;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.ShareInfo;
import com.enjoyf.platform.service.sync.SyncServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.JsonPagination;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.ResultPageMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityMygiftDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.JoymeAppGiftDetailDTO;
import com.enjoyf.webapps.joyme.dto.share.JoymeAppShareInfoDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.AllowExchangeStatus;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
@RequestMapping(value = "/joymeapp/activitygift")
public class JoymeAppAuthActivityGiftController extends BaseRestSpringController {

    private static final Logger logger = LoggerFactory.getLogger(JoymeAppAuthActivityGiftController.class);

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    @ResponseBody
    @RequestMapping(value = "/list")
    public String getGift(@RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                          @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                          @RequestParam(value = "ordertype", required = false, defaultValue = "1") Integer type,
                          @RequestParam(value = "appkey", required = false) String appkey,
                          @RequestParam(value = "platform", required = false) String platform,
                          @RequestParam(value = "channelid", required = false) String channelid,
                          @RequestParam(value = "client_id", required = false) String clientId,
                          @RequestParam(value = "client_token", required = false) String clientToken) {
        ResultPageMsg resultMsg = new ResultPageMsg(ResultPageMsg.CODE_S);
        if (StringUtil.isEmpty(clientId)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientToken)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(appkey)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(platform)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(channelid)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        Pagination pagination = new Pagination(count * page, page, count);
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                .add(QueryCriterions.eq(ActivityGoodsField.CHANNEL_TYPE, MobileExclusive.DEFAULT.getCode()))
                .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()));

        if (type == 1) {
            queryExpress.add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
        } else {
            queryExpress.add(QuerySort.add(ActivityGoodsField.CREATEDATE, QuerySortOrder.DESC));
        }
        try {
            PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, pagination);
            if (pageRows == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("gift.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            resultMsg.setResult(pageRows.getRows());
            resultMsg.setPage(new JsonPagination(pageRows.getPage()));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/listbygameid")
    public String getGiftByApp(@RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                               @RequestParam(value = "count", required = false, defaultValue = "1") Integer count,
                               @RequestParam(value = "ordertype", required = false, defaultValue = "1") Integer type,
                               @RequestParam(value = "appkey", required = false) String appkey,
                               @RequestParam(value = "gameid", required = false) String gameid,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "channelid", required = false) String channelid,
                               @RequestParam(value = "client_id", required = false) String clientId,
                               @RequestParam(value = "client_token", required = false) String clientToken) {
        ResultPageMsg resultMsg = new ResultPageMsg(ResultPageMsg.CODE_S);
        if (StringUtil.isEmpty(clientId)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientToken)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(appkey)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(platform)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(channelid)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        long gid = 0;
        if (!StringUtil.isEmpty(gameid)) {
            try {
                gid = Long.parseLong(gameid);
            } catch (NumberFormatException e) {
            }
        }

        if (gid == 0) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("game.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        Pagination pagination = new Pagination(count * page, page, count);
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(ActivityGoodsField.GAME_DB_ID, gameid))
                .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()));

        if (type == 1) {
            queryExpress.add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
        } else {
            queryExpress.add(QuerySort.add(ActivityGoodsField.CREATEDATE, QuerySortOrder.DESC));
        }
        try {
            PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, pagination);
            if (pageRows == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("gift.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            resultMsg.setResult(pageRows.getRows());
            resultMsg.setPage(new JsonPagination(pageRows.getPage()));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/giftdetail")
    public String getGiftById(@RequestParam(value = "gid", required = false) Long gid,
                              @RequestParam(value = "appkey", required = false) String appkey,
                              @RequestParam(value = "platform", required = false) String platform,
                              @RequestParam(value = "client_id", required = false) String clientId,
                              @RequestParam(value = "client_token", required = false) String clientToken,
                              @RequestParam(value = "channelid", required = false) String channelid) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);
        if (StringUtil.isEmpty(appkey)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientId)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientToken)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(platform)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(channelid)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (gid == null) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/getcode")
    public String getCode(HttpServletRequest request, @RequestParam(value = "uno", required = false) String uno,
                          @RequestParam(value = "gid", required = false) Long gid,
                          @RequestParam(value = "profileId", required = false) String profileId,
                          @RequestParam(value = "appkey", required = false) String appkey,
                          @RequestParam(value = "platform", required = false) String platform,
                          @RequestParam(value = "client_id", required = false) String clientId,
                          @RequestParam(value = "client_token", required = false) String clientToken,
                          @RequestParam(value = "channelid", required = false) String channelid) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);

        if (StringUtil.isEmpty(appkey)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientId)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientToken)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(platform)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(channelid)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        try {
            if (StringUtil.isEmpty(uno)) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (gid == 0 || gid == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(gid);
            long tomorrow = 60l * 60l * 24l * 1000l;
            long endTime = activityGoods.getEndTime().getTime();
            long now = System.currentTimeMillis();
            if (now > (endTime + tomorrow)) {
                resultMsg.setMsg("time.is.out");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            //通过 商品领取类型 和 用户领取记录 判断 是否允许用户 继续 领取
            AllowExchangeStatus allowExchangeStatus = giftMarketWebLogic.allowGetCode(activityGoods, profileId);
            if (allowExchangeStatus.equals(AllowExchangeStatus.NO_ALLOW)) {
                resultMsg.setMsg("message.user.syserror");   //系统繁忙
                resultMsg.setRs(ResultListMsg.CODE_E);
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED)) {
                resultMsg.setMsg("user.gift.getcode.one");   //永久一次
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED_BY_DAY)) {
                resultMsg.setMsg("user.gift.getcode.by.day");    //一天一次
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED_BY_INTRVAL)) {
                resultMsg.setMsg("user.gift.getcode.by.intaval"); //时间间隔
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            UserExchangeLog userExchangeLog = pointWebLogic.getCode(uno, profileId, appkey, gid, getIp(request), true);
            if (userExchangeLog == null) {
                resultMsg.setMsg("getcode.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("value", userExchangeLog.getSnValue1());
            resultMsg.setMsg("getcode.is.success");
            resultMsg.setResult(map);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/taocode")
    public String taoCode(HttpServletRequest request, @RequestParam(value = "uno", required = false) String uno,
                          @RequestParam(value = "profileid", required = false) String profileid,
                          @RequestParam(value = "gid", required = false) Long gid,
                          @RequestParam(value = "appkey", required = false) String appkey,
                          @RequestParam(value = "platform", required = false) String platform,
                          @RequestParam(value = "client_id", required = false) String clientId,
                          @RequestParam(value = "client_token", required = false) String clientToken,
                          @RequestParam(value = "channelid", required = false) String channelid) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);
        if (StringUtil.isEmpty(appkey)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientId)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientToken)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(platform)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(channelid)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        if (StringUtil.isEmpty(uno)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        if (gid == 0 || gid == null) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        try {
            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(gid);
            long tomorrow = 60l * 60l * 24l * 1000l;
            long endTime = activityGoods.getEndTime().getTime();
            long now = System.currentTimeMillis();
            if (now > (endTime + tomorrow)) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("time.is.out");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        List<ExchangeGoodsItem> list = pointWebLogic.taoCode(uno, profileid, appkey, gid, getIp(request));

        if (list.size() < 10) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("taocode.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> valueList = new LinkedList<String>();
        for (int i = 0; i < list.size(); i++) {
            valueList.add(list.get(i).getSnValue1());
        }
        map.put("value", valueList);
        resultMsg.setMsg("taocode.is.success");
        resultMsg.setResult(map);
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/mygift")
    public String mygift(@RequestParam(value = "pnum", required = false) Integer page,
                         @RequestParam(value = "count", required = false) Integer count,
                         @RequestParam(value = "uno", required = false) String uno,
                         @RequestParam(value = "appkey", required = false) String appkey,
                         @RequestParam(value = "platform", required = false) String platform,
                         @RequestParam(value = "client_id", required = false) String clientId,
                         @RequestParam(value = "client_token", required = false) String clientToken,
                         @RequestParam(value = "channelid", required = false) String channelid) {
        ResultPageMsg resultMsg = new ResultPageMsg(ResultPageMsg.CODE_S);
        page = page == null ? 1 : page;
        page = page <= 0 ? 1 : page;
        count = count == null ? 10 : count;
        if (StringUtil.isEmpty(appkey)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientId)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientToken)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(platform)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(channelid)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(uno)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        Pagination pagination = new Pagination(count * page, page, count);
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(UserExchangeLogField.EXCHANGE_TIME, QuerySortOrder.DESC));

        try {
            PageRows<ActivityMygiftDTO> pageRows = pointWebLogic.queryUserExchangeLogByUno(uno, pagination);
            if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("gift.is.null");
                resultMsg.setPage(new JsonPagination(pageRows.getPage()));
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            resultMsg.setResult(pageRows.getRows());
            resultMsg.setPage(new JsonPagination(pageRows.getPage()));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/mygiftdetail")
    public String getMyGiftDetail(@RequestParam(value = "aid", required = false) Long aid,
                                  @RequestParam(value = "gid", required = false) Long gid,
                                  @RequestParam(value = "uno", required = false) String uno,
                                  @RequestParam(value = "appkey", required = false) String appkey,
                                  @RequestParam(value = "platform", required = false) String platform,
                                  @RequestParam(value = "client_id", required = false) String clientId,
                                  @RequestParam(value = "client_token", required = false) String clientToken,
                                  @RequestParam(value = "channelid", required = false) String channelid) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);
        if (StringUtil.isEmpty(appkey)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientId)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientToken)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(platform)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(channelid)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(uno)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (aid == null) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (gid == null) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }


        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/syncInfo")
    public String syncInfo(@RequestParam(value = "appkey", required = false) String appkey,
                           @RequestParam(value = "platform", required = false) String platform,
                           @RequestParam(value = "client_id", required = false) String clientId,
                           @RequestParam(value = "client_token", required = false) String clientToken,
                           @RequestParam(value = "channelid", required = false) String channelid,
                           @RequestParam(value = "sid", required = false) Long sid
    ) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);
        if (StringUtil.isEmpty(appkey)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientId)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientToken)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(platform)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(channelid)) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        if (sid == null || sid == 0) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        try {
            ShareInfo shareInfo = SyncServiceSngl.get().choiceShareInfo(sid);
            if (shareInfo == null) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("shareinfo.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            JoymeAppShareInfoDTO joymeAppShareInfoDTO = new JoymeAppShareInfoDTO();
            joymeAppShareInfoDTO.setShareBody(shareInfo.getShareBody().getShareBody());
            joymeAppShareInfoDTO.setShareTopic(shareInfo.getShareTopic().getShareTopic());
            joymeAppShareInfoDTO.setShareUrl(shareInfo.getBaseInfo().getShareSource());
            joymeAppShareInfoDTO.setPicUrl(shareInfo.getShareBody().getPicUrl());
            joymeAppShareInfoDTO.setShareId(sid);

            resultMsg.setResult(joymeAppShareInfoDTO);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }


    @ResponseBody
    @RequestMapping(value = "/syncinfo")
    public String redSyncInfo(@RequestParam(value = "appkey", required = false) String appkey,
                              @RequestParam(value = "platform", required = false) String platform,
                              @RequestParam(value = "client_id", required = false) String clientId,
                              @RequestParam(value = "client_token", required = false) String clientToken,
                              @RequestParam(value = "channelid", required = false) String channelid,
                              @RequestParam(value = "sid", required = false) Long sid
    ) {
        return syncInfo(appkey, platform, clientId, clientToken, channelid, sid);
    }
}
