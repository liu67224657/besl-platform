package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.webview;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.content.MobileExclusive;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.point.pointwall.PointwallWall;
import com.enjoyf.platform.service.point.pointwall.PointwallWallAppField;
import com.enjoyf.platform.service.point.pointwall.PointwallWallField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.*;
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
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDetailDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityMygiftDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.AllowExchangeStatus;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-12-5
 * Time: 下午4:16
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/gameclient/webview/giftmarket")
public class GameClientGiftmarketWebViewController extends AbstractGameClientBaseController {
    private int pointValue = 5;//签到基础分
    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String uno = HTTPUtil.getParam(request, "uno");
            String appkey = HTTPUtil.getParam(request, "appkey");
            //刷新后显示哪个tag页  1=libao 2=迷豆商城
            String tabflag = HTTPUtil.getParam(request, "retype");

            //session优先级 比参数优先级低  ，不使用session来控制进入的tab页，使用retype参数
//            HttpSession session = request.getSession();
//            if (StringUtil.isEmpty(tabflag) && session != null) {
//                if (session.getAttribute(KEY_GIFT_TAB_FLAG) != null) {
//                    tabflag = (String) session.getAttribute(KEY_GIFT_TAB_FLAG);
//                }
//            }
//
//            if (session != null) {
//                session.removeAttribute(KEY_GIFT_TAB_FLAG);
//            }

            if (StringUtil.isEmpty(tabflag)) {
                tabflag = "libao";
            }

            //TODO 2个未空连续放一块吧
            if (StringUtil.isEmpty(appkey)) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            if (StringUtil.isEmpty(uno)) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            appkey = AppUtil.getAppKey(appkey);
            mapMessage.put("appkey", appkey);
            mapMessage.put("uno", uno);
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                mapMessage.put("errorMessage", "authapp.is.null");
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }

            //TODO 3个查询都用到Pagination 定义一个吧？
            Profile profile = UserCenterServiceSngl.get().getProfileByUno(uno, authApp.getProfileKey());
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.HOT_ACTIVITY, 1))//TODO
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QueryCriterions.ne(ActivityGoodsField.CHANNEL_TYPE, MobileExclusive.WEIXIN.getCode()))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
            PageRows<ActivityDTO> hotPageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, new Pagination(15, 1, 15));
            mapMessage.put("hotDto", hotPageRows);
            //TODO 注释
            if (hotPageRows == null || (hotPageRows != null && hotPageRows.getPage().getMaxPage() <= hotPageRows.getPage().getCurPage())) {

                //TODO rename
                QueryExpress queryExpress3 = new QueryExpress()
                        .add(QueryCriterions.eq(ActivityGoodsField.HOT_ACTIVITY, 0))//TODO
                        .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QueryCriterions.ne(ActivityGoodsField.CHANNEL_TYPE, MobileExclusive.WEIXIN.getCode()))
                        .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
                PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress3, new Pagination(15, 1, 15));
                mapMessage.put("newDto", pageRows);
            }

            mapMessage.put("profile", profile);

