/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.controller.profile;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.misc.Region;
import com.enjoyf.platform.service.point.GiftLottery;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.UserLotteryLog;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.joyme.dto.usercenter.UserinfoDTO;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.profile.CustomizeWebLogic;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserCenterWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.cache.SysCommCache;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

/**
 * /customize
 * /customize/{domainid}
 * http://www.XXX.com/customize 个人资料，模板，互动
 * http://www.XXX.com/customize/ziwolf  自定义
 * /customize/baseinfo
 *
 * @author zhaoxin liuhao
 */
@Controller
@RequestMapping(value = "/profile/customize")
public class CustomizeController extends BaseRestSpringController {
    Logger logger = LoggerFactory.getLogger(CustomizeController.class);

    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;

    @Resource(name = "customizeWebLogic")
    private CustomizeWebLogic customizeWebLogic;


    @Resource(name = "userCenterWebLogic")
    private UserCenterWebLogic userCenterWebLogic;
//    /**
//     * 用户设置跳转页
//     *
//     * @return
//     */
//    @RequestMapping
//    public ModelAndView index(HttpServletRequest request, @RequestParam(value = "nav", required = false) String nav) {
//
//        return new ModelAndView("redirect:/profile/customize/basic");
//    }

    //基本设置
    @RequestMapping("/basic")
    public ModelAndView basic(HttpServletRequest request) {

        //TODO 根据需求，暂时跳转到wiki设置页面
        //   return new ModelAndView("redirect:http://wiki." + WebappConfig.get().getDomain() + "/home/%E7%89%B9%E6%AE%8A:%E8%B4%A6%E5%8F%B7%E5%AE%89%E5%85%A8");
//        Map<String, Object> mapMessage = new HashMap<String, Object>();
//
//        UserCenterSession userCenterSession = this.getUserCenterSeesion(request);
//
//        mapMessage.put("nav", "basic");
//        mapMessage.put("profile", userCenterSession);
//
//        //得到省列表
//        mapMessage.put("regionlist", SysCommCache.get().getRegionMap().values());
//
//        if (userCenterSession != null) {
//            mapMessage.put("desc", HtmlUtils.htmlUnescape(userCenterSession.getDescription()));
//        }
//
//        return new ModelAndView("/views/jsp/customize/basic", mapMessage);

        return new ModelAndView("redirect:" + WebappConfig.get().getUrlUc() + "/usercenter/account/safe");
    }

    // 头像设置 todo
    @RequestMapping("/headicon")
    public ModelAndView headicon(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("nav", "headicon");
        //从数据库拿出来新的ProfileBlog，更新session

        return new ModelAndView("/views/jsp/customize/headicon", mapMessage);
    }

    //域名设置
    @RequestMapping("/domain")
    public ModelAndView domain(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("nav", "domain");

        return new ModelAndView("/views/jsp/customize/domain", mapMessage);
    }


    //绑定设置
    @RequestMapping("/bind")
    public ModelAndView bindSetting(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("nav", "bind");
        try {
            Map<String, LoginDomain> syncProviderMap = this.getSupportBindDomain();


            UserCenterSession userSession = getUserCenterSeesion(request);
            Set<LoginDomain> domainSetByFlag = userSession.getFlag().getLoginDomain();

            Set<LoginDomain> bindDomainSet = new HashSet<LoginDomain>();

            for (LoginDomain loginDomain : domainSetByFlag) {
                if (syncProviderMap.containsValue(loginDomain)) {
                    bindDomainSet.add(loginDomain);
                }
            }

            mapMessage.put("bindedSet", bindDomainSet);
            mapMessage.put("syncProviderMap", syncProviderMap);

            String errorCode = request.getParameter("errorcode");
            if (!StringUtil.isEmpty(errorCode)) {
                mapMessage.put("errorcode", errorCode);
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException:", e);
        }
        return new ModelAndView("/views/jsp/customize/bind", mapMessage);
    }


    //修改密码
    @RequestMapping("/pwd")
    public ModelAndView resetpwd(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        UserCenterSession userSession = getUserCenterSeesion(request);
        String uno = userSession.getUno();

        if (!userSession.getFlag().hasPassword()) {
            mapMessage.put("message", "user.userpwd.not.default");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        mapMessage.put("nav", "pwd");
        return new ModelAndView("/views/jsp/customize/pwd", mapMessage);
    }

    //邮箱管理
    @RequestMapping("/email")
    public ModelAndView email(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UserCenterSession userSession = getUserCenterSeesion(request);
        if (!userSession.getFlag().hasFlag(ProfileFlag.FLAG_EMAIL)) {
            mapMessage.put("message", "user.email.not.default");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        UserLogin userLogin = null;
        try {
            Set<LoginDomain> loginDomains = new HashSet<LoginDomain>();
            loginDomains.add(LoginDomain.EMAIL);
            List<UserLogin> userLogins = UserCenterServiceSngl.get().queryUserLoginUno(userSession.getUno(), loginDomains);

            if (!CollectionUtil.isEmpty(userLogins)) {
                userLogin = userLogins.get(0);
            }
        } catch (ServiceException e) {
            logger.error("ajaxResetPwd occured ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        if (userLogin == null) {
            mapMessage.put("message", "user.userpwd.not.default");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        mapMessage.put("nav", "email");
        mapMessage.put("authStatus", userSession.getAccountFlag().hasFlag(AccountFlag.FLAG_EMAIL_VERIFY));
        mapMessage.put("email", userLogin.getLoginKey());
        mapMessage.put("userid", parseEmailStr(userLogin.getLoginKey()));

        return new ModelAndView("/views/jsp/customize/email", mapMessage);
    }

    //修改邮箱
    @RequestMapping("/email/modifypage")
    public ModelAndView emailmodifyPage(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        UserCenterSession userSession = getUserCenterSeesion(request);

        if (!userSession.getFlag().hasFlag(ProfileFlag.FLAG_EMAIL)) {
            mapMessage.put("message", "user.email.not.default");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        UserLogin userLogin = null;
        try {
            Set<LoginDomain> loginDomains = new HashSet<LoginDomain>();
            loginDomains.add(LoginDomain.EMAIL);
            List<UserLogin> userLogins = UserCenterServiceSngl.get().queryUserLoginUno(userSession.getUno(), loginDomains);

            if (!CollectionUtil.isEmpty(userLogins)) {
                userLogin = userLogins.get(0);
            }
        } catch (ServiceException e) {
            logger.error("emailModify occured ServiceException.e: ", e);
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        if (userLogin == null) {
            mapMessage.put("message", "user.userpwd.not.default");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        mapMessage.put("nav", "email");
        mapMessage.put("userLogin", userLogin);
        mapMessage.put("email", parseEmailStr(userLogin.getLoginKey()));
        return new ModelAndView("/views/jsp/customize/email-modify", mapMessage);
    }

    private String parseEmailStr(String userid) {
        if (userid.indexOf("@") != -1) {
            String[] mail = userid.split("@");
            char[] chars = mail[0].toCharArray();
            if (chars.length > 6) {
                return chars[0] + "****" + chars[chars.length - 1] + "@" + mail[1];
            } else {
                return chars[0] + "****" + "@" + mail[1];
            }
        } else {
            return userid;
        }
    }

}
