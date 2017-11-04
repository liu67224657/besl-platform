package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.json;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Address;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.ResultPageMsg;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDetailDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityMygiftDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.AllowExchangeStatus;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-12-5
 * Time: 下午4:16
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/json/gameclient/webview/giftmarket")
public class GameClientGiftmarketJsonController extends BaseRestSpringController {

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    @RequestMapping(value = "/newsclientgiftlist")
    @ResponseBody
    public String jsonGiftList(@RequestParam(value = "pnum", required = false) String page,
                               @RequestParam(value = "npnum", required = false) String npage,
                               @RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            PageRows<ActivityDTO> hotPageRows = null;
            int cpage = 1;
            Pagination pagination = new Pagination(count * cpage, cpage, count);
            if (!StringUtil.isEmpty(page)) {
                cpage = Integer.parseInt(page) + 1;
                pagination = new Pagination(count * cpage, cpage, count);
            }
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.HOT_ACTIVITY, 1))
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QueryCriterions.in(ActivityGoodsField.CHANNEL_TYPE, new Object[]{MobileExclusive.DEFAULT.getCode(), MobileExclusive.NEWSCLIENT.getCode()}))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
            hotPageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, pagination);
            if (hotPageRows == null || (hotPageRows != null && hotPageRows.getPage().getMaxPage() <= hotPageRows.getPage().getCurPage())) {
                if (StringUtil.isEmpty(npage)) {
                    npage = "1";
                }
                int newPage = Integer.parseInt(npage);
                Pagination pagination2 = new Pagination(count * newPage, newPage, count);
                QueryExpress queryExpress3 = new QueryExpress()
                        .add(QueryCriterions.eq(ActivityGoodsField.HOT_ACTIVITY, 0))
                        .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QueryCriterions.ne(ActivityGoodsField.CHANNEL_TYPE, MobileExclusive.WEIXIN.getCode()))
                        .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
                PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress3, pagination2);
                if (pageRows == null) {
                    msg.setRs(ResultCodeConstants.GAME_GUIDE_RESULT_NULL.getCode());
                    msg.setMsg(ResultCodeConstants.GAME_GUIDE_RESULT_NULL.getMsg());
                    return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(msg);
                }
                returnMap.put("newrows", pageRows.getRows());
                returnMap.put("newpage", pageRows.getPage());
            } else {
                returnMap.put("newrows", "");
            }

//            List<GiftReserve> list = PointServiceSngl.get().queryGiftReserveByList(new QueryExpress().add(QueryCriterions.eq(GiftReserveField.PROFILEID, profileId)).add(QueryCriterions.eq(GiftReserveField.APPKEY, appkey)));
            if (hotPageRows != null) {
                if (StringUtil.isEmpty(page)) {
                    returnMap.put("hotrows", hotPageRows.getRows());
                    returnMap.put("hotpage", hotPageRows.getPage());
                } else if (hotPageRows.getPage().getMaxPage() > Integer.parseInt(page)) {
                    returnMap.put("hotrows", hotPageRows.getRows());
                    returnMap.put("hotpage", hotPageRows.getPage());
                } else {
                    returnMap.put("hotrows", "");
                }
            } else {
                returnMap.put("hotrows", "");
            }
