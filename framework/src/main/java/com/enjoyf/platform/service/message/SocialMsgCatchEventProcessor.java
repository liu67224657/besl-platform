package com.enjoyf.platform.service.message;

import com.enjoyf.platform.db.message.MessageHandler;
import com.enjoyf.platform.serv.message.SocialMessageCache;
import com.enjoyf.platform.service.event.system.SocialMessageEvent;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-18
 * Time: 下午3:13
 * To change this template use File | Settings | File Templates.
 */
public interface SocialMsgCatchEventProcessor {

    public void catchEvent(SocialMessageEvent catEvent, MessageHandler writeAbleHandler, MessageHandler readonlyMessageHandler, SocialMessageCache socialMessageCache);

}
