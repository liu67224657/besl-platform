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
 * Time: 下午3:19
 * To change this template use File | Settings | File Templates.
 */
public class SocialMsgHotProcessor extends AbstractMessageProcessor implements SocialMsgCatchEventProcessor {

    @Override
    public void catchEvent(SocialMessageEvent catEvent, MessageHandler writeAbleMessageHandler, MessageHandler readonlyMessageHandler, SocialMessageCache socialMessageCache) {
        try {
            SocialNotice receiveNotice = writeAbleMessageHandler.getSocialNotice(catEvent.getOwnUno());
            int sum = 0;
            if (receiveNotice == null) {
                sum = 1;
                receiveNotice = new SocialNotice();
                receiveNotice.setOwnUno(catEvent.getOwnUno());
                receiveNotice.setDescription(catEvent.getMsgBody());
                receiveNotice.setHotCount(1);
                receiveNotice.setAgreeCount(0);
                receiveNotice.setNoticeCount(0);
                receiveNotice.setFocusCount(0);
                receiveNotice.setReplyCount(0);
                receiveNotice.setRemoveStatus(ActStatus.UNACT);
                receiveNotice.setCreateDate(new Date());
                receiveNotice.setReadHotDate(new Date());
                writeAbleMessageHandler.insertSocialNotice(receiveNotice);
            } else {
                sum = receiveNotice.getHotCount() + 1;
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(SocialNoticeField.HOT_COUNT, 1);
                if (receiveNotice.getReadHotDate() == null) {
                    updateExpress.set(SocialNoticeField.READ_HOT_DATE, new Date());
                }
                writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
                socialMessageCache.removeSocialNotice(catEvent.getOwnUno());
            }
            SocialProfile profile = ProfileServiceSngl.get().getSocialProfileByUno(catEvent.getOwnUno());
            String sound = "nosound.caf";
            if (profile != null && profile.getSetting() != null && AllowType.A_ALLOW.equals(profile.getSetting().getAllowSoundSocial())) {
                sound = "default";
            }

            String body = "您有1篇文章被推荐到了咔哒热门频道，快来看看吧";
            if (sum > 0) {
                body = "您有" + sum + "篇文章被推荐到了咔哒热门频道，快来看看吧";
            }

            long lastTime = receiveNotice.getReadHotDate() == null ? 0l : receiveNotice.getReadHotDate().getTime();
            long nowTime = System.currentTimeMillis();
            //todo
            if (getIntervalHour(lastTime, nowTime) >= 10) {
                sendSocialMessage(writeAbleMessageHandler, catEvent.getOwnUno(), body, catEvent.getType(), sound);
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(SocialNoticeField.READ_HOT_DATE, new Date());
                writeAbleMessageHandler.updateSocialNotice(catEvent.getOwnUno(), updateExpress);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }

    }
}
