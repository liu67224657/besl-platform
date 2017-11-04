package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.client;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppTopMenuCategory;
import com.enjoyf.platform.service.joymeapp.ClientLine;
import com.enjoyf.platform.service.joymeapp.ClientLineField;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.JsonPagination;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.ResultPageMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.joymeapp.client.ClientRecomMenuDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.client.ClientTopMenuDTO;
import com.enjoyf.webapps.joyme.dto.joymeclient.ArticleDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.client.ClientWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 下午1:03
 * Description:
 */
@Controller
@RequestMapping(value = "/joymeapp/client/home")
public class ClientHomeController extends BaseRestSpringController {
    private static final String KEY_CLIENT_HOME_HEAD_MENU = "menu";
    private static final String KEY_CLIENT_HOME_RECOMMEND = "recom";

    private static final String CLIENT_HOME_RECOM_MENU_CODE = "homerecom_";
    private static final String CLIENT_HOME_NEWS_CODE = "homenews_";

    private int CLIENT_HOME_NEWS_PAGE_SIZE = 10;

    @Resource(name = "clientWebLogic")
    private ClientWebLogic clientWebLogic;

    /**
     * 主页轮播图
     *
     * @param request
     * @param clientId
     * @param clientToken
     * @param appKey
     * @param platform
     * @param channelCode
     * @param access_token
     * @param token_secr
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/headmenu")
    public String headMenu(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "client_id", required = false) String clientId,
                           @RequestParam(value = "client_token", required = false) String clientToken,
                           @RequestParam(value = "appkey", required = false) String appKey,
                           @RequestParam(value = "platform", required = false) String platform,
                           @RequestParam(value = "channelid", required = false) String channelCode,
                           @RequestParam(value = "access_token", required = false) String access_token,
                           @RequestParam(value = "token_secr", required = false) String token_secr,
                           @RequestParam(value = "querychannel", required = false, defaultValue = "false") Boolean queryChannel,
                           @RequestParam(value = "flag", required = false) String flag) {
        ResultObjectMsg resultMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        Map<String, Object> menuMap = new HashMap<String, Object>();
        try {
            if (platform == null) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            List<ClientTopMenuDTO> topMenuList = clientWebLogic.queryClientTopMenuList(appKey, AppTopMenuCategory.CLIENT_LINE.getCode(), channelCode, Integer.parseInt(platform), flag);
            if (CollectionUtil.isEmpty(topMenuList)) {
                topMenuList = new LinkedList<ClientTopMenuDTO>();
            }

            menuMap.put(KEY_CLIENT_HOME_HEAD_MENU, topMenuList);

            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode()))
                    .add(QueryCriterions.eq(ClientLineField.CODE, CLIENT_HOME_RECOM_MENU_CODE + platform)));

            if (clientLine == null) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("client.line.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            List<ClientRecomMenuDTO> recomMenuList = clientWebLogic.queryClientRecomMenuList(CLIENT_HOME_RECOM_MENU_CODE + platform, flag);
            if (CollectionUtil.isEmpty(recomMenuList)) {
                recomMenuList = new LinkedList<ClientRecomMenuDTO>();
            }

            menuMap.put(KEY_CLIENT_HOME_RECOMMEND, recomMenuList);

            resultMsg.setResult(menuMap);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    @ResponseBody
    @RequestMapping(value = "/homenews")
    public String homeNews(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "client_id", required = false) String clientId,
                           @RequestParam(value = "client_token", required = false) String clientToken,
                           @RequestParam(value = "appkey", required = false) String appKey,
                           @RequestParam(value = "platform", required = false) String platform,
                           @RequestParam(value = "channelid", required = false) String channelCode,
                           @RequestParam(value = "access_token", required = false) String access_token,
                           @RequestParam(value = "token_secr", required = false) String token_secr,
                           @RequestParam(value = "pnum", required = false) String page,
                           @RequestParam(value = "count", required = false) String pSize,
                           @RequestParam(value = "flag", required = false) String flag) {
        ResultPageMsg resultMsg = new ResultPageMsg(ResultPageMsg.CODE_S);

        if (platform == null) {
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            resultMsg.setMsg("param.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }


        int pageNo = 1;
        try {
            pageNo = StringUtil.isEmpty(page) ? 1 : Integer.parseInt(page);
        } catch (NumberFormatException e) {

        }
        if (!StringUtil.isEmpty(pSize) && Integer.parseInt(pSize) > 0) {
            CLIENT_HOME_NEWS_PAGE_SIZE = Integer.parseInt(pSize);
        }
        Pagination pagination = new Pagination(pageNo * CLIENT_HOME_NEWS_PAGE_SIZE, pageNo, CLIENT_HOME_NEWS_PAGE_SIZE);

        try {

            PageRows<ArticleDTO> homeNewsMenuPageRows = clientWebLogic.queryArticleListByItems(CLIENT_HOME_NEWS_CODE + platform, flag, pagination);
            if (homeNewsMenuPageRows == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("system.error");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            resultMsg.setResult(homeNewsMenuPageRows.getRows());
            resultMsg.setPage(new JsonPagination(homeNewsMenuPageRows.getPage()));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setMsg("system.error");
            resultMsg.setRs(ResultObjectMsg.CODE_E);
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }
}
