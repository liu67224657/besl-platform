package com.enjoyf.webapps.joyme.webpage.controller.pv;

import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.weblogic.advertise.AdvertiseWebLogic;
import com.enjoyf.webapps.joyme.weblogic.pageview.PageViewWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <pre>
 * /register
 * http://www.XXX.com/pv 统计写log
 * </pre>
 * <p/>
 * pv统计的action
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@Controller
@RequestMapping(value = "/pv")
public class PageViewController extends BaseRestSpringController {

    @Resource(name = "pageViewWebLogic")
    private PageViewWebLogic pageViewWebLogic;

    // adv
    private static final String KEY_REQUEST_COOKIE_ADVERTISE_PUBLISHID = "adv.pid";
    private static final String KEY_REQUEST_COOKIE_ADVERTISE_LOCATIONCODE = "adv.lc";

    @Deprecated
    @RequestMapping
    public void writeLog(HttpServletRequest request, HttpServletResponse response) {
        try {
            //
            String refer = request.getParameter("ref");
            String app = request.getParameter("app");
            AtomicReference<String> browserVersion = new AtomicReference<String>(request.getParameter("browserVersion"));
            String os = request.getParameter("os");
            String screen = request.getParameter("screen");
            String locationId = request.getParameter("fid");
            String refererId = request.getParameter("rid");

            //
            String publishId = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_ADVERTISE_PUBLISHID);
            String locationCode = HTTPUtil.getCookieValue(request, KEY_REQUEST_COOKIE_ADVERTISE_LOCATIONCODE);

            //
            PageViewEvent event = new PageViewEvent();

            //the location info
            event.setLocationUrl(HTTPUtil.getRequestedUrl(request));
            event.setLocationId(locationId);

            //
            event.setExplorerType(app);
            event.setExplorerVersion(browserVersion.get());
            event.setOs(os);
            event.setScreen(screen);

            //
            event.setSessionId(request.getSession(true).getId());
            event.setGlobalId(pageViewWebLogic.getGlobalIdFromCookie(request, response));
            event.setUno(pageViewWebLogic.getUnoBySession(request));

            //
            event.setRefer(refer);
            event.setReferId(refererId);

            //
            event.setEventIp(getIp(request));
            event.setEventDate(new Date());

            //set adv param
            event.setPublishId(publishId);
            event.setLocationCode(locationCode);

            //
            pageViewWebLogic.spiderFormat(request, event);

            //
            EventServiceSngl.get().reportPageViewEvent(event);

        } catch (Exception e) {
            //
            GAlerter.lab("PageViewController call the EventService to reportPageViewEvent error.", e);
        }
    }


    @RequestMapping(value = "/report")
    public void report(HttpServletRequest request, HttpServletResponse response) {
        //只种cookie
    }
}
