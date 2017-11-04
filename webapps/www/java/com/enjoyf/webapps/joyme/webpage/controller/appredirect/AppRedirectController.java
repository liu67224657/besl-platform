package com.enjoyf.webapps.joyme.webpage.controller.appredirect;

import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.controller.auth.AbstractAuthController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/12
 * Description:
 */
@Controller
@RequestMapping("/appredirect")
public class AppRedirectController extends AbstractAuthController {


    @RequestMapping("/loginsuccess")
    public ModelAndView loginsuccess(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam(value = "loginkey", required = false, defaultValue = "") String loginKey,
                                     @RequestParam(value = "reurl", required = false, defaultValue = "") String reurl,
                                     @RequestParam(value = "password", required = false, defaultValue = "") String pasword,
                                     @RequestParam(value = "logindomain", required = false, defaultValue = "") String lDomainCode,
                                     @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey) {

        Map mapMessage = new HashMap();
        mapMessage.put("rurl", reurl);
        UserCenterSession userCenterSession = this.getUserCenterSeesion(request);
        mapMessage.put("token", userCenterSession.getToken());
        mapMessage.put("icon", userCenterSession.getIcon());
        mapMessage.put("nick", userCenterSession.getNick());
        mapMessage.put("uid", userCenterSession.getUid());
        mapMessage.put("uno", userCenterSession.getUno());
        mapMessage.put("description", userCenterSession.getDescription());

        return new ModelAndView("/views/jsp/appredirect/loginsuccess", mapMessage);
    }

    @RequestMapping("/tokenfailed")
    public ModelAndView appRedirect(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "loginkey", required = false, defaultValue = "") String loginKey,
                                    @RequestParam(value = "reurl", required = false, defaultValue = "") String reurl,
                                    @RequestParam(value = "password", required = false, defaultValue = "") String pasword,
                                    @RequestParam(value = "logindomain", required = false, defaultValue = "") String lDomainCode,
                                    @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey) {

        Map mapMessage = new HashMap();
        mapMessage.put("rurl", reurl);
        UserCenterSession userCenterSession = this.getUserCenterSeesion(request);
        mapMessage.put("token", userCenterSession.getToken());
        mapMessage.put("icon", userCenterSession.getIcon());
        mapMessage.put("nick", userCenterSession.getNick());
        mapMessage.put("uid", userCenterSession.getUid());
        mapMessage.put("uno", userCenterSession.getUno());
        mapMessage.put("description", userCenterSession.getDescription());

        return new ModelAndView("/views/jsp/appredirect/tokenfailed", mapMessage);
    }
}
