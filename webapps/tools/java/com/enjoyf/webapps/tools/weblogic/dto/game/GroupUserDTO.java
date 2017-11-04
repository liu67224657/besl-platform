package com.enjoyf.webapps.tools.weblogic.dto.game;

import com.enjoyf.platform.service.gameres.GroupUser;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-25 下午7:46
 * Description:
 */
public class GroupUserDTO {
    private String screenName;
    private GroupUser groupUser;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public GroupUser getGroupUser() {
        return groupUser;
    }

    public void setGroupUser(GroupUser groupUser) {
        this.groupUser = groupUser;
    }
}
