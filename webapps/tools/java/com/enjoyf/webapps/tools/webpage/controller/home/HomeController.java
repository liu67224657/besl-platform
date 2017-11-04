package com.enjoyf.webapps.tools.webpage.controller.home;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.tools.weblogic.privilege.CacheUtil;
import com.enjoyf.webapps.tools.weblogic.privilege.ToolsUserInfo;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-10-27
 * Time: 上午10:32
 * Desc:
 * /home
 * /home/left   左侧工具条
 * /home/main
 */

@Controller
@RequestMapping(value = "/home")
public class HomeController extends ToolsBaseController {

     private Logger logger = LoggerFactory.getLogger(this.getClass());

     /**
     *
     * @param request
     * @return
     */
    @RequestMapping
    public ModelAndView home(HttpServletRequest request) {
        logger.debug("进入 /home");
        return new ModelAndView("/home") ;
    }

    /**
     * 取左侧工具条
     * @param request
     * @return
     */
    @RequestMapping(value = "/left")
    public ModelAndView toolsLeftMenu(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String cookieMessage = CookieUtil.getCookieValue(request, CacheUtil.TOOLS_COOKIEKEY_MESSAGE);
            if (!StringUtil.isEmpty(cookieMessage)) {
                String cookieKey = MD5Util.Md5(CacheUtil.getToolsCookeySecretKey() + cookieMessage);
                ToolsUserInfo toolsUserInfo = CacheUtil.getToolsUserInfoCache(cookieKey);
                if(toolsUserInfo == null){
                    response.sendRedirect("/loginpage");
                }
                map.put("toolsUserInfo", toolsUserInfo);
            }
            logger.debug("进入 /home/left");
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return new ModelAndView("/left", map);
    }

    /**
     * 主界面
     * @param request
     * @return
     */
    @RequestMapping(value = "/main")
    public ModelAndView toolsMain(HttpServletRequest request) {
        logger.debug("进入 /home/main");
        return new ModelAndView("/main") ;
    }

}
