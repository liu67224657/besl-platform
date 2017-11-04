package com.enjoyf.webapps.joyme.weblogic.comment;

import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-13
 * Time: 上午11:23
 * To change this template use File | Settings | File Templates.
 */
public interface CommentProcessor {

    ResultObjectMsg buildResultObjectMsg(ResultObjectMsg resultObjectMsg, String uniKey, CommentDomain commentDomain, String jsonParam, int pageNum, int pageSize, Long authorUid, boolean desc) throws ServiceException;

    ResultObjectMsg buildResultObjectMsgByReplyId(ResultObjectMsg resultObjectMsg, String uniKey, CommentDomain commentDomain, String jsonParam, long replyId, int pageSize, Long authorUid, boolean desc) throws ServiceException;
}
