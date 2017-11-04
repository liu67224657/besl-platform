package com.enjoyf.platform.util;

import com.enjoyf.platform.service.joymeapp.AppPlatform;

import com.enjoyf.platform.util.regex.RegexUtil;
import com.enjoyf.util.CookieUtil;
import com.enjoyf.util.MD5Util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;

public class HTTPUtil {

    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String COOKIE_HOME_PREF = "home.pref";
    private static final String COOKIE_HOME_PREF_MOBILE = "m";
    private static final String COOKIE_HOME_PREF_WWW = "w";
    public static final String HEADER_JPARAM = "JParam";

    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级反向代理
        if (null != ip && !"".equals(ip.trim())) {
            StringTokenizer st = new StringTokenizer(ip, ",");
            String ipTmp = "";
            if (st.countTokens() > 1) {
                while (st.hasMoreTokens()) {
                    ipTmp = st.nextToken();
                    if (ipTmp != null && ipTmp.length() != 0
                            && !"unknown".equalsIgnoreCase(ipTmp)) {
                        ip = ipTmp;
                        break;
                    }
                }
            }
        }
        return ip;
    }

    public static String getCookieValue(HttpServletRequest request, String key) {
        String value = null;
        if (request != null && request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals(key)) {
                    value = c.getValue();
                    break;
                }
            }
        }
        return value;
    }

    public static void setCookie(HttpServletResponse resp, Cookie sessionCookie) {
        resp.addCookie(sessionCookie);
    }

    public static void setCookie(HttpServletResponse resp, String name, String value) {
        resp.addCookie(new Cookie(name, value));
    }

    public static void removeCookie(HttpServletResponse resp, Cookie c) {
        c.setValue(null);
        c.setMaxAge(0);
        resp.addCookie(c);
    }

    public static String getRedr(HttpServletRequest request) {
        String redr = "";
        if (request.getMethod().equalsIgnoreCase("get")) {
            redr = getRequestedUrl(request);
        } else {
            redr = request.getHeader("referer");
        }

        redr = StringUtil.nullToblank(redr);

        try {
            redr = URLEncoder.encode(redr, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }

        return redr;
    }

    public static String getRequestedUrl(HttpServletRequest request) {
        StringBuffer urlBuf = new StringBuffer(16);

        urlBuf.append(request.getRequestURL());

        if (!StringUtil.isEmpty(request.getQueryString())) {
            urlBuf.append("?").append(request.getQueryString());
        }

        return urlBuf.toString();
    }

    public static String getRequestedURIQueryString(HttpServletRequest request) {
        StringBuffer urlBuf = new StringBuffer();

        urlBuf.append(request.getRequestURI());

        if (!StringUtil.isEmpty(request.getQueryString())) {
            urlBuf.append("?").append(request.getQueryString());
        }

        return urlBuf.toString();
    }

    public static String getURIQueryString(String url) {
        StringBuffer urlBuf = new StringBuffer();

        try {
            URL urlObj = new URL(url);

            if (urlObj != null) {
                urlBuf.append(urlObj.getFile());
            }
        } catch (Exception e) {
            //
        }

        return urlBuf.toString();
    }

    public static String getServerBaseUrl(HttpServletRequest request, String url) {
        StringBuffer sb = new StringBuffer();
        sb.append(request.getServletContext().getRealPath(url));
        return sb.toString();
    }

    public static void writeJson(HttpServletResponse response, String jsonStr) throws IOException {
        response.setContentType("text/plain");

        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.write(jsonStr);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }


    public static Map<String, String> getJParam(String JParam) {
        if (StringUtil.isEmpty(JParam)) {
            return Collections.EMPTY_MAP;
        }
        Map<String, String> map = new HashMap<String, String>();
        try {
            JParam = URLDecoder.decode(JParam, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] jparam = JParam.split("; ");
        for (String j : jparam) {
            String[] keyValue = j.split("=");
            if (keyValue != null && keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }


    /**
     * 优先级  paramter-->head-->jParam
     *
     * @param request
     * @param key
     * @return
     */
    public static String getParam(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        if (!StringUtil.isEmpty(value)) {
            return value;
        }
        value = request.getHeader(key);
        if (!StringUtil.isEmpty(value)) {
            return value;
        }
        //从cookie里取jparam
        Cookie cookie = CookieUtil.getCookie(request, HEADER_JPARAM);
        if (cookie != null) {
            Map<String, String> jParam = getJParam(cookie.getValue());
            if (!CollectionUtil.isEmpty(jParam)) {
                value = jParam.get(key);
                if (!StringUtil.isEmpty(value)) {
                    return value;
                }
            }
        }

        Map<String, String> jParam = getJParam(request.getHeader(HEADER_JPARAM));
        if (!CollectionUtil.isEmpty(jParam)) {
            value = jParam.get(key);
        }

        return value;
    }

    /**
     * 将request请求map对象
     *
     * @return
     */
    public static HashMap<String, String> getRequestToMap(HttpServletRequest request) {
        String paramName = "";
        String paramValue = "";
        HashMap<String, String> paramMap = new HashMap<String, String>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            paramName = (String) paramNames.nextElement();
            if (paramName.equals("sign")) {
                continue;
            }
            paramValue = HTTPUtil.getParam(request, paramName);
            paramMap.put(paramName, paramValue);
        }
        return paramMap;
    }

    /**
     * 签名生成算法
     * <p/>
     * HashMap<String,String> params 请求参数集，所有参数必须已转换为字符串类型
     * String
     * secret 签名密钥
     *
     * @return 签名
     */
    public static String getSignature(HashMap<String, String> params, String secret) {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<String, String>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"keyvalue"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            if (!param.getKey().equalsIgnoreCase("\\x0A") && !param.getKey().equalsIgnoreCase("\n")) {//todo for test
                basestring.append(param.getKey()).append(param.getValue() == null ? "" : param.getValue());
            }
        }
        basestring.append(secret);
        // 使用MD5对待签名串求签
        return MD5Util.Md5(basestring.toString());
    }

    /**
     * 玩霸判断是否是新版本
     *
     * @param request
     * @return
     */
    public static boolean isNewVersion(HttpServletRequest request) {
        String version = getParam(request, "version");
        String platform = getParam(request, "platform");
        if (StringUtil.isEmpty(version) || StringUtil.isEmpty(platform)) {
            return false;
        }
        Integer verint = Integer.valueOf(version.replaceAll("\\.", ""));
        if ((platform.equals(AppPlatform.IOS.getCode() + "") && verint >= 203) || (platform.equals(AppPlatform.ANDROID.getCode() + "") && verint >= 204)) {
            return true;
        }
        return false;
    }

    public static String appendParam(String url, String paramString) {
       return url.indexOf("?") >= 0 ? url + "&" + paramString : url + "?" + paramString;
    }


    public static String encodeUrl(String url) throws UnsupportedEncodingException {
        Matcher matcher = RegexUtil.URL_ENCODE_PATTERN.matcher(url);

        StringBuffer resutlStringBuffer = new StringBuffer();

        while (matcher.find()) {
            String text = matcher.group(0);
            text = URLEncoder.encode(text, "UTF-8");
            matcher.appendReplacement(resutlStringBuffer, text);
        }
        matcher.appendTail(resutlStringBuffer);


        return resutlStringBuffer.toString();
    }
}
