package com.enjoyf.webapps.tools.webpage.controller.video;

import com.enjoyf.platform.cloudfile.BucketInfo;
import com.enjoyf.platform.cloudfile.FileHandlerFactory;
import com.enjoyf.platform.cloudfile.QiniuUploadHandler;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterService;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weibo4j.model.Query;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by xupeng on 16/3/18.
 */
@Controller
@RequestMapping(value = "/video")
public class VideoController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "type", required = false) String type,
                             @RequestParam(value = "gamesdk", required = false) String gamesdk,
                             @RequestParam(value = "actstatus", required = false) String actstatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;

        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);


        try {
            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(title)) {
                queryExpress.add(QueryCriterions.like(CommentVideoField.VIDEO_TITLE, "%" + title + "%"));
                mapMessage.put("title", title);
            }
            if (!StringUtil.isEmpty(type)) {
                queryExpress.add(QueryCriterions.eq(CommentVideoField.COMMENT_VIDEO_TYPE, Integer.parseInt(type)));
                mapMessage.put("type", type);
            }
            if (!StringUtil.isEmpty(gamesdk)) {
                queryExpress.add(QueryCriterions.eq(CommentVideoField.SDK_KEY, gamesdk));
                mapMessage.put("gamesdk", gamesdk);
            }
            if (!StringUtil.isEmpty(actstatus)) {
                queryExpress.add(QueryCriterions.eq(CommentVideoField.ACT_STATUS, actstatus));
                mapMessage.put("actstatus", actstatus);
            }

            queryExpress.add(QuerySort.add(CommentVideoField.CREATE_TIME, QuerySortOrder.DESC));


            PageRows<CommentVideo> pageRows = CommentServiceSngl.get().queryCommentVideoPage(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());

            List<AuthApp> appAuthList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.VIDEO_SDK.getCode())));
            mapMessage.put("gamelist", appAuthList);

            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                List<CommentVideo> commentVideos = pageRows.getRows();
                Set<String> profileIdSet = new HashSet<String>();
                for (CommentVideo commentVideo : commentVideos) {
                    if (!StringUtil.isEmpty(commentVideo.getProfileid())) {
                        profileIdSet.add(commentVideo.getProfileid());
                    }
                }
                if (!CollectionUtil.isEmpty(profileIdSet)) {
                    Map<String, Profile> profilesMap = UserCenterServiceSngl.get().queryProfiles(profileIdSet);
                    mapMessage.put("profiles", profilesMap == null ? "" : profilesMap.values());
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/video/videolist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<AuthApp> appAuthList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.VIDEO_SDK.getCode()))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode())));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uptoken", "G8_5kjfXfaufU53Da4bnGQ3YP-dhdmqct9sR6ImI:hDqktMztsq157D_Zfe1heeY659A=:eyJzY29wZSI6ImpveW1lcGljIiwiZGVhZGxpbmUiOjE0NTg4ODE0NTB9");
            mapMessage.put("token", jsonObject.toString());
            mapMessage.put("gamelist", appAuthList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/video/video_cretepage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(HttpServletRequest request, @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "gamesdk", required = false) String gameSdk,
                               @RequestParam(value = "videourl", required = false) String videourl,
                               @RequestParam(value = "videopic", required = false) String videoPic) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        CommentVideo commentVideo = new CommentVideo();
        try {
            commentVideo.setVideoTitle(title);
            commentVideo.setSdk_key(gameSdk);
            commentVideo.setAppkey(gameSdk);
            commentVideo.setVideoPic(videoPic);
            commentVideo.setCommentVideoType(CommentVideoType.BACKGROUND);
            CommentVideoUrl commentVideoUrl = new CommentVideoUrl();
            commentVideoUrl.setQnurl(videourl);
            commentVideo.setJsonUrl(commentVideoUrl);
            commentVideo.setActStatus(ActStatus.ACTING);
            commentVideo.setCreateTime(new Date());
            commentVideo.setCreateIp(getIp());

            CommentServiceSngl.get().createCommentVideo(commentVideo);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("redirect:/video/list");

    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "id", required = false) String id,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "gamesdk", required = false) String gamesdk,
                                   @RequestParam(value = "actstatus", required = false) String actstatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<AuthApp> appAuthList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.VIDEO_SDK.getCode()))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode())));
            mapMessage.put("gamelist", appAuthList);

            CommentVideo commentVideo = CommentServiceSngl.get().getCommentVideo(new QueryExpress()
                    .add(QueryCriterions.eq(CommentVideoField.COMMENT_VIDEO_ID, Long.parseLong(id))));
            mapMessage.put("commentVideo", commentVideo);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        if (!StringUtil.isEmpty(type)) {
            mapMessage.put("type", type);
        }
        if (!StringUtil.isEmpty(actstatus)) {
            mapMessage.put("actstatus", actstatus);
        }

        return new ModelAndView("/video/video_modifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(HttpServletRequest request, @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "gamesdk", required = false) String gameSdk,
                               @RequestParam(value = "videourl", required = false) String videourl,
                               @RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "actstatus", required = false) String actstatus,
                               @RequestParam(value = "videopic", required = false) String videoPic,
                               @RequestParam(value = "id", required = false) String id) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (StringUtil.isEmpty(id)) {
                new ModelAndView("redirect:/video/list");
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(CommentVideoField.VIDEO_TITLE, title);
            updateExpress.set(CommentVideoField.VIDEO_PIC, videoPic);
            updateExpress.set(CommentVideoField.SDK_KEY, gameSdk);
            CommentVideoUrl commentVideoUrl = new CommentVideoUrl();
            commentVideoUrl.setQnurl(videourl);
            updateExpress.set(CommentVideoField.JSON_URL, commentVideoUrl.toJson());

            CommentServiceSngl.get().modifyCommentVideo(updateExpress, id, "");
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("redirect:/video/list?type=" + type + "&actstatus=" + actstatus + "&gamesdk=" + gameSdk);

    }

    @RequestMapping(value = "/modifystatus")
    public ModelAndView modifyStatus(@RequestParam(value = "id", required = false) String id,
                                     @RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "url", required = false) String url,
                                     @RequestParam(value = "gamesdk", required = false, defaultValue = "") String gameSdk,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "actstatus", required = false) String actstatus) {
        try {
            if (StringUtil.isEmpty(id)) {
                new ModelAndView("redirect:/video/list");
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(CommentVideoField.ACT_STATUS, status);
            String querytype = "";
            if ("hot".equals(url)) {
                querytype = "true";
            }
            CommentServiceSngl.get().modifyCommentVideo(updateExpress, id, querytype);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }
        if ("hot".equals(url)) {
            return new ModelAndView("redirect:/hot/video/list?gamesdk=" + gameSdk);
        } else {
            return new ModelAndView("redirect:/video/list?type=" + type + "&actstatus=" + actstatus + "&gamesdk=" + gameSdk);
        }
    }


    @RequestMapping(value = "/sendhot")
    @ResponseBody
    public String sendHot(@RequestParam(value = "commentids", required = false) String commentIds,
                          @RequestParam(value = "gamekey", required = false) String gamekey) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (StringUtil.isEmpty(commentIds)) {
                return "";
            }
            List<CommentVideo> commentVideos = CommentServiceSngl.get().queryCommentVideoByList(new QueryExpress()
                    .add(QueryCriterions.eq(CommentVideoField.SDK_KEY, gamekey))
                    .add(QueryCriterions.eq(CommentVideoField.ACT_STATUS, ActStatus.ACTED.getCode())));


            String commentVideoIds[] = commentIds.split("@");
            //最多只能有6个热门视频
            if (!CollectionUtil.isEmpty(commentVideos)) {
                if (commentVideos.size() == 6) {
                    jsonObject.put("rs", "-2");
                    return jsonObject.toString();
                } else if (commentVideoIds.length + commentVideos.size() > 6) {
                    jsonObject.put("rs", "-1");
                    jsonObject.put("result", 6 - commentVideos.size());
                    return jsonObject.toString();
                }
            }

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(CommentVideoField.ACT_STATUS, ActStatus.ACTED.getCode());
            List<Long> commentVideoIdList = new ArrayList<Long>();
            for (int i = 0; i < commentVideoIds.length; i++) {
                boolean bool = CommentServiceSngl.get().modifyCommentVideo(updateExpress, commentVideoIds[i], "");
                if (bool) {
                    commentVideoIdList.add(Long.parseLong(commentVideoIds[i]));
                }
            }

            if (!CollectionUtil.isEmpty(commentVideoIdList)) {
                CommentServiceSngl.get().putHotVideoList(commentVideoIdList);
            }
            jsonObject.put("msg", "success");
            jsonObject.put("rs", "1");
            return jsonObject.toString();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return "";
    }

    @RequestMapping(value = "/gettoken")
    @ResponseBody
    public String getToken() {

        try {
            BucketInfo bucketInfo = getBucketInfo();
            String token = FileHandlerFactory.getToken(bucketInfo);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uptoken", token);
            return jsonObject.toString();
        } catch (Exception e) {

            e.printStackTrace();
            return "";
        }
    }

    private BucketInfo getBucketInfo() {
        return BucketInfo.getByCode(WebappConfig.get().getDefaultUploadBucket());
    }
}
