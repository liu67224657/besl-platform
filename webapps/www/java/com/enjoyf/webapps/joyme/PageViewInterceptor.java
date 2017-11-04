/**
 *
 */
package com.enjoyf.webapps.joyme;


import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.joyme.weblogic.advertise.AdvertiseWebLogic;
import com.enjoyf.webapps.joyme.weblogic.pageview.PageViewWebLogic;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Date;
import java.util.UUID;


public class PageViewInterceptor extends HandlerInterceptorAdapter {
    //
    private static final String KEY_REQUEST_HEADER_REFERER = "referer";

    //
    private static final String KEY_REQUEST_PARAM_REFERER_ID = "rid";
    private static final String KEY_REQUEST_PARAM_SUB_REFERER_ID = "srid";

    private static final String KEY_REQUEST_PARAM_LOCATION_ID = "lid";

    private static final String KEY_REQUEST_COOKIE_OS = "pv.os";
    private static final String KEY_REQUEST_COOKIE_EXPLORE_TYPE = "pv.et";
    private static final String KEY_REQUEST_COOKIE_EXPLORE_VERSION = "pv.ev";
    private static final String KEY_REQUEST_COOKIE_SCREEN = "pv.scn";

    //
    private static final String KEY_HEADER_MOBILE_CLIENT_TYPE = "m_client_type";
    private static final String KEY_HEADER_MOBILE_HD_TYPE = "m_hd_type";
    private static final String KEY_HEADER_MOBILE_OS_VERSION = "m_os_version";
    private static final String KEY_HEADER_MOBILE_SERIAL = "m_serial";

    // adv
    private static final String KEY_REQUEST_COOKIE_ADVERTISE_PUBLISHID = "adv.pid";
    private static final String KEY_REQUEST_COOKIE_ADVERTISE_LOCATIONCODE = "adv.lc";


    private static final String KEY_REQUEST_COOKIE_STAT_GOLBALID = "jmst_glid";
    private static final String KEY_REQUEST_COOKIE_STAT_SESSIONID = "jmst_ssid";


    //
    private Logger logger = LoggerFactory.getLogger(PageViewInterceptor.class);

    @Resource(name = "pageViewWebLogic")
    private PageViewWebLogic pageViewWebLogic;

    @Resource(name = "advertiseWebLogic")
    private AdvertiseWebLogic advertiseWebLogic;

    //
    private WebHotdeployConfig webHotdeployConfig = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);

    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, java.lang.Object handler) throws java.lang.Exception {
//        process(request, response, handler, null);

        //设置cookie
        addId(request, response);

        return true;
    }


    private void addId(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        Cookie gobalCookie = CookieUtil.getCookie(request, KEY_REQUEST_COOKIE_STAT_GOLBALID);
        if (gobalCookie == null) {
            CookieUtil.setCookie(request, response, KEY_REQUEST_COOKIE_STAT_GOLBALID, Md5Utils.md5(UUID.randomUUID().toString()));
        }

        Cookie sessionCookie = CookieUtil.getCookie(request, KEY_REQUEST_COOKIE_STAT_SESSIONID);
        if (sessionCookie == null) {
            CookieUtil.setCookie(request, response, KEY_REQUEST_COOKIE_STAT_SESSIONID, request.getSession(true).getId(), -1);
        }
    }

    //@Override
