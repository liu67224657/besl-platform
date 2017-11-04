package com.enjoyf.webapps.tools.webpage.controller.apiwiki;

import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.PageRowsUtil;
import com.enjoyf.platform.cloud.domain.profileservice.WikiAppProfileDTO;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.util.MicroAuthUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Response;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * wikiapp推荐用户
 * Created by ericliu on 2017/6/27.
 */
@Controller
@RequestMapping("/apiwiki/recommendprofile")
public class ApiwikiRecommendProfileController extends ToolsBaseController {

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @RequestMapping("/list")
    public ModelAndView findAll(
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "errorMsg", required = false) String error) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("pageStartIndex", pageStartIndex);
        mapMessage.put("maxPageItems", pageSize);
        mapMessage.put("error", error);

        try {
            int curPage = (pageStartIndex / pageSize) + 1;

            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getProfileServiceUrl() + "/api/wikiapp-profiles/recommend/page?page=" + (curPage-1) + "&size=" + pageSize;
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            PageRows pageRows = PageRowsUtil.getPage(response, pageStartIndex, pageSize, WikiAppProfileDTO.class);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "occured error.e:", e);
        }


        return new ModelAndView("/apiwiki/recommendprofile/list", mapMessage);
    }

    @RequestMapping("/createpage")
    public ModelAndView createRecommendProfile() {
        return new ModelAndView("/apiwiki/recommendprofile/createpage");
    }

    @RequestMapping("/create")
    public ModelAndView createRecommendProfile(@RequestParam("nick") String nick) {

        Map map = new HashMap();
        map.put("nick", nick);
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);


            if (profile != null) {
                String authorization = MicroAuthUtil.getToken();
                String urlpost = WebappConfig.get().getProfileServiceUrl() + "/api/wikiapp-profiles/recommend?uid=" + profile.getUid();

                Response response = OkHttpUtil.doPost(urlpost, new JsonObject(), authorization, null);
                if (response.code() != 200) {
                    map.put("errorMsg", "recommend.profile.failed");
                    return new ModelAndView("/apiwiki/recommendprofile/createpage", map);
                }
            } else {
                map.put("errorMsg", "profile.has.notexists");
                return new ModelAndView("/apiwiki/recommendprofile/createpage", map);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.", e);
            map.put("errorMsg", "system.error");
            return new ModelAndView("/apiwiki/recommendprofile/createpage", map);
        }

        return new ModelAndView("redirect:/apiwiki/recommendprofile/list");
    }

    @RequestMapping("/json/delete")
    @ResponseBody
    public String deleteRecommendProfile(@RequestParam("uid") Long uid) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String authorization = MicroAuthUtil.getToken();
            String urlDelete = WebappConfig.get().getProfileServiceUrl() + "/api/wikiapp-profiles/recommend/" + uid;

            Response response = OkHttpUtil.doDelete(urlDelete, authorization, null);

            if (response.code() != 200) {
                return i18nSource.getMessage("recommend.delete.failed", null, Locale.CHINA);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.", e);
            return i18nSource.getMessage("system.error", null, Locale.CHINA);

        }

        return "success";
    }
}