//            returnMap.put("reservelist", list);
            msg.setResult(returnMap);
        } catch (ServiceException e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    @RequestMapping(value = "/newsclientmygiftlist")
    @ResponseBody
    public String jsonMyGiftList(@RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                                 @RequestParam(value = "profileid", required = false) String profileId,
                                 @RequestParam(value = "appkey", required = false) String appkey
    ) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            Pagination pagination = new Pagination(count * page, page, count);
            long now = System.currentTimeMillis();
            long lastWeek = 60l * 60l * 24l * 1000 * 7;
            long selectDate = now - lastWeek;
            UserCenterSession userSession = new UserCenterSession();
            userSession.setProfileId(profileId);
            PageRows<ActivityMygiftDTO> pageRows = giftMarketWebLogic.queryActivityMygiftDTO(pagination, new Date(selectDate), new Date(now), userSession.getProfileId(), appkey);

            if (pageRows == null) {
                msg.setRs(ResultCodeConstants.GAME_GUIDE_RESULT_NULL.getCode());
                msg.setMsg(ResultCodeConstants.GAME_GUIDE_RESULT_NULL.getMsg());
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(msg);
            }
            Map<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("rows", pageRows.getRows());
            returnMap.put("page", pageRows.getPage());
            msg.setResult(returnMap);
        } catch (ServiceException e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    @ResponseBody
    @RequestMapping(value = "/getcode")
    public String getCode(HttpServletRequest request, @RequestParam(value = "uno", required = false) String uno,
                          @RequestParam(value = "profileid", required = false) String profileid,
                          @RequestParam(value = "appkey", required = false) String appkey,
                          @RequestParam(value = "gid", required = false) Long gid,
                          @RequestParam(value = "clientid", required = false) String clientId) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(profileid)) {
                appkey = AppUtil.getAppKey(appkey);
                AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
                if (authApp == null) {
                    resultMsg.setRs(ResultCodeConstants.APP_NOT_EXISTS.getCode());
                    resultMsg.setMsg(ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                    return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
                if (authApp.getProfileKey() == null) {
                    resultMsg.setRs(ResultListMsg.CODE_E);
                    resultMsg.setMsg("profilekey.is.null");
                    return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
                profileid = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            }
            if (StringUtil.isEmpty(uno) || StringUtil.isEmpty(profileid) || StringUtil.isEmpty(appkey)) {
                resultMsg.setRs(ResultCodeConstants.GAME_GUIDE_PARAM_NULL.getCode());
                resultMsg.setMsg(ResultCodeConstants.GAME_GUIDE_PARAM_NULL.getMsg());
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (gid == 0 || gid == null) {
                resultMsg.setRs(ResultCodeConstants.GAME_GUIDE_PARAM_NULL.getCode());
                resultMsg.setMsg(ResultCodeConstants.GAME_GUIDE_PARAM_NULL.getMsg());
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileid);
            if (profile == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("user.not.login");
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
            }


            ActivityGoods activityGoods = PointServiceSngl.get().getActivityGoods(gid);
            if (activityGoods.getChannelType().getCode() != MobileExclusive.DEFAULT.getCode() && activityGoods.getChannelType().getCode() != MobileExclusive.NEWSCLIENT.getCode()) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("is.not.newsclient.exclusive");
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
            }


            long tomorrow = 60l * 60l * 24l * 1000l;
            long endTime = activityGoods.getEndTime().getTime();
            long now = System.currentTimeMillis();
            if (now > (endTime + tomorrow)) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("time.is.out");
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            //通过 商品领取类型 和 用户领取记录 判断 是否允许用户 继续 领取
            //
            AllowExchangeStatus allowExchangeStatus = giftMarketWebLogic.allowGetCode(activityGoods, profileid);
            if (allowExchangeStatus.equals(AllowExchangeStatus.NO_ALLOW)) {
                resultMsg.setMsg("message.user.syserror");   //系统繁忙
                resultMsg.setRs(ResultListMsg.CODE_E);
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED)) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("user.gift.getcode.one");   //永久一次
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED_BY_DAY)) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("user.gift.getcode.by.day");    //一天一次
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if (allowExchangeStatus.equals(AllowExchangeStatus.HAS_EXCHANGED_BY_INTRVAL)) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("user.gift.getcode.by.intaval"); //时间间隔
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            //当天时间
            //按设备防刷
            String date_short = DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_SHORT);
            if (activityGoods.getTimeType().equals(ConsumeTimesType.ONETIMESMANYDAY)) {
                //type为2时 是取数据  为1时是存数据
                String value = JoymeAppServiceSngl.get().validateInfo(date_short + "_" + clientId + "_" + activityGoods.getActivityGoodsId(), "2");
                if (!StringUtil.isEmpty(value)) {
                    resultMsg.setRs(ResultCodeConstants.VALID_INFO_FAILED.getCode());
                    resultMsg.setMsg(ResultCodeConstants.VALID_INFO_FAILED.getMsg());
                    return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
                }
            }


            UserExchangeLog userExchangeLog = pointWebLogic.getCode(uno, profileid, AppUtil.getAppKey(appkey), gid, getIp(request), false);
            if (userExchangeLog == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("getcode.is.null");
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
            } else {
                if (activityGoods.getTimeType().equals(ConsumeTimesType.ONETIMESMANYDAY)) {
                    JoymeAppServiceSngl.get().validateInfo(date_short + "_" + clientId + "_" + activityGoods.getActivityGoodsId(), "1");
                }
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("value", userExchangeLog.getSnValue1());
            resultMsg.setMsg("getcode.is.success");
            resultMsg.setResult(map);
        } catch (ServiceException e) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            if (e.equals(PointServiceException.USER_POINT_UPDATE_FAILED)) {

                resultMsg.setMsg("user.point.update.failed");
            } else if (e.equals(PointServiceException.USER_POINT_NOT_ENOUGH)) {  //1
                resultMsg.setMsg("point.not.enough");
            } else {
                resultMsg.setMsg("system.error");
                GAlerter.lab(this.getClass().getName() + " occured ServicdeExcpetion.e", e);
            }
            return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

}
