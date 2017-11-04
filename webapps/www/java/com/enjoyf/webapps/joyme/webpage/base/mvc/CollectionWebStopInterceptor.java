package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 15-5-18
 * Time: 下午10:59
 * To change this template use File | Settings | File Templates.
 */
public class CollectionWebStopInterceptor extends AbstractStopInterceptor {

    private WebHotdeployConfig config = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (!config.getCollectionTrigger()) {
            handlerFailed(httpServletRequest, httpServletResponse);
            return false;
        }
        return true;
    }
}
