package com.enjoyf.platform.props;

import com.enjoyf.platform.util.FiveProps;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-16
 * Time: 下午5:10
 * To change this template use File | Settings | File Templates.
 */
public class ImageConfig {
    private static ImageConfig instance;

    private FiveProps props;

    //image name.suffix
    private static final String KEY_M_IMAGE_SUFFIX = "m.image.suffix";
    private static final String KEY_S_IMAGE_SUFFIX = "s.image.suffix";
    private static final String KEY_SS_IMAGE_SUFFIX = "ss.image.suffix";

    //image size
    private static final String KEY_BIG_IMAGE_SIZE = "big.image.size";
    private static final String KEY_BIG_MAX_IMAGE_SIZE = "big.max.image.size";

    //width and height
    private static final String KEY_M_IMAGE_WIDTH = "m.image.width";
    private static final String KEY_M_IMAGE_HEIGHT = "m.image.height";
    private static final String KEY_S_IMAGE_WIDTH = "s.image.width";
    private static final String KEY_S_IMAGE_HEIGHT = "s.image.height";
    private static final String KEY_SS_IMAGE_WIDTH = "ss.image.width";
    private static final String KEY_SS_IMAGE_HEIGHT = "ss.image.height";

    private static final String KEY_RESIZE_BEISHU = "resize.beishu";
    private static final String KEY_RESIZE_BTKG = "resize.btkg";
    private static final String KEY_RESIZE_WALL_RATE = "resize.wall.rate";

    private String mImageSuffix = "_M";
    private String sImageSuffix = "_S";
    private String ssImageSuffix = "_SS";

    private int bigImageSize = 2097152;
    private int bigMaxImageSize = 4194304;

    private int mImageWidth = 590;
    private int mImageHeight = 590;
    private int sImageWidth = 110;
    private int sImageHeight = 110;
    private int ssImageWidth = 188;
    private int ssImageHeight = 141;

    private double resizeBeishu = 3.67;
    private int resizeBTKG = 30;
    private double resizeWallRate = 1.33;

    private ImageConfig() {
        init();
    }

    public static synchronized ImageConfig get() {
        if (instance == null) {
            instance = new ImageConfig();
        }

        return instance;
    }

    private void init() {
        props = new FiveProps(EnvConfig.get().getImageConfigFile());

        if (props != null) {
            mImageSuffix = props.get(KEY_M_IMAGE_SUFFIX,mImageSuffix);
            sImageSuffix = props.get(KEY_S_IMAGE_SUFFIX,sImageSuffix);
            ssImageSuffix = props.get(KEY_SS_IMAGE_SUFFIX,ssImageSuffix);

            bigImageSize = props.getInt(KEY_BIG_IMAGE_SIZE,bigImageSize);
            bigMaxImageSize = props.getInt(KEY_BIG_MAX_IMAGE_SIZE,bigMaxImageSize);

            mImageWidth = props.getInt(KEY_M_IMAGE_WIDTH,mImageWidth);
            mImageHeight = props.getInt(KEY_M_IMAGE_HEIGHT,mImageHeight);
            sImageWidth = props.getInt(KEY_S_IMAGE_WIDTH,sImageWidth);
            sImageHeight = props.getInt(KEY_S_IMAGE_HEIGHT,sImageHeight);
            ssImageWidth = props.getInt(KEY_SS_IMAGE_WIDTH,ssImageWidth);
            ssImageHeight = props.getInt(KEY_SS_IMAGE_HEIGHT,ssImageHeight);

            resizeBeishu = props.getDouble(KEY_RESIZE_BEISHU,resizeBeishu);
            resizeBTKG = props.getInt(KEY_RESIZE_BTKG,resizeBTKG);
            resizeWallRate = props.getDouble(KEY_RESIZE_WALL_RATE,resizeWallRate);

        }

    }

    // getter and setter
    public String getmImageSuffix() {
        return mImageSuffix;
    }

    public void setmImageSuffix(String mImageSuffix) {
        this.mImageSuffix = mImageSuffix;
    }

    public String getsImageSuffix() {
        return sImageSuffix;
    }

    public void setsImageSuffix(String sImageSuffix) {
        this.sImageSuffix = sImageSuffix;
    }

    public String getSsImageSuffix() {
        return ssImageSuffix;
    }

    public void setSsImageSuffix(String ssImageSuffix) {
        this.ssImageSuffix = ssImageSuffix;
    }

    public int getBigImageSize() {
        return bigImageSize;
    }

    public void setBigImageSize(int bigImageSize) {
        this.bigImageSize = bigImageSize;
    }

    public int getBigMaxImageSize() {
        return bigMaxImageSize;
    }

    public void setBigMaxImageSize(int bigMaxImageSize) {
        this.bigMaxImageSize = bigMaxImageSize;
    }

    public int getmImageWidth() {
        return mImageWidth;
    }

    public void setmImageWidth(int mImageWidth) {
        this.mImageWidth = mImageWidth;
    }

    public int getmImageHeight() {
        return mImageHeight;
    }

    public void setmImageHeight(int mImageHeight) {
        this.mImageHeight = mImageHeight;
    }

    public int getsImageWidth() {
        return sImageWidth;
    }

    public void setsImageWidth(int sImageWidth) {
        this.sImageWidth = sImageWidth;
    }

    public int getsImageHeight() {
        return sImageHeight;
    }

    public void setsImageHeight(int sImageHeight) {
        this.sImageHeight = sImageHeight;
    }

    public int getSsImageWidth() {
        return ssImageWidth;
    }

    public void setSsImageWidth(int ssImageWidth) {
        this.ssImageWidth = ssImageWidth;
    }

    public int getSsImageHeight() {
        return ssImageHeight;
    }

    public void setSsImageHeight(int ssImageHeight) {
        this.ssImageHeight = ssImageHeight;
    }

    public double getResizeBeishu() {
        return resizeBeishu;
    }

    public void setResizeBeishu(double resizeBeishu) {
        this.resizeBeishu = resizeBeishu;
    }

    public int getResizeBTKG() {
        return resizeBTKG;
    }

    public void setResizeBTKG(int resizeBTKG) {
        this.resizeBTKG = resizeBTKG;
    }

    public double getResizeWallRate() {
        return resizeWallRate;
    }

    public void setResizeWallRate(double resizeWallRate) {
        this.resizeWallRate = resizeWallRate;
    }
}
