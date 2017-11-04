package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.oauth.TimestampVerification;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.security.DESUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by guoliangnan on 14-1-10.
 */
public class JsonShareInteceptor extends HandlerInterceptorAdapter {

    private JsonBinder jsonBinder = JsonBinder.buildNormalBinder();


    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, java.lang.Object handler) throws java.lang.Exception {

        //通过id分享直接返回
        String url = request.getRequestURL().toString();
        if(url.contains("json/share/getbyid")){
             return true;
        }

        String appkeyParam = request.getParameter("appkey");
        String uno = request.getParameter("uno");
        String timestamp = request.getParameter("timestamp");
        String signClient = request.getParameter("sign");

        String appkey = getAppKey(appkeyParam);

        //验证参数
        if (StringUtil.isEmpty(appkey) || StringUtil.isEmpty(uno) || StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(signClient)) {
            //参数错误
            ResultObjectMsg resultMsg = new ResultObjectMsg(ResultCodeConstants.PARAM_ISEMPTY.getCode(), ResultCodeConstants.PARAM_ISEMPTY.getMsg());
            sendJson(response, resultMsg);
            return false;
        }

        //根据uno查询memcache结果判断是否之前有访问，如果有直接返回
        TimestampVerification timestampByServer = OAuthServiceSngl.get().getTimestampByUno(uno, timestamp);
        if (timestampByServer != null && timestamp.equals(timestampByServer.getTimestamp())) {
            ResultObjectMsg resultMsg = new ResultObjectMsg(ResultCodeConstants.REQUEST_ERROR.getCode(), ResultCodeConstants.REQUEST_ERROR.getMsg());
            sendJson(response, resultMsg);
            return false;
        }

        AuthApp app = OAuthServiceSngl.get().getApp(appkey);
        if (app == null) {
            ResultObjectMsg resultMsg = new ResultObjectMsg(ResultCodeConstants.APP_INVALID.getCode(), ResultCodeConstants.APP_INVALID.getMsg());
            sendJson(response, resultMsg);
            return false;
        }

        //根据appkey加密参数
        String param = "&" + appkeyParam + "&" + uno + "&" + timestamp;
        String encryptParam = DESUtil.desEncrypt(app.getAppKey(), param);

        //如果加密结果不同，认定无效请求，直接返回，否则存入memCache通过验证
        if (!signClient.equals(encryptParam)) {
            ResultObjectMsg resultMsg = new ResultObjectMsg(ResultCodeConstants.SIGN_FAILD.getCode(), ResultCodeConstants.SIGN_FAILD.getMsg());
            sendJson(response, resultMsg);
            return false;
        } else {
            TimestampVerification shareVerification = new TimestampVerification();
            shareVerification.setUno(uno);
            shareVerification.setTimestamp(timestamp);
            OAuthServiceSngl.get().saveTimestamp(shareVerification);
        }
        return true;
    }

    private void sendJson(HttpServletResponse response, ResultObjectMsg msg) {
        try {
            msg.setResult("");
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
