package com.enjoyf.webapps.joyme.webpage.controller.comment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.contentservice.CommentDTO;
import com.enjoyf.platform.cloud.contentservice.CommentDetailDTO;
import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.BlackListHistory;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.content.ForignContentDomain;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.util.MicroAuthUtil;
import com.enjoyf.webapps.joyme.weblogic.comment.AllowCommentStatus;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-2-18
 * Time: 下午10:10
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCommentController extends BaseRestSpringController {

    protected static final String COOKIEKEY_UNO = "jmuc_uno";

    protected String getPostBody(String body, String uno) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(body);
        resolveContent.setContentUno(uno);
        resolveContent = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_POST_REPLY).process(resolveContent);

        return resolveContent.getContent();
    }

    protected String getForeignId(ForignContentDomain domain, String url) {
        String fid = "";
        if (ForignContentDomain.WIKI.equals(domain)) {
            fid = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")) + ".shtml";
        } else if (ForignContentDomain.CMS.equals(domain)) {
            fid = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        }
        return fid;
    }

    protected String getKeywords(String channel, String url) {
        String keywords = "";
        if (url.indexOf(Constant.DOMAIN) > 0) {
            if (url.startsWith("http://www.")) {
                keywords = url.substring(0, url.lastIndexOf("/")).replaceAll("http://www." + Constant.DOMAIN + "/" + channel + "/", "");
            } else {
                keywords = url.substring(0, url.lastIndexOf("/")).replaceAll("http://", "").replaceAll("." + Constant.DOMAIN + "/", "");
            }
        }
        return keywords;
    }

    protected AllowCommentStatus checkScoreAllowStatus(String uniKey, CommentDomain domain, String profileId, String ip) {
        AllowCommentStatus status = AllowCommentStatus.NO_ALLOW;
        String commentId = MD5Util.Md5(uniKey + domain.getCode());
        try {
            CommentHistory commentHistory = CommentServiceSngl.get().getCommentHistoryByCache(profileId, commentId, CommentHistoryType.SCORE, domain);
            if (commentHistory == null) {
                commentHistory = new CommentHistory();
                commentHistory.setObjectId(commentId);
                commentHistory.setDomain(domain);
                commentHistory.setProfileId(profileId);
                commentHistory.setActionIp(ip);
                commentHistory.setActionDate(new Date());
                commentHistory.setActionTimes(1);
                commentHistory.setHistoryType(CommentHistoryType.SCORE);
                CommentServiceSngl.get().createCommentHistory(commentHistory, null, null);
                return AllowCommentStatus.ALLOW;
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (df.format(new Date()).equals(df.format(commentHistory.getActionDate()))) {
                if (commentHistory.getActionTimes() >= 20) {
                    return AllowCommentStatus.TWENTY_TIMES_A_DAY_ONE_COMMENT;
                } else {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.increase(CommentHistoryField.ACTION_TIMES, 1);
                    CommentServiceSngl.get().modifyCommentHistoryByProfileId(commentId, domain, profileId, updateExpress);
                    return AllowCommentStatus.ALLOW;
                }
            } else {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(CommentHistoryField.ACTION_TIMES, 1);
                updateExpress.set(CommentHistoryField.ACTION_DATE, new Date());
                CommentServiceSngl.get().modifyCommentHistoryByProfileId(commentId, domain, profileId, updateExpress);
                return AllowCommentStatus.ALLOW;
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e:", e);
        }
        return status;
    }

    protected AllowCommentStatus checkPostReplyForbid(String profileId, String postText) {
        AllowCommentStatus status = AllowCommentStatus.NO_ALLOW;
        try {
            CommentForbid forbid = CommentServiceSngl.get().getCommentForbidByCache(profileId);
            if (forbid == null) {
                status = AllowCommentStatus.ALLOW;
            } else {
                status = AllowCommentStatus.FORBID_UNO;
            }

            //黑名单
            BlackListHistory blackListHistory = AskServiceSngl.get().getBlackListHistory(profileId);

            if (blackListHistory == null) {
                status = AllowCommentStatus.ALLOW;
            } else {
                status = AllowCommentStatus.FORBID_UNO;
            }

            String lastText = CommentServiceSngl.get().getCommentIntervalCache(profileId);
            if (!StringUtil.isEmpty(lastText) && lastText.equals(profileId)) {
                status = AllowCommentStatus.NOT_POST_SAME_TEXT_FIFTEEN_INTERVAL;
            } else if (!StringUtil.isEmpty(lastText) && lastText.equals(postText)) {
                status = AllowCommentStatus.NOT_POST_SAME_TEXT_INTERVAL;
            }


        } catch (ServiceException e) {
            GAlerter.lab("AbstractCommentController checkPostReplyForbid.profileId:" + profileId, e);
        } finally {
            return status;
        }
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
            String authorization = MicroAuthUtil.getToken();
            String urlget = WebappConfig.get().getContentServiceUrl() + "/api/comments/ids?ids=" + id;
            Response resource = OkHttpUtil.doGet(urlget, authorization, null);
            if (!resource.isSuccessful()) {
                GAlerter.lan(this.getClass().getName() + " id =" + id + " code:" + resource.code() + " message:" + resource.body().toString());
            }
            Map<Long, CommentDTO> map = JSONObject.parseObject(resource.body().string(), new TypeReference<Map<Long, CommentDTO>>() {
            });
            return map.get(Long.parseLong(id));

        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " resource error ：", e);
            return null;
        }
    }


}
