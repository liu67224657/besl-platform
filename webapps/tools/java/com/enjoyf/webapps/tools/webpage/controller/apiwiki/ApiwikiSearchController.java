package com.enjoyf.webapps.tools.webpage.controller.apiwiki;

import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.misc.FiledValue;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli on 2017-3-26 0026.
 */
@Controller
@RequestMapping(value = "/apiwiki/search")
public class ApiwikiSearchController extends ToolsBaseController {

    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            Map<String, String> keyMap = MiscServiceSngl.get().hgetAll(AskUtil.JOYMEWIKI_SEARCH_SUGGEST_KEY);

            List<FiledValue> listkey = null;
            if (CollectionUtil.isEmpty(listkey)) {
                listkey = new ArrayList<FiledValue>();
            }
            for (String in : keyMap.keySet()) {
                listkey.add(new FiledValue(in, keyMap.get(in)));
            }

            Collections.sort(listkey, new Comparator<FiledValue>() {
                @Override
                public int compare(FiledValue a, FiledValue b) {
                    return Long.valueOf(a.getValue()) < Long.valueOf(b.getValue()) ? 1 : (a == b ? 0 : -1);
                }
            });

            mapMessage.put("listkey", listkey);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
        }
        return new ModelAndView("/apiwiki/search/searchlist", mapMessage);
    }

    @ResponseBody
    @RequestMapping("/save")
    public String save(HttpServletRequest request) {
        try {
            String method = request.getParameter("method");
            if ("add".equals(method)) {
                String searchText = request.getParameter("searchtext");
                MiscServiceSngl.get().hset(AskUtil.JOYMEWIKI_SEARCH_SUGGEST_KEY, searchText.trim(), String.valueOf(System.currentTimeMillis()));
            } else if ("del".equals(method)) {
                String searchText = request.getParameter("searchtext");
                MiscServiceSngl.get().hdel(AskUtil.JOYMEWIKI_SEARCH_SUGGEST_KEY, searchText);
            } else if ("sort".equals(method)) {
                String searchText = request.getParameter("searchtext");
                String othertext = request.getParameter("othertext");
                if (!StringUtil.isEmpty(othertext)) {
                    String firtValue = MiscServiceSngl.get().hget(AskUtil.JOYMEWIKI_SEARCH_SUGGEST_KEY, searchText);
                    String otherValue = MiscServiceSngl.get().hget(AskUtil.JOYMEWIKI_SEARCH_SUGGEST_KEY, othertext);

                    MiscServiceSngl.get().hset(AskUtil.JOYMEWIKI_SEARCH_SUGGEST_KEY, searchText, otherValue);
                    MiscServiceSngl.get().hset(AskUtil.JOYMEWIKI_SEARCH_SUGGEST_KEY, othertext, firtValue);
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }


}
