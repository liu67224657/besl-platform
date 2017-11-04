/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:29
 * Description:
 */
public class ProfileSum implements Serializable {
    //
    private String uno;

    private int focusSum = 0;
    private int fansSum = 0;
    private int blogSum = 0;
    private int forwardSum = 0;
    private int favorSum = 0;

    // EXTSTR01 --> last content id
    private String lastContentId;

    // EXTSTR02 --> last reply id
    private String lastReplyId;


    private int socialBlogSum = 0;//博客数
    private int socialPlaySum = 0;//播放数

    private int socialFocusSum = 0;
    private int socialFansSum = 0;

    private int socialAgreeMsgSum = 0;
    private int socialReplyMsgSum = 0;
    private int socialNoticeMsgSum = 0;

    public ProfileSum() {
    }

    public ProfileSum(String uno) {
        this.uno = uno;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public Integer getFocusSum() {
        return focusSum;
    }

    public void setFocusSum(Integer focusSum) {
        this.focusSum = focusSum;
    }

    public int getFansSum() {
        return fansSum;
    }

    public void setFansSum(int fansSum) {
        this.fansSum = fansSum;
    }

    public int getBlogSum() {
        return blogSum;
    }

    public void setBlogSum(int blogSum) {
        this.blogSum = blogSum;
    }

    public int getForwardSum() {
        return forwardSum;
    }

    public void setForwardSum(int forwardSum) {
        this.forwardSum = forwardSum;
    }

    public int getFavorSum() {
        return favorSum;
    }

    public void setFavorSum(int favorSum) {
        this.favorSum = favorSum;
    }

    public String getLastContentId() {
        return lastContentId;
    }

    public void setLastContentId(String lastContentId) {
        this.lastContentId = lastContentId;
    }

    public String getLastReplyId() {
        return lastReplyId;
    }

    public void setLastReplyId(String lastReplyId) {
        this.lastReplyId = lastReplyId;
    }

    public int getSocialBlogSum() {
        return socialBlogSum;
    }

    public void setSocialBlogSum(int socialBlogSum) {
        this.socialBlogSum = socialBlogSum;
    }

    public int getSocialPlaySum() {
        return socialPlaySum;
    }

    public void setSocialPlaySum(int socialPlaySum) {
        this.socialPlaySum = socialPlaySum;
    }

    public int getSocialFocusSum() {
        return socialFocusSum;
    }

    public void setSocialFocusSum(int socialFocusSum) {
        this.socialFocusSum = socialFocusSum;
    }

    public int getSocialFansSum() {
        return socialFansSum;
    }

    public void setSocialFansSum(int socialFansSum) {
        this.socialFansSum = socialFansSum;
    }

    public int getSocialAgreeMsgSum() {
        return socialAgreeMsgSum;
    }

    public void setSocialAgreeMsgSum(int socialAgreeMsgSum) {
        this.socialAgreeMsgSum = socialAgreeMsgSum;
    }

    public int getSocialReplyMsgSum() {
        return socialReplyMsgSum;
    }

    public void setSocialReplyMsgSum(int socialReplyMsgSum) {
        this.socialReplyMsgSum = socialReplyMsgSum;
    }

    public int getSocialNoticeMsgSum() {
        return socialNoticeMsgSum;
    }

    public void setSocialNoticeMsgSum(int socialNoticeMsgSum) {
        this.socialNoticeMsgSum = socialNoticeMsgSum;
    }

    //
    @Override
    public int hashCode() {
        return uno.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
