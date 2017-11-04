package com.enjoyf.webapps.joyme.webpage.controller.social;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.timeline.UserTimeline;
import com.enjoyf.platform.service.timeline.UserTimelineDomain;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.weblogic.usercenter.UserTimelineWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import com.google.common.collect.Lists;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * <p/>
 * Description:用户动态
 * </p>
 * <pre>
 * /focus	关注
 * /unfocus	取消关注
 * </pre>
 *
 * @author: <a href=mailto:wengangsai@enjoyfound.com>saiwengang</a>
 */
@Controller
@RequestMapping("/api/timeline")
public class UserTimelineController extends BaseRestSpringController {
    Logger logger = LoggerFactory.getLogger(UserTimelineController.class);

    @Resource(name = "userTimelineWebLogic")
    private UserTimelineWebLogic userTimelineWebLogic;

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    private JsonBinder binder = JsonBinder.buildNormalBinder();

    /**
     * 增加用户动态 todo rename
     *
     * @param profileid 被关注人的profileid
     * @param request
     * @return
     */
    @RequestMapping(value = "/addusertimeline")
    @ResponseBody
    public String addUserTimeline(@RequestParam(value = "profileid", required = false) String profileid,
                                  @RequestParam(value = "type", required = false) String type,
                                  @RequestParam(value = "actionType", required = false) String actionType,
                                  @RequestParam(value = "extendBody", required = false) String extendBody,
                                  @RequestParam(value = "time", required = false) String time,
                                  HttpServletRequest request) {

        JSONObject jsonObject = ResultCodeConstants.FAILED.getJsonObject();
        try {
            Date dateTime = new Date();
            if (!StringUtil.isEmpty(time)) {
                try {
                    dateTime = new Date(Long.parseLong(time));
                } catch (NumberFormatException e) {
                    return ResultCodeConstants.FAILED.toString();
                }
            }
            UserTimeline userTimeline = userTimelineWebLogic.buildUserTimeline(profileid, actionType, type, extendBody, dateTime);
            if (userTimeline != null) {
                jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
                List result = Lists.newArrayList();
                result.add(userTimeline);
                jsonObject.put("result", result);
            }
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " focus occured ServiceException:", e);
        }

        return jsonObject.toString();
    }

    /**
     * 获取-我的动态/我的wiki
     * 他的动态/wiki
     *
     * @param profileid 用户动态的的profileid
     * @param request
     * @return
     */
    @RequestMapping(value = "/getUserTimelineByType")
    @ResponseBody
    public String getUserTimelineByType(@RequestParam(value = "profileid") String profileid,
                                        @RequestParam(value = "type") String type,
                                        @RequestParam(value = "pno") String pno,
                                        @RequestParam(value = "psize") String psize, HttpServletRequest request) {

        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        //登录
        UserSession userSession = this.getUserBySession(request);
        if (userSession == null) {
            msg.setStatus_code(JoymeResultMsg.CODE_NOT_LOGIN);
            msg.setMsg("user.not.login");
            return binder.toJson(msg);
        }
        try {
            //我的动态，我的wiki
            String domain = UserTimelineDomain.MY.getCode();
            if (StringUtil.isEmpty(profileid)) {
                profileid = userSession.getBlogwebsite().getUno();
            }
            Pagination page = new Pagination();
            page.setPageSize(Integer.parseInt(psize));
            page.setCurPage(Integer.parseInt(pno));
            PageRows<UserTimeline> pageRows = userTimelineWebLogic.queryUserTimeline(profileid, domain, type, page);
            msg.setResult(pageRows.getRows());
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " focus occured ServiceException:", e);
            msg.setMsg(i18nSource.getMessage("system.error", new Object[]{}, Locale.CHINA));
        }

        return binder.toJson(msg);
    }

    /**
     * 获取-好友动态
     *
     * @param profileid 用户动态的的profileid
     * @param request
     * @return
     */
    @RequestMapping(value = "/getFreindUserTimeline")
    @ResponseBody
    public String getFreindUserTimeline(@RequestParam(value = "profileid") String profileid,
                                        @RequestParam(value = "type") String type,
                                        @RequestParam(value = "pno") String pno,
                                        @RequestParam(value = "psize") String psize, HttpServletRequest request) {

        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        //登录
        UserSession userSession = this.getUserBySession(request);
        if (userSession == null) {
            msg.setStatus_code(JoymeResultMsg.CODE_NOT_LOGIN);
            msg.setMsg("user.not.login");
            return binder.toJson(msg);
        }
        try {
            Pagination page = new Pagination();
            page.setPageSize(Integer.parseInt(psize));
            page.setCurPage(Integer.parseInt(pno));
            String domain = UserTimelineDomain.FRIEND.getCode();
            PageRows<UserTimeline> pageRows = userTimelineWebLogic.queryUserTimeline(profileid, domain, type, page);
            msg.setResult(pageRows.getRows());
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " focus occured ServiceException:", e);
            msg.setMsg(i18nSource.getMessage("system.error", new Object[]{}, Locale.CHINA));
        }

        return binder.toJson(msg);
    }

    /**
     * 删除-好友动态
     *
     * @param profileid 用户动态的的profileid
     * @param request
     * @return
     */
    @RequestMapping(value = "/delUserTimeline")
    @ResponseBody
    public String delUserTimeline(@RequestParam(value = "profileid") String profileid,
                                  @RequestParam(value = "type") String type,
                                  @RequestParam(value = "destProfileid") String destProfileid,
                                  @RequestParam(value = "destId") String destId,
                                  HttpServletRequest request) {

        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        //登录
        UserSession userSession = this.getUserBySession(request);
        if (userSession == null) {
            msg.setStatus_code(JoymeResultMsg.CODE_NOT_LOGIN);
            msg.setMsg("user.not.login");
            return binder.toJson(msg);
        }
        try {
            String domain = UserTimelineDomain.MY.getCode();
            boolean delFlag = userTimelineWebLogic.delUserTimeline(profileid, domain, type, destId, destProfileid);
            if (!delFlag) {
                msg.setStatus_code(JoymeResultMsg.CODE_S);
                msg.setMsg("删除成功");
            }
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " delete  timeline ServiceException:", e);
            msg.setMsg(i18nSource.getMessage("system.error", new Object[]{}, Locale.CHINA));
        }

        return binder.toJson(msg);
    }
}
