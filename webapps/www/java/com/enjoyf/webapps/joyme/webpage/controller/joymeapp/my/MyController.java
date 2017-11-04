package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.my;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.point.pointwall.PointwallWall;
import com.enjoyf.platform.service.point.pointwall.PointwallWallField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Address;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserAccount;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.userprops.UserPropDomain;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserProperty;
import com.enjoyf.platform.service.userprops.UserPropsServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDetailDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.apache.openjpa.lib.conf.StringValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping(value = "/my")
public class MyController extends BaseRestSpringController {
    private int pointValue = 5;//签到基础分
    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            String appkey = HTTPUtil.getParam(request, "appkey");
            String platform = HTTPUtil.getParam(request, "platform");
            String uno = HTTPUtil.getParam(request, "uno");
            String clientId = HTTPUtil.getParam(request, "clientid");

            if (StringUtil.isEmpty(uno)) {
                mapMessage.put("errorMessage", "uno.is.null");
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            if (StringUtil.isEmpty(appkey)) {
                mapMessage.put("errorMessage", "appkey.is.null");
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            if (StringUtil.isEmpty(clientId)) {
                mapMessage.put("errorMessage", "clientid.is.null");
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            if (StringUtil.isEmpty(platform)) {
                mapMessage.put("errorMessage", "platform.is.null");
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            appkey = AppUtil.getAppKey(appkey);
            PointwallWall pointwallWall = PointServiceSngl.get().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallField.APPKEY, appkey)));
            String template = null;
            String shopkey = null;
            if (pointwallWall != null) {
                template = pointwallWall.getTemplate();
                if ("3".equals(pointwallWall.getTemplate())) {
                    return new ModelAndView("redirect:/my/hotapp?appkey=" + appkey + "&uno=" + uno + "&platform=" + platform + "&clientid=" + clientId);
                }
                mapMessage.put("wallname", pointwallWall.getWallMoneyName());
                shopkey = pointwallWall.getShopKey() + "";
            } else {
                mapMessage.put("wallname", "迷豆");
            }
            mapMessage.put("platform", platform);
            mapMessage.put("clientid", clientId);
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                mapMessage.put("errorMessage", "authapp.is.null");
                return new ModelAndView("/views/jsp/my/myindex", mapMessage);
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByUno(uno, authApp.getProfileKey());
            UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);
            int userHasPoint = userPoint == null ? 0 : userPoint.getUserPoint();
            PageRows<ActivityGoods> giftActivityPageRows = PointServiceSngl.get().queryActivityGoodsByPage(new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, GoodsActionType.GIFT.getCode()))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC)), new Pagination(1, 1, 1));
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))

                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));

            if (!StringUtil.isEmpty(shopkey)) {
                queryExpress.add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, Integer.parseInt(shopkey)));
            } else {
                queryExpress.add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, GoodsActionType.MIDOU.getCode()));
            }

            PageRows<ActivityGoods> coinActivityPageRows = PointServiceSngl.get().queryActivityGoodsByPage(queryExpress, new Pagination(1, 1, 1));
            if (giftActivityPageRows != null && !CollectionUtil.isEmpty(giftActivityPageRows.getRows())) {
                mapMessage.put("giftname", giftActivityPageRows.getRows().get(0).getActivitySubject());
            }
            if (coinActivityPageRows != null && !CollectionUtil.isEmpty(coinActivityPageRows.getRows())) {
                mapMessage.put("coinname", coinActivityPageRows.getRows().get(0).getActivitySubject());
            }

            UserPropKey userPropKey = new UserPropKey(UserPropDomain.DEFAULT, profile.getProfileId(), "task.mine.sign");
            UserProperty property = UserPropsServiceSngl.get().getUserProperty(userPropKey);
            if (StringUtil.isEmpty(property.getValue())) {
                mapMessage.put("today", pointValue);
                mapMessage.put("yesterday", "");
                mapMessage.put("tomorrow", pointValue * 2);
            } else {
                int signDay = property.getIntValue();
                Date date = property.getModifyDate();
                Date now = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                //昨天
                int propDay = calendar.get(Calendar.DAY_OF_YEAR);
                calendar.setTime(now);
                //现在
                int nowDay = calendar.get(Calendar.DAY_OF_YEAR);

                if (nowDay - propDay > 1) {
                    signDay = signDay + 1;
                    signDay = signDay % 7;
                    if (signDay == 0) {
                        mapMessage.put("today", 7 * pointValue);
                        mapMessage.put("tomorrow", pointValue);
                    } else {
                        mapMessage.put("today", signDay * pointValue);
                        mapMessage.put("tomorrow", (signDay + 1) * pointValue);
                    }
                    mapMessage.put("yesterday", "");

                } else if (nowDay == propDay) {
                    mapMessage.put("sign", "true");
                    signDay = signDay % 7;
                    if (signDay == 0) {
                        mapMessage.put("today", 7 * pointValue);
                        mapMessage.put("tomorrow", pointValue);
                    } else {
                        mapMessage.put("today", signDay * pointValue);
                        mapMessage.put("tomorrow", (signDay + 1) * pointValue);
                    }
                    //上次签到
                    Date preDate = property.getInitialDate();
                    calendar.setTime(preDate);
                    int preDay = calendar.get(Calendar.DAY_OF_YEAR);
                    if (propDay - preDay > 1 || propDay == preDay) {
                        mapMessage.put("yesterday", "");
                    } else {
                        if (signDay != 0) {
                            if (signDay == 1) {
                                mapMessage.put("yesterday", 7 * pointValue);

                            } else {
                                mapMessage.put("yesterday", (signDay - 1) * pointValue);
                            }
                        } else {
                            mapMessage.put("yesterday", (7 - 1) * pointValue);
                        }
                    }
                } else {
                    signDay = signDay + 1;
                    signDay = signDay % 7;
                    if (signDay == 0) {
                        mapMessage.put("today", 7 * pointValue);
                        mapMessage.put("tomorrow", pointValue);
                        mapMessage.put("yesterday", (7 - 1) * pointValue);
                    } else {
                        mapMessage.put("today", signDay * pointValue);
                        mapMessage.put("tomorrow", (signDay + 1) * pointValue);
                        if (signDay == 1) {
                            mapMessage.put("yesterday", 7 * pointValue);

                        } else {
                            mapMessage.put("yesterday", (signDay - 1) * pointValue);
                        }
                    }
                }
            }
            mapMessage.put("point", userHasPoint);
            mapMessage.put("profile", profile);
            mapMessage.put("appkey", appkey);
            mapMessage.put("uno", uno);
            if (!StringUtil.isEmpty(template)) {
                if ("2".equals(template)) {
                    return new ModelAndView("/views/jsp/my/myindex2", mapMessage);
                }
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
        return new ModelAndView("/views/jsp/my/myindex", mapMessage);
    }

    /*
     积分商城的礼包
     */
    @RequestMapping(value = "/giftlist")
    public ModelAndView giftList(@RequestParam(value = "profileid", required = false) String profileId,
                                 @RequestParam(value = "appkey", required = false) String appkey,
                                 @RequestParam(value = "type", required = false) String type,
                                 @RequestParam(value = "clientid", required = false) String clientId,
                                 @RequestParam(value = "platform", required = false) String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(profileId)) {
                return new ModelAndView("/views/jsp/my/404", mapMessage);
            }
            appkey = AppUtil.getAppKey(appkey);
            PointwallWall pointwallWall = PointServiceSngl.get().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallField.APPKEY, appkey)));
            String shopkey = null;
            if (pointwallWall != null) {
                if (StringUtil.isEmpty(pointwallWall.getWallMoneyName())) {
                    mapMessage.put("wallname", "迷豆");
                } else {
                    mapMessage.put("wallname", pointwallWall.getWallMoneyName());
                }
                shopkey = pointwallWall.getShopKey() + "";
            } else {
                mapMessage.put("wallname", "迷豆");
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);
            int userHasPoint = userPoint == null ? 0 : userPoint.getUserPoint();
            mapMessage.put("point", userHasPoint);
            mapMessage.put("appkey", appkey);
            mapMessage.put("profile", profile);
            mapMessage.put("platform", platform);
            mapMessage.put("clientid", clientId);

            String template = pointwallWall.getTemplate();
            if (StringUtil.isEmpty(template)) {
                template = "";
            }
            mapMessage.put("template", template);

            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
            if (Integer.parseInt(type) == GoodsActionType.GIFT.getCode()) {
                queryExpress.add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, GoodsActionType.GIFT.getCode()));
                List<GiftReserve> list = PointServiceSngl.get().queryGiftReserveByList(new QueryExpress().add(QueryCriterions.eq(GiftReserveField.PROFILEID, profile.getProfileId())).add(QueryCriterions.eq(GiftReserveField.APPKEY, appkey)));
                mapMessage.put("reservelist", list);
                PageRows<ActivityDTO> goodsActivityPageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, new Pagination(10, 1, 10));
                mapMessage.put("giftDto", goodsActivityPageRows);
                return new ModelAndView("/views/jsp/my/giftlist", mapMessage);
            } else {

                if (!StringUtil.isEmpty(shopkey)) {
                    queryExpress.add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, Integer.parseInt(shopkey)));
                } else {
                    queryExpress.add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, GoodsActionType.MIDOU.getCode()));

                }
                PageRows<ActivityDTO> goodsActivityPageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, new Pagination(10, 1, 10));
                mapMessage.put("type", shopkey);
                mapMessage.put("giftDto", goodsActivityPageRows);

                if (template.equals("3")) {
                    return new ModelAndView("/views/jsp/my/coinlist-template3", mapMessage);
                }

                return new ModelAndView("/views/jsp/my/coinlist", mapMessage);

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

    @RequestMapping(value = "/giftdetail")
    public ModelAndView giftDetail(@RequestParam(value = "aid", required = false) String aid,
                                   @RequestParam(value = "profileid", required = false) String profileId,
                                   @RequestParam(value = "appkey", required = false) String appkey,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "platform", required = false) String platform,
                                   @RequestParam(value = "clientid", required = false) String clientId,
                                   @RequestParam(value = "moneyname", required = false) String moneyName,
                                   HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(appkey) || StringUtil.isEmpty(aid)) {
                return new ModelAndView("/views/jsp/my/detail", mapMessage);
            }
            appkey = AppUtil.getAppKey(appkey);
            ActivityDetailDTO activityDetailDTO = giftMarketWebLogic.getGoodsActivityDetailDTO(Long.parseLong(aid));
            if (activityDetailDTO == null) {
                return new ModelAndView("redirect:/my/giftlist?appkey=" + appkey + "&profileid=" + profileId);
            }
            PointwallWall pointwallWall = PointServiceSngl.get().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallField.APPKEY, appkey)));
            String shopkey = "";
            if (pointwallWall != null) {
                shopkey = String.valueOf(pointwallWall.getShopKey());
            }

            if (activityDetailDTO.getReserveType() == 1) {
                List<GiftReserve> list = PointServiceSngl.get().queryGiftReserveByList(new QueryExpress().add(QueryCriterions.eq(GiftReserveField.APPKEY, appkey)).add(QueryCriterions.eq(GiftReserveField.AID, Long.parseLong(aid))));
                mapMessage.put("reserveNum", list.size());
                GiftReserve giftReserve = PointServiceSngl.get().getGiftReserve(new QueryExpress().add(QueryCriterions.eq(GiftReserveField.PROFILEID, profileId))
                        .add(QueryCriterions.eq(GiftReserveField.APPKEY, appkey))
                        .add(QueryCriterions.eq(GiftReserveField.AID, Long.parseLong(aid))));
                mapMessage.put("giftReserve", giftReserve);
            } else {

                QueryExpress queryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC))
                        .add(QueryCriterions.ne(ActivityGoodsField.ACTIVITY_GOODS_ID, Long.parseLong(aid)));

                if (!StringUtil.isEmpty(shopkey)) {
                    queryExpress.add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, Integer.parseInt(shopkey)));
                } else {
                    queryExpress.add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, GoodsActionType.MIDOU.getCode()));

                }
                PageRows<ActivityDTO> goodsActivityPageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, new Pagination(2, 1, 2));
                mapMessage.put("giftDto", goodsActivityPageRows);
            }
            mapMessage.put("clientid", clientId);
            mapMessage.put("platform", platform);
            mapMessage.put("profileid", profileId);
            mapMessage.put("appkey", appkey);
            mapMessage.put("activityDetailDTO", activityDetailDTO);


            String template = pointwallWall.getTemplate();
            if (StringUtil.isEmpty(template)) {
                template = "";
            }
            mapMessage.put("template", template);

            if (StringUtil.isEmpty(moneyName)) {
                mapMessage.put("wallname", "迷豆");
            } else {
                mapMessage.put("wallname", moneyName);
            }
            String sdkVersion = HTTPUtil.getParam(request, "sdkversion");
            if (!StringUtil.isEmpty(sdkVersion)) {
                Integer verint = Integer.valueOf(sdkVersion.replaceAll("\\.", ""));
                if (verint >= 203) {
                    mapMessage.put("sdkversion", "new");
                }
            }
            if ("1".equals(type)) {
                return new ModelAndView("/views/jsp/my/giftdetail", mapMessage);
            } else {
                mapMessage.put("type", type);
                return new ModelAndView("/views/jsp/my/coindetail", mapMessage);
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
    public ModelAndView pointRule(@RequestParam(value = "moneyname", required = false) String moneyName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (StringUtil.isEmpty(moneyName)) {
            mapMessage.put("wallname", "迷豆");
        } else {
            mapMessage.put("wallname", moneyName);
        }
        return new ModelAndView("/views/jsp/my/pointrule", mapMessage);
    }

    @RequestMapping(value = "/mygift")
    public ModelAndView myGift(@RequestParam(value = "profileid", required = false) String profileId,
                               @RequestParam(value = "appkey", required = false) String appkey,
                               @RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "moneyname", required = false) String moneyName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        appkey = AppUtil.getAppKey(appkey);
        mapMessage.put("profileid", profileId);
        mapMessage.put("appkey", appkey);
        mapMessage.put("type", type);
        if (StringUtil.isEmpty(moneyName)) {
            mapMessage.put("wallname", "迷豆");
        } else {
            mapMessage.put("wallname", moneyName);
        }
        try {

            PointwallWall pointwallWall = PointServiceSngl.get().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallField.APPKEY, appkey)));
            String template = pointwallWall.getTemplate();
            if (StringUtil.isEmpty(template)) {
                template = "";
            }
            mapMessage.put("template", template);

            Pagination pagination = new Pagination(10, 1, 10);

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);
            int userHasPoint = userPoint == null ? 0 : userPoint.getUserPoint();
            mapMessage.put("point", userHasPoint);
            mapMessage.put("uno", profile.getUno());

            if (GoodsActionType.GIFT.getCode() == Integer.parseInt(type)) {
                PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryCoinActivityList(profileId, appkey, Integer.parseInt(type), pagination);
                if (pageRows == null) {
                    return new ModelAndView("/views/jsp/my/mygiftlist", mapMessage);
                }
                mapMessage.put("dto", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
                return new ModelAndView("/views/jsp/my/mygiftlist", mapMessage);
            } else {


                PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryCoinActivityList(profileId, appkey, Integer.parseInt(type), pagination);
                if (pageRows == null) {
                    return new ModelAndView("/views/jsp/my/mycoinlist", mapMessage);
                }
                mapMessage.put("dto", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());

                if (template.equals("3")) {
                    return new ModelAndView("/views/jsp/my/mycoinlist-template3", mapMessage);
                }
                return new ModelAndView("/views/jsp/my/mycoinlist", mapMessage);
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

    @RequestMapping(value = "/mygiftdetail")
    public ModelAndView myGiftDetail(@RequestParam(value = "aid", required = false) String aid,
                                     @RequestParam(value = "profileid", required = false) String profileId,
                                     @RequestParam(value = "appkey", required = false) String appkey,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "consumeorder", required = false) String consumeOrder,
                                     @RequestParam(value = "moneyname", required = false) String moneyName,
                                     HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (StringUtil.isEmpty(moneyName)) {
            mapMessage.put("wallname", "迷豆");
        } else {
            mapMessage.put("wallname", moneyName);
        }
        try {
            appkey = AppUtil.getAppKey(appkey);
            String sdkVersion = HTTPUtil.getParam(request, "sdkversion");
            if (!StringUtil.isEmpty(sdkVersion)) {
                Integer verint = Integer.valueOf(sdkVersion.replaceAll("\\.", ""));
                if (verint >= 203) {
                    mapMessage.put("sdkversion", "new");
                }
            }
            if (GoodsActionType.GIFT.getCode() == Integer.parseInt(type)) {
                ActivityDetailDTO activityDetailDTO = giftMarketWebLogic.getGoodsActivityDetailDTO(Long.parseLong(aid));
                if (activityDetailDTO != null) {
                    mapMessage.put("dto", activityDetailDTO);
                    Long goodsId = activityDetailDTO.getGid();
                    List<UserConsumeLog> list = PointServiceSngl.get().queryConsumeLogByGoodsId(profileId, goodsId, "");
                    if (!CollectionUtil.isEmpty(list)) {
                        List<GoodsItem> goodsItems = new ArrayList<GoodsItem>();
                        for (UserConsumeLog userConsumeLog : list) {
                            if (userConsumeLog.getGoodsItemId() != 0) {
                                GoodsItem goodsItem = PointServiceSngl.get().getGoodsItemById(userConsumeLog.getGoodsItemId());
                                goodsItems.add(goodsItem);
                            }
                        }
                        mapMessage.put("goodsItem", goodsItems);
                    }
                    return new ModelAndView("/views/jsp/my/mygiftdetail", mapMessage);
                }
            } else {
                ActivityDetailDTO activityDetailDTO = giftMarketWebLogic.getGoodsActivityDetailDTO(Long.parseLong(aid));
                if (activityDetailDTO != null) {
                    Long goodsId = activityDetailDTO.getGid();
                    List<UserConsumeLog> list = PointServiceSngl.get().queryConsumeLogByGoodsId(profileId, goodsId, consumeOrder);
                    if (!CollectionUtil.isEmpty(list)) {
                        List<GoodsItem> goodsItems = new ArrayList<GoodsItem>();
                        Address address = new Address();
                        for (UserConsumeLog userConsumeLog : list) {
                            if (userConsumeLog.getGoodsItemId() != 0) {
                                GoodsItem goodsItem = PointServiceSngl.get().getGoodsItemById(userConsumeLog.getGoodsItemId());
                                goodsItems.add(goodsItem);
                            }
                            address = userConsumeLog.getAddress();
                            activityDetailDTO.setConsumeOrder(userConsumeLog.getConsumeOrder() + "");
                            activityDetailDTO.setEndTime(userConsumeLog.getConsumeTime());
                        }
                        mapMessage.put("goodsItem", goodsItems);
                        mapMessage.put("address", address);
                        mapMessage.put("dto", activityDetailDTO);
                    }

                    return new ModelAndView("/views/jsp/my/mycoindetail", mapMessage);
                }

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


        return new ModelAndView("/views/jsp/my/mygiftdetail");
    }

    @RequestMapping(value = "/totask")
    public ModelAndView giftDetail(@RequestParam(value = "profileid", required = false) String profileId,
                                   @RequestParam(value = "appkey", required = false) String appkey,
                                   @RequestParam(value = "platform", required = false) String platform,
                                   @RequestParam(value = "clientid", required = false) String clientId,
                                   @RequestParam(value = "moneyname", required = false) String moneyName) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        appkey = AppUtil.getAppKey(appkey);
        mapMessage.put("profileid", profileId);
        mapMessage.put("appkey", appkey);
        mapMessage.put("platform", platform);
        mapMessage.put("clientid", clientId);
        if (StringUtil.isEmpty(moneyName)) {
            mapMessage.put("wallname", "迷豆");
        } else {
            mapMessage.put("wallname", moneyName);
        }

        return new ModelAndView("/views/jsp/my/task", mapMessage);

    }
}
