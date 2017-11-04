package com.enjoyf.webapps.tools.webpage.controller.home;

import com.enjoyf.webapps.tools.weblogic.privilege.PrivilegeWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Author: zhaoxin
 * Date: 11-10-27
 * Time: 上午10:49
 * Desc:
 */
@Controller
public class AccountController extends ToolsBaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @Resource(name = "privilegeWebLogic")
    private PrivilegeWebLogic privilegeWebLogic;

    /**
     * session timeout
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/timeout")
    public ModelAndView timeOut(HttpServletRequest request) {

        logger.debug("timeOut");
        //request.getSession()
        //session timeout 需要处理的事
        return new ModelAndView("/login");

    }


}
