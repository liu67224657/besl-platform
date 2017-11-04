package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-27
 * Time: 下午4:51
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public class UserRelationBuildEvent extends SystemEvent {

    private String sendUno;
    private String receiveUno;
    private String msgBody;
    private Date createDate;
    private String createIp;
    private String createUserId;

    //todo rename USER_RELATION_BUILD
    public UserRelationBuildEvent() {
        super(SystemEventType.USERRELATION_BUILD);
    }

    public String getSendUno() {
        return sendUno;
    }

    public void setSrcPid(String sendUno) {
        this.sendUno = sendUno;
    }

    public String getReceiveUno() {
        return receiveUno;
    }

    public void setReceiveUno(String receiveUno) {
        this.receiveUno = receiveUno;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
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
