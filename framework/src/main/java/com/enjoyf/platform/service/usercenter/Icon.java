package com.enjoyf.platform.service.usercenter;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-12
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class Icon implements Serializable{
    private Integer id;//排序使用
    private String icon;//头像

    public Icon() {
    }

    public Icon(Integer id, String icon) {
        this.id = id;
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
