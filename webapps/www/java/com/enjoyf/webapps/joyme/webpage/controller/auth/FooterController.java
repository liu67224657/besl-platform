package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ericliu on 14/10/22.
 */
@Controller
@RequestMapping("/auth/footer")
public class FooterController extends AbstractAuthController {

    @RequestMapping(value = "/m")
    public ModelAndView mHeader(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "hdflag", required = false) String hdflag,
                                @RequestParam(value = "redr", required = false) String redr) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String refer = request.getHeader("referer");
        if(StringUtil.isEmpty(refer) || !refer.contains(WebappConfig.get().DOMAIN)){
            //为空 或者 被钓鱼，返回到礼包首页
            refer = WebappConfig.get().getUrlM();
        }
        String pcUrl = "";
        if(refer.contains("#")){
            pcUrl = refer.substring(0, refer.indexOf("#"));
        }else {
            pcUrl = refer;
        }
        if(pcUrl.contains("?")){
            pcUrl += "&tab_device=wap_pc";
        }else {
            pcUrl += "?tab_device=wap_pc";
        }
        pcUrl += refer.substring(refer.indexOf("#")+1, refer.length());
        mapMessage.put("pcUrl", "http://www."+WebappConfig.get().getDomain()+"/#wappc");
        mapMessage.put("mUrl", "http://m."+WebappConfig.get().getDomain());
        return new ModelAndView("/views/jsp/passport/m/footer-m", mapMessage);
    }
}
