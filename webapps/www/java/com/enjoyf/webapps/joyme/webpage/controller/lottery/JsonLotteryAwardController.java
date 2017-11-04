package com.enjoyf.webapps.joyme.webpage.controller.lottery;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.webapps.joyme.dto.lottery.LotterySimpleDTO;
import com.enjoyf.webapps.joyme.weblogic.lottery.AllowLotteryStatus;
import com.enjoyf.webapps.joyme.weblogic.lottery.LotteryWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
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
 * User: zhitaoshi
 * Date: 13-6-25
 * Time: 上午10:26
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/json/huodong/chinajoy2013conference")
public class JsonLotteryAwardController extends AbstractLotteryBaseController {

    private JsonBinder jsonBinder = JsonBinder.buildNormalBinder();

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    @Resource(name = "lotteryWebLogic")
    private LotteryWebLogic lotteryWebLogic;

    @ResponseBody
    @RequestMapping
    public String lottery(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "lid") Long lotteryId) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);

        UserCenterSession userSession = getUserCenterSeesion(request);
        String uno = userSession.getUno();
        String screenName=userSession.getNick();
        Date lotteryDate = new Date();

        try {
            AllowLotteryStatus allowLotteryStatus = lotteryWebLogic.allowLottery(lotteryId, uno, lotteryDate);
            if (!AllowLotteryStatus.ALLOW.equals(allowLotteryStatus)) {
                if (AllowLotteryStatus.HAS_LOTTERY_TODAY.equals(allowLotteryStatus)) {
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    resultMsg.setMsg(i18nSource.getMessage("user.has.lottery.today", null, Locale.CHINA));
                } else {
                    resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
                    resultMsg.setMsg(i18nSource.getMessage("user.has.lottery", null, Locale.CHINA));
                }
                return jsonBinder.toJson(resultMsg);
            }

            UserLotteryLog userLotteryLog = LotteryServiceSngl.get().userLotteryAward(uno, screenName, getIp(request), lotteryDate, lotteryId);

            sendOutShareInfo(userSession, lotteryId);

            Lottery lottery = null;
            LotteryAward award = null;
            LotteryAwardItem awardItem = null;
            if(userLotteryLog.getLotteryId()>0){
                lottery = LotteryServiceSngl.get().getLotteryById(lotteryId);
            }
            if(userLotteryLog.getLotteryAwardId()>0){
                award = LotteryServiceSngl.get().getLotteryAwardById(userLotteryLog.getLotteryAwardId());
            }
            if(userLotteryLog.getLotteryAwardItemId()>0){
                awardItem = LotteryServiceSngl.get().getLotteryAwardItemById(userLotteryLog.getLotteryAwardItemId());
            }

            resultMsg.setResult(new ArrayList<LotterySimpleDTO>());
            resultMsg.getResult().add(getDTO(lottery, award, awardItem));
        } catch (ServiceException e) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);

            if (e.equals(LotteryServiceException.LOTTERY_AWARD_GET_FAILED)) {
                resultMsg.setMsg(i18nSource.getMessage("lottery.award.get.failed", null, Locale.CHINA));
            } else if (e.equals(LotteryServiceException.LOTTERY_AWARD_ITEM_GET_FAILED)) {
                resultMsg.setMsg(i18nSource.getMessage("lottery.award.item.get.failed", null, Locale.CHINA));
            } else if (e.equals(LotteryServiceException.LOTTERY_AWARD_NOT_EXISTS)) {
                resultMsg.setMsg(i18nSource.getMessage("lottery.award.not.exists", null, Locale.CHINA));
            } else if (e.equals(LotteryServiceException.LOTTERY_AWARD_ITEM_NOT_EXISTS)) {
                resultMsg.setMsg(i18nSource.getMessage("lottery.award.item.not.exists", null, Locale.CHINA));
            } else if (e.equals(LotteryServiceException.LOTTERY_AWARD_OUTOF_REST_AMMOUNT)) {
                resultMsg.setMsg(i18nSource.getMessage("lottery.award.out.rest.amount", null, Locale.CHINA));
            } else if (e.equals(LotteryServiceException.USER_DAY_LOTTERY_INSERT_FAILD)) {
                resultMsg.setMsg(i18nSource.getMessage("usr.day.lottery.insert.failed", null, Locale.CHINA));
            } else if (e.equals(LotteryServiceException.USER_LOTTERY_LOG_INSERT_FAILD)) {
                resultMsg.setMsg(i18nSource.getMessage("usr.lottery.log.insert.failed", null, Locale.CHINA));
            } else {
                resultMsg.setMsg(i18nSource.getMessage("system.error", null, Locale.CHINA));
                GAlerter.lab(this.getClass().getName() + " occured ServicdeExcpetion.e", e);
            }
            return jsonBinder.toJson(resultMsg);
        }

        return jsonBinder.toJson(resultMsg);
    }

//    private void sendOutShareInfo(UserSession userSession, long shareId) {
//        if (CollectionUtil.isEmpty(userSession.getSyncDomainSet())) {
//            return;
//        }
//
//        Set<AccountDomain> domainSet = new HashSet<AccountDomain>();
//        for (AccountDomain accountDomain : userSession.getSyncDomainSet()) {
//            ThirdApiProps thirdApiProps = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getThirdApiPropByAccountDomain(accountDomain);
//            if (thirdApiProps != null && thirdApiProps.isSupportShare()) {
//                domainSet.add(accountDomain);
//            }
//        }
//
//        ShareSyncEvent segEvent = new ShareSyncEvent();
//        segEvent.setProfileUno(userSession.getBlogwebsite().getUno());
//        segEvent.setShareId(shareId);
//        segEvent.setAccountDomainSet(domainSet);
//
//        try {
//            EventDispatchServiceSngl.get().dispatch(segEvent);
//        } catch (Exception e) {
//            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
//        }
//    }


    private LotterySimpleDTO getDTO(Lottery lottery, LotteryAward award, LotteryAwardItem awardItem) {
        LotterySimpleDTO dto = new LotterySimpleDTO();
        dto.setLotteryId(lottery.getLotteryId());
        dto.setLotteryName(lottery.getLotteryName());
        dto.setLotteryDesc(lottery.getLotteryDesc());
        dto.setLotteryTimesType(lottery.getLotteryTimesType().getCode());

        if (award != null) {
            dto.setAwardId(award.getLotteryAwardId());
            dto.setAwardName(award.getLotteryAwardName());
            dto.setAwardDesc(award.getLotteryAwardDesc());
            dto.setAwardLevel(award.getLotteryAwardLevel());
            dto.setAwardPic(award.getLotteryAwardPic());
            dto.setAwardType(award.getLotteryAwardType().getCode());
        }
        if (awardItem != null) {
            dto.setAwardItemId(awardItem.getLotteryAwardItemId());
            dto.setItemName1(awardItem.getName1());
            dto.setItemValue1(awardItem.getValue1());
            dto.setItemName2(awardItem.getName2());
            dto.setItemValue2(awardItem.getValue2());
        }
        return dto;
    }

}