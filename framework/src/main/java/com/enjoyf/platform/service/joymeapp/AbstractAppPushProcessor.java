package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.message.ClientDevice;
import com.enjoyf.platform.service.message.ClientDeviceField;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-2
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAppPushProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAppPushProcessor.class);

    protected Set<String> getDeviceSetByDB(PushMessage pushMessage) throws ServiceException {
        Set<String> deviceSet = new HashSet<String>();

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ClientDeviceField.APP_ID, getAppKey(pushMessage.getAppKey())));
        if (pushMessage.getAppPlatform().getCode() == 0) {
            queryExpress.add(QueryCriterions.eq(ClientDeviceField.PLATFORM, 0));
            queryExpress.add(QueryCriterions.eq(ClientDeviceField.ENTERPRISER_TYPE, AppEnterpriserType.DEFAULT));
        } else if (pushMessage.getAppPlatform().getCode() == 1) {
            queryExpress.add(QueryCriterions.eq(ClientDeviceField.PLATFORM, 1));
        } else if (pushMessage.getAppPlatform().getCode() == 2) {
            queryExpress.add(QueryCriterions.eq(ClientDeviceField.PLATFORM, 0));
            queryExpress.add(QueryCriterions.eq(ClientDeviceField.ENTERPRISER_TYPE, AppEnterpriserType.ENTERPRISE));
        }
        if (!StringUtil.isEmpty(pushMessage.getUnos())) {
            String[] profileArr = pushMessage.getUnos().split(" ");
            queryExpress.add(QueryCriterions.in(ClientDeviceField.UNO, profileArr));
        }
        if (!StringUtil.isEmpty(pushMessage.getAppVersion())) {
            String[] versionArr = pushMessage.getAppVersion().split("\\|");
            queryExpress.add(QueryCriterions.in(ClientDeviceField.APP_VERSION, versionArr));
        }
        if (!StringUtil.isEmpty(pushMessage.getAppChannel())) {
            String[] channelArr = pushMessage.getAppChannel().split("\\|");
            queryExpress.add(QueryCriterions.in(ClientDeviceField.APP_CHANNEL, channelArr));
        }

        Pagination pagination = null;
        int cp = 0;
        do {
            cp += 1;
            pagination = new Pagination(200 * cp, cp, 200);
            PageRows<ClientDevice> devicePageRows = MessageServiceSngl.get().queryClientDeviceByPage(queryExpress, pagination);
            if (devicePageRows == null || CollectionUtil.isEmpty(devicePageRows.getRows())) {
                break;
            }
            pagination = devicePageRows.getPage();
            for (ClientDevice device : devicePageRows.getRows()) {
                deviceSet.add(device.getClientToken());
            }
        } while (!pagination.isLastPage());

        return deviceSet;
    }

    protected static String getAppKey(String appKey) {
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }

}
