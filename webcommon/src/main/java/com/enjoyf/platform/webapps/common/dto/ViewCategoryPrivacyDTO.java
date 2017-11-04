/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.dto;

import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.viewline.ViewCategoryPrivacy;
import com.enjoyf.platform.service.viewline.ViewCategoryPrivacyLevel;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description: view category
 */
public class ViewCategoryPrivacyDTO implements Serializable {

    private ProfileBlog profileBlog;

    private ViewCategoryPrivacy categoryPrivacy;

    //
    public ViewCategoryPrivacyDTO() {
    }

    public ProfileBlog getProfileBlog() {
        return profileBlog;
    }

    public void setProfileBlog(ProfileBlog profileBlog) {
        this.profileBlog = profileBlog;
    }

    public ViewCategoryPrivacy getCategoryPrivacy() {
        return categoryPrivacy;
    }

    public void setCategoryPrivacy(ViewCategoryPrivacy categoryPrivacy) {
        this.categoryPrivacy = categoryPrivacy;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
