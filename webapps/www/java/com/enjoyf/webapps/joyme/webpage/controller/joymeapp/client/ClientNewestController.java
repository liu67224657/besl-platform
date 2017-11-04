package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.client;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.ClientLine;
import com.enjoyf.platform.service.joymeapp.ClientLineField;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
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
import com.enjoyf.webapps.joyme.dto.joymeclient.ArticleDTO;
import com.enjoyf.webapps.joyme.dto.joymeclient.ClientSpecialDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.client.ClientWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.JoymeAppBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 下午1:03
 * Description:
 */
@Controller
@RequestMapping(value = "/joymeapp/client/newest")
public class ClientNewestController extends JoymeAppBaseController {
    private static final String CLIENT_LOCATION_CODE = "newsetarticle_";
    private static final String CLIENT_SPECIAL_CODE = "newgamespecial_";
    private int CLIENT_NEWST_PAGE_SIZE = 10;
    private static final int CLIENT_Special_PAGE_SIZE = 7;

    @Resource(name = "clientWebLogic")
    private ClientWebLogic clientWebLogic;


    /**
     * @return
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(HttpServletRequest request,
                             @RequestParam(value = "client_id", required = false) String clientId,
                             @RequestParam(value = "client_token", required = false) String clientToken,
                             @RequestParam(value = "appkey", required = false) String appkey,
                             @RequestParam(value = "platform", required = false) String platform,
                             @RequestParam(value = "channelid", required = false) String channelId,
                             @RequestParam(value = "access_token", required = false) String access_token,
                             @RequestParam(value = "token_secr", required = false) String token_secr,
                             @RequestParam(value = "pnum", required = false) String page,
                             @RequestParam(value = "count", required = false) String pSize,
                             @RequestParam(value = "flag", required = false) String flag) {
        JsonBinder jsonBinder = JsonBinder.buildNormalBinder();
        ResultPageMsg resultMsg = new ResultPageMsg(ResultPageMsg.CODE_S);
        Map<String, Object> map = new HashMap<String, Object>();


        String result;
        try {
            if (getPlatformByPlatformStr(platform) == null) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.null");
                map.put("result", JsonBinder.buildNormalBinder().toJson(resultMsg));
                return new ModelAndView("/views/jsp/json/result", map);
            }

            if (StringUtil.isEmpty(appkey)) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("param.is.null");
                map.put("result", JsonBinder.buildNormalBinder().toJson(resultMsg));
                return new ModelAndView("/views/jsp/json/result", map);
            }

            int pageNo = 1;
            try {
                pageNo = StringUtil.isEmpty(page) ? 1 : Integer.parseInt(page);
            } catch (NumberFormatException e) {
            }
            if (!StringUtil.isEmpty(pSize) && Integer.parseInt(pSize) > 0) {
                CLIENT_NEWST_PAGE_SIZE = Integer.parseInt(pSize);
            }
            Pagination pagination = new Pagination(pageNo * CLIENT_NEWST_PAGE_SIZE, pageNo, CLIENT_NEWST_PAGE_SIZE);
            PageRows<ArticleDTO> pageRows = clientWebLogic.queryArticleListByItems(CLIENT_LOCATION_CODE + platform, flag, pagination);

            if (pageRows == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("system.error");
                map.put("result", JsonBinder.buildNormalBinder().toJson(resultMsg));
                return new ModelAndView("/views/jsp/json/result", map);
            }

            resultMsg.setResult(pageRows.getRows());
            resultMsg.setPage(new JsonPagination(pageRows.getPage()));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            map.put("result", JsonBinder.buildNormalBinder().toJson(resultMsg));
            return new ModelAndView("/views/jsp/json/result", map);
        }

        map.put("result", JsonBinder.buildNormalBinder().toJson(resultMsg));
        return new ModelAndView("/views/jsp/json/result", map);
    }

    @ResponseBody
    @RequestMapping(value = "/special/list")
    public String special(@RequestParam(value = "client_id", required = false) String clientId,
                          @RequestParam(value = "client_token", required = false) String clientToken,
                          @RequestParam(value = "appkey", required = false) String appkey,
                          @RequestParam(value = "platform", required = false) String platform,
                          @RequestParam(value = "channelid", required = false) String channelId,
                          @RequestParam(value = "access_token", required = false) String access_token,
                          @RequestParam(value = "token_secr", required = false) String token_secr,
                          @RequestParam(value = "flag", required = false) String flag) {
        ResultPageMsg resultMsg = new ResultPageMsg(ResultPageMsg.CODE_S);
        if (StringUtil.isEmpty(clientId)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(clientToken)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(appkey)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (StringUtil.isEmpty(platform)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.null");

        }
        if (StringUtil.isEmpty(channelId)) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.null");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        try {
//            QueryExpress queryExpress = new QueryExpress()
//                    .add(QueryCriterions.eq(ClientLineField.CODE, CLIENT_SPECIAL_CODE + platform))
//                    .add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode()));
//            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(queryExpress);
//
//            if (clientLine == null) {
//                resultMsg.setMsg("client.line.is.null");
//                return JsonBinder.buildNormalBinder().toJson(resultMsg);
//            }
            Pagination pagination = new Pagination(1 * CLIENT_Special_PAGE_SIZE, 1, CLIENT_Special_PAGE_SIZE);

            PageRows<ClientSpecialDTO> pageRows = clientWebLogic.querySpecial(CLIENT_SPECIAL_CODE + platform, flag, pagination);
            if (pageRows == null) {
                resultMsg.setMsg("client.line.item.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            resultMsg.setResult(pageRows.getRows());
            resultMsg.setPage(new JsonPagination(pageRows.getPage()));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }
}
