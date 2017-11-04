package com.enjoyf.platform.service.message;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.message.MessageHandler;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-18
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
public class AbstractMessageProcessor {

    public void sendSocialMessage(MessageHandler readonlyMessageHandler, String ownUno, String body, int type, String sound) {
        GAlerter.lab("-----------------------------------------START SEND MESSAGE FACTORY------------------------------------------------");
        List<ClientDevice> deviceList = null;
        try {
            deviceList = readonlyMessageHandler.queryClientDevice(new QueryExpress().add(QueryCriterions.eq(ClientDeviceField.UNO, ownUno)).add(QueryCriterions.eq(ClientDeviceField.PLATFORM, 0)));
            if (CollectionUtil.isEmpty(deviceList)) {
                GAlerter.lab("-----------------------------------------DEVICE EMPTY " + ownUno + "------------------------------------------------");
                return;
            }
        } catch (DbException e) {
            GAlerter.lab(this.getClass().getName() + " queryClientDevice occur exception.e", e);
        }
        Set<String> deviceSet = new HashSet<String>();
        for (ClientDevice device : deviceList) {
            deviceSet.add(device.getClientToken());
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("deviceset", deviceSet);
        paramMap.put("body", body);
        paramMap.put("msgtype", type);
        paramMap.put("sound", sound);
        paramMap.put("info", ownUno);
        GAlerter.lab("-----------------------------------------START PROCESS MESSAGE FACTORY------------------------------------------------");
        MessageProcessFactory.get().factory(new SocialMessage()).processSendMessage(paramMap);
    }

    public static int getIntervalHour(long begin, long end) {
        long ms = (end - begin) / 1000 / 60;
        return (int) ms;
    }

}
