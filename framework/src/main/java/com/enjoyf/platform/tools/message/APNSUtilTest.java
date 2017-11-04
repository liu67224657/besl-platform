package com.enjoyf.platform.tools.message;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.message.MessageHandler;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebApiHotdeployConfig;
import com.enjoyf.platform.service.message.ClientDevice;
import com.enjoyf.platform.service.message.ClientDeviceField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.message.APNSUtil;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import javapns.notification.PushNotificationPayload;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-14
 * Time: 上午9:51
 * To change this template use File | Settings | File Templates.
 */
public class APNSUtilTest {

    private static Logger logger = LoggerFactory.getLogger(APNSUtilTest.class);

    private static MessageHandler messageHandler;
    private static RedisManager manager;
    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        manager = new RedisManager(servProps);
        try {
            messageHandler = new MessageHandler("message", servProps,manager);
        } catch (DbException e) {
            System.exit(0);
            logger.error("update pointHandler error.");
        }
        javapnsTest();
    }

    private static void javapnsTest() {
        String ownUno = "e4d9d78b-caf0-49bf-86ab-f518ea3e1941";
        List<ClientDevice> deviceList = null;
        try {
            deviceList = messageHandler.queryClientDevice(new QueryExpress().add(QueryCriterions.eq(ClientDeviceField.UNO, ownUno)).add(QueryCriterions.eq(ClientDeviceField.PLATFORM, 0)));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        Set<String> deviceSet = new HashSet<String>();
        for (ClientDevice device : deviceList) {
            deviceSet.add(device.getClientToken());
        }

        String body = "这个是测试的。";
        int type = 1;
        String sound = "default";

        int badge = 1;
        List<String> tokens = new ArrayList<String>();
        tokens.addAll(deviceSet);

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

        boolean b = APNSUtil.sendToAPNS(tokens, payLoad, certificatePath, certificatePassword, isProduction);

        //失败重新发送一次
        if (!b) {
            APNSUtil.sendToAPNS(tokens, payLoad, certificatePath, certificatePassword, isProduction);
        }
    }
}
