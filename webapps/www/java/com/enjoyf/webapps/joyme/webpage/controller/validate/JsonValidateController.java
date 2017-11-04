package com.enjoyf.webapps.joyme.webpage.controller.validate;

import com.enjoyf.platform.service.profile.ProfileActiveStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.html.ValidateImageUtil;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.webapps.joyme.weblogic.blogwebsite.BlogWebSiteWebLogic;
import com.enjoyf.webapps.joyme.weblogic.user.UserPrivilageWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import com.enjoyf.webapps.joyme.webpage.util.SystemWordFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


/**
 * <p/>
 * Description:验证的Action多用于ajax调用
 * /json/validate/userexists  ajax验证用户是否存在，发送json格式
 * /json/validate/blogexists  ajax验证域名是否存在，发送json格式
 * <p/>
 * <p/>
 * <p/>
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
@Controller
@RequestMapping("/json/validate")
public class JsonValidateController extends BaseRestSpringController {
    Logger logger = LoggerFactory.getLogger(JsonValidateController.class);

    private JsonBinder binder = JsonBinder.buildNormalBinder();


    @Resource(name = "blogWebSiteWebLogic")
    private BlogWebSiteWebLogic blogWebSiteWebLogic;


    @Resource(name = "userPrivilageWebLogic")
    private UserPrivilageWebLogic privilageService;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    /**
     * ajax验证用户是否存在，发送json格式
     *
     * @return
     */
    @RequestMapping(value = "/userexists", method = RequestMethod.POST)
    @ResponseBody
    public String jsonUserExists(@RequestParam(value = "userid") String userid) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);

        {
            msg.setStatus_code(JoymeResultMsg.CODE_E);
            logger.debug("jsonUserExists:" + userid);
        }

        return binder.toJson(msg);

    }

    /**
     * ajax验证用户是否存在，发送json格式
     *
     * @return
     */
    @RequestMapping(value = "/vcode", method = RequestMethod.POST)
    @ResponseBody
    public String jsonValidateCode(@RequestParam(value = "vcode") String vocde, HttpServletRequest request) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);

        if (StringUtil.isEmpty(vocde) || !ValidateImageUtil.checkImage(vocde, request)) {
            msg.setMsg(i18nSource.getMessage("imgcode.error", null, null));
            msg.setStatus_code(JoymeResultMsg.CODE_E);
        }

        return binder.toJson(msg);

    }

    @RequestMapping("/blogexists")
    @ResponseBody
    public String jsonValidSysDomain(HttpServletRequest request, @RequestParam(value = "domain") String domain, @RequestParam(value = "uno", required = false) String uno) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        UserCenterSession userCenterSession = getUserCenterSeesion(request);

        if (userCenterSession != null && userCenterSession.getDomain().equalsIgnoreCase(domain)) {
            return binder.toJson(msg);
        }

        if (ContextFilterUtils.checkDomainFormulaRegexs(domain.trim())) {//合法的域名
            if (ContextFilterUtils.checkDomainBlackList(domain.trim())) {
                //含有保留字
                msg.setStatus_code(JoymeResultMsg.CODE_S);
                msg.setMsg("user.blogdomain.illegl");
                return binder.toJson(msg);
            }
        } else {
            msg.setStatus_code(JoymeResultMsg.CODE_S);
            msg.setMsg("user.blogdomain.illegl");
            return binder.toJson(msg);
        }

        Profile profile = null;
        try {
            profile = UserCenterServiceSngl.get().getProfileByDomain(domain);

            if (profile != null) {
                msg.setStatus_code(JoymeResultMsg.CODE_S);
                msg.setMsg("user.blogdomain.exists");
                return binder.toJson(msg);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException:", e);

        }
        return binder.toJson(msg);
    }

    @RequestMapping("/nicknameexists")
    @ResponseBody
    public String validSysName(HttpServletRequest request, @RequestParam(value = "nickname") String nickname, @RequestParam(value = "uno", required = false) String uno) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        UserCenterSession userCenterSession = getUserCenterSeesion(request);

        if (userCenterSession != null && userCenterSession.getNick().equals(nickname)) {
            return binder.toJson(msg);
        }

        //过滤关键词

        if (!ContextFilterUtils.checkNickNames(nickname) || ContextFilterUtils.checkNickNamesBlackList(nickname)) {
            msg.setStatus_code(JoymeResultMsg.CODE_S);
            msg.setMsg("user.word.illegl");
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nickname);

            if (profile != null) {
                msg.setStatus_code(JoymeResultMsg.CODE_S);
                msg.setMsg("user.blogname.exists");
                logger.debug("user.blogdomain.exists:" + nickname);
                return binder.toJson(msg);
            }

        } catch (ServiceException e) {
            logger.error("blogDomainExists", e);
        }

        return binder.toJson(msg);

    }

    @RequestMapping("/checkNickName")
    @ResponseBody
    public String checkNickName(@RequestParam(value = "nickname") String nickname, HttpServletRequest request) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        String[] str = nickname.split(" ");
        if (str.length > 1) {
            msg.setStatus_code(JoymeResultMsg.CODE_E);
            msg.setMsg("user.word.illegl");
            return binder.toJson(msg);
        }
        //过滤关键词
        UserSession userSession = getUserBySession(request);
        if (!userSession.getBlogwebsite().getScreenName().equals(nickname)) {
            if (!SystemWordFilter.checkNiCheng(nickname) || !ContextFilterUtils.checkNickNames(nickname) || ContextFilterUtils.checkNickNamesBlackList(nickname)) {
                msg.setStatus_code(JoymeResultMsg.CODE_E);
                msg.setMsg("user.word.illegl");
                logger.debug("含有非法字符-----:" + nickname);
            }
        }
        return binder.toJson(msg);
    }

    @RequestMapping("/pwd")
    @ResponseBody
    public String checkPwd(@RequestParam(value = "userpwd") String pwd) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        String[] str = pwd.split(" ");
        if (str[0].length() < pwd.length() || str.length > 1) {
            msg.setStatus_code(JoymeResultMsg.CODE_E);
            msg.setMsg("user.userpwd.illegl");
            logger.debug("中间有空格-----:" + pwd);
            return binder.toJson(msg);
        }
        return binder.toJson(msg);
    }

    @RequestMapping("/desc")
    @ResponseBody
    public String checkDesc(@RequestParam(value = "desc") String desc) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        //过滤关键词
        if (!SystemWordFilter.checkNiCheng(desc) || !ContextFilterUtils.checkNickNames(desc)) {
            msg.setStatus_code(JoymeResultMsg.CODE_E);
            msg.setMsg("user.word.illegl");
            logger.debug("含有非法内容-----:" + desc);
        }
        return binder.toJson(msg);
    }

    @Deprecated
    @RequestMapping("/sysdomain")
    @ResponseBody
    public String validSysDomain(@RequestParam(value = "domain") String domain) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        //过滤关键词
