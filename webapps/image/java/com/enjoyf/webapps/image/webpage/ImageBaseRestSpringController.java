package com.enjoyf.webapps.image.webpage;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.webapps.image.webpage.bese.SessionConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * BaseAction定义了一些image web层基础方法
 */
public class ImageBaseRestSpringController {
    protected static final String USER_UPLOAD_PATH = "/upload";
    protected static final String USER_UPLOAD_STATIC_PATH = "/static";

    protected String getStaticRealPath(HttpServletRequest request) {
        return HTTPUtil.getServerBaseUrl(request, USER_UPLOAD_STATIC_PATH);
    }

    protected String getUploadPath() {
        return WebappConfig.get().getUploadRootpath() + USER_UPLOAD_PATH;
    }

    /**
     * 得到ip，（通过Nginx的ip需要从请求头部取）
     *
     * @param request
     * @return
     */
    protected String getIp(HttpServletRequest request) {
        return HTTPUtil.getRemoteAddr(request);
    }
}
