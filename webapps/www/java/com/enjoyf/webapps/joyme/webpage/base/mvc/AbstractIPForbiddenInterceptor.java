/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.misc.IpForbidden;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.IpUtil;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.joyme.weblogic.misc.MiscWebLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.UnknownHostException;

/**
 * <p/>
 * Description: IP限制的抽象类
 * </p>
 *
 * @author: zx
 */
public abstract class AbstractIPForbiddenInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(AbstractIPForbiddenInterceptor.class);


    @Resource(name = "miscWebLogic")
    private MiscWebLogic miscWebLogic;

    /**
     * 进入方法之前验证
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("ip Forbidden AbstractIPForbiddenInterceptor.");
        }
        //ip
        if (ipForbidden(request, response)) {
            CookieUtil.deleteALLCookies(request, response);
            request.getSession().invalidate();
            handleForbidLogin(request, response);
            return false;
        }

        return true;
    }


    private boolean ipForbidden(HttpServletRequest request, HttpServletResponse response) {

        //ip
        long ipValue = 0;
        try {
            ipValue = IpUtil.cvtToLong(HTTPUtil.getRemoteAddr(request));
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //todo 遍历，有没有更快速的办法？
        for (IpForbidden ipForbidden : miscWebLogic.queryIpForbiddens("all")) {
            if (ipValue <= ipForbidden.getEndIp() && ipValue >= ipForbidden.getStartIP()) {

                return true;
            }
        }
        return false;
    }

    protected abstract void handleForbidPost(HttpServletRequest request, HttpServletResponse response);

    protected abstract void handleForbidLogin(HttpServletRequest request, HttpServletResponse response);

}
