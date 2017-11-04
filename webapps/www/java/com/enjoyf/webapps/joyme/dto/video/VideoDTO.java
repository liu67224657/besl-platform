package com.enjoyf.webapps.joyme.dto.video;

/**
 * Created by zhitaoshi on 2016/2/24.
 */
public class VideoDTO {
    private String name;//视频名
    private String desc;//视频描述
    private String uri;//视频路径
    private String appkey;//视频的appkey

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }
}
