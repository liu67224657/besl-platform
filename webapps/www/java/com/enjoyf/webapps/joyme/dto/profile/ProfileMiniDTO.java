package com.enjoyf.webapps.joyme.dto.profile;

import com.enjoyf.platform.service.profile.VerifyType;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-22 下午1:07
 * Description:
 */
public class ProfileMiniDTO {
    private String domain;
    private String name;
    private String headicon;
    private VerifyType verifyType;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadicon() {
        return headicon;
    }

    public void setHeadicon(String headicon) {
        this.headicon = headicon;
    }

    public VerifyType getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(VerifyType verifyType) {
        this.verifyType = verifyType;
    }
}
