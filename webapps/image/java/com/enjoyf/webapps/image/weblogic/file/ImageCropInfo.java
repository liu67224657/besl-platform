package com.enjoyf.webapps.image.weblogic.file;

import java.io.Serializable;

/**
 * 图片截图参数
 * @author huazhang
 *
 */
public class ImageCropInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;//x坐标位置
	private int y;//y坐标位置
	private int width;//截图宽度
	private int height;//截图高度
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
	
	
}
