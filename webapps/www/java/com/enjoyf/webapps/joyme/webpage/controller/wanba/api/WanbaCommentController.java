package com.enjoyf.webapps.joyme.webpage.controller.wanba.api;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ask.Answer;
import com.enjoyf.platform.service.ask.AnswerSumField;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.notice.NoticeServiceSngl;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.WanbaPointType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.ContextFilterUtils;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.Wanba.MessageListDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaReplyDTO;
import com.enjoyf.webapps.joyme.dto.comment.CommentJsonParam;
import com.enjoyf.webapps.joyme.dto.comment.MainReplyDTO;
import com.enjoyf.webapps.joyme.weblogic.comment.AllowCommentStatus;
import com.enjoyf.webapps.joyme.weblogic.comment.CommentWebLogic;
import com.enjoyf.webapps.joyme.weblogic.point.PointWebLogic;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaCommentWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.comment.AbstractCommentController;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by zhimingli on 2016/9/18 0018.
 */

@RequestMapping("/wanba/api/comment")
@Controller
public class WanbaCommentController extends AbstractCommentController {

    @Resource(name = "commentWebLogic")
    private CommentWebLogic commentWebLogic;

    @Resource(name = "wanbaCommentWebLogic")
    private WanbaCommentWebLogic wanbaCommentWebLogic;


