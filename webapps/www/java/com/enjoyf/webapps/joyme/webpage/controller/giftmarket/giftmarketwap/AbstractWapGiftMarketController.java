package com.enjoyf.webapps.joyme.webpage.controller.giftmarket.giftmarketwap;

import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.AuthProfile;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.controller.giftmarket.AbstractGiftMarketBaseController;
import com.enjoyf.webapps.joyme.webpage.controller.weixin.WeixinUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/6
 * Description:
 */
public class AbstractWapGiftMarketController extends AbstractGiftMarketBaseController {

    //todo
    protected UserCenterSession getWxUserSession(String openId, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        AuthProfile authProfile = UserCenterServiceSngl.get().auth(openId, LoginDomain.WEIXIN, null, "", "", "wxdyh", getIp(request), new Date(), HTTPUtil.getRequestToMap(request));
        writeAuthCookie(request, response, authProfile, "wxdyh", LoginDomain.WEIXIN);
        return buildUserCenterSession(authProfile, request);
    }


    protected boolean validateOpenIdAndToken(HttpServletRequest request, HttpServletResponse response, String openId, String token) throws ServiceException {
        if (StringUtil.isEmpty(openId) || StringUtil.isEmpty(token)) {
            return false;
        }
        //通过token得到openid
        String saveOpenId = WeixinUtil.getOpenIdByToken(token);
        if (StringUtil.isEmpty(saveOpenId)) {
            GAlerter.lan(this.getClass().getName() + " ==wxwap=== get cookie is null save cookie false.openid" + openId + " uno:" + token);
            return false;
        } else if (!openId.equals(saveOpenId)) {
            GAlerter.lan(this.getClass().getName() + " ==wxwap=== get cookie is openid not eq save cookie false.openid" + openId + " uno:" + token);
            return false;
        }
        return true;
    }
}
