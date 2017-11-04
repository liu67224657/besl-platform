/**
 * CopyRight 2012 joyme.com
 */
package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.encode.EncodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 12-4-13
 * Time: 下午4:02
 * Desc:
 */
public class IPForbiddenInterceptor extends AbstractIPForbiddenInterceptor {

    private Logger logger = LoggerFactory.getLogger(IPForbiddenInterceptor.class);

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @Override
    protected void handleForbidPost(HttpServletRequest request, HttpServletResponse response) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void handleForbidLogin(HttpServletRequest request, HttpServletResponse response) {
        try {

            String redr = EncodeUtils.urlDecode(HTTPUtil.getRedr(request)) != null ? EncodeUtils.urlDecode(HTTPUtil.getRedr(request)).trim() : null;
            if (redr.endsWith("loginpage")|| redr.endsWith("login")) {
                request.setAttribute("ipMessage", i18nSource.getMessage("user.ipforbidden.forbidlogin", new Object[]{}, Locale.CHINA));
                redr = "/loginpage";
            } else if(redr.endsWith("registerpage")|| redr.endsWith("register")){
                request.setAttribute("ipMessage", i18nSource.getMessage("user.ipforbidden.forbidregister", new Object[]{}, Locale.CHINA));
                redr = "/registerpage";
            } else {
                request.setAttribute("ipMessage", i18nSource.getMessage("user.ipforbidden.forbidlogin", new Object[]{}, Locale.CHINA));
                redr = "/index";
            }

            request.getRequestDispatcher(redr).forward(request, response);

        } catch (Exception e) {
            logger.error("handleForbidPost sendRedirect occured IOException:", e);
        }
    }
}
