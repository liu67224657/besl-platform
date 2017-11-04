package com.enjoyf.webapps.joyme.webpage.controller.weixin;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.weixin.resp.RespTextMessage;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.weixin.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-7-22
 * Time: 下午5:01
 * To change this template use File | Settings | File Templates.
 */
public class WeixinUtil {
    public static final String WX_TOKEN_KEY = "wxtk_";
    public static final int EXPIRE = 3600 * 6;

    public static String saveToken(String fromUserName) {
        String token = Md5Utils.md5(UUID.randomUUID().toString());
        try {
            MiscServiceSngl.get().saveRedisMiscValue(WX_TOKEN_KEY + token, fromUserName, EXPIRE);
        } catch (ServiceException e) {
            GAlerter.lab(WeixinUtil.class.getName() + " occured ServiceException.e:", e);
        }
        return token;
    }

    public static String getOpenIdByToken(String token) {
        try {
           return  MiscServiceSngl.get().getRedisMiscValue(WX_TOKEN_KEY + token);
        } catch (ServiceException e) {
            GAlerter.lab(WeixinUtil.class.getName() + " occured ServiceException.e:", e);
        }
        return null;
    }
}
