package com.enjoyf.webapps.tools.webpage.controller.apiwiki;

import com.alibaba.fastjson.JSON;
import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.PageRowsUtil;
import com.enjoyf.platform.cloud.domain.profileservice.VertualProfile;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.util.MicroAuthUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ericliu on 2017/6/27.
 */
@Controller
@RequestMapping("/apiwiki/vertualprofile")
public class ApiwikiVertualProfileController extends ToolsBaseController {


    @RequestMapping("/list")
    public ModelAndView findAll(
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "error", required = false) String error) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("pageStartIndex", pageStartIndex);
        mapMessage.put("maxPageItems", pageSize);
        mapMessage.put("error", error);
        mapMessage.put("text", text);
        if (type != null) {
            mapMessage.put("type", type);
        }

        if (type == null) {
            try {
                int curPage = (pageStartIndex / pageSize) + 1;
                String authorization = MicroAuthUtil.getToken();
                String urlget = WebappConfig.get().getProfileServiceUrl() + "/api/vertual-profiles?page=" + (curPage - 1) + "&size=" + pageSize + "&sort=id,DESC";
                Response response = OkHttpUtil.doGet(urlget, authorization, null);
                PageRows<VertualProfile> pageRows = PageRowsUtil.getPage(response, curPage, pageSize, VertualProfile.class);
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + "occured error.e:", e);
            }
        } else {
            try {
                String urlget = "";
                if (type == 1) {
                    try {
                        String authorization = MicroAuthUtil.getToken();
                        urlget = WebappConfig.get().getProfileServiceUrl() + "/api/vertual-profiles/" + Long.parseLong(text);
                        Response response = OkHttpUtil.doGet(urlget, authorization, null);
                        VertualProfile vertualProfile = JSON.parseObject(response.body().string(), VertualProfile.class);
                        if (vertualProfile != null) {
                            List<VertualProfile> list = new ArrayList<VertualProfile>();
                            list.add(vertualProfile);
                            mapMessage.put("list", list);
                            mapMessage.put("page", new Pagination(1 * pageSize, 1, pageSize));
                        }
                    } catch (NumberFormatException e) {
                        GAlerter.lab(this.getClass().getName() + " occured NumberFormatException:", e);
                    }
                } else {
                    urlget = WebappConfig.get().getProfileServiceUrl() + "/api/vertual-profiles/like/nick?nick=" + text;
                    Response response = OkHttpUtil.doGet(urlget);
                    List vertualProfileList = JSON.parseObject(response.body().string(), List.class);
                    mapMessage.put("list", vertualProfileList);
                    mapMessage.put("page", new Pagination(1 * pageSize, 1, pageSize));
                }
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + "occured error.e:", e);
            }
        }
        return new ModelAndView("/apiwiki/vertualprofile/list", mapMessage);
    }

    @RequestMapping("/createpage")
    public ModelAndView createVertualProfile() {
        String token = getQiniuToken();
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("uploadtoken", token);
        return new ModelAndView("/apiwiki/vertualprofile/createpage", mapMessage);
    }

    @RequestMapping("/create")
    public ModelAndView createVertualProfile(@RequestParam("nick") String nick,
                                             @RequestParam("icon") String icon,
                                             @RequestParam("sex") Integer sex,
                                             @RequestParam("description") String description) {

        String error = "";
        try {
            Profile profile = null;

            profile = UserCenterServiceSngl.get().getProfileByNick(nick);

            String urlpost = null;
            JsonObject json = null;

            if (profile == null) {
                String authorization = MicroAuthUtil.getToken();
                urlpost = WebappConfig.get().getProfileServiceUrl() + "/api/vertual-profiles";

                json = new JsonObject();
                json.addProperty("nick", nick);
                json.addProperty("icon", icon);
                json.addProperty("sex", sex);
                json.addProperty("description", description);

                Response response = OkHttpUtil.doPost(urlpost, json, authorization, null);
            } else {
                error = "profile.has.exists";
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.", e);
        }

        return new ModelAndView("redirect:/apiwiki/vertualprofile/list?error=" + error);
    }


    @RequestMapping("/updatepage")
    public ModelAndView updateVertualProfilePage(@RequestParam Long uid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getProfileServiceUrl() + "/api/vertual-profiles/" + uid;
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            VertualProfile vertualProfile = JSON.parseObject(response.body().string(), VertualProfile.class);
            if (vertualProfile != null) {
                mapMessage.put("vertualProfile", vertualProfile);
            } else {
                return new ModelAndView("redirect:/apiwiki/vertualprofile/list?error=profile.not.exists");
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.", e);
            return new ModelAndView("redirect:/apiwiki/vertualprofile/list?error=system.error");
        }

        String token = getQiniuToken();
        mapMessage.put("uploadtoken", token);
        return new ModelAndView("/apiwiki/vertualprofile/updatepage", mapMessage);
    }

    @RequestMapping("/update")
    public ModelAndView updateVertualProfile(@RequestParam("uid") Long uid,
                                             @RequestParam("nick") String nick,
                                             @RequestParam("icon") String icon,
                                             @RequestParam("sex") Integer sex,
                                             @RequestParam("description") String description) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String authorization = MicroAuthUtil.getToken();
            String urlPut = WebappConfig.get().getProfileServiceUrl() + "/api/vertual-profiles/";

            JsonObject json = new JsonObject();
            json.addProperty("id", uid);
            json.addProperty("nick", nick);
            json.addProperty("icon", icon);
            json.addProperty("sex", sex);
            json.addProperty("description", description);

            Response response = OkHttpUtil.doPut(urlPut, json, authorization, null);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.", e);
            return new ModelAndView("redirect:/apiwiki/vertualprofile/list?error=system.error");
        }

        return new ModelAndView("redirect:/apiwiki/vertualprofile/list?type=1&text=" + uid);
    }
}
