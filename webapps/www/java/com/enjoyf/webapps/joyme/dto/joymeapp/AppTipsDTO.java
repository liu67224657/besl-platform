package com.enjoyf.webapps.joyme.dto.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-20
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
public class AppTipsDTO {

    private long tipsid;
    private String description;
    private long updatetime;

    public long getTipsid() {
        return tipsid;
    }

    public void setTipsid(long tipsid) {
        this.tipsid = tipsid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
