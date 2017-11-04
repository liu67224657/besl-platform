package com.enjoyf.webapps.joyme.webpage.controller.message;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.service.message.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.webapps.joyme.weblogic.notice.NoticeWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.platform.service.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:系统信息Action
 * </p>
 *
 * /message/notice/list  读取系统信息
 * /message/notice/delete 删除系统信息
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a> ,zx
 */
@Controller
 @RequestMapping(value = "/message/notice")
public class NoticeController extends BaseRestSpringController {
    Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Resource(name = "noticeWebLogic")
    private NoticeWebLogic noticeWebLogic;


    /**
     * 系统消息列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/list")
    public ModelAndView noticeMessage(HttpServletRequest request) {
        logger.debug("noticeMessage action");

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        String uno = getUserBySession(request).getBlogwebsite().getUno();
        Pagination page = new Pagination();
        page.setPageSize(100);
        try {
            PageRows<Message> pageRows = noticeWebLogic.querySysnoticeByUnoAndType(uno, MessageType.OPERATION, page);

            mapMessage.put("pageRows", pageRows);
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " noticeMessage occured ServiceException.e:", e);
        }

        try {
            MessageServiceSngl.get().readNoticeByType(uno, NoticeType.NEW_BULLETIN);
        } catch (ServiceException e) {
            logger.error(" bulletin inbox read memo bulletin occuredServiceException:e.", e);
        }
        return new ModelAndView("/views/jsp/message/noticelist", mapMessage);
    }
}
