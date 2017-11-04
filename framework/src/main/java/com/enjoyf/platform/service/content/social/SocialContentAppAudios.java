package com.enjoyf.platform.service.content.social;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-17
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public class SocialContentAppAudios implements Serializable {
    private String mp3;
    private String amr;

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getAmr() {
        return amr;
    }

    public void setAmr(String amr) {
        this.amr = amr;
    }
}
