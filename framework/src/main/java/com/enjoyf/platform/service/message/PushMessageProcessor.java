package com.enjoyf.platform.service.message;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebApiHotdeployConfig;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.message.APNSUtil;
import javapns.notification.PushNotificationPayload;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-1
 * Time: 下午5:02
 * To change this template use File | Settings | File Templates.
 */
public class PushMessageProcessor implements MessageProcessor {

    private static Logger logger = LoggerFactory.getLogger(PushMessageProcessor.class);
    @Override
    public boolean processSendMessage(Map<String, Object> paramMap) {
        PushMsg pushMsg = paramMap.containsKey("pushmsg") ? (PushMsg) paramMap.get("pushmsg") : null;
        if(pushMsg == null){
            return false;
        }
        String deviceToken = pushMsg.getProfileMobileDevice().getMdSerial();//iphone手机获取的token

        // default ..if other modify   (message.pushMsgCode)
        int badge = 1;
        String sound = "default";//铃音

        List<String> tokens = new ArrayList<String>();
        tokens.add(deviceToken);

        PushNotificationPayload payLoad = new PushNotificationPayload();

        try {
            payLoad.addAlert(pushMsg.getPushMessage().getMsgBody()); // 消息内容
            payLoad.addBadge(badge); // iphone应用图标上小红圈上的数值

            if (pushMsg.getPushMessage().getMsgOptions().getCid() != null) {
                payLoad.addCustomDictionary("cid", pushMsg.getPushMessage().getMsgOptions().getCid());
            }
            if (pushMsg.getPushMessage().getMsgOptions().getCuno() != null) {
                payLoad.addCustomDictionary("uno", pushMsg.getPushMessage().getMsgOptions().getCuno());
            }
            if (pushMsg.getPushMessage().getMsgOptions().getDomain() != null) {
                payLoad.addCustomDictionary("dm", pushMsg.getPushMessage().getMsgOptions().getDomain());
            }
            if (pushMsg.getPushMessage().getPushMsgCode() != null) {
                payLoad.addCustomDictionary("mc", pushMsg.getPushMessage().getPushMsgCode().getCode());
            }

            if (!StringUtil.isEmpty(sound)) {
                payLoad.addSound(sound);//铃音
            }
        } catch (JSONException e) {
            logger.debug("payLoad is error " + e);

        }

        String certificatePath = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getCertificatePath();
        String certificatePassword = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getCertificatePassword();
        boolean isProduction = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).isCertificateProduction();
        GAlerter.lab("------------------------------------" + certificatePath + "---------------------------------------");
        GAlerter.lab("------------------------------------" + certificatePassword + "---------------------------------------");
        GAlerter.lab("------------------------------------" + isProduction + "---------------------------------------");

        if (logger.isDebugEnabled()) {
            logger.debug("push msg to APNS : certificatePath is " + certificatePath);
            logger.debug("certificatePassword is " + certificatePassword);
            logger.debug("isProduction : " + certificatePassword);
        }

        boolean b = APNSUtil.sendToAPNS(tokens, payLoad, certificatePath, certificatePassword, isProduction);

        //失败重新发送一次
        if (!b) {
            b = APNSUtil.sendToAPNS(tokens, payLoad, certificatePath, certificatePassword, isProduction);
        }
        return b;
    }
}
