package com.enjoyf.webapps.tools.webpage.controller.apiwiki;

import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.PageRowsUtil;
import com.enjoyf.platform.cloud.domain.profileservice.VerifyProfileDTO;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.util.MicroAuthUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.gson.Gson;
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
 * Created by ericliu on 2017/6/27.
 */
@Controller
@RequestMapping("/apiwiki/verifyprofile")
public class ApiwikiVerifyProfileController extends ToolsBaseController {

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @RequestMapping("/list")
    public ModelAndView findAll(
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "errorMsg", required = false) String errorMsg) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("pageStartIndex", pageStartIndex);
        mapMessage.put("maxPageItems", pageSize);
        mapMessage.put("errorMsg", errorMsg);

        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getProfileServiceUrl() + "/api/verify-profiles?page=" + (curPage - 1) + "&size=" + pageSize + "&sort=id,DESC";
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            PageRows<VerifyProfileDTO> pageRows = PageRowsUtil.getPage(response, curPage, pageSize, VerifyProfileDTO.class);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "occured errorMsg.e:", e);
        }


        return new ModelAndView("/apiwiki/verifyprofile/list", mapMessage);
    }

    @RequestMapping("/createpage")
    public ModelAndView createVerifyProfile() {
        return new ModelAndView("/apiwiki/verifyprofile/createpage");
    }

    @RequestMapping("/create")
    public ModelAndView createVerifyProfile(@RequestParam("nick") String nick,
                                            @RequestParam("verifyInfo") String verifyInfo,
                                            @RequestParam("qq") String qq,
                                            @RequestParam("microMsg") String microMsg,
                                            @RequestParam("mobile") String mobile) {

        Map map = new HashMap();
        map.put("nick", nick);
        map.put("verifyInfo", verifyInfo);
        map.put("qq", qq);
        map.put("microMsg", microMsg);
        map.put("mobile", mobile);
        String errorMsg = "";
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByNick(nick);

            if (profile != null) {
                String authorization = MicroAuthUtil.getToken();
                String urlpost = WebappConfig.get().getProfileServiceUrl() + "/api/verify-profiles";
                JsonObject json = new JsonObject();
                json.addProperty("id", profile.getUid());
                json.addProperty("verifyInfo", verifyInfo);
                json.addProperty("qq", qq);
                json.addProperty("microMsg", microMsg);
                json.addProperty("mobile", mobile);

                Response response = OkHttpUtil.doPost(urlpost, json, authorization, null);
                if (response.code() != 200) {
                    map.put("errorMsg", "failed");
                    return new ModelAndView("/apiwiki/verifyprofile/createpage", map);
                }

            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured errorMsg.", e);
            map.put("errorMsg", "system.errorMsg");
            return new ModelAndView("/apiwiki/verifyprofile/createpage", map);

        }

        return new ModelAndView("redirect:/apiwiki/verifyprofile/list?errorMsg=");
    }


    @RequestMapping("/updatepage")
    public ModelAndView updateVerifyProfilePage(@RequestParam Long uid, @RequestParam(required = false) String errorMsg) {

        try {
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getProfileServiceUrl() + "/api/verify-profiles/" + uid;
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            VerifyProfileDTO verifyProfile = new Gson().fromJson(response.body().string(), VerifyProfileDTO.class);
            if (verifyProfile != null) {
                Map map = new HashMap();
                map.put("errorMsg", errorMsg);
                map.put("verifyProfile", verifyProfile);
                return new ModelAndView("/apiwiki/verifyprofile/updatepage", map);
            } else {
                return new ModelAndView("redirect:/apiwiki/verifyprofile/list?errorMsg=profile.not.exists");
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured errorMsg.", e);
            return new ModelAndView("redirect:/apiwiki/verifyprofile/list?errorMsg=system.errorMsg");
        }


    }

    @RequestMapping("/update")
    public ModelAndView updateVerifyProfile(@RequestParam("uid") Long uid,
                                            @RequestParam("verifyInfo") String verifyInfo,
                                            @RequestParam("qq") String qq,
                                            @RequestParam("microMsg") String microMsg,
                                            @RequestParam("mobile") String mobile) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String authorization = MicroAuthUtil.getToken();
            String urlPut = WebappConfig.get().getProfileServiceUrl() + "/api/verify-profiles/";

            JsonObject json = new JsonObject();
            json.addProperty("id", uid);
            json.addProperty("verifyInfo", verifyInfo);
            json.addProperty("qq", qq);
            json.addProperty("microMsg", microMsg);
            json.addProperty("mobile", mobile);
            Response response = OkHttpUtil.doPut(urlPut, json, authorization, null);

            if (response.code() != 200) {
                return new ModelAndView("redirect:/apiwiki/verifyprofile/updatepage?errorMsg=verifyprofile.update.failed&uid=" + uid);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured errorMsg.", e);
            return new ModelAndView("redirect:/apiwiki/verifyprofile/updatepage?errorMsg=system.errorMsg&uid=" + uid);

        }

        return new ModelAndView("redirect:/apiwiki/verifyprofile/list");
    }

    @RequestMapping("/json/delete")
    @ResponseBody
    public String deleteVerifyProfile(@RequestParam("uid") Long uid) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String authorization = MicroAuthUtil.getToken();
            String urlDelete = WebappConfig.get().getProfileServiceUrl() + "/api/verify-profiles/" + uid;
            Response response = OkHttpUtil.doDelete(urlDelete, authorization, null);

            if (response.code() != 200) {
                return i18nSource.getMessage("verifyprofile.delete.failed", null, Locale.CHINA);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured errorMsg.", e);
            return i18nSource.getMessage("system.errorMsg", null, Locale.CHINA);

        }

        return "success";
    }
}
