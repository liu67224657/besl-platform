package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.notice.AppNoticeSum;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.weblogic.notice.WikiNoticeWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ericliu on 14/10/22.
 */
@Controller
@RequestMapping("/auth/header")
public class HeaderController extends AbstractAuthController {

    @Resource(name = "wikiNoticeWebLogic")
    private WikiNoticeWebLogic wikiNoticeWebLogic;


    /**
     * 静态头
     *
     * @return
     */
    @RequestMapping
    public ModelAndView htmlHeader(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "hdflag", required = false) String hdflag, @RequestParam(value = "redr", required = false) String redr) {
//        request.getSession().removeAttribute(Constant.SESSION_HEARDER_MENU_FLAG);
//        if (!StringUtil.isEmpty(hdflag)) {
//            request.getSession().setAttribute(Constant.SESSION_HEARDER_MENU_FLAG, hdflag);
//        }
        UserCenterSession userCenterSession = getUserCenterSeesion(request);
//        if (userCenterSession == null) {
//            userCenterSession = authUserCenterSession(request, response);
//        }

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("redr", redr);
        if (userCenterSession != null && userCenterSession.getLoginDomain() != null
                && !userCenterSession.getLoginDomain().equals(LoginDomain.CLIENT)) {
            mapMessage.put("userinfo", userCenterSession);
        }

        return new ModelAndView("/views/jsp/passport/header", mapMessage);
    }

    @RequestMapping(value = "/userinfo")
    public ModelAndView htmlRight(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "hdflag", required = false) String hdflag,
                                  @RequestParam(value = "redr", required = false) String redr) {

        UserCenterSession userCenterSession = getUserCenterSeesion(request);

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("redr", redr);
        if (userCenterSession != null && userCenterSession.getLoginDomain() != null
                && !userCenterSession.getLoginDomain().equals(LoginDomain.CLIENT)) {
            mapMessage.put("userinfo", userCenterSession);
        }

        String template = request.getParameter("t");
        mapMessage.put("userCenterSession", userCenterSession);
        if (!StringUtil.isEmpty(template) && template.equals("index")) {
            return new ModelAndView("/views/jsp/passport/header-info-newversion", mapMessage);
        } else if (!StringUtil.isEmpty(template) && template.equals("simple")) {
            return new ModelAndView("/views/jsp/passport/header-info-simpleversion", mapMessage);
        } else if (!StringUtil.isEmpty(template) && template.equals("wiki")) {
            if (userCenterSession != null && !StringUtil.isEmpty(userCenterSession.getProfileId())) {
                try {
                    Set<NoticeType> noticeTypes = new HashSet<NoticeType>();
                    noticeTypes.addAll(NoticeType.getAll());
                    //查询未读消息数量
                    Map<String, AppNoticeSum> noticeSumMap = wikiNoticeWebLogic.queryNoticeSum(userCenterSession.getProfileId(), DEFAULT_APPKEY, "", "", noticeTypes);
                    mapMessage.put("noticeSumMap", noticeSumMap);

                } catch (ServiceException e) {
                    GAlerter.lab(this.getClass().getName() + "  ServiceException e", e);
                }
                if (!request.getRequestURL().toString().contains("joyme.test") && !request.getRequestURL().toString().contains("joyme.dev")) {
                    int privateMessageNum = queryUserPrivateMessageNum(userCenterSession.getUid());
                    mapMessage.put("messageNum", privateMessageNum == 0 ? null : privateMessageNum);
                }

            }
            return new ModelAndView("/views/jsp/passport/header-info-wikiversion", mapMessage);
        }

        return new ModelAndView("/views/jsp/passport/header-info", mapMessage);
    }

    @RequestMapping(value = "/m")
    public ModelAndView mHeader(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "hdflag", required = false) String hdflag,
                                @RequestParam(value = "redr", required = false) String redr) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UserCenterSession userCenterSession = getUserCenterSeesion(request);
        if (userCenterSession != null && userCenterSession.getLoginDomain() != null
                && !userCenterSession.getLoginDomain().equals(LoginDomain.CLIENT)) {
            mapMessage.put("userSession", userCenterSession);
        }

        String refer = request.getHeader("referer");
        if (StringUtil.isEmpty(refer) || !refer.contains(WebappConfig.get().DOMAIN)) {
            //为空 或者 被钓鱼，返回到礼包首页
            refer = WebappConfig.get().getUrlM();
        }
        try {
            URL url = new URL(refer);
            mapMessage.put("referHost", url.getHost());
            mapMessage.put("referPath", url.getPath());
        } catch (MalformedURLException e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
        return new ModelAndView("/views/jsp/passport/m/header-m", mapMessage);
    }


    private int queryUserPrivateMessageNum(Long uid) {
        HttpClientManager httpClientManager = new HttpClientManager();
        HttpResult httpResult = httpClientManager.get("http://wiki." + WebappConfig.get().DOMAIN + "/home/api.php?action=userboard&uid=" + uid + "&format=json", new HttpParameter[]{});
        try {
            JSONObject jsonObject = JSONObject.fromObject(httpResult.getResult());
            if (jsonObject.containsKey("data")) {
                JSONObject rsObject = jsonObject.getJSONObject("data");
                if (rsObject.containsKey("rs") && rsObject.get("rs").toString().equals("1")) {
                    Object reusltObject = rsObject.get("result");
                    if (reusltObject == null) {
                        return 0;
                    } else {
                        return Integer.parseInt(reusltObject.toString());
                    }
                }
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }
}
