package com.enjoyf.webapps.joyme.weblogic.comment;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.dto.comment.CommentJsonParam;
import com.enjoyf.webapps.joyme.dto.comment.MainReplyDTO;
import com.enjoyf.webapps.joyme.dto.comment.ScoreDTO;
import com.enjoyf.webapps.joyme.dto.comment.ScoreEntity;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-13
 * Time: 上午11:26
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DigitalScoreProcessor extends AbstractCommentProcessor{

    @Resource(name = "commentWebLogic")
    private CommentWebLogic commentWebLogic = new CommentWebLogic();

    @Override
    public ResultObjectMsg buildResultObjectMsg(ResultObjectMsg resultObjectMsg, String uniKey, CommentDomain commentDomain, String jsonParam, int pageNum, int pageSize, Long authorUid, boolean desc) throws ServiceException {
        String commentId = CommentUtil.genCommentId(uniKey ,commentDomain);
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
        CommentBean commentBean = CommentServiceSngl.get().getCommentBeanById(commentId);
        if (commentBean == null) {
            //joyme.com
            if (param != null && !StringUtil.isEmpty(param.getUri()) && param.getUri().indexOf("http://") >= 0 && param.getUri().indexOf(Constant.DOMAIN) > 0) {
                CommentBean comment = new CommentBean();
                comment.setUniqueKey(uniKey);
                comment.setDomain(commentDomain);
                if (param != null) {
                    comment.setUri(param.getUri());
                    comment.setTitle(param.getTitle());
                    comment.setPic(param.getPic());
                    comment.setDescription(param.getDescription());
                    comment.setLongCommentSum(param.getStar());
                }
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
            }

            resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
            resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
            return resultObjectMsg;
        }else {
            if(param != null){
                if(!commentBean.getUri().equals(param.getUri()) || !commentBean.getTitle().equals(param.getTitle()) || !commentBean.getPic().equals(param.getPic()) || commentBean.getDescription().equals(param.getDescription()) || commentBean.getLongCommentSum() != param.getStar()){
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(CommentBeanField.URI, param.getUri());
                    updateExpress.set(CommentBeanField.TITLE, param.getTitle());
                    updateExpress.set(CommentBeanField.PIC, param.getPic());
                    updateExpress.set(CommentBeanField.DESCRIPTION, param.getDescription());
                    updateExpress.set(CommentBeanField.LONG_COMMENT_SUM, param.getStar());
                    int uriLength = param.getUri() == null ? 0 : param.getUri().length();
                    int titleLength = param.getTitle() == null ? 0 : param.getTitle().length();
                    int picLength = param.getPic() == null ? 0 : param.getPic().length();
                    int descLength = param.getDescription() == null ? 0 : param.getDescription().length();
                    //不符合要求的数据不入库
                    if(uriLength > 512 || titleLength > 128 || picLength > 256 || descLength > 2048){
                        return resultObjectMsg;
                    }
                    boolean bool = CommentServiceSngl.get().modifyCommentBeanById(commentId, updateExpress);
                    if(bool){
                        commentBean.setUri(param.getUri());
                        commentBean.setTitle(param.getTitle());
                        commentBean.setPic(param.getPic());
                        commentBean.setDescription(param.getDescription());
                        commentBean.setLongCommentSum(param.getStar());
                    }
                }
            }
        }
        ScoreEntity scoreEntity = commentWebLogic.buildScoreEntity(commentBean);
        PageRows<MainReplyDTO> mainReplyRows = commentWebLogic.queryReplyByOrderField(commentBean, pageNum, pageSize, CommentReplyField.AGREE_SUM, QuerySortOrder.DESC);
        ScoreDTO returnDTO = new ScoreDTO();
        returnDTO.setMainreplys(mainReplyRows);
        returnDTO.setScore(scoreEntity);

        resultObjectMsg.setRs(ResultCodeConstants.SUCCESS.getCode());
        resultObjectMsg.setMsg(ResultCodeConstants.SUCCESS.getMsg());
        resultObjectMsg.setResult(returnDTO);
        return resultObjectMsg;
    }
}
