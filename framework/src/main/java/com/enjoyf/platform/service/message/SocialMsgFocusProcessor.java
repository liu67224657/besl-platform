package com.enjoyf.platform.service.message;

import com.enjoyf.platform.db.message.MessageHandler;
import com.enjoyf.platform.serv.message.SocialMessageCache;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.system.SocialMessageEvent;
import com.enjoyf.platform.service.profile.AllowType;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.socialclient.SocialProfile;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-18
 * Time: 下午3:21
 * To change this template use File | Settings | File Templates.
 */
public class SocialMsgFocusProcessor extends AbstractMessageProcessor implements SocialMsgCatchEventProcessor {


    @Override
    public void catchEvent(SocialMessageEvent catEvent, MessageHandler writeAbleMessageHandler, MessageHandler readonlyMessageHandler, SocialMessageCache socialMessageCache) {
        try {
            SocialNotice receiveNotice = writeAbleMessageHandler.getSocialNotice(catEvent.getOwnUno());
            if (receiveNotice == null) {
                receiveNotice = new SocialNotice();
                receiveNotice.setOwnUno(catEvent.getOwnUno());
                receiveNotice.setDescription(catEvent.getMsgBody());
                receiveNotice.setFocusCount(1);
                receiveNotice.setHotCount(0);
                receiveNotice.setAgreeCount(0);
                receiveNotice.setNoticeCount(0);
                receiveNotice.setReplyCount(0);
                receiveNotice.setRemoveStatus(ActStatus.UNACT);
                receiveNotice.setCreateDate(new Date());
                receiveNotice.setReadFocusDate(new Date());
                writeAbleMessageHandler.insertSocialNotice(receiveNotice);
            } else {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(SocialNoticeField.FOCUS_COUNT, 1);
                if (receiveNotice.getReadFocusDate() == null) {
                    updateExpress.set(SocialNoticeField.READ_FOCUS_DATE, new Date());
                }
                writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
                socialMessageCache.removeSocialNotice(catEvent.getOwnUno());
            }

            SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByUno(catEvent.getOwnUno());

            SocialProfile sendProfile = ProfileServiceSngl.get().getSocialProfileByUno(catEvent.getSendUno());
            if (profile != null && profile.getSetting() != null && AllowType.A_ALLOW.equals(profile.getSetting().getAllowFocusSocial())) {
                int sum = receiveNotice.getFocusCount() + 1;
                String body = "您有" + sum + "个新粉丝~";
                if (sendProfile != null && sendProfile.getBlog() != null) {
                    if (sum == 1) {
                        body = sendProfile.getBlog().getScreenName() + "成为了您的粉丝";
                    } else {
                        body = sendProfile.getBlog().getScreenName() + "等" + sum + "人成为了您的粉丝";
                    }
                }
                String sound = "nosound.caf";
                if (AllowType.A_ALLOW.equals(profile.getSetting().getAllowSoundSocial())) {
                    sound = "default";
                }
                long lastTime = receiveNotice.getReadFocusDate() == null ? 0l : receiveNotice.getReadFocusDate().getTime();
                long nowTime = System.currentTimeMillis();
                //todo
                if (getIntervalHour(lastTime, nowTime) >= 10) {
                    sendSocialMessage(writeAbleMessageHandler, catEvent.getOwnUno(), body, catEvent.getType(), sound);
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(SocialNoticeField.READ_FOCUS_DATE, new Date());
                    writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
    }
}
