package com.enjoyf.webapps.joyme.weblogic.advertise;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.advertise.AdvertisePublish;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.service.event.system.AdvertisePageViewEvent;
import com.enjoyf.platform.service.event.system.AdvertisePublishClickEvent;
import com.enjoyf.platform.service.event.system.AdvertiseUserRegisterEvent;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import com.enjoyf.webapps.joyme.weblogic.pageview.PageViewWebLogic;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Service(value = "advertiseWebLogic")
public class AdvertiseWebLogic {
    //
    private static final Logger logger = LoggerFactory.getLogger(AdvertiseWebLogic.class);

    //
    private static final String KEY_REQUEST_COOKIE_ADVERTISE_PUBLISHID = "adv.pid";
    private static final String KEY_REQUEST_COOKIE_ADVERTISE_LOCATIONCODE = "adv.lc";
    private static final String DEFAULT_REDIRECT_URL = "http://www.joyme.com/";

    //
    private WebHotdeployConfig webHotdeployConfig = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);

    //
    @Resource(name = "pageViewWebLogic")
    private PageViewWebLogic pageViewWebLogic;

    //
    public String getAdvertisePublishRedirectUrl(
            HttpServletRequest request, HttpServletResponse response, String publishId, String locationCode) {
        String returnValue = null;

        //check the config
        if (!webHotdeployConfig.isAdvertiseClickSupport()) {
            return DEFAULT_REDIRECT_URL;
        }

        //get the url from cache.
        returnValue = getRedirectUrl(publishId);

        /////////////////////////////////////////
        //save to response.
        if (!Strings.isNullOrEmpty(returnValue)) {
            Cookie publishIdCookie = new Cookie(KEY_REQUEST_COOKIE_ADVERTISE_PUBLISHID, publishId);

            publishIdCookie.setMaxAge(-1); //-1, clean the cookie when close broswer.
            publishIdCookie.setPath("/");
            publishIdCookie.setDomain(WebUtil.getDomain());

            //
            response.addCookie(publishIdCookie);

            Cookie JSessionIdCookie = new Cookie("JSESSIONID", request.getSession(true).getId());

            JSessionIdCookie.setMaxAge(-1); //-1, clean the cookie when close broswer.
            JSessionIdCookie.setPath("/");
            JSessionIdCookie.setDomain(WebUtil.getDomain());
            //
            response.addCookie(JSessionIdCookie);

            if (!Strings.isNullOrEmpty(locationCode)) {
                Cookie publishLocationCookie = new Cookie(KEY_REQUEST_COOKIE_ADVERTISE_LOCATIONCODE, locationCode);

                publishLocationCookie.setMaxAge(-1);
                publishLocationCookie.setPath("/");
                publishLocationCookie.setDomain(WebUtil.getDomain());

                //
                response.addCookie(publishLocationCookie);
            } else {
                // 为空 删掉locationcode
                Cookie publishLocationCookie = new Cookie(KEY_REQUEST_COOKIE_ADVERTISE_LOCATIONCODE, locationCode);

                publishLocationCookie.setMaxAge(0);
                publishLocationCookie.setPath("/");
                publishLocationCookie.setDomain(WebUtil.getDomain());

                //
                response.addCookie(publishLocationCookie);
            }

            //sendou the click event.
            AdvertisePublishClickEvent event = new AdvertisePublishClickEvent();

            event.setPublishId(publishId);
            event.setLocationCode(locationCode);

            event.setSessionId(request.getSession(true).getId());
            event.setGlobalId(pageViewWebLogic.getGlobalIdFromCookie(request, response));

            event.setEventIp(HTTPUtil.getRemoteAddr(request));
            event.setEventDate(new Date());

            //
            try {
                EventDispatchServiceSngl.get().dispatch(event);
            } catch (Exception e) {
                //
                GAlerter.lan("AdvertiseWebLogic send out AdvertisePublishClickEvent error.", e);
            }
        }

        //////////////////////////////////////
        return Strings.isNullOrEmpty(returnValue) ? DEFAULT_REDIRECT_URL : returnValue;
    }

//    @Cacheable(value = "advertisePublishCache", key = "#publishId")
    private String getRedirectUrl(String publishId) {
        if (logger.isDebugEnabled()) {
            logger.debug("AdvertiseWebLogic getRedirectUrl from service by publishId:" + publishId);
        }

        //
        AdvertisePublish publish = null;

        try {
            publish = AdvertiseServiceSngl.get().getPublish(publishId);
        } catch (Exception e) {
            GAlerter.lan("AdvertiseWebLogic call AdvertiseService getPublish error.", e);
        }

        return publish == null ? null : publish.getRedirectUrl();
    }

    public void sendAdvertisePageViewEvent(HttpServletRequest request, HttpServletResponse response, PageViewEvent event) {
        //
        String publishId = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_ADVERTISE_PUBLISHID);
        String locationCode = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_ADVERTISE_LOCATIONCODE);

        if (!Strings.isNullOrEmpty(publishId)) {
            //
            AdvertisePageViewEvent advertisePageViewEvent = new AdvertisePageViewEvent();

            advertisePageViewEvent.setLocationCode(locationCode);
            advertisePageViewEvent.setPublishId(publishId);
            advertisePageViewEvent.setPageViewEvent(event);

            //
            try {
                EventDispatchServiceSngl.get().dispatch(advertisePageViewEvent);
            } catch (Exception e) {
                //
                GAlerter.lan("AdvertiseWebLogic send out AdvertisePageViewEvent error.", e);
            }
        }

    }

    public void sendAdvertiseUserRegisterEvent(HttpServletRequest request, HttpServletResponse response,
                                               String profileUno, String ip) {
        //
        String publishId = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_ADVERTISE_PUBLISHID);
        String locationCode = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_ADVERTISE_LOCATIONCODE);

        if (!Strings.isNullOrEmpty(publishId)) {
            //
            AdvertiseUserRegisterEvent advertiseUserRegisterEvent = new AdvertiseUserRegisterEvent();

            advertiseUserRegisterEvent.setLocationCode(locationCode);
            advertiseUserRegisterEvent.setPublishId(publishId);

            advertiseUserRegisterEvent.setUno(profileUno);
            advertiseUserRegisterEvent.setSessionId(request.getSession(true).getId());
            advertiseUserRegisterEvent.setGlobalId(pageViewWebLogic.getGlobalIdFromCookie(request, response));

            advertiseUserRegisterEvent.setEventDate(new Date());
            advertiseUserRegisterEvent.setEventIp(ip);

            //
            try {
                EventDispatchServiceSngl.get().dispatch(advertiseUserRegisterEvent);
            } catch (Exception e) {
                //
                GAlerter.lan("AdvertiseWebLogic send out AdvertisePageViewEvent error.", e);
            }
        }
    }
}
