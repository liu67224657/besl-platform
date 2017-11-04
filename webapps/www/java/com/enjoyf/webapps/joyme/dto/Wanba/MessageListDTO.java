package com.enjoyf.webapps.joyme.dto.Wanba;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by zhimingli on 2016/9/21 0021.
 */
public class MessageListDTO implements Serializable {
    private WanbaSocialMessageDTO message;
    private WanbaProfileDTO profile;

    public WanbaSocialMessageDTO getMessage() {
        return message;
    }

    public void setMessage(WanbaSocialMessageDTO message) {
        this.message = message;
    }

    public WanbaProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(WanbaProfileDTO profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
