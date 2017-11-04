package com.enjoyf.platform.service.message;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-27
 * Time: 下午3:40
 * To change this template use File | Settings | File Templates.
 */
public class SocialMessage implements Serializable {


    //sequence id
    private Long msgId;
    private String msgBody;
    private SocialMessageType msgType;
    private SocialMessageCategory msgCategory;

    //消息的所有者
    private String ownUno;

    //消息的发送方和接收方。
    private String sendUno;
    private String receiveUno;

    private Long replyId;
    private String replyUno;

    private Long parentId;
    private String parentUno;

    private Long rootId;
    private String rootUno;

    private Long contentId;
    private String contentUno;

    private ActStatus readStatus = ActStatus.UNACT;
    private Date readTime;

    private ActStatus publishStatus = ActStatus.UNACT;
    private Date publishTime;

    private ActStatus removeStatus = ActStatus.UNACT;
    private Date createDate;
    private String createIp;
    private String createUserId;
    private Date modifyDate;
    private String modifyIp;
    private String modifyUserId;


    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public SocialMessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(SocialMessageType msgType) {
        this.msgType = msgType;
    }

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
    }

    public String getSendUno() {
        return sendUno;
    }

    public void setSendUno(String sendUno) {
        this.sendUno = sendUno;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentUno() {
        return parentUno;
    }

    public void setParentUno(String parentUno) {
        this.parentUno = parentUno;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public String getRootUno() {
        return rootUno;
    }

    public void setRootUno(String rootUno) {
        this.rootUno = rootUno;
    }

    public ActStatus getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(ActStatus readStatus) {
        this.readStatus = readStatus;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public String getReplyUno() {
        return replyUno;
    }

    public void setReplyUno(String replyUno) {
        this.replyUno = replyUno;
    }

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public SocialMessageCategory getMsgCategory() {
        return msgCategory;
    }

    public void setMsgCategory(SocialMessageCategory msgCategory) {
        this.msgCategory = msgCategory;
    }

    public String getReceiveUno() {
        return receiveUno;
    }

    public void setReceiveUno(String receiveUno) {
        this.receiveUno = receiveUno;
    }

    public ActStatus getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(ActStatus publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getModifyIp() {
        return modifyIp;
    }

    public void setModifyIp(String modifyIp) {
        this.modifyIp = modifyIp;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return ownUno.hashCode();
    }
}
