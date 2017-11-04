package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

import com.enjoyf.platform.util.PageRows;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-18
 * Time: 下午8:17
 * To change this template use File | Settings | File Templates.
 */
public class SocialContentHomeBlogDTO {
    private PageRows<SocialProfileContentDTO> contentlist;
    private ProfileDTO profile;

    public PageRows<SocialProfileContentDTO> getContentlist() {
        return contentlist;
    }

    public void setContentlist(PageRows<SocialProfileContentDTO> contentlist) {
        this.contentlist = contentlist;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }
}
