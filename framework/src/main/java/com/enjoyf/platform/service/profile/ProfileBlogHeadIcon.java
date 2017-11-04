package com.enjoyf.platform.service.profile;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-12
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class ProfileBlogHeadIcon implements Serializable{
    private Integer id;//排序使用
    private String headIcon;//头像
    private Boolean validStatus = true; //默认可以显示

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public Boolean getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Boolean validStatus) {
        this.validStatus = validStatus;
    }
}
