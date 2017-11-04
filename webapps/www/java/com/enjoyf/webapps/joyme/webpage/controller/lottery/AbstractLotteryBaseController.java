package com.enjoyf.webapps.joyme.webpage.controller.lottery;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.ShareSyncEvent;
import com.enjoyf.platform.service.event.system.UserPointEvent;
import com.enjoyf.platform.service.lottery.LotteryAward;
import com.enjoyf.platform.service.lottery.LotteryAwardItem;
import com.enjoyf.platform.service.lottery.LotteryAwardType;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-25
 * Time: 上午10:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractLotteryBaseController extends BaseRestSpringController {

    private TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    public void sendOutShareInfo(UserCenterSession userSession, long shareId) {
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

    public void pushNotice(String uno, LotteryAward award, LotteryAwardItem awardItem) {
        try {
            Map<String, String> paramMap = new HashMap<String, String>();
            String noticeTemplate = "";
            if (award != null) {
                String level = "";
                if (award.getLotteryAwardLevel() == 1) {
                    level = "一等奖";
                } else if (award.getLotteryAwardLevel() == 2) {
                    level = "二等奖";
                } else if (award.getLotteryAwardLevel() == 3) {
                    level = "三等奖";
                } else if (award.getLotteryAwardLevel() == 4) {
                    level = "四等奖";
                } else {
                    level = "参与奖";
                }
                paramMap.put("level", level);
                paramMap.put("awardName", award.getLotteryAwardName());
                if (award.getLotteryAwardType().equals(LotteryAwardType.VIRTUAL)) {
                    if (awardItem != null) {
                        paramMap.put("itemCode", awardItem.getValue1());
                        noticeTemplate = templateConfig.getCJ2014LotteryAwardItemTemplate();
                    }
                } else {
                    noticeTemplate = templateConfig.getCJ2014LotteryAwardTemplate();
                }
            }
            if (StringUtil.isEmpty(noticeTemplate)) {
                return;
            }

            String noticeBody = NamedTemplate.parse(noticeTemplate).format(paramMap);

            Message message = new Message();
            message.setBody(noticeBody);
            message.setMsgType(MessageType.OPERATION);
            message.setOwnUno(uno);
            message.setRecieverUno(uno);
            message.setSendDate(new Date());

            MessageServiceSngl.get().postMessage(uno, message);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Excpetion.e", e);
        }
    }

    public void sendOutUserPointIncrease(String uno, long lotteryAwardId, int point) {
        try {
            UserPointEvent event = new UserPointEvent();
            //TODO
            event.setProfileId(uno);
            event.setObjectId("" + lotteryAwardId);
            event.setPoint(point);
            event.setPointActionType(PointActionType.LOTTERY_AWARD);
            event.setDescription("cj2014");
            EventDispatchServiceSngl.get().dispatch(event);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Excpetion.e", e);
        }
    }

}
