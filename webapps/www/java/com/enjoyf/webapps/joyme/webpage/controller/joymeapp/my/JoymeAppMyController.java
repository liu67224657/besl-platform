package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.my;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.service.userprops.UserPropDomain;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserProperty;
import com.enjoyf.platform.service.userprops.UserPropsServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.joymeapp.JoymeAppClientConstant;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.ResultPageMsg;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.goods.GoodsSimpleDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.my.AppMyConfigDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.AllowExchangeStatus;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.anime.AbstractAnimeBaseController;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-12-2
 * Time: 下午7:48
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/my")
public class JoymeAppMyController extends AbstractAnimeBaseController {


    private static int pointValue = 5;//签到基础分

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @RequestMapping(value = "/getconfig")
    @ResponseBody
    public String getConfig(HttpServletRequest request, HttpServletResponse response) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            JoymeAppClientConstant joymeAppClientConstant = getJoymeAppClientConstant(request);
            if (joymeAppClientConstant == null) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            String appkey = joymeAppClientConstant.getAppkey();
            if (StringUtil.isEmpty(appkey)) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("appkey.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("authApp.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            AppMyConfigDTO appMyConfigDTO = new AppMyConfigDTO();
            if (authApp.getDisplayMy() == 0) {
                appMyConfigDTO.setOpenmy("false");
            } else {
                appMyConfigDTO.setOpenmy("true");
            }
//            if (authApp.getDisplayRed() == 0) {
//                appMyConfigDTO.setOpenred("false");
//            } else {
//                appMyConfigDTO.setOpenred("true");
//            }

            appMyConfigDTO.setTime(authApp.getModifyDate() == null ? new Date().getTime() : authApp.getModifyDate().getTime());
            appMyConfigDTO.setUrl(WebappConfig.get().getUrlWww() + "/my/list");
//            appMyConfigDTO.setUrl("http://op.joyme.com/public/test/appwebview.html");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rows", appMyConfigDTO);
            msg.setResult(map);
            msg.setMsg("success");

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
        }

        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    @RequestMapping(value = "/querypoint")
    @ResponseBody
    public String queryPoint(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "uno", required = false) String uno) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {

            JoymeAppClientConstant joymeAppClientConstant = getJoymeAppClientConstant(request);
            if (joymeAppClientConstant == null) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            String appkey = joymeAppClientConstant.getAppkey();
            if (StringUtil.isEmpty(appkey)) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("appkey.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("auth.app.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            if (StringUtil.isEmpty(authApp.getProfileKey())) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("pointkey.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByUno(uno, authApp.getProfileKey());

            PointKeyType pointKeyType = PointKeyType.getByCode(appkey);
            String pointKey = "";
            if (pointKeyType == null) {
                pointKey = "default";
            } else {
                pointKey = pointKeyType.getValue();
            }

            UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);
            //todo 放到logic

            int userHasPoint = userPoint == null ? 0 : userPoint.getUserPoint();

            Map<String, Object> returnMap = new HashMap<String, Object>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("point", userHasPoint);
            returnMap.put("rows", map);
            msg.setResult(map);
            msg.setMsg("success");
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    @RequestMapping(value = "/modifypoint")
    @ResponseBody
    public String modifyPoint(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "uno", required = false) String uno,
                              @RequestParam(value = "point", required = false) String point) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);

        try {
            if (StringUtil.isEmpty(uno)) {
                uno = HTTPUtil.getParam(request, "uno");
            }
            String appkey = HTTPUtil.getParam(request, "appkey");
            if (StringUtil.isEmpty(appkey)) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("appkey.is.null");
                msg.setResult("");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("auth.app.is.null");
                msg.setResult("");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            if (StringUtil.isEmpty(authApp.getProfileKey())) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("profilekey.is.null");
                msg.setResult("");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByUno(uno, authApp.getProfileKey());
            if (profile == null) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("profile.is.null");
                msg.setResult("");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

//            appkey = AppUtil.getAppKey(appkey);
//            PointwallWall wall = PointServiceSngl.get().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallAppField.APPKEY, appkey)));
//            PointKeyType pointKeyType = null;
//            if (wall != null && !com.enjoyf.platform.util.StringUtil.isEmpty(wall.getPointKey())) {
//                pointKeyType = PointKeyType.getByCode(wall.getPointKey());
//            }
//            if (pointKeyType == null) {
//                pointKeyType = PointKeyType.getByCode(appkey);
//            }
//
//            if (pointKeyType == null) {
//                pointKeyType = PointKeyType.DEFAULT;
//            }
//
//            UserPoint userPoint = PointServiceSngl.get().getUserPoint(new QueryExpress().add(QueryCriterions.eq(UserPointField.PROFILEID, profile.getProfileId()))
//                    .add(QueryCriterions.eq(UserPointField.POINTKEY, pointKeyType.getValue())));
//            //todo 放到logic
//            if (userPoint == null) {
//                userPoint = new UserPoint();
//                userPoint.setUserNo(profile.getUno());
//                userPoint.setProfileId(profile.getProfileId());
//                userPoint.setConsumeAmount(0);
//                userPoint.setConsumeExchange(0);
//                userPoint.setUserPoint(0);
//                userPoint.setPointKey(pointKeyType.getValue());
//                PointServiceSngl.get().addUserPoint(userPoint);
//            }
            //todo
            UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);
            int userHasPoint = userPoint == null ? 0 : userPoint.getUserPoint();
            int intPoint = Integer.parseInt(point);
            if (intPoint > userHasPoint || intPoint <= 0) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("point.not.enough");
                msg.setResult("");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            //TODO
            PointKeyType pointKeyType = PointServiceSngl.get().getPointKeyType(appkey);
            PointActionHistory actionHistory = new PointActionHistory();
            actionHistory.setUserNo(profile.getUno());
            actionHistory.setProfileId(profile.getProfileId());
            actionHistory.setActionType(PointActionType.APPKEY);
            actionHistory.setActionDescription("通过【" + authApp.getAppName() + "】客户端操作");
            actionHistory.setPointValue(-intPoint);
            actionHistory.setCreateDate(new Date());
            actionHistory.setActionDate(new Date());
            boolean bool = PointServiceSngl.get().increasePointActionHistory(actionHistory, pointKeyType);
            if (bool) {
                Map<String, Object> mapMessage = new HashMap<String, Object>();
                mapMessage.put("remain_point", (userHasPoint - intPoint));
                msg.setMsg("success");
                msg.setResult(mapMessage);
            }
            return JsonBinder.buildNormalBinder().toJson(msg);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
        }
