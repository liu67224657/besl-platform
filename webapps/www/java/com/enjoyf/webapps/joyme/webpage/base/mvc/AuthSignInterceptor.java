package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigUtil;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;
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
public class AuthSignInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String appKey = request.getParameter("appkey");
        String sign = request.getParameter("sign");
        String time = request.getParameter("time");


        if (StringUtil.isEmpty(appKey) || StringUtil.isEmpty(sign) || StringUtil.isEmpty(time)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
            jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
            HTTPUtil.writeJson(response, jsonObject.toString());
            return false;
        }

        String platform = request.getParameter("platform");
        String channelid = request.getParameter("channelid");
        String clientid = request.getParameter("clientid");

        //解决 ‘我的’ sdk  1.0.5版本不兼容。“设备自注册失败: 参数为空” po-卢捷 邮件中提供的appkey
        if (!(appKey.equals("119mpBeIV49bCDFJj5uSZ4") || appKey.equals("1B7Ftb6ed33p1wAk0o49Yk") || appKey.equals("2bEzjgsNt5cHVVl5mmA1cq"))
                && (StringUtil.isEmpty(platform) || StringUtil.isEmpty(channelid) || StringUtil.isEmpty(clientid))) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.PARAM_EMPTY.getCode()));
            jsonObject.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
            HTTPUtil.writeJson(response, jsonObject.toString());
            return false;
        }

        try {
            AuthApp app = OAuthServiceSngl.get().getApp(appKey);
            if (app == null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.APP_NOT_EXISTS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                HTTPUtil.writeJson(response, jsonObject.toString());
                return false;
            }

            String signString = "";

            //有设备来源和模拟 就需要拦截
            String source = request.getParameter("source");
            String mock = request.getParameter("mock");
            if (!StringUtil.isEmpty(source) && !StringUtil.isEmpty(mock)) {
                HashMap<String, String> paramMap = HTTPUtil.getRequestToMap(request);

                //get sign secret
                AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(AppConfigUtil.getAppConfigId(appKey, platform, request.getParameter("version"), channelid, String.valueOf(AppEnterpriserType.getEnterpriser(appKey))));
                String secret = null;
                //先从appconfig里面查找是否存在
                if (appConfig == null || StringUtil.isEmpty(appConfig.getAppSecret())) {
                    if (AppPlatform.IOS.equals(AppPlatform.getByCode(Integer.valueOf(platform)))) {
                        secret = app.getAppSecret().getIos();
                    } else if (AppPlatform.ANDROID.equals(AppPlatform.getByCode(Integer.valueOf(platform)))) {
                        secret = app.getAppSecret().getAndroid();
                    }
                } else {
                    secret = appConfig.getAppSecret();
                }

                //vaidate sign
                signString = HTTPUtil.getSignature(paramMap, secret);
                if (!signString.equalsIgnoreCase(sign)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_AUTH_SIGNERROR.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_AUTH_SIGNERROR.getMsg() + " new");
                    HTTPUtil.writeJson(response, jsonObject.toString());
                    return false;
                }
            } else {
                //之前的加密逻辑 修改时间:2015-03-24
                signString = Md5Utils.md5(appKey + app.getAppKey() + time);
                if (!signString.equalsIgnoreCase(sign)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_AUTH_SIGNERROR.getCode()));
                    jsonObject.put("msg", ResultCodeConstants.USERCENTER_AUTH_SIGNERROR.getMsg());
                    HTTPUtil.writeJson(response, jsonObject.toString());
                    return false;
                }
            }


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SYSTEM_ERROR.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
            HTTPUtil.writeJson(response, jsonObject.toString());
            return false;

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SYSTEM_ERROR.getCode()));
            jsonObject.put("msg", ResultCodeConstants.SYSTEM_ERROR.getMsg());
            HTTPUtil.writeJson(response, jsonObject.toString());
            return false;
        }

        return true;
    }


}
