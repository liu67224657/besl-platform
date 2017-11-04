package com.enjoyf.webapps.joyme.dto.content;

import com.enjoyf.platform.service.content.ForignContentReply;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.webapps.joyme.dto.profile.ProfileMiniDTO;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-20 下午5:59
 * Description:
 */
public class ForignContentReplyEntry {
    private ForignContentReply reply;
    private ProfileMiniDTO profile;
    private ProfileMiniDTO parentProfile;

    public ForignContentReply getReply() {
        return reply;
    }

    public void setReply(ForignContentReply reply) {
        this.reply = reply;
    }

    public ProfileMiniDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileMiniDTO profile) {
        this.profile = profile;
    }

    public ProfileMiniDTO getParentProfile() {
        return parentProfile;
    }

    public void setParentProfile(ProfileMiniDTO parentProfile) {
        this.parentProfile = parentProfile;
    }
}
