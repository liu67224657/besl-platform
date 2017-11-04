package com.enjoyf.platform.service.content.social;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-13
 * Time: 下午12:36
 * To change this template use File | Settings | File Templates.
 */
public class SocialAward implements Serializable {

    private String name;
    private String level;
    private String total;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}