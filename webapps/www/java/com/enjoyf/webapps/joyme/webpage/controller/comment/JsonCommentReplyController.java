package com.enjoyf.webapps.joyme.webpage.controller.comment;

import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.contentservice.CommentDTO;
import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.BlackListHistory;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weibo4j.model.Comment;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.platform.webapps.common.util.MicroAuthUtil;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.util.CookieUtil;
import com.enjoyf.webapps.joyme.dto.comment.*;
import com.enjoyf.webapps.joyme.dto.vote.JSONWikiVote;
import com.enjoyf.webapps.joyme.dto.vote.JSONWikiVoteSet;
import com.enjoyf.webapps.joyme.weblogic.comment.AllowCommentStatus;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentFactory;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentWebLogic;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Response;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-10
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/jsoncomment/reply")
public class JsonCommentReplyController extends AbstractCommentController {

    @Resource(name = "commentWebLogic")
    private CommentWebLogic commentWebLogic;

    private static final String WIKI_APP_KEY = "2ojbX21Pd7WqJJRWmIniM0";//wikiappkey

    /**
     * 主楼列表
     *
     * @param request
     * @param response
     * @param uniKey
     * @param domain
     * @param jsonParam
     * @param pNum
     * @param pSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/query")
    public String query(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "uid", required = false, defaultValue = "0") String uid,//文章、帖子、投票  的作者、发起人
                        @RequestParam(value = "unikey", required = false) String uniKey,
                        @RequestParam(value = "domain", required = false) String domain,
                        @RequestParam(value = "jsonparam", required = false) String jsonParam,
                        @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                        @RequestParam(value = "psize", required = false, defaultValue = "10") String pSize,
                        @RequestParam(value = "flag", required = false) String flag,
                        @RequestParam(value = "ordertype", required = false, defaultValue = "DESC") String orderType
    ) {
        String replyId = request.getParameter("replyid");
        long rId = -1l;
        try {
            rId = Long.parseLong(replyId);
        } catch (NumberFormatException e) {
        }

        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            //数字站评分
            if (!StringUtil.isEmpty(flag) && flag.equals("avgscore")) {
                if (StringUtil.isEmpty(uniKey)) {
                    resultObjectMsg.setRs(ResultCodeConstants.ERROR.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.ERROR.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        return binder.toJson(resultObjectMsg);
                    } else {
                        return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                    }
                }
                String keyWords = uniKey.split("\\|")[0];
                List<CommentBean> list = CommentServiceSngl.get().queryCommentBeanByAvgScore(keyWords);
                List<CommentBean> returnList = new ArrayList<CommentBean>();
                for (CommentBean bean : list) {
                    //http://ms.joyme.com/images/ms/6/6d/Tx0296.png
                    //http://joymepic.joyme.com/wiki/images/ms/6/6d/Tx0296.png
                    if (!bean.getPic().startsWith("http://joymepic.joyme.com/wiki")) {
                        String pic = bean.getPic();
                        bean.setPic("http://joymepic.joyme.com/wiki" + pic.substring(pic.indexOf("/images"), pic.length()));
                        returnList.add(bean);
                    }
                }
                resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
                resultObjectMsg.setResult(returnList);
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            } else if (!StringUtil.isEmpty(flag) && flag.equals("score")) {
                JSONWikiVoteSet voteSet = JSONWikiVoteSet.parse(jsonParam);
                if (CollectionUtil.isEmpty(voteSet.getVoteSet())) {
                    resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                    resultObjectMsg.setMsg("param.error");
                    if (StringUtil.isEmpty(callback)) {
                        return binder.toJson(resultObjectMsg);
                    } else {
                        return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                    }
                }

                Set<String> idSet = new HashSet<String>();
                for (JSONWikiVote vote : voteSet.getVoteSet()) {
                    String uniqueKey = URLDecoder.decode(vote.getUrl(), "UTF-8");
                    String commentBeanId = CommentUtil.genCommentId(uniqueKey, CommentDomain.DIGITAL_SCORE);
                    idSet.add(commentBeanId);
                }

                List<CommentBean> list = new ArrayList<CommentBean>();
                if (!CollectionUtil.isEmpty(idSet)) {
                    for (String id : idSet) {
                        CommentBean bean = CommentServiceSngl.get().getCommentBeanById(id);
                        if (bean != null) {
                            DecimalFormat df = new DecimalFormat("#.0");
                            if (bean.getScoreTimes() > 0) {
                                bean.setAverageScore(Double.valueOf(df.format(bean.getScoreSum() / (double) bean.getScoreTimes())));
                            } else {
                                bean.setAverageScore(0);
                            }
                            if (!bean.getPic().startsWith("http://joymepic.joyme.com/wiki")) {
                                String pic = bean.getPic();
                                bean.setPic("http://joymepic.joyme.com/wiki" + pic.substring(pic.indexOf("/images"), pic.length()));
                            }
                            list.add(bean);
                        }
                    }
                }

                resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
                resultObjectMsg.setResult(list);
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            } else if (!StringUtil.isEmpty(flag) && flag.equals("hot")) {
                if (StringUtil.isEmpty(uniKey)) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        return binder.toJson(resultObjectMsg);
                    } else {
                        return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                    }
                }
                Pattern pattern = Pattern.compile("[0-9]+");
                if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        return binder.toJson(resultObjectMsg);
                    } else {
                        return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                    }
                }
                CommentDomain commentDomain = CommentDomain.getByCode(Integer.valueOf(domain));
                if (commentDomain == null) {
                    return ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getJsonString(callback);
                }
                String commentId = CommentUtil.genCommentId(uniKey, commentDomain);

                CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
                if (commentBean == null) {
                    if (StringUtil.isEmpty(jsonParam)) {
                        resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_JSONPARAM_NULL.getCode());
                        resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_JSONPARAM_NULL.getMsg());
                        if (StringUtil.isEmpty(callback)) {
                            return binder.toJson(resultObjectMsg);
                        } else {
                            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                        }
                    }
                    CommentJsonParam param = null;
                    try {
                        param = CommentJsonParam.parse(jsonParam);
                    } catch (Exception e) {
                        resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_COMMENTPARAM_ERROR.getCode());
                        resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_COMMENTPARAM_ERROR.getMsg());
                        if (StringUtil.isEmpty(callback)) {
                            return binder.toJson(resultObjectMsg);
                        } else {
                            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                        }
                    }

                    commentWebLogic.createCommentBean(uniKey, commentDomain, param);

                    resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        return binder.toJson(resultObjectMsg);
                    } else {
                        return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                    }
                }

                //获取热门评论的规则
                int hotSize = 1;
                if (commentBean.getTotalRows() <= 10) {
                    hotSize = 1;
                } else if (commentBean.getTotalRows() > 10 && commentBean.getTotalRows() <= 30) {
                    hotSize = 2;
                } else if (commentBean.getTotalRows() > 30 && commentBean.getTotalRows() <= 50) {
                    hotSize = 3;
                } else if (commentBean.getTotalRows() > 50 && commentBean.getTotalRows() <= 100) {
                    hotSize = 4;
                } else if (commentBean.getTotalRows() > 100) {
                    hotSize = 5;
                }
                List<MainReplyDTO> hotList = commentWebLogic.queryHotMainReplyDTO(commentBean, hotSize, true);

                int pageNum = Integer.valueOf(pNum);
                int pageSize = Integer.valueOf(pSize);
                boolean desc = true;
                if (QuerySortOrder.ASC.getCode().equalsIgnoreCase(orderType)) {
                    desc = false;
                }

                PageRows<MainReplyDTO> mainReplyRows;
                //todo  只有ugcwiki会有这个逻辑
                if (rId > 0) {
                    mainReplyRows = commentWebLogic.queryMainReplyDTOByQueryReplyId(commentBean, rId, pageSize, desc);
                } else {
                    mainReplyRows = commentWebLogic.queryMainReplyDTO(commentBean, pageNum, pageSize, desc);
                }

                CommentResult result = new CommentResult();
                result.setComment_sum(commentBean.getCommentSum());
                result.setDescription(commentBean.getDescription());
                result.setPic(commentBean.getPic());
                result.setTitle(commentBean.getTitle());
                result.setUri(commentBean.getUri());
                result.setMainreplys(mainReplyRows);
                result.setHotlist(hotList);
                result.setShare_sum(commentBean.getShareSum());

                resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
                resultObjectMsg.setResult(result);
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            } else {

                if (StringUtil.isEmpty(uniKey)) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
                Pattern pattern = Pattern.compile("[0-9]+");
                if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
                String appkey = HTTPUtil.getParam(request, "appkey");

                CommentDomain commentDomain = CommentDomain.getByCode(Integer.valueOf(domain));
                //兼容微服务
                if (WIKI_APP_KEY.equals(appkey)) {
                    String commentId = CommentUtil.genCommentId(uniKey, CommentDomain.getByCode(Integer.valueOf(domain)));
                    CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
                    if (commentBean == null) {
                        commentWebLogic.createCommentBean(uniKey, commentDomain, null);
                    }
                }
                int pageNum = Integer.valueOf(pNum);
                int pageSize = Integer.valueOf(pSize);
                boolean desc = true;
                if (QuerySortOrder.DESC.getCode().equalsIgnoreCase(orderType)) {
                    desc = true;
                } else if (QuerySortOrder.ASC.getCode().equalsIgnoreCase(orderType)) {
                    desc = false;
                }
                Long authorUid = 0l;
                if (!StringUtil.isEmpty(uid)) {
                    authorUid = Long.valueOf(uid);
                }
                if (commentDomain.equals(CommentDomain.UGCWIKI_COMMENT) && rId > 0) {
                    resultObjectMsg = CommentFactory.get().factory(commentDomain).buildResultObjectMsgByReplyId(resultObjectMsg, uniKey, commentDomain, jsonParam, rId, pageSize, authorUid, desc);
                } else {
                    resultObjectMsg = CommentFactory.get().factory(commentDomain).buildResultObjectMsg(resultObjectMsg, uniKey, commentDomain, jsonParam, pageNum, pageSize, authorUid, desc);
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
            resultObjectMsg.setRs(ResultCodeConstants.ERROR.getCode());
            resultObjectMsg.setMsg(ResultCodeConstants.ERROR.getMsg());
        }
        if (StringUtil.isEmpty(callback)) {
            return binder.toJson(resultObjectMsg);
        } else {
            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
        }
    }

    /**
     * 发评论
     *
     * @param request
     * @param response
     * @param uniKey
     * @param appkey
     * @param domain
     * @param oid
     * @param pid
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping("/post")
    public String post(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "unikey", required = false) String uniKey,
                       @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey,
                       @RequestParam(value = "domain", required = false) String domain,
                       @RequestParam(value = "oid", required = false, defaultValue = "0") String oid,
                       @RequestParam(value = "pid", required = false, defaultValue = "0") String pid,
                       @RequestParam(value = "parentid", required = false) String parentid,
                       @RequestParam(value = "body", required = false) String body

    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            String uno = request.getParameter("uno");
            if (StringUtil.isEmpty(uno)) {
                uno = CookieUtil.getCookieValue(request, COOKIEKEY_UNO);
            }
            if (StringUtil.isEmpty(uno)) {
                resultObjectMsg.setRs(ResultCodeConstants.USER_NOT_LOGIN.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USER_NOT_LOGIN.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(uniKey)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            Pattern pattern = Pattern.compile("[0-9]+");
            if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(body)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null || StringUtil.isEmpty(authApp.getProfileKey())) {
                resultObjectMsg.setRs(ResultCodeConstants.APP_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            String profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                resultObjectMsg.setRs(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            ReplyBody replyBody = ReplyBody.parse(body);
            if (replyBody == null || StringUtil.isEmpty(replyBody.getText())) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            AllowCommentStatus allowStatus = checkPostReplyForbid(profileId, replyBody.getText());
            if (allowStatus.equals(AllowCommentStatus.NO_ALLOW)) {
                resultObjectMsg.setRs(ResultCodeConstants.ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.ERROR.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            } else if (allowStatus.equals(AllowCommentStatus.FORBID_UNO)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PROFILE_FORBID.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PROFILE_FORBID.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            } else if (allowStatus.equals(AllowCommentStatus.NOT_POST_SAME_TEXT_FIFTEEN_INTERVAL)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_POST_SAME_TEXT_FIFTEEN_INTERVAL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_POST_SAME_TEXT_FIFTEEN_INTERVAL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            } else if (allowStatus.equals(AllowCommentStatus.NOT_POST_SAME_TEXT_INTERVAL)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_POST_SAME_TEXT_INTERVAL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_POST_SAME_TEXT_INTERVAL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            String commentId = MD5Util.Md5(uniKey + domain);
            CommentBean commentBean = null;
            if (!CommentDomain.WIKIAPP_COMMENT.equals(CommentDomain.getByCode(Integer.parseInt(domain)))) {
                commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
                if (commentBean == null) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_BEAN_NULL.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_BEAN_NULL.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        return binder.toJson(resultObjectMsg);
                    } else {
                        return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                    }
                }
            }

            long rootId = 0l;
            try {
                if (!StringUtil.isEmpty(oid)) {
                    rootId = Long.valueOf(oid);
                }
            } catch (NumberFormatException e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_OID_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_OID_ERROR.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            String rootUno = "";
            String rootProfileId = "";
            String rootProfileKey = "";
            CommentReply rootReply = null;
            if (rootId > 0l) {
                rootReply = CommentServiceSngl.get().getCommentReplyById(commentId, rootId);
                if (rootReply == null || StringUtil.isEmpty(rootReply.getReplyUno())) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_ROOT_REPLY_NULL.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_ROOT_REPLY_NULL.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        return binder.toJson(resultObjectMsg);
                    } else {
                        return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                    }
                }
                rootUno = rootReply.getReplyUno();
                rootProfileId = rootReply.getReplyProfileId();
                rootProfileKey = rootReply.getReplyProfileKey();
            }

            long parentId = 0l;
            try {
                //TODO 这里parentid和pid兼容，因为app客户端pid==profileid
                if (!StringUtil.isEmpty(parentid)) {
                    parentId = Long.valueOf(parentid);
                } else {
                    if (!StringUtil.isEmpty(pid)) {
                        parentId = Long.valueOf(pid);
                    }
                }
            } catch (NumberFormatException e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_PID_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_PID_ERROR.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            String parentUno = "";
            String parentProfileId = "";
            String parentProfileKey = "";
            if (parentId > 0l) {
                CommentReply parentReply = CommentServiceSngl.get().getCommentReplyById(commentId, parentId);
                if (parentReply == null || StringUtil.isEmpty(parentReply.getReplyUno())) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARENT_REPLY_NULL.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARENT_REPLY_NULL.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        return binder.toJson(resultObjectMsg);
                    } else {
                        return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                    }
                }
                parentUno = parentReply.getReplyUno();
                parentProfileId = parentReply.getReplyProfileId();
                parentProfileKey = parentReply.getReplyProfileKey();
            }

            //内容中的img标签不判断敏感词
            String illegeText = replyBody.getText();
            String regex = "<(img|IMG)[^>]*>";//todo megic code 改掉
            Pattern imgPattern = Pattern.compile(regex);
            Matcher matcher = imgPattern.matcher(illegeText);
            while (matcher.find()) {
                String img = matcher.group(0);
                illegeText = illegeText.replace(img, "");
            }
            Set<String> simpleKeyword = ContextFilterUtils.getSimpleEditorBlackList(illegeText);
            Set<String> postKeyword = ContextFilterUtils.getPostContainBlackList(illegeText);
            if (!CollectionUtil.isEmpty(simpleKeyword) || !CollectionUtil.isEmpty(postKeyword)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getMsg());
                Set<String> keyword = new HashSet<String>();
                keyword.addAll(simpleKeyword);
                keyword.addAll(postKeyword);
                resultObjectMsg.setResult(keyword);
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            CommentReply reply = new CommentReply();
            reply.setCommentId(commentId);
            if (uniKey.contains("|")) {
                reply.setSubKey(uniKey.substring(0, uniKey.indexOf("|")));
            }

            reply.setReplyUno(uno);
            reply.setReplyProfileId(profileId);
            reply.setReplyProfileKey(authApp.getProfileKey());

            reply.setRootId(rootId);
            reply.setRootUno(rootUno);
            reply.setRootProfileId(rootProfileId);
            reply.setRootProfileKey(rootProfileKey);

            reply.setParentId(parentId);
            reply.setParentUno(parentUno);
            reply.setParentProfileId(parentProfileId);
            reply.setParentProfileKey(parentProfileKey);

            reply.setAgreeSum(0);
            reply.setDisagreeSum(0);
            reply.setSubReplySum(0);
            reply.setBody(replyBody);
            reply.setCreateTime(new Date());
            reply.setCreateIp(getIp(request));
            reply.setRemoveStatus(ActStatus.UNACT);
            reply.setTotalRows(0);
            if ((rootId == 0l && parentId == 0l) || rootReply == null) {
                reply.setFloorNum(commentBean == null ? 0 : commentBean.getTotalRows() + 1);//todo 以后删掉
            } else {
                reply.setFloorNum(rootReply.getTotalRows() + 1);//todo 以后删掉
            }
            reply.setDomain(CommentDomain.getByCode(Integer.valueOf(domain)));
            reply = CommentServiceSngl.get().createCommentReply(reply, commentBean == null ? 0 : commentBean.getTotalRows());

            Set<String> profileIdSet = new HashSet<String>();
            profileIdSet.add(reply.getReplyProfileId());
            if (!StringUtil.isEmpty(reply.getParentProfileId())) {
                profileIdSet.add(reply.getParentProfileId());
            }
            Map<String, UserEntity> userMap = commentWebLogic.queryUserEntity(profileIdSet);
            ReplyDTO replyDTO = commentWebLogic.buildReplyDTO(reply, userMap);

            resultObjectMsg.setResult(replyDTO);
            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
            if (appkey.equals(WIKI_APP_KEY) && CommentDomain.WIKIAPP_COMMENT.equals(CommentDomain.getByCode(Integer.parseInt(domain)))) {
                //上报回复数+1
                modifyWikiAppReplyNum(uniKey, "1");
                //查询点评信息
                CommentDTO comment = getComment(uniKey);
                //获得回复点评人的UID
                long replyUid = userMap.get(reply.getReplyProfileId()).getUid();
                //判断是否是回复别人 如果是回复别人则给被回复人发消息 如果是回复点评则给发表点评作者发送消息
                if (parentId > 0) {
                    if (!reply.getParentProfileId().equals(reply.getReplyProfileId())) {
                        JsonObject json = new JsonObject();
                        json.addProperty("appkey", appkey);
                        json.addProperty("uid", userMap.get(reply.getParentProfileId()).getUid());
                        json.addProperty("commentId", uniKey);
                        json.addProperty("gameId", comment == null ? 0 : comment.getGameId());
                        json.addProperty("replyUid", replyUid);
                        json.addProperty("replyId", reply.getReplyId());
                        sendUserMessage(json, domain);
                    }
                } else {
                    if (comment.getUid() != replyUid) {//如果发表点评人和回复点评人不是同一人则发送推送消息
                        //上报回复点评消息
                        JsonObject json = new JsonObject();
                        json.addProperty("appkey", appkey);
                        json.addProperty("uid", comment == null ? 0 : comment.getUid());
                        json.addProperty("commentId", uniKey);
                        json.addProperty("gameId", comment == null ? 0 : comment.getGameId());
                        json.addProperty("replyUid", userMap.get(reply.getReplyProfileId()).getUid());
                        json.addProperty("replyId", reply.getReplyId());
                        sendUserMessage(json, domain);
                    }
                }
            } else if (appkey.equals(WIKI_APP_KEY)) {
                //上报新闻回复消息
                if (parentId > 0l && !reply.getParentProfileId().equals(reply.getReplyProfileId())) {
                    JsonObject json = new JsonObject();
                    json.addProperty("appkey", appkey);
                    json.addProperty("uid", userMap.get(reply.getParentProfileId()).getUid());
                    json.addProperty("title", commentBean.getTitle());
                    json.addProperty("url", commentBean.getUri());
                    json.addProperty("replyUid", userMap.get(reply.getReplyProfileId()).getUid());
                    json.addProperty("replyId", reply.getReplyId());
                    sendUserMessage(json, domain);
                }
            }
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occur Exception.e:", e);
            resultObjectMsg.setRs(ResultCodeConstants.ERROR.getCode());
            resultObjectMsg.setMsg(ResultCodeConstants.ERROR.getMsg());
            if (StringUtil.isEmpty(callback)) {
                return binder.toJson(resultObjectMsg);
            } else {
                return callback + "([" + binder.toJson(resultObjectMsg) + "])";
            }
        }
        if (StringUtil.isEmpty(callback)) {
            return binder.toJson(resultObjectMsg);
        } else {
            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
        }
    }

    private void sendUserMessage(JsonObject json, String domain) {
        try {
            String authorization = MicroAuthUtil.getToken();
            String urlget = "";
            if (CommentDomain.WIKIAPP_COMMENT.equals(CommentDomain.getByCode(Integer.parseInt(domain)))) {
                urlget = WebappConfig.get().getMessageServiceUrl() + "/api/wikiapp-message-events/replycomment";
            } else {
                urlget = WebappConfig.get().getMessageServiceUrl() + "/api/wikiapp-message-events/replynews";
            }
            Response response = OkHttpUtil.doPost(urlget, json, authorization, null);
            if (!response.isSuccessful()) {
                GAlerter.lan("send user message fail json=" + json.toString() + " domain=" + domain);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "send user message error", e);
        }
    }

    /**
     * 发评论
     *
     * @param request
     * @param response
     * @param uniKey
     * @param domain
     * @return
     */
    @ResponseBody
    @RequestMapping("/sharereport")
    public String share(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "unikey", required = false) String uniKey,
                        @RequestParam(value = "domain", required = false) String domain,
                        @RequestParam(value = "jsonparam", required = false) String jsonParam
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            if (StringUtil.isEmpty(uniKey)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            Pattern pattern = Pattern.compile("[0-9]+");
            if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            String commentId = MD5Util.Md5(uniKey + domain);
            CommentDomain commentDomain = CommentDomain.getByCode(Integer.valueOf(domain));
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                CommentJsonParam param = null;
                try {
                    param = CommentJsonParam.parse(jsonParam);
                } catch (Exception e) {
                    resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_COMMENTPARAM_ERROR.getCode());
                    resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_COMMENTPARAM_ERROR.getMsg());
                    if (StringUtil.isEmpty(callback)) {
                        return binder.toJson(resultObjectMsg);
                    } else {
                        return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                    }
                }

                commentBean = commentWebLogic.createCommentBean(uniKey, commentDomain, param);
            }
            if (commentBean != null) {
                CommentServiceSngl.get().modifyCommentBeanById(commentBean.getCommentId(), new UpdateExpress().increase(CommentBeanField.SHARE_SUM, 1));
            }
            resultObjectMsg.setResult("");
            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occur Exception.e:", e);
            resultObjectMsg.setRs(ResultCodeConstants.ERROR.getCode());
            resultObjectMsg.setMsg(ResultCodeConstants.ERROR.getMsg());
            if (StringUtil.isEmpty(callback)) {
                return binder.toJson(resultObjectMsg);
            } else {
                return callback + "([" + binder.toJson(resultObjectMsg) + "])";
            }
        }
        if (StringUtil.isEmpty(callback)) {
            return binder.toJson(resultObjectMsg);
        } else {
            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
        }
    }

    /**
     * 赞
     *
     * @param request
     * @param response
     * @param uniKey
     * @param domain
     * @param rid
     * @return
     */
    @ResponseBody
    @RequestMapping("/agree")
    public String agree(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "unikey", required = false) String uniKey,
                        @RequestParam(value = "domain", required = false) String domain,
                        @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey,
                        @RequestParam(value = "rid", required = false) String rid
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            String uno = request.getParameter("uno");
            if (StringUtil.isEmpty(uno)) {
                uno = CookieUtil.getCookieValue(request, COOKIEKEY_UNO);
            }
            if (StringUtil.isEmpty(uno)) {
                resultObjectMsg.setRs(ResultCodeConstants.USER_NOT_LOGIN.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USER_NOT_LOGIN.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(uniKey)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            Pattern pattern = Pattern.compile("[0-9]+");
            if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(rid)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_RID_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_RID_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            String commentId = MD5Util.Md5(uniKey + domain);
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            long replyId = 0l;
            try {
                replyId = Long.valueOf(rid);
            } catch (NumberFormatException e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_RID_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_RID_ERROR.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            CommentReply reply = CommentServiceSngl.get().getCommentReplyById(commentId, replyId);
            if (reply == null || reply.getRemoveStatus().equals(ActStatus.ACTED)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_REPLY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_REPLY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null || StringUtil.isEmpty(authApp.getProfileKey())) {
                resultObjectMsg.setRs(ResultCodeConstants.APP_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            CommentDomain commentDomain = CommentDomain.getByCode(Integer.valueOf(domain));
            String profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                resultObjectMsg.setRs(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            CommentHistory history = CommentServiceSngl.get().getCommentHistoryByCache(profileId, String.valueOf(replyId), CommentHistoryType.AGREE, commentDomain);
            if (history != null) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_HAS_AGREE.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_HAS_AGREE.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.increase(CommentReplyField.AGREE_SUM, 1);
            updateExpress.increase(CommentReplyField.REPLY_AGREE_SUM, 1);
            CommentServiceSngl.get().modifyCommentReplyById(commentId, replyId, updateExpress);

            CommentHistory commentHistory = new CommentHistory();
            commentHistory.setProfileId(profileId);
            commentHistory.setObjectId(String.valueOf(replyId));
            commentHistory.setHistoryType(CommentHistoryType.AGREE);
            commentHistory.setDomain(commentDomain);
            commentHistory.setActionIp(getIp(request));
            commentHistory.setActionDate(new Date());
            commentHistory.setActionTimes(1);
            CommentServiceSngl.get().createCommentHistory(commentHistory, commentBean, reply);

            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
        }
        if (StringUtil.isEmpty(callback)) {
            return binder.toJson(resultObjectMsg);
        } else {
            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
        }
    }

    /**
     * 直播内容点赞
     *
     * @param response
     * @param domain
     * @param cid
     * @return
     */
    @ResponseBody
    @RequestMapping("/commentAgree")
    public String commentAgree(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "domain", required = false) String domain,
                               @RequestParam(value = "cid", required = false) String cid) {
        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        try {

            Pattern pattern = Pattern.compile("[0-9]+");
            if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(cid)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(cid);
            if (commentBean == null) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            } else {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(CommentBeanField.SCORE_COMMENT_SUM, 1);
                CommentServiceSngl.get().modifyCommentBeanById(cid, updateExpress);

            }

            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
        }
        if (StringUtil.isEmpty(callback)) {
            return binder.toJson(resultObjectMsg);
        } else {
            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
        }
    }

    /**
     * 楼中楼列表
     *
     * @param request
     * @param response
     * @param uniKey
     * @param domain
     * @param oid
     * @param pNum
     * @param pSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/sublist")
    public String sublist(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "unikey", required = false) String uniKey,
                          @RequestParam(value = "domain", required = false) String domain,
                          @RequestParam(value = "oid", required = false, defaultValue = "0") String oid,
                          @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                          @RequestParam(value = "psize", required = false, defaultValue = "10") String pSize,
                          @RequestParam(value = "ordertype", required = false, defaultValue = "DESC") String orderType

    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            if (StringUtil.isEmpty(uniKey)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            Pattern pattern = Pattern.compile("[0-9]+");
            if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(oid)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_OID_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_OID_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            long rootId = 0l;
            try {
                rootId = Long.valueOf(oid);
            } catch (NumberFormatException e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_OID_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_OID_ERROR.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            String commentId = MD5Util.Md5(uniKey + domain);
            CommentReply rootReply = CommentServiceSngl.get().getCommentReplyById(commentId, rootId);
            if (rootReply == null) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_ROOT_REPLY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_ROOT_REPLY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            int pageNum = Integer.valueOf(pNum);
            int pageSize = Integer.valueOf(pSize);

            PageRows<ReplyDTO> returnRows = new PageRows<ReplyDTO>();
            List<ReplyDTO> returnList = new ArrayList<ReplyDTO>();

            boolean desc = true;
            if (QuerySortOrder.DESC.getCode().equalsIgnoreCase(orderType)) {
                desc = true;
            } else if (QuerySortOrder.ASC.getCode().equalsIgnoreCase(orderType)) {
                desc = false;
            }
            PageRows<CommentReply> subReplyRows = commentWebLogic.buildCommentReply(commentId, rootReply.getReplyId(), rootReply.getTotalRows(), pageNum, pageSize, desc);
            if (subReplyRows != null && !CollectionUtil.isEmpty(subReplyRows.getRows())) {
                Set<String> profileIdSet = new HashSet<String>();
                for (CommentReply subReply : subReplyRows.getRows()) {
                    profileIdSet.add(subReply.getReplyProfileId());
                    if (subReply.getParentId() != 0 && subReply.getParentId() != subReply.getRootId()) {
                        profileIdSet.add(subReply.getParentProfileId());
                    }
                }

                Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIdSet);
                Map<String, Map<String, String>> profileChooseMap = PointServiceSngl.get().queryChooseLottery(profileIdSet);
                Map<String, VerifyProfile> verifyProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIdSet);
                for (CommentReply subReply : subReplyRows.getRows()) {
                    UserEntity user = commentWebLogic.buildUserEntity(profileMap.get(subReply.getReplyProfileId()), profileChooseMap.get(subReply.getReplyProfileId()), verifyProfileMap.get(subReply.getReplyProfileId()));
                    if (user == null) {
                        continue;
                    }

                    ReplyEntity replyEntity = commentWebLogic.buildReplyEntry(subReply);

                    ReplyDTO replyDTO = new ReplyDTO();

                    replyDTO.setUser(user);
                    replyDTO.setReply(replyEntity);

                    if (subReply.getParentId() != 0 && subReply.getParentId() != subReply.getRootId()) {
                        UserEntity puser = commentWebLogic.buildUserEntity(profileMap.get(subReply.getParentProfileId()), profileChooseMap.get(subReply.getParentProfileId()), verifyProfileMap.get(subReply.getParentProfileId()));
                        replyDTO.setPuser(puser);
                    }
                    returnList.add(replyDTO);
                }

                returnRows.setPage(subReplyRows.getPage());
                returnRows.setRows(returnList);
            }
            resultObjectMsg.setResult(returnRows);
            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
        }
        if (StringUtil.isEmpty(callback)) {
            return binder.toJson(resultObjectMsg);
        } else {
            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
        }
    }

    /**
     * 踩
     *
     * @param request
     * @param response
     * @param uniKey
     * @param domain
     * @param rid
     * @return
     */
    @ResponseBody
    @RequestMapping("/disagree")
    public String disagree(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "unikey", required = false) String uniKey,
                           @RequestParam(value = "domain", required = false) String domain,
                           @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey,
                           @RequestParam(value = "rid", required = false) String rid

    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            String uno = request.getParameter("uno");
            if (StringUtil.isEmpty(uno)) {
                uno = CookieUtil.getCookieValue(request, COOKIEKEY_UNO);
            }
            if (StringUtil.isEmpty(uno)) {
                resultObjectMsg.setRs(ResultCodeConstants.USER_NOT_LOGIN.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USER_NOT_LOGIN.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(uniKey)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            Pattern pattern = Pattern.compile("[0-9]+");
            if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null || StringUtil.isEmpty(authApp.getProfileKey())) {
                resultObjectMsg.setRs(ResultCodeConstants.APP_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            String profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                resultObjectMsg.setRs(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(rid)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_RID_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_RID_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            long replyId = 0l;
            try {
                replyId = Long.valueOf(rid);
            } catch (NumberFormatException e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_RID_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_RID_ERROR.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            String commentId = MD5Util.Md5(uniKey + domain);

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.increase(CommentReplyField.AGREE_SUM, -1);

            CommentServiceSngl.get().modifyCommentReplyById(commentId, replyId, updateExpress);

            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
        }
        if (StringUtil.isEmpty(callback)) {
            return binder.toJson(resultObjectMsg);
        } else {
            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
        }
    }

    /**
     * 删除评论
     *
     * @param request
     * @param response
     * @param uniKey
     * @param domain
     * @param rid
     * @return
     */
    @ResponseBody
    @RequestMapping("/remove")
    public String remove(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "unikey", required = false) String uniKey,
                         @RequestParam(value = "domain", required = false) String domain,
                         @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey,
                         @RequestParam(value = "rid", required = false) String rid
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        JsonBinder binder = JsonBinder.buildNormalBinder();
        binder.setDateFormat("yyyy-MM-dd");
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultCodeConstants.SUCCESS.getCode());
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            String uno = request.getParameter("uno");
            if (StringUtil.isEmpty(uno)) {
                uno = CookieUtil.getCookieValue(request, COOKIEKEY_UNO);
            }
            if (StringUtil.isEmpty(uno)) {
                resultObjectMsg.setRs(ResultCodeConstants.USER_NOT_LOGIN.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USER_NOT_LOGIN.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(uniKey)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            Pattern pattern = Pattern.compile("[0-9]+");
            if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null || StringUtil.isEmpty(authApp.getProfileKey())) {
                resultObjectMsg.setRs(ResultCodeConstants.APP_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }
            String profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                resultObjectMsg.setRs(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            if (StringUtil.isEmpty(rid)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_RID_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_RID_NULL.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

            String commentId = MD5Util.Md5(uniKey + domain);
            long replyId = 0l;
            try {
                replyId = Long.valueOf(rid);
                CommentReply reply = CommentServiceSngl.get().getCommentReplyById(commentId, replyId);
                if (reply != null && reply.getReplyProfileId().equals(profileId) && !reply.getRemoveStatus().equals(ActStatus.ACTED)) {
                    CommentServiceSngl.get().removeCommentReply(commentId, replyId, reply.getRootId(), null);
                }
            } catch (NumberFormatException e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_RID_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_RID_ERROR.getMsg());
                if (StringUtil.isEmpty(callback)) {
                    return binder.toJson(resultObjectMsg);
                } else {
                    return callback + "([" + binder.toJson(resultObjectMsg) + "])";
                }
            }

//            CommentReply lastReply = CommentServiceSngl.get().getLastCommentReply(commentId);
//            if(lastReply != null){
//                Profile lastProfile = UserCenterServiceSngl.get().getProfileByProfileId(lastReply.getReplyProfileId());
//                UserEntity userEntity = commentWebLogic.buildUserEntity(lastProfile);
//                ReplyEntity replyEntity = commentWebLogic.buildReplyEntry(lastReply);
//
//                ReplyDTO reply = new ReplyDTO();
//                reply.setUser(userEntity);
//                reply.setReply(replyEntity);
//                resultObjectMsg.setResult(reply);
//            }

            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
            if (WIKI_APP_KEY.equals(appkey) && CommentDomain.WIKIAPP_COMMENT.equals(CommentDomain.getByCode(Integer.parseInt(domain)))) {
                modifyWikiAppReplyNum(uniKey, "0");
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
        }
        if (StringUtil.isEmpty(callback)) {
            return binder.toJson(resultObjectMsg);
        } else {
            return callback + "([" + binder.toJson(resultObjectMsg) + "])";
        }
    }

    @ResponseBody
    @RequestMapping("/mlist")
    public String mList(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "uid", required = false, defaultValue = "0") String uid,//文章、帖子、投票  的作者、发起人
                        @RequestParam(value = "unikey", required = false) String uniKey,
                        @RequestParam(value = "domain", required = false) String domain,
                        @RequestParam(value = "jsonparam", required = false) String jsonParam,
                        @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                        @RequestParam(value = "psize", required = false, defaultValue = "10") String pSize,
                        @RequestParam(value = "flag", required = false) String flag,
                        @RequestParam(value = "ordertype", required = false, defaultValue = "DESC") String orderType
    ) {
        String callback = HTTPUtil.getParam(request, "callback");
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            if (StringUtil.isEmpty(uniKey)) {
                if (StringUtil.isEmpty(callback)) {
                    return ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getJsonString();
                } else {
                    return callback + "([" + ResultCodeConstants.COMMENT_PARAM_UNIKEY_NULL.getJsonString() + "])";
                }
            }
            Pattern pattern = Pattern.compile("[0-9]+");
            if (StringUtil.isEmpty(domain) || (!pattern.matcher(domain).matches())) {
                if (StringUtil.isEmpty(callback)) {
                    return ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getJsonString();
                } else {
                    return callback + "([" + ResultCodeConstants.COMMENT_PARAM_DOMAIN_NULL.getJsonString() + "])";
                }
            }
            CommentDomain commentDomain = CommentDomain.getByCode(Integer.valueOf(domain));
            int pageNum = Integer.valueOf(pNum);
            int pageSize = Integer.valueOf(pSize);
            Pagination pagination = new Pagination(pageSize * pageNum, pageNum, pageSize);

            String commentId = MD5Util.Md5(uniKey + commentDomain.getCode());
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                CommentJsonParam param = null;
                if (!StringUtil.isEmpty(jsonParam)) {
                    try {
                        param = CommentJsonParam.parse(jsonParam);
                    } catch (Exception e) {
                        if (StringUtil.isEmpty(callback)) {
                            return ResultCodeConstants.COMMENT_PARAM_COMMENTPARAM_ERROR.getJsonString();
                        } else {
                            return callback + "([" + ResultCodeConstants.COMMENT_PARAM_COMMENTPARAM_ERROR.getJsonString() + "])";
                        }
                    }
                }

                CommentBean comment = new CommentBean();
                comment.setUniqueKey(uniKey);
                comment.setDomain(commentDomain);
                comment.setUri(param.getUri());
                comment.setTitle(param.getTitle());
                comment.setPic(param.getPic());
                comment.setDescription(param.getDescription());
                comment.setCreateTime(new Date());
                comment.setRemoveStatus(ActStatus.UNACT);
                comment.setTotalRows(0);
                comment.setCommentSum(0);
                comment.setScoreSum(0);
                comment.setScoreTimes(0);
                comment.setOneUserSum(0);
                comment.setTwoUserSum(0);
                comment.setThreeUserSum(0);
                comment.setFourUserSum(0);
                comment.setFiveUserSum(0);
                CommentServiceSngl.get().createCommentBean(comment);

                if (StringUtil.isEmpty(callback)) {
                    return ResultCodeConstants.SUCCESS.getJsonString();
                } else {
                    return callback + "([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
                }
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId));
            queryExpress.add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            queryExpress.add(QuerySort.add(CommentReplyField.REPLY_ID, QuerySortOrder.DESC));
            PageRows<CommentReply> pageRows = CommentServiceSngl.get().queryCommentReplyByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                PageRows<ReplyDTO> result = new PageRows<ReplyDTO>();
                result.setPage(pageRows.getPage());

                List<ReplyDTO> list = new ArrayList<ReplyDTO>();

                Set<String> profileIdSet = new HashSet<String>();
                for (CommentReply reply : pageRows.getRows()) {
                    profileIdSet.add(reply.getReplyProfileId());
                    profileIdSet.add(reply.getParentProfileId());
                }
                Map<String, UserEntity> profileMap = commentWebLogic.queryUserEntity(profileIdSet);
                for (CommentReply reply : pageRows.getRows()) {
                    ReplyDTO replyDTO = commentWebLogic.buildReplyDTO(reply, profileMap);
                    if (replyDTO != null) {
                        list.add(replyDTO);
                    }
                }
                result.setRows(list);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                jsonObject.put("result", result);

                if (StringUtil.isEmpty(callback)) {
                    return jsonObject.toString();
                } else {
                    return callback + "([" + jsonObject.toString() + "])";
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
            if (StringUtil.isEmpty(callback)) {
                return ResultCodeConstants.ERROR.getJsonString();
            } else {
                return callback + "([" + ResultCodeConstants.ERROR.getJsonString() + "])";
            }
        }
        if (StringUtil.isEmpty(callback)) {
            return ResultCodeConstants.SUCCESS.getJsonString();
        } else {
            return callback + "([" + ResultCodeConstants.SUCCESS.getJsonString() + "])";
        }
    }

    @RequestMapping("/checkForbidStatus")//查询用户封禁状态
    @ResponseBody
    public String checkForbidStatus(@RequestParam(value = "pid", required = true) String pid) {
        //黑名单
        try {
            BlackListHistory blackListHistory = AskServiceSngl.get().getBlackListHistory(pid);
            if (blackListHistory != null) {
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString();
            } else {
                return ResultCodeConstants.SUCCESS.getJsonString();
            }
        } catch (ServiceException e) {
            return ResultCodeConstants.ERROR.getJsonString();
        }
    }

    public static void main(String[] args) {
        //http://ms.joyme.com/images/ms/6/6d/Tx0296.png
        //http://joymepic.joyme.com/wiki/images/ms/6/6d/Tx0296.png
//        String pic = "http://ms.joyme.com/images/ms/6/6d/Tx0296.png";
//        if (!pic.startsWith("http://joymepic.joyme.com/wiki")) {
//            pic = "http://joymepic.joyme.com/wiki" + pic.substring(pic.indexOf("/images"), pic.length());
//            System.out.println(pic);
//        }
        try {
            System.out.println(URLEncoder.encode("_-_-_-_-_-_", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
