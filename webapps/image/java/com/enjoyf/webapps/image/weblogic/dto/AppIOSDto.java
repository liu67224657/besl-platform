package com.enjoyf.webapps.image.weblogic.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 * Description:苹果应用dto用于上传后回调显示
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class AppIOSDto {
    private String iconSrc;
    private String appCardSrc;
    private String resourceUrl;

    private List<Map<String,String>> screenShot=new ArrayList<Map<String,String>>();

    public String getIconSrc() {
        return iconSrc;
    }

    public void setIconSrc(String iconSrc) {
        this.iconSrc = iconSrc;
    }

    public String getAppCardSrc() {
        return appCardSrc;
    }

    public void setAppCardSrc(String appCardSrc) {
        this.appCardSrc = appCardSrc;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public List<Map<String,String>> getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(List<Map<String,String>> screenShot) {
        this.screenShot = screenShot;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
