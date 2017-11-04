package com.enjoyf.webapps.joyme.weblogic.chinajoy;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-5-24
 * Time: 下午3:33
 * To change this template use File | Settings | File Templates.
 */
public class ContentInfoDTO {
    private String title;
    private String context;

    private Set<ClientImageContent> images;
    private Set<ClientAudioContent> audios;
    private Set<ClientVideoContent> videos;
    private Set<ClientAppContent> apps;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Set<ClientImageContent> getImages() {
        return images;
    }

    public void setImages(Set<ClientImageContent> images) {
        this.images = images;
    }

    public Set<ClientAudioContent> getAudios() {
        return audios;
    }

    public void setAudios(Set<ClientAudioContent> audios) {
        this.audios = audios;
    }

    public Set<ClientVideoContent> getVideos() {
        return videos;
    }

    public void setVideos(Set<ClientVideoContent> videos) {
        this.videos = videos;
    }

    public Set<ClientAppContent> getApps() {
        return apps;
    }

    public void setApps(Set<ClientAppContent> apps) {
        this.apps = apps;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
