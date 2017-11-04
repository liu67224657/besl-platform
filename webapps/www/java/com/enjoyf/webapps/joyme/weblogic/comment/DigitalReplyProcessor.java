package com.enjoyf.webapps.joyme.weblogic.comment;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.comment.CommentUtil;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.VerifyProfile;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.dto.comment.CommentJsonParam;
import com.enjoyf.webapps.joyme.dto.comment.CommentResult;
import com.enjoyf.webapps.joyme.dto.comment.MainReplyDTO;
import com.enjoyf.webapps.joyme.dto.comment.UserEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-13
 * Time: 上午11:26
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DigitalReplyProcessor extends AbstractCommentProcessor {

    @Resource(name = "commentWebLogic")
    private CommentWebLogic commentWebLogic = new CommentWebLogic();

    @Override
    public ResultObjectMsg buildResultObjectMsg(ResultObjectMsg resultObjectMsg, String uniKey, CommentDomain commentDomain, String jsonParam, int pageNum, int pageSize, Long authorUid, boolean desc) throws ServiceException {
        String commentId = CommentUtil.genCommentId(uniKey, commentDomain);
        CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
        if (commentBean == null) {
            if (StringUtil.isEmpty(jsonParam)) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_JSONPARAM_NULL.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_JSONPARAM_NULL.getMsg());
                return resultObjectMsg;
            }
            CommentJsonParam param = null;
            try {
                param = CommentJsonParam.parse(jsonParam);
            } catch (Exception e) {
                resultObjectMsg.setRs(ResultCodeConstants.COMMENT_PARAM_COMMENTPARAM_ERROR.getCode());
                resultObjectMsg.setMsg(ResultCodeConstants.COMMENT_PARAM_COMMENTPARAM_ERROR.getMsg());
                return resultObjectMsg;
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

            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            return resultObjectMsg;
        }
        PageRows<MainReplyDTO> mainReplyRows = commentWebLogic.queryMainReplyDTO(commentBean, pageNum, pageSize, desc);
        CommentResult result = new CommentResult();
        result.setComment_sum(commentBean.getCommentSum());
        result.setDescription(commentBean.getDescription());
        result.setPic(commentBean.getPic());
        result.setTitle(commentBean.getTitle());
        result.setUri(commentBean.getUri());
        result.setMainreplys(mainReplyRows);
        result.setHotlist(new ArrayList<MainReplyDTO>());
        result.setShare_sum(commentBean.getShareSum());

        if (authorUid != null && authorUid > 0l) {
            Profile author = UserCenterServiceSngl.get().getProfileByUid(authorUid);
            Map<String, String> chooseMap = PointServiceSngl.get().getChooseLottery(author.getProfileId());
            if (author != null) {
                VerifyProfile verifyProfile = UserCenterServiceSngl.get().getVerifyProfileById(author.getProfileId());
                UserEntity authorUser = commentWebLogic.buildUserEntity(author, chooseMap, verifyProfile);
                result.setUser(authorUser);
            }
        }

        resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
        resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
        resultObjectMsg.setResult(result);
        return resultObjectMsg;
    }
}
