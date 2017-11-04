package com.enjoyf.webapps.joyme.dto.joymeapp.advertise;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.advertise.app.PublishParam;
import com.enjoyf.platform.service.joymeapp.AppPlatform;

import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  2014/6/10 11:49
 * Description:
 */
public class AdvertiseDTO {
    private long id;
    private String name;
    private String desc;
    private String url;
    private String pic_url;
    private String pic_url2;

    //non db
    private long start_time;
    private long end_time;
    private long publish_id;
    private PublishParam param;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getPic_url2() {
        return pic_url2;
    }

    public void setPic_url2(String pic_url2) {
        this.pic_url2 = pic_url2;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public long getPublish_id() {
        return publish_id;
    }

    public void setPublish_id(long publish_id) {
        this.publish_id = publish_id;
    }

    public PublishParam getParam() {
        return param;
    }

    public void setParam(PublishParam param) {
        this.param = param;
    }
}
