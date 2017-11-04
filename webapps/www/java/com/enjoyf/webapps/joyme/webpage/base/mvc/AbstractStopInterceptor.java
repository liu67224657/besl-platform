package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.util.http.AppUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 15-5-18
 * Time: 下午10:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractStopInterceptor extends HandlerInterceptorAdapter {

    private WebHotdeployConfig config = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);

    protected void handlerFailed(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        if (AppUtil.checkIsIOS(request) || AppUtil.checkIsAndroid(request)) {
            request.setAttribute("message", "userlogin.not.open");
            request.getRequestDispatcher("/views/jsp/common/custompage-stop.jsp").forward(request, response);
        } else {
            request.setAttribute("message", "userlogin.not.open");
            request.getRequestDispatcher("/views/jsp/common/custompage-stop.jsp").forward(request, response);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (!config.getGiftmakretTrigger()) {
            handlerFailed(httpServletRequest, httpServletResponse);
            return false;
        }
        return true;
    }


}
