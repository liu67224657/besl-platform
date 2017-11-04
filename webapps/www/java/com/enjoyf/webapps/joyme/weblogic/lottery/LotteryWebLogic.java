package com.enjoyf.webapps.joyme.weblogic.lottery;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.event.ActivityConstants;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.ShareSyncEvent;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.PointKeyType;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.userprops.UserPropDomain;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserProperty;
import com.enjoyf.platform.service.userprops.UserPropsServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-7-5
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "lotteryWebLogic")
public class LotteryWebLogic {

    private TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    public AllowLotteryStatus allowLottery(long lotteryId, String uno, Date date) throws ServiceException {
        Lottery lottery = LotteryServiceSngl.get().getLotteryById(lotteryId);
        if (lottery == null) {
            return AllowLotteryStatus.NO_ALLOW;
        }
        List<UserLotteryLog> userLotteryLogList = LotteryServiceSngl.get().queryUserLotteryLog(lotteryId, uno);
        UserDayLottery userDayLottery = LotteryServiceSngl.get().getUserDayLottery(lotteryId, uno, date);

        if (LotteryTimesType.ONLY_ONE.equals(lottery.getLotteryTimesType())) {
            if (!CollectionUtil.isEmpty(userLotteryLogList)) {
                return AllowLotteryStatus.HAS_LOTTERY;
            } else {
                return AllowLotteryStatus.ALLOW;
            }
        } else if (LotteryTimesType.BYDAY.equals(lottery.getLotteryTimesType())) {
            if (userDayLottery != null) {
                return AllowLotteryStatus.HAS_LOTTERY_TODAY;
            } else {
                return AllowLotteryStatus.ALLOW;
            }
        } else if (LotteryTimesType.MANY.equals(lottery.getLotteryTimesType())) {
            return AllowLotteryStatus.ALLOW;
        } else if(LotteryTimesType.NEED_CHANCE.equals(lottery.getLotteryTimesType())){
            UserProperty userProperty= UserPropsServiceSngl.get().getUserProperty(new UserPropKey(UserPropDomain.DEFAULT,uno, ActivityConstants.KEY_LOTTERY_ACTIVITY+lotteryId));
            if (userProperty == null) {
                return AllowLotteryStatus.NOT_CHANCE;
            } else {
                return AllowLotteryStatus.ALLOW;
            }
        }

        return AllowLotteryStatus.NO_ALLOW;
    }


    public UserLotteryLog lottery(long lotteryId, UserCenterSession userSession, String ip) throws ServiceException {
        Date lotteryDate = new Date();
        UserLotteryLog userLotteryLog = LotteryServiceSngl.get().userLotteryAward(userSession.getUno(), userSession.getNick(), ip, lotteryDate, lotteryId);

        if (userLotteryLog.getLotteryAwardId() <= 0) {
            return userLotteryLog;
        }

        LotteryAward award = LotteryServiceSngl.get().getLotteryAwardById(userLotteryLog.getLotteryAwardId());
        if (award == null) {
            return userLotteryLog;
        }
        //加积分
        if (award.getLotteryAwardType().equals(LotteryAwardType.POINT)) {
            increasePoint(userSession.getUno(), award, lotteryDate);
        }

        Lottery lottery = LotteryServiceSngl.get().getLotteryById(userLotteryLog.getLotteryId());
        //分享
        if (lottery != null && lottery.getShareId() > 0) {
            sendOutShareInfo(userSession, lottery.getShareId());
        }
        //发送消息
        pushNotice(userLotteryLog, award);
        return userLotteryLog;
    }


    private void pushNotice(UserLotteryLog userLotteryLog, LotteryAward award) {
        try {
            Map<String, String> paramMap = new HashMap<String, String>();

            String noticeTemplate = "";

            String messageBody = "";
            if (award.getLotteryAwardType().equals(LotteryAwardType.GOODS)) {
                paramMap.put("awardname", String.valueOf(userLotteryLog.getLotteryAwardName()));
                noticeTemplate = templateConfig.getLotteryAwardGTemplate();
            } else if (award.getLotteryAwardType().equals(LotteryAwardType.VIRTUAL)) {
                paramMap.put("awardname", String.valueOf(userLotteryLog.getLotteryAwardName()));
                StringBuilder sb = new StringBuilder();
                sb.append(userLotteryLog.getName1()).append("：").append(userLotteryLog.getValue1());
                if (!StringUtil.isEmpty(userLotteryLog.getName2()) && !StringUtil.isEmpty(userLotteryLog.getValue2())) {
                    sb.append(userLotteryLog.getName2()).append("：").append(userLotteryLog.getValue2());
                }
                paramMap.put("key", sb.toString());
                noticeTemplate = templateConfig.getLotteryAwardVTemplate();
            } else if (award.getLotteryAwardType().equals(LotteryAwardType.POINT)) {
                paramMap.put("awardname", String.valueOf(userLotteryLog.getLotteryAwardDesc()));
                noticeTemplate = templateConfig.getLotteryAwardPTemplate();
            }

            messageBody = NamedTemplate.parse(noticeTemplate).format(paramMap);
            Message message = new Message();
            message.setBody(messageBody);
            message.setMsgType(MessageType.OPERATION);
            message.setOwnUno(userLotteryLog.getUno());
            message.setRecieverUno(userLotteryLog.getUno());
            message.setSendDate(new Date());

            MessageServiceSngl.get().postMessage(userLotteryLog.getUno(), message);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
        }
    }

    private boolean increasePoint(String uno, LotteryAward award, Date lotteryDate) {
        boolean returnBool = false;
        try {
            PointActionHistory pointActionHistory = new PointActionHistory();
            pointActionHistory.setActionDate(lotteryDate);
            pointActionHistory.setActionDescription(award.getLotteryAwardName());
            pointActionHistory.setActionType(PointActionType.LOTTERY_AWARD);
            pointActionHistory.setPointValue(Integer.parseInt(award.getLotteryAwardDesc()));
            pointActionHistory.setDestId(String.valueOf(award.getLotteryId()));
            pointActionHistory.setUserNo(uno);
            pointActionHistory.setCreateDate(lotteryDate);

            returnBool = PointServiceSngl.get().increasePointActionHistory(pointActionHistory, PointKeyType.WWW);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " sendMailToFW Exception.e:", e);
        }
        return returnBool;
    }

    private void sendOutShareInfo(UserCenterSession userSession, long shareId) {
        if (userSession.getFlag()==null) {
            return;
        }

//        Set<AccountDomain> domainSet = new HashSet<AccountDomain>();
//        for (AccountDomain accountDomain : userSession.getSyncDomainSet()) {
//            ThirdApiProps thirdApiProps = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getThirdApiPropByAccountDomain(accountDomain);
//            if (thirdApiProps != null && thirdApiProps.isSupportShare()) {
//                domainSet.add(accountDomain);
//            }
//        }

        ShareSyncEvent segEvent = new ShareSyncEvent();
        segEvent.setProfileUno(userSession.getUno());
        segEvent.setShareId(shareId);
        segEvent.setProfileFlag(userSession.getFlag());

        try {
            EventDispatchServiceSngl.get().dispatch(segEvent);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
        }
    }

}
