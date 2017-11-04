package com.enjoyf.webapps.joyme.dto.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-3
 * Time: 下午6:18
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppMenuTagDTO implements Serializable{
    private long tagid;
    private String tagname;
    private long menuid;

    public long getTagid() {
        return tagid;
    }

    public void setTagid(long tagid) {
        this.tagid = tagid;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public long getMenuid() {
        return menuid;
    }

    public void setMenuid(long menuid) {
        this.menuid = menuid;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
