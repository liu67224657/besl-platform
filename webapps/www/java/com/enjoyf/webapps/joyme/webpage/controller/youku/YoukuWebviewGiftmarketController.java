package com.enjoyf.webapps.joyme.webpage.controller.youku;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.IntRemoveStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.task.Task;
import com.enjoyf.platform.service.event.task.TaskLog;
import com.enjoyf.platform.service.event.task.TaskUtil;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.JoymeAppTopMenu;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.point.pointwall.PointwallWall;
import com.enjoyf.platform.service.point.pointwall.PointwallWallAppField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Address;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDetailDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.AllowExchangeStatus;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
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
 * Date: 15-5-26
 * Time: 上午11:58
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/youku/webview/giftmarket")
public class YoukuWebviewGiftmarketController extends AbstractYoukuController {
    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;

    private static final int PLATFORM = 0;

    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
//            //=======
            mapMessage.put("appkey", APPKEY);
            mapMessage.put("platform", 0);
            List<JoymeAppTopMenu> joymeAppMenuList = JoymeAppConfigServiceSngl.get().queryJoymeAppTopMenuByAppKey(APPKEY);
            List<JoymeAppTopMenu> returnAppMenuList = new ArrayList<JoymeAppTopMenu>();
            for (JoymeAppTopMenu joymeAppTopMenu : joymeAppMenuList) {
                joymeAppTopMenu.setPicUrl1(URLUtils.getJoymeDnUrl(joymeAppTopMenu.getPicUrl1()));
                joymeAppTopMenu.setPicUrl(URLUtils.getJoymeDnUrl(joymeAppTopMenu.getPicUrl()));
                joymeAppTopMenu.setPicUrl2(URLUtils.getJoymeDnUrl(joymeAppTopMenu.getPicUrl2()));
                returnAppMenuList.add(joymeAppTopMenu);
            }
            mapMessage.put("menulist", returnAppMenuList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        }
        return new ModelAndView("/views/jsp/youku/youkugiftmarket", mapMessage);
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detial(HttpServletRequest request, @RequestParam(value = "aid", required = false) String agid,
                               @RequestParam(value = "secid", required = false) String secId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(agid)) {
                return new ModelAndView("redirect:/youku/webview/giftmarket/list");
            }
            long aid = 0l;
            try {
                aid = Long.valueOf(agid);
            } catch (NumberFormatException e) {
            }
            if (aid <= 0l) {
                return new ModelAndView("redirect:/youku/webview/giftmarket/list");
            }

            ActivityDetailDTO activityDetailDTO = giftMarketWebLogic.getGoodsActivityDetailDTO(aid);
            if (activityDetailDTO == null) {
                return new ModelAndView("redirect:/youku/webview/giftmarket/list");
            }
            Date now = new Date();
            long startTime = activityDetailDTO.getStartTime().getTime();
            long endTime = activityDetailDTO.getEndTime().getTime();
            //秒杀进来的  秒杀ID不空
            if (ChooseType.YES.getCode() == activityDetailDTO.getSeckillType() && !StringUtil.isEmpty(secId)) {
                long seckillId = 0l;
                try {
                    seckillId = Long.valueOf(secId);
                } catch (NumberFormatException e) {
                }
                GoodsSeckill goodsSeckill = PointServiceSngl.get().getGoodsSeckillById(seckillId);
                if (goodsSeckill == null || !goodsSeckill.getGoodsId().equals(agid) || !goodsSeckill.getRemoveStatus().equals(IntRemoveStatus.USED)) {
                    return new ModelAndView("redirect:/youku/webview/giftmarket/list");
                }
                startTime = goodsSeckill.getStartTime().getTime();
                endTime = goodsSeckill.getEndTime().getTime();

                mapMessage.put("goodsSecKill", giftMarketWebLogic.buildGoodsSeckillDTO(goodsSeckill, null));
                mapMessage.put("currentTime", System.currentTimeMillis());
            }

