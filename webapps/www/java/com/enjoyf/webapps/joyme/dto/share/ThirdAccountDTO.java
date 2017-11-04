package com.enjoyf.webapps.joyme.dto.share;

import com.enjoyf.platform.service.account.TokenInfo;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-25
 * Time: 上午11:52
 * To change this template use File | Settings | File Templates.
 */
public class ThirdAccountDTO {
    private String domainCode;
    private TokenInfo tokenInfo;

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }




}
