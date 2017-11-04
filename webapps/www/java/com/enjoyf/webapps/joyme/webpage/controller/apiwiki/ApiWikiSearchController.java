package com.enjoyf.webapps.joyme.webpage.controller.apiwiki;

import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.search.WikiappSearchType;
import com.enjoyf.platform.service.misc.FiledValue;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.apiwiki.ContentDTO;
import com.enjoyf.webapps.joyme.dto.joymewiki.GameDTO;
import com.enjoyf.webapps.joyme.weblogic.apiwiki.ApiwikiWebLogic;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhimingli on 2017-3-26 0026.
 * ####
 */
@RequestMapping("/api/wiki/search")
@Controller
public class ApiWikiSearchController {

    @Resource(name = "apiwikiWebLogic")
    private ApiwikiWebLogic apiwikiWebLogic;

    @ResponseBody
    @RequestMapping("/suggest")
    public String suggest(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        try {
            Map<String, String> keyMap = MiscServiceSngl.get().hgetAll(AskUtil.JOYMEWIKI_SEARCH_SUGGEST_KEY);
            List<String> searchList = new ArrayList<String>();
            if (!CollectionUtil.isEmpty(keyMap)) {
                List<FiledValue> filedValues = new ArrayList<FiledValue>();
                for (String in : keyMap.keySet()) {
                    filedValues.add(new FiledValue(in, keyMap.get(in)));
                }
                Collections.sort(filedValues, new Comparator<FiledValue>() {
                    @Override
                    public int compare(FiledValue a, FiledValue b) {
                        return Long.valueOf(a.getValue()) < Long.valueOf(b.getValue()) ? 1 : (a == b ? 0 : -1);
                    }
                });

                for (FiledValue filedValue : filedValues) {
                    searchList.add(filedValue.getKey());
                }
            }
            Map map = new HashMap();
            map.put("rows", searchList == null ? new ArrayList<String>() : searchList);
            jsonObject.put("result", map);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/content")
    public String content(HttpServletRequest request,
                          @RequestParam(value = "pnum", required = false) String pnum,
                          @RequestParam(value = "punm", required = false, defaultValue = "1") String punm,
                          @RequestParam(value = "pcount", required = false, defaultValue = "20") String pcount,
                          @RequestParam(value = "type", required = false, defaultValue = "1") String type) {
        String text = request.getParameter("text");
        int pageNo = 1;
        try {
            pageNo = Integer.parseInt(punm);

            //
            if (!StringUtil.isEmpty(pnum)) {
                pageNo = Integer.parseInt(pnum);
            }

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
            Map map = new HashMap();
            //只搜索文章
            if (WikiappSearchType.ARCHIVE.getCode() == Integer.valueOf(type)) {
                PageRows<ContentDTO> pageRows = apiwikiWebLogic.searchArchivByText(text, page);
                map.put("page", pageRows.getPage() == null ? page : pageRows.getPage());
                map.put("rows", pageRows.getRows() == null ? new ArrayList<ContentDTO>() : pageRows.getRows());
            } else if (WikiappSearchType.GAME.getCode() == Integer.valueOf(type)) {//只搜索游戏
                String version = HTTPUtil.getParam(request, "version");
                Integer verint = StringUtil.isEmpty(version) ? 0 : Integer.valueOf(version.replaceAll("\\.", ""));

                PageRows<GameDTO> pageRows = apiwikiWebLogic.searchGameByText(text, page, verint);
                map.put("page", pageRows.getPage() == null ? page : pageRows.getPage());
                map.put("rows", pageRows.getRows() == null ? new ArrayList<GameDTO>() : pageRows.getRows());
            }
            jsonObject.put("result", map);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


}
