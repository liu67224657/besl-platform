package com.enjoyf.webapps.joyme.weblogic.wanba;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ask.Answer;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.WanbaJt;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.wanba.WanbaReplyEvent;
import com.enjoyf.platform.service.notice.NoticeServiceSngl;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.UserNotice;
import com.enjoyf.platform.service.notice.wanba.WanbaReplyBody;
import com.enjoyf.platform.service.notice.wanba.WanbaReplyBodyType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.dto.Wanba.MessageListDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaReplyDTO;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaReplyEntity;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaSocialMessageDTO;
import com.enjoyf.webapps.joyme.dto.comment.MainReplyDTO;
import com.enjoyf.webapps.joyme.dto.comment.ReplyDTO;
import com.enjoyf.webapps.joyme.dto.comment.ReplyEntity;
import com.enjoyf.webapps.joyme.dto.comment.UserEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhimingli on 2016/9/19 0019.
 */

@Service(value = "wanbaCommentWebLogic")
public class WanbaCommentWebLogic extends AbstractWanbaWebLogic {

    public PageRows<WanbaReplyDTO> queryWanbaReplyDTO(PageRows<MainReplyDTO> pageRows) {
        PageRows<WanbaReplyDTO> returnObj = new PageRows<WanbaReplyDTO>();

        if (CollectionUtil.isEmpty(pageRows.getRows())) {
            return returnObj;
        }
        returnObj.setPage(pageRows.getPage());


        try {
            List<WanbaReplyDTO> replyDTOList = new ArrayList<WanbaReplyDTO>();

            //列出集合uids
            Set<Long> uids = getUids(pageRows.getRows());

            if (!CollectionUtil.isEmpty(uids)) {
                //列出profile集合
                Map<Long, Profile> profileUidsMap = UserCenterServiceSngl.get().queryProfilesByUids(uids);
                Set<String> profileIds = new HashSet<String>();
                for (Long uid : profileUidsMap.keySet()) {
                    profileIds.add(profileUidsMap.get(uid).getProfileId());
                }

                //列出wanbaprofile集合
                Map<String, VerifyProfile> wanbaProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIds);


                for (MainReplyDTO mainReplyDTO : pageRows.getRows()) {
                    WanbaReplyDTO dto = new WanbaReplyDTO();

                    ReplyDTO replyDTO = mainReplyDTO.getReply();
                    if (replyDTO != null) {
                        ReplyEntity reply = replyDTO.getReply();
                        dto.setReply(buildWanbaReplyEntity(reply));
                        UserEntity userEntity = mainReplyDTO.getReply().getUser();
                        if (userEntity != null) {
                            Profile profile = profileUidsMap.get(userEntity.getUid());
                            if (profile != null) {
                                VerifyProfile wanbaProfile = wanbaProfileMap.get(profile.getProfileId());
                                dto.setReplyprofile(wanbaProfileDTO(profile, wanbaProfile));
                                replyDTOList.add(dto);
                            }
                        }
                    }
                }
            }
            returnObj.setRows(replyDTOList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }

        return returnObj;
    }


    public CommentReply createReply(CommentBean commentBean, Profile profile, String profilekey, Long parent_id, ReplyBody replyBody, String ip) throws ServiceException {
        CommentReply reply = new CommentReply();
        reply.setCommentId(commentBean.getCommentId());
        reply.setReplyUno(profile.getUno());
        reply.setReplyProfileId(profile.getProfileId());
        reply.setReplyProfileKey(profilekey);

        reply.setRootId(0);
        reply.setRootUno("");
        reply.setRootProfileId("");
        reply.setRootProfileKey("");

        Long parentId = parent_id;
        String parentUno = "";
        String parentProfileId = "";
        String parentProfileKey = "";
        if (parentId > 0) {
            CommentReply parentReply = CommentServiceSngl.get().getCommentReplyById(commentBean.getCommentId(), parentId);
            if (parentReply != null) {
                parentUno = parentReply.getReplyUno();
                parentProfileId = parentReply.getReplyProfileId();
                parentProfileKey = parentReply.getReplyProfileKey();
            }
        }

        reply.setParentId(parentId);
        reply.setParentUno(parentUno);
        reply.setParentProfileId(parentProfileId);
        reply.setParentProfileKey(profilekey);


        reply.setAgreeSum(0);
        reply.setDisagreeSum(0);
        reply.setSubReplySum(0);
        reply.setBody(replyBody);
        reply.setCreateTime(new Date());
        reply.setCreateIp(ip);
        reply.setRemoveStatus(ActStatus.UNACT);
        reply.setTotalRows(0);


        reply = CommentServiceSngl.get().createCommentReply(reply, commentBean.getTotalRows());

        return reply;
    }


