/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.controller.message;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.event.user.UserEventType;
import com.enjoyf.platform.service.message.*;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;

import com.enjoyf.webapps.joyme.weblogic.blogwebsite.BlogWebSiteWebLogic;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.weblogic.message.UserMessageWebLogic;
import com.enjoyf.webapps.joyme.weblogic.user.UserPrivilageWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.util.SystemWordFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 私信操作action  返回json
 * <pre>
 * /json/message/private/send  发件ajax逻辑
 * /json/message/private/delete 删除私信
 * /json/message/private/deletegroup 删除一组私信
 * </pre>
 *
 * @author xinzhao  ericliu
 */
@RequestMapping(value = "/json/message/private")
@Controller
public class JsonMessageController extends BaseRestSpringController {
    private Logger logger = LoggerFactory.getLogger(JsonMessageController.class);

    /**
     * 搜索服务
     */
    @Resource(name = "userMessageWebLogic")
    private UserMessageWebLogic userMessageWebLogic;

    @Resource(name = "blogWebSiteWebLogic")
    private BlogWebSiteWebLogic blogWebSiteWebLogic;

    @Resource(name = "userPrivilageWebLogic")
    private UserPrivilageWebLogic userPrivilageWebLogic;

    private JsonBinder binder = JsonBinder.buildNormalBinder();

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;


    /**
     * 发送私信
     */
    @RequestMapping(value = "/send")
    @ResponseBody
    public String jsonSendMessage(HttpServletRequest request,
                                  @RequestParam(value = "messagebody") String messagebody,
                                  @RequestParam(value = "receivename") String receivename) {

        logger.debug("sendMessage action");

        JoymeResultMsg msg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        try {
            String sendUno = getUserBySession(request).getBlogwebsite().getUno();

            if (StringUtil.length(messagebody) > (double) WebappConfig.get().getMessageBodyLength()) {
                msg.setMsg(i18nSource.getMessage("message.content.length", new Object[]{}, Locale.CHINA));
                return binder.toJson(msg);
            }

            if (StringUtil.isEmpty(receivename) || StringUtil.isEmpty(messagebody)) {
                msg.setMsg(i18nSource.getMessage("message.content.empty", new Object[]{}, Locale.CHINA));
                return binder.toJson(msg);
            }

            if (!SystemWordFilter.checkPost(messagebody)) {
                msg.setMsg(i18nSource.getMessage("message.body.illegl", new Object[]{}, Locale.CHINA));
                return binder.toJson(msg);
            }

            ProfileBlog recieverProfileBlog = blogWebSiteWebLogic.findBlogWebSiteByBlogname(receivename);
            if (recieverProfileBlog == null) {
                msg.setMsg(i18nSource.getMessage("message.user.notexists", new Object[]{receivename}, Locale.CHINA));
                return binder.toJson(msg);
            }

            Message message = new Message();
            message.setBody(messagebody);
            message.setMsgType(MessageType.PRIVATE);
            message.setRecieverUno(recieverProfileBlog.getUno());
            message.setSenderUno(sendUno);
            message.setOwnUno(sendUno);
            message.setSendDate(new Date());
            message.setSendIp(getIp(request));

            userMessageWebLogic.sendMessage(message); //发消息的返回对象

            sendOutPostMessageUserEvent(sendUno, recieverProfileBlog.getUno(), message.getSendDate(), getIp(request));

            List<Message> messageList = new ArrayList<Message>();
            message.setBody(processMessage(message.getBody()));
            messageList.add(message);
            msg.setResult(messageList);

            msg.setStatus_code(JoymeResultMsg.CODE_S);

        } catch (ServiceException e) {
            if (e.equals(MessageServiceException.SEND_MESSAGE_REPEAT)) {
                logger.error(this.getClass().getName() + " sendMessage occured ServiceException.e:" + e);
                msg.setMsg(i18nSource.getMessage("message.send.repeat", new Object[]{}, Locale.CHINA));
            } else {
                logger.error(this.getClass().getName() + " sendMessage occured ServiceException.e:" + e);
                msg.setMsg(i18nSource.getMessage("message.user.syserror", new Object[]{}, Locale.CHINA));
            }
            return binder.toJson(msg);
        }

        binder.setDateFormat("HH:mm");

        return binder.toJson(msg);
    }

    private void sendOutPostMessageUserEvent(String uno, String recieverUno, Date date, String ip) {
        //post event
        UserEvent userEvent = new UserEvent(uno);

        userEvent.setDestUno(recieverUno);
        userEvent.setEventType(UserEventType.USER_SOCIAL_MESSAGE_POST);
        userEvent.setCount(1l);
        userEvent.setEventDate(date);
        userEvent.setEventIp(ip);

        try {
            EventDispatchServiceSngl.get().dispatch(userEvent);
        } catch (Exception e) {
            logger.error("sendOutPostMessageUserEvent error.", e);
        }
    }

    /**
     * 删除一组
     *
     * @param sendUno
     * @return
     */
    @RequestMapping(value = "/deletegroup")
    @ResponseBody
    public String jsonDeleteGroup(HttpServletRequest request,
                                  @RequestParam(value = "sendUno") String sendUno) {
        logger.debug("deleteGroup action");
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        String uno = getUserBySession(request).getBlogwebsite().getUno();
        try {
            boolean bVal = MessageServiceSngl.get().removeSenderMessages(uno, sendUno);
            if (!bVal) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            }
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " deleteGroup occured ServiceException.e:", e);
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
        }

        return binder.toJson(resultMsg);
    }

    /**
     * ajax删除单挑消息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public String jsonDelete(@RequestParam(value = "messageid") long id, HttpServletRequest request) {
        logger.debug("message delete action");

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        String uno = getUserBySession(request).getBlogwebsite().getUno();

        try {
            boolean bVal = MessageServiceSngl.get().removeMessage(uno, id);
            if (!bVal) {
                resultMsg.setStatus_code(JoymeResultMsg.CODE_E);

                //
                sendOutRemoveMessageUserEvent(uno, new Date(), getIp(request));
            }
        } catch (ServiceException e) {
            logger.error(this.getClass().getName() + " delete occured ServiceException.e:", e);
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
        }

        return binder.toJson(resultMsg);
    }

    private void sendOutRemoveMessageUserEvent(String uno, Date date, String ip) {
        //post event
        UserEvent userEvent = new UserEvent(uno);

        userEvent.setEventType(UserEventType.USER_SOCIAL_MESSAGE_REMOVE);
        userEvent.setCount(1l);
        userEvent.setEventDate(date);
        userEvent.setEventIp(ip);

        try {
            EventDispatchServiceSngl.get().dispatch(userEvent);
        } catch (Exception e) {
            logger.error("sendOutRemoveMessageUserEvent error.", e);
        }
    }

    private String processMessage(String content) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(content);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_PRIVIEW_MESSAGE);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent.getContent();
    }
}