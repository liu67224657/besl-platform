package com.enjoyf.webapps.tools.weblogic.privilege;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ToolsHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.tools.PrivilegeResource;
import com.enjoyf.platform.service.tools.PrivilegeUser;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.tools.webpage.controller.SessionConstants;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 访问每个具体的资源 进行权限过滤。如果用户权限列表没有这个资源的记录,返回没有权限页面。
 *
 * @author zx
 */
public class CheckPrivilegeFilter implements Filter {

    private static final String PARA_SESSIONOUT_URL_NAME = "sessionOutURL";
    private static final String PARA_NOPRIVILEGE_URL_NAME = "noPrivilegeURL";
    private static final String PARA_EXCEPT_FILES_NAME = "exceptFiles";
    private static final String PARA_EXCEPT_FLODER_NAME = "exceptFloder";
    private FilterConfig filterConfig = null;
    private static final String PARA_EXCEPT_FILES_SPLIT_SYMBOL = "#";

    private String exceptFiles = null;  //不过虑的文件
    private String exceptFloder = null;  //不过虑的文件夹路径
    private String sessionOutURL = null;   //session超时的转向地址
    private String noPrivilegeURL = null;
    private static Logger logger = Logger.getLogger(CheckPrivilegeFilter.class);

    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            String reqUri = httpRequest.getRequestURI();
            if (reqUri.startsWith("/cmstools/")) {
                checkPrivilegeByCookie(request, response, filterChain, httpRequest, httpResponse);
            } else {
                checkPrivilegeBySession(request, response, filterChain, httpRequest, httpResponse);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
    }

    private void checkPrivilegeByCookie(ServletRequest request, ServletResponse response, FilterChain filterChain, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
        String contextPath = httpRequest.getContextPath();
        String basePath = httpRequest.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + contextPath;

        String[] exceptFileList = this.exceptFiles.split(PARA_EXCEPT_FILES_SPLIT_SYMBOL);
        for (int i = 0; i < exceptFileList.length; i++) {
            if (httpRequest.getRequestURI().equals(exceptFileList[i])) {
                filterChain.doFilter(request, response); //正常
                return;
            }
        }

        String[] exceptFloderList = this.exceptFloder.split(PARA_EXCEPT_FILES_SPLIT_SYMBOL);
        for (int i = 0; i < exceptFloderList.length; i++) {
            if (httpRequest.getRequestURI().indexOf(exceptFloderList[i]) != -1) {
                filterChain.doFilter(request, response); //正常
                return;
            }
        }

        boolean login = isLoginCookie(httpRequest);
        if (!login) {
            httpResponse.sendRedirect(basePath + this.sessionOutURL + getReurl(httpRequest)); //超时跳转
            return;
        }

        switch (checkPrivilegeCookie(httpRequest)) {
            case 0:
                filterChain.doFilter(request, response); //正常
                break;
            case 1:
                httpResponse.sendRedirect(basePath + this.sessionOutURL + getReurl(httpRequest)); //超时跳转
                break;
            case 2:
                httpRequest.getRequestDispatcher(this.noPrivilegeURL).forward(httpRequest, httpResponse); //没有权限跳转
                break;
            default:
                filterChain.doFilter(request, response); //正常
                break;
        }
    }

    private void checkPrivilegeBySession(ServletRequest request, ServletResponse response, FilterChain filterChain, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
        String contextPath = httpRequest.getContextPath();
        String basePath = httpRequest.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + contextPath;

        switch (checkPrivilegeSession(httpRequest)) {
            case 0:
                filterChain.doFilter(request, response); //正常
                break;
            case 1:
                httpResponse.sendRedirect(basePath + this.sessionOutURL); //超时跳转
                break;
            case 2:
                httpRequest.getRequestDispatcher(this.noPrivilegeURL).forward(httpRequest, httpResponse); //没有权限跳转
                break;
            default:
                filterChain.doFilter(request, response); //正常
                break;
        }
    }

    private int checkPrivilegeSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        requestURI = requestURI.substring(contextPath.length());

        String[] exceptFileList = this.exceptFiles.split(PARA_EXCEPT_FILES_SPLIT_SYMBOL);
        for (int i = 0; i < exceptFileList.length; i++) {
            if (requestURI.equals(exceptFileList[i])) {
                return 0;//0代表正常
            }
        }

