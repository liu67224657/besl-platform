package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.client;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.JsonPagination;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultPageMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.joymeclient.GameDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.client.ClientWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 下午1:03
 * Description:
 */
@Controller
@RequestMapping(value = "/joymeapp/gameguide")
public class ClientGameGuideController extends BaseRestSpringController {

    private static final String CLIENT_GAME_GUIDE_CODE = "gameguide_";

    @Resource(name = "clientWebLogic")
    private ClientWebLogic clientWebLogic;

    /**
     * @return
     */
    @ResponseBody
    @RequestMapping("/recommend")
    public String recommend(@RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                            @RequestParam(value = "client_id", required = false) String clientId,
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

        Pagination pagination = new Pagination(count * page, page, count);
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(ClientLineField.CODE, CLIENT_GAME_GUIDE_CODE + platform))
                .add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode()))
                .add(QuerySort.add(ClientLineField.CREATE_DATE, QuerySortOrder.ASC));

        try {

            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(queryExpress);
            if (clientLine == null) {
                resultMsg.setMsg("client.line.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            PageRows<GameDTO> pageRows = clientWebLogic.queryGameDTO(clientLine.getCode(), flag, pagination);
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

    @RequestMapping(value = "/search")
    @ResponseBody
    public String search(@RequestParam(value = "pnum", required = false, defaultValue = "1") Integer page,
                         @RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
                         @RequestParam(value = "client_id", required = false) String clientId,
                         @RequestParam(value = "client_token", required = false) String clientToken,
                         @RequestParam(value = "appkey", required = false) String appkey,
                         @RequestParam(value = "platform", required = false) String platform,
                         @RequestParam(value = "channelid", required = false) String channelId,
                         @RequestParam(value = "access_token", required = false) String access_token,
                         @RequestParam(value = "token_secr", required = false) String token_secr,
                         @RequestParam(value = "cond", required = false) String cond) {
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
        Pagination pagination = new Pagination(count * page, page, count);
        try {
            PageRows<GameDTO> pageRows = new PageRows<GameDTO>();
            if (pageRows == null) {
                resultMsg.setMsg("cond.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }
            resultMsg.setResult(pageRows.getRows());
            resultMsg.setPage(new JsonPagination(pageRows.getPage()));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }
}
