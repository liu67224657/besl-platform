package com.enjoyf.platform.service.content;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-8-20
 * Time: 下午2:20
 * To change this template use File | Settings | File Templates.
 */
public class InteractionContentType implements Serializable {
    //是否有图片
    public static final int IMAGE = 2;

    //是否有音乐
    public static final int AUDIO = 4;

    //是否有视频
    public static final int VIDEO = 8;

    //是否是APP
    public static final int APP = 16;

    //全部
    public static final int ALL = 31;

    private int value = 0;

    //
    public InteractionContentType() {
    }

    private InteractionContentType(int v) {
        value = v;
    }

    public InteractionContentType has(int v) {
        value += v;
        return this;
    }

    public int getValue() {
        return value;
    }

    public boolean hasImage() {
        return (value & IMAGE) > 0;
    }

    public boolean onlyImage() {
        return value == IMAGE;
    }

    public boolean hasAudio() {
        return (value & AUDIO) > 0;
    }

    public boolean onlyAudio() {
        return value == AUDIO;
    }

    public boolean hasVideo() {
        return (value & VIDEO) > 0;
    }

    public boolean onlyVideo() {
        return value == VIDEO;
    }

    public boolean hasApp() {
        return (value & APP) > 0;
    }

    public boolean onlyAPP() {
        return value == APP;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "InteractionContentType: value=" + value;
    }

    public static InteractionContentType getByValue(Integer v) {
        return new InteractionContentType(v);
    }
}
