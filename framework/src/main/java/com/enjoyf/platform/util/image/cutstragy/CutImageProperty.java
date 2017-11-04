package com.enjoyf.platform.util.image.cutstragy;

/**
 * Created with IntelliJ IDEA.
 * User: erciliu
 * Date: 12-7-16
 * Time: 下午10:15
 * To change this template use File | Settings | File Templates.
 */
public class CutImageProperty {
    private int x;
    private int y;
    private int width;
    private int height;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
        return "CutProperty{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
