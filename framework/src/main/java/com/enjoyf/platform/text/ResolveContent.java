package com.enjoyf.platform.text;

import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;

import java.util.*;

/**
 * <p/>
 * Description:博文内容处理类的实体
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ResolveContent {
    private String contentUno;
    private String content;
    //  图片，声音，视频
    private Map<Integer, ImageContent> images = new HashMap<Integer, ImageContent>();
    private Map<Integer, AudioContent> audios = new HashMap<Integer, AudioContent>();
    private Map<Integer, VideoContent> videos = new HashMap<Integer, VideoContent>();
    private Map<Integer, AppsContent> apps = new HashMap<Integer, AppsContent>();
    private Map<Integer, GameContent> games = new HashMap<Integer, GameContent>();

    private boolean hasReplyHide;

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<Integer, ImageContent> getImages() {
        return images;
    }

    public void setImages(Map<Integer, ImageContent> images) {
        this.images = images;
    }

    public Map<Integer, AudioContent> getAudios() {
        return audios;
    }

    public void setAudios(Map<Integer, AudioContent> audios) {
        this.audios = audios;
    }

    public Map<Integer, VideoContent> getVideos() {
        return videos;
    }

    public void setVideos(Map<Integer, VideoContent> videos) {
        this.videos = videos;
    }

    public Map<Integer, AppsContent> getApps() {
        return apps;
    }

    public void setApps(Map<Integer, AppsContent> apps) {
        this.apps = apps;
    }

    public Map<Integer, GameContent> getGames() {
        return games;
    }

    public void setGames(Map<Integer, GameContent> games) {
        this.games = games;
    }

    public boolean isHasReplyHide() {
        return hasReplyHide;
    }

    public void setHasReplyHide(boolean hasReplyHide) {
        this.hasReplyHide = hasReplyHide;
    }

    public boolean isNull() {
        return StringUtil.isEmpty(this.content) && CollectionUtil.isEmpty(this.images) && CollectionUtil.isEmpty(this.videos) && CollectionUtil.isEmpty(this.audios) && CollectionUtil.isEmpty(this.apps) && CollectionUtil.isEmpty(this.games);
    }

    public static ResolveContent transferByContent(Content content) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(content.getContent());

        int i = 0;
        if (content.getAudios() != null) {
            for (AudioContent audioContent : content.getAudios().getAudios()) {
                resolveContent.getAudios().put(i, audioContent);
                i++;
            }
        }

        if (content.getImages() != null) {
            i = 0;
            for (ImageContent imageContent : content.getImages().getImages()) {
                resolveContent.getImages().put(i, imageContent);
                i++;
            }
        }

        if (content.getVideos() != null) {
            i = 0;
            for (VideoContent videoContent : content.getVideos().getVideos()) {
                resolveContent.getVideos().put(i, videoContent);
                i++;
            }
        }

        if (content.getApps() != null) {
            i = 0;
            for (AppsContent appsContent : content.getApps().getApps()) {
                resolveContent.getApps().put(i, appsContent);
                i++;
            }
        }

        if (content.getGames() != null) {
            i = 0;
            for (GameContent gameContent : content.getGames().getGames()) {
                resolveContent.getGames().put(i, gameContent);
                i++;
            }
        }

        return resolveContent;
    }
}
