package com.enjoyf.platform.serv.sync;


import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.usercenter.LoginDomain;

import java.io.Serializable;

//用于处理队列
class ShareWrap implements Serializable {
    private TokenInfo tokenInfo;
    private SyncContent syncContent;
    private LoginDomain loginDomain;
    private String profileId;
    private long shareId;

    ShareWrap(TokenInfo tokenInfo, SyncContent syncContent, LoginDomain loginDomain,String profileId,long shareId) {
        this.tokenInfo = tokenInfo;
        this.syncContent = syncContent;
        this.loginDomain = loginDomain;
        this.profileId =profileId;
        this.shareId=shareId;
    }

    public long getShareId() {
        return shareId;
    }

    public void setShareId(long shareId) {
        this.shareId = shareId;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public SyncContent getSyncContent() {
        return syncContent;
    }

    public LoginDomain getLoginDomain() {
        return loginDomain;
    }

    public String getProfileId() {
        return profileId;
    }

}