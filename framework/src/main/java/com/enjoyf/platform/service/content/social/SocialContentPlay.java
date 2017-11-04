package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-22
 * Time: 上午10:55
 * To change this template use File | Settings | File Templates.
 */
public class SocialContentPlay implements Serializable {
    private long cid;
    private int playNum;

    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
