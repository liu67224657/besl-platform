package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.naming.ResourceServerMonitor;
import com.enjoyf.platform.service.oauth.OAuthInfoResult;
import com.enjoyf.platform.service.usercenter.AuthProfile;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.ProfileFlag;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.thirdapi.oauth.OAuthInfo;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weibo4j.model.PostParameter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.util.HttpUpload;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 用户的认证controller抽象类
 * Created by ericliu on 14/10/22.
 */
public abstract class AbstractAuthController extends BaseRestSpringController {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    //将登录存到cookie
    protected void saveCookieReUrl(AuthUrlParam authUrlParam, HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.setCookie(request, response, "uc_reurl", authUrlParam.getCookieValue(), -1);
    }

    //取cookie
    protected AuthUrlParam getCookieReurl(HttpServletRequest request) {
        return AuthUrlParam.getByCookieValue(CookieUtil.getCookieValue(request, "uc_reurl"));
    }

    //删除cookie
    protected void deleteCookieReurl(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.setCookie(request, response, "uc_reurl", "", 0);
    }


    protected String getReurl(HttpServletRequest request) {
        String reurl = HTTPUtil.getParam(request, "reurl");
        if (StringUtil.isEmpty(reurl)) {
            reurl = request.getHeader("referer");
        }
        if (StringUtil.isEmpty(reurl) || reurl.contains("/security")
                || reurl.contains("/profile/sync")
                || reurl.contains("/profile/mobile")
                || reurl.contains("/auth/savenick")
                || reurl.contains("/auth/login") || reurl.contains("/auth/register")) {
            reurl = WebappConfig.get().getUrlWww();
            return reurl;
        }

        try {
            reurl = URLDecoder.decode(reurl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return reurl;
    }

    protected JSONObject getResultByAuthProfile(AuthProfile profile) {
        JSONObject jsonObject = new JSONObject();

        JSONObject tokenJson = new JSONObject();
        tokenJson.put("token", profile.getToken().getToken());
        tokenJson.put("expires", String.valueOf(profile.getToken().getTokenExpires()));

        JSONObject profileJson = new JSONObject();
        profileJson.put("uno", profile.getProfile().getUno());
        profileJson.put("profileid", profile.getProfile().getProfileId());
        profileJson.put("pid", profile.getProfile().getProfileId());
        profileJson.put("icon", ImageURLTag.parseUserCenterHeadIcon(profile.getProfile().getIcon(), profile.getProfile().getSex(), "m", true));
        profileJson.put("nick", profile.getProfile().getNick() == null ? "" : profile.getProfile().getNick());
        profileJson.put("uid", String.valueOf(profile.getProfile().getUid()));
        profileJson.put("desc", profile.getProfile().getDescription() == null ? "" : profile.getProfile().getDescription());
        profileJson.put("logindomain", (profile.getUserLogin() == null || profile.getUserLogin().getLoginDomain() == null) ? "" : profile.getUserLogin().getLoginDomain().getCode());
        profileJson.put("flag", profile.getProfile().getFlag().getValue());
        profileJson.put("mobile", profile.getProfile().getMobile() == null ? "" : profile.getProfile().getMobile());
        profileJson.put("province", profile.getProfile().getProvinceId() == null ? "" : profile.getProfile().getProvinceId());
        profileJson.put("city", profile.getProfile().getCityId() == null ? "" : profile.getProfile().getCityId());
        profileJson.put("sex", profile.getProfile().getSex() == null ? "" : profile.getProfile().getSex());
        String fresh = "false";
        if (profile.getProfile().getFreshUser()) {
            fresh = "true";
        }
        profileJson.put("fresh", fresh);

        jsonObject.put("rs", 1);
        jsonObject.put("token", tokenJson);
        jsonObject.put("profile", profileJson);

        return jsonObject;
    }

    protected String uploadThirdIconByUrl(String figUrl) {
        //获得第三方 用户信息 的 头像
        if (StringUtil.isEmpty(figUrl)) {
            return null;
        }
        String uploadUrl = "http://" + ResourceServerMonitor.get().getRandomUploadDomainPrefix() + "." + Constant.DOMAIN + "/json/upload/figureurl";

        String token = "joymeplatform";
        String response;
        try {
            response = new HttpUpload().post(uploadUrl,
                    new PostParameter[]{new PostParameter("url", figUrl),
                            new PostParameter(CookieUtil.ACCESS_TOKEN, token)}, token);
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(response);

            if (jsonObject == null) {
                return null;
            }

            if (!jsonObject.getString("rs").equals("1")) {
                return null;
            }

            return jsonObject.getString("url");
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " HttpUpload post " + e);
        }

        return null;
    }


    protected ModelAndView getModelAndViewAfterLogin(AuthProfile authProfile, String url, HttpServletRequest request, String appkey, LoginDomain loginDomain) throws UnsupportedEncodingException {
        String nick = authProfile.getProfile().getNick();
        //手机端处理方案
        if ((AppUtil.checkIsAndroid(request) || AppUtil.checkIsIOS(request)) && !AppUtil.checkIsWeixin(request)) {
            if (!authProfile.getProfile().getFlag().hasFlag(ProfileFlag.FLAG_NICK_HASCOMPLETE)) {
                return new ModelAndView("redirect:http://passport." + WebappConfig.get().getDomain() + "/auth/savenickpage?reurl=" + URLEncoder.encode(url, "UTF-8") + "&appkey=" + appkey + "&profileid=" + authProfile.getProfile().getProfileId() + "&logindomain=" + loginDomain.getCode());
            } else if (ContextFilterUtils.checkNickRegexs(nick)) {
                return new ModelAndView("redirect:http://passport." + WebappConfig.get().getDomain() + "/auth/savenickpage?reurl=" + URLEncoder.encode(url, "UTF-8") + "&appkey=" + appkey + "&profileid=" + authProfile.getProfile().getProfileId() + "&logindomain=" + loginDomain.getCode());
            } else {
                return new ModelAndView("redirect:http://passport." + WebappConfig.get().getDomain() + "/appredirect/loginsuccess?reurl=" + URLEncoder.encode(url, "UTF-8"));
            }
        } else {
            if (!authProfile.getProfile().getFlag().hasFlag(ProfileFlag.FLAG_NICK_HASCOMPLETE)) {
                return new ModelAndView("redirect:http://passport." + WebappConfig.get().getDomain() + "/auth/savenickpage?reurl=" + URLEncoder.encode(url, "UTF-8") + "&appkey=" + appkey + "&profileid=" + authProfile.getProfile().getProfileId() + "&logindomain=" + loginDomain.getCode());
            } else if (ContextFilterUtils.checkNickRegexs(nick)) {
                return new ModelAndView("redirect:http://passport." + WebappConfig.get().getDomain() + "/auth/savenickpage?reurl=" + URLEncoder.encode(url, "UTF-8") + "&appkey=" + appkey + "&profileid=" + authProfile.getProfile().getProfileId() + "&logindomain=" + loginDomain.getCode());
            } else {
                return new ModelAndView("redirect:" + HTTPUtil.encodeUrl(url));
            }
        }
    }

    //以后不再用了
    protected ResultObjectMsg getSyncJson(OAuthInfo oAuthInfo, AuthProfile authProfile, String apiCode) {
        ResultObjectMsg msg = new ResultObjectMsg();
        if (oAuthInfo == null || authProfile == null) {
            msg.setMsg(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getMsg());
            msg.setRs(ResultCodeConstants.OAUTH2_AUTHPARAM_AUTHINFO.getCode());
            return msg;
        }

        OAuthInfoResult oAuthInfoResult = new OAuthInfoResult();
        oAuthInfoResult.setUno(authProfile.getProfile().getUno());
        oAuthInfoResult.setAccesstoken(oAuthInfo.getAccessToken().getAccessToken());
        oAuthInfoResult.setRefreshtoken(StringUtil.isEmpty(oAuthInfo.getAccessToken().getRefreshToken()) ? "default" : oAuthInfo.getAccessToken().getRefreshToken());

        long expire_date = -1;
        try {
            expire_date = Long.parseLong(oAuthInfo.getAccessToken().getExpire());
        } catch (Exception e) {
            logger.info(this.getClass().getName() + " get expire_date " + oAuthInfo + " error.e:" + e.getMessage());
        }
        oAuthInfoResult.setExpire_date(expire_date);
        oAuthInfoResult.setType(apiCode);
        oAuthInfoResult.setUsername(authProfile.getProfile().getNick());

        //用户头像
        String icon = authProfile.getProfile().getIcon();
        oAuthInfoResult.setIcon(icon);

        msg.setResult(oAuthInfoResult);
        msg.setRs(ResultCodeConstants.SUCCESS.getCode());

        return msg;
    }


}
