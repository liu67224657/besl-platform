package com.enjoyf.platform.service.gameres.privilege;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-16
 * Time: 下午6:33
 * To change this template use File | Settings | File Templates.
 */
public class ProfileCount implements Serializable {

    private long profileCountId;
    private long groupId;
    private String uno;
    private int postNum;
    private int newPostNum;
    private int replyNum;
    private int newReplyNum;
    private int deleteNum;
    private int newDeleteNum;
    private int extInt1;
    private int extInt2;

    public long getProfileCountId() {
        return profileCountId;
    }

    public void setProfileCountId(long profileCountId) {
        this.profileCountId = profileCountId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public int getPostNum() {
        return postNum;
    }

    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }

    public int getNewPostNum() {
        return newPostNum;
    }

    public void setNewPostNum(int newPostNum) {
        this.newPostNum = newPostNum;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getNewReplyNum() {
        return newReplyNum;
    }

    public void setNewReplyNum(int newReplyNum) {
        this.newReplyNum = newReplyNum;
    }

    public int getDeleteNum() {
        return deleteNum;
    }

    public void setDeleteNum(int deleteNum) {
        this.deleteNum = deleteNum;
    }

    public int getNewDeleteNum() {
        return newDeleteNum;
    }

    public void setNewDeleteNum(int newDeleteNum) {
        this.newDeleteNum = newDeleteNum;
    }

    public int getExtInt1() {
        return extInt1;
    }

    public void setExtInt1(int extInt1) {
        this.extInt1 = extInt1;
    }

    public int getExtInt2() {
        return extInt2;
    }

    public void setExtInt2(int extInt2) {
        this.extInt2 = extInt2;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
