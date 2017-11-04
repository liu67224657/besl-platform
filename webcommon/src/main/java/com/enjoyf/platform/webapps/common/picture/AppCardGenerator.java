package com.enjoyf.platform.webapps.common.picture;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FileUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * <p/>
 * Description:生成app logo以及app 名片类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class AppCardGenerator {
    private static final String APP_BACK_GROUND = "/img/ios/ios-background.jpg";
    private static final String LOGO_MASK = "/img/ios/mask.png";
    private static final String IMG_EXTNAME_JPG = ".jpg";
    private static final String IMG_FONTS_SRC =  "/fonts/msyh.ttf";
    
    private static Logger logger = LoggerFactory.getLogger(AppCardGenerator.class);

    /**
     * @param "D:\\app\\logo.jpg"
     * @return
     * @throws IOException
     * @throws IM4JavaException
     * @throws InterruptedException
     */
    public String generatorAppLogo(InputStream inputStream, String logoDir, String logoName, String rootPath, String basePath) throws IOException, IM4JavaException, InterruptedException {
        String returnSrc = logoDir + logoName + IMG_EXTNAME_JPG;

        String dirPath = rootPath + logoDir;
        if (!FileUtil.isFileOrDirExist(dirPath)) {
            try {
                boolean logoExists = FileUtil.createDirectory(dirPath);
                logger.debug("create app logo dir：" + logoExists);
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
            }
        }

        String allFilename = dirPath + logoName + IMG_EXTNAME_JPG;
        Image img = ImageIO.read(inputStream);

        CompositeCmd logoCmd = new CompositeCmd(true);
        IMOperation logoOP = new IMOperation();
        logoOP.quality(100.00);
        logoOP.thumbnail(178, 178);
        logoOP.addImage();
        logoOP.addImage();
        logoOP.addImage();
        logoCmd.run(logoOP, basePath + LOGO_MASK, img, allFilename);
        return returnSrc;
    }

    /**
     * 生成app名片
     * @param appDir /r001/app/2011/12/50/
     * @param appFileName xxxxxxx
     * @param logo /roo1/app/2011/12/50/xxxxx.jpg
     * @param paramMap  dnymic pictur elemnts containts (appName price size)
     * @param appDevicePics ipad iphone
     * @param rootPath /opt/uploads/
     * @param basePath  /hotdocs/...../
     * @return appdir+appfileName+ext
     * @throws IM4JavaException
     * @throws InterruptedException
     */
    public String generatorImage(String appDir, String appFileName, String logo, Map<PictureElement, String> paramMap, Set<AppDevicePic> appDevicePics, String rootPath, String basePath) throws IM4JavaException, InterruptedException {
        String returnSrc = appDir + appFileName + IMG_EXTNAME_JPG;

        ConvertCmd convert = new ConvertCmd(true);
        String dirPath = rootPath + appDir;
        if (!FileUtil.isFileOrDirExist(dirPath)) {
            try {
                boolean exists = FileUtil.createDirectory(dirPath);
                logger.debug(" app logo dir：" + exists);
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
            }
        }

        String allFileName = dirPath + appFileName + IMG_EXTNAME_JPG;

        IMOperation op = new IMOperation();
        op.quality(100.00);
        if (paramMap.containsKey(PictureElement.IOS_ELEMENT_NAME)) {
            String appName=paramMap.get(PictureElement.IOS_ELEMENT_NAME).length()>30?paramMap.get(PictureElement.IOS_ELEMENT_NAME).substring(0,29)+"...":paramMap.get(PictureElement.IOS_ELEMENT_NAME);
            appName=appName.replace("'","\\'");
            op.font(basePath + IMG_FONTS_SRC).gravity("northwest").pointsize(18).fill("#555").draw("text 22,35 '" + appName + "'");
        }
        if (paramMap.containsKey(PictureElement.IOS_ELEMENT_CATEGORY)) {
             op.font(basePath + IMG_FONTS_SRC).gravity("northwest").pointsize(14).fill("#555").draw("text 255,65 '" + paramMap.get(PictureElement.IOS_ELEMENT_CATEGORY) + "'");
        }
        if (paramMap.containsKey(PictureElement.IOS_ELEMENT_PRICE)) {
            op.font(basePath + IMG_FONTS_SRC).gravity("northwest").pointsize(14).fill("#555").draw("text 255,89 '" + paramMap.get(PictureElement.IOS_ELEMENT_PRICE) + "'");
        }

        String size=paramMap.get(PictureElement.IOS_ELEMENT_SIZE);
        if (!StringUtil.isEmpty(size)) {
            op.font(basePath + IMG_FONTS_SRC).gravity("northwest").pointsize(14).fill("#555").draw("text 255,138 '" + size.toUpperCase() + "'");
        }
        op.addImage();
        op.addImage();

        CompositeCmd compositeCmd = new CompositeCmd(true);
        IMOperation compOp = new IMOperation();
        compOp.quality(100.00);
        compOp.gravity("west");
        compOp.addRawArgs("-geometry", "+20+11");
        compOp.addImage();
        compOp.addImage();
        compOp.addImage();

        try {
            convert.run(op, basePath + APP_BACK_GROUND, allFileName);
        } catch (IOException e) {
            logger.error("generatorImage occured IOException.", e);
        } catch (InterruptedException e) {
            logger.error("generatorImage occured InterruptedException.", e);
        } catch (IM4JavaException e) {
            logger.error("generatorImage occured IM4JavaException.", e);
        }

        try {
            compositeCmd.run(compOp, logo, allFileName, allFileName);
        } catch (IOException e) {
            logger.error("generatorImage occured IOException.", e);
        }
        if (paramMap.containsKey(PictureElement.IOS_ELEMENT_RATING)) {
                try {
                    Double rating = Double.parseDouble(paramMap.get(PictureElement.IOS_ELEMENT_RATING));
                    drawRating(rating, basePath, allFileName);
                } catch (NumberFormatException e) {
                }
            }
            drawDevice(appDevicePics, basePath, allFileName);

        return returnSrc;
    }

    private void drawDevice(Set<AppDevicePic> appDevicePics, String basePath, String outPutPath) {
        try {
            int xAxis = 280;
            if (!CollectionUtil.isEmpty(appDevicePics)) {
                for (AppDevicePic type : appDevicePics) {
                    CompositeCmd typeCmd = new CompositeCmd(true);
                    IMOperation typeOp = new IMOperation();
                    typeOp.quality(100.00);
                    typeOp.gravity("northwest");
                    typeOp.addRawArgs("-geometry", "+" + xAxis + "+150");
                    typeOp.addImage();
                    typeOp.addImage();
                    typeOp.addImage();
                    typeCmd.run(typeOp, basePath + type.getDeviceSrc(), outPutPath, outPutPath);
                    xAxis += 30;
                }
            }
        } catch (IOException e) {
            logger.error("drawDevice occured IOException.", e);
        } catch (InterruptedException e) {
            logger.error("drawDevice occured InterruptedException.", e);
        } catch (IM4JavaException e) {
            logger.error("drawDevice occured IM4JavaException.", e);
        }
    }

    private void drawRating(Double rating, String basePath, String outPutPath) {

        IOSRatingPic iosRating = IOSRatingPic.getByCode(rating);

        if (iosRating == null) {
            return;
        }
        CompositeCmd typeCmd = new CompositeCmd(true);
        IMOperation typeOp = new IMOperation();
        typeOp.quality(100.00);
        typeOp.gravity("northwest");
        typeOp.addRawArgs("-geometry", "+253+102");
        typeOp.addImage();
        typeOp.addImage();
        typeOp.addImage();
        try {
            typeCmd.run(typeOp, basePath + iosRating.getRatingSrc(), outPutPath, outPutPath);
        } catch (IOException e) {
            logger.error("drawRating occured IOException.", e);
        } catch (InterruptedException e) {
            logger.error("drawRating occured InterruptedException.", e);
        } catch (IM4JavaException e) {
            logger.error("drawRating occured IM4JavaException.", e);
        }
    }

    /**
     * <p/>
     * Description:图片评分
     * </p>
     *
     * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
     */
    public static class IOSRatingPic {
        private Double rating;
        private String ratingSrc;

        private static Map<Double, IOSRatingPic> map = new HashMap<Double, IOSRatingPic>();

        public static final IOSRatingPic STAR_HALF = new IOSRatingPic(0.5, "/img/ios/rating_half.png");
        public static final IOSRatingPic STAR_1 = new IOSRatingPic(1.0, "/img/ios/rating_1.png");
        public static final IOSRatingPic STAR_HALF_1 = new IOSRatingPic(1.5, "/img/ios/rating_half_1.png");
        public static final IOSRatingPic STAR_2 = new IOSRatingPic(2.0, "/img/ios/rating_2.png");
        public static final IOSRatingPic STAR_HALF_2 = new IOSRatingPic(2.5, "/img/ios/rating_half_2.png");
        public static final IOSRatingPic STAR_3 = new IOSRatingPic(3.0, "/img/ios/rating_3.png");
        public static final IOSRatingPic STAR_HALF_3 = new IOSRatingPic(3.5, "/img/ios/rating_half_3.png");
        public static final IOSRatingPic STAR_4 = new IOSRatingPic(4.0, "/img/ios/rating_4.png");
        public static final IOSRatingPic STAR_HALF_4 = new IOSRatingPic(4.5, "/img/ios/rating_half_4.png");
        public static final IOSRatingPic STAR_5 = new IOSRatingPic(5.0, "/img/ios/rating_5.png");


        private IOSRatingPic(Double rating, String ratingSrc) {
            this.rating = rating;
            this.ratingSrc = ratingSrc;
            map.put(this.rating, this);
        }

        public Double getRating() {
            return rating;
        }

        public String getRatingSrc() {
            return ratingSrc;
        }

        public static IOSRatingPic getByCode(Double rating) {

            return map.get(rating);
        }

        @Override
        public int hashCode() {
            return rating.hashCode();
        }

        @Override
        public String toString() {
            return ReflectUtil.fieldsToString(this);
        }

        @Override
        public boolean equals(Object obj) {
            if ((obj == null) || !(obj instanceof IOSRatingPic)) {
                return false;
            }

            return rating.doubleValue() == (((IOSRatingPic) obj).getRating().doubleValue());
        }
    }

    /**
     * <p/>
     * Description:ios动态坐标元素的key
     * </p>
     *
     * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
     */
     public static class PictureElement {
            private static final Map<String, PictureElement> map = new HashMap<String, PictureElement>();

            public static final PictureElement IOS_ELEMENT_NAME = new PictureElement("name");
            public static final PictureElement IOS_ELEMENT_PRICE = new PictureElement("price");
            public static final PictureElement IOS_ELEMENT_RATING = new PictureElement("rating");
            public static final PictureElement IOS_ELEMENT_SIZE = new PictureElement("size");
            public static final PictureElement IOS_ELEMENT_CATEGORY = new PictureElement("category");
            private String code;


            public PictureElement(String c) {
                code = c.toLowerCase();

                map.put(code, this);
            }

            public String getCode() {
                return code;
            }

            @Override
            public int hashCode() {
                return code.hashCode();
            }

            @Override
            public String toString() {
                return ReflectUtil.fieldsToString(this);
            }

            @Override
            public boolean equals(Object obj) {
                if ((obj == null) || !(obj instanceof PictureElement)) {
                    return false;
                }

                return code.equalsIgnoreCase(((PictureElement) obj).getCode());
            }

            public static PictureElement getByCode(String c) {
                return map.get(c.toLowerCase());
            }

            public static Collection<PictureElement> getAll() {
                return map.values();
            }
        }

    /**
     * <p/>
     * Description:app设备画图
     * </p>
     *
     * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
     */
    public static class AppDevicePic implements Serializable {
        private String deviceName;
        private String deviceSrc;

        private static Map<String, AppDevicePic> map = new HashMap<String, AppDevicePic>();

        public static final AppDevicePic IPAD = new AppDevicePic("iPad", "/img/ios/ipad.gif");
        public static final AppDevicePic IPHONE = new AppDevicePic("iPhone", "/img/ios/iphone.jpg");

        private AppDevicePic(String deviceName, String deviceSrc) {
            this.deviceName = deviceName.toLowerCase();
            this.deviceSrc = deviceSrc;
            map.put(this.deviceName, this);
        }

        public String getDeviceName() {
            return deviceName;
        }

        public String getDeviceSrc() {
            return deviceSrc;
        }

        public static AppDevicePic getByCode(String deviceName){
            if(!map.containsKey(deviceName.toLowerCase())){
                return IPHONE;
            }

           return map.get(deviceName.toLowerCase());
        }

        @Override
        public int hashCode() {
            return deviceName.hashCode();
        }

        @Override
        public String toString() {
            return ReflectUtil.fieldsToString(this);
        }

        @Override
        public boolean equals(Object obj) {
            if ((obj == null) || !(obj instanceof AppDevicePic)) {
                return false;
            }

            return deviceName.equals(((AppDevicePic) obj).getDeviceName());
        }
    }

}
