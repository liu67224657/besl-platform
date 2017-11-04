package com.enjoyf.platform.service.weixin.resp;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-14
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 * 音乐消息
 */
public class RespMusicMessage extends RespBaseMessage{
    // 音乐
    private Music Music;

    public Music getMusic() {
        return Music;
    }

    public void setMusic(Music music) {
        Music = music;
    }
}
