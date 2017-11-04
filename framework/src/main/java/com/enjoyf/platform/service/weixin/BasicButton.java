package com.enjoyf.platform.service.weixin;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-13
 * Time: 下午2:24
 * To change this template use File | Settings | File Templates.
 */
public class BasicButton implements Serializable {
    private String name;//菜单名称


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
