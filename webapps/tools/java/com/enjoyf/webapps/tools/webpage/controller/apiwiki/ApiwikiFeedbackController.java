package com.enjoyf.webapps.tools.webpage.controller.apiwiki;

import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.PageRowsUtil;
import com.enjoyf.platform.cloud.contentservice.CommentDTO;
import com.enjoyf.platform.cloud.contentservice.Feedback;
import com.enjoyf.platform.cloud.contentservice.game.Game;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.util.MicroAuthUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Response;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by ericliu on 2017/7/3.
 */
@Controller
@RequestMapping("/apiwiki/feedback")
public class ApiwikiFeedbackController extends ToolsBaseController {

    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "feedbackType", required = false) String feedbackType,
            @RequestParam(value = "errorMsg", required = false) String error) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();


        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/feedbacks?" +
                    "page=" + (curPage - 1) + "&size=" + pageSize;

            if (!StringUtil.isEmpty(status)) {
                mapMessage.put("status", status);
                urlget += "&status=" + status;
            }

            if (!StringUtil.isEmpty(feedbackType)) {
                mapMessage.put("feedbackType", feedbackType);
                urlget += "&feedbackType=" + feedbackType;
            }

            Response response = OkHttpUtil.doGet(urlget);
            PageRows<Feedback> pageRows = PageRowsUtil.getPage(response, pageStartIndex, pageSize, Feedback.class);

            Set<Long> gameIds = new HashSet<Long>();
            Set<Long> uids = new HashSet<Long>();
            Set<Long> commentIds = new HashSet<Long>();

            for (Feedback feedback : pageRows.getRows()) {
                if (feedback.getFeedbackType().equals("GAME")) {
                    gameIds.add(feedback.getDestid());
                }
                if (feedback.getFeedbackType().equals("USER")) {
                    uids.add(feedback.getDestid());
                }
                if (feedback.getFeedbackType().equals("COMMENT")) {
                    commentIds.add(feedback.getDestid());
                }
                //todo reply先不处理
            }
            Map<Long, Game> gameMap = new HashMap<Long, Game>();
            if (!CollectionUtil.isEmpty(gameIds)) {
                gameMap.putAll(getGame(gameIds));
            }
            mapMessage.put("gameMap", gameMap);

            Map<Long, Profile> userMap = new HashMap<Long, Profile>();
            if (!CollectionUtil.isEmpty(uids)) {
                userMap.putAll(getProfiles(uids));
            }
            mapMessage.put("userMap", userMap);

            Map<Long, CommentDTO> commentMap = new HashMap<Long, CommentDTO>();
            if (!CollectionUtil.isEmpty(commentIds)) {
                commentMap.putAll(getComment(commentIds));
            }
            mapMessage.put("commentMap", commentMap);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "occured error.e:", e);
            mapMessage.put("system.error", e);
        }

        return new ModelAndView("/apiwiki/feedback/list", mapMessage);
    }

    @RequestMapping("/json/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Long id) {
        try {
            String urlDelete = WebappConfig.get().getContentServiceUrl() + "/api/feedbacks/" + id;
            String token = MicroAuthUtil.getToken();
            Response response = OkHttpUtil.doDelete(urlDelete, token, null);
            return response.code() == 200 ? "success" : "failed";
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "occured error.e:", e);
            return i18nSource.getMessage("sysyem.error", null, Locale.CHINA);
        }
    }

    @RequestMapping("/json/valid")
    @ResponseBody
    public String valid(@RequestParam("id") Long id) {
        try {
            String urlPut = WebappConfig.get().getContentServiceUrl() + "/api/feedbacks/" + id + "/status";
            String token = MicroAuthUtil.getToken();
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("status", "VALID");
            Response response = OkHttpUtil.doPut(urlPut, paramMap, token, null);
            return response.code() == 200 ? "success" : "failed";
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "occured error.e:", e);
            return i18nSource.getMessage("system.error", null, Locale.CHINA);
        }
    }


    @RequestMapping("/json/unvalid")
    @ResponseBody
    public String unvalid(@RequestParam("id") Long id) {
        try {
            String urlPut = WebappConfig.get().getContentServiceUrl() + "/api/feedbacks/" + id + "/status";
            String token = MicroAuthUtil.getToken();
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("status", "UNVALID");
            Response response = OkHttpUtil.doPut(urlPut, paramMap, token, null);
            return response.code() == 200 ? "success" : "failed";
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "occured error.e:", e);
            return i18nSource.getMessage("sysyem.error", null, Locale.CHINA);
        }
    }


    private Map<Long, Profile> getProfiles(Set<Long> uids) throws ServiceException {
        return UserCenterServiceSngl.get().queryProfilesByUids(uids);
    }

    private Map<Long, Game> getGame(Set<Long> ids) throws IOException, ServiceException {
        Map<Long, Game> result = new HashMap<Long, Game>();
        StringBuffer idsSb = new StringBuffer();
        for (Long id : ids) {
            idsSb.append(id).append(",");
        }
        String param = idsSb.substring(0, idsSb.length() - 1);
        String urlGet = WebappConfig.get().getContentServiceUrl() + "/api/game/ids?ids=" + param;
        String token = MicroAuthUtil.getToken();
        Response response = OkHttpUtil.doGet(urlGet, token, null);
        String res = response.body().string();
        Type type = new TypeToken<Map<Long, Game>>() {}.getType();
        Map<Long, Game> map = new Gson().fromJson(res, type);
        result.putAll(map);

        return result;
    }

    private Map<Long, CommentDTO> getComment(Set<Long> ids) throws IOException, ServiceException {
        Map<Long, CommentDTO> result = new HashMap<Long, CommentDTO>();
        StringBuffer idsSb = new StringBuffer();
        for (Long id : ids) {
            idsSb.append(id).append(",");
        }
        String param= idsSb.substring(0, idsSb.length() - 1);
        String urlGet = WebappConfig.get().getContentServiceUrl() + "/api/comments/ids?ids=" + param;
        String token = MicroAuthUtil.getToken();
        Response response = OkHttpUtil.doGet(urlGet, token, null);
        String res = response.body().string();
        Type type = new TypeToken<Map<Long, CommentDTO>>() {}.getType();
        Map<Long, CommentDTO> map = new Gson().fromJson(res, type);
        result.putAll(map);

        return result;
    }
}
