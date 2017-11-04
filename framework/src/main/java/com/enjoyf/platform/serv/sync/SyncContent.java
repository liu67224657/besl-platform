package com.enjoyf.platform.serv.sync;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SyncContent implements Serializable{
    private String syncTopic;
    private String syncTitle;
    private String syncText;
    private String syncImg;
    private String syncContentUrl;
    private String syncContentImageUrl="";

    public String getSyncText() {
        return syncText;
    }

    public void setSyncText(String syncText) {
        this.syncText = syncText;
    }

    public String getSyncImg() {
        return syncImg;
    }

    public void setSyncImg(String syncImg) {
        this.syncImg = syncImg;
    }

    public String getSyncContentUrl() {
        return syncContentUrl;
    }

    public void setSyncContentUrl(String syncContentUrl) {
        this.syncContentUrl = syncContentUrl;
    }

    public String getSyncContentImageUrl() {
        return syncContentImageUrl;
    }

    public void setSyncContentImageUrl(String syncContentImageUrl) {
        this.syncContentImageUrl = syncContentImageUrl;
    }

    public String getSyncTitle() {
        return syncTitle;
    }

    public void setSyncTitle(String syncTitle) {
        this.syncTitle = syncTitle;
    }

    public String getSyncTopic() {
        return syncTopic;
    }

    public void setSyncTopic(String syncTopic) {
        this.syncTopic = syncTopic;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
