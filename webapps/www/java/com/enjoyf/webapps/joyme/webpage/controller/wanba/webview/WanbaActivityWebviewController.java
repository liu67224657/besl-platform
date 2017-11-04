package com.enjoyf.webapps.joyme.webpage.controller.wanba.webview;

import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.WanbaItemDomain;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaActivityDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaRecommendDTO;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaAskWebLogic;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaTagWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2016/10/19 0019.
 */

@Controller
@RequestMapping("/wanba/webview/activity")
public class WanbaActivityWebviewController {

    @Resource
    private WanbaTagWebLogic wanbaTagWebLogic;

    @Resource
    private WanbaAskWebLogic wanbaAskWebLogic;


    @RequestMapping(value = "/listbytag")
    public ModelAndView listbytag(HttpServletRequest request,
                                  @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                                  @RequestParam(value = "tagid", required = false) String tagid,
                                  @RequestParam(value = "pcount", required = false, defaultValue = "10") String pSize) {


        int pageNo = 1;
        try {
            pageNo = Integer.parseInt(pNum);
        } catch (NumberFormatException e) {
            return new ModelAndView("/views/jsp/common/custompage");
        }


        if (StringUtil.isEmpty(tagid)) {
            return new ModelAndView("/views/jsp/common/custompage");
        }


        //活动基本信息及嘉宾列表
        WanbaActivityDTO wanbaActivityDTO = wanbaTagWebLogic.getActivityTag(tagid);


        String linekey = AskUtil.getAskLineKey(tagid, WanbaItemDomain.RECOMMEND);
        PageRows<WanbaRecommendDTO> pageRows = null;
        try {
            pageRows = wanbaAskWebLogic.queryQuestionRecommendByLineKey(request, linekey, pageNo, true, "", false);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return new ModelAndView("/views/jsp/common/custompage");
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("activity", wanbaActivityDTO);
        resultMap.put("rows", pageRows.getRows());
        resultMap.put("page", pageRows.getPage());

        return new ModelAndView("/views/jsp/wanba/activity/shareactivity", resultMap);
    }


}
