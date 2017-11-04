package com.enjoyf.platform.util.oauth.qqv2;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-2-4
 * Time: 上午7:53
 * To change this template use File | Settings | File Templates.
 */
public class QqUserInfo {
//    private String unionId;
    private String nickname;
    private boolean isYellowYearVip;
    private boolean vip;
    private int level;
    private String figureurl_1;
    private String figureurl_2;
    private String figureurl;

    private String figureurl_qq_1;
    private String figureurl_qq_2;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isYellowYearVip() {
        return isYellowYearVip;
    }

    public void setYellowYearVip(boolean yellowYearVip) {
        isYellowYearVip = yellowYearVip;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getFigureurl_1() {
        return figureurl_1;
    }

    public void setFigureurl_1(String figureurl_1) {
        this.figureurl_1 = figureurl_1;
    }

    public String getFigureurl_2() {
        return figureurl_2;
    }

    public void setFigureurl_2(String figureurl_2) {
        this.figureurl_2 = figureurl_2;
    }

    public String getFigureurl() {
        return figureurl;
    }

    public void setFigureurl(String figureurl) {
        this.figureurl = figureurl;
    }

    public String getFigureurl_qq_1() {
        return figureurl_qq_1;
    }

    public void setFigureurl_qq_1(String figureurl_qq_1) {
        this.figureurl_qq_1 = figureurl_qq_1;
    }

    public String getFigureurl_qq_2() {
        return figureurl_qq_2;
    }

    public void setFigureurl_qq_2(String figureurl_qq_2) {
        this.figureurl_qq_2 = figureurl_qq_2;
    }

//    public String getUnionId() {
//        return unionId;
//    }
//
//    public void setUnionId(String unionId) {
//        this.unionId = unionId;
//    }
}
