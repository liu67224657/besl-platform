package com.enjoyf.webapps.joyme.webpage.controller.shorturl;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.ShortUrlSumIncreaseEvent;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.event.user.UserEventType;
import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.service.shorturl.ShortUrlField;
import com.enjoyf.platform.service.shorturl.ShortUrlServiceSngl;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p/>
 * Description:short url action
 * </p>
 */
@Controller
@RequestMapping(value = "/shorturl")
public class ShortUrlController extends BaseRestSpringController {
    Logger logger = LoggerFactory.getLogger(ShortUrlController.class);

    /**
     * redirect 操作
     *
     * @param urlKey
     * @param request
     * @param response
     */
    @RequestMapping(value = "/{urlKey}")
    public ModelAndView redirect(@PathVariable(value = "urlKey") String urlKey, HttpServletRequest request, HttpServletResponse response) {
        ShortUrl url = null;

        try {
            url = ShortUrlServiceSngl.get().getUrl(urlKey);

            //
            if (url != null) {
                String uno = this.getUserBySession(request) != null ? this.getUserBySession(request).getBlogwebsite().getUno() : "unknown";

                sendOutShortClickEvent(uno, urlKey, url.getUrl(), this.getIp(request));
            }
        } catch (Exception e) {
            //
            GAlerter.lab("ShortUrlController call ShortUrlService to getUrl error.", e);
        }

        String returnUrl = "http://www.joyme.com";
        if (url != null) {
            try {
                returnUrl = encodeUrl(url.getUrl());
            } catch (UnsupportedEncodingException e) {
                logger.error("UnsupportedEncodingException,", e);
            }
        }

        RedirectView view = new RedirectView(returnUrl);
        view.setStatusCode(HttpStatus.MOVED_PERMANENTLY);

        return new ModelAndView(view);
    }

    private void sendOutShortClickEvent(String uno, String urlKey, String url, String ip) {
        //post event
        UserEvent userEvent = new UserEvent(uno);

        userEvent.setEventType(UserEventType.USER_CONTENT_SHORTURL_CLICK);
        userEvent.setCount(1l);
        userEvent.setEventDate(new Date());
        userEvent.setEventIp(ip);
        userEvent.setDescription(urlKey + "->" + url);

        ShortUrlSumIncreaseEvent systemEvent = new ShortUrlSumIncreaseEvent();

        systemEvent.setCount(1);
        systemEvent.setUrlKey(urlKey);
        systemEvent.setField(ShortUrlField.CLICKTIMES);

        try {
            EventDispatchServiceSngl.get().dispatch(userEvent);
            EventDispatchServiceSngl.get().dispatch(systemEvent);
        } catch (Exception e) {
            logger.error("sendOutShortClickEvent error.", e);
        }
    }

    private String encodeUrl(String url) throws UnsupportedEncodingException {

        Pattern chinesePattern = Pattern.compile("[\\u4e00-\\u9fa5]+");

        Matcher matcher = chinesePattern.matcher(url);

        StringBuffer resutlStringBuffer = new StringBuffer();

        while (matcher.find()) {
            String text = matcher.group(0);
            text = URLEncoder.encode(text, "UTF-8");
            matcher.appendReplacement(resutlStringBuffer, text);
        }
        matcher.appendTail(resutlStringBuffer);


        return resutlStringBuffer.toString();
    }

}
