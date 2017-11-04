package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
public class HearderMenuInterceptorAdapter extends HandlerInterceptorAdapter {

    private String flag;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StringUtil.isEmpty(flag)) {
            request.getSession().removeAttribute(Constant.SESSION_HEARDER_MENU_FLAG);
            return true;
        }

        HeaderMenu menu = HeaderMenu.getByCode(flag);

        if (menu != null) {
            request.getSession().setAttribute(Constant.SESSION_HEARDER_MENU_FLAG, menu.getCode());
        } else {
            request.getSession().setAttribute(Constant.SESSION_HEARDER_MENU_FLAG, HeaderMenu.HEAD_MENU_HOME.getCode());
        }

        request.getSession().removeAttribute(Constant.SESSION_REGHT_MENU_FLAG);
        return true;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
