package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileDetail;
import com.enjoyf.platform.service.profile.ProfileSetting;
import com.enjoyf.platform.service.profile.ProfileSum;
import com.enjoyf.webapps.joyme.dto.RelationMiniResourceDTO;

import java.util.*;

/**
 * <p/>
 * Description:用户Session类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class UserSession {

    private ProfileBlog blogwebsite;
    private ProfileSum countdata;
    private ProfileSetting userDefine;
    private ProfileDetail userDetailinfo;

    private Map<Long, RelationMiniResourceDTO> boardMap = new LinkedHashMap<Long, RelationMiniResourceDTO>();

    private Boolean first = false;
    private Boolean freshmen = false;

    private String accountUno;
    private AccountDomain accountDomain = AccountDomain.DEFAULT;
    private boolean fullDefAccount = false;
    private Set<AccountDomain> syncDomainSet = new HashSet<AccountDomain>();

    private AuthToken authToken;

    public Integer[] userinfostep = {1, 1, 1, 1};

    private boolean activityTips = true;
    private boolean recommendGroupTips;

    private int pointAmount;

    private Long getCodeTime;
    private Long taoCodeTime;
    //微信订阅号的用户是否绑定手机  n=没有绑定  y=已绑定
    private String weixinPhoneBindStatus;

    public String getAccountUno() {
        return accountUno;
    }

    public void setAccountUno(String accountUno) {
        this.accountUno = accountUno;
    }

    //    public Account getAccount() {
//        return account;
//    }

//    public void setAccount(Account account) {
//        this.account = account;
//    }

    public ProfileSum getCountdata() {
        return countdata;
    }

    public void setCountdata(ProfileSum countdata) {
        this.countdata = countdata;
    }

    public ProfileBlog getBlogwebsite() {
        return blogwebsite;
    }

    public void setBlogwebsite(ProfileBlog blogwebsite) {
        this.blogwebsite = blogwebsite;
    }

    public ProfileSetting getUserDefine() {
        return userDefine;
    }

    public void setUserDefine(ProfileSetting userDefine) {
        this.userDefine = userDefine;
    }

    public ProfileDetail getUserDetailinfo() {
        return userDetailinfo;
    }

    public void setUserDetailinfo(ProfileDetail userDetailinfo) {
        this.userDetailinfo = userDetailinfo;
    }

    public Integer[] getUserinfostep() {
        return userinfostep;
    }

    public void setUserinfostep(Integer[] userinfostep) {
        this.userinfostep = userinfostep;
    }

    public Map<Long, RelationMiniResourceDTO> getBoardMap() {
        return boardMap;
    }

    public void setBoardMap(Map<Long, RelationMiniResourceDTO> boardMap) {
        this.boardMap = boardMap;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getFreshmen() {
        return freshmen;
    }

    public void setFreshmen(Boolean freshmen) {
        this.freshmen = freshmen;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public Set<AccountDomain> getSyncDomainSet() {
        return syncDomainSet;
    }

    public void setSyncDomainSet(Set<AccountDomain> syncDomainSet) {
        this.syncDomainSet = syncDomainSet;
    }

    public boolean containsSyncDomain(AccountDomain syncDomain) {
        return syncDomainSet.contains(syncDomain);
    }

    public AccountDomain getAccountDomain() {
        return accountDomain;
    }

    public void setAccountDomain(AccountDomain accountDomain) {
        this.accountDomain = accountDomain;
    }

    public boolean isActivityTips() {
        return activityTips;
    }

    public void setActivityTips(boolean activityTips) {
        this.activityTips = activityTips;
    }

    public boolean isFullDefAccount() {
        return fullDefAccount;
    }

    public void setFullDefAccount(boolean fullDefAccount) {
        this.fullDefAccount = fullDefAccount;
    }

    public boolean isRecommendGroupTips() {
        return recommendGroupTips;
    }

    public void setRecommendGroupTips(boolean recommendGroupTips) {
        this.recommendGroupTips = recommendGroupTips;
    }

    public int getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(int pointAmount) {
        this.pointAmount = pointAmount;
    }

    public Long getGetCodeTime() {
        return getCodeTime;
    }

    public void setGetCodeTime(Long getCodeTime) {
        this.getCodeTime = getCodeTime;
    }

    public Long getTaoCodeTime() {
        return taoCodeTime;
    }

    public void setTaoCodeTime(Long taoCodeTime) {
        this.taoCodeTime = taoCodeTime;
    }

    public String getWeixinPhoneBindStatus() {
        return weixinPhoneBindStatus;
    }

    public void setWeixinPhoneBindStatus(String weixinPhoneBindStatus) {
        this.weixinPhoneBindStatus = weixinPhoneBindStatus;
    }
}
