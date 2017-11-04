package com.enjoyf.webapps.tools.webpage.controller.gamev2;

import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.PageRowsUtil;
import com.enjoyf.platform.cloud.contentservice.game.GameTag;
import com.enjoyf.platform.cloud.enumeration.ValidStatus;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.util.MicroAuthUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ericliu on 2017/8/28.
 */
@Controller
@RequestMapping(value = "/gamev2/tag")
public class GameV2TagController {

    private final String findOneByTagNameURL = WebappConfig.get().getContentServiceUrl() + "/api/game-tags/name/";

    @RequestMapping("/getbyname")
    @ResponseBody
    public String getTag(@RequestParam String tagName) {
        try {
            String urlget = findOneByTagNameURL + tagName;
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            if (response.isSuccessful()) {
                String rs = response.body().string();
                GameTag gameTag = new Gson().fromJson(rs, GameTag.class);
                if (gameTag.getValidStatus().equals(ValidStatus.UNVALID)) {
                    return "ban";
                }
                return rs;
            } else {
                return String.valueOf(response.code());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "occured error.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "tagName", required = false, defaultValue = "") String tagName,
                             @RequestParam(value = "validStatus", required = false, defaultValue = "") String validStatus,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("validStatus", validStatus);
        mapMessage.put("tagName", tagName);
        mapMessage.put("pageStartIndex", pageStartIndex);
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/game-tags?page=" + (curPage - 1) + "&size=" + pageSize +
                    "&sort=id,desc&validStatus=" + validStatus + "&tagName=" + tagName;
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            PageRows<GameTag> pageRows = PageRowsUtil.getPage(response, curPage, pageSize, GameTag.class);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/gamev2/tag/gametaglist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        return new ModelAndView("/gamev2/tag/createpage");
    }

    @ResponseBody
    @RequestMapping(value = "/create")
    public String create(@RequestParam(value = "tagName", required = false) String tagName,
                         HttpServletRequest request) {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("tagName", tagName);
            String url = WebappConfig.get().getContentServiceUrl() + "/api/game-tags";
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doPost(url, json, authorization, null);

            //标签存在
            if (!response.isSuccessful()) {
                return ResultCodeConstants.ERROR.getJsonString();
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }


    @RequestMapping(value = "/modifystatus")
    public ModelAndView valid(@RequestParam(value = "id", required = false) Long id,
                              @RequestParam(value = "valid", required = false) String valid,
                              HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String url = WebappConfig.get().getContentServiceUrl() + "/api/game-tags/validstatus/" + id + "/" + valid;
            String authorization = MicroAuthUtil.getToken();
            OkHttpUtil.doPut(url, new JsonObject(), authorization, null);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            ///mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/gamev2/tag/list");
    }

}
