package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JsonPagination;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultPageMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.dto.joymeapp.AppResourceDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.JoymeAppWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-20
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/recommend")
public class JoymeAppRecommendController extends JoymeAppBaseController {
    @Resource(name = "joymeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    @ResponseBody
    @RequestMapping(value = "/list")
    public String reportInstall(HttpServletRequest request,
                                @RequestParam(value = "appkey", required = false) String appkey,
                                @RequestParam(value = "platform", required = false) Integer platform,
                                @RequestParam(value = "p", required = false) Integer pageNo,
                                @RequestParam(value = "maxId", required = false) Integer maxId,
                                @RequestParam(value = "mininId", required = false) Integer miniId) {
        ResultPageMsg resultMsg = new ResultPageMsg(ResultPageMsg.CODE_S);

        if (StringUtil.isEmpty(appkey) || platform == null) {
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("param.is.empty");
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        pageNo = pageNo <= 0 ? 1 : pageNo;
        Pagination pagination = new Pagination(DEFAULT_PAGE_SIZE * pageNo, pageNo, DEFAULT_PAGE_SIZE);

        String appKey = getAppKey(appkey);
        AppPlatform appPlatform = AppPlatform.getByCode(platform);
        try {
            PageRows<AppResourceDTO> pageRows = new PageRows<AppResourceDTO>();
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
