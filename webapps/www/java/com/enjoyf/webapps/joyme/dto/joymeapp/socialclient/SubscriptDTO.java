package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

import com.enjoyf.platform.service.content.social.SubscriptType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-30
 * Time: 下午2:27
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptDTO {

    private int type = SubscriptType.NULL.getCode();
    private long starttime;
    private long endtime;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }
}
