/**
 *
 */
package com.enjoyf.platform.webapps.common.picture;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.content.ResourceFileType;
import com.enjoyf.platform.util.FileUtil;
import com.enjoyf.platform.util.ResourceFilePathUtil;
import com.enjoyf.platform.util.StreamUtil;
import org.apache.commons.io.FileUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片工具类
 *
 * @author xinzhao
 */
public class HandleFacePic {

    private static Logger logger = LoggerFactory.getLogger(HandleFacePic.class);

    // private static String BIG_IMAGE = "_B"; //大
    private static String MEDIUM_IMAGE = "_M";// 中
    private static String SMALL_IMAGE = "_S";// 小
    private static String S_SMALL_IMAGE = "_SS";// 超小

    private static int big_size_width = 387;// 2011-11-04 应PD要求将 350-387;
    private static int big_size_height = 210;//  2011-11-04 应PD要求将 190-210;

    private static int medium_size_width = 200;  //2011-10-31 应PD要求将115-200
    private static int medium_size_height = 200; //2011-10-31 应PD要求将115-200

    private static int small_size_width = 58;
    private static int small_size_height = 58;

    private static int s_small_size_width = 20;
    private static int s_small_size_height = 20;

    // 原始图片的宽，高
    private static int pic_width = 0;
    private static int pic_height = 0;