            String profileId = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_PROFILEID);
            if (!StringUtil.isEmpty(profileId)) {
                Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
                UserPoint userPoint = giftMarketWebLogic.getUserPoint(APPKEY, profile);

                ActivityGoods activityGoods = new ActivityGoods();
                activityGoods.setTimeType(ConsumeTimesType.getByCode(activityDetailDTO.getExchangeTimeType()));
                activityGoods.setActivityGoodsId(activityDetailDTO.getGid());

                AllowExchangeStatus allowExchangeStatus = giftMarketWebLogic.allowExchangeGoods(activityGoods, profile.getProfileId());
                if (allowExchangeStatus != null) {
                    mapMessage.put("allowExchangeStatus", allowExchangeStatus.getCode());
                }
                if (ConsumeTimesType.ONETIMESADAY.equals(activityGoods.getTimeType())) {
                    mapMessage.put("exchangeStatus", AllowExchangeStatus.HAS_EXCHANGED_BY_DAY.getCode());
                } else if (ConsumeTimesType.ONETIMESMANYDAY.equals(activityGoods.getTimeType())) {
                    mapMessage.put("exchangeStatus", AllowExchangeStatus.HAS_EXCHANGED.getCode());
                }
                mapMessage.put("userpoint", userPoint.getUserPoint());
                mapMessage.put("goodstype", activityDetailDTO.getGoodsType());

                mapMessage.put("profile", profile);
            }
            mapMessage.put("activityDetailDTO", activityDetailDTO);
            mapMessage.put("appkey", APPKEY);


            PointwallWall wall = PointServiceSngl.get().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallAppField.APPKEY, APPKEY)));
            if (wall != null) {
                QueryExpress queryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(ActivityGoodsField.SECKILL_TYPE, 0))
                        .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.GOODS.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.GOODS_ACTION_TYPE, wall.getShopKey()))
                        .add(QueryCriterions.ne(ActivityGoodsField.ACTIVITY_GOODS_ID, aid))
                        .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));

                PageRows<ActivityDTO> goodsActivityPageRows = giftMarketWebLogic.queryActivityDTOs(queryExpress, new Pagination(2, 1, 2));
                mapMessage.put("giftDto", goodsActivityPageRows);
            }
            //结束时间
            mapMessage.put("endTime", now.getTime() > endTime ? true : false);
            //开始时间
            mapMessage.put("startTime", now.getTime() > startTime ? true : false);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        }
        return new ModelAndView("/views/jsp/youku/youkugiftdetail", mapMessage);
    }

    @RequestMapping(value = "/mygift")
    public ModelAndView myGift(@RequestParam(value = "profileid", required = false) String profileId,
                               @RequestParam(value = "appkey", required = false) String appkey,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "type", required = false, defaultValue = "2") String type,
                               HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userCenterSession = getUserCenterSeesion(request);
            if (userCenterSession == null) {
//                String reUrl = request.getRequestURI();
//                String domain = WebappConfig.get().getDomain();
//                return new ModelAndView("redirect:/mlogin?reurl=" + "http://api." + domain + reUrl);
                return new ModelAndView("/views/jsp/common/custompage-wap", putErrorMessage("user.profile.notexists"));
            }
            AppPlatform appPlatform = null;
            try {
                appPlatform = AppPlatform.getByCode(PLATFORM);
            } catch (Exception e) {
            }
            String clientId = HTTPUtil.getParam(request, "clientid"); //用于任务系统
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(userCenterSession.getProfileId());
            appkey = AppUtil.getAppKey(APPKEY);
            mapMessage.put("profileid", profile.getProfileId());
            mapMessage.put("appkey", appkey);
            mapMessage.put("platform", 0);
            mapMessage.put("type", type);


            // PointwallWall pointwallWall = PointServiceSngl.get().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallField.APPKEY, appkey)));

            Pagination pagination = new Pagination(8, 1, 8);

            UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);
            int userHasPoint = userPoint == null ? 0 : userPoint.getUserPoint();
            mapMessage.put("point", userHasPoint);
            mapMessage.put("profile", profile);

            List<ActivityDTO> list = giftMarketWebLogic.queryCoinActivityListNoPage(profile.getProfileId(), appkey, Integer.parseInt(type));
            mapMessage.put("dto", list);

            String groupId = TaskUtil.getTaskGroupId(PREFIX_YOUKU_SIGN_, appPlatform);
            List<Task> tasks = EventServiceSngl.get().getTaskGroupList(groupId);
            if (CollectionUtil.isEmpty(tasks)) {
                mapMessage.put("signcomplete", false);
                mapMessage.put("signnum", 0);        //总签到次数
                return new ModelAndView("/views/jsp/youku/youku-mygift", mapMessage);
            }
            Task FirstTask = tasks.get(0);
            int taskVerifyId = FirstTask.getTaskVerifyId();
            String userId = getIdBytaskVerifyId(taskVerifyId, profile.getProfileId(), clientId);
            if (StringUtil.isEmpty(userId)) {
                return new ModelAndView("/views/jsp/common/custompage-wap", putErrorMessage("param.empty"));
            }
            TaskLog taskLog = EventServiceSngl.get().getTaskLogByGroupIdProfileId(FirstTask.getTaskGroupId(), userId, new Date());
            int signNum = EventServiceSngl.get().querySignSumByProfileIdGroupId(userId, FirstTask.getTaskGroupId());
            mapMessage.put("signcomplete", taskLog != null);
            mapMessage.put("signnum", signNum);        //总签到次数
//            PageRows<ActivityDTO> pageRows = giftMarketWebLogic.queryCoinActivityListByPage(profile.getProfileId(), appkey, Integer.parseInt(type), pagination);
//            if (pageRows == null) {
//                return new ModelAndView("/views/jsp/youku/youku-mygift", mapMessage);
//            }


//            mapMessage.put("page", pageRows.getPage());


            return new ModelAndView("/views/jsp/youku/youku-mygift", mapMessage);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        }

    }

    @RequestMapping(value = "/mygiftdetail")
    public ModelAndView myGiftDetail(@RequestParam(value = "aid", required = false) String aid,
                                     @RequestParam(value = "consumeorder", required = false) String consumeOrder,
                                     HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            UserCenterSession userCenterSession = getUserCenterSeesion(request);
            if (userCenterSession == null) {
//                String reUrl = request.getRequestURI();
//                String domain = WebappConfig.get().getDomain();
//                return new ModelAndView("redirect:/mlogin?reurl=" + "http://api." + domain + reUrl);
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(userCenterSession.getProfileId());
            String appkey = AppUtil.getAppKey(APPKEY);
            mapMessage.put("profileid", profile.getProfileId());
            mapMessage.put("appkey", appkey);
            mapMessage.put("platform", 0);


            ActivityDetailDTO activityDetailDTO = giftMarketWebLogic.getGoodsActivityDetailDTO(Long.parseLong(aid));
            if (activityDetailDTO != null) {
                Long goodsId = activityDetailDTO.getGid();
                List<UserConsumeLog> list = PointServiceSngl.get().queryConsumeLogByGoodsId(profile.getProfileId(), goodsId, consumeOrder);
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
            }

            return new ModelAndView("/views/jsp/youku/mycoindetail", mapMessage);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/my/404", mapMessage);
        }

    }

    @RequestMapping(value = "/novice")
    public ModelAndView novice() {

        return new ModelAndView("/views/jsp/youku/novice");
    }

}
