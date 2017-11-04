package com.enjoyf.webapps.joyme.dto.comment;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-12
 * Time: 下午9:28
 * To change this template use File | Settings | File Templates.
 */
public class ReplyDTO {

    private ReplyEntity reply;
    private UserEntity user;
    private UserEntity puser;

    public ReplyEntity getReply() {
        return reply;
    }

    public void setReply(ReplyEntity reply) {
        this.reply = reply;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getPuser() {
        return puser;
    }

    public void setPuser(UserEntity puser) {
        this.puser = puser;
    }
}
