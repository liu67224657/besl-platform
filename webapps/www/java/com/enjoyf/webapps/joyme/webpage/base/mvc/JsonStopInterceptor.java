package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 15-5-18
 * Time: 下午10:59
 * To change this template use File | Settings | File Templates.
 */
public class JsonStopInterceptor extends AbstractStopInterceptor {

    protected void handlerFailed(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String callback = request.getParameter("callback");
        if (callback == null || StringUtil.isEmpty(callback)) {
            HTTPUtil.writeJson(response, ResultCodeConstants.SYSTEM_MAINTENANCE.getJsonString());
        } else {
            String callbackStr = callback + "([" +  ResultCodeConstants.SYSTEM_MAINTENANCE.getJsonString() + "])";
            HTTPUtil.writeJson(response, callbackStr);
        }
    }
}
