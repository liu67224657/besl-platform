package com.enjoyf.platform.service.profile;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-26
 * Time: 上午9:45
 * To change this template use File | Settings | File Templates.
 */
public class DeveloperCountry implements Serializable{

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
