package com.enjoyf.platform.service.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-1
 * Time: 下午3:33
 * To change this template use File | Settings | File Templates.
 */
public class SocialMsgCatchEventFactory {

    private static SocialMsgCatchEventFactory instance;

    private static Map<Integer, SocialMsgCatchEventProcessor> map = new HashMap<Integer, SocialMsgCatchEventProcessor>();


    public static SocialMsgCatchEventFactory get() {
        if (instance == null) {
            synchronized (SocialMsgCatchEventFactory.class) {
                if (instance == null) {
                    instance = new SocialMsgCatchEventFactory();
                }
            }
        }
        return instance;
    }

    public SocialMsgCatchEventProcessor factory(SocialMessageType type) {
        SocialMsgCatchEventProcessor returnProcessor = map.containsKey(type) ? map.get(type) : null;
        if (returnProcessor == null) {
            synchronized (map) {
                if (SocialMessageType.HOT.equals(type)) {
                    returnProcessor = new SocialMsgHotProcessor();
                } else if (SocialMessageType.FOCUS.equals(type)) {
                    returnProcessor = new SocialMsgFocusProcessor();
                } else if (SocialMessageType.AGREE.equals(type)) {
                    returnProcessor = new SocialMsgAgreeProcessor();
                } else if (SocialMessageType.REPLY.equals(type)) {
                    returnProcessor = new SocialMsgReplyProcessor();
                }
            }

            if(returnProcessor != null){
                map.put(type.getCode(), returnProcessor);
            }
        }

        return returnProcessor;
    }
}
