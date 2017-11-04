package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-19
 * Time: 下午7:21
 * To change this template use File | Settings | File Templates.
 */
public class MobileProfileSum implements Serializable {

    private Integer focus;
    private Integer fans;
    private Integer blog;
    private Integer fwd;
    private Integer favor;

    public Integer getFocus() {
        return focus;
    }

    public void setFocus(Integer focus) {
        this.focus = focus;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public Integer getBlog() {
        return blog;
    }

    public void setBlog(Integer blog) {
        this.blog = blog;
    }

    public Integer getFwd() {
        return fwd;
    }

    public void setFwd(Integer fwd) {
        this.fwd = fwd;
    }

    public Integer getFavor() {
        return favor;
    }

    public void setFavor(Integer favor) {
        this.favor = favor;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
