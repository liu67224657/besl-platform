package com.enjoyf.platform.util.image.cutstragy;

/**
 * Created with IntelliJ IDEA.
 * User: erciliu
 * Date: 12-7-16
 * Time: 下午10:12
 * To change this template use File | Settings | File Templates.
 */
public class CutImageStragyByMiniSize implements CutImageStragy {
    private int requireWidth;
    private int requireHeight;

    public CutImageStragyByMiniSize(int requireWidth, int requireHeight) {
        this.requireWidth = requireWidth;
        this.requireHeight = requireHeight;
    }

    public CutImageProperty calCutproperty(int x, int y, int width, int height) {
        CutImageProperty cutProperty = new CutImageProperty();

        int cutWidth = width;
        int cutHeight = height;
        int cutX = x;
        int cutY = y;

        if (width / height != requireWidth / requireHeight) {
            if (width > height) {
                cutHeight = height > requireHeight ? requireHeight : height;
                cutWidth = (int) ((double) cutHeight * (double) requireWidth / (double) requireHeight);
            } else if (width < height) {
                cutWidth = width > requireWidth ? requireWidth : width;
                cutHeight = (int) ((double) cutWidth * (double) requireHeight / (double) requireWidth);
            } else {
                cutHeight = height > requireHeight ? requireHeight : height;
                cutWidth = width > requireWidth ? requireWidth : width;
            }
            cutX = x + (width - cutWidth) / 2;
            cutY = y + (height - cutHeight) / 2;

        }

        cutProperty.setX(cutX);
        cutProperty.setY(cutY);
        cutProperty.setWidth(cutWidth);
        cutProperty.setHeight(cutHeight);

        return cutProperty;
    }

    public static void main(String[] args) {

        System.out.println(new CutImageStragyByMiniSize(400, 300).calCutproperty(0, 0, 800, 600));

        System.out.println(new CutImageStragyByMiniSize(600, 800).calCutproperty(0, 0, 800, 600));
//
//        System.out.println(new CutImageStragyByMiniSize(600, 800).calCutproperty(0, 0, 300, 100));
    }
}
