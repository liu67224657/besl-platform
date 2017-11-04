package com.enjoyf.platform.service.social;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-10-17
 * Time: 下午1:25
 * To change this template use File | Settings | File Templates.
 */
public class SocialRelationAtFocus implements Serializable{
    private String uno;
    private String screenName;
    private String description;

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
