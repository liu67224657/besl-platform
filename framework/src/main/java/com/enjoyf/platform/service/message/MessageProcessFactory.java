package com.enjoyf.platform.service.message;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-1
 * Time: 下午3:33
 * To change this template use File | Settings | File Templates.
 */
public class MessageProcessFactory {

    private static MessageProcessFactory instance;


    public static MessageProcessFactory get() {
        if (instance == null) {
            synchronized (MessageProcessFactory.class) {
                if (instance == null) {
                    instance = new MessageProcessFactory();
                }
            }
        }
        return instance;
    }

    public MessageProcessor factory(Object obj) {
        MessageProcessor returnProcessor = null;

        if (obj instanceof SocialMessage) {
            returnProcessor = new SocialMessageProcessor();
        } else if (obj instanceof PushMsg) {
            returnProcessor = new PushMessageProcessor();
        }
        return returnProcessor;
    }
}
