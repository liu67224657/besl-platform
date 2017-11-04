package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/17
 * Description:
 */
public class GameDBUtils {

    /**
     * 1=iphone 2=ipad 3=android
     */
    public static String getGameDbDeviceByPlatform(AppPlatform appPlatform) {
        if (appPlatform.equals(AppPlatform.IOS)) {
            return "1";
        } else if (appPlatform.equals(AppPlatform.ANDROID)) {
            return "3";
        }
        return "0";
    }

    //device+channelcode--->object
    public static Map<String, String> getGameDownloadMap(GameDB gameDb) {
        Map<String, String> map = new HashMap<String, String>();

        Set<GameDBChannelInfo> gameDBChannelInfoSet = gameDb.getChannelInfoSet();
        if (CollectionUtil.isEmpty(gameDBChannelInfoSet)) {
            return map;
        }

        for (GameDBChannelInfo channelInfo : gameDBChannelInfoSet) {
            String device = channelInfo.getDevice();
            String channelid = channelInfo.getChannel_id();
            map.put(device + channelid, channelInfo.getChannelDownloadInfo().getDownload());
        }
        return map;
    }

    //device+channelcode--->object
    public static String getGameDownload(GameDB gameDb, AppPlatform appPlatform, long channelId) throws ServiceException {
        String device = getGameDbDeviceByPlatform(appPlatform);

        return getGameDownloadMap(gameDb).get(device + channelId);
    }
}
