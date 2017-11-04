package com.enjoyf.platform.util.image;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ImageElement {
    private ElementLocation location;
    private int xAxis;
    private int yAxis;
    private String imgSrc;
    private ImageBorder imageBoder;

    public ImageBorder getImageBoder() {
        return imageBoder;
    }

    public void setImageBoder(ImageBorder imageBoder) {
        this.imageBoder = imageBoder;
    }

    public ElementLocation getLocation() {
        return location;
    }

    public void setLocation(ElementLocation location) {
        this.location = location;
    }

    public int getxAxis() {
        return xAxis;
    }

    public void setxAxis(int xAxis) {
        this.xAxis = xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }

    public void setyAxis(int yAxis) {
        this.yAxis = yAxis;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
