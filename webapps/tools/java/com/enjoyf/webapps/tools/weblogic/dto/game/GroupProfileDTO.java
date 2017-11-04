package com.enjoyf.webapps.tools.weblogic.dto.game;

import com.enjoyf.platform.service.gameres.GroupUser;
import com.enjoyf.platform.service.gameres.privilege.GroupProfile;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-25 下午7:46
 * Description:
 */
public class GroupProfileDTO {
    private String screenName;
    private GroupProfile groupProfile;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public GroupProfile getGroupProfile() {
        return groupProfile;
    }

    public void setGroupProfile(GroupProfile groupProfile) {
        this.groupProfile = groupProfile;
    }
}