//    public void process(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        if (ex == null && webHotdeployConfig.isPageViewSupport()) {
//            try {
//                //
//                PageViewEvent event = new PageViewEvent();
//
//                //the classic web browser.
//                if (Strings.isNullOrEmpty(request.getHeader(KEY_HEADER_MOBILE_CLIENT_TYPE))) {
//                    //get the request url and the referer
//                    String locationUrl = HTTPUtil.getRequestedUrl(request);
//                    String locationId = request.getParameter(KEY_REQUEST_PARAM_LOCATION_ID);
//
//                    //
//                    String publishId = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_ADVERTISE_PUBLISHID);
//                    String locationCode = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_ADVERTISE_LOCATIONCODE);
//
//                    //fullfill the pageview event then send it to the event server.
//                    //in the event server, the queue thread will check the referid and the pageid,
//                    //then insert it into the db.
//                    //the client info.
//                    event.setLocationId(locationId);
//                    event.setLocationUrl(locationUrl);
//
//                    //the client info
//                    String os = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_OS);
//                    String exploreVersion = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_EXPLORE_VERSION);
//                    String explorerType = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_EXPLORE_TYPE);
//                    String screen = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_SCREEN);
//
//                    if (!StringUtil.isEmpty(os)) {
//                        event.setOs(URLDecoder.decode(os, "UTF-8"));
//                    }
//                    if (!StringUtil.isEmpty(exploreVersion)) {
//                        event.setExplorerVersion(URLDecoder.decode(exploreVersion, "UTF-8"));
//                    }
//                    if (!StringUtil.isEmpty(explorerType)) {
//                        event.setExplorerType(URLDecoder.decode(explorerType, "UTF-8"));
//                    }
//                    if (!StringUtil.isEmpty(screen)) {
//                        event.setScreen(URLDecoder.decode(screen, "UTF-8"));
//                    }
//
//                    //the user info
//                    event.setSessionId(request.getSession(true).getId());
//                    event.setGlobalId(pageViewWebLogic.getGlobalIdFromCookie(request, response));
//                    event.setUno(pageViewWebLogic.getUnoBySession(request));
//
//                    //the referer
//                    String referer = request.getHeader(KEY_REQUEST_HEADER_REFERER);
//                    String refererId = request.getParameter(KEY_REQUEST_PARAM_REFERER_ID);
//                    String subRefererId = request.getParameter(KEY_REQUEST_PARAM_SUB_REFERER_ID);
//
//                    event.setRefer(referer);
//                    event.setReferId(refererId);
//                    event.setSubReferId(subRefererId);
//
//                    //the ip dand date
//                    event.setEventIp(HTTPUtil.getRemoteAddr(request));
//                    event.setEventDate(new Date());
//
//                    //set adv param
//                    event.setPublishId(publishId);
//                    event.setLocationCode(locationCode);
//
//                    //
//                    pageViewWebLogic.spiderFormat(request, event);
//                } else {
//                    //the mobile client
//                    //get the request url and the referer
//                    String locationUrl = HTTPUtil.getRequestedUrl(request);
//                    String locationId = request.getParameter(KEY_REQUEST_PARAM_LOCATION_ID);
//
//                    //fullfill the pageview event then send it to the event server.
//                    //in the event server, the queue thread will check the referid and the pageid,
//                    //then insert it into the db.
//                    //the client info.
//                    event.setLocationId(locationId);
//                    event.setLocationUrl(locationUrl);
//
//                    //the client info
//                    event.setOs(request.getHeader(KEY_HEADER_MOBILE_CLIENT_TYPE));
//                    event.setExplorerType(request.getHeader(KEY_HEADER_MOBILE_OS_VERSION));
//                    event.setScreen(request.getHeader(KEY_HEADER_MOBILE_HD_TYPE));
//
//                    //the user info
//                    //event.setSessionId(request.getSession(true).getId());
//                    event.setGlobalId(request.getHeader(KEY_HEADER_MOBILE_SERIAL));
//                    //event.setUno(pageViewWebLogic.getUnoBySession(request));
//
//                    //the referer
//                    String referer = request.getHeader(KEY_REQUEST_HEADER_REFERER);
//                    String refererId = request.getParameter(KEY_REQUEST_PARAM_REFERER_ID);
//                    String subRefererId = request.getParameter(KEY_REQUEST_PARAM_SUB_REFERER_ID);
//
//                    event.setRefer(referer);
//                    event.setReferId(refererId);
//                    event.setSubReferId(subRefererId);
//
//                    //the ip dand date
//                    event.setEventIp(HTTPUtil.getRemoteAddr(request));
//                    event.setEventDate(new Date());
//                }
//
//                //log the page view event.
//                if (logger.isDebugEnabled()) {
//                    logger.debug("PageViewInterceptor, pageview event:" + event);
//                }
//
//                //write to event.
//                EventServiceSngl.get().reportPageViewEvent(event);
//
//            } catch (Exception e) {
//                //
//                GAlerter.lab("PageViewController call the EventService to reportPageViewEvent error.", e);
//            }
//        }
//    }
}


