package com.enjoyf.webapps.joyme.webpage.controller.lottery;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.event.ActivityConstants;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.userprops.UserPropDomain;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserPropsServiceSngl;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.joyme.weblogic.lottery.AllowLotteryStatus;
import com.enjoyf.webapps.joyme.weblogic.lottery.LotteryWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * User: ericliu
 * Date: 13-7-18
 * Time: 下午5:16
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/lottery")
public class LotteryController extends AbstractLotteryBaseController {

    Logger logger = LoggerFactory.getLogger(LotteryController.class);

    private static final long WANGMENG_LOTTERY_ID = 10010l;

    @Resource(name = "lotteryWebLogic")
    private LotteryWebLogic lotteryWebLogic;


    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        return null;
    }


    @RequestMapping("/page")
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "lid", required = false) Long lid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (lid == null) {
            mapMessage.put("message", "lottery.not.exists");
            return new ModelAndView("/views/jsp/lottery/lottery-failed", mapMessage);
        }
        mapMessage.put("lid", lid);

        return new ModelAndView("/views/jsp/lottery/" + lid + "/lottery-page", mapMessage);
    }

    @RequestMapping
    public ModelAndView action(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "lid", required = false) Long lid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (lid == null) {
            mapMessage.put("message", "lottery.not.exists");
            return new ModelAndView("/views/jsp/lottery/lottery-failed", mapMessage);
        }
        mapMessage.put("lid", lid);

        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null) {
            mapMessage.put("message", "user.not.login");
            return new ModelAndView("/views/jsp/lottery/lottery-failed", mapMessage);
        }

        String uno = userSession.getUno();
        Date lotteryDate = new Date();

        try {
            UserDayLottery userDayLottery = LotteryServiceSngl.get().getUserDayLottery(lid, uno, lotteryDate);
            if (userDayLottery != null) {
                mapMessage.put("message", "user.has.lottery.today");
                return new ModelAndView("/views/jsp/lottery/lottery-failed", mapMessage);
            }

            AllowLotteryStatus allowLotteryStatus = lotteryWebLogic.allowLottery(lid, uno, lotteryDate);
            if (!AllowLotteryStatus.ALLOW.equals(allowLotteryStatus)) {
                if (AllowLotteryStatus.HAS_LOTTERY_TODAY.equals(allowLotteryStatus)) {
                    mapMessage.put("message", "user.has.lottery.today");
                } else if (AllowLotteryStatus.NOT_CHANCE.equals(allowLotteryStatus)) {
                    mapMessage.put("message", "user.has.not.chance");
                } else {
                    mapMessage.put("message", "user.has.lottery");
                }
                return new ModelAndView("/views/jsp/lottery/lottery-failed", mapMessage);
            }

            UserLotteryLog userLotteryLog = lotteryWebLogic.lottery(lid, userSession, getIp(request));
            if(AllowLotteryStatus.NOT_CHANCE.equals(allowLotteryStatus)){
                UserPropsServiceSngl.get().deleteUserProperty(new UserPropKey(UserPropDomain.DEFAULT,uno, ActivityConstants.KEY_LOTTERY_ACTIVITY+lid));
            }

            mapMessage.put("userLotteryLog", userLotteryLog);

            if (userLotteryLog.getLotteryAwardId() > 0) {
                LotteryAward lotteryAward = LotteryServiceSngl.get().getLotteryAwardById(userLotteryLog.getLotteryAwardId());
                if (LotteryAwardType.POINT.equals(lotteryAward.getLotteryAwardType())) {
                    return new ModelAndView("/views/jsp/lottery/lottery-point", mapMessage);
                }
            }
        } catch (ServiceException e) {
            if (e.equals(LotteryServiceException.LOTTERY_AWARD_GET_FAILED)) {
                mapMessage.put("message", "lottery.award.get.failed");
            } else if (e.equals(LotteryServiceException.LOTTERY_AWARD_ITEM_GET_FAILED)) {
                mapMessage.put("message", "lottery.award.item.get.failed");
            } else if (e.equals(LotteryServiceException.LOTTERY_AWARD_NOT_EXISTS)) {
                mapMessage.put("message", "lottery.award.not.exists");
            } else if (e.equals(LotteryServiceException.LOTTERY_AWARD_ITEM_NOT_EXISTS)) {
                mapMessage.put("message", "lottery.award.item.not.exists");
            } else if (e.equals(LotteryServiceException.LOTTERY_AWARD_OUTOF_REST_AMMOUNT)) {
                mapMessage.put("message", "lottery.award.out.rest.amount");
            } else if (e.equals(LotteryServiceException.USER_LOTTERY_LOG_INSERT_FAILD)) {
                mapMessage.put("message", "lottery.not.exists");
            } else if (e.equals(LotteryServiceException.USER_DAY_LOTTERY_INSERT_FAILD)) {
                mapMessage.put("message", "lottery.not.exists");
            } else {
                mapMessage.put("message", "system.error");
            }
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e", e);
            return new ModelAndView("/views/jsp/lottery/lottery-failed", mapMessage);
        }
        return new ModelAndView("/views/jsp/lottery/" + lid + "/lottery-success", mapMessage);
    }

    //bind
    @RequestMapping("/extaction1")
    public ModelAndView extAction1(@RequestParam(value = "thirdCode", required = true) String thirdCode,
                                   @RequestParam(value = "lid", required = false) Long lid,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        Map<String, Object> messageMap = new HashMap<String, Object>();
        AccountDomain accountDomain = AccountDomain.getByCode(thirdCode);
        if (accountDomain == null) {
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error.notsupport"));
        }

        String rurl = "/lottery/extaction2?lid=" + lid;

        UserSession userSession = getUserBySession(request);
        if (userSession == null) {
            return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl=" + rurl + "&icn=true");
        }

        if (!userSession.getSyncDomainSet().contains(accountDomain)) {
            return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl=" + rurl + "&icn=true");
        }

        return new ModelAndView("redirect:/lottery/extaction2?lid=" + lid, messageMap);
    }

    //verify
    @RequestMapping("/extaction2")
    public ModelAndView extAction2(@RequestParam(value = "lid", required = true) Long lid,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

//        try {
        UserSession userSession = getUserBySession(request);
        if (userSession == null) {
            mapMessage.put("message", "user.not.login");
            return new ModelAndView("/views/jsp/lottery/lottery-failed", mapMessage);
        }

        return new ModelAndView("redirect:/lottery?lid=" + lid);
    }

    //跑马灯iframe
    @RequestMapping("/extaction3")
    public ModelAndView extAction3(HttpServletRequest request,
                                   @RequestParam(value = "lid", required = true) Long lotteryId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<UserLotteryLog> userLogList = LotteryServiceSngl.get().queryLastestUserLotteryLog(lotteryId);

            List<LotteryAward> lotteryAwardList = LotteryServiceSngl.get().queryLotteryAward(new QueryExpress()
                    .add(QueryCriterions.eq(LotteryAwardField.LOTTERY_ID, lotteryId))
                    .add(QueryCriterions.eq(LotteryAwardField.VALID_STATUS, ValidStatus.VALID.getCode()))
                    .add(QuerySort.add(LotteryAwardField.LOTTERY_AWARD_LEVEL, QuerySortOrder.ASC))
            );

            for (UserLotteryLog userLotteryLog : userLogList) {
                userLotteryLog.setScreenName(userLotteryLog.getScreenName().substring(0, 1) + "****");
                int awardLevel = getAwardLevelByRandom();
                if (awardLevel >= lotteryAwardList.size()) {
                    awardLevel = lotteryAwardList.size() - 1;
                }

                userLotteryLog.setLotteryAwardName(lotteryAwardList.get(awardLevel).getLotteryAwardName());
            }

            mapMessage.put("userLogList", userLogList);
        } catch (Exception e) {
            GAlerter.lab("bind occured ServiceException.e:", e);
        }
        return new ModelAndView("/views/jsp/lottery/awarduserlist", mapMessage);
    }


    @RequestMapping("/extaction4")
    public ModelAndView extAction4(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("lid", WANGMENG_LOTTERY_ID);
        return new ModelAndView("redirect:/wangmeng/lottery/page", mapMessage);
    }

    @RequestMapping("/extaction5")
    public ModelAndView extAction5(HttpServletRequest request) {
        return null;
    }


    private int getAwardLevelByRandom() {
        int randomNum = (int) (Math.random() * 500);

        if (randomNum <= 10) {
            return 0;
        } else if (randomNum <= 30) {
            return 1;
        } else if (randomNum <= 60) {
            return 2;
        } else if (randomNum <= 60) {
            return 3;
        } else {
            return 4;
        }
    }
}