//        try {
//
//            JoymeAppClientConstant joymeAppClientConstant = getJoymeAppClientConstant(request);
//            if (joymeAppClientConstant == null) {
//                msg.setRs(ResultObjectMsg.CODE_E);
//                msg.setMsg("param.is.empty");
//                return JsonBinder.buildNormalBinder().toJson(msg);
//            }
//            String appkey = joymeAppClientConstant.getAppkey();
//            if (StringUtil.isEmpty(appkey)) {
//                msg.setRs(ResultObjectMsg.CODE_E);
//                msg.setMsg("appkey.is.null");
//                return JsonBinder.buildNormalBinder().toJson(msg);
//            }
//            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
//            if (authApp == null) {
//                msg.setRs(ResultObjectMsg.CODE_E);
//                msg.setMsg("auth.app.is.null");
//                return JsonBinder.buildNormalBinder().toJson(msg);
//            }
//
//            if (StringUtil.isEmpty(authApp.getProfileKey())) {
//                msg.setRs(ResultObjectMsg.CODE_E);
//                msg.setMsg("pointkey.is.null");
//                return JsonBinder.buildNormalBinder().toJson(msg);
//            }
//            Profile profile = UserCenterServiceSngl.get().getProfileByUno(uno, authApp.getProfileKey());
//
//            PointKeyType pointKeyType = PointKeyType.getByCode(appkey);
//            if (pointKeyType == null) {
//                pointKeyType = PointKeyType.DEFAULT;
//            }
//
//            PointActionHistory actionHistory = new PointActionHistory();
//            actionHistory.setUserNo(profile.getUno());
//            actionHistory.setProfileId(profile.getProfileId());
//            actionHistory.setActionType(PointActionType.APPKEY);
//            actionHistory.setActionDescription("通过【" + authApp.getAppName() + "】客户端操作");
//            actionHistory.setPointValue(Integer.parseInt(point));
//            actionHistory.setCreateDate(new Date());
//            actionHistory.setActionDate(new Date());
//
//            boolean bool = PointServiceSngl.get().increasePointActionHistory(actionHistory, pointKeyType);
//
//
//            Map<String, Object> returnMap = new HashMap<String, Object>();
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("point", "");
//            returnMap.put("rows", map);
//            msg.setResult(map);
//            msg.setMsg("success");
//        } catch (ServiceException e) {
//            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
//            msg.setRs(ResultObjectMsg.CODE_E);
//            msg.setMsg("system.error");
//        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    @RequestMapping(value = "/tasksign")
    @ResponseBody
    public String sign(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "profileid", required = false) String profileId,
                       @RequestParam(value = "appkey", required = false) String appkey) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(profileId)) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("profileId.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("profile.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            UserPropKey userPropKey = new UserPropKey(UserPropDomain.DEFAULT, profileId, "task.mine.sign");

            PointKeyType pointKeyType = PointServiceSngl.get().getPointKeyType(appkey);

            UserProperty property = UserPropsServiceSngl.get().getUserProperty(userPropKey);

            if (StringUtil.isEmpty(property.getValue())) {
                property = UserPropsServiceSngl.get().increaseUserProperty(userPropKey, 1);
                addSignValue(pointValue, pointKeyType, profile);
                msg.setResult(pointValue);
                msg.setMsg("success");
                return JsonBinder.buildNormalBinder().toJson(msg);
                // 第一次 +积分
            } else {
                Date date = property.getModifyDate();
                Date now = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int propDay = calendar.get(Calendar.DAY_OF_YEAR);
                calendar.setTime(now);
                int nowDay = calendar.get(Calendar.DAY_OF_YEAR);

                // 同一天签到直接返回错误
                if (nowDay == propDay) {
                    msg.setRs(ResultObjectMsg.CODE_E);
                    msg.setMsg("today.is.not.sign");
                    return JsonBinder.buildNormalBinder().toJson(msg);
                    //断签
                } else if (nowDay - propDay > 1) {
                    /*
                    * 不是连续签到 按上一次签到的天数+1天 加积分 然后置空连续签到天数
                    * 例如：上次连续签到3天 然后断签 那这次可领取的就是第4天的分数
                     */
                    int day = property.getIntValue();
                    int value = 0;
                    int signDay = day + 1;
                    signDay = signDay % 7;
                    if (signDay == 0) {
                        value = 7 * pointValue;
                    } else {
                        value = signDay * pointValue;
                    }
                    addSignValue(value, pointKeyType, profile);
                    property.setInitialDate(date);
                    property.setModifyDate(now);
                    property.setUserPropKey(userPropKey);
                    property.setValue((day + 1) + "");
                    boolean bool = UserPropsServiceSngl.get().modifyUserProperty(property);
                    msg.setResult(value);
                    msg.setMsg("success");
                    return JsonBinder.buildNormalBinder().toJson(msg);
                    // 第一次 +积分
                } else {
                    int signDay = property.getIntValue();
                    property.setInitialDate(date);
                    property.setModifyDate(now);
                    property.setUserPropKey(userPropKey);
                    property.setValue((signDay + 1) + "");
                    boolean bool = UserPropsServiceSngl.get().modifyUserProperty(property);

                    int value;
                    signDay = signDay + 1;
                    signDay = signDay % 7;
                    if (signDay == 0) {
                        value = 7 * pointValue;
                    } else {
                        value = signDay * pointValue;
                    }
                    addSignValue(value, pointKeyType, profile);
                    msg.setResult(value);
                    msg.setMsg("success");
                }
            }
        } catch (ServiceException e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    @RequestMapping(value = "/jsongiftlist")
    @ResponseBody
    public String jsonGiftList(@RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                               @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                               @RequestParam(value = "profileid", required = false) String profileId,
                               @RequestParam(value = "appkey", required = false) String appkey,
                               @RequestParam(value = "type", required = false) String type) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            Pagination pagination = new Pagination(count * page, page, count);
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))

                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));

            if ("1".equals(type)) {
                queryExpress.add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, GoodsActionType.GIFT.getCode()));
            } else {
                GoodsActionType goodsActionType = GoodsActionType.getByCode(Integer.parseInt(type));
                if (goodsActionType == null) {
                    goodsActionType = GoodsActionType.MIDOU;
                }
                queryExpress.add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, goodsActionType.getCode()));

            }
            PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, pagination);

            if (pageRows == null) {
                msg.setRs(ResultCodeConstants.GAME_GUIDE_RESULT_NULL.getCode());
                msg.setMsg(ResultCodeConstants.GAME_GUIDE_RESULT_NULL.getMsg());
                return com.enjoyf.platform.webapps.common.encode.JsonBinder.buildNormalBinder().toJson(msg);
            }
            List<GiftReserve> list = PointServiceSngl.get().queryGiftReserveByList(new QueryExpress().add(QueryCriterions.eq(GiftReserveField.PROFILEID, profileId)).add(QueryCriterions.eq(GiftReserveField.APPKEY, appkey)));
            Map<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("rows", pageRows.getRows());
            returnMap.put("page", pageRows.getPage());
            returnMap.put("reservelist", list);
            msg.setResult(returnMap);
        } catch (ServiceException e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    @RequestMapping(value = "/jsonmygiftlist")
    @ResponseBody
    public String jsonMyGiftList(@RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                                 @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                                 @RequestParam(value = "profileid", required = false) String profileId,
                                 @RequestParam(value = "appkey", required = false) String appkey,
                                 @RequestParam(value = "type", required = false) String type) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            Pagination pagination = new Pagination(count * page, page, count);
            PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryCoinActivityList(profileId, appkey, Integer.parseInt(type), pagination);
            if (CollectionUtil.isEmpty(pageRows.getRows())) {
                msg.setRs(ResultObjectMsg.CODE_E);
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            Map<String, Object> returnMap = new HashMap<String, Object>();
            returnMap.put("rows", pageRows.getRows());
            returnMap.put("page", pageRows.getPage());
            msg.setResult(returnMap);
            return JsonBinder.buildNormalBinder().toJson(msg);
        } catch (ServiceException e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
    }

    @RequestMapping(value = "/reserve")
    @ResponseBody
    public String reserve(HttpServletRequest request, @RequestParam(value = "appkey", required = false) String appkey,
                          @RequestParam(value = "profileid", required = false) String profileId,
                          @RequestParam(value = "aid", required = false) String aid) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(appkey) || StringUtil.isEmpty(profileId) || StringUtil.isEmpty(aid)) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("param.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            GiftReserve giftReserve = PointServiceSngl.get().getGiftReserve(new QueryExpress().add(QueryCriterions.eq(GiftReserveField.APPKEY, appkey))
                    .add(QueryCriterions.eq(GiftReserveField.AID, Long.parseLong(aid)))
                    .add(QueryCriterions.eq(GiftReserveField.PROFILEID, profileId)));
            if (giftReserve != null) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("reserve.is.exsits");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                msg.setRs(ResultObjectMsg.CODE_E);
                msg.setMsg("profile.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }
            giftReserve = new GiftReserve();
            giftReserve.setAppkey(appkey);
            giftReserve.setAid(Long.parseLong(aid));
            giftReserve.setCreateIp(getIp(request));
            giftReserve.setCreateTime(new Date());
            giftReserve.setLoginDomain(LoginDomain.CLIENT);
            giftReserve.setProfileId(profileId);
            giftReserve.setValidStatus(ValidStatus.INVALID);
            giftReserve.setUno(profile.getUno());
            PointServiceSngl.get().createGiftReserve(giftReserve);

        } catch (ServiceException e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    /**
     * 兑换商品
     *
     * @param request
     * @param response
     * @param goodsId
     * @param activityId
     * @param appkey
     * @param profileId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/exchange")
    public String exchange(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "gid", required = true) long goodsId,
                           @RequestParam(value = "aid", required = true) long activityId,
                           @RequestParam(value = "appkey", required = false) String appkey,
                           @RequestParam(value = "profileid", required = false) String profileId,
                           @RequestParam(value = "type", required = false) String type) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        try {
            ActivityGoods activityGoods= PointServiceSngl.get().getActivityGoods(goodsId);
            if (activityGoods == null) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                resultMsg.setMsg(i18nSource.getMessage("activity.is.null", null, Locale.CHINA));
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            if ((activityGoods.getEndTime().getTime() - System.currentTimeMillis()) < 0) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                resultMsg.setMsg(i18nSource.getMessage("activity.has.expire", null, Locale.CHINA));
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);


            UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);

            int userHasPoint = userPoint == null ? 0 : userPoint.getUserPoint();
            if (userHasPoint < activityGoods.getGoodsPoint()) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                resultMsg.setMsg(i18nSource.getMessage("point.not.enough", null, Locale.CHINA));
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            AllowExchangeStatus allowExchangeStatus = giftMarketWebLogic.allowExchangeGoods(activityGoods, profile.getProfileId());

            if (!AllowExchangeStatus.ALLOW.equals(allowExchangeStatus)) {
                if (AllowExchangeStatus.HAS_EXCHANGED_BY_DAY.equals(allowExchangeStatus)) {
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    resultMsg.setMsg(i18nSource.getMessage("goods.has.exchanged.day", null, Locale.CHINA));
                } else {
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    resultMsg.setMsg(i18nSource.getMessage("goods.has.exchanged", null, Locale.CHINA));
                }
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            GoodsActionType goodsActionType = null;
            String address = "";
            if ("1".equals(type)) {
                goodsActionType = GoodsActionType.GIFT;
            } else {
                goodsActionType = GoodsActionType.getByCode(Integer.parseInt(type));
                UserAccount userAccount = UserCenterServiceSngl.get().getUserAccount(profile.getUno());
                if (userAccount != null) {
                    if (userAccount.getAddress() != null) {
                        address = userAccount.getAddress().toJsonStr();
                    }
                }
            }
            UserConsumeLog userConsumeLog = PointServiceSngl.get().consumeGoods(profile.getUno(), profile.getProfileId(), appkey, goodsId, new Date(), getIp(request), goodsActionType, address);
            GoodsItem goodsItem = null;
            if (userConsumeLog.getGoodsItemId() > 0) {
                goodsItem = PointServiceSngl.get().getGoodsItemById(userConsumeLog.getGoodsItemId());
            }
            resultMsg.getResult().add(genDTO(activityGoods, goodsItem));
        } catch (ServiceException e) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);

            if (e.equals(PointServiceException.GOODS_NOT_EXISTS)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.not.exists", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.GOODS_GET_FAILED)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.get.failed", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.GOODS_OUTOF_RESTAMMOUNT)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.out.of.rest.amount", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.GOODS_ITEM_NOT_EXISTS)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.item.not.exists", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.GOODS_ITEM_GET_FAILED)) {
                resultMsg.setMsg(i18nSource.getMessage("goods.item.not.exists", null, Locale.CHINA));
            } else if (e.equals(PointServiceException.USER_POINT_NOT_ENOUGH)) {
                resultMsg.setMsg(i18nSource.getMessage("point.not.enough", null, Locale.CHINA));
            } else {
                resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
                GAlerter.lab(this.getClass().getName() + " occured ServicdeExcpetion.e", e);
            }
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @RequestMapping(value = "/getaddress")
    @ResponseBody
    public String getAddress(@RequestParam(value = "profileid", required = false) String profileId) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(profileId)) {
                msg.setRs(ResultPageMsg.CODE_E);
                msg.setMsg("profileId.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            UserAccount userAccount = UserCenterServiceSngl.get().getUserAccount(profile.getUno());
            if (userAccount == null) {
                msg.setMsg("address.is.null");
            } else {
                if (userAccount.getAddress() == null) {
                    msg.setMsg("address.is.null");
                } else {
                    msg.setResult(userAccount);
                }
            }
        } catch (ServiceException e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    @RequestMapping(value = "/modifyaddress")
    @ResponseBody
    public String insertAddress(@RequestParam(value = "profileid", required = false) String profileId,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "phone", required = false) String phone,
                                @RequestParam(value = "zipcode", required = false) String zipcode,
                                @RequestParam(value = "address", required = false) String address,
                                @RequestParam(value = "province", required = false) String province,
                                @RequestParam(value = "city", required = false) String city,
                                @RequestParam(value = "county", required = false) String county) {
        ResultObjectMsg msg = new ResultObjectMsg(ResultPageMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(profileId)) {
                msg.setRs(ResultPageMsg.CODE_E);
                msg.setMsg("profileId.is.null");
                return JsonBinder.buildNormalBinder().toJson(msg);
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            Address userAddress = new Address();
            userAddress.setAddress(address);
            userAddress.setContact(name);
            userAddress.setPhone(phone);
            userAddress.setPostcode(zipcode);
            userAddress.setCity(city);
            userAddress.setProvince(province);
            userAddress.setCounty(county);

            boolean bool = UserCenterServiceSngl.get().modifyUserAccount(new UpdateExpress().set(UserAccountField.ADDRESS, userAddress.toJsonStr()), profile.getUno());
            if (bool) {
                msg.setMsg("success");
            }
        } catch (ServiceException e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(msg);
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }

    private GoodsSimpleDTO genDTO(ActivityGoods goods, GoodsItem goodsItem) {
        GoodsSimpleDTO dto = new GoodsSimpleDTO();
        dto.setGoodsId(goods.getActivityGoodsId());
        dto.setGoodsName(goods.getActivitySubject());
        dto.setGoodsType(goods.getActivitygoodsType().getCode());

        if (goodsItem != null) {
            dto.setGoodsItemId(goodsItem.getGoodsItemId());
            dto.setItemName1(goodsItem.getSnName1());
            dto.setItemValue1(goodsItem.getSnValue1());
            dto.setItemName2(goodsItem.getSnName2());
            dto.setItemValue2(goodsItem.getSnValue2());
        }

        return dto;
    }

    private boolean addSignValue(int value, PointKeyType pointKeyType, Profile profile) throws ServiceException {
//        SignPointType signPointType = SignPointType.getByCode(value);
        PointActionHistory pointActionHistory = new PointActionHistory();
        pointActionHistory.setProfileId(profile.getProfileId());
        pointActionHistory.setUserNo(profile.getUno());
        pointActionHistory.setActionDate(new Date());
        pointActionHistory.setActionDescription("签到");
        pointActionHistory.setActionType(PointActionType.SIGN);
//        if (signPointType == null) {
//            pointActionHistory.setPointValue(Integer.parseInt(SignPointType.OTHER.getValue()));
//        } else {
//
//        }
        pointActionHistory.setPointValue(value);
        pointActionHistory.setCreateDate(new Date());
        return PointServiceSngl.get().increasePointActionHistory(pointActionHistory, pointKeyType);
    }


}