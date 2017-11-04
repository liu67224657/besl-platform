package com.enjoyf.webapps.joyme.weblogic.entity;

import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageTopic;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * 消息封装的对象
 * Author: zhaoxin
 * Date: 11-9-5
 * Time: 下午2:16
 * Desc:
 */
public class MessageProfile {
     private Profile profile;
     private MessageTopic messageTopic;

    // 当查询结果为message类型时使用
    private Message message;


    public MessageProfile() {
    }

    public MessageProfile(Profile profile, Message message) {
        this.profile = profile;
        this.message = message;
    }

    public MessageProfile(Profile profile, MessageTopic messageTopic) {
        this.profile = profile;
        this.messageTopic = messageTopic;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public MessageTopic getMessageTopic() {
        return messageTopic;
    }

    public void setMessageTopic(MessageTopic messageTopic) {
        this.messageTopic = messageTopic;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
