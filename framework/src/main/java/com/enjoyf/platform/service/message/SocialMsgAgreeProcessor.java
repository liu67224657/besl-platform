package com.enjoyf.platform.service.message;

import com.enjoyf.platform.db.message.MessageHandler;
import com.enjoyf.platform.serv.message.SocialMessageCache;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.system.SocialMessageEvent;
import com.enjoyf.platform.service.profile.AllowType;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.ProfileSumField;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-18
 * Time: 下午3:21
 * To change this template use File | Settings | File Templates.
 */
public class SocialMsgAgreeProcessor extends AbstractMessageProcessor implements SocialMsgCatchEventProcessor {


    @Override
    public void catchEvent(SocialMessageEvent catEvent, MessageHandler writeAbleMessageHandler, MessageHandler readonlyMessageHandler, SocialMessageCache socialMessageCache) {
        try {
            SocialMessage socialMessage = new SocialMessage();
            socialMessage.setMsgBody(catEvent.getMsgBody());
            socialMessage.setMsgType(SocialMessageType.getByCode(catEvent.getType()));
            socialMessage.setMsgCategory(SocialMessageCategory.DEFAULT_MSG);

            socialMessage.setOwnUno(catEvent.getOwnUno());
            socialMessage.setSendUno(catEvent.getSendUno());
            socialMessage.setReceiveUno(catEvent.getReceiveUno());

            socialMessage.setReplyId(catEvent.getReplyId());
            socialMessage.setReplyUno(catEvent.getReplyUno());
            socialMessage.setContentId(catEvent.getContentId());
            socialMessage.setContentUno(catEvent.getContentUno());
            socialMessage.setParentId(catEvent.getParentId());
            socialMessage.setParentUno(catEvent.getParentUno());
            socialMessage.setRootId(catEvent.getRootId());
            socialMessage.setRootUno(catEvent.getRootUno());

            socialMessage.setCreateDate(catEvent.getCreateDate());
            socialMessage.setRemoveStatus(ActStatus.UNACT);
            writeAbleMessageHandler.insertSocialMessage(socialMessage);

            Map<ObjectField, Object> paramMap = new HashMap<ObjectField, Object>();
            paramMap.put(ProfileSumField.SOCIALAGREEMSGSUM, 1);
            ProfileServiceSngl.get().increaseProfileSum(catEvent.getOwnUno(), paramMap);

            SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByUno(catEvent.getOwnUno());
            if (profile == null || profile.getBlog() == null) {
                return;
            }
            SocialProfile sendProfile = ProfileServiceSngl.get().getSocialProfileByUno(catEvent.getSendUno());
            if (sendProfile == null || sendProfile.getBlog() == null) {
                return;
            }

            int totals = profile.getSum() == null ? 0 : profile.getSum().getSocialAgreeMsgSum();

            socialMessageCache.removeSocialMessageIdList(catEvent.getOwnUno(), catEvent.getType(), totals - 1);

            SocialNotice receiveNotice = readonlyMessageHandler.getSocialNotice(catEvent.getOwnUno());
            if (receiveNotice == null) {
                receiveNotice = new SocialNotice();
                receiveNotice.setOwnUno(catEvent.getOwnUno());
                receiveNotice.setDescription(catEvent.getMsgBody());
                receiveNotice.setAgreeCount(1);
                receiveNotice.setReplyCount(0);
                receiveNotice.setHotCount(0);
                receiveNotice.setFocusCount(0);
                receiveNotice.setNoticeCount(0);
                receiveNotice.setRemoveStatus(ActStatus.UNACT);
                receiveNotice.setCreateDate(new Date());
                receiveNotice.setReadAgreeDate(new Date());
                writeAbleMessageHandler.insertSocialNotice(receiveNotice);
            } else {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(SocialNoticeField.AGREE_COUNT, 1);
                if (receiveNotice.getReadAgreeDate() == null) {
                    updateExpress.set(SocialNoticeField.READ_AGREE_DATE, new Date());
                }
                writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
                socialMessageCache.removeSocialNotice(catEvent.getOwnUno());
            }

            if (profile != null && profile.getSetting() != null && AllowType.A_ALLOW.equals(profile.getSetting().getAllowAgreeSocial())) {
                long lastTime = 0l;
                int sum = receiveNotice.getAgreeCount() + 1;
                String body = "您有" + sum + "条新赞~";
                if (sendProfile != null && sendProfile.getBlog() != null) {
                    if (sum == 1) {
                        body = sendProfile.getBlog().getScreenName() + "赞了您的文章";
                    } else {
                        body = sendProfile.getBlog().getScreenName() + "等" + sum + "人赞了您的文章";
                    }

                }

                String sound = "nosound.caf";
                if (AllowType.A_ALLOW.equals(profile.getSetting().getAllowSoundSocial())) {
                    sound = "default";
                }
                lastTime = receiveNotice.getReadAgreeDate() == null ? 0l : receiveNotice.getReadAgreeDate().getTime();
                long nowTime = System.currentTimeMillis();
                if (getIntervalHour(lastTime, nowTime) >= 10) {
                    sendSocialMessage(readonlyMessageHandler, catEvent.getOwnUno(), body, catEvent.getType(), sound);
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
    }
}