    @Resource(name = "pointWebLogic")
    private PointWebLogic pointWebLogic;


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    //评论查询
    @ResponseBody
    @RequestMapping("/query")
    public String query(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "pid", required = false) String pid,
                        @RequestParam(value = "unikey", required = false) String uniKey,
                        @RequestParam(value = "pnum", required = false, defaultValue = "1") String pNum,
                        @RequestParam(value = "pcount", required = false, defaultValue = "20") String pSize) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();

        if (StringUtil.isEmpty(uniKey)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        String commentBeanId = CommentUtil.genCommentId(uniKey, CommentDomain.WAN_ASK_COMMENT);
        try {
            CommentBean bean = CommentServiceSngl.get().getCommentBeanById(commentBeanId);
            if (bean == null) {
                Answer answer = AskServiceSngl.get().getAnswer(Long.valueOf(uniKey));
                if (answer == null) {
                    return WanbaResultCodeConstants.WANBA_ASK_ANSWERHAS_NOT_EXISTS.getJsonString();
                }

                //创建commentbean
                bean = createBean(answer);
            }
            PageRows<MainReplyDTO> mainReplyRows = commentWebLogic.queryMainReplyDTO(bean, Integer.valueOf(pNum), Integer.valueOf(pSize), true);


            //变成玩霸的profile
            PageRows<WanbaReplyDTO> wanbaReplyDTOPageRows = wanbaCommentWebLogic.queryWanbaReplyDTO(mainReplyRows);


            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("rows", wanbaReplyDTOPageRows.getRows());
            resultMap.put("page", wanbaReplyDTOPageRows.getPage());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(resultMap));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping("/post")
    public String post(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "profilekey", required = false, defaultValue = "www") String profilekey,
                       @RequestParam(value = "unikey", required = false) String uniKey,
                       @RequestParam(value = "parentid", required = false, defaultValue = "0") Long parent_id,
                       @RequestParam(value = "parentnick", required = false, defaultValue = "0") String parentnick,
                       @RequestParam(value = "pid", required = false) String pid,
                       @RequestParam(value = "body", required = false) String body) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();

        if (StringUtil.isEmpty(uniKey) || StringUtil.isEmpty(body)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {
            //查看用户是否存在
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(pid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            ReplyBody replyBody = new ReplyBody();
            replyBody.setText(body);
            AllowCommentStatus allowStatus = checkPostReplyForbid(pid, replyBody.getText());


            if (allowStatus.equals(AllowCommentStatus.NO_ALLOW)) {//系统错误
                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
            } else if (allowStatus.equals(AllowCommentStatus.FORBID_UNO)) {//用户黑名单
                return ResultCodeConstants.COMMENT_PROFILE_FORBID.getJsonString();
            } else if (allowStatus.equals(AllowCommentStatus.NOT_POST_SAME_TEXT_FIFTEEN_INTERVAL)) {
                return ResultCodeConstants.COMMENT_POST_SAME_TEXT_FIFTEEN_INTERVAL.getJsonString();//评论需在15秒间隔
            } else if (allowStatus.equals(AllowCommentStatus.NOT_POST_SAME_TEXT_INTERVAL)) {
                return ResultCodeConstants.COMMENT_POST_SAME_TEXT_INTERVAL.getJsonString();//1分钟不能有同样的内容
            }


            //敏感词检验
            String jsonString = wanbaCommentWebLogic.checkContent(replyBody.getText());
            if (!StringUtil.isEmpty(jsonString)) {
                return jsonString;
            }


            //wanba_ask_comment
            CommentDomain domain = CommentDomain.WAN_ASK_COMMENT;
            String commentBeanId = CommentUtil.genCommentId(uniKey, domain);
            CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentBeanId);
            Answer answer = AskServiceSngl.get().getAnswer(Long.valueOf(uniKey));
            if (commentBean == null) {

                if (answer != null) {
                    //创建commentbean
                    commentBean = createBean(answer);
                }
            }

            if (parent_id > 0 && !StringUtil.isEmpty(parentnick)) {
                replyBody.setText("@" + parentnick + ":" + body);
            }


            //插入评论
            CommentReply reply = wanbaCommentWebLogic.createReply(commentBean, profile, profilekey, parent_id, replyBody, getIp(request));
            Map map = new HashMap();
            map.put("replyid", reply.getReplyId());
            jsonObject.put("result", map);

            //answer:replynum+1
            if (reply != null && reply.getReplyId() > 0 && answer != null) {
                AskServiceSngl.get().modifyAnswerSum(AnswerSumField.REPLYSUM, 1, answer.getAnswerId());
            }

            //send socialMessage
            CommentReply parentReply = null;
            if (reply != null && parent_id > 0) {
                parentReply = CommentServiceSngl.get().getCommentReply(new QueryExpress().add(QueryCriterions.eq(CommentReplyField.REPLY_ID, parent_id)));
            }

            wanbaCommentWebLogic.sendWanbaAskMessage(commentBean, parentReply, reply, answer);


            String appkey = HTTPUtil.getParam(request, "appkey");
            int point = 0;
            if (!StringUtil.isEmpty(appkey)) {
                //玩霸加积分
                point = pointWebLogic.modifyUserPoint(PointActionType.WANBA_REPLY, pid, AppUtil.getAppKey(appkey), WanbaPointType.REPLY, null);
            }
            String pointText = "";
            if (point > 0) {
                pointText = i18nSource.getMessage("point.reply.success", new Object[]{WanbaPointType.REPLY}, Locale.CHINA);
            }
            jsonObject.put("pointtext", pointText);


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }


        return jsonObject.toString();
    }


    @ResponseBody
    @RequestMapping("/remove")
    public String remove(@RequestParam(value = "unikey", required = false) String uniKey,
                         @RequestParam(value = "appkey", required = false, defaultValue = "www") String appkey,
                         @RequestParam(value = "replyid", required = false) String replyid,
                         @RequestParam(value = "pid", required = false) String pid) {

        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();

        if (StringUtil.isEmpty(uniKey) || StringUtil.isEmpty(replyid) || StringUtil.isEmpty(pid)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(pid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            CommentDomain domain = CommentDomain.WAN_ASK_COMMENT;
            String commentBeanId = CommentUtil.genCommentId(uniKey, domain);

            CommentReply reply = CommentServiceSngl.get().getCommentReplyById(commentBeanId, Long.valueOf(replyid));
            if (reply != null && reply.getReplyProfileId().equals(pid) && !reply.getRemoveStatus().equals(ActStatus.ACTED)) {
                CommentServiceSngl.get().removeCommentReply(commentBeanId, Long.valueOf(replyid), reply.getRootId(), null);

                Answer answer = AskServiceSngl.get().getAnswer(Long.valueOf(uniKey));


                //answer:replynum-1
                if (reply != null && reply.getReplyId() > 0 && answer != null) {
                    AskServiceSngl.get().modifyAnswerSum(AnswerSumField.REPLYSUM, -1, answer.getAnswerId());
                    if (reply.getParentId() > 0) {
                        NoticeServiceSngl.get().deleteUserNoticeByDestId(reply.getParentProfileId(), String.valueOf(reply.getReplyId()));
                    } else {
                        NoticeServiceSngl.get().deleteUserNoticeByDestId(answer.getAnswerProfileId(), String.valueOf(reply.getReplyId()));
                    }
                }

            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }

    //回复我的
    @ResponseBody
    @RequestMapping("/messagelist")
    public String messagelist(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "pid", required = false) String pid,
                              @RequestParam(value = "pnum", required = false, defaultValue = "1") Integer pNum,
                              @RequestParam(value = "pcount", required = false, defaultValue = "20") Integer pSize) {
        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        try {
            if (StringUtil.isEmpty(pid)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }


            Pagination pagination = new Pagination(pNum * pSize, pNum, pSize);


            PageRows<MessageListDTO> pageRows = wanbaCommentWebLogic.queryWanbaSocialMessageList(pid, pagination);

            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("rows", pageRows.getRows());
            resultMap.put("page", pageRows.getPage());
            jsonObject.put("result", JsonBinder.buildNonNullBinder(resultMap));


            if (!StringUtil.isEmpty(pid)) {
                try {
                    NoticeServiceSngl.get().readNotice(pid, "", NoticeType.REPLY);
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + " read notice error.e:", e);
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


    private CommentBean createBean(Answer answer) {
        //查询答案是否存在
        CommentJsonParam param = new CommentJsonParam();
        param.setUri(String.valueOf(answer.getAnswerId()));

        //
        String title = "";
        if (answer.getBody() != null) {
            if (answer.getAskVoice() != null && !StringUtil.isEmpty(answer.getAskVoice().getUrl())) {
                title = "[音频]";
            }
            if (!StringUtil.isEmpty(answer.getBody().getText())) {
                title = title + answer.getBody().getText();
            }
        }
        param.setTitle(title.length() > 128 ? title.substring(0, 128) : title);
        return commentWebLogic.createCommentBean(String.valueOf(answer.getAnswerId()), CommentDomain.WAN_ASK_COMMENT, param);
    }

}
