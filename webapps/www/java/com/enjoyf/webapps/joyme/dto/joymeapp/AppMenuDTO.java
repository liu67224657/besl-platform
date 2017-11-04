package com.enjoyf.webapps.joyme.dto.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-11
 * Time: 下午4:48
 * To change this template use File | Settings | File Templates.
 */
public class AppMenuDTO implements Serializable{
    private AppCategoryMenuDTO top = new AppCategoryMenuDTO();
    private AppCategoryMenuDTO topBelow = new AppCategoryMenuDTO();
    private AppCategoryMenuDTO middle = new AppCategoryMenuDTO();
    private AppCategoryMenuDTO hotrecommend = new AppCategoryMenuDTO();

    public AppCategoryMenuDTO getTop() {
        return top;
    }

    public void setTop(AppCategoryMenuDTO top) {
        this.top = top;
    }

    public AppCategoryMenuDTO getTopBelow() {
        return topBelow;
    }

    public void setTopBelow(AppCategoryMenuDTO topBelow) {
        this.topBelow = topBelow;
    }

    public AppCategoryMenuDTO getMiddle() {
        return middle;
    }

    public void setMiddle(AppCategoryMenuDTO middle) {
        this.middle = middle;
    }

    public AppCategoryMenuDTO getHotrecommend() {
        return hotrecommend;
    }

    public void setHotrecommend(AppCategoryMenuDTO hotrecommend) {
        this.hotrecommend = hotrecommend;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
