package com.enjoyf.webapps.joyme.webpage.controller.giftmarket.giftmarketwap;

import com.enjoyf.platform.service.joymeapp.ClientLine;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.activity.ActivityDTO;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import org.apache.lucene.util.CollectionUtil;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-6-20
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/gift/wap/")
public class SearchGiftController extends AbstractWapGiftMarketController {

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    //    private static final String APPID = "wx758f0b2d30620771";  服务号
    private static final String APPID = "wxedaaf0b0315d44e7";   //订阅号
    //SECRET
//    private static final String SECRET = "b58cd348c7f5908055e5e691eed45c39";  //服务号
    private static final String SECRET = "afde3c1cd927e508f663b92e6a084b6b ";   //订阅号

    @RequestMapping(value = "/searchpage")
    public ModelAndView searchPage(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "openid", required = false) String openId,
                                   @RequestParam(value = "token", required = false) String token) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(openId) || !StringUtil.isEmpty(token)) {
            mapMessage.put("openid", openId);
            mapMessage.put("token", token);
        }

        UserCenterSession userSession = getUserCenterSeesion(request);
        try {
//            String code = request.getParameter("code");
//            GAlerter.lab(this.getClass().getName() + " reserve code===================" + code);

            if (userSession == null) {
//                String openId = "";
//                if (!StringUtil.isEmpty(code)) {
//                    String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
//                    url = url.replace("APPID", APPID).replace("SECRET", SECRET).replace("CODE", code);
//                    GAlerter.lab(this.getClass().getName() +" url===================" + url);
//                    JSONObject jsonObject = WeixinUtil.httpRequest(url, "GET", null);
//                    openId = (String) jsonObject.get("openid");
//                    GAlerter.lab(this.getClass().getName() + " getOpenIdJsonObject===================" + jsonObject);
//                    GAlerter.lab(this.getClass().getName() +" openId===================" + openId);
//                }
//                openId = "oopVHuIXwSKpKj8OGVE1DuHC6gxc";
                if (!StringUtil.isEmpty(openId)) {
                    userSession = getWxUserSession(openId, request, response);

//                    userSession = new UserSession();
//                    userSession = giftMarketWebLogic.registerAndLogin(userSession, userWebLogic, openId, getIp(request));
//                    saveUserInSession(request, userSession);
                }
            }
            PageRows<ClientLineItem> pageRows = JoymeAppServiceSngl.get().queryItemsByLineCode("weixin_hot_search_list", "", new Pagination(giftMarketExchangeSize, pageNo, giftMarketExchangeSize));
            if (pageRows != null && !CollectionUtils.isEmpty(pageRows.getRows())) {
                mapMessage.put("searchnamelist", pageRows.getRows());
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/giftmarketsearch", mapMessage);
    }

    /**
     * 礼包中心wap页 搜索功能
     *
     * @param cond 搜索关键字
     * @param uno  用户的uno
     * @return
     */
    @RequestMapping(value = "/search")
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "searchtext", required = false) String cond,
                               @RequestParam(value = "uno", required = false) String uno,
                               @RequestParam(value = "openid", required = false) String openId,
                               @RequestParam(value = "token", required = false) String token) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(openId) || !StringUtil.isEmpty(token)) {
            mapMessage.put("openid", openId);
            mapMessage.put("token", token);
        }
        try {
            UserCenterSession userSession = getUserCenterSeesion(request);
            if (userSession == null) {
                if (!StringUtil.isEmpty(openId)) {
                    userSession = getWxUserSession(openId, request, response);
//                    userSession = new UserSession();
//                    userSession = giftMarketWebLogic.registerAndLogin(userSession, userWebLogic, openId, getIp(request));
//                    saveUserInSession(request, userSession);
                }
            }

            PageRows<ActivityDTO> activityDTOPageRows = giftMarketWebLogic.searchActivity(cond, new Pagination(10, 1, 10));
            mapMessage.put("cond", replaceHtmlText(cond));
            mapMessage.put("uno", uno);
            if (activityDTOPageRows != null) {
                mapMessage.put("list", activityDTOPageRows.getRows());
                mapMessage.put("page", activityDTOPageRows.getPage());
            } else {
                mapMessage.put("list", "");
                mapMessage.put("page", new Pagination());
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/giftmarket/giftmarketwap/gift_search_result", mapMessage);
    }

    @ResponseBody
    @RequestMapping(value = "/jsonsearch")
    public String jsonSearch(@RequestParam(value = "searchtext", required = false) String cond,
                             @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer pnum,
                             @RequestParam(value = "token", required = false) String token,
                             @RequestParam(value = "openid", required = false) String openId) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultListMsg.CODE_S);
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            PageRows<ActivityDTO> activityDTOPageRows = giftMarketWebLogic.searchActivity(cond, new Pagination(10 * pnum, pnum, 10));
            if (activityDTOPageRows != null) {
                mapMessage.put("list", activityDTOPageRows.getRows());
                mapMessage.put("page", activityDTOPageRows.getPage());
            } else {
                mapMessage.put("list", "");
                mapMessage.put("page", "");
            }

            if (!StringUtil.isEmpty(openId) || !StringUtil.isEmpty(token)) {
                mapMessage.put("openid", openId);
                mapMessage.put("token", token);
            }

            resultMsg.setResult(mapMessage);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    private String replaceHtmlText(String text) {
        if (StringUtil.isEmpty(text)) {
            return "";
        }
        text = text.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replace("'", "&apos;");
        return text;
    }
}
