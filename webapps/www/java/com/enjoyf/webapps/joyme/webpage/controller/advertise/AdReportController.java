package com.enjoyf.webapps.joyme.webpage.controller.advertise;

import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhimingli
 * Date: 2015/01/14
 * Time: 16:36
 */
@Controller
@RequestMapping("/joymeapp/ad")
public class AdReportController extends BaseRestSpringController {
    @ResponseBody
    @RequestMapping(value = "/report")
    public String report(HttpServletRequest request,
                         @RequestParam(value = "idfa", required = false) String idfa,
                         @RequestParam(value = "appid", required = false) String appid,
                         @RequestParam(value = "source", required = false) String source) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (StringUtil.isEmpty(idfa)) {
                jsonObject.put("success", false);
                jsonObject.put("message", "idfa.is.null");
                return jsonObject.toString();
            }
            MiscServiceSngl.get().saveAdvertiseInfo(appid + "_" + idfa, source);
            jsonObject.put("success", true);
            jsonObject.put("message", "success");
        } catch (Exception e) {
            jsonObject.put("message", "fail");
            jsonObject.put("success", false);
        }
        return jsonObject.toString();

    }
}
