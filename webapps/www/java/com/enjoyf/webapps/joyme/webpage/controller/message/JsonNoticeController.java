/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.controller.message;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.weblogic.notice.NoticeWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 系统信息操作action  返回json
 * <pre>
 * /json/message/notice/delete 删除系统信息
 * </pre>
 *
 * @author xinzhao  ericliu
 */
@RequestMapping(value = "/json/message/notice")
@Controller
public class JsonNoticeController extends BaseRestSpringController {
    private Logger logger = LoggerFactory.getLogger(JsonNoticeController.class);

    /**
     * 搜索服务
     */

    @Resource(name = "noticeWebLogic")
    private NoticeWebLogic noticeWebLogic;

    private JsonBinder binder = JsonBinder.buildNormalBinder();

    /**
     * ajax删除系统消息
     *
     * @param noticeid
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public String jsonDelete(@RequestParam(value = "noticeid") Long noticeid, HttpServletRequest request) {
        logger.debug("delete notice action");
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        String uno = getUserBySession(request).getBlogwebsite().getUno();
        try {
            boolean bVal = noticeWebLogic.deleteSysMessage(uno, noticeid);
            if (!bVal) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            }
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " delete occured ServiceException.e:", e);
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
        }

        return binder.toJson(resultMsg);
    }
}