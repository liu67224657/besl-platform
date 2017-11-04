/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.util.HTTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录权限验证
 *
 * @author zhaoxin
 */
public class RequestParamInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String redr = HTTPUtil.getRedr(request);
            request.setAttribute("browsersURL", redr);
        } catch (Exception e) {
            logger.error("occured error.",e);
        }
        return true;
    }
}
