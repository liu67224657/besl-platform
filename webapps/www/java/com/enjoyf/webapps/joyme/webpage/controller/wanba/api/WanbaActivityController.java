package com.enjoyf.webapps.joyme.webpage.controller.wanba.api;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.WanbaItemDomain;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaActivityDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaRecommendDTO;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaAskWebLogic;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaTagWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2016/9/26 0026.
 */
@Controller
@RequestMapping("/wanba/api/activity")
public class WanbaActivityController extends BaseRestSpringController {
    @Resource
    private WanbaTagWebLogic wanbaTagWebLogic;

    @Resource
    private WanbaAskWebLogic wanbaAskWebLogic;

    @ResponseBody
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                       @RequestParam(value = "pcount", required = false, defaultValue = "10") String pSize) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        int pageNo = 1;
        try {
            pageNo = Integer.parseInt(pNum);
        } catch (NumberFormatException e) {
        }

        int pagCount = 10;
        try {
            pagCount = Integer.parseInt(pSize);
        } catch (NumberFormatException e) {
        }


        Pagination pagination = new Pagination(pagCount * pageNo, pageNo, pagCount);
        PageRows<WanbaActivityDTO> returnRows = wanbaTagWebLogic.getActivityTagList(request, pagination);


        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", returnRows.getRows());
        resultMap.put("page", returnRows.getPage());
        jsonObject.put("result", JsonBinder.buildNonNullBinder(resultMap));

        return jsonObject.toString();
    }


    @ResponseBody
    @RequestMapping(value = "/listbytag")
    public String listbytag(HttpServletRequest request,
                            @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                            @RequestParam(value = "tagid", required = false) String tagid,
                            @RequestParam(value = "pcount", required = false, defaultValue = "10") String pSize) {
        String pid = HTTPUtil.getParam(request, "pid");
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        int pageNo = 1;
        try {
            pageNo = Integer.parseInt(pNum);
        } catch (NumberFormatException e) {
        }


        if (StringUtil.isEmpty(tagid)) {
            return WanbaResultCodeConstants.PARAM_EMPTY.getJsonString();
        }


        //活动基本信息及嘉宾列表
        WanbaActivityDTO wanbaActivityDTO = wanbaTagWebLogic.getActivityTag(tagid);


        String linekey = AskUtil.getAskLineKey(tagid, WanbaItemDomain.RECOMMEND);
        PageRows<WanbaRecommendDTO> pageRows = null;
        try {
            pageRows = wanbaAskWebLogic.queryQuestionRecommendByLineKey(request, linekey, pageNo, true, pid, false);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("activity", wanbaActivityDTO);
        resultMap.put("rows", pageRows.getRows());
        resultMap.put("page", pageRows.getPage());
        jsonObject.put("result", JsonBinder.buildNonNullBinder(resultMap));
        return jsonObject.toString();
    }

}
