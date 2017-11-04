package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-7
 * Time: 下午5:22
 * To change this template use File | Settings | File Templates.
 */
public class SocialMessageDTO implements Serializable {

    private long msgid;
    private String msgbody;
    private int msgtype;
    private long msgtime;
    private String info;//活动ID，web url，文章ID

    private String fromuno;
    private String fromuname;
    private String fromuicon;   //文章图像（暂时，后续需要的话再调整）

    private long cid;
    private String cuno;

    private long replyid;

    private long parentid;
    private String parentuno;
    private String parentuname;

    private long rootid;
    private String rootuno;

    public long getMsgid() {
        return msgid;
    }

    public void setMsgid(long msgid) {
        this.msgid = msgid;
    }

    public String getMsgbody() {
        return msgbody;
    }

    public void setMsgbody(String msgbody) {
        this.msgbody = msgbody;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public long getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(long msgtime) {
        this.msgtime = msgtime;
    }

    public String getFromuno() {
        return fromuno;
    }

    public void setFromuno(String fromuno) {
        this.fromuno = fromuno;
    }

    public String getFromuname() {
        return fromuname;
    }

    public void setFromuname(String fromuname) {
        this.fromuname = fromuname;
    }

    public String getFromuicon() {
        return fromuicon;
    }

    public void setFromuicon(String fromuicon) {
        this.fromuicon = fromuicon;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getCuno() {
        return cuno;
    }

    public void setCuno(String cuno) {
        this.cuno = cuno;
    }

    public long getReplyid() {
        return replyid;
    }

    public void setReplyid(long replyid) {
        this.replyid = replyid;
    }

    public long getParentid() {
        return parentid;
    }

    public void setParentid(long parentid) {
        this.parentid = parentid;
    }

    public String getParentuno() {
        return parentuno;
    }

    public void setParentuno(String parentuno) {
        this.parentuno = parentuno;
    }

    public String getParentuname() {
        return parentuname;
    }

    public void setParentuname(String parentuname) {
        this.parentuname = parentuname;
    }

    public long getRootid() {
        return rootid;
    }

    public void setRootid(long rootid) {
        this.rootid = rootid;
    }

    public String getRootuno() {
        return rootuno;
    }

    public void setRootuno(String rootuno) {
        this.rootuno = rootuno;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return String.valueOf(msgid).hashCode();
    }
}