//            List<GiftReserve> list = PointServiceSngl.get().queryGiftReserveByList(new QueryExpress().add(QueryCriterions.eq(GiftReserveField.PROFILEID, profile.getProfileId())).add(QueryCriterions.eq(GiftReserveField.APPKEY, appkey)));
//            mapMessage.put("reservelist", list);

            PointwallWall wall = PointServiceSngl.get().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallField.APPKEY, appkey)));
            if (wall == null) {
                mapMessage.put("midouDto", "");
                return new ModelAndView("/views/jsp/gameclient/webview/giftmarket/giftmarketlist", mapMessage);
            }

            //TODO rename
            QueryExpress queryExpress2 = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC))
                    .add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, wall.getShopKey()));
            PageRows<ActivityDTO> midouPageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress2, new Pagination(15, 1, 15));
            mapMessage.put("midouDto", midouPageRows);
            mapMessage.put("type", wall.getShopKey());

            mapMessage.put("tabflag", tabflag);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        }
        return new ModelAndView("/views/jsp/gameclient/webview/giftmarket/giftmarketlist", mapMessage);
    }

    @RequestMapping(value = "/giftdetail")
    public ModelAndView giftDetail(HttpServletRequest request,
                                   @RequestParam(value = "aid", required = false) String aid,
                                   @RequestParam(value = "profileid", required = false) String profileId,
                                   @RequestParam(value = "type", required = false) String type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String uno = HTTPUtil.getParam(request, "uno");
            String appkey = HTTPUtil.getParam(request, "appkey");
            appkey = AppUtil.getAppKey(appkey);
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                mapMessage.put("errorMessage", "authapp.is.null");
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }

            if (!StringUtil.isEmpty(uno)) {
                profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            }

            if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(appkey) || StringUtil.isEmpty(aid)) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }

            ActivityDetailDTO activityDetailDTO = null;

            //TODO 加个类？
            if ("1".equals(type)) {
                activityDetailDTO = giftMarketWebLogic.getActivityGiftDetailDTO(Long.parseLong(aid));
            } else {
                activityDetailDTO = giftMarketWebLogic.getGoodsActivityDetailDTO(Long.parseLong(aid));
            }
            if (activityDetailDTO == null) {
                return new ModelAndView("redirect:/joymeapp/gameclient/webview/giftmarket/list?appkey=" + appkey + "&profileid=" + profileId);
            }

            //TODO 加个类？
            if (activityDetailDTO.getReserveType() == 1) {
                List<GiftReserve> list = PointServiceSngl.get().queryGiftReserveByList(new QueryExpress().add(QueryCriterions.eq(GiftReserveField.APPKEY, appkey)).add(QueryCriterions.eq(GiftReserveField.AID, Long.parseLong(aid))));
                mapMessage.put("reserveNum", list.size());
                GiftReserve giftReserve = PointServiceSngl.get().getGiftReserve(new QueryExpress().add(QueryCriterions.eq(GiftReserveField.PROFILEID, profileId))
                        .add(QueryCriterions.eq(GiftReserveField.APPKEY, appkey))
                        .add(QueryCriterions.eq(GiftReserveField.AID, Long.parseLong(aid))));
                mapMessage.put("giftReserve", giftReserve);
            }
            String uid = HTTPUtil.getParam(request, "uid");
            String logindomain = HTTPUtil.getParam(request, "logindomain");
            String otherid = HTTPUtil.getParam(request, "otherid");
            String token = HTTPUtil.getParam(request, "token");
            String channelid = HTTPUtil.getParam(request, "channelid");
            String clientid = HTTPUtil.getParam(request, "clientid");
            mapMessage.put("token", token);
            mapMessage.put("otherid", otherid);
            mapMessage.put("channelid", channelid);
            mapMessage.put("clientid", clientid);
            mapMessage.put("profileid", profileId);
            mapMessage.put("appkey", appkey);
            mapMessage.put("activityDetailDTO", activityDetailDTO);
            mapMessage.put("uno", uno);
            mapMessage.put("uid", uid);
            mapMessage.put("logindomain", logindomain);
            mapMessage.put("platform", HTTPUtil.getParam(request, "platform"));

            //TODO 加个类？
            if ("1".equals(type)) {
                Long gameDbId = activityDetailDTO.getGameDbId();
                if (gameDbId != 0 && gameDbId != null) {
                    mapMessage.put("gameid", gameDbId);
                    Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
                    String relationId = SocialUtil.getSocialRelationId(profile.getProfileId(), String.valueOf(gameDbId), profile.getProfileKey(), ObjectRelationType.GAME);
                    //判断是否已经关注这个游戏
                    ObjectRelation objectRelation = SocialServiceSngl.get().getObjectRelation(relationId);
                    //如果已经关注该游戏 把gameid清空 则不弹关注框
                    if (objectRelation != null && objectRelation.getStatus().equals(IntValidStatus.VALID)) {
                        mapMessage.put("gameid", "");
                    } else {
                        List<ObjectRelation> list = SocialServiceSngl.get().queryObjectRelations(new QueryExpress()
                                .add(QueryCriterions.eq(ObjectRelationField.PROFILEID, profile.getProfileId()))
                                .add(QueryCriterions.eq(ObjectRelationField.STATUS, IntValidStatus.VALID.getCode()))
                                .add(QueryCriterions.eq(ObjectRelationField.OBJECTTYPE, ObjectRelationType.GAME.getCode())));
                        //关注的游戏超过6个也不弹关注框
                        if (!CollectionUtil.isEmpty(list) && list.size() >= 6) {
                            mapMessage.put("gameid", "");
                        }
                    }
                }
                //判断领取状态
                if (activityDetailDTO.getExchangeTimeType() == 1) {
                    List<UserExchangeLog> userExchangeLogs = PointServiceSngl.get().queryUserExchangeByQueryExpress(new QueryExpress()
                            .add(QueryCriterions.eq(UserExchangeLogField.PROFILEID, profileId))
                            .add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_GOODS_ID, activityDetailDTO.getGid())));
                    if (!CollectionUtil.isEmpty(userExchangeLogs)) {
                        mapMessage.put("code", userExchangeLogs.get(0).getSnValue1());
                        mapMessage.put("times_type", activityDetailDTO.getExchangeTimeType());
                    }
                } else if (activityDetailDTO.getExchangeTimeType() == 2) {
                    SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                    SimpleDateFormat dateformat2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    List<UserExchangeLog> userExchangeLogs = PointServiceSngl.get().queryUserExchangeByQueryExpress(new QueryExpress()
                            .add(QueryCriterions.eq(UserExchangeLogField.PROFILEID, profileId))
                            .add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_GOODS_ID, activityDetailDTO.getGid()))
                            .add(QueryCriterions.gt(UserExchangeLogField.EXCHANGE_TIME, df.parse(dateformat1.format(new Date()))))
                            .add(QueryCriterions.lt(UserExchangeLogField.EXCHANGE_TIME, df.parse(dateformat2.format(new Date())))));
                    if (!CollectionUtil.isEmpty(userExchangeLogs)) {
                        mapMessage.put("code", userExchangeLogs.get(0).getSnValue1());
                        mapMessage.put("times_type", activityDetailDTO.getExchangeTimeType());
                    }
                } else {
                    mapMessage.put("times_type", "");
                }
                return new ModelAndView("/views/jsp/gameclient/webview/giftmarket/giftdetail", mapMessage);
            } else {
                PointwallWall wall = PointServiceSngl.get().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallAppField.APPKEY, appkey)));
                if (wall != null) {
                    QueryExpress queryExpress = new QueryExpress()
                            .add(QueryCriterions.ne(ActivityGoodsField.ACTIVITY_GOODS_ID, Long.parseLong(aid)))
                            .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                            .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                            .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC))
                            .add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, wall.getShopKey()));
                    PageRows<ActivityDTO> goodsActivityPageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, new Pagination(2, 1, 2));
                    mapMessage.put("giftDto", goodsActivityPageRows);
                }
                Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
                UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);
                ActivityGoods activityGoods = new ActivityGoods();
                activityGoods.setTimeType(ConsumeTimesType.getByCode(activityDetailDTO.getExchangeTimeType()));
                activityGoods.setActivityGoodsId(activityDetailDTO.getGid());
                AllowExchangeStatus allowExchangeStatus = giftMarketWebLogic.allowExchangeGoods(activityGoods, profile.getProfileId());
                if (allowExchangeStatus != null) {
                    mapMessage.put("allowExchangeStatus", allowExchangeStatus.getCode());
                }
                if (activityGoods.getTimeType().equals(ConsumeTimesType.ONETIMESADAY)) {
                    mapMessage.put("exchangeStatus", AllowExchangeStatus.HAS_EXCHANGED_BY_DAY.getCode());
                } else if (activityGoods.getTimeType().equals(ConsumeTimesType.ONETIMESMANYDAY)) {
                    mapMessage.put("exchangeStatus", AllowExchangeStatus.HAS_EXCHANGED.getCode());
                }
                mapMessage.put("goodstype", activityDetailDTO.getGoodsType());
                mapMessage.put("userpoint", userPoint.getUserPoint());
                boolean bool = false;
                //TODO delete tomorrow?
                long tomorrow = 60l * 60l * 24l * 1000;
                long endTime = activityDetailDTO.getEndTime().getTime();
                long now = System.currentTimeMillis();
                if (now > endTime) {
                    bool = true;
                }
                mapMessage.put("isBool", bool);
                return new ModelAndView("/views/jsp/gameclient/webview/giftmarket/coindetail", mapMessage);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        }
    }

    @RequestMapping(value = "/pointrule")
    public ModelAndView pointRule() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("wallname", "迷豆");
        return new ModelAndView("/views/jsp/my/pointrule", mapMessage);
    }

    @RequestMapping(value = "/mygift")
    public ModelAndView myGift(HttpServletRequest request,
                               @RequestParam(value = "profileid", required = false) String profileId,
                               @RequestParam(value = "retype", required = false) String retype) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String uno = HTTPUtil.getParam(request, "uno");
            String appkey = HTTPUtil.getParam(request, "appkey");


            appkey = AppUtil.getAppKey(appkey);
            if (StringUtil.isEmpty(profileId)) {
                AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
                if (authApp == null) {
                    mapMessage.put("errorMessage", "authapp.is.null");
                    return new ModelAndView("/views/jsp/my/404", mapMessage);
                }
                Profile profile = UserCenterServiceSngl.get().getProfileByUno(uno, authApp.getProfileKey());
                profileId = profile.getProfileId();
            }
            mapMessage.put("retype", retype);
            mapMessage.put("profileid", profileId);
            mapMessage.put("appkey", appkey);
            String title ="我的礼包";
            if("midou".equals(retype)){
                title="我的订单";
            }
            mapMessage.put("title",title);

            Pagination pagination = new Pagination(8, 1, 8);

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);

            mapMessage.put("uno", profile.getUno());
            long now = System.currentTimeMillis();
            long lastWeek = 60l * 60l * 24l * 1000 * 7;
            long selectDate = now - lastWeek;

            UserCenterSession userSession = new UserCenterSession();
            userSession.setProfileId(profileId);
            PageRows<ActivityMygiftDTO> pageRows = giftMarketWebLogic.queryActivityMygiftDTO(pagination, new Date(selectDate), new Date(now), userSession.getProfileId(), appkey);

            mapMessage.put("giftdto", pageRows);

            PageRows<ActivityDTO> coinActivity = giftMarketWebLogic.queryCoinActivityList(profileId, appkey, 2, pagination);

            mapMessage.put("midoudto", coinActivity);
            return new ModelAndView("/views/jsp/gameclient/webview/giftmarket/mygiftmarketlist", mapMessage);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        }

    }

    @RequestMapping(value = "/mygiftdetail")
    public ModelAndView myGiftDetail(@RequestParam(value = "aid", required = false) String aid,
                                     @RequestParam(value = "profileid", required = false) String profileId,
                                     @RequestParam(value = "appkey", required = false) String appkey,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "consumeorder", required = false) String consumeOrder,
                                     @RequestParam(value = "lid", required = false) String lid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (GoodsActionType.GIFT.getCode() == Integer.parseInt(type)) {
                ActivityDetailDTO activityDetailDTO = giftMarketWebLogic.getActivityGiftDetailDTO(Long.parseLong(aid));
                if (activityDetailDTO != null) {

                    List<UserExchangeLog> userExchangeLog = PointServiceSngl.get().queryUserExchangeByQueryExpress(new QueryExpress().add(QueryCriterions.eq(UserExchangeLogField.USER_EXCHANGE_LOG_ID, Long.parseLong(lid))));
                    mapMessage.put("userExchangeLog", userExchangeLog);
                    if(!CollectionUtil.isEmpty(userExchangeLog)) {
                        activityDetailDTO.setPoint(userExchangeLog.get(0).getExchangePoint());
                    }
                    mapMessage.put("dto", activityDetailDTO);
                    mapMessage.put("activityDetailDTO", activityDetailDTO);
                    return new ModelAndView("/views/jsp/gameclient/webview/giftmarket/mygiftdetail", mapMessage);
                }
            } else {
                ActivityDetailDTO activityDetailDTO = giftMarketWebLogic.getGoodsActivityDetailDTO(Long.parseLong(aid));
                if (activityDetailDTO != null) {
                    Long goodsId = activityDetailDTO.getGid();
                    List<UserConsumeLog> list = PointServiceSngl.get().queryConsumeLogByGoodsId(profileId, goodsId, consumeOrder);
                    if (!CollectionUtil.isEmpty(list)) {
                        List<GoodsItem> goodsItems = new ArrayList<GoodsItem>();
                        Address address = new Address();
                        //TODO 考虑加个方法返回Map<Integer,GoodsItem>
                        for (UserConsumeLog userConsumeLog : list) {
                            if (userConsumeLog.getGoodsItemId() != 0) {
                                GoodsItem goodsItem = PointServiceSngl.get().getGoodsItemById(userConsumeLog.getGoodsItemId());
                                goodsItems.add(goodsItem);
                            }
                            address = userConsumeLog.getAddress();
                            activityDetailDTO.setConsumeOrder(userConsumeLog.getConsumeOrder() + "");
                            activityDetailDTO.setEndTime(userConsumeLog.getConsumeTime());
                            activityDetailDTO.setPoint(userConsumeLog.getConsumeAmount());
                        }
                        mapMessage.put("goodsItem", goodsItems);
                        mapMessage.put("address", address);
                        mapMessage.put("dto", activityDetailDTO);
                    }

                    return new ModelAndView("/views/jsp/gameclient/webview/giftmarket/mycoindetail", mapMessage);
                }

            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        }
//
        return new ModelAndView("/views/jsp/gameclient/webview/giftmarket/mygiftdetail", mapMessage);
    }

}
