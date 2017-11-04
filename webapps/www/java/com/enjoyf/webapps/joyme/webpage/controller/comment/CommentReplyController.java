package com.enjoyf.webapps.joyme.webpage.controller.comment;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.util.CookieUtil;
import com.enjoyf.webapps.joyme.dto.comment.*;
import com.enjoyf.webapps.joyme.weblogic.comment.AllowCommentStatus;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-17
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/comment/reply")
public class CommentReplyController extends AbstractCommentController {

    @Resource(name = "commentWebLogic")
    private CommentWebLogic commentWebLogic;

    @RequestMapping("/page")
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "unikey", required = false) String uniKey,
                             @RequestParam(value = "domain", required = false) String domain,
                             @RequestParam(value = "flag", required = false, defaultValue = "recent") String flag,
                             @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                             @RequestParam(value = "psize", required = false, defaultValue = "10") String pSize,
                             @RequestParam(value = "code", required = false, defaultValue = "false") String code,
                             @RequestParam(value = "errormsg", required = false) String errorMessage
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("errorMessage", errorMessage);
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            if (StringUtil.isEmpty(uniKey)) {
                mapMessage.put("errorMessage", "param.unikey.null");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
//            if (code.equals("true")) {
//                uniKey = URLEncoder.encode(uniKey, "UTF-8");
//            }
            mapMessage.put("uniKey", uniKey);

            if (StringUtil.isEmpty(domain)) {
                mapMessage.put("errorMessage", "param.domain.null");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            mapMessage.put("domain", domain);
            mapMessage.put("flag", WebUtil.replaceHtmlText(flag));

            int pageNum = Integer.valueOf(pNum);
            int pageSize = Integer.valueOf(pSize);

            String commentId = MD5Util.Md5(uniKey + domain);
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                mapMessage.put("errorMessage", "comment.bean.null");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            PageRows<MainReplyDTO> mainReplyRows = commentWebLogic.queryMainReplyDTO(commentBean, pageNum, pageSize, true);
            CommentResult result = new CommentResult();
            result.setComment_sum(commentBean.getCommentSum());
            result.setDescription(commentBean.getDescription());
            result.setPic(commentBean.getPic());
            result.setTitle(commentBean.getTitle());

            //先取referer里面的URi,如果没空，就取commbean
//            if (pageNum == 1) {
//                String referer = request.getHeader("referer");
//                if (!StringUtil.isEmpty(referer)) {
//                    commentBean.setUri(referer);
//                }
//            }
            String reUri = commentBean.getUri();
            Pattern chinesePattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
            Matcher matcher = chinesePattern.matcher(reUri);
            if (matcher.find()) {
                String chineseUrl = reUri.substring(matcher.start(), reUri.length());
                reUri = reUri.replace(chineseUrl, "") + URLEncoder.encode(chineseUrl, "UTF-8");
            }
            result.setUri(reUri);
            result.setMainreplys(mainReplyRows);
            result.setShare_sum(commentBean.getShareSum());
            result.setHotlist(new ArrayList<MainReplyDTO>());

            mapMessage.put("resultDTO", result);
            mapMessage.put("page", mainReplyRows.getPage());

            if (commentBean.getDomain().equals(CommentDomain.CMS_COMMENT)) {
                String rightHtml = null;
                try {
                    rightHtml = CommentServiceSngl.get().getRightHtmlByArticleId(commentBean);
                } catch (ServiceException e) {
                    GAlerter.lab(this.getClass().getName() + " occured get rightHtml ServiceException.e:", e);
                }
                mapMessage.put("rightHtml", rightHtml);
            }
        } catch (Exception e) {
            GAlerter.lab("CommentScoreController occur Exception.e:", e);
            mapMessage.put("errorMessage", "forign.content.null");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }
        return new ModelAndView("/views/jsp/comment/reply-page", mapMessage);
    }

    @RequestMapping("/post")
    public ModelAndView post(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "unikey", required = false) String uniKey,
                             @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey,
                             @RequestParam(value = "domain", required = false) String domain,
                             @RequestParam(value = "oid", required = false, defaultValue = "0") String oid,
                             @RequestParam(value = "pid", required = false, defaultValue = "0") String pid,
                             @RequestParam(value = "body", required = false) String body

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        String codeUniKey = "";
        try {
            codeUniKey = URLEncoder.encode(uniKey, "UTF-8");
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occur UnsupportedEncodingException.e:", e);
        }

        String uno = request.getParameter("uno");
        if (StringUtil.isEmpty(uno)) {
            uno = CookieUtil.getCookieValue(request, COOKIEKEY_UNO);
        }
        if (StringUtil.isEmpty(uno)) {
            mapMessage.put("errorMessage", "user.not.login");
            return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=user.not.login");
        }

        if (StringUtil.isEmpty(uniKey)) {
            mapMessage.put("errorMessage", "param.unikey.null");
            return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=param.unikey.null");
        }

        if (StringUtil.isEmpty(domain)) {
            mapMessage.put("errorMessage", "param.domain.null");
            return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=param.domain.null");
        }

        if (StringUtil.isEmpty(body)) {
            mapMessage.put("errorMessage", "param.body.null");
            return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=param.body.null");
        }

        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null || StringUtil.isEmpty(authApp.getProfileKey())) {
                mapMessage.put("errorMessage", "auth.app.null");
                return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=auth.app.null");
            }

            String profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            String commentId = MD5Util.Md5(uniKey + domain);
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                mapMessage.put("errorMessage", "comment.bean.null");
                return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=comment.bean.null");
            }

            ReplyBody replyBody = ReplyBody.parse(body);
            if (replyBody == null || StringUtil.isEmpty(replyBody.getText())) {
                mapMessage.put("errorMessage", ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getMsg());
                return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=" + ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getMsg());
            }

            AllowCommentStatus allowStatus = checkPostReplyForbid(profileId, replyBody.getText());
            if (allowStatus.equals(AllowCommentStatus.NO_ALLOW)) {
                return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=" + ResultCodeConstants.COMMENT_PROFILE_FORBID.getMsg());
            } else if (allowStatus.equals(AllowCommentStatus.FORBID_UNO)) {
                return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=" + ResultCodeConstants.COMMENT_PROFILE_FORBID.getMsg());
            } else if (allowStatus.equals(AllowCommentStatus.NOT_POST_SAME_TEXT_FIFTEEN_INTERVAL)) {
                return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=" + ResultCodeConstants.COMMENT_POST_SAME_TEXT_FIFTEEN_INTERVAL.getMsg());
            } else if (allowStatus.equals(AllowCommentStatus.NOT_POST_SAME_TEXT_INTERVAL)) {
                return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=" + ResultCodeConstants.COMMENT_POST_SAME_TEXT_INTERVAL.getMsg());
            }

            long rootId = 0l;
            try {
                if (!StringUtil.isEmpty(oid)) {
                    rootId = Long.valueOf(oid);
                }
            } catch (NumberFormatException e) {
                mapMessage.put("errorMessage", "param.oid.not.number");
                return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=param.oid.not.number");
            }
            String rootUno = "";
            String rootProfileId = "";
            String rootProfileKey = "";
            if (rootId > 0l) {
                CommentReply rootReply = CommentServiceSngl.get().getCommentReplyById(commentId, rootId);
                if (rootReply == null || StringUtil.isEmpty(rootReply.getReplyUno())) {
                    mapMessage.put("errorMessage", "root.null");
                    return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=root.null");
                }
                rootUno = rootReply.getReplyUno();
                rootProfileId = rootReply.getReplyProfileId();
                rootProfileKey = rootReply.getReplyProfileKey();
            }

            long parentId = 0l;
            try {
                if (!StringUtil.isEmpty(pid)) {
                    parentId = Long.valueOf(pid);
                }
            } catch (NumberFormatException e) {
                mapMessage.put("errorMessage", "param.pid.not.number");
                return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=param.pid.not.number");
            }
            String parentUno = "";
            String parentProfileId = "";
            String parentProfileKey = "";
            if (parentId > 0l) {
                CommentReply parentReply = CommentServiceSngl.get().getCommentReplyById(commentId, parentId);
                if (parentReply == null || StringUtil.isEmpty(parentReply.getReplyUno())) {
                    mapMessage.put("errorMessage", "parent.null");
                    return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=parent.null");
                }
                parentUno = parentReply.getReplyUno();
                parentProfileId = parentReply.getReplyProfileId();
                parentProfileKey = parentReply.getReplyProfileKey();
            }

            Set<String> simpleKeyword = ContextFilterUtils.getSimpleEditorBlackList(replyBody.getText());
            Set<String> postKeyword = ContextFilterUtils.getPostContainBlackList(replyBody.getText());
            if (!CollectionUtil.isEmpty(simpleKeyword) || !CollectionUtil.isEmpty(postKeyword)) {
                mapMessage.put("errorMessage", ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getMsg());
                return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=" + ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getMsg());
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
            reply.setBody(ReplyBody.parse(body));
            reply.setCreateTime(new Date());
            reply.setCreateIp(getIp(request));
            reply.setRemoveStatus(ActStatus.UNACT);
            reply.setTotalRows(0);
            reply.setFloorNum(commentBean.getTotalRows() + 1);//todo 以后删掉
            reply.setDomain(CommentDomain.getByCode(Integer.valueOf(domain)));
            CommentServiceSngl.get().createCommentReply(reply, commentBean.getTotalRows());
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=system.error");
        }
        return new ModelAndView("redirect:/comment/reply/page?unikey=" + codeUniKey + "&domain=" + domain + "&code=true");
    }


    //wiki 的
    @RequestMapping("/mobilepage")
    public ModelAndView mobilepage(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "unikey", required = false) String uniKey,
                                   @RequestParam(value = "domain", required = false) String domain,
                                   @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                                   @RequestParam(value = "psize", required = false, defaultValue = "10") String pSize,
                                   @RequestParam(value = "code", required = false, defaultValue = "false") String code,
                                   @RequestParam(value = "errormsg", required = false) String errorMessage
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("errorMessage", errorMessage);
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        try {
            if (StringUtil.isEmpty(uniKey)) {
                mapMessage.put("errorMessage", "param.unikey.null");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
//            if (code.equals("true")) {
//                uniKey = URLEncoder.decode(uniKey, "UTF-8");
//            }

            mapMessage.put("unikey", uniKey);

            if (StringUtil.isEmpty(domain)) {
                mapMessage.put("errorMessage", "param.domain.null");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            mapMessage.put("domain", domain);

            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(MD5Util.Md5(uniKey + domain));
            if (commentBean == null) {
                mapMessage.put("errorMessage", "comment.bean.null");
                return new ModelAndView("/views/jsp/common/custompage", mapMessage);
            }
            int pageNum = Integer.valueOf(pNum);
            int pageSize = Integer.valueOf(pSize);
            PageRows<MainReplyDTO> mainReplyRows = commentWebLogic.queryMainReplyDTO(commentBean, pageNum, pageSize, true);

            mapMessage.put("rows", mainReplyRows.getRows());
            mapMessage.put("commentBean", commentBean);
            mapMessage.put("page", mainReplyRows.getPage());

            //mwiki评论
            if (domain.equals("1")) {
                String unikyArr[] = uniKey.split("\\|");
                if (unikyArr.length == 2) {
                    mapMessage.put("wikikey", unikyArr[0]);
                }
            }
        } catch (Exception e) {
            GAlerter.lab("CommentScoreController occur Exception.e:", e);
            mapMessage.put("errorMessage", "forign.content.null");
            return new ModelAndView("/views/jsp/common/custompage", mapMessage);
        }

        //todo 增加参数channel，通过channel拼出页面

        //说明是客户端的,登录需要走native
        String JParam = request.getHeader("JParam");
        if (!StringUtil.isEmpty(JParam)) {
            String uid = HTTPUtil.getParam(request, "uid");
            String logindomain = HTTPUtil.getParam(request, "logindomain");
            String token = HTTPUtil.getParam(request, "token");
            String uno = HTTPUtil.getParam(request, "uno");
            mapMessage.put("token", token);
            mapMessage.put("uno", uno);
            mapMessage.put("uid", uid);
            mapMessage.put("logindomain", logindomain);
            return new ModelAndView("/views/jsp/comment/native-reply-default", mapMessage);
        }

        return new ModelAndView("/views/jsp/comment/mobile-reply-default", mapMessage);
    }

    @RequestMapping("/mobilepost")
    public ModelAndView mobilePost(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "unikey", required = false) String uniKey,
                                   @RequestParam(value = "appkey", required = false, defaultValue = "default") String appkey,
                                   @RequestParam(value = "domain", required = false) String domain,
                                   @RequestParam(value = "oid", required = false, defaultValue = "0") String oid,
                                   @RequestParam(value = "pid", required = false, defaultValue = "0") String pid,
                                   @RequestParam(value = "body", required = false) String body

    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }
        String codeUniKey = "";
        try {
            codeUniKey = URLEncoder.encode(uniKey, "UTF-8");
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occur UnsupportedEncodingException.e:", e);
        }

        String uno = request.getParameter("uno");
        if (StringUtil.isEmpty(uno)) {
            uno = CookieUtil.getCookieValue(request, COOKIEKEY_UNO);
        }
        if (StringUtil.isEmpty(uno)) {
            mapMessage.put("errorMessage", "user.not.login");
            return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=user.not.login");
        }

        if (StringUtil.isEmpty(uniKey)) {
            mapMessage.put("errorMessage", "param.unikey.null");
            return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=param.unikey.null");
        }

        if (StringUtil.isEmpty(domain)) {
            mapMessage.put("errorMessage", "param.domain.null");
            return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=param.domain.null");
        }

        if (StringUtil.isEmpty(body)) {
            mapMessage.put("errorMessage", "param.body.null");
            return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=param.body.null");
        }

        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null || StringUtil.isEmpty(authApp.getProfileKey())) {
                mapMessage.put("errorMessage", "auth.app.null");
                return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=auth.app.null");
            }

            String profileId = UserCenterUtil.getProfileId(uno, authApp.getProfileKey());
            String commentId = MD5Util.Md5(uniKey + domain);
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                mapMessage.put("errorMessage", "comment.bean.null");
                return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=comment.bean.null");
            }

            ReplyBody replyBody = ReplyBody.parse(body);
            if (replyBody == null || StringUtil.isEmpty(replyBody.getText())) {
                mapMessage.put("errorMessage", ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getMsg());
                return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=" + ResultCodeConstants.COMMENT_PARAM_BODY_NULL.getMsg());
            }

            AllowCommentStatus allowStatus = checkPostReplyForbid(profileId, replyBody.getText());
            if (allowStatus.equals(AllowCommentStatus.NO_ALLOW)) {
                return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=" + ResultCodeConstants.COMMENT_PROFILE_FORBID.getMsg());
            } else if (allowStatus.equals(AllowCommentStatus.FORBID_UNO)) {
                return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=" + ResultCodeConstants.COMMENT_PROFILE_FORBID.getMsg());
            } else if (allowStatus.equals(AllowCommentStatus.NOT_POST_SAME_TEXT_INTERVAL)) {
                return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=" + ResultCodeConstants.COMMENT_POST_SAME_TEXT_INTERVAL.getMsg());
            }

            long rootId = 0l;
            try {
                if (!StringUtil.isEmpty(oid)) {
                    rootId = Long.valueOf(oid);
                }
            } catch (NumberFormatException e) {
                mapMessage.put("errorMessage", "param.oid.not.number");
                return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=param.oid.not.number");
            }
            String rootUno = "";
            String rootProfileId = "";
            String rootProfileKey = "";
            if (rootId > 0l) {
                CommentReply rootReply = CommentServiceSngl.get().getCommentReplyById(commentId, rootId);
                if (rootReply == null || StringUtil.isEmpty(rootReply.getReplyUno())) {
                    mapMessage.put("errorMessage", "root.null");
                    return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=root.null");
                }
                rootUno = rootReply.getReplyUno();
                rootProfileId = rootReply.getReplyProfileId();
                rootProfileKey = rootReply.getReplyProfileKey();
            }

            long parentId = 0l;
            try {
                if (!StringUtil.isEmpty(pid)) {
                    parentId = Long.valueOf(pid);
                }
            } catch (NumberFormatException e) {
                mapMessage.put("errorMessage", "param.pid.not.number");
                return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=param.pid.not.number");
            }
            String parentUno = "";
            String parentProfileId = "";
            String parentProfileKey = "";
            if (parentId > 0l) {
                CommentReply parentReply = CommentServiceSngl.get().getCommentReplyById(commentId, parentId);
                if (parentReply == null || StringUtil.isEmpty(parentReply.getReplyUno())) {
                    mapMessage.put("errorMessage", "parent.null");
                    return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=parent.null");
                }
                parentUno = parentReply.getReplyUno();
                parentProfileId = parentReply.getReplyProfileId();
                parentProfileKey = parentReply.getReplyProfileKey();
            }

            Set<String> simpleKeyword = ContextFilterUtils.getSimpleEditorBlackList(replyBody.getText());
            Set<String> postKeyword = ContextFilterUtils.getPostContainBlackList(replyBody.getText());
            if (!CollectionUtil.isEmpty(simpleKeyword) || !CollectionUtil.isEmpty(postKeyword)) {
                mapMessage.put("errorMessage", ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getMsg());
                return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=" + ResultCodeConstants.COMMENT_REPLY_BODY_TEXT_ILLEGE.getMsg());
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

            reply.setBody(ReplyBody.parse(body));


            reply.setCreateTime(new Date());
            reply.setCreateIp(getIp(request));
            reply.setRemoveStatus(ActStatus.UNACT);
            reply.setTotalRows(0);
            reply.setFloorNum(commentBean.getTotalRows() + 1);//todo 以后删掉
            reply.setDomain(CommentDomain.getByCode(Integer.valueOf(domain)));
            CommentServiceSngl.get().createCommentReply(reply, commentBean.getTotalRows());
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occur Exception.e:", e);
            mapMessage.put("errorMessage", "system.error");
            return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true&errormsg=system.error");
        }
        return new ModelAndView("redirect:/comment/reply/mobilepage?unikey=" + codeUniKey + "&domain=" + domain + "&code=true");
    }

    @RequestMapping("/mpage")
    public ModelAndView mList(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "uid", required = false, defaultValue = "0") String uid,//文章、帖子、投票  的作者、发起人
                              @RequestParam(value = "unikey", required = false) String uniKey,
                              @RequestParam(value = "domain", required = false) String domain,
                              @RequestParam(value = "jsonparam", required = false) String jsonParam,
                              @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                              @RequestParam(value = "psize", required = false, defaultValue = "10") String pSize,
                              @RequestParam(value = "flag", required = false) String flag,
                              @RequestParam(value = "pid", required = false) String pid,
                              @RequestParam(value = "ordertype", required = false, defaultValue = "DESC") String orderType
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("flag", flag);
        if (!StringUtil.isEmpty(uniKey)) {
            if (uniKey.indexOf("?") >= 0) {
                uniKey = uniKey.substring(0, uniKey.indexOf("?"));
            }
        }

        String refer = request.getHeader("referer");
        mapMessage.put("refer", refer);
        try {
            UserCenterSession userCenterSession = getUserCenterSeesion(request);
            if (userCenterSession != null) {
                mapMessage.put("userSession", userCenterSession);
            }

            if (StringUtil.isEmpty(uniKey)) {
                mapMessage.put("message", "param.unikey.null");
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            mapMessage.put("unikey", uniKey);

            if (StringUtil.isEmpty(domain)) {
                mapMessage.put("message", "param.domain.null");
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            mapMessage.put("domain", domain);

            String commentId = MD5Util.Md5(uniKey + domain);
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
            if (commentBean == null) {
                mapMessage.put("message", "comment.bean.null");
                return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
            }
            mapMessage.put("commentBean", commentBean);

            if (!StringUtil.isEmpty(pid)) {
                try {
                    long parentReplyId = Long.valueOf(pid);
                    CommentReply parentReply = CommentServiceSngl.get().getCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, parentReplyId)));
                    if (parentReply != null) {
                        mapMessage.put("parentReply", parentReply);
                        Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(parentReply.getReplyProfileId());
                        if (profile != null) {
                            mapMessage.put("profile", profile);
                        }
                    }
                } catch (NumberFormatException e) {
                }
            }

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentReplyField.COMMENT_ID, commentId));
            queryExpress.add(QueryCriterions.ne(CommentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            queryExpress.add(QuerySort.add(CommentReplyField.REPLY_ID, QuerySortOrder.DESC));

            int pageNum = Integer.valueOf(pNum);
            int pageSize = Integer.valueOf(pSize);

            PageRows<CommentReply> pageRows = CommentServiceSngl.get().queryCommentReplyByPage(queryExpress, new Pagination(pageSize * pageNum, pageNum, pageSize));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
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
                mapMessage.put("list", list);
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception:", e);
            mapMessage.put("message", "system.error");
            return new ModelAndView("/views/jsp/common/custompage-wap", mapMessage);
        }
        return new ModelAndView("/views/jsp/comment/reply-page-m", mapMessage);
    }

}
