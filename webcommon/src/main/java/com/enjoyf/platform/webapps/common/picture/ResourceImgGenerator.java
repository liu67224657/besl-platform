package com.enjoyf.platform.webapps.common.picture;

import com.enjoyf.platform.util.FileUtil;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ResourceImgGenerator {
    private static final String IMG_EXTNAME_JPG = ".jpg";

    private static final String GAME_SUFFIX_LARGE_LOGO = "_GLL";
    private static final String GAME_SUFFIX_SMALL_LOGO = "_GSL";

    private static final String APP_SUFFIX_LARGE_LOGO = "_I";
    private static final String APP_SUFFIX_SMAL_LOGO = "_SI";

    private static final int IMG_ONE_SIZE = 60;

    private static Logger logger = LoggerFactory.getLogger(ResourceImgGenerator.class);


    public static Map<String, String> genResourceImage(InputStream inputStream, String rootpath, String imgScale, String outputDir,
                                                       String outputFileName) throws IOException, InterruptedException, IM4JavaException {
        Map<String, String> map = new HashMap<String, String>();
        String llImage = uploadImage(inputStream, rootpath, GAME_SUFFIX_LARGE_LOGO, outputDir, outputFileName);

        String[] scaleArray = imgScale.split(":");
        if (scaleArray.length != 2) {
            return null;
        }
        int scaleWidth = Integer.parseInt(scaleArray[0]) * IMG_ONE_SIZE;
        int scaleHeight = Integer.parseInt(scaleArray[1]) * IMG_ONE_SIZE;
        llImage = cutResourceImage(llImage, GAME_SUFFIX_LARGE_LOGO, rootpath,scaleWidth,scaleHeight);

        String slImage = cutResourceImage(llImage, GAME_SUFFIX_SMALL_LOGO, rootpath,IMG_ONE_SIZE,IMG_ONE_SIZE);
        map.put("ll", llImage);
        map.put("sl", slImage);
        return map;
    }

    public static Map<String, String> genAppImage(InputStream inputStream, String rootpath, String outputDir,
                                                  String outputFileName) throws IOException, InterruptedException, IM4JavaException {
        Map<String, String> map = new HashMap<String, String>();
        String llImage = uploadImage(inputStream, rootpath, APP_SUFFIX_LARGE_LOGO, outputDir, outputFileName);

        llImage = cutResourceImage(llImage, APP_SUFFIX_LARGE_LOGO, rootpath,175,175);

        String slImage = cutResourceImage(llImage, APP_SUFFIX_SMAL_LOGO, rootpath,IMG_ONE_SIZE,IMG_ONE_SIZE);
        map.put("ll", llImage);
        map.put("sl", slImage);
        return map;
    }

    //上传图片
    private static String uploadImage(InputStream inputStream, String rootpath, String imageSizeSuffix, String outputDir,
                                      String outputFileName) throws IOException, InterruptedException, IM4JavaException {
        String returnSrc = outputDir + outputFileName + imageSizeSuffix + IMG_EXTNAME_JPG;
        String dirPath = rootpath + outputDir;
        if (!FileUtil.isFileOrDirExist(dirPath)) {
            try {
                boolean exists = FileUtil.createDirectory(dirPath);
                logger.debug(" app logo dir：" + exists);
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
            }
        }

        String allFileName = dirPath + outputFileName + imageSizeSuffix + IMG_EXTNAME_JPG;
        Image img = ImageIO.read(inputStream);
        ConvertCmd cmd = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        op.quality(100.00);// 压缩率
        op.addImage();// input
        op.addImage(allFileName);
        cmd.run(op, img);

        return returnSrc;
    }

    private static String resizeResourceImage(String srcPath, String imgScale, String imageSizeSuffix, String rootPath) throws IOException, InterruptedException, IM4JavaException {

        ConvertCmd resizeCmd = new ConvertCmd(true);
        IMOperation resizeOp = new IMOperation();
        resizeOp.quality(100.00);// 压缩率
        resizeOp.addImage();// input

        String[] scaleArray = imgScale.split(":");
        if (scaleArray.length != 2) {
            return null;
        }
        BufferedInputStream biStream = new BufferedInputStream(new FileInputStream(rootPath + srcPath));
        Image img = ImageIO.read(biStream);

        int scaleWidth = Integer.parseInt(scaleArray[0]) * IMG_ONE_SIZE;
        int scaleHeight = Integer.parseInt(scaleArray[1]) * IMG_ONE_SIZE;

        int resizeWidth = img.getWidth(null);
        int resizeHeight = img.getHeight(null);

        if (img.getWidth(null) > scaleWidth) {
            resizeWidth = scaleWidth;
        }

        if (img.getHeight(null) > scaleHeight) {
            resizeHeight = scaleHeight;
        }

        if (img.getWidth(null) > img.getHeight(null)) {
            resizeOp.resize(resizeWidth);
        } else {
            resizeOp.resize(resizeHeight);
        }

        resizeOp.resize(resizeWidth, resizeHeight);
        resizeOp.addImage();// output

        String outPutPath = outPutPath = UploadUtil.genImageName(srcPath, imageSizeSuffix);
        resizeCmd.run(resizeOp, rootPath + srcPath, rootPath + outPutPath);

        return outPutPath;
    }

    private static String cutResourceImage(String srcPath, String imageSizeSuffix, String rootPath, int cutWidth, int cutHeight) throws IOException, InterruptedException, IM4JavaException {

        ConvertCmd cutCmd = new ConvertCmd(true);

        IMOperation cutOp = new IMOperation();
        cutOp.quality(100.00);// 压缩率
        cutOp.addImage();// input


        String outPutPath;
        BufferedInputStream biStream = null;
        try {
            biStream = new BufferedInputStream(new FileInputStream(rootPath + srcPath));
            Image img = ImageIO.read(biStream);


            int xAxis = 0;
            int yAxis = 0;
            if ((img.getWidth(null) - cutWidth) > 0 && (img.getHeight(null) - cutHeight) > 0) {
                xAxis = (img.getWidth(null) - cutWidth) / 2;
                yAxis = (img.getHeight(null) - cutHeight) / 2;
            }
            cutOp.crop(cutWidth, cutHeight, xAxis, yAxis);
            cutOp.addImage();// output
            outPutPath = UploadUtil.genImageName(srcPath, imageSizeSuffix);
            cutCmd.run(cutOp, rootPath + srcPath, rootPath + outPutPath);
        } finally {
            if (biStream != null) {
                biStream.close();
            }
        }

        return outPutPath;
    }


}
