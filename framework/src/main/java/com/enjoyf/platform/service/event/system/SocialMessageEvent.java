package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.message.SocialMessageCategory;
import com.enjoyf.platform.service.message.SocialMessageType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-27
 * Time: 下午4:51
 * To change this template use File | Settings | File Templates.
 */
public class SocialMessageEvent extends SystemEvent {
    private String ownUno;
    private String sendUno;
    private String receiveUno;
    private int type;

    private String msgBody;

    private long contentId;
    private String contentUno;

    private long replyId;
    private String replyUno;

    private long parentId;
    private String parentUno;

    private long rootId;
    private String rootUno;

    private Date createDate;
    private String createIp;
    private String createUserId;

    public SocialMessageEvent() {
        super(SystemEventType.MESSAGE_SOCIAL_MESSAGE_CREATE);
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

    public String getReceiveUno() {
        return receiveUno;
    }

    public void setReceiveUno(String receiveUno) {
        this.receiveUno = receiveUno;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public String getReplyUno() {
        return replyUno;
    }

    public void setReplyUno(String replyUno) {
        this.replyUno = replyUno;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getParentUno() {
        return parentUno;
    }

    public void setParentUno(String parentUno) {
        this.parentUno = parentUno;
    }

    public long getRootId() {
        return rootId;
    }

    public void setRootId(long rootId) {
        this.rootId = rootId;
    }

    public String getRootUno() {
        return rootUno;
    }

    public void setRootUno(String rootUno) {
        this.rootUno = rootUno;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return receiveUno.hashCode();
    }
}
