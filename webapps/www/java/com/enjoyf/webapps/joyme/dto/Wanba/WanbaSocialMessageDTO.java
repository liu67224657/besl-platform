package com.enjoyf.webapps.joyme.dto.Wanba;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by zhimingli on 2016/9/21 0021.
 */
public class WanbaSocialMessageDTO implements Serializable {
    private String jt;
    private String ji;
    private String unikey;//answid
    // private String replyid;//评论ID
    private String type;// 1回复答案 2回复评论
    private String replydesc;
    private String desc;
    private String time;

    public String getJt() {
        return jt;
    }

    public void setJt(String jt) {
        this.jt = jt;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }


//    public String getReplyid() {
//        return replyid;
//    }
//
//    public void setReplyid(String replyid) {
//        this.replyid = replyid;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReplydesc() {
        return replydesc;
    }

    public void setReplydesc(String replydesc) {
        this.replydesc = replydesc;
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

    public String getUnikey() {
        return unikey;
    }

    public void setUnikey(String unikey) {
        this.unikey = unikey;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
