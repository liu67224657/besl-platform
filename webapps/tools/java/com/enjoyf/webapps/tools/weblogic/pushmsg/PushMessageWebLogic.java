package com.enjoyf.webapps.tools.weblogic.pushmsg;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.PushMessage;
import com.enjoyf.platform.service.message.PushMessageField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-6-13
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "pushMessageWebLogic")
public class PushMessageWebLogic {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public PageRows<PushMessage> getPushMessages(Date pushMsgStartDate, Date pushMsgEndDate, String sendStatus, Pagination pagination) throws ServiceException {

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.leq(PushMessageField.SENDDATE, pushMsgEndDate));
        queryExpress.add(QueryCriterions.geq(PushMessageField.SENDDATE, pushMsgStartDate));
        if (!StringUtil.isEmpty(sendStatus)) {
            queryExpress.add(QueryCriterions.eq(PushMessageField.SENDSTATUS, sendStatus));
        } else {
            queryExpress.add(QueryCriterions.eq(PushMessageField.SENDSTATUS, ActStatus.UNACT.getCode()));
        }


        return MessageServiceSngl.get().query(queryExpress, pagination);
    }

    public PushMessage getPushMessageById(String pushMsgId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("get PushMessage by id ,id: " + pushMsgId);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(PushMessageField.PUSHMSGID, Long.parseLong(pushMsgId)));

        return MessageServiceSngl.get().get(queryExpress);
    }


    public PushMessage addPushMessage(PushMessage pushMessage) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("save push message, PushMessage: " + pushMessage);
        }

        return MessageServiceSngl.get().create(pushMessage);
    }


    public boolean pushMsg(QueryExpress queryExpress, boolean isTest) throws ServiceException {
        PushMessage pushMessage = MessageServiceSngl.get().get(queryExpress);

        boolean result = false;
//        if (MessageServiceSngl.get().sendPushMessage(pushMessage, isTest)) {
//            UpdateExpress updateExpress = new UpdateExpress();
//            updateExpress.set(PushMessageField.SENDSTATUS, ActStatus.ACTED.getCode());
//
//            result = MessageServiceSngl.get().modifyPushMessage(updateExpress, queryExpress) > 0;
//        }

        return result;
    }

    public boolean modifyPushMessage(UpdateExpress updateExpress, String pushMsgId) throws ServiceException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(PushMessageField.PUSHMSGID, Long.valueOf(pushMsgId)));

//        return MessageServiceSngl.get().modifyPushMessage(updateExpress, queryExpress) > 0;
        return false;
    }
}
