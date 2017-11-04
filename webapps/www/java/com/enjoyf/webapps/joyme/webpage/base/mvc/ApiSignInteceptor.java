/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.security.DESUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * api接口签名认证的key
 *
 * @author ericliu
 */
public class ApiSignInteceptor extends HandlerInterceptorAdapter {

    private JsonBinder jsonBinder = JsonBinder.buildNormalBinder();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String appkey = request.getParameter("appkey");
        String client_id = request.getParameter("client_id");
        String client_token = request.getParameter("client_token");
        String timestamp = request.getParameter("timestamp");
        String signClient = request.getParameter("sign");


        if (StringUtil.isEmpty(appkey) ||
                StringUtil.isEmpty(client_id) ||
                StringUtil.isEmpty(client_token) ||
                StringUtil.isEmpty(timestamp)) {
            //参数错误
            ResultObjectMsg resultMsg = new ResultObjectMsg(ResultCodeConstants.CODE_ERROR_SIGN_PARAMEMPTY.getCode(), ResultCodeConstants.CODE_ERROR_SIGN_PARAMEMPTY.getMsg());
            sendJson(response, resultMsg);
            return false;
        }

        AuthApp app = OAuthServiceSngl.get().getApp(getAppKey(appkey));
        if (app == null) {
            ResultObjectMsg resultMsg = new ResultObjectMsg(ResultCodeConstants.CODE_ERROR_APP_NOTEXISTS.getCode(),ResultCodeConstants.CODE_ERROR_APP_NOTEXISTS.getMsg());
            sendJson(response, resultMsg);
            return false;
        }

        String pwdKey = encrypt(appkey, app.getAppKey(), client_id, client_token, timestamp);

        if (!pwdKey.equals(signClient)) {
            ResultObjectMsg resultMsg = new ResultObjectMsg(ResultCodeConstants.CODE_ERROR_SIGN_FAIED.getCode(),ResultCodeConstants.CODE_ERROR_SIGN_FAIED.getMsg());
            sendJson(response, resultMsg);
            return false;
        }

        return true;
    }

    private String encrypt(String appkey, String appSecr, String clientId, String clientToken, String timestamp) throws Exception {
        String info = "&" + appkey + "&" + clientId + "&" + clientToken + "&" + timestamp;
        return DESUtil.desEncrypt(appSecr, info);
    }

    private void sendJson(HttpServletResponse response, ResultObjectMsg msg) {
        try {
            HTTPUtil.writeJson(response, jsonBinder.toJson(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getAppKey(String appKey) {
        if (StringUtil.isEmpty(appKey)) {
            return "";
        }
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }


}
