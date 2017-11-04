package com.enjoyf.platform.serv.content.processor;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentInteraction;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-12
 * Time: 下午2:10
 * To change this template use File | Settings | File Templates.
 */
public class BillingEncourageProcessor implements ContentProcessor {
    @Override
    public void process(Object obj) {
        if (obj instanceof Content) {
//            processDepositContent((Content) obj);
        } else if (obj instanceof ContentInteraction) {
//            processDepositReply((ContentInteraction) obj);
        } else {
            GAlerter.lab("In contextProcessQueueThreadN, there is a unknown obj.");
        }
    }

//    private void processDepositContent(Content content) {
//        //billing deposit event : forwarded
//        if (content.getPublishType().equals(ContentPublishType.FORWARD) && !content.getUno().equals(content.getRootContentUno())) {
//
//            BillingEncourageEvent depositEvent = new BillingEncourageEvent();
//            depositEvent.setDepositIp(content.getPublishIp());
//            depositEvent.setEncourageCode(EncourageCode.FORWARDED);
//
//            depositEvent.setDepositUno(content.getUno());
//            depositEvent.setOwnUno(content.getRootContentUno());
//            depositEvent.setSrcId(content.getContentId());
//
//            try {
//                EventDispatchServiceSngl.get().dispatch(depositEvent);
//            } catch (Exception e) {
//                GAlerter.lan("ContentLogic send out billingDepositEvent forwarded error.", e);
//            }
//
//        }
//
//    }

//    private void processDepositReply(ContentInteraction contentReply) {
//        //billing deposit event : replied
//        if (!contentReply.getContentUno().equals(contentReply.getInteractionUno())) {
//            BillingEncourageEvent depositEvent = new BillingEncourageEvent();
//            depositEvent.setDepositIp(contentReply.getCreateIp());
//            depositEvent.setEncourageCode(EncourageCode.REPLIED);
//
//            depositEvent.setDepositUno(contentReply.getInteractionUno());
//            depositEvent.setOwnUno(contentReply.getContentUno());
//            depositEvent.setSrcId(contentReply.getInteractionId());
//
//            try {
//                EventDispatchServiceSngl.get().dispatch(depositEvent);
//            } catch (Exception e) {
//                GAlerter.lan("ContentLogic send out billingDepositEvent replied error.", e);
//            }
//        }
//    }
}
