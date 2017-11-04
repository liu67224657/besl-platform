package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ask.WanbaItemDomain;
import com.enjoyf.util.MD5Util;

/**
 * Created by zhimingli on 2016/9/18 0018.
 */
public class ActivityTopMenuUtil {

    public static String getActivityTopMenuLineKey(String appkey, Long channelid, Integer platform) {
        return MD5Util.Md5(appkey + String.valueOf(channelid) + String.valueOf(platform));
    }
}
