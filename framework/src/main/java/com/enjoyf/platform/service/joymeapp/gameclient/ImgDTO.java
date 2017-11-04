package com.enjoyf.platform.service.joymeapp.gameclient;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 15-10-28
 * Time: 下午5:00
 * To change this template use File | Settings | File Templates.
 */
public class ImgDTO implements Serializable {
    private String pic;
    private int width;
    private int height;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
