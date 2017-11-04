package com.enjoyf.webapps.joyme.weblogic.message;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.service.message.MessageTopic;
import com.enjoyf.platform.service.profile.ProfileBlog;

/**
 * <p/>
 * Description:得到私信和用户信息
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ReceMessageInfo {

    private PageRows<MessageTopic> rePage = null;
    private ProfileBlog profileBlog = null;


    public PageRows<MessageTopic> getRePage() {
        return rePage;
    }

    public void setRePage(PageRows<MessageTopic> rePage) {
        this.rePage = rePage;
    }

    public ProfileBlog getProfileBlog() {
        return profileBlog;
    }

    public void setProfileBlog(ProfileBlog profileBlog) {
        this.profileBlog = profileBlog;
    }
}
