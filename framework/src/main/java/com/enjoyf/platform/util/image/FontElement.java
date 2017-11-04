package com.enjoyf.platform.util.image;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class FontElement {
    private ElementLocation location;
    private String ttfPath;
    private int fontSize; private String fontColor;
    private int xAxis;
    private int yAxis;
    private String text;

    public ElementLocation getLocation() {
        return location;
    }

    public void setLocation(ElementLocation location) {
        this.location = location;
    }

    public String getTtfPath() {
        return ttfPath;
    }

    public void setTtfPath(String ttfPath) {
        this.ttfPath = ttfPath;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
