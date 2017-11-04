package com.enjoyf.webapps.joyme.webpage.controller.wanba.api;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.Wanba.QuestionDTO;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaSearchWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhimingli on 2016/9/12 0012.
 * 显示大家正在搜
 */
@Controller
@RequestMapping("/wanba/api/search")
public class WanbaSearchController extends BaseRestSpringController {

    @Resource
    private WanbaSearchWebLogic wanbaSearchWebLogic;

    //显示大家正在搜
    @ResponseBody
    @RequestMapping(value = "/page")
    public String suggest(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());

        List<String> searchList = null;
        try {
            searchList = MiscServiceSngl.get().getRedisListKey(AskUtil.SEARCH_SUGGEST_KEY);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
//        searchList.add("阴阳师");
//        searchList.add("克鲁赛德战记");
//        searchList.add("奇迹暖暖");

        Map map = new HashMap();
        map.put("rows", searchList == null ? new ArrayList<String>() : searchList);

        jsonObject.put("result", map);
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/question")
    public String searchQuestion(HttpServletRequest request,
                                 @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum,
                                 @RequestParam(value = "pcount", required = false, defaultValue = "10") String pcount) {
        String pid = HTTPUtil.getParam(request, "pid");
        String text = request.getParameter("text");
        int pageNo = 1;
        try {
            pageNo = Integer.parseInt(pnum);
        } catch (NumberFormatException e) {
        }
        int pageSize = 10;
        try {
            pageSize = Integer.parseInt(pcount);
        } catch (NumberFormatException e) {
        }

        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            Pagination page = new Pagination(pageNo * pageSize, pageNo, pageSize);
            PageRows<QuestionDTO> pageRows = wanbaSearchWebLogic.searchQuestionByText(text, page, pid);
            Map map = new HashMap();
            map.put("page", pageRows.getPage());
            map.put("rows", pageRows.getRows());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(map));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }
}
