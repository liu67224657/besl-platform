package com.enjoyf.platform.util.message;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebApiHotdeployConfig;
import com.enjoyf.platform.service.message.ClientDeviceField;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.ClientDevice;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-12
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class APNSUtil {

    private static final int MAX_PAYLOAD_SIZE = 180;
    private static Logger logger = LoggerFactory.getLogger(APNSUtil.class);

    public static boolean sendToAPNS(List<String> tokens, PushNotificationPayload payLoad, String certificatePath, String certificatePassword, boolean isProduction) {
        int successful = 0;
        try {
            logger.info("-----------------------------------------START CONN TO APNS SERVER------------------------------------------------");
            PushNotificationManager pushManager = new PushNotificationManager();
            //true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
            pushManager.initializeConnection(new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, isProduction));
            List<PushedNotification> notifications = new ArrayList<PushedNotification>();

            // 发送push消息
            if (tokens.size() == 1) {

                Device device = new BasicDevice();
                device.setToken(tokens.get(0));

                if (logger.isDebugEnabled()) {
                    logger.debug("deviceToken is " + device.getToken());
                }
                logger.info("-----------------------------------------START SEND NOTIFICATION" + tokens.get(0) + "------------------------------------------------");
                PushedNotification notification = pushManager.sendNotification(device, payLoad, true);
                notifications.add(notification);

            } else {
                List<Device> device = new ArrayList<Device>();
                for (String token : tokens) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("deviceToken is " + token);
                    }

                    device.add(new BasicDevice(token));
                }
                logger.info("-----------------------------------------START SEND NOTIFICATIONS" + tokens.toString() + "------------------------------------------------");
                notifications = pushManager.sendNotifications(payLoad, device);
            }

            List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications);
            List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);

            int failed = failedNotifications.size();
            successful = successfulNotifications.size();
            if (logger.isDebugEnabled()) {
                logger.error("failedNotifications size: " + failed);
                logger.debug("successfulNotifications size: " + successful);
            }
            logger.info("-----------------------------------------SEND NOTIFICATIONS SUCCESS:" + successful + ",FAILED:" + failed + "------------------------------------------------");
            //            pushManager.stopConnection();
        } catch (Exception e) {
            GAlerter.lab("APNSUtil occur Exception.e", e);
        }
        return tokens.size() == successful;
    }

    public static PushNotificationPayload checkPayload(PushNotificationPayload payLoad, String checkStr, int len) throws Exception {
        JSONObject payloadJson = payLoad.getPayload();
        JSONObject apsJson = payloadJson.getJSONObject("aps");
        String alert = String.valueOf(apsJson.get("alert"));

        if (alert != null) {
            byte[] c = checkStr.getBytes("UTF-8");
            byte[] a = alert.getBytes("UTF-8");

            if (c.length + a.length > MAX_PAYLOAD_SIZE - len) {
                String subAlert = new String(a, 0, a.length - (c.length + a.length - MAX_PAYLOAD_SIZE - len) - 1, "UTF-8");
                int length = subAlert.length();
                if (subAlert.charAt(subAlert.length() - 1) != alert.charAt(subAlert.length() - 1)) {
                    subAlert = subAlert.substring(0, length - 1);
                }
                if (subAlert.length() > 4) {
                    subAlert = subAlert.substring(0, subAlert.length() - 1) + "...";
                }
                payLoad.addAlert(subAlert);
            }
        }
        return payLoad;
    }

    public static void main(String[] args) {
//        String ownUno = "e4d9d78b-caf0-49bf-86ab-f518ea3e1941";
//        List<ClientDevice> deviceList = null;
//        try {
//            deviceList = MessageServiceSngl.get().queryClientDevice(new QueryExpress().add(QueryCriterions.eq(ClientDeviceField.UNO, ownUno)).add(QueryCriterions.eq(ClientDeviceField.PLATFORM, 0)));
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
//        Set<String> deviceSet = new HashSet<String>();
//        for (ClientDevice device : deviceList) {
//            deviceSet.add(device.getClientToken());
//        }

        String body = "这个是测试的。";
        int type = 1;
        String sound = "default";

        int badge = 1;
        List<String> tokens = new ArrayList<String>();
        tokens.add("c5d754f42d4eac05c21c8ff30f346773e855dd49ef25b2fc82893f46bf7030ae");

        PushNotificationPayload payLoad = new PushNotificationPayload();

        try {
            payLoad.addAlert(body); // 消息内容
            payLoad.addBadge(badge); // iphone应用图标上小红圈上的数值
//            if (pushMessageOptions != null) {
//                payLoad.addCustomDictionary("option", JsonBinder.buildNormalBinder().toJson(pushMessageOptions));
//            }
            if (!StringUtil.isEmpty(sound)) {
                payLoad.addSound(sound);//铃音
            } else {
                payLoad.addSound("");
            }

            payLoad.addCustomDictionary("jt", 4);
            payLoad.addCustomDictionary("ji", "http:www.baidu.com/");
        } catch (JSONException e) {
            logger.debug("payLoad is error " + e);
        }


        String certificatePath = "/opt/package/ioscertificate/pictorial/alpha/Pictorial_Dev_Certificates_joyme1234.p12";
        String certificatePassword = null;

        certificatePath = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getCertificatePath();
        certificatePassword = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getCertificatePassword();
        boolean isProduction = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).isCertificateProduction();

        if (logger.isDebugEnabled()) {
            logger.debug("push msg to APNS : certificatePath is " + certificatePath);
            logger.debug("certificatePassword is " + certificatePassword);
            logger.debug("isProduction : " + isProduction);
        }

        boolean b = APNSUtil.sendToAPNS(tokens, payLoad, certificatePath, certificatePassword, isProduction);

        //失败重新发送一次
        if (!b) {
            APNSUtil.sendToAPNS(tokens, payLoad, certificatePath, certificatePassword, isProduction);
        }


    }

}
