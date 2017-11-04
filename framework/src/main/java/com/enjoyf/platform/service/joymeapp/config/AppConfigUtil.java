package com.enjoyf.platform.service.joymeapp.config;

import com.enjoyf.platform.service.joymeapp.AppChannelType;
import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/3/23
 * Description:
 */
public class AppConfigUtil {

    public static String getAppConfigId(String appkey, String platform, String versionStr, String channel, String enterprise) {
        return Md5Utils.md5(AppUtil.getAppKey(appkey) + "|" + platform + "|" + versionStr + "|" + channel + "|" + enterprise);
    }

    //appkey+platform+channel+type+directid
    public static String getShakeItemId(String appkey, AppPlatform platform, AppChannelType channel, int shakeType, String directId) {
        return Md5Utils.md5(appkey + platform.getCode() + channel.getCode() + shakeType + directId);
    }

    public static String getShakePoolKey(String appkey, int platform, String channel, ShakeType shakeType, ShakeConfig shakeConfig) {
        return Md5Utils.md5(appkey + platform + channel + shakeType.getCode() + shakeConfig.getBegintime() + shakeConfig.getEndtime());
    }
}
