package com.enjoyf.webapps.joyme.webpage.controller.point;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.UserPointEvent;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.point.ActionHistoryDTO;
import com.enjoyf.webapps.joyme.dto.point.RankDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pengxu on 2016/11/24.
 */
//
@Deprecated//todo 用戶激励体系删除
@Controller
@RequestMapping(value = "/joyme/api/point")
public class JoymePointController extends BaseRestSpringController {
    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;


    //积分声望上报接口

    /**
     * @param request
     * @param response
     * @param actionType       PointActionType 区分是什么事件
     * @param currentProfileId 当前用户的PID
     * @param reportProfileId  上报用户的PID
     * @param num              次数 //可崇拜多次
     * @return
     */
    @RequestMapping(value = "/report")
    @ResponseBody
    public String report(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "actiontype", required = false) String actionType,
                         @RequestParam(value = "cpid", required = false) String currentProfileId,
                         @RequestParam(value = "rpid", required = false) String reportProfileId,
                         @RequestParam(value = "num", required = false) String num) {
        if (StringUtil.isEmpty(actionType) || StringUtil.isEmpty(reportProfileId)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        int intActionType = -1;
        try {
            intActionType = Integer.parseInt(actionType);
        } catch (NumberFormatException e) {
            return ResultCodeConstants.FAILED.getJsonString();
        }

        PointActionType pointActionType = PointActionType.getByCode(intActionType);
        if (pointActionType == null) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        //todo 如果每个动作一个方法 那就没必要把actionType当参数传过去了
        if (pointActionType.equals(PointActionType.WORSHIP)) {
            if (StringUtil.isEmpty(currentProfileId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            //膜拜                                `
            return pointWebLogic.worshipReport(PointActionType.WORSHIP, currentProfileId, reportProfileId, num);
        } else if (pointActionType.equals(PointActionType.THANKS)) {
            if (StringUtil.isEmpty(currentProfileId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            //感谢
            return pointWebLogic.thanksReport(pointActionType, currentProfileId, reportProfileId);
        } else {
            //积分声望上报
            return pointWebLogic.wikiReport(pointActionType, reportProfileId);
        }
    }

    @RequestMapping(value = "/reducepoint")
    @ResponseBody
    public String reducePoint(@RequestParam(value = "pid", required = false) String profileId,
                              @RequestParam(value = "point", required = false) Integer point,
                              @RequestParam(value = "desc", required = false) String desc) {
        try {
            if (StringUtil.isEmpty(profileId) || StringUtil.isEmpty(desc)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            UserPointEvent userPointEvent = new UserPointEvent();
            userPointEvent.setProfileId(profileId);
            userPointEvent.setDescription(desc);
            userPointEvent.setPoint(-Math.abs(point));
            userPointEvent.setActionDate(new Date());
            userPointEvent.setAppkey(DEFAULT_APPKEY);
            userPointEvent.setPointActionType(PointActionType.ADJUST);
            EventDispatchServiceSngl.get().dispatch(userPointEvent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " reducePoint ServiceException e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " reducePoint Exception e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }


    @RequestMapping(value = "/rank/list")
    @ResponseBody
    public String rankList(@RequestParam(value = "ranktype", required = false) String ranktype,
                           @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                           @RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {

        Pagination pagination = new Pagination(count * page, page, count);
        try {
            Map<String, List<RankDTO>> map = pointWebLogic.queryRankList(ranktype, pagination);
            JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
            jsonObject.put("result", map);
            return jsonObject.toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " rankList ServiceException e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " rankList Exception e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    private static final int BOX_POINT = 500;//宝箱500分
    @RequestMapping(value = "/exchangegiftbox")
    @ResponseBody
    public String getGiftBox(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "num", required = false, defaultValue = "1") Integer num) {
        try {
            UserCenterSession userCenterSession = getUserCenterSeesion(request);
            if (userCenterSession == null) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(userCenterSession.getProfileId());
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            UserPoint userPoint = pointWebLogic.getUserPoint(DEFAULT_APPKEY, profile.getProfileId());
            if (BOX_POINT * num > userPoint.getUserPoint()) {
                return ResultCodeConstants.POINT_NOT_ENOUGH.getJsonString();
            }

            int resultValue = PointServiceSngl.get().exchangeGiftBox(num, profile.getProfileId(), DEFAULT_APPKEY);
            if (resultValue < 1) {
                return ResultCodeConstants.POINT_NOT_ENOUGH.getJsonString();
            } else {
                JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
                jsonObject.put("result", resultValue);
                return jsonObject.toString();
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "getGiftBox  Exception e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @RequestMapping(value = "/opengiftbox")
    @ResponseBody
    public String openGiftBox(HttpServletRequest request, HttpServletResponse response) {
        try {
            UserCenterSession userCenterSession = getUserCenterSeesion(request);
            if (userCenterSession == null) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(userCenterSession.getProfileId());
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            UserLotteryLog userLotteryLog = PointServiceSngl.get().openGiftLottery(profile.getProfileId());

            JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
            jsonObject.put("result", userLotteryLog.getGiftLotteryId());
            return jsonObject.toString();
        } catch (ServiceException e) {
            if (e.equals(PointServiceException.GIFT_LOTTERY_NOT_ENOUGH)) {
                return ResultCodeConstants.GIFT_LOTTERY_NOT_ENOUGH.getJsonString();
            } else if (e.equals(PointServiceException.GIFT_LOTTERY_NOT_EXISTS)) {
                return ResultCodeConstants.GIFT_LOTTERY_NOT_EXISTS.getJsonString();
            } else if (e.equals(PointServiceException.GIFT_LOTTERY_EXIST)) {
                GiftLottery giftLottery = GiftLottery.getByJson(e.getName());
                JSONObject jsonObject = ResultCodeConstants.GIFT_LOTTERY_EXIST.getJsonObject();
                jsonObject.put("result", giftLottery);
                return jsonObject.toString();
            } else {
                GAlerter.lab(this.getClass().getName() + "getGiftBox  Exception e", e);
                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
            }
        }
    }


    @RequestMapping(value = "/choosegift")
    @ResponseBody
    public String chooseGift(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "logid", required = false) Long lotteryId,
                             @RequestParam(value = "flag", required = false) String bool) {
        try {
            UserCenterSession userCenterSession = getUserCenterSeesion(request);
            if (userCenterSession == null) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(userCenterSession.getProfileId());
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            PointServiceSngl.get().chooseLottery(profile.getProfileId(), lotteryId, Boolean.parseBoolean(bool));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "chooseGift  Exception e", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public String queryMyPointList(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "p", required = false, defaultValue = "1") Integer p,
                                   @RequestParam(value = "psize", required = false, defaultValue = "10") Integer psize,
                                   @RequestParam(value = "type", required = false) String type) {
        Map mapMessage = new HashMap();
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null || StringUtil.isEmpty(userSession.getProfileId())) {
            return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
        }
        try {
            String profiileId = userSession.getProfileId();
            UserPoint userPoint = pointWebLogic.getUserPoint(DEFAULT_APPKEY, profiileId);

            Pagination pagination = new Pagination(p * psize, p, psize);
            mapMessage.put("type", type);

            String flag = "increase";
            if (!StringUtil.isEmpty(type)) {
                flag = "reduce";
            }
            PageRows<PointActionHistory> pointActionHistoryPageRows = PointServiceSngl.get().queryMyPointByCache(profiileId, flag, userPoint.getPointKey(), pagination);
            if (pointActionHistoryPageRows != null && !CollectionUtil.isEmpty(pointActionHistoryPageRows.getRows())) {
                Map<String, List<ActionHistoryDTO>> map = pointWebLogic.buildActionHistoryDTO(pointActionHistoryPageRows.getRows());
                mapMessage.put("monthHistoryList", map.get("monthHistoryList"));
                mapMessage.put("allHistoryList", map.get("allHistoryList"));
            } else {
                mapMessage.put("monthHistoryList", "");
                mapMessage.put("allHistoryList", "");
            }
            mapMessage.put("page", pointActionHistoryPageRows.getPage());
            jsonObject.put("result", mapMessage);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return jsonObject.toString();
    }
}
