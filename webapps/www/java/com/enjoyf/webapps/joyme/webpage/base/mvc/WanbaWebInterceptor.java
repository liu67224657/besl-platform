package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigUtil;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.util.CheckAuthUtil;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/3
 * Description:
 */
public class WanbaWebInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = HTTPUtil.getParam(request, "token");
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(token)) {
            jsonObject.put("rs", ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
            jsonObject.put("msg", ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
            HTTPUtil.writeJson(response, jsonObject.toString());
            return false;
        }
        boolean bool = CheckAuthUtil.isNormalRequest(token, request, false);
        if (!bool) {
            jsonObject.put("rs", ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
            jsonObject.put("msg", ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
            HTTPUtil.writeJson(response, jsonObject.toString());
            return false;
        }
        return true;
    }
}
