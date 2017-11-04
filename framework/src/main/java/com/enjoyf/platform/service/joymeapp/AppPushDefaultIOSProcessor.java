package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.event.system.AppPushEvent;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.message.APNSUtil;
import javapns.notification.PushNotificationPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-2
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
public class AppPushDefaultIOSProcessor extends AbstractAppPushProcessor implements AppPushProcessor {
	private static final Logger logger = LoggerFactory.getLogger(AppPushDefaultIOSProcessor.class);

	@Override
	public void sendPushMessage(AppPushEvent appPushEvent) {
		try {
			AppDeployment certificate = JoymeAppConfigServiceSngl.get().getAppDeploymentByCache(appPushEvent.getAppKey(), appPushEvent.getPlatform().getCode(), AppDeploymentType.CERTIFICATE.getCode(), null, null);
			if (certificate == null) {
				logger.info("---------------------------AppPushDefaultIOSProcessor sendPushMessage certificate null-----------------------------");
				return;
			}
			String path = certificate.getPath();
			String password = certificate.getPassword();
			boolean isProd = certificate.getIsProduct();
			logger.info("---------------------------AppPushDefaultIOSProcessor sendPushMessage certificate.path:" + path + "-----------------------------");
			logger.info("---------------------------AppPushDefaultIOSProcessor sendPushMessage certificate.password:" + password + "-----------------------------");
			logger.info("---------------------------AppPushDefaultIOSProcessor sendPushMessage certificate.isProd:" + isProd + "-----------------------------");
		} catch (Exception e) {
			GAlerter.lab(this.getClass().getName() + " sendSociailPushMessage occur exception.e", e);
		}
	}
}
