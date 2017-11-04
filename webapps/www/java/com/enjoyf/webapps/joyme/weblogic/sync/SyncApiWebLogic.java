package com.enjoyf.webapps.joyme.weblogic.sync;

import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.thirdapi.oauth.IOauthApi;
import com.enjoyf.platform.thirdapi.oauth.OAuthInterfaceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.webapps.joyme.weblogic.advertise.AdvertiseWebLogic;
import com.enjoyf.webapps.joyme.weblogic.oauth.OAuthWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@Service(value = "syncApiWebLogic")
public class SyncApiWebLogic extends BaseRestSpringController {
    Logger logger = LoggerFactory.getLogger(SyncApiWebLogic.class);

    @Resource(name = "advertiseWebLogic")
    private AdvertiseWebLogic advertiseWebLogic;

    @Resource(name = "oauthWebLogic")
    private OAuthWebLogic oauthWebLogic;

    @Deprecated
    public String getAuthorizeUrl(AccountDomain provider, AuthVersion version, AuthUrlParam param) throws ServiceException {
        IOauthApi oauthInterface = OAuthInterfaceSngl.get().getApiByProvider(provider, version);
        return oauthInterface.getAuthorizeUrl(param);
    }


    public String getAuthorizeUrl(LoginDomain loginDomain, AuthVersion version, AuthUrlParam param) throws ServiceException {
        IOauthApi oauthInterface = OAuthInterfaceSngl.get().getApiByProvider(loginDomain, version);
        return oauthInterface.getAuthorizeUrl(param);
    }

    /**
     * 将用户从session中取出
     *
     * @param request
     * @return
     */
    protected UserSession getUserBySession(HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(Constant.SESSION_USER_INFO);
        if (obj != null) {
            return (UserSession) obj;
        }
        return null;
    }

    protected Map<String, Object> putErrorMessage(String errorMessage) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("message", errorMessage);
        return mapMessage;
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
