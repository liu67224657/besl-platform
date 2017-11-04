package com.enjoyf.platform.cloud.contentservice;

/**
 * Created by pengxu on 2017/6/21.
 */
public class CommentDTO {
    private Long id;
    private Long gameId;//游戏ID
    private String gameName = "";
    private Long uid;
    private String nick = "";//姓名
    private String icon = "";//头像
    private String body = "";//点评内容s
    private int score;//点评分数
    private long time;//创建时间
    private int agreeNum;//点赞数量
    private boolean hasAgree;//是否点赞
    private int replyNum;//回复数量
    private int highQuality = 0;//是否是优质评论
    private int verifyProfileType = 1;//是否认证

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getAgreeNum() {
        return agreeNum;
    }

    public void setAgreeNum(int agreeNum) {
        this.agreeNum = agreeNum;
    }

    public boolean isHasAgree() {
        return hasAgree;
    }

    public void setHasAgree(boolean hasAgree) {
        this.hasAgree = hasAgree;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getHighQuality() {
        return highQuality;
    }

    public void setHighQuality(int highQuality) {
        this.highQuality = highQuality;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getVerifyProfileType() {
        return verifyProfileType;
    }

    public void setVerifyProfileType(int verifyProfileType) {
        this.verifyProfileType = verifyProfileType;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
