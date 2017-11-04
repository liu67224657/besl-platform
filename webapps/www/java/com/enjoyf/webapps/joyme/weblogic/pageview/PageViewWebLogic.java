package com.enjoyf.webapps.joyme.weblogic.pageview;

import com.enjoyf.platform.props.hotdeploy.EventHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.UUIDUtil;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Service(value = "pageViewWebLogic")
public class PageViewWebLogic {
    //
    private static final String KEY_REQUEST_COOKIE_GLOBALID = "pv.gbid";

    private EventHotdeployConfig eventHotdeployConfig = HotdeployConfigFactory.get().getConfig(EventHotdeployConfig.class);

    //
    public String getGlobalIdFromCookie(HttpServletRequest request, HttpServletResponse response) {
        String returnValue = null;

        //get from the request.
        returnValue = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_GLOBALID);

        //if this is the first time viewing.
        if (Strings.isNullOrEmpty(returnValue)) {
            //new the globalid
            returnValue = KEY_REQUEST_COOKIE_GLOBALID + "-" + UUIDUtil.getShortUUID();

            //save to response.
            Cookie globalIdCookie = new Cookie(KEY_REQUEST_COOKIE_GLOBALID, returnValue);

            globalIdCookie.setMaxAge(Integer.MAX_VALUE);
            globalIdCookie.setPath("/");
            globalIdCookie.setDomain(WebUtil.getDomain());

            //
            response.addCookie(globalIdCookie);
        }

        //
        return returnValue;
    }

    public String getUnoBySession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            Object obj = session.getAttribute(Constant.SESSION_USER_INFO);
            if (obj != null) {
                return ((UserSession) obj).getBlogwebsite().getUno();
            }
        }

        return null;
    }

    public void spiderFormat(HttpServletRequest request, PageViewEvent event) {
        //
        String userAgent = request.getHeader("User-agent");
        if(StringUtil.isEmpty(userAgent)){
            return ;
        }

        if (eventHotdeployConfig.isSpiderUserAgent(userAgent)) {
            event.setOs("spider");
        }
    }
}
