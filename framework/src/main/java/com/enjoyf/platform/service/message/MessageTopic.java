/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.message;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午5:21
 * Description: 用来表达两个用户间同一话题的消息集合。
 */
public class MessageTopic implements Serializable {
    //topic id.
    private Long topicId;

    //
    private String reletionUno;

    //
    private Message lastestMessage;

    //
    private Integer msgSize;


    //////////////////////////////
    public MessageTopic() {
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getReletionUno() {
        return reletionUno;
    }

    public void setReletionUno(String reletionUno) {
        this.reletionUno = reletionUno;
    }

    public Message getLastestMessage() {
        return lastestMessage;
    }

    public void setLastestMessage(Message lastestMessage) {
        this.lastestMessage = lastestMessage;
    }

    public Integer getMsgSize() {
        return msgSize;
    }

    public void setMsgSize(Integer msgSize) {
        this.msgSize = msgSize;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
