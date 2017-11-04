package com.enjoyf.webapps.tools.weblogic.dto.developers;

import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileDetail;
import com.enjoyf.platform.service.profile.ProfileDeveloper;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-23
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class ProfileDeveloperDTO {

    private ProfileBlog blog;
    private ProfileDetail detail;

    private ProfileDeveloper developer;

    public ProfileBlog getBlog() {
        return blog;
    }

    public void setBlog(ProfileBlog blog) {
        this.blog = blog;
    }

    public ProfileDetail getDetail() {
        return detail;
    }

    public void setDetail(ProfileDetail detail) {
        this.detail = detail;
    }

    public ProfileDeveloper getDeveloper() {
        return developer;
    }

    public void setDeveloper(ProfileDeveloper developer) {
        this.developer = developer;
    }
}
