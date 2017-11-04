/**
 *
 */
package com.enjoyf.webapps.joyme;


import com.enjoyf.platform.service.log.LogServiceSngl;
import com.enjoyf.platform.service.log.MobileAccessLog;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


public class MobileAccessInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        process(request, response, handler, null);

        return true;
    }

    //@Override
    public void process(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        try {
            MobileAccessLog log = new MobileAccessLog();
            log.setUno(request.getParameter("uno"));
            log.setIp(HTTPUtil.getRemoteAddr(request));
            log.setAppkey(request.getParameter("appkey"));
            log.setChannel(request.getParameter("channelid"));
            log.setDeviceid(request.getParameter("clientid"));
            log.setPlatform(request.getParameter("platform"));
            log.setUrl(request.getRequestURL().toString());
            log.setVersion(request.getParameter("version"));
            log.setParams(request.getParameterMap());

            LogServiceSngl.get().reportMobileAccessInfo(log);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
        }
    }
}


