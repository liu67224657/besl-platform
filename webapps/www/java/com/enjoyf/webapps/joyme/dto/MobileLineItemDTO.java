package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-9
 * Time: 下午4:29
 * To change this template use File | Settings | File Templates.
 */
public class MobileLineItemDTO implements Serializable {

    private MobileProfileMiniDTO mini;
    private MobileContentDTO ct;

    private MobileLineItemDTO root;

    //是否喜欢，1：喜欢， 0：不喜欢
    private Integer fav;
    //喜欢日期
    private Long fdt;

    public MobileProfileMiniDTO getMini() {
        return mini;
    }

    public void setMini(MobileProfileMiniDTO mini) {
        this.mini = mini;
    }

    public MobileContentDTO getCt() {
        return ct;
    }

    public void setCt(MobileContentDTO ct) {
        this.ct = ct;
    }

    public MobileLineItemDTO getRoot() {
        return root;
    }

    public void setRoot(MobileLineItemDTO root) {
        this.root = root;
    }

    public Integer getFav() {
        return fav;
    }

    public void setFav(Integer fav) {
        this.fav = fav;
    }

    public Long getFdt() {
        return fdt;
    }

    public void setFdt(Long fdt) {
        this.fdt = fdt;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