    //玩霸问答评论成功后调用的方法
    public void sendWanbaAskMessage(CommentBean commentBean, CommentReply parentReply, CommentReply commentReply, Answer answer) {
        try {
            //如果是子楼，评论pid等于答案pid，需要进行屏蔽
            if (parentReply == null && answer.getAnswerProfileId().equals(commentReply.getReplyProfileId())) {
                return;
            }

            //TODO 需要屏蔽自己
            if (commentReply.getParentId() > 0 && commentReply.getReplyProfileId().equals(parentReply.getReplyProfileId())) {
                return;
            }
            WanbaReplyEvent wanbaReplyEvent = new WanbaReplyEvent();
            wanbaReplyEvent.setType(NoticeType.REPLY);
            wanbaReplyEvent.setCreateTime(new Date());
            WanbaReplyBody wanbaReplyBody = new WanbaReplyBody();
            wanbaReplyBody.setReplyDesc(commentReply.getBody().getText());
            wanbaReplyBody.setCommentId(commentBean.getCommentId());
            wanbaReplyBody.setReplyId(String.valueOf(commentReply.getReplyId()));
            wanbaReplyBody.setReplyProfileId(commentReply.getReplyProfileId());
            wanbaReplyBody.setAnswerId(answer.getAnswerId());
            wanbaReplyEvent.setDestId(String.valueOf(commentReply.getReplyId()));
            if (commentReply.getParentId() == 0) {
                wanbaReplyBody.setParentDesc(commentBean.getTitle());
                wanbaReplyEvent.setProfileId(answer.getAnswerProfileId());
                wanbaReplyBody.setReplyBodyType(WanbaReplyBodyType.REPLY_ANSWER);
            } else {
                wanbaReplyBody.setParentIdreplyId(parentReply.getReplyId());
                wanbaReplyBody.setParentDesc(parentReply.getBody().getText());
                wanbaReplyBody.setReplyBodyType(WanbaReplyBodyType.REPLY_REPLEY);
                wanbaReplyEvent.setProfileId(parentReply.getReplyProfileId());
            }

            wanbaReplyEvent.setWanbaReplyBody(wanbaReplyBody);
            EventDispatchServiceSngl.get().dispatch(wanbaReplyEvent);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }
    }


