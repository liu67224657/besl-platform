package com.enjoyf.webapps.tools.webpage.controller.apiwiki;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.PageRowsUtil;
import com.enjoyf.platform.cloud.contentservice.CommentDTO;
import com.enjoyf.platform.cloud.contentservice.CommentDetailDTO;
import com.enjoyf.platform.cloud.contentservice.game.Game;
import com.enjoyf.platform.cloud.domain.profileservice.VertualProfile;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by zhimingli on 2017/6/23.
 */

@Controller
@RequestMapping(value = "/apiwiki/comment")
public class ApiCommentController extends ToolsBaseController {
    private static final String WIKI_APP_KEY = "2ojbX21Pd7WqJJRWmIniM0";//wikiappkey

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "type", required = false, defaultValue = "0") String type,
                             @RequestParam(value = "status", required = false, defaultValue = "valid") String status,
                             @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                             @RequestParam(value = "order", required = false, defaultValue = "") String order,
                             HttpServletRequest request) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("type", type);
        mapMessage.put("searchText", searchText);
        mapMessage.put("status", status);
        mapMessage.put("order", order);
        try {
            int curPage = (pageStartIndex / pageSize);
            String orderBy = "createTime";
            if (!StringUtil.isEmpty(order) && !order.equals(orderBy)) {
                orderBy = order + ",createTime";
            }
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/comments?page=" + curPage + "&size=" + pageSize +
                    "&sort=" + orderBy + ",desc";
            if (type.equals("1")) {
                urlget += "&gameId=" + searchText;
            }
            if (type.equals("2")) {
                urlget += "&id=" + searchText;
            }
            if (type.equals("3")) {
                urlget += "&body=" + searchText;
            }
            urlget += "&validStatus=" + status;
            Response response = OkHttpUtil.doGet(urlget);
            PageRows<CommentDTO> pageRows = PageRowsUtil.getPage(response, curPage + 1, pageSize, CommentDTO.class);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/apiwiki/comment/commentlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(HttpServletRequest request,
                                   @RequestParam(value = "id", required = false) String id,
                                   @RequestParam(value = "status", required = false, defaultValue = "valid") String status,
                                   @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                                   @RequestParam(value = "maxPageItems", required = false, defaultValue = "15") int pageSize) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            mapMessage.put("status", status);
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/comments/detail/" + id;
            Response response = OkHttpUtil.doGet(urlget);
            if (!response.isSuccessful()) {
                GAlerter.lan("id =" + id + response.body().toString());
                return new ModelAndView("/apiwiki/comment/createpage", mapMessage);
            }
            CommentDetailDTO commentDetailDTO = JSON.parseObject(response.body().string(), CommentDetailDTO.class);
            mapMessage.put("commentDetailDTO", commentDetailDTO);
            mapMessage.put("id", id);


            if (ValidStatus.VALID.equals(ValidStatus.getByCode(status))) {
                int curPage = (pageStartIndex / pageSize) + 1;
                Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
                QueryExpress queryExpress = new QueryExpress();
                String commentId = CommentUtil.genCommentId(id, CommentDomain.WIKIAPP_COMMENT);
                queryExpress.add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId));
                queryExpress.add(QueryCriterions.eq(CommentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
                queryExpress.add(QuerySort.add(CommentReplyField.CREATE_TIME, QuerySortOrder.DESC));
                PageRows<CommentReply> pageRows = CommentServiceSngl.get().queryCommentReplyByPage(queryExpress, pagination);
                //查询评论信息
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    Set<String> pidSet = new HashSet<String>();
                    for (CommentReply commentReply : pageRows.getRows()) {
                        pidSet.add(commentReply.getReplyProfileId());
                    }
                    //查询发帖人信息
                    Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(pidSet);
                    mapMessage.put("profileMap", profileMap);
                    mapMessage.put("list", pageRows.getRows());
                }
                mapMessage.put("page", pageRows.getPage());
            } else if (ValidStatus.REMOVED.equals(ValidStatus.getByCode(status))) {
                String reason = CommentServiceSngl.get().getCommentOperReason(id, 0);
                mapMessage.put("reason", reason);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("/apiwiki/comment/detailpage", mapMessage);
    }


    @RequestMapping(value = "/updatehighQuality")
    public ModelAndView updatehighQuality(@RequestParam(value = "id", required = false) String id,
                                          @RequestParam(value = "status", required = false) String status) {
        try {
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/comments/detail/" + id + "?status=" + status;
            Response response = OkHttpUtil.doPut(urlget, new JsonObject(), authorization, null);
            return new ModelAndView("redirect:/apiwiki/comment/createpage?id=" + id);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/apiwiki/comment/createpage?id=" + id);
        }
    }

    @RequestMapping(value = "/replyCommentPage")
    public ModelAndView replyCommentPage(@RequestParam(value = "id", required = false) String id) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String commentId = CommentUtil.genCommentId(id, CommentDomain.WIKIAPP_COMMENT);
            CommentBean commentBean = CommentServiceSngl.get().getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId)));
            if (commentBean == null) {
                commentBean = new CommentBean();
                commentBean.setUri("");
                commentBean.setTitle("");
                commentBean.setDomain(CommentDomain.WIKIAPP_COMMENT);
                commentBean.setUniqueKey(id);
                commentBean.setCommentId(commentId);
                commentBean.setCreateTime(new Date());
                commentBean.setRemoveStatus(ActStatus.UNACT);
                CommentServiceSngl.get().createCommentBean(commentBean);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        mapMessage.put("id", id);
        return new ModelAndView("/apiwiki/comment/replycommentpage", mapMessage);
    }

    @RequestMapping("/get/vertualProfileList")
    @ResponseBody
    public String getVertualProfileList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,      //数据库记录索引
                                        @RequestParam(value = "rows", required = false, defaultValue = "20") int rows) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {
            int pageStartIndex = (page - 1) * rows;
            int pageSize = rows;

            int curPage = (pageStartIndex / pageSize) + 1;
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getProfileServiceUrl() + "/api/vertual-profiles?page=" + (curPage - 1) + "&size=" + pageSize + "&sort=id,DESC";
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            PageRows<VertualProfile> pageRows = PageRowsUtil.getPage(response, curPage, pageSize, VertualProfile.class);
            if (pageRows != null) {
                mapMessage.put("rows", pageRows.getRows());
                mapMessage.put("total", pageRows.getPage().getTotalRows());
            }

            return binder.toJson(mapMessage);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }

    @RequestMapping("/get/gameList")
    @ResponseBody
    public String getGameList(@RequestParam(value = "page", required = false, defaultValue = "1") int page,      //数据库记录索引
                              @RequestParam(value = "rows", required = false, defaultValue = "20") int rows,
                              @RequestParam(value = "type", required = false, defaultValue = "1") String type,
                              @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {
            int pageStartIndex = (page - 1) * rows;
            int pageSize = rows;

            int curPage = (pageStartIndex / pageSize) + 1;
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/games?page=" + (curPage - 1) + "&size=" + pageSize +
                    "&sort=createTime,desc";
            if (!StringUtil.isEmpty(searchText) && type.equals("1")) {
                urlget += "&id=" + searchText;
            }
            if (!StringUtil.isEmpty(searchText) && type.equals("2")) {
                urlget += "&name=" + searchText;
            }
            Response response = OkHttpUtil.doGet(urlget, authorization, null);
            PageRows<Game> pageRows = PageRowsUtil.getPage(response, curPage, pageSize, Game.class);
            if (pageRows != null) {
                mapMessage.put("rows", pageRows.getRows());
                mapMessage.put("total", pageRows.getPage().getTotalRows());
            }

            return binder.toJson(mapMessage);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }


    private String postreply(String text) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(text);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_POST_REPLY);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent.getContent();
    }

    @RequestMapping(value = "/reply/comment")
    @ResponseBody
    public String replyComment(@RequestParam(value = "appkey", required = false, defaultValue = "2ojbX21Pd7WqJJRWmIniM0") String appkey,
                               @RequestParam(value = "uid", required = false) String uid,
                               @RequestParam(value = "unikey", required = false) String unikey,
                               @RequestParam(value = "text", required = false) String text) {
        JSONObject jsonObject = new JSONObject();
        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(Long.parseLong(uid));
            String commentId = CommentUtil.genCommentId(unikey, CommentDomain.WIKIAPP_COMMENT);
            CommentBean commentBean = CommentServiceSngl.get().getCommentBean(new QueryExpress().add(QueryCriterions.eq(CommentBeanField.COMMENT_ID, commentId)));

            ReplyBody replyBody = ReplyBody.parse(postreply(text));
            if (replyBody == null || StringUtil.isEmpty(replyBody.getText())) {
                jsonObject.put("rs", "-1");
                jsonObject.put("errorMsg", "error.viewline.item.input.wrong.data");
                return jsonObject.toString();
            }

            CommentReply reply = new CommentReply();
            reply.setCommentId(commentBean.getCommentId());

            reply.setReplyUno(profile.getUno());
            reply.setReplyProfileId(profile.getProfileId());
            reply.setReplyProfileKey(authApp.getProfileKey());
            reply.setSubKey(commentBean.getUniqueKey());
            reply.setFloorNum(commentBean.getTotalRows() + 1);
            reply.setAgreeSum(0);
            reply.setDisagreeSum(0);
            reply.setSubReplySum(0);
            reply.setBody(replyBody);
            reply.setCreateTime(new Date());
            reply.setCreateIp(getIp());
            reply.setRemoveStatus(ActStatus.UNACT);
            reply.setTotalRows(0);

            reply.setDomain(CommentDomain.WIKIAPP_COMMENT);
            reply = CommentServiceSngl.get().createCommentReply(reply, commentBean.getTotalRows());

            modifyWikiAppReplyNum(unikey, "1");

            //查询点评信息
            CommentDTO comment = getComment(unikey);
            JsonObject json = new JsonObject();
            json.addProperty("appkey", appkey);
            json.addProperty("uid", comment == null ? 0 : comment.getUid());
            json.addProperty("commentId", unikey);
            json.addProperty("gameId", comment == null ? 0 : comment.getGameId());
            json.addProperty("replyUid", profile.getUid());
            json.addProperty("replyId", reply.getReplyId());
            //发送回复消息
            sendUserMessage(json, 2);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    private void sendUserMessage(JsonObject json, int type) {
        try {
            String authorization = com.enjoyf.platform.webapps.common.util.MicroAuthUtil.getToken();
            String urlget = "";
            switch (type) {
                case 1:
                    //回复点评
                    urlget = WebappConfig.get().getMessageServiceUrl() + "/api/wikiapp-message-events/replycomment";
                    break;
                case 2:
                    //回复新闻
                    urlget = WebappConfig.get().getMessageServiceUrl() + "/api/wikiapp-message-events/replynews";
                    break;
                case 3:
                    //刪除點評
                    urlget = WebappConfig.get().getMessageServiceUrl() + "/api/wikiapp-message-events/deletecomment";
                    break;
                case 4:
                    //恢复点评
                    urlget = WebappConfig.get().getMessageServiceUrl() + "/api/wikiapp-message-events/recovercomment";
                    break;
                default:
                    break;
            }

            Response response = OkHttpUtil.doPost(urlget, json, authorization, null);
            if (!response.isSuccessful()) {
                GAlerter.lan("send user message fail json=" + json.toString() + " type=" + type);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "send user message error", e);
        }
    }

    @RequestMapping(value = "/delete/reply")
    public ModelAndView deleteReply(@RequestParam(value = "commentId", required = false) String commentId,
                                    @RequestParam(value = "id", required = false) String id,
                                    @RequestParam(value = "rid", required = false) String rid) {
        try {
            CommentReply reply = CommentServiceSngl.get().getCommentReplyById(commentId, Long.parseLong(rid));
            if (reply != null && !reply.getRemoveStatus().equals(ActStatus.ACTED)) {
                CommentServiceSngl.get().removeCommentReply(commentId, Long.parseLong(rid), reply.getRootId(), null);
                modifyWikiAppReplyNum(id, "0");
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/apiwiki/comment/createpage?id=" + id);

    }

    @RequestMapping(value = "/deleteComment")
    public ModelAndView deleteComment(@RequestParam(value = "id", required = false) String id,
                                      @RequestParam(value = "reason", required = false) String reason
    ) {
        try {
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/comments/tools/" + id + "?status=removed";
            Response response = OkHttpUtil.doPut(urlget, new JsonObject(), authorization, null);
            if (!response.isSuccessful()) {
                GAlerter.lan("id =" + id + response.body().toString());
                return new ModelAndView("redirect:/apiwiki/comment/createpage?id=" + id);
            }
            Boolean bool = JSON.parseObject(response.body().string(), Boolean.class);
            if (bool) {
                //放入删除原因进redis
                CommentServiceSngl.get().addCommentOperReason(id, reason, 0);
                CommentDTO comment = getComment(id);
                JsonObject json = new JsonObject();
                json.addProperty("appkey", WIKI_APP_KEY);
                json.addProperty("uid", comment.getUid());
                json.addProperty("gameId", comment.getGameId());
                json.addProperty("commentId", comment.getId());
                json.addProperty("reason", reason);
                //发送删除点评消息
                sendUserMessage(json, 3);

            }
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        return new ModelAndView("redirect:/apiwiki/comment/list");
    }

    @RequestMapping(value = "/recoverComment")
    @ResponseBody
    public String recoverComment(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "gameId", required = false) String gameId,
            @RequestParam(value = "reason", required = false) String reason) {
        try {
            String getReason = CommentServiceSngl.get().getCommentOperReason(id, 1);
            if (!StringUtil.isEmpty(getReason)) {//每条点评只能恢复一次  如果有恢复原因表示已经恢复过了
                JSONObject jsonObject = ResultCodeConstants.FAILED.getJsonObject();
                jsonObject.put("error", "only.recover.once");
                return jsonObject.toString();
            }

            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/comments/tools/" + id + "?status=valid&uid=" + uid + "&gameId=" + gameId;
            Response response = OkHttpUtil.doPut(urlget, new JsonObject(), authorization, null);
            if (!response.isSuccessful()) {
                GAlerter.lan("id =" + id + response.body().toString());
                JSONObject jsonObject = ResultCodeConstants.FAILED.getJsonObject();
                jsonObject.put("error", response.headers().get("X-contentServiceApp-error"));
                return jsonObject.toString();
            }
            Boolean bool = JSON.parseObject(response.body().string(), Boolean.class);
            if (bool) {
                CommentServiceSngl.get().addCommentOperReason(id, reason, 1);
                CommentDTO comment = getComment(id);
                JsonObject json = new JsonObject();
                json.addProperty("appkey", WIKI_APP_KEY);
                json.addProperty("uid", comment.getUid());
                json.addProperty("gameId", comment.getGameId());
                json.addProperty("commentId", comment.getId());
                json.addProperty("reason", reason);
                //发送回复消息
                sendUserMessage(json, 4);
            }
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }


    @RequestMapping(value = "/postcomment/page")
    public ModelAndView postCommentPage() {

        return new ModelAndView("/apiwiki/comment/postcommentpage");
    }


    @RequestMapping(value = "/postcomment")
    public ModelAndView postComment(@RequestParam(value = "uid", required = false) String uid,
                                    @RequestParam(value = "gameid", required = false) String gameId,
                                    @RequestParam(value = "score", required = false) String score,
                                    @RequestParam(value = "body", required = false) String body) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/comments/tools/";
            JsonObject json = new JsonObject();
            json.addProperty("uid", uid);
            json.addProperty("gameId", gameId);
            json.addProperty("score", score);
            json.addProperty("body", body);

            Response response = OkHttpUtil.doPost(urlget, json, authorization, null);
            if (!response.isSuccessful()) {
                GAlerter.lan(this.getClass().getName() + " postcomment=" + response.headers().get("X-contentServiceApp-error"));
                mapMessage.put("error", response.headers().get("X-contentServiceApp-error"));
                return new ModelAndView("/apiwiki/comment/postcommentpage", mapMessage);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/apiwiki/comment/list");
    }


    /**
     * wikiAPP点评 回复OR删除 回调上报评论接口
     *
     * @param id
     * @param type 0=删除  1=新增
     */
    public void modifyWikiAppReplyNum(String id, String type) {
        try {
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/comments/tools/increply/" + id + "?type=" + type;
            Response resource = OkHttpUtil.doPut(urlget, new JsonObject(), authorization, null);
            if (!resource.isSuccessful()) {
                GAlerter.lan(this.getClass().getName() + " id =" + id + " code:" + resource.code() + " message:" + resource.body().toString());
            }
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " resource error ：", e);
        }
    }

    public CommentDTO getComment(String id) {
        try {
            String authorization = com.enjoyf.platform.webapps.common.util.MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/comments/ids?ids=" + id;
            Response resource = OkHttpUtil.doGet(urlget, authorization, null);
            if (!resource.isSuccessful()) {
                GAlerter.lan(this.getClass().getName() + " id =" + id + " code:" + resource.code() + " message:" + resource.body().toString());
            }
            Map<Long, CommentDTO> map = com.alibaba.fastjson.JSONObject.parseObject(resource.body().string(), new TypeReference<Map<Long, CommentDTO>>() {
            });
            return map.get(Long.parseLong(id));

        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " resource error ：", e);
            return null;
        }
    }
}
