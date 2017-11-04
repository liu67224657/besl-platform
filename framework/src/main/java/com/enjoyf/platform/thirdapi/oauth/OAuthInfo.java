package com.enjoyf.platform.thirdapi.oauth;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.account.AuthVersion;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * <p/>
 * Description:第三方接口认证的token实体类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class OAuthInfo implements Serializable{
    private long tokenId;

    private AuthVersion authVersion;
    private AccountDomain syncProvider;
    private String forignId;
    private String forignName;
    private String uno;  //only use import data

    private String figureUrl;

    private TokenInfo accessToken=new TokenInfo();
    private Date authDate;
    private Date createDate;

    private ActStatus removeStatus=ActStatus.UNACT;

    public String getForignId() {
        return forignId;
    }

    public void setForignId(String forignId) {
        this.forignId = forignId;
    }

    public String getForignName() {
        return forignName;
    }

    public void setForignName(String forignName) {
        this.forignName = forignName;
    }

    public AccountDomain getSyncProvider() {
        return syncProvider;
    }

    public void setSyncProvider(AccountDomain syncProvider) {
        this.syncProvider = syncProvider;
    }

    public TokenInfo getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(TokenInfo accessToken) {
        this.accessToken = accessToken;
    }

    public Date getAuthDate() {
        return authDate;
    }

    public void setAuthDate(Date authDate) {
        this.authDate = authDate;
    }

    public ActStatus getRemoveStatus() {
        return removeStatus;
    }

    public void setRemoveStatus(ActStatus removeStatus) {
        this.removeStatus = removeStatus;
    }

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public AuthVersion getAuthVersion() {
        return authVersion;
    }

    public void setAuthVersion(AuthVersion authVersion) {
        this.authVersion = authVersion;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getFigureUrl() {
        return figureUrl;
    }

    public void setFigureUrl(String figureUrl) {
        this.figureUrl = figureUrl;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


}