    /**
     * 头像大图调用主方法
     *
     * @param inputStream    *            文件输入流
     * @param inputFileName  *            源图
     * @param rootpath       *            容器根路径
     * @param outputDir      *            输出路径
     * @param outputFileName *            输出文件名
     * @return map 返回多个图片的地址
     */
    public static Map hadnleFP(InputStream inputStream, String inputFileName, String rootpath, String outputDir,
                               String outputFileName) {

        Map<String, String> map = new HashMap<String, String>();//返回图片路径的map
        Image img = null;
        String sPath = null;// 图片路径
        try {

            img = grenImage(inputStream);
            if (img == null) {
                return null;
            }

            /*------------------   最大的图  begin ------------------*/

            // 宽高≤350*190
            if ((pic_width <= big_size_width) && (pic_height <= big_size_height)) {
                sPath = resizeFix(img, big_size_width, big_size_height, rootpath, outputDir, outputFileName);
                logger.debug(sPath + "###  宽高≤350*190");

                //宽>350 高<190 宽=350，高自适应
            } else if ((pic_width > big_size_width) && (pic_height < big_size_height)) {
                sPath = resizeFix(img, big_size_width, pic_height, rootpath, outputDir, outputFileName);
                logger.debug(sPath + "### 宽>350 高<190 宽=350，高自适应");

                //宽<350 高>190  高=190，宽自适应
            } else if ((pic_width < big_size_width) && (pic_height > big_size_height)) {
                sPath = resizeFix(img, pic_width, big_size_height, rootpath, outputDir, outputFileName);
                logger.debug(sPath + "### 宽<350 高>190  高=190，宽自适应");

                //宽高≥350*190
            } else {
                //宽>1.84高
                if (pic_width > pic_height * 1.842) {
                    sPath = resizeFix(img, big_size_width, pic_height, rootpath, outputDir, outputFileName);
                    logger.debug(sPath + "### 宽>1.84高      宽=350，高自适应");
                    System.out.println(sPath + "### 宽>1.84高      宽=350，高自适应");
                } else {
                    sPath = resizeFix(img, pic_width, big_size_height, rootpath, outputDir, outputFileName);
                    logger.debug(sPath + "### 宽<1.84高      高=190，宽自适应");
                    System.out.println(sPath + "### 宽<1.84高      高=190，宽自适应");
                }
            }
            map.put("b", sPath);//封装大图返回地址

            /*------------------   最大的图  end ------------------*/

            sPath = resizeFix(img, medium_size_width, medium_size_height, rootpath, outputDir, outputFileName + MEDIUM_IMAGE);
            map.put("m", sPath);//封装中图返回地址

            sPath = resizeFix(img, small_size_width, small_size_height, rootpath, outputDir, outputFileName + SMALL_IMAGE);
            map.put("s", sPath);//封装小图返回地址

            sPath = resizeFix(img, s_small_size_width, s_small_size_height, rootpath, outputDir, outputFileName + S_SMALL_IMAGE);
            map.put("ss", sPath);//封装超小图返回地址


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IM4JavaException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return map;
    }


    /**
     * 头像剪切主方法
     */
    public static Map cutImageMain(int x, int y, int w, int h, String srcImg, String rootPath) {
        Map map = new HashMap();
        String sPath = null;
        Image img = null;
        String fileqian = null;
        FileInputStream is = null;
        if (srcImg.indexOf("?") != -1)
            srcImg = srcImg.substring(0, srcImg.indexOf("?"));
        try {
            if (((w - x) > medium_size_width) && ((h - y) > medium_size_height)) {
                sPath = cutImage(x, y, w, h, srcImg, rootPath, MEDIUM_IMAGE);

                // 读取图片文件
                is = new FileInputStream(rootPath + sPath);
                img = grenImage(is);

//                fileqian = sPath.substring(0, sPath.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1);
                String filepa = srcImg.substring(0, srcImg.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1);//文件路径  /r001/image/xxxx/xx/
                sPath = resizeFix(img, medium_size_width, medium_size_height, rootPath, filepa, grenImgName(sPath, MEDIUM_IMAGE));

            } else {
                sPath = cutImage(x, y, w, h, srcImg, rootPath, MEDIUM_IMAGE);
            }
            map.put("m", sPath);

            // ---- small begin -------
            is = new FileInputStream(rootPath + sPath);
            img = grenImage(is);
            String filepa = srcImg.substring(0, srcImg.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1);
            sPath = resizeFix(img, small_size_width, small_size_height, rootPath, filepa, grenImgName(sPath, SMALL_IMAGE));
            map.put("s", sPath);
            // ---- small end -------

            // ---- super small begin -------
            is = new FileInputStream(rootPath + sPath);
            img = grenImage(is);
            filepa = srcImg.substring(0, srcImg.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1);
            sPath = resizeFix(img, s_small_size_width, s_small_size_height, rootPath, filepa, grenImgName(sPath, S_SMALL_IMAGE));
            map.put("ss", sPath);
            // ---- small end -------


            // 重新生成图片名称   四个图片
            map = moveFiles(rootPath, sPath);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        } finally {
            StreamUtil.closeInputStream(is);
        }

        return map;
    }

    public static String cutImage(int x, int y, int w, int h, String srcImg, String rootPath) {
//        Map map = new HashMap();
        String sPath = null;
        Image img = null;
        String fileqian = null;
        FileInputStream is = null;
        if (srcImg.indexOf("?") != -1)
            srcImg = srcImg.substring(0, srcImg.indexOf("?"));
        try {
            if (((w - x) > medium_size_width) && ((h - y) > medium_size_height)) {
                sPath = cutImage(x, y, w, h, srcImg, rootPath, "");

                // 读取图片文件
                is = new FileInputStream(rootPath + sPath);
                img = grenImage(is);

//                fileqian = sPath.substring(0, sPath.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1);
                String filepa = srcImg.substring(WebappConfig.get().getQiniuHost().length(), srcImg.length());//文件路径  /r001/image/xxxx/xx/
                filepa = filepa.substring(0, filepa.lastIndexOf("/") + 1);
                sPath = resizeFix(img, medium_size_width, medium_size_height, rootPath, filepa, grenImgName(sPath, MEDIUM_IMAGE));

            } else {
                sPath = cutImage(x, y, w, h, srcImg, rootPath, "");
            }


            // 重新生成图片名称   四个图片
            return moveFile(rootPath, sPath, "");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        } finally {
            StreamUtil.closeInputStream(is);
        }

        return "";
    }

    private static Map moveFiles(String rootPath, String sPath) throws IOException {
        String newName = ResourceFilePathUtil.getFileName(sPath);
        Map map = new HashMap();
        String filepath = sPath.substring(0, sPath.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1); //文件路径
        String fileqian = sPath.substring(sPath.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1, sPath.indexOf("."));
        if (fileqian.lastIndexOf("_") != -1)
            fileqian = fileqian.substring(0, fileqian.lastIndexOf("_"));

        FileUtils.copyFile(new File(rootPath + filepath + fileqian + ".jpg"), new File(rootPath + filepath + newName + ".jpg"));
        FileUtils.copyFile(new File(rootPath + filepath + fileqian + MEDIUM_IMAGE + ".jpg"), new File(rootPath + filepath + newName + MEDIUM_IMAGE + ".jpg"));
        FileUtils.copyFile(new File(rootPath + filepath + fileqian + SMALL_IMAGE + ".jpg"), new File(rootPath + filepath + newName + SMALL_IMAGE + ".jpg"));
        FileUtils.copyFile(new File(rootPath + filepath + fileqian + S_SMALL_IMAGE + ".jpg"), new File(rootPath + filepath + newName + S_SMALL_IMAGE + ".jpg"));


        map.put("b", filepath + newName + ".jpg");
        map.put("m", filepath + newName + MEDIUM_IMAGE + ".jpg");
        map.put("s", filepath + newName + SMALL_IMAGE + ".jpg");
        map.put("ss", filepath + newName + S_SMALL_IMAGE + ".jpg");


//        FileUtils.deleteDirectory(new File(rootPath +filepath + fileqian+".jpg"));
//        FileUtils.deleteDirectory(new File(rootPath +filepath + fileqian+MEDIUM_IMAGE+".jpg"));
//        FileUtils.deleteDirectory(new File(rootPath +filepath + fileqian+SMALL_IMAGE+".jpg"));
//        FileUtils.deleteDirectory(new File(rootPath +filepath + fileqian+S_SMALL_IMAGE+".jpg"));

        logger.debug("moveFile:    " + map);
        return map;
    }

    private static String moveFile(String rootPath, String sPath, String exName) throws IOException {
        String newName = ResourceFilePathUtil.getFileName(sPath);
        Map map = new HashMap();
        String filepath = sPath.substring(0, sPath.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1); //文件路径
        String fileqian = sPath.substring(sPath.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1, sPath.indexOf("."));
        if (fileqian.lastIndexOf("_") != -1) {
            fileqian = fileqian.substring(0, fileqian.lastIndexOf("_"));
        }

        FileUtils.copyFile(new File(rootPath + filepath + fileqian + exName + ".jpg"), new File(rootPath + filepath + newName + exName + ".jpg"));
        return filepath + newName + exName + ".jpg";
    }


    private static String grenImgName(String srcImg, String exname) {
        String fileqian = srcImg.substring(srcImg.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1, srcImg.indexOf("."));
        if (fileqian.lastIndexOf("_") != -1)
            fileqian = fileqian.substring(0, fileqian.lastIndexOf("_"));
        //String filehou = srcImg.substring(srcImg.indexOf(".")); // .jpg
        //String filepa = srcImg.substring(0, srcImg.lastIndexOf(HashIDUtil.PATH_SEPARATOR) + 1);//文件路径  /r001/image/xxxx/xx/
        return fileqian + exname;
    }


    // 生成Image 对象，并取得大小
    private static Image grenImage(InputStream inputStream) throws IOException {
        //BufferedImage srcBufferImage = javax.imageio.ImageIO.read(inputStream);
        Image img = ImageIO.read(inputStream);
        pic_width = img.getWidth(null);
        pic_height = img.getHeight(null);
        return img;
    }


    public static String resize(Image img, int width, int height, String rootpath, String outputDir, String outputFileName) throws IOException, IM4JavaException, InterruptedException {
        String sReString = null;
        if (!FileUtil.isFileOrDirExist(rootpath + outputDir)) {

            try {
                boolean bIsCreate = FileUtil.createDirectory(rootpath + outputDir);
                logger.debug("创建文件夹是否成功：" + bIsCreate);
            } catch (FileNotFoundException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
// create command
        ConvertCmd cmd = new ConvertCmd(true);

// create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.quality(95.00);// 压缩率
        op.addImage();                        // input
        op.resize(width, height);
        op.addImage(rootpath + outputDir + outputFileName + ".jpg");
// execute the operation
        cmd.run(op, img);
        sReString = outputDir + outputFileName + ".jpg";
        System.out.println("sReString:" + sReString);
        return sReString;
    }

    public static String resizeByWidth(int w, Image img, String rootpath, String outputDir, String outputFileName) throws IOException, InterruptedException, IM4JavaException {
        float h = ((float) pic_height * w / pic_width);
        return resize(img, w, new Float(h).intValue(), rootpath, outputDir, outputFileName);
    }


    public static String resizeByHeight(int h, Image img, String rootpath, String outputDir, String outputFileName) throws IOException, InterruptedException, IM4JavaException {
        float w = ((float) pic_width * h / pic_height);
        return resize(img, new Float(w).intValue(), h, rootpath, outputDir, outputFileName);
    }

    public static String resizeFix(Image img, int w, int h, String rootpath, String outputDir, String outputFileName) throws IOException, InterruptedException, IM4JavaException {
        if (pic_width <= w && pic_width <= h) {
            return resize(img, pic_width, pic_height, rootpath, outputDir, outputFileName);
        } else if (pic_width > w && pic_width > h) {
            return resize(img, w, h, rootpath, outputDir, outputFileName);
        } else if ((float) pic_width / pic_height > (float) w / h) {
            return resizeByWidth(w, img, rootpath, outputDir, outputFileName);
        } else {
            return resizeByHeight(h, img, rootpath, outputDir, outputFileName);
        }
    }


    /**
     * 剪切图片
     *
     * @param w        宽
     * @param h        高
     * @param x        x
     * @param y        y
     * @param srcImg   源图位置
     * @param rootPath 容器根路径
     * @param exname   _M,_S,_SS
     * @throws java.io.IOException
     */
    public static String cutImage(int x, int y, int w, int h, String srcImg, String rootPath, String exname) throws IOException, IM4JavaException, InterruptedException {
        String fileqian = srcImg.substring(srcImg.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1, srcImg.lastIndexOf("."));
        if (fileqian.lastIndexOf("_") != -1)
            fileqian = fileqian.substring(0, fileqian.lastIndexOf("_"));
        String filepa = srcImg.substring(WebappConfig.get().getQiniuHost().length(), srcImg.length());//文件路径  /r001/image/xxxx/xx/
        filepa = filepa.substring(0, filepa.lastIndexOf("/") + 1);
        if (!FileUtil.isFileOrDirExist(rootPath + filepa)) {

            try {
                boolean bIsCreate = FileUtil.createDirectory(rootPath + filepa);
                logger.debug("创建文件夹是否成功：" + bIsCreate);
            } catch (FileNotFoundException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        // create command
        ConvertCmd cmd = new ConvertCmd(true);

        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.quality(95.00);// 压缩率
        op.addImage(srcImg); // input
        op.append().crop(w, h, x, y);//切割图片
        op.addImage(rootPath + filepa + fileqian + exname + ".jpg");
        cmd.run(op);
        return filepa + fileqian + exname + ".jpg";
    }

    public static String cutViewLineItemImage(int x, int y, int w, int h, String srcImg, String rootPath) throws IOException, IM4JavaException, InterruptedException {
        //ResourceFilePathUtil
        String[] splitImgName = srcImg.split(ResourceFilePathUtil.PATH_SEPARATOR);
        splitImgName[2] = ResourceFileType.ORIGINAL.getCode();
        String fileName = "";
        for (String splitContent : splitImgName) {
            fileName += splitContent + ResourceFilePathUtil.PATH_SEPARATOR;
        }
        fileName = fileName.substring(0, fileName.length() - 1);

        String origSrcImg = srcImg;

        srcImg = fileName;

        String fileqian = srcImg.substring(srcImg.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1, srcImg.indexOf("."));
        if (fileqian.lastIndexOf("_") != -1)
            fileqian = fileqian.substring(0, fileqian.lastIndexOf("_"));

        String filepa = srcImg.substring(0, srcImg.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1);//文件路径  /r001/image/xxxx/xx/

        String newFileName = ResourceFilePathUtil.getFileName(fileqian);

        if (!FileUtil.isFileOrDirExist(rootPath + filepa)) {

            try {
                boolean bIsCreate = FileUtil.createDirectory(rootPath + filepa);
                logger.debug("创建文件夹是否成功：" + bIsCreate);
            } catch (FileNotFoundException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        // create command
        ConvertCmd cmd = new ConvertCmd(true);

        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.quality(95.00);// 压缩率
        op.addImage(rootPath + origSrcImg); // input
        op.append().crop(w, h, x, y);//切割图片
        op.addImage(rootPath + filepa + newFileName + ".jpg");
        cmd.run(op);
        return filepa + newFileName + ".jpg";
    }


    public static void main(String[] arg) throws IOException {
        System.out.println(System.getProperty("java.library.path"));
        //FileInputStream fis = new FileInputStream(new File("d:/1.jpg"));
        //System.out.println(hadnleFP(fis,"1.jpg","d:/","/","2"));

        System.out.println(HandleFacePic.cutImageMain(100, 100, 200, 200, "1.jpg", "d:/"));
//        try {
//            cutImg();
//        } catch (IM4JavaException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        } catch (InterruptedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


    }


}
