/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.message;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午5:21
 * Description: 用来表达两个用户间的消息。
 */
public class Message implements Serializable {
    //sequence id
    private Long msgId;

    //
    private MessageType msgType;

    //topic id, 初始时跟发起方文章msgid一样。以后的回复topicid不变。
    private Long topicId;

    //消息的所有者
    private String ownUno;

    //消息的发送方和接收方。
    private String senderUno;
    private String recieverUno;

    //
    private String body;

    //
    private Date sendDate;
    private String sendIp;

    //
    private ActStatus readStatus = ActStatus.UNACT;
    private Date readDate;

    //
    private ActStatus removeStatus = ActStatus.UNACT;
    private Date removeDate;

    //////////////////////////////
    public Message() {
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }

    public String getSenderUno() {
        return senderUno;
    }

    public void setSenderUno(String senderUno) {
        this.senderUno = senderUno;
    }

    public String getRecieverUno() {
        return recieverUno;
    }

    public void setRecieverUno(String recieverUno) {
        this.recieverUno = recieverUno;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendIp() {
        return sendIp;
    }

    public void setSendIp(String sendIp) {
        this.sendIp = sendIp;
    }

    public ActStatus getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(ActStatus readStatus) {
        this.readStatus = readStatus;
    }

    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getRemoveDate() {
        return removeDate;
    }

    public void setRemoveDate(Date removeDate) {
        this.removeDate = removeDate;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
