package com.enjoyf.platform.webapps.common.dto.profile;

import com.enjoyf.platform.service.profile.VerifyType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Set;

/**
 * @Auther: <a mailto="ericLiu@stuff.enjoyfound.com">ericliu</a>
 * Create time: 12-8-31
 * Description:
 */
public class ProfileSimpleDTO {
    private String uno;
    private String screenName;
    private String blogDomain;
    private String headIcon;
    private VerifyType verifyType;
    private String desc;
    private Set<ProfileSimpleDTO> destProfileList;
    private int destProfileSize;
    private String sex;

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getBlogDomain() {
        return blogDomain;
    }

    public void setBlogDomain(String blogDomain) {
        this.blogDomain = blogDomain;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public VerifyType getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(VerifyType verifyType) {
        this.verifyType = verifyType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Set<ProfileSimpleDTO> getDestProfileList() {
        return destProfileList;
    }

    public void setDestProfileList(Set<ProfileSimpleDTO> destProfileList) {
        this.destProfileList = destProfileList;
    }

    public int getDestProfileSize() {
        return destProfileSize;
    }

    public void setDestProfileSize(int destProfileSize) {
        this.destProfileSize = destProfileSize;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return uno.hashCode();
    }

}
