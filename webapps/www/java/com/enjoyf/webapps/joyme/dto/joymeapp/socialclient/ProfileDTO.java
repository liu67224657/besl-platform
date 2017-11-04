package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-17
 * Time: 上午11:38
 * To change this template use File | Settings | File Templates.
 */
public class ProfileDTO {

    private String uno;

    private String icon; //头像

    private String sex; //性别

    private String birthday;  //生日

    private String signature; //签名

    private String username;//昵称

    private String focus;//关注状态

    private String play;//正在玩

    private String backpic;//背景图

    private long blogsum;//博客数

    private long playsum;//播放数

    private long fanssum;//粉丝

	private long newfanssum;//新增粉丝

    private long focussum;//关注

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getBackpic() {
        return backpic;
    }

    public void setBackpic(String backpic) {
        this.backpic = backpic;
    }

    public long getBlogsum() {
        return blogsum;
    }

    public void setBlogsum(long blogsum) {
        this.blogsum = blogsum;
    }

    public long getPlaysum() {
        return playsum;
    }

    public void setPlaysum(long playsum) {
        this.playsum = playsum;
    }

    public long getFanssum() {
        return fanssum;
    }

    public void setFanssum(long fanssum) {
        this.fanssum = fanssum;
    }

    public long getFocussum() {
        return focussum;
    }

    public void setFocussum(long focussum) {
        this.focussum = focussum;
    }

	public long getNewfanssum() {
		return newfanssum;
	}

	public void setNewfanssum(long newfanssum) {
		this.newfanssum = newfanssum;
	}
}
