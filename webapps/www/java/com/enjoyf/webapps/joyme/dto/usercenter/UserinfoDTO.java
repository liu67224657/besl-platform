package com.enjoyf.webapps.joyme.dto.usercenter;

/**
 * Created by ericliu on 14/10/22.
 */
public class UserinfoDTO {
    private long uid;
    private String uno;
    private String profileId;
    private String nick;
    private String icon;
    private String desc;
    private String verify;
    private int fans;
    private int follows;
    private String sex;
    private String edits;
    private String followStatus;//关注状态
    private Integer prestige;//声望
    private String worship;//膜拜

    private String vdesc;//认证原因
    private String vtitle;//认证title
    private long vtype;//认证类型
    private String headskin = ""; //头像框
    private String replyskin = ""; //评论框
    private String cardskin = "";  //名片框
    private String bubbleskin = ""; //聊天气泡

    private UserMWikiDTO userMWikiDTO;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEdits() {
        return edits;
    }

    public void setEdits(String edits) {
        this.edits = edits;
    }

    public String getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(String followStatus) {
        this.followStatus = followStatus;
    }

    public Integer getPrestige() {
        return prestige;
    }

    public void setPrestige(Integer prestige) {
        this.prestige = prestige;
    }

    public String getWorship() {
        return worship;
    }

    public void setWorship(String worship) {
        this.worship = worship;
    }

    public String getVdesc() {
        return vdesc;
    }

    public void setVdesc(String vdesc) {
        this.vdesc = vdesc;
    }

    public String getVtitle() {
        return vtitle;
    }

    public void setVtitle(String vtitle) {
        this.vtitle = vtitle;
    }

    public long getVtype() {
        return vtype;
    }

    public void setVtype(long vtype) {
        this.vtype = vtype;
    }

    public String getHeadskin() {
        return headskin;
    }

    public void setHeadskin(String headskin) {
        this.headskin = headskin;
    }

    public String getReplyskin() {
        return replyskin;
    }

    public void setReplyskin(String replyskin) {
        this.replyskin = replyskin;
    }

    public String getCardskin() {
        return cardskin;
    }

    public void setCardskin(String cardskin) {
        this.cardskin = cardskin;
    }

    public String getBubbleskin() {
        return bubbleskin;
    }

    public void setBubbleskin(String bubbleskin) {
        this.bubbleskin = bubbleskin;
    }

    public UserMWikiDTO getUserMWikiDTO() {
        return userMWikiDTO;
    }

    public void setUserMWikiDTO(UserMWikiDTO userMWikiDTO) {
        this.userMWikiDTO = userMWikiDTO;
    }
}
