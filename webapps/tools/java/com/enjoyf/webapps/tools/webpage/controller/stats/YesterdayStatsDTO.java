package com.enjoyf.webapps.tools.webpage.controller.stats;

import com.enjoyf.platform.service.stats.StatItem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class YesterdayStatsDTO {
    ///////////////////////////////////section one
    private StatItem yesterdayPageView;
    private StatItem yesterdayUniqueUser;
    private StatItem yesterdayIP;
    private StatItem yesterdayAvgTimeOfStay;

    //
    private List<StatItem> yesterdayUniqueRefs = new ArrayList<StatItem>();
    private List<StatItem> yesterdayPageViewPages = new ArrayList<StatItem>();

    //////////section two
    private List<StatItem> secondDayBacks = new ArrayList<StatItem>();
    private List<StatItem> fourDayBacks = new ArrayList<StatItem>();
    private List<StatItem> sevenDayBacks = new ArrayList<StatItem>();

    //////////section three
    private StatItem yesterdayNewArticle;
    private StatItem yesterdayNewComment;
    private StatItem yesterdayNewLike;
    private StatItem yesterdayNewForward;
    private StatItem yesterdayNewRegister;

    private List<StatItem> yesterdayRegisterRefs = new ArrayList<StatItem>();
    private List<StatItem> yesterdayPostPhases = new ArrayList<StatItem>();
    private List<StatItem> yesterdayCommentPhases = new ArrayList<StatItem>();
    private List<StatItem> yesterdayForwardPhases = new ArrayList<StatItem>();

    private List<StatItem> yesterdayTopPostUsers = new ArrayList<StatItem>();

    /////////section four.
    private List<StatItem> lastWeekTopHotArticles = new ArrayList<StatItem>();
    private List<StatItem> lastWeekTopViewedArticles = new ArrayList<StatItem>();

    public StatItem getYesterdayPageView() {
        return yesterdayPageView;
    }

    public void setYesterdayPageView(StatItem yesterdayPageView) {
        this.yesterdayPageView = yesterdayPageView;
    }

    public StatItem getYesterdayUniqueUser() {
        return yesterdayUniqueUser;
    }

    public void setYesterdayUniqueUser(StatItem yesterdayUniqueUser) {
        this.yesterdayUniqueUser = yesterdayUniqueUser;
    }

    public StatItem getYesterdayIP() {
        return yesterdayIP;
    }

    public void setYesterdayIP(StatItem yesterdayIP) {
        this.yesterdayIP = yesterdayIP;
    }

    public StatItem getYesterdayAvgTimeOfStay() {
        return yesterdayAvgTimeOfStay;
    }

    public void setYesterdayAvgTimeOfStay(StatItem yesterdayAvgTimeOfStay) {
        this.yesterdayAvgTimeOfStay = yesterdayAvgTimeOfStay;
    }

    public List<StatItem> getYesterdayUniqueRefs() {
        return yesterdayUniqueRefs;
    }

    public void setYesterdayUniqueRefs(List<StatItem> yesterdayUniqueRefs) {
        this.yesterdayUniqueRefs = yesterdayUniqueRefs;
    }

    public List<StatItem> getYesterdayPageViewPages() {
        return yesterdayPageViewPages;
    }

    public void setYesterdayPageViewPages(List<StatItem> yesterdayPageViewPages) {
        this.yesterdayPageViewPages = yesterdayPageViewPages;
    }

    public List<StatItem> getSecondDayBacks() {
        return secondDayBacks;
    }

    public void setSecondDayBacks(List<StatItem> secondDayBacks) {
        this.secondDayBacks = secondDayBacks;
    }

    public List<StatItem> getFourDayBacks() {
        return fourDayBacks;
    }

    public void setFourDayBacks(List<StatItem> fourDayBacks) {
        this.fourDayBacks = fourDayBacks;
    }

    public List<StatItem> getSevenDayBacks() {
        return sevenDayBacks;
    }

    public void setSevenDayBacks(List<StatItem> sevenDayBacks) {
        this.sevenDayBacks = sevenDayBacks;
    }

    public StatItem getYesterdayNewArticle() {
        return yesterdayNewArticle;
    }

    public void setYesterdayNewArticle(StatItem yesterdayNewArticle) {
        this.yesterdayNewArticle = yesterdayNewArticle;
    }

    public StatItem getYesterdayNewComment() {
        return yesterdayNewComment;
    }

    public void setYesterdayNewComment(StatItem yesterdayNewComment) {
        this.yesterdayNewComment = yesterdayNewComment;
    }

    public StatItem getYesterdayNewLike() {
        return yesterdayNewLike;
    }

    public void setYesterdayNewLike(StatItem yesterdayNewLike) {
        this.yesterdayNewLike = yesterdayNewLike;
    }

    public StatItem getYesterdayNewForward() {
        return yesterdayNewForward;
    }

    public void setYesterdayNewForward(StatItem yesterdayNewForward) {
        this.yesterdayNewForward = yesterdayNewForward;
    }

    public StatItem getYesterdayNewRegister() {
        return yesterdayNewRegister;
    }

    public void setYesterdayNewRegister(StatItem yesterdayNewRegister) {
        this.yesterdayNewRegister = yesterdayNewRegister;
    }

    public List<StatItem> getYesterdayRegisterRefs() {
        return yesterdayRegisterRefs;
    }

    public void setYesterdayRegisterRefs(List<StatItem> yesterdayRegisterRefs) {
        this.yesterdayRegisterRefs = yesterdayRegisterRefs;
    }

    public List<StatItem> getYesterdayPostPhases() {
        return yesterdayPostPhases;
    }

    public void setYesterdayPostPhases(List<StatItem> yesterdayPostPhases) {
        this.yesterdayPostPhases = yesterdayPostPhases;
    }

    public List<StatItem> getYesterdayCommentPhases() {
        return yesterdayCommentPhases;
    }

    public void setYesterdayCommentPhases(List<StatItem> yesterdayCommentPhases) {
        this.yesterdayCommentPhases = yesterdayCommentPhases;
    }

    public List<StatItem> getYesterdayForwardPhases() {
        return yesterdayForwardPhases;
    }

    public void setYesterdayForwardPhases(List<StatItem> yesterdayForwardPhases) {
        this.yesterdayForwardPhases = yesterdayForwardPhases;
    }

    public List<StatItem> getYesterdayTopPostUsers() {
        return yesterdayTopPostUsers;
    }

    public void setYesterdayTopPostUsers(List<StatItem> yesterdayTopPostUsers) {
        this.yesterdayTopPostUsers = yesterdayTopPostUsers;
    }

    public List<StatItem> getLastWeekTopHotArticles() {
        return lastWeekTopHotArticles;
    }

    public void setLastWeekTopHotArticles(List<StatItem> lastWeekTopHotArticles) {
        this.lastWeekTopHotArticles = lastWeekTopHotArticles;
    }

    public List<StatItem> getLastWeekTopViewedArticles() {
        return lastWeekTopViewedArticles;
    }

    public void setLastWeekTopViewedArticles(List<StatItem> lastWeekTopViewedArticles) {
        this.lastWeekTopViewedArticles = lastWeekTopViewedArticles;
    }
}
