package com.enjoyf.webapps.joyme.webpage.util;

import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.oauth.Sticket;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-25 下午4:56
 * Description:
 */
public class VerifyHttpUtil {
    private static Pattern pattern = Pattern.compile("code=([a-zA-Z0-9\\-_]+)");

    public static void saveTicket(HttpServletRequest request, Sticket st) {
        request.getSession().setAttribute(Constant.SESSION_SESSION_ST, st.getSecr());
        request.setAttribute(Constant.REQUES_KEY_PARAM_REFER_CODE, st.getrCode());
    }

    public static boolean verifyHttpRequest(HttpServletRequest request, String uno) throws ServiceException {

        Sticket sticket = OAuthServiceSngl.get().getSTicket(uno);
        if (sticket == null) {
            GAlerter.lan(VerifyHttpUtil.class.getName() + " sticket is null.uno:" + uno);
            return false;
        }

        return verifyTicket(request, sticket);
    }


    private static boolean verifyTicket(HttpServletRequest request, Sticket st) {
        Object obj = request.getSession().getAttribute(Constant.SESSION_SESSION_ST);
        if (obj == null) {
            GAlerter.lan(VerifyHttpUtil.class.getName() + " SESSION_SESSION_ST is null.");
            return false;
        }
        String stString = (String) obj;

        boolean returnBoolean = stString.equals(st.getSecr());

        if (!returnBoolean) {
            GAlerter.lan(VerifyHttpUtil.class.getName() + " not eq session:" + stString + " st:" + st);
        }

        request.getSession().removeAttribute(Constant.SESSION_SESSION_ST);
        return returnBoolean;
    }

}
