package com.enjoyf.platform.tools.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.joymeapp.JoymeAppHandler;
import com.enjoyf.platform.db.message.MessageHandler;
import com.enjoyf.platform.db.oauth.OAuthHandler;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebApiHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.message.ClientDevice;
import com.enjoyf.platform.service.message.ClientDeviceField;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-2
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class ImportDeviceController {

    private static final Logger logger = LoggerFactory.getLogger(ImportDeviceController.class);

    private static JoymeAppHandler joymeAppHandler;
    private static MessageHandler messageHandler;
    private static RedisManager manager;

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        manager = new RedisManager(servProps);
        try {
            joymeAppHandler = new JoymeAppHandler("joymeapp", servProps);
            messageHandler = new MessageHandler("message", servProps, manager);
        } catch (DbException e) {
            System.exit(0);
            logger.error("update pointHandler error.");
        }
//		importDevice();
    }

//	private static void importDevice() {
//		try {
//			Pagination page = new Pagination(200, 1, 200);
//			do {
//				PageRows<PushMessageDevice> devicePageRows = joymeAppHandler.queryPushMessageDevice(new QueryExpress().add(QueryCriterions.eq(PushMessageDeviceField.APPKEY, "17yfn24TFexGybOF0PqjdY")), page);
//				if (devicePageRows != null && !CollectionUtil.isEmpty(devicePageRows.getRows())) {
//					page = devicePageRows.getPage();
//					for (PushMessageDevice device : devicePageRows.getRows()) {
//						ClientDevice clientDevice = new ClientDevice();
//						clientDevice.setClientId(device.getClientId());
//						clientDevice.setClientToken(device.getClientToken());
//						clientDevice.setPlatform(device.getPlatform());
//						clientDevice.setAppId(device.getAppKey());
//						clientDevice.setLastMsgId(device.getLastMsgId());
//						clientDevice.setUno("");
//						clientDevice.setPushChannel(AppPushChannel.DEFAULT);
//						clientDevice.setAppVersion("1.3.0");
//						clientDevice.setAppChannel("");
//						clientDevice.setTags("");
//						clientDevice.setAdvId("");
//						messageHandler.createClientDevice(clientDevice);
//					}
//				}
//			} while (!page.isLastPage());
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//
////        try {
////            boolean bool = messageHandler.modifyClientDevice(new QueryExpress()
////                    .add(QueryCriterions.eq(ClientDeviceField.PLATFORM, 0)),
////                    new UpdateExpress().set(ClientDeviceField.APP_CHANNEL, "appstore"));
////        } catch (ServiceException e) {
////            e.printStackTrace();
////        }
//	}
}
