package com.enjoyf.platform.thirdapi;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.springframework.web.util.HtmlUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class AuthUrlParam {
    private String rurl;
    private boolean requiredLogin = false;
    private String redirectType;
    private String appKey;

    public AuthUrlParam(String rurl, boolean requiredLogin, String redirectType, String appKey) {
        this.rurl = rurl;
        this.requiredLogin = requiredLogin;
        this.redirectType = redirectType;
        this.appKey = appKey;
    }

    public void setRurl(String rurl) {
        this.rurl = rurl;
    }


    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    @Deprecated
    public String generatorUrlParam() {
        return "?" + (StringUtil.isEmpty(rurl) ? "" : "rurl=" + rurl)
                + (StringUtil.isEmpty(redirectType) ? "" : "&rt=" + redirectType)
                + (StringUtil.isEmpty(appKey) ? "" : "&ak=" + appKey)
                + ("&rl=" + requiredLogin);
    }

    @Deprecated
    public String generatorCallbackParam() {
        return "?callbackparam=" + (StringUtil.isEmpty(rurl) ? "" : "rurl_-_-_-_" + rurl)
                + (StringUtil.isEmpty(redirectType) ? "" : "-_-_-_-rt_-_-_-_" + redirectType)
                + (StringUtil.isEmpty(appKey) ? "" : "-_-_-_-ak_-_-_-_" + appKey)
                + ("-_-_-_-rl_-_-_-_" + requiredLogin);
    }


    public String getCookieValue() {
        try {
            return (StringUtil.isEmpty(rurl) ? "" : "rurl_-_-_-_" + URLEncoder.encode(rurl, "UTF-8"))
                    + (StringUtil.isEmpty(redirectType) ? "" : "-_-_-_-rt_-_-_-_" + redirectType)
                    + (StringUtil.isEmpty(appKey) ? "" : "-_-_-_-ak_-_-_-_" + appKey)
                    + ("-_-_-_-rl_-_-_-_" + requiredLogin);
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public static AuthUrlParam getByCookieValue(String cookieValue) {
        if (StringUtil.isEmpty(cookieValue)) {
            return null;
        }

        String[] params = cookieValue.split("-_-_-_-"); //&
        String rurl = "";
        boolean requiredLogin = false;
        String redirectType = "";
        String appKey = "";
        for (String param : params) {
            String[] paramArray = param.split("_-_-_-_");
            if (paramArray.length == 2) {
                if (paramArray[0].equalsIgnoreCase("rurl")) {
                    try {
                        rurl = URLDecoder.decode(paramArray[1], "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                } else if (paramArray[0].equalsIgnoreCase("rt")) {
                    redirectType = paramArray[1];
                } else if (paramArray[0].equalsIgnoreCase("rl")) {
                    requiredLogin = Boolean.parseBoolean(paramArray[1]);
                } else if (paramArray[0].equalsIgnoreCase("ak")) {
                    appKey = paramArray[1];
                }
            }
        }

        return new AuthUrlParam(rurl, requiredLogin, redirectType, appKey);
    }


    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

//    public String getGid() {
//        return gid;
//    }
//
//    public void setGid(String gid) {
//        this.gid = gid;
//    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getRurl() {
        return rurl;
    }

    public boolean isRequiredLogin() {
        return requiredLogin;
    }

    public String getAppKey() {
        return appKey;
    }

    public static void main(String[] args) {
        System.out.println(new AuthUrlParam("http://www.joyme.test", true, "xxx", "").generatorCallbackParam());

        System.out.println(HtmlUtils.htmlUnescape(new AuthUrlParam("http://www.joyme.test", true, "xxx", "").generatorCallbackParam()));

        try {
            System.out.println(URLEncoder.encode(new AuthUrlParam("http://www.joyme.test", true, "xxx", "").generatorUrlParam(), "UTF-8"));
            System.out.println(HtmlUtils.htmlEscape(URLEncoder.encode(new AuthUrlParam("http://www.joyme.test", true, "xxx", "").generatorUrlParam(), "UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
