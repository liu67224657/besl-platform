package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: zhaoxin
 * Date: 11-8-22
 * Time: 下午5:28
 * Desc: 音乐实体类
 */
public class VideoContentSet implements Serializable {
    //
    private Set<VideoContent> videos = new LinkedHashSet<VideoContent>();

    public VideoContentSet() {
    }

    public VideoContentSet(Set set) {
        this.videos = set;
    }

    public VideoContentSet(Collection videos) {
        this.videos.addAll(videos);
    }

    public VideoContentSet(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                videos = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<VideoContent>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("VideoContentSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public Set<VideoContent> getVideos() {
        return videos;
    }

    public void add(VideoContent video) {
        videos.add(video);
    }

    public void add(Set<VideoContent> videos) {
        this.videos.addAll(videos);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(videos);
    }

    public static VideoContentSet parse(String jsonStr) {
        VideoContentSet returnValue = new VideoContentSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<VideoContent> videoContents = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<Set<VideoContent>>() {
                });

                returnValue.add(videoContents);
            } catch (IOException e) {
                GAlerter.lab("VideoContentSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
