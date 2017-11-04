package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhimingli on 2016/12/5 0005.
 */

@Controller
@RequestMapping("/wanba/search")
public class WanbaSearchController {


    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String msg = "";
            String success = request.getParameter("success");
            if (!StringUtil.isEmpty(success)) {
                if ("success".equals(success)) {
                    msg = "成功";
                } else {
                    msg = "失败";
                }
            }
            mapMessage.put("msg", msg);
            List<String> listkey = MiscServiceSngl.get().getRedisListKey(AskUtil.SEARCH_SUGGEST_KEY);


            if (CollectionUtil.isEmpty(listkey)) {
                listkey = new ArrayList<String>();
            }

            for (int i = listkey.size(); i < 10; i++) {
                listkey.add("");
            }
            mapMessage.put("listkey", listkey);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
        }
        return new ModelAndView("/wanba/search/searchlist", mapMessage);
    }

    @RequestMapping("/save")
    public ModelAndView save(HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<String> strings = new ArrayList<String>();
            String[] guests = request.getParameterValues("pid");
            if (guests.length > 0) {
                for (int i = 0; i < guests.length; i++) {
                    String str = guests[i].trim();
                    if (!StringUtil.isEmpty(str)) {
                        strings.add(str);
                    }
                }
            }
            MiscServiceSngl.get().updateRedisListKey(AskUtil.SEARCH_SUGGEST_KEY, strings);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " exception e", e);
            e.printStackTrace();
            return new ModelAndView("redirect:/wanba/search/list?success=fail");
        }
        return new ModelAndView("redirect:/wanba/search/list?success=success");
    }
}
