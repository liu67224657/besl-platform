package com.enjoyf.webapps.tools.util;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.AuthProfile;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.StringUtil;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by ericliu on 2017/6/30.
 */
public class MicroAuthUtil {

    private static final String loginKey = "toolsadmin";

    public static String getToken() throws ServiceException {
        String token = getMicroToken();
        if (StringUtil.isEmpty(token)) {
            AuthProfile authProfile = UserCenterServiceSngl.get().auth(loginKey, LoginDomain.CLIENT, null, "", "toolsadmin", "default", "127.0.0.1", new Date(), new HashMap<String, String>());
            if (authProfile == null) {
                return "";
            }

            token = authProfile.getToken().getToken();
            putMicroToken("Bearer " + token);
        }

        return token;
    }

    public static void putMicroToken(String token) {
        WebappConfig.get().getMemCacheManager().put("tools:miscrotoken", token, 86400 * 31);
    }

    private static String getMicroToken() {
        Object obj = WebappConfig.get().getMemCacheManager().get("tools:miscrotoken");
        if (obj != null) {
            return (String) obj;
        }
        return "";
    }


    public static void removeMiscroToken() {
        WebappConfig.get().getMemCacheManager().remove("tools:miscrotoken");
    }

}
