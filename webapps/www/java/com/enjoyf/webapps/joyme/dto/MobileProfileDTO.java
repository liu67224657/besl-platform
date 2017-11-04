package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-19
 * Time: 下午7:16
 * To change this template use File | Settings | File Templates.
 */
public class MobileProfileDTO implements Serializable {
    private String uno;
    private String name;
    private String icon;
    private String desc;
    //是否已关注，关注为1，未关注为0
    private Integer f;

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getF() {
        return f;
    }

    public void setF(Integer f) {
        this.f = f;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
