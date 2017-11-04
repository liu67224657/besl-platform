/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.controller.message;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.message.*;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.joyme.weblogic.entity.MessageProfile;
import com.enjoyf.webapps.joyme.weblogic.message.UserMessageWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 私信操作action
 * <pre>
 * /message/private/reply  回复私信
 * /message/private/list 私信列表
 * </pre>
 *
 * @author xinzhao  ericliu
 */
@RequestMapping(value = "/message/private")
@Controller
public class MessageController extends BaseRestSpringController {
    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    /**
     * 搜索服务
     */
    @Resource(name = "userMessageWebLogic")
    private UserMessageWebLogic userMesssageWebLogic;

    /**
     * 回复私信的列表页面
     *
     * @param request
     * @param sendUno
     * @return
     */
    @RequestMapping(value = "/reply")
    public ModelAndView feedbackMessage(HttpServletRequest request,
                                        @RequestParam(value = "p", required = false) Integer pageNo,
                                        @RequestParam(value = "senduno") String sendUno) {
        logger.debug("feedbackMessage action");
        if(StringUtil.isEmpty(sendUno)){
            return new ModelAndView("/views/jsp/common/error");
        }

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("sendUno", sendUno);

        String receuno = this.getUserBySession(request).getBlogwebsite().getUno();
        try {
            ProfileBlog blog = ProfileServiceSngl.get().getProfileBlogByUno(sendUno);
            mapMessage.put("screenName", blog.getScreenName());
            mapMessage.put("replyBlog",blog);
            //init page
            pageNo = pageNo == null ? 1 : pageNo;

            //totalRows为空直接返回
            Pagination page = new Pagination(pageNo * WebappConfig.get().getMessageReplyListPageSize(), pageNo, WebappConfig.get().getMessageReplyListPageSize());
            PageRows<MessageProfile> pageRows = userMesssageWebLogic.queryReceiveMessageListByUno(receuno, sendUno, MessageType.PRIVATE, page);
            mapMessage.put("recelist", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " feedbackMessage occured ServiceException.e:", e);
            return new ModelAndView("/views/jsp/common/error");
        }

        return new ModelAndView("/views/jsp/message/reply", mapMessage);
    }


    /**
     * 查询私信
     */
    @RequestMapping(value = "/list")
    public ModelAndView listReMessage(HttpServletRequest request,
                                      @RequestParam(value = "p", required = false) Integer pageNo) {
        logger.debug("listReMessage action");

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UserSession userSession = this.getUserBySession(request);
        String uno = userSession.getBlogwebsite().getUno();

        try {
            //init page
            pageNo = pageNo == null ? 1 : pageNo;
            //totalRows为空直接返回
            Pagination page = new Pagination(pageNo * WebappConfig.get().getMessageListPageSize(), pageNo, WebappConfig.get().getMessageListPageSize());
            PageRows<MessageProfile> mesProPageRows = userMesssageWebLogic.queryReceiveMessageListByUno(uno, page);
            mapMessage.put("reMessages", mesProPageRows);
            mapMessage.put("page", mesProPageRows.getPage());
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " listReMessage occured ServiceException.e", e);
        }

        try {
            MessageServiceSngl.get().readNoticeByType(uno, NoticeType.NEW_MESSAGE);
        } catch (ServiceException e) {
            logger.error(" message inbox read memo message occuredServiceException:e.", e);
        }
        return new ModelAndView("/views/jsp/message/message", mapMessage);
    }

}