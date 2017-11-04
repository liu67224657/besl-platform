package com.enjoyf.webapps.joyme.webpage.controller.shorturl;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.service.shorturl.ShortUrlServiceSngl;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <p/>
 * Description:short url action
 * </p>
 */
@Controller
@RequestMapping(value = "/json/shorturl")
public class JsonShortUrlController extends BaseRestSpringController {
    /**
     * redirect 操作
     *
     * @param urlKey
     */
    @RequestMapping(value = "/geturl")
    @ResponseBody
    public String redirect(@RequestParam(value = "urlKey") String urlKey) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        try {
            ShortUrl url = ShortUrlServiceSngl.get().getUrl(urlKey);
            resultMsg.setStatus_code(JoymeResultMsg.CODE_S);
            resultMsg.setMsg(url.getUrl());
        } catch (Exception e) {
            //
            GAlerter.lab("ShortUrlController call ShortUrlService to getUrl error.", e);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

}
