package com.enjoyf.webapps.joyme.webpage.controller.wanba.api;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.WanbaItemDomain;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.Wanba.ActivityTopMenuDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaRecommendDTO;
import com.enjoyf.webapps.joyme.dto.joymeapp.gameclient.GameClientTagDTO;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaAskWebLogic;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaTagWebLogic;
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
 * 首页5个标签显示
 */
@Controller
@RequestMapping("/wanba/api/tag")
public class WanbaTagController {

    @Resource
    private WanbaTagWebLogic wanbaTagWebLogic;


    @Resource
    private WanbaAskWebLogic wanbaAskWebLogic;


    //显示标签列表
    @ResponseBody
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();

        List<GameClientTagDTO> returnRows = wanbaTagWebLogic.getTagList(request, AnimeTagAppType.WANBA_ASK);


        Map map = new HashMap();
        map.put("rows", returnRows);
        map.put("questionconfig", wanbaTagWebLogic.questionConfigList());
        jsonObject.put("result", JsonBinder.buildNonNullBinder(map));
        return jsonObject.toString();
    }


    //显示标签列表详情
    @ResponseBody
    @RequestMapping(value = "/tagdetail")
    public String tagdetail(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        String pid = HTTPUtil.getParam(request, "pid");
        String tagid = request.getParameter("tagid");
        if (StringUtil.isEmpty(tagid)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }


        int pageNo = 1;
        int tagIdInteger = 1;
        try {
            tagIdInteger = Integer.parseInt(tagid);
        } catch (NumberFormatException e) {
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        try {
            pageNo = Integer.parseInt(pnum);
        } catch (NumberFormatException e) {
        }


        WanbaItemDomain wanbaItemDomain = WanbaItemDomain.RECOMMEND;


        String linekey = AskUtil.getAskLineKey(tagid, wanbaItemDomain);
        PageRows<WanbaRecommendDTO> pageRows = null;
        try {
            pageRows = wanbaAskWebLogic.queryQuestionRecommendByLineKey(request, linekey, pageNo, true, pid, true);

            Map resultMap = new HashMap();
            //标签轮播图
            List<ActivityTopMenuDTO> hotActivityMenuList = wanbaAskWebLogic.queryActivityTopMenu(request, tagIdInteger);

            resultMap.put("head", pageNo == 1 ? hotActivityMenuList : new ArrayList<ActivityTopMenuDTO>());
            resultMap.put("rows", pageRows.getRows());
            resultMap.put("page", pageRows.getPage());

            jsonObject.put("result", JsonBinder.buildNonNullBinder(resultMap));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }
        return jsonObject.toString();
    }


}
