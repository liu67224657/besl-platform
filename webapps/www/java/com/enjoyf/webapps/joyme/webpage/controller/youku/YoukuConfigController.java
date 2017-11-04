package com.enjoyf.webapps.joyme.webpage.controller.youku;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: EricLiu
 * Date: 15-5-26
 * Time: 上午11:58
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/youku/config")
public class YoukuConfigController extends AbstractYoukuController {

    @RequestMapping
    @ResponseBody
    public String config(HttpServletRequest request, HttpServletResponse response) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ykdomain", WebappConfig.get().getUrlYouku());
        jsonObject.put("appkey", APPKEY);

        return jsonObject.toString();
    }
}
