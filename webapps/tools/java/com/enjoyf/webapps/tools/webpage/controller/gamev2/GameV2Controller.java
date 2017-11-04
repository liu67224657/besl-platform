package com.enjoyf.webapps.tools.webpage.controller.gamev2;

import com.alibaba.fastjson.JSON;
import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.PageRowsUtil;
import com.enjoyf.platform.cloud.contentservice.game.Game;
import com.enjoyf.platform.cloud.contentservice.game.GameDTO;
import com.enjoyf.platform.cloud.contentservice.game.enumeration.GameOperStatus;
import com.enjoyf.platform.cloud.contentservice.game.enumeration.GameType;
import com.enjoyf.platform.cloud.enumeration.ValidStatus;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.util.MicroAuthUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Response;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * for new gamedb tools web
 */
@Controller
@RequestMapping(value = "/gamev2")
public class GameV2Controller extends ToolsBaseController {


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "type", required = false, defaultValue = "1") String type,
                             @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                             @RequestParam(value = "id", required = false, defaultValue = "") String id
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("type", type);
        mapMessage.put("searchText", searchText);
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            String authorization = MicroAuthUtil.getToken();
            String urlget = null;
            if (StringUtil.isEmpty(id)) {
                urlget = WebappConfig.get().getContentServiceUrl() + "/api/tools/games?page=" + (curPage - 1) + "&size=" + pageSize +
                        "&sort=createTime,desc";
            }
            if (!StringUtil.isEmpty(searchText) && type.equals("1")) {
                urlget += "&id=" + searchText;
            }
            if (!StringUtil.isEmpty(searchText) && type.equals("2")) {
                urlget += "&name=" + searchText;
            }
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            PageRows pageRows = PageRowsUtil.getPage(response, curPage, pageSize, Game.class);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/gamev2/gamelist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("gameType", GameType.values());
        mapMessage.put("operStatus", GameOperStatus.values());
        mapMessage.put("uploadtoken", getQiniuToken());
        return new ModelAndView("/gamev2/createpage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(
            @RequestParam String name,//游戏名称
            @RequestParam(required = false) String aliasName,//游戏别名
            @RequestParam(required = false) String gameTag,//游戏标签
            @RequestParam(required = false) GameType gameType,//游戏类型
            @RequestParam(required = false) GameOperStatus operStatus,
            @RequestParam(required = false, defaultValue = "false") Boolean android,
            @RequestParam(required = false, defaultValue = "false") Boolean ios,
            @RequestParam(required = false, defaultValue = "false") Boolean pc,
            @RequestParam String gameLogo,//游戏LOGO
            @RequestParam(required = false) String gameDeveloper,//游戏开发商
            @RequestParam(required = false) String gamePublisher,//游戏发行商
            @RequestParam(required = false) boolean isOnline,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String iosDownload,
            @RequestParam(required = false) String androidDownload,
            @RequestParam(required = false) String pcDownload,
            @RequestParam(required = false) String video,
            @RequestParam(required = false) String pic1,
            @RequestParam(required = false) String pic2,
            @RequestParam(required = false) String pic3,
            @RequestParam(required = false) String pic4,
            @RequestParam(required = false) String pic5,
            @RequestParam(required = false) String gameDesc,
            @RequestParam(required = false) String recommend,//一句话推荐
            @RequestParam(required = false) String recommendAuth,//一句话推荐作者
            @RequestParam(required = false) String language//语言,多以逗号分隔 ： 中文 英文 日文 其他
    ) {
        Set<String> picSet = new LinkedHashSet<String>();
        StringBuilder pic = new StringBuilder();
        if (!StringUtil.isEmpty(pic1)) {
            pic.append(pic1).append(",");
            picSet.add(pic1);
        }
        if (!StringUtil.isEmpty(pic2)) {
            picSet.add(pic2);
        }
        if (!StringUtil.isEmpty(pic3)) {
            picSet.add(pic3);
        }
        if (!StringUtil.isEmpty(pic4)) {
            picSet.add(pic4);
        }
        if (!StringUtil.isEmpty(pic5)) {
            picSet.add(pic5);
        }
        if (!CollectionUtil.isEmpty(picSet)) {
            for (String picUrl : picSet) {
                pic.append(picUrl).append(",");
            }
        }
        if (!StringUtil.isEmpty(gameTag)) {
            gameTag = gameTag.trim();
            if (gameTag.endsWith(",")) {
                gameTag = gameTag.substring(0, gameTag.lastIndexOf(","));
            }
        }

        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("aliasName", aliasName);
        json.addProperty("gameTag", gameTag);
        json.addProperty("validStatus", ValidStatus.VALID.name());
        json.addProperty("gameType", gameType.name());
        json.addProperty("operStatus", operStatus.name());
        json.addProperty("createTime", System.currentTimeMillis());
        json.addProperty("ios", ios);
        json.addProperty("android", android);
        json.addProperty("pc", pc);

        JsonObject extjson = new JsonObject();
        extjson.addProperty("gameLogo", gameLogo);
        extjson.addProperty("gameDeveloper", gameDeveloper);
        extjson.addProperty("gamePublisher", gamePublisher);
        extjson.addProperty("iosDownload", iosDownload);
        extjson.addProperty("androidDownload", androidDownload);
        extjson.addProperty("size", size);
        extjson.addProperty("video", video);
        if (!StringUtil.isEmpty(recommend)) {
            extjson.addProperty("recommend", recommend);
        }
        if (!StringUtil.isEmpty(recommendAuth)) {
            extjson.addProperty("recommendAuth", recommendAuth);
        }
        extjson.addProperty("pic", pic.toString());
        extjson.addProperty("gameDesc", gameDesc);
        extjson.addProperty("createUser", "");
        extjson.addProperty("price", price);
        extjson.addProperty("language", language);
        extjson.addProperty("iosDownload", iosDownload);
        extjson.addProperty("androidDownload", androidDownload);
        extjson.addProperty("pcDownload", pcDownload);
        extjson.addProperty("isOnline", isOnline);
        json.add("extjson", extjson);
        try {
            String urlstr = WebappConfig.get().getContentServiceUrl() + "/api/tools/games";
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doPost(urlstr, json, authorization, null);
            if (response.isSuccessful()) {
                Game gameObj = JSON.parseObject(response.body().string(), Game.class);
                if (gameObj != null && gameObj.getExtJson() != null) {
                    System.out.println(gameObj);
                }

            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " create error.e:", e);
            return new ModelAndView("redirect:/gamev2/createpage?errorMsg=system.error");
        }

        return new ModelAndView("redirect:/gamev2/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "id", required = false) Long id) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String url = WebappConfig.get().getContentServiceUrl() + "/api/tools/games/" + id;
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doGet(url, authorization, null);
            GameDTO game = JSON.parseObject(response.body().string(), GameDTO.class);
            mapMessage.put("game", game);

            mapMessage.put("gameType", GameType.values());
            mapMessage.put("operStatus", GameOperStatus.values());
            mapMessage.put("uploadtoken", getQiniuToken());

            int i = 1;
            for (String pic : game.getPicList()) {
                mapMessage.put("pic" + i, pic);
                i++;
            }

            if (!CollectionUtil.isEmpty(game.getLanguage())) {
                StringBuilder languageMsg = new StringBuilder();
                for (String language : game.getLanguage()) {
                    languageMsg.append(language).append(",");
                }
                mapMessage.put("language", languageMsg.toString().substring(0, languageMsg.length() - 1));
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");

        }
        return new ModelAndView("/gamev2/modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam String id,//游戏名称
                               @RequestParam String name,//游戏名称
                               @RequestParam(required = false) String aliasName,//游戏别名
                               @RequestParam(required = false) String gameTag,//游戏标签
                               @RequestParam(required = false) GameType gameType,//游戏类型
                               @RequestParam(required = false) GameOperStatus operStatus,
                               @RequestParam(required = false, defaultValue = "false") Boolean android,
                               @RequestParam(required = false, defaultValue = "false") Boolean ios,
                               @RequestParam(required = false, defaultValue = "false") Boolean pc,
                               @RequestParam String gameLogo,//游戏LOGO
                               @RequestParam(required = false) String gameDeveloper,//游戏开发商
                               @RequestParam(required = false) String gamePublisher,//游戏发行商
                               @RequestParam(required = false) boolean isOnline,
                               @RequestParam(required = false) String size,
                               @RequestParam(required = false) String price,
                               @RequestParam(required = false) String iosDownload,
                               @RequestParam(required = false) String androidDownload,
                               @RequestParam(required = false) String pcDownload,
                               @RequestParam(required = false) String video,
                               @RequestParam(required = false) String pic1,
                               @RequestParam(required = false) String pic2,
                               @RequestParam(required = false) String pic3,
                               @RequestParam(required = false) String pic4,
                               @RequestParam(required = false) String pic5,
                               @RequestParam(required = false) String gameDesc,
                               @RequestParam(required = false) String recommend,//一句话推荐
                               @RequestParam(required = false) String recommendAuth,//一句话推荐作者
                               @RequestParam(required = false) String language//语言,多张以逗号分隔 ： 中文 英文 日文 其他
    ) {
        Set<String> picSet = new LinkedHashSet<String>();
        StringBuilder pic = new StringBuilder();
        if (!StringUtil.isEmpty(pic1)) {
            pic.append(pic1).append(",");
            picSet.add(pic1);
        }
        if (!StringUtil.isEmpty(pic2)) {
            picSet.add(pic2);
        }
        if (!StringUtil.isEmpty(pic3)) {
            picSet.add(pic3);
        }
        if (!StringUtil.isEmpty(pic4)) {
            picSet.add(pic4);
        }
        if (!StringUtil.isEmpty(pic5)) {
            picSet.add(pic5);
        }
        if (!CollectionUtil.isEmpty(picSet)) {
            for (String picUrl : picSet) {
                pic.append(picUrl).append(",");
            }
        }
        if (!StringUtil.isEmpty(gameTag)) {
            gameTag = gameTag.trim();
            if (gameTag.endsWith(",")) {
                gameTag = gameTag.substring(0, gameTag.lastIndexOf(","));
            }
        }

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("aliasName", aliasName);
        json.addProperty("gameTag", gameTag);
        json.addProperty("validStatus", ValidStatus.VALID.name());
        json.addProperty("gameType", gameType.name());
        json.addProperty("operStatus", operStatus.name());
        json.addProperty("createTime", System.currentTimeMillis());
        json.addProperty("ios", ios);
        json.addProperty("android", android);
        json.addProperty("pc", pc);

        JsonObject extjson = new JsonObject();
        extjson.addProperty("gameLogo", gameLogo);
        extjson.addProperty("gameDeveloper", gameDeveloper);
        extjson.addProperty("gamePublisher", gamePublisher);
        extjson.addProperty("iosDownload", iosDownload);
        extjson.addProperty("androidDownload", androidDownload);
        extjson.addProperty("size", size);
        extjson.addProperty("video", video);
        if (!StringUtil.isEmpty(recommend)) {
            extjson.addProperty("recommend", recommend);
        }
        if (!StringUtil.isEmpty(recommendAuth)) {
            extjson.addProperty("recommendAuth", recommendAuth);
        }
        extjson.addProperty("pic", pic.toString());
        extjson.addProperty("gameDesc", gameDesc);
        extjson.addProperty("createUser", "");
        extjson.addProperty("price", price);
        extjson.addProperty("language", language);
        extjson.addProperty("iosDownload", iosDownload);
        extjson.addProperty("androidDownload", androidDownload);
        extjson.addProperty("pcDownload", pcDownload);
        extjson.addProperty("online", isOnline);
        json.add("extjson", extjson);
        try {
            String urlstr = WebappConfig.get().getContentServiceUrl() + "/api/tools/games";
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doPut(urlstr, json, authorization, null);
            if (response.isSuccessful()) {
                Game gameObj = JSON.parseObject(response.body().string(), Game.class);
                if (gameObj != null && gameObj.getExtJson() != null) {
                    System.out.println(gameObj);
                }

            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " create error.e:", e);
            return new ModelAndView("redirect:/gamev2/createpage?errorMsg=system.error");
        }

        return new ModelAndView("redirect:/gamev2/list?type=1&searchText=" + id);
    }


    @RequestMapping(value = "/json/delete")
    @ResponseBody
    public String delete(@RequestParam(value = "id", required = false, defaultValue = "") Long gameid) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String url = WebappConfig.get().getContentServiceUrl() + "/api/tools/games/" + gameid;
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doDelete(url, authorization, null);
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return String.valueOf(response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = ResultCodeConstants.ERROR.getJsonObject();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/json/search/indexbuild")
    @ResponseBody
    public String delete() {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            String url = WebappConfig.get().getContentServiceUrl() + "/api/tools/games/search/indexbuild" ;
            String authorization = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doGet(url, authorization, null);
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return String.valueOf(response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject = ResultCodeConstants.ERROR.getJsonObject();
        }
        return jsonObject.toString();
    }

}
