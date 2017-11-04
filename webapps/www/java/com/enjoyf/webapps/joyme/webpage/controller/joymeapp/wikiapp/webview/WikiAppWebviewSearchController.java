package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.wikiapp.webview;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp.WikiPageDTO;
import com.enjoyf.webapps.joyme.weblogic.search.SearchWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.wikiapp.AbstractWikiAppController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/20
 * Description:
 */
@Controller
@RequestMapping(value = "/joymeapp/wikiapp/webview/search")
public class WikiAppWebviewSearchController extends AbstractWikiAppController {

    @Resource(name = "searchWebLogic")
    private SearchWebLogic searchWebLogic;

    @RequestMapping(value = "/wikipage")
    public ModelAndView searchWikipage(HttpServletRequest request, HttpServletResponse response) {


        String wikikey = request.getParameter("wikikey");
        String text = request.getParameter("text");

        if (StringUtil.isEmpty(wikikey) || StringUtil.isEmpty(text)) {
            //todo error
            return new ModelAndView("/views/jsp/wikiapp/webview/search-wikipage");
        }


        Pagination pagination = getPaginationbyRequest(request);
        PageRows<WikiPageDTO> rows = searchWebLogic.searchWikipage(wikikey, text.split(" "), pagination.getCurPage(), pagination.getPageSize());

        Map map = new HashMap();
        map.put("wikikey", wikikey);
        map.put("text", text);
        map.put("result", rows.getRows());
        map.put("page", rows.getPage());

        return new ModelAndView("/views/jsp/wikiapp/webview/search-wikipage", map);
    }


}
