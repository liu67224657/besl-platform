/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>  ,zx
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class ContentType implements Serializable {
    //一句话
    public static final int PHRASE = 1;

    //是否有图片
    public static final int IMAGE = 2;

    //是否有音乐
    public static final int AUDIO = 4;

    //是否有视频
    public static final int VIDEO = 8;

    //是否是长文
    public static final int TEXT = 16;

    //APP
    public static final int APP = 32;

    //VOTE
    public static final int VOTE = 64;

    //VOTED_FORWARD
    public static final int VOTE_FORWARD = 128;

    //GAME
    public static final int GAME = 256;

    //replyHide
    public static final int REPLYHIDE = 512;

    //全部
    public static final int ALL = 1023;

    private int value = 0;

    //
    public ContentType() {
    }

    private ContentType(int v) {
        value = v;
    }

    public ContentType has(int v) {
        value += v;

        return this;
    }

    public int getValue() {
        return value;
    }

    public boolean hasText() {
        return (value & TEXT) > 0;
    }

    public boolean hasPhrase() {
        return (value & PHRASE) > 0;
    }

    public boolean hasImage() {
        return (value & IMAGE) > 0;
    }

    public boolean hasVote() {
        return (value & VOTE) > 0;
    }

    public boolean hasVoteForWard() {
        return (value & VOTE_FORWARD) > 0;
    }

    public boolean onlyImage() {
        return value == TEXT + IMAGE || value == PHRASE + IMAGE;
    }

    public boolean hasAudio() {
        return (value & AUDIO) > 0;
    }

    public boolean onlyAudio() {
        return value == TEXT + AUDIO || value == PHRASE + AUDIO;
    }

    public boolean hasVideo() {
        return (value & VIDEO) > 0;
    }

    public boolean onlyVideo() {
        return value == TEXT + VIDEO || value == PHRASE + VIDEO;
    }

    public boolean hasApp() {
        return (value & APP) > 0;
    }

    public boolean hasGame() {
        return (value & GAME) > 0;
    }

    public boolean hasReplayHide() {
        return (value & REPLYHIDE) > 0;
    }

    public boolean onlyAPP() {
        return value == TEXT + APP || value == PHRASE + APP;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "ContentType: value=" + value;
    }

    public static ContentType getByValue(Integer v) {
        return new ContentType(v);
    }

    public static int getWallContentValue() {
        return APP + IMAGE + VIDEO + AUDIO + TEXT;
    }

}
