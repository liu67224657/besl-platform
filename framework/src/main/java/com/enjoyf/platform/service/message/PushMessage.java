package com.enjoyf.platform.service.message;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午3:13
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(value={"removeStatus","pushMessageType","pushMsgCode","sendStatus","removeStatus"})
public class PushMessage implements Serializable {
    private long pushMsgId;

    //
    private PushMessageType pushMessageType;

    //
    private PushMessageCode pushMsgCode;

    //
    private String msgBody;

    //
    private PushMessageOptions msgOptions;

    //
    private Date sendDate;

    //
    private ActStatus sendStatus = ActStatus.ACTING;

    //
    private ValidStatus removeStatus = ValidStatus.VALID;

    //
    private Date removeDate;

    public PushMessage() {
    }

    public long getPushMsgId() {
        return pushMsgId;
    }

    public void setPushMsgId(long pushMsgId) {
        this.pushMsgId = pushMsgId;
    }

    public PushMessageType getPushMessageType() {
        return pushMessageType;
    }

    public void setPushMessageType(PushMessageType pushMessageType) {
        this.pushMessageType = pushMessageType;
    }

    public PushMessageCode getPushMsgCode() {
        return pushMsgCode;
    }

    public void setPushMsgCode(PushMessageCode pushMsgCode) {
        this.pushMsgCode = pushMsgCode;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public PushMessageOptions getMsgOptions() {
        return msgOptions;
    }

    public void setMsgOptions(PushMessageOptions msgOptions) {
        this.msgOptions = msgOptions;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public ActStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(ActStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public ValidStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ValidStatus removeStatus) {
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

    public String toJsonStr(){
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static PushMessage parse(String jsonStr) {
        try {
            return JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr,PushMessage.class);
        } catch (IOException e) {
            GAlerter.lab("PushMessage parse error, jsonStr:" + jsonStr, e);
        }
        return null;
    }

    public static void main(String[] args) {
        String a = "{\"pushMsgId\":100010,\"msgBody\":\"ios测试消息ddddddddd\",\"sendDate\":1339418863553,\"removeStatus\":{\"code\":\"valid\"},\"removeDate\":null,\"pushMessageType\":{\"code\":\"device\"},\"pushMsgCode\":{\"code\":\"content\"},\"msgOptions\":{\"domain\":\"1\",\"cuno\":\"1\",\"cid\":\"1\"},\"sendStatus\":{\"code\":\"ing\"}}" ;
        PushMessage pm =  PushMessage.parse(a);
        System.out.println(pm);

    }
}
