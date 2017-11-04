package com.enjoyf.webapps.joyme.webpage.controller.lottery;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.webapps.joyme.weblogic.lottery.AllowLotteryStatus;
import com.enjoyf.webapps.joyme.weblogic.lottery.LotteryWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-18
 * Time: 下午5:16
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/wangmeng/lottery")
public class WangMengLotteryController extends AbstractLotteryBaseController {

    Logger logger = LoggerFactory.getLogger(WangMengLotteryController.class);

    private static final long WANGMENG_LOTTERY_ID = 10010l;

    @Resource(name = "lotteryWebLogic")
    private LotteryWebLogic lotteryWebLogic;

    @RequestMapping("/page")
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        return new ModelAndView("/views/jsp/lottery/wangmeng/lottery-page", mapMessage);
    }

    @RequestMapping(value = "/{thirdCode}/bind")
    public ModelAndView bind(@PathVariable(value = "thirdCode") String thirdCode,
                             HttpServletRequest request,
                             HttpServletResponse response) {


        Map<String, Object> messageMap = new HashMap<String, Object>();
        AccountDomain accountDomain = AccountDomain.getByCode(thirdCode);
        if (accountDomain == null) {
            return new ModelAndView("/views/jsp/common/custompage", putErrorMessage("sync.auth.error.notsupport"));
        }

        String rurl="/wangmeng/lottery";

        UserSession userSession = getUserBySession(request);
        if (userSession == null) {
            return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl="+rurl+"&icn=true");
        }

        if (!userSession.getSyncDomainSet().contains(accountDomain)) {
            return new ModelAndView("redirect:/profile/sync/" + thirdCode + "/bind?rurl="+rurl+"&icn=true");
        }

        return new ModelAndView("redirect:" + rurl,messageMap);
    }

    @RequestMapping
    public ModelAndView action(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UserCenterSession userSession = getUserCenterSeesion(request);
        if (userSession == null) {
            mapMessage.put("message", "user.not.login");
            return new ModelAndView("/views/jsp/lottery/wangmeng/lottery-failed", mapMessage);
        }

        String uno = userSession.getUno();
        Date lotteryDate = new Date();

        try {
            UserDayLottery userDayLottery = LotteryServiceSngl.get().getUserDayLottery(WANGMENG_LOTTERY_ID, uno, lotteryDate);
            if (userDayLottery != null) {
                mapMessage.put("message", "user.has.lottery.today");
                return new ModelAndView("/views/jsp/lottery/wangmeng/lottery-failed", mapMessage);
            }
            AllowLotteryStatus allowLotteryStatus = lotteryWebLogic.allowLottery(WANGMENG_LOTTERY_ID, uno, lotteryDate);
            if (!AllowLotteryStatus.ALLOW.equals(allowLotteryStatus)) {
                if (AllowLotteryStatus.HAS_LOTTERY_TODAY.equals(allowLotteryStatus)) {
                    mapMessage.put("message", "user.has.lottery.today");
                } else {
                    mapMessage.put("message", "user.has.lottery");
                }
                return new ModelAndView("/views/jsp/lottery/wangmeng/lottery-failed", mapMessage);
            }

            UserLotteryLog userLotteryLog = lotteryWebLogic.lottery(WANGMENG_LOTTERY_ID, userSession, getIp(request));
            mapMessage.put("userLotteryLog", userLotteryLog);

            if (userLotteryLog.getLotteryAwardId() > 0) {
                LotteryAward lotteryAward = LotteryServiceSngl.get().getLotteryAwardById(userLotteryLog.getLotteryAwardId());
                if (LotteryAwardType.POINT.equals(lotteryAward.getLotteryAwardType())) {
                    return new ModelAndView("/views/jsp/lottery/wangmeng/lottery-point", mapMessage);
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
            logger.error("do lottery action occured ServiceException.e:", e);
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e", e);
            return new ModelAndView("/views/jsp/lottery/wangmeng/lottery-failed", mapMessage);
        }
        return new ModelAndView("/views/jsp/lottery/wangmeng/lottery-success", mapMessage);
    }

    //跑马灯iframe
    @RequestMapping("/rewardlist")
    public ModelAndView extAction3(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<UserLotteryLog> userLogList = LotteryServiceSngl.get().queryLastestUserLotteryLog(WANGMENG_LOTTERY_ID);

            List<LotteryAward> lotteryAwardList = LotteryServiceSngl.get().queryLotteryAward(new QueryExpress()
                    .add(QueryCriterions.eq(LotteryAwardField.LOTTERY_ID, WANGMENG_LOTTERY_ID))
                    .add(QueryCriterions.eq(LotteryAwardField.VALID_STATUS, ValidStatus.VALID.getCode()))
            );

            for (UserLotteryLog userLotteryLog : userLogList) {
                userLotteryLog.setScreenName(userLotteryLog.getScreenName().substring(0, 1) + "****");
                int randomNum = (int) ((Math.random() * (lotteryAwardList.size() - 1)) + 1);
                if (randomNum == 1 && randomNum < (lotteryAwardList.size()-1)) {
                    randomNum = randomNum + 1;
                }

                userLotteryLog.setLotteryAwardName(lotteryAwardList.get(randomNum).getLotteryAwardName());
            }

            mapMessage.put("userLogList", userLogList);
        } catch (Exception e) {
            logger.error("rewardlist occured ServiceException.e:", e);
            GAlerter.lab("rewardlist occured ServiceException.e:", e);
        }
        return new ModelAndView("/views/jsp/lottery/wangmeng/awarduserlist", mapMessage);
    }


    @RequestMapping("/extaction5")
    public ModelAndView extAction5(HttpServletRequest request) {
        return null;
    }
}
