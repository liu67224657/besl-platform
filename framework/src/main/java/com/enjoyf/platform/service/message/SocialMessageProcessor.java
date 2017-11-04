package com.enjoyf.platform.service.message;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebApiHotdeployConfig;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.message.APNSUtil;
import javapns.notification.PushNotificationPayload;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-1
 * Time: 下午5:02
 * To change this template use File | Settings | File Templates.
 */
public class SocialMessageProcessor implements MessageProcessor {

    private static Logger logger = LoggerFactory.getLogger(SocialMessageProcessor.class);

    @Override
    public boolean processSendMessage(Map<String, Object> paramMap) {
        Set<String> deviceSet = paramMap.containsKey("deviceset") ? (Set<String>) paramMap.get("deviceset") : null;
        if (CollectionUtil.isEmpty(deviceSet)) {
            return false;
        }
        String body = String.valueOf(paramMap.get("body"));
        int type = paramMap.containsKey("msgtype") ? (Integer) paramMap.get("msgtype") : 0;
        if (type == 0) {
            return false;
        }
        String sound = String.valueOf(paramMap.get("sound"));
        if(StringUtil.isEmpty(sound)){
            sound = "nosound.caf";
        }
        String info = String.valueOf(paramMap.get("info"));
        if(StringUtil.isEmpty(info)){
            info = "";
        }
        GAlerter.lab("-----------------------------------------START LOAD APNS------------------------------------------------");
        int badge = 1;
        List<String> tokens = new ArrayList<String>();
        tokens.addAll(deviceSet);

        PushNotificationPayload payLoad = new PushNotificationPayload();

        try {
            payLoad.addAlert(body); // 消息内容
            payLoad.addBadge(badge); // iphone应用图标上小红圈上的数值
            payLoad.addCustomDictionary("info", info);
            if (!StringUtil.isEmpty(sound)) {
                payLoad.addSound(sound);//铃音
            } else {
                payLoad.addSound("nosound.caf");
            }

            payLoad.addCustomDictionary("msgtype", type);
        } catch (JSONException e) {
            logger.debug("payLoad is error " + e);
        }


        String certificatePath = null;
        String certificatePassword = null;

        certificatePath = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getCertificatePath();
        certificatePassword = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getCertificatePassword();
        boolean isProduction = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).isCertificateProduction();

        if (logger.isDebugEnabled()) {
            logger.debug("push msg to APNS : certificatePath is " + certificatePath);
            logger.debug("certificatePassword is " + certificatePassword);
            logger.debug("isProduction : " + isProduction);
        }
        GAlerter.lab("-----------------------------------------START SEND APNS------------------------------------------------");
        boolean b = APNSUtil.sendToAPNS(tokens, payLoad, certificatePath, certificatePassword, isProduction);

        //失败重新发送一次
        if (!b) {
            APNSUtil.sendToAPNS(tokens, payLoad, certificatePath, certificatePassword, isProduction);
        }
        return b;
    }


}