        String[] exceptFloderList = this.exceptFloder.split(PARA_EXCEPT_FILES_SPLIT_SYMBOL);
        for (int i = 0; i < exceptFloderList.length; i++) {
            if (requestURI.indexOf(exceptFloderList[i]) != -1) {
                return 0;//0代表正常
            }
        }

        if (session == null || session.getAttribute(SessionConstants.CURRENT_USER) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("session timeout go sessionOutUrl:" + sessionOutURL);
                logger.debug("requestURI:" + requestURI);
            }

            return 1;//1代表session超时
        }


        //过滤的内容：
        Map<?, ?> userprivilege = (Map<?, ?>) session.getAttribute(SessionConstants.USER_SESSION_PRIVILEGE_ID);
        if (userprivilege == null) {
            logger.debug("在过滤" + requestURI + "地址时,用户session中没有取到用户权限集合,跳转到" + sessionOutURL);
            return 2;
        }
        int index = requestURI.indexOf('?');
        if (index != -1) {
            requestURI = requestURI.substring(0, index);
        }

        if (requestURI.startsWith("/")) {
            requestURI = requestURI.substring(1);
        }

        PrivilegeResource prs = null;
        Set<?> keySet = CacheUtil.getSysRsUrlO().keySet();
        for (Object o : keySet) {
            //compKey 用于临时比较地址
            String key = (String) o;
            String compKey = key;
            if (compKey.startsWith("/")) {
                compKey = compKey.substring(1);
            }

//            if (compKey != null && (compKey.indexOf(requestURI) != -1 || requestURI.equals(compKey))) {
            if (compKey != null && (requestURI.equals(compKey))) {
                prs = CacheUtil.getSysRsByURL(key);
                break;
            }
        }

        if (prs == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("在过滤" + requestURI + "地址时,系统缓存中不存在这个地址的编号,跳转到" + sessionOutURL);
            }
            return 2; // 没有通过URL取到ACTION NO
        }

        if (userprivilege.get(prs.getRsid()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("在过滤" + requestURI + "地址时,用户session中取到用户权限集合不包含这个地址,跳转到" + sessionOutURL);
            }
            return 2;//2代表没有权限
        }

        PrivilegeUser currentUser = (PrivilegeUser) session.getAttribute(SessionConstants.CURRENT_USER);
        if (currentUser.getLimitLocation().getCode().equals(ActStatus.ACTED.getCode())) {
            if (!matcheUserConfig(HTTPUtil.getRemoteAddr(request))) {
                if (logger.isDebugEnabled()) {
                    logger.debug("所访问资源未开放给此用户： " + currentUser.getUsername() + " ip:" + HTTPUtil.getRemoteAddr(request));
                }
                return 2;
            }

        }

        return 0;
    }

    public static boolean isLoginCookie(HttpServletRequest request) {
        String messgae = CookieUtil.getCookieValue(request, CacheUtil.TOOLS_COOKIEKEY_MESSAGE);
        String encrypt = CookieUtil.getCookieValue(request, CacheUtil.TOOLS_COOKIEKEY_ENCRYPT);
        if (com.enjoyf.util.StringUtil.isEmpty(messgae) || com.enjoyf.util.StringUtil.isEmpty(encrypt)) {
            return false;
        }

        String utfMesage = null;
        try {
            utfMesage = java.net.URLDecoder.decode(messgae, "utf-8");
            String marticleEncrypt = com.enjoyf.util.MD5Util.Md5(CacheUtil.getToolsCookeySecretKey() + messgae);

            if (!encrypt.equals(marticleEncrypt)) {
                return false;
            }

            String roid[] = utfMesage.split("\\|");
            //时间是否过期
            if (roid.length >= 5) {
                long setCookieTime = Long.valueOf(roid[4]);
                if (System.currentTimeMillis() - setCookieTime > CacheUtil.MEM_CACHE_TIME_OUT * 1000L) {
                    return false;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getReurl(HttpServletRequest httpRequest) {
        String reuri = httpRequest.getRequestURI();
        if (reuri.startsWith("/cmstools/")) {
            return "?reurl=" + httpRequest.getRequestURL().toString();
        }
        return "";
    }

    public void init(FilterConfig filterConfig) throws ServletException {

        this.filterConfig = filterConfig;
        this.sessionOutURL = filterConfig.getInitParameter(PARA_SESSIONOUT_URL_NAME);
        this.noPrivilegeURL = filterConfig.getInitParameter(PARA_NOPRIVILEGE_URL_NAME);

        if (this.sessionOutURL == null) {
            this.sessionOutURL = "/";
        }

        this.exceptFiles = filterConfig.getInitParameter(PARA_EXCEPT_FILES_NAME);
        if (this.exceptFiles == null) {
            this.exceptFiles = "";
        }

        this.exceptFloder = filterConfig.getInitParameter(PARA_EXCEPT_FLODER_NAME);
        if (this.exceptFloder == null) {
            this.exceptFloder = "";
        }
    }


    private int checkPrivilegeCookie(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        requestURI = requestURI.substring(contextPath.length());

        String cookieMessage = CookieUtil.getCookieValue(request, CacheUtil.TOOLS_COOKIEKEY_MESSAGE);
        if (StringUtil.isEmpty(cookieMessage)) {
            if (logger.isDebugEnabled()) {
                logger.debug("session timeout go sessionOutUrl:" + sessionOutURL);
                logger.debug("requestURI:" + requestURI);
            }

            return 1;//1代表session超时
        }
        String cookieKey = MD5Util.Md5(CacheUtil.getToolsCookeySecretKey() + cookieMessage);
        ToolsUserInfo toolsUserInfo = CacheUtil.getToolsUserInfoCache(cookieKey);
        if (toolsUserInfo == null) {
            logger.debug("在过滤" + requestURI + "地址时,用户session中没有取到用户权限集合,跳转到" + sessionOutURL);
            return 1;
        }

        //过滤的内容：
        Map<?, ?> userprivilege = toolsUserInfo.getPrivilegeIds();//Map<?, ?>) session.getAttribute(SessionConstants.USER_SESSION_PRIVILEGE_ID);
        if (userprivilege == null) {
            logger.debug("在过滤" + requestURI + "地址时,用户session中没有取到用户权限集合,跳转到" + sessionOutURL);
            return 2;
        }
        int index = requestURI.indexOf('?');
        if (index != -1) {
            requestURI = requestURI.substring(0, index);
        }

        if (requestURI.startsWith("/")) {
            requestURI = requestURI.substring(1);
        }

        PrivilegeResource prs = null;
        Set<?> keySet = CacheUtil.getSysRsUrlO().keySet();
        for (Object o : keySet) {
            //compKey 用于临时比较地址
            String key = (String) o;
            String compKey = key;
            if (compKey.startsWith("/")) {
                compKey = compKey.substring(1);
            }

//            if (compKey != null && (compKey.indexOf(requestURI) != -1 || requestURI.equals(compKey))) {
            if (compKey != null && (requestURI.equals(compKey))) {
                prs = CacheUtil.getSysRsByURL(key);
                break;
            }
        }

        if (prs == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("在过滤" + requestURI + "地址时,系统缓存中不存在这个地址的编号,跳转到" + sessionOutURL);
            }
            return 2; // 没有通过URL取到ACTION NO
        }

        if (userprivilege.get(prs.getRsid()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("在过滤" + requestURI + "地址时,用户session中取到用户权限集合不包含这个地址,跳转到" + sessionOutURL);
            }
            return 2;//2代表没有权限
        }

        PrivilegeUser currentUser = toolsUserInfo.getPrivilegeUser();//(PrivilegeUser) session.getAttribute(SessionConstants.CURRENT_USER);
        if (currentUser.getLimitLocation().getCode().equals(ActStatus.ACTED.getCode())) {
            if (!matcheUserConfig(HTTPUtil.getRemoteAddr(request))) {
                if (logger.isDebugEnabled()) {
                    logger.debug("所访问资源未开放给此用户： " + currentUser.getUsername() + " ip:" + HTTPUtil.getRemoteAddr(request));
                }
                return 2;
            }

        }

        return 0;
    }

    private boolean matcheUserConfig(String charactors) {

        Set<Pattern> regexes = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class).getRegexPatterns();
        if (CollectionUtil.isEmpty(regexes)) {
            return true;
        }

        Iterator<Pattern> iterator = regexes.iterator();
        while (iterator.hasNext()) {
            Pattern p = iterator.next();
            if (p.matcher(charactors).matches()) {
                return true;
            }
        }
        return false;
    }


    public void destroy() {
        this.filterConfig = null;
        this.exceptFiles = null;
        this.noPrivilegeURL = null;
        this.sessionOutURL = null;
    }
}
