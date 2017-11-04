package com.enjoyf.platform.cloud.domain.profileservice;

import java.io.Serializable;

/**
 * A VertualProfile.
 */
public class VertualProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;//equals userprofileid

    private String nick;

    private String description;

    private String icon;

    private Integer sex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
