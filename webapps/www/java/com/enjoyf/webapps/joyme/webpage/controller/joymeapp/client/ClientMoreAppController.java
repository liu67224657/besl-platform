package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.client;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.ClientLine;
import com.enjoyf.platform.service.joymeapp.ClientLineField;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
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
import com.enjoyf.webapps.joyme.dto.joymeapp.client.ClientMoreAppDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.client.ClientWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 下午1:03
 * Description:
 */
@Controller
@RequestMapping(value = "/joymeapp/client")
public class ClientMoreAppController extends BaseRestSpringController {

    private static final String CLIENT_MORE_APP_CODE = "moreapp_";

    private int CLIENT_MORE_APP_PAGE_SIZE = 10;

    @Resource(name = "clientWebLogic")
    private ClientWebLogic clientWebLogic;

    /**
     * 着迷攻略系列-ios
     *
     * @param request
     * @param response
     * @param platform
     * @param channelCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/moreapp")
    public String recom(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "platform", required = false) String platform,
                        @RequestParam(value = "channelid", required = false) String channelCode,
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
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        if (!StringUtil.isEmpty(pSize) && Integer.parseInt(pSize) > 0) {
            CLIENT_MORE_APP_PAGE_SIZE = Integer.parseInt(pSize);
        }

        Pagination pagination = new Pagination(pageNo * CLIENT_MORE_APP_PAGE_SIZE, pageNo, CLIENT_MORE_APP_PAGE_SIZE);

        try {

            ClientLine clientLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode()))
                    .add(QueryCriterions.eq(ClientLineField.CODE, CLIENT_MORE_APP_CODE + platform)));

            if (clientLine == null) {
                resultMsg.setRs(ResultObjectMsg.CODE_E);
                resultMsg.setMsg("client.line.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            PageRows<ClientMoreAppDTO> moreAppPageRows = clientWebLogic.queryClientMoreAppList(CLIENT_MORE_APP_CODE + platform, flag, pagination);

            if (moreAppPageRows == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("client.line.item.is.null");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            resultMsg.setResult(moreAppPageRows.getRows());
            resultMsg.setPage(new JsonPagination(moreAppPageRows.getPage()));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            resultMsg.setMsg("system.error");
            resultMsg.setRs(ResultListMsg.CODE_E);
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

}
