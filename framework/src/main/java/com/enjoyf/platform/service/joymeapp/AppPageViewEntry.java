package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-19
 * Time: 下午7:37
 * To change this template use File | Settings | File Templates.
 */
public class AppPageViewEntry implements Serializable{
    private long id;
    private int locationtype;//0ffline 1online 2native
    private String location;
    private int rtime;
    private String refer;
    private int refertype;//0页面 1菜单 2native
    private Date createTime;

    public int getLocationtype() {
        return locationtype;
    }

    public void setLocationtype(int locationtype) {
        this.locationtype = locationtype;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRtime() {
        return rtime;
    }

    public void setRtime(int rtime) {
        this.rtime = rtime;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public int getRefertype() {
        return refertype;
    }

    public void setRefertype(int refertype) {
        this.refertype = refertype;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
