package com.enjoyf.webapps.joyme.dto.comment;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 15-8-31
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
public class MiyouDTO {
    private String ji;  //跳转类型
    private String jt;  //跳转参数
    private String commentid;   //帖子ID
    private String nick;        //用户昵称
    private String uid;         //用户UID
    private String picurl;     //用户头像
    private String groupid;    //圈子ID
    private String groupname;  //圈子名称
    private String desc;        //用户发布的信息
    private String time;        //发布时间
    private String agreenum;   //点赞数
    private String commentnum; //评论数
    private String isrecommend;  //是否推荐
    private String isagree;      //是否已赞

    private List<String> imgs;   //用户发布的图片
    private String type;   //1.帖子

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAgreenum() {
        return agreenum;
    }

    public void setAgreenum(String agreenum) {
        this.agreenum = agreenum;
    }

    public String getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(String commentnum) {
        this.commentnum = commentnum;
    }

    public String getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(String isrecommend) {
        this.isrecommend = isrecommend;
    }

    public String getIsagree() {
        return isagree;
    }

    public void setIsagree(String isagree) {
        this.isagree = isagree;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getJt() {
        return jt;
    }

    public void setJt(String jt) {
        this.jt = jt;
    }
}