//        if (!SystemWordFilter.checkMM(domain)) {
//            msg.setStatus_code(ResultMsg.CODE_E);
//            msg.setMsg("user.blogdomain.illegl");
//            return binder.toJson(msg);
//        }
        if (ContextFilterUtils.checkDomainFormulaRegexs(domain.trim())) {//合法的域名
            if (ContextFilterUtils.checkDomainBlackList(domain.trim())) {
                //含有保留字
                msg.setStatus_code(JoymeResultMsg.CODE_E);
                msg.setMsg("user.blogdomain.illegl");
                return binder.toJson(msg);
            }
        } else {
            msg.setStatus_code(JoymeResultMsg.CODE_E);
            msg.setMsg("user.blogdomain.illegl");
            return binder.toJson(msg);
        }
        return binder.toJson(msg);
    }

    @RequestMapping("/sysword")
    @ResponseBody
    public String validSysWord(@RequestParam(value = "word") String word) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        //过滤关键词
        if (!SystemWordFilter.checkText(word)) {
            msg.setStatus_code(JoymeResultMsg.CODE_E);
            msg.setMsg("user.word.illegl");
            return binder.toJson(msg);
        }
        return binder.toJson(msg);
    }

    @RequestMapping("/postword")
    @ResponseBody
    public String vlidSysPost(@RequestParam(value = "word") String word) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        //过滤关键词
        if (ContextFilterUtils.postContainBlackList(word)) {
            msg.setStatus_code(JoymeResultMsg.CODE_E);
            msg.setMsg("user.word.illegl");
            return binder.toJson(msg);
        }
        return binder.toJson(msg);
    }


    @RequestMapping("/priReply")
    @ResponseBody
    public String validPriFeedback(@RequestParam(value = "bloguno") String bloguno, @RequestParam(value = "replyUno") String replyUno) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        return binder.toJson(msg);
    }

    @RequestMapping("/invite/title")
    @ResponseBody
    public String validPriFeedback(@RequestParam(value = "title") String title) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        //过滤关键词
        if (ContextFilterUtils.checkNickNamesBlackList(title)) {
            msg.setStatus_code(JoymeResultMsg.CODE_E);
            msg.setMsg("user.blogdomain.illegl");
            return binder.toJson(msg);
        }
        return binder.toJson(msg);
    }

    /**
     * 判断是否有权限
     *
     * @param request
     * @param rps     requriedProfileActiveStatus的简写
     * @return
     */
    @RequestMapping("/hasprivilage")
    @ResponseBody
    public String validateProfileActive(HttpServletRequest request,
                                        @RequestParam(required = false) boolean rps) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        return binder.toJson(msg);
    }


    /**
     * ajax验证 session是否过期
     *
     * @param request
     * @return
     */
    @RequestMapping("/islogin")
    @ResponseBody
    public String isLogin(HttpServletRequest request) {
        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        UserSession userSession = getUserBySession(request);
        if (userSession == null) {
            msg.setStatus_code(JoymeResultMsg.CODE_E);
            msg.setMsg("");
            return binder.toJson(msg);
        }
        return binder.toJson(msg);
    }


}
