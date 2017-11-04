package com.enjoyf.webapps.joyme.webpage.controller.weixin;

import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.weixin.MessageUtil;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
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
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 */

@Controller
@RequestMapping(value = "/weixin/callback")
public class WeiXinCallBackController extends BaseRestSpringController {
    //
    private Logger logger = LoggerFactory.getLogger(WeiXinCallBackController.class);
    //
    private final static String TOKEN_FOR_WEIXIN = "5ja8joym4emt7nline9edfk";


    ///weixin/callback/validate?signature=xxxx&timestamp=yyyyy&nonce=zzzz&echostr=xyz
    @RequestMapping(method = RequestMethod.GET, value = "/validate")
    @ResponseBody
    public String jsonPartVote(
                               @RequestParam(value = "timestamp", required = true) String timestamp,
                               @RequestParam(value = "nonce", required = true) String nonce,
                               @RequestParam(value = "echostr", required = true) String echostr) {
        List<String> inputKeyValues = new ArrayList<String>();

        inputKeyValues.add(TOKEN_FOR_WEIXIN);
        inputKeyValues.add(nonce);
        inputKeyValues.add(timestamp);

        Collections.sort(inputKeyValues);

        String returnValue = echostr;


        return returnValue;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validate")
    @ResponseBody
    public String jsonPartVote(HttpServletRequest request) {
        String xml = null;
        try {
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 消息类型
            String msgType = requestMap.get("MsgType");
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                xml = new WeixinTextDataProcessor().processRequest(requestMap);    //文本消息
                return xml;
            }
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                xml = new WeixinImageDataProcessor().processRequest(requestMap);
                return xml;
            }

            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) { //事件推送
                String eventType = requestMap.get("Event");    // 事件类型

                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    xml = new WeixinSubscribeDataProcessor().processRequest(requestMap);   //用户关注后回复的
                    return xml;

                } else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {// 自定义菜单点击事件
                    String key = requestMap.get("EventKey");

                    if ("feedback".equals(key)) {
                        xml = new WeixinFeedbackDataProcessor().processRequest(requestMap);  //意见反馈
                    } else if ("giftmarket".equals(key)) {//领礼包的菜单
                        xml = new WeixinGiftmarketDataProcessor().processRequest(requestMap);
                    } else if ("mygift".equals(key)) {
                        xml = new WeixinMygiftDataProcessor().processRequest(requestMap);
                    } else if ("search".equals(key)) {
                        xml = new WeixinSearchGiftDataProcessor().processRequest(requestMap);
                    }
                }
            }

            logger.info(this.getClass().getName() + "====xml====================" + xml);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return null;
        }

        return xml;
    }

}
