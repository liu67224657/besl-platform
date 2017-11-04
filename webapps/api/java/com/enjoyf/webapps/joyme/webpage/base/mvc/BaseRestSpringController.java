package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ResourceDomainHotdeployConfig;
import com.enjoyf.platform.util.HTTPUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * BaseAction定义了一些web层基础方法
 */
public class BaseRestSpringController {
    protected static final String KEY_AJAX_RESULT_FLAG = "returnFlag";
    protected static final String KEY_AJAX_RESULT_MESSAGE = "errorMsg";

    private static ResourceDomainHotdeployConfig resourceDomainHotdeployConfig = HotdeployConfigFactory.get().getConfig(ResourceDomainHotdeployConfig.class);


    /**
     * 得到ip，（通过Nginx的ip需要从请求头部取）
     *
     * @param request
     * @return
     */
    protected String getIp(HttpServletRequest request) {
        return HTTPUtil.getRemoteAddr(request);
    }

    protected Map<String, Object> putErrorMessage(String errorMessage) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("message", errorMessage);
        return mapMessage;
    }


}