    public PageRows<MessageListDTO> queryWanbaSocialMessageList(String pid, Pagination pagination) throws ServiceException {
        PageRows<UserNotice> userNoticeList = NoticeServiceSngl.get().queryUserNotice(pid, "", NoticeType.REPLY, pagination);

        Set<String> profileIdSet = new HashSet<String>();
        Map<Long, WanbaReplyBody> bodyMap = new HashMap<Long, WanbaReplyBody>();
        for (UserNotice notice : userNoticeList.getRows()) {
            WanbaReplyBody body = WanbaReplyBody.fromJson(notice.getBody());
            if (body == null) {
                continue;
            }
            profileIdSet.add(body.getReplyProfileId());
            profileIdSet.add(notice.getProfileId());
            bodyMap.put(notice.getUserNoticeId(), body);
        }

        Map<String, Profile> profileMap = UserCenterServiceSngl.get().queryProfiles(profileIdSet);
        //列出wanbaprofile集合
        Map<String, VerifyProfile> wanbaProfileMap = UserCenterServiceSngl.get().queryProfileByIds(profileIdSet);

        Answer answer = null;
        CommentReply commentReply = null;
        CommentReply parentReply = null;
        List<MessageListDTO> returnList = new ArrayList<MessageListDTO>();
        for (UserNotice notice : userNoticeList.getRows()) {
            WanbaReplyBody body = bodyMap.get(notice.getUserNoticeId());
            if (body == null) {
                continue;
            }
            MessageListDTO dto = new MessageListDTO();
            WanbaSocialMessageDTO messageDTO = new WanbaSocialMessageDTO();
            messageDTO.setJi(String.valueOf(body.getAnswerId()));
            messageDTO.setJt(String.valueOf(WanbaJt.ANSWER_DETAIL.getCode()));
            messageDTO.setUnikey(String.valueOf(body.getAnswerId()));
            // messageDTO.setReplyid(body.getReplyId());
            messageDTO.setType(String.valueOf(body.getReplyBodyType().getCode()));

            //查询answer
            answer = AskServiceSngl.get().getAnswer(body.getAnswerId());


            if (body.getReplyBodyType().equals(WanbaReplyBodyType.REPLY_ANSWER)) {
                if (answer == null) {
                    messageDTO.setReplydesc("该答案已删除");
                } else {
                    messageDTO.setReplydesc(body.getParentDesc());
                }
            } else {
                commentReply = CommentServiceSngl.get().getCommentReplyById(CommentUtil.genCommentId(String.valueOf(body.getAnswerId()), CommentDomain.WAN_ASK_COMMENT),
                        Long.valueOf(body.getParentIdreplyId()));

                if (commentReply == null) {
                    messageDTO.setReplydesc("该评论已删除");
                } else {
                    messageDTO.setReplydesc(body.getParentDesc());
                }
            }
            messageDTO.setDesc(body.getReplyDesc());
            messageDTO.setTime(String.valueOf(notice.getCreateTime().getTime()));
            dto.setMessage(messageDTO);
            dto.setProfile(wanbaProfileDTO(profileMap.get(body.getReplyProfileId()), wanbaProfileMap.get(body.getReplyProfileId())));
            returnList.add(dto);
        }

        PageRows<MessageListDTO> pageRows = new PageRows<MessageListDTO>();
        pageRows.setPage(userNoticeList.getPage());
        pageRows.setRows(returnList);

        return pageRows;
    }


    //内容中的img标签不判断敏感词
    public String illegeText(String body) {
        String illegeText = body;
        String regex = "<(img|IMG)[^>]*>";
        Pattern imgPattern = Pattern.compile(regex);
        Matcher matcher = imgPattern.matcher(illegeText);
        while (matcher.find()) {
            String img = matcher.group(0);
            illegeText = illegeText.replace(img, "");
        }
        return illegeText;
    }

    private WanbaReplyEntity buildWanbaReplyEntity(ReplyEntity reply) {
        WanbaReplyEntity replyEntity = new WanbaReplyEntity();
        replyEntity.setReplyid(reply.getRid());
        replyEntity.setParentid(reply.getPid());
        replyEntity.setBody(reply.getBody());
        replyEntity.setPost_time(reply.getPost_time());
        replyEntity.setRootid(reply.getOid());
        return replyEntity;
    }

    private Set<Long> getUids(List<MainReplyDTO> mainReplyDTOList) {
        Set<Long> uids = new HashSet<Long>();
        for (MainReplyDTO mainReplyDTO : mainReplyDTOList) {
            ReplyDTO replyDTO = mainReplyDTO.getReply();
            if (replyDTO != null) {
                UserEntity user = replyDTO.getUser();
                UserEntity puser = replyDTO.getPuser();
                if (user != null) {
                    uids.add(user.getUid());
                }
                if (puser != null) {
                    uids.add(puser.getUid());
                }
            }
        }
        return uids;
    }
}
