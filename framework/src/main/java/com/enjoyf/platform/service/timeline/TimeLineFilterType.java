package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.service.content.ContentType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-10-19
 * Time: 下午7:33
 * Desc:
 */
public class TimeLineFilterType implements Serializable{


   public static final Integer NOT_FILTER = 1023;// 1023;  全部

    //我关注的人
   public static final Integer MY_FOCUS  = 2;

    //投票
    public static final Integer VOTE  = 4;

    //投票转发
    public static final Integer VOTE_FORWARD  = 8;

   //---文章类型
   //一句话
    public static final Integer PHRASE = 32;

    //是否有图片
    public static final Integer IMAGE = 64;

    //是否有音乐
    public static final Integer AUDIO = 128;

    //是否有视频
    public static final Integer VIDEO = 256;

    //是否是长文
    public static final Integer TEXT = 512;




   private Integer value = 0;

    //
    public TimeLineFilterType() {
    }

    private TimeLineFilterType(Integer v) {
        value = v;
    }

    public TimeLineFilterType has(Integer v) {
        value += v;

        return this;
    }

    public Integer getValue() {
        return value;
    }
//
    public boolean hasMyFocus() {
        return (value & MY_FOCUS) > 0;
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

    public boolean hasAudio() {
        return (value & AUDIO) > 0;
    }

    public boolean hasVideo() {
        return (value & VIDEO) > 0;
    }

    public boolean hasVote() {
        return (value & VOTE) > 0;
    }

    public boolean hasVoteForWard() {
        return (value & VOTE_FORWARD) > 0;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "TimeLineFilterType: value=" + value;
    }

    public static TimeLineFilterType getByValue(Integer v) {
        return new TimeLineFilterType(v);
    }

    /**把文章类型转换成timeline类型*/
    public static TimeLineFilterType ContentTypeToTimeLineFilterType(ContentType contentType){
        TimeLineFilterType timeLineFilterType = new TimeLineFilterType();

        if(contentType == null){
            timeLineFilterType.has(1);
            return  timeLineFilterType;
        }

        if(contentType.hasImage()){
            timeLineFilterType.has(IMAGE);
        }
        if(contentType.hasVideo()){
            timeLineFilterType.has(VIDEO);
        }
        if(contentType.hasAudio()){
            timeLineFilterType.has(AUDIO);
        }
        if(contentType.hasPhrase()){
            timeLineFilterType.has(PHRASE);
        }
        if(contentType.hasText()){
            timeLineFilterType.has(TEXT);
        }
        if(contentType.hasVote()){
            timeLineFilterType.has(VOTE);    
        }
        if(contentType.hasVoteForWard()){
            timeLineFilterType.has(VOTE_FORWARD);
        }

        return timeLineFilterType;
    }
}
