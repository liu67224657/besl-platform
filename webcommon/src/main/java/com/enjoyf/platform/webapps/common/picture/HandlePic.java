/**
 *
 */
package com.enjoyf.platform.webapps.common.picture;

import com.enjoyf.platform.props.ImageConfig;
import com.enjoyf.platform.util.FileUtil;
import com.gif4j.GifDecoder;
import com.gif4j.GifEncoder;
import com.gif4j.GifImage;
import com.gif4j.GifTransformer;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片工具类
 *
 * @author xinzhao
 */
public class HandlePic {

    private static Logger logger = LoggerFactory.getLogger(HandlePic.class);
    ImageConfig imageConfig;

    private static String MEDIUM_IMAGE = ImageConfig.get().getmImageSuffix();
    private static String SMALL_IMAGE = ImageConfig.get().getsImageSuffix();
    private static String S_SMALL_IMAGE = ImageConfig.get().getSsImageSuffix();

    private static Integer BIG_IMAGE_SIZE = ImageConfig.get().getBigImageSize();// "2M";
    private static Integer BIG_MAX_IMAGE_SIZE = ImageConfig.get().getBigMaxImageSize();// "8M";

    private static int medium_size_width = ImageConfig.get().getmImageWidth(); // 558->620 ,2011.9.3 根据新图片规则范
    private static int medium_size_height = ImageConfig.get().getmImageHeight(); // 558->620 ,2011.9.3 根据新图片规则范

    // 小图
    private static int small_size_width = ImageConfig.get().getsImageWidth();
    private static int small_size_height = ImageConfig.get().getsImageHeight();

    // 小图，用于发现墙       图片按比例压缩至宽208px*高156px以内
    private static int s_small_size_width = ImageConfig.get().getSsImageWidth();   //2011.9.3 to 150-->208
    private static int s_small_size_height = ImageConfig.get().getSsImageHeight();  //2011.9.3 to 150-->156

    private static double beishu = ImageConfig.get().getResizeBeishu();// 倍数
    private static int btkg = ImageConfig.get().getResizeBTKG();// 变态宽高

    private static double fx_wall_rete = ImageConfig.get().getResizeWallRate(); //倍数


    /**
     * 调用主方法
     *
     * @param inputStream    * 文件输入流
     * @param inputFileName  * 源图
     * @param rootpath       * 容器根路径
     * @param outputDir      * 输出路径
     * @param outputFileName * 输出文件名
     * @return map 返回多个图片的地址
     */
    public static Map<String, String> handlePhoto(InputStream inputStream, String inputFileName, String rootpath, String outputDir,
                                                  String outputFileName) throws IOException, InterruptedException, IM4JavaException {

        Map<String, String> map = new HashMap<String, String>();// 返回图片路径的map
        String sPath = null;// 图片路径
        String sBImgPath = null;

        // 文件扩展名
        String exname = inputFileName.substring(inputFileName.lastIndexOf(".") + 1);

        // 首先存储原图
        sBImgPath = createImage(inputStream, inputFileName, rootpath, outputDir, outputFileName);
        if (exname.equalsIgnoreCase("gif")) {
            sBImgPath = compGifImage(sBImgPath, rootpath);
        } else {
            int i = 10;
            while (!isBigSize(rootpath + sBImgPath) && i < 80) {
                sBImgPath = compImage(sBImgPath, rootpath, i);
                i = i + 10;
            }
        }
        map.put("b", sBImgPath);

        //中图
        if (!exname.equalsIgnoreCase("gif")) {
            map = compMImage(sBImgPath, rootpath, MEDIUM_IMAGE, map);
        } else {
            map = compGifMImage(sBImgPath, rootpath, MEDIUM_IMAGE, map);
        }


        //ss小图
        if (exname.equalsIgnoreCase("gif")) {
            sPath = compGifSSImage(sBImgPath, rootpath, S_SMALL_IMAGE);
        } else {
            sPath = compSSImage(sBImgPath, rootpath, S_SMALL_IMAGE);
        }
        map.put("ss", sPath);// 封装中图返回地址

        //超小图
        if (exname.equalsIgnoreCase("gif")) {
            sBImgPath = compGifSImage(sBImgPath, rootpath, SMALL_IMAGE);
        } else {
            sBImgPath = compSImage(sBImgPath, rootpath, SMALL_IMAGE);
        }
        map.put("s", sBImgPath);// 封装中图返回地址

        return map;
    }

    // 判断文件大小是否大于最大长度
    // 当文件长度小于最大长度时返回真值
    private static boolean isBigSize(String path) {
        boolean bRe = true;
        File file = new File(path);
        System.out.println(file.length() + "()" + BIG_IMAGE_SIZE);

        if (file.length() < BIG_MAX_IMAGE_SIZE && file.length() > BIG_IMAGE_SIZE) {
            bRe = false;
        }

        return bRe;
    }

    // 生成Image 对象，并取得大小
    private static Image grenImage(String path) throws IOException {
        logger.debug("path:" + path);
        Image img = ImageIO.read(new FileInputStream(new File(path)));
        // pic_width = img.getWidth(null);
        // pic_height = img.getHeight(null);
        return img;
    }

    public static String createImage(InputStream inputStream, String inputFileName, String rootpath, String outputDir,
                                     String outputFileName) throws IOException, IM4JavaException, InterruptedException {
        String sReString = null;
        // 取文件扩展名
        String exname = inputFileName.substring(inputFileName.lastIndexOf(".") + 1);
        sReString = outputDir + outputFileName + ".jpg";

        if (!FileUtil.isFileOrDirExist(rootpath + outputDir)) {
            try {
                boolean bIsCreate = FileUtil.createDirectory(rootpath + outputDir);
                logger.debug("创建文件夹是否成功：" + bIsCreate);
            } catch (FileNotFoundException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        if (exname.equalsIgnoreCase("gif")) {
            sReString = createGifImageresize(inputStream, inputFileName, rootpath, outputDir, outputFileName);

        } else {
            Image img = ImageIO.read(inputStream);
            ConvertCmd cmd = new ConvertCmd(true);
            IMOperation op = new IMOperation();
            op.addImage();// input
            op.addImage(rootpath + sReString);// output
            cmd.run(op, img);
        }

        System.out.println("createImage sReString:" + sReString);
        return sReString;
    }

    // 压缩中图
    public static Map<String, String> compMImage(String srcPath, String rootpath, String exname, Map<String, String> map) throws IOException,
            InterruptedException, IM4JavaException {

        Image img = grenImage(rootpath + (srcPath == null ? "" : srcPath));

        String outPath = UploadUtil.genImageName(srcPath, exname);
        String sReString = outPath;
        ConvertCmd cmd = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        op.quality(99.00);// 压缩率
        op.addImage();// input

        map.put("w", String.valueOf(img.getWidth(null)));
        map.put("h", String.valueOf(img.getHeight(null)));
        if (img.getWidth(null) > medium_size_width) {
            if (img.getWidth(null) < img.getHeight(null)) {
                op.resize(medium_size_width, medium_size_height, '^');// ^
            } else {
                op.resize(medium_size_width);
            }
            map.put("w", String.valueOf(medium_size_width));
            int h = img.getHeight(null);
            int height = Double.valueOf(h * medium_size_width / img.getWidth(null)).intValue();
            map.put("h", String.valueOf(height));
        }

        op.addImage();// output
        cmd.run(op, rootpath + srcPath, rootpath + outPath);

        if (logger.isDebugEnabled()) {
            logger.debug("compImage m sReString:" + sReString);
        }
        map.put("m", sReString);// 封装中图返回地址
        return map;
//        Image img = grenImage(rootpath + (srcPath == null ? "" : srcPath));
//
//        String outPath = UploadUtil.genImageName(srcPath, exname);
//        String sReString = outPath;
//        ConvertCmd cmd = new ConvertCmd(true);
//        IMOperation op = new IMOperation();
////        op.quality(90.00);// 压缩率
//        op.addImage(rootpath + srcPath);// input
//
//        map.put("w", String.valueOf(img.getWidth(null)));
//        map.put("h", String.valueOf(img.getHeight(null)));
//
//        int resizeWidth = img.getWidth(null);
//        if (img.getWidth(null) > medium_size_width) {
//            resizeWidth = medium_size_width;
//            map.put("w", String.valueOf(medium_size_width));
//
//            int h = img.getHeight(null);
//            int height = Double.valueOf(h * medium_size_width / img.getWidth(null)).intValue();
//            map.put("h", String.valueOf(height));
//
//            op.resize(resizeWidth,null);
//        }
//
//        op.addImage(rootpath + outPath);// output
//        cmd.run(op);
//
//        System.out.println("compImage m sReString:" + sReString);
//        map.put("m", sReString);// 封装中图返回地址
//        return map;
    }

    // 压缩中图
    public static Map<String, String> compGifMImage(String srcPath, String rootpath, String exname, Map<String, String> map) throws IOException,
            InterruptedException, IM4JavaException {

        String outPath = UploadUtil.genImageName(srcPath, exname);
        String sReString = outPath;

        GifImage gifImage = GifDecoder.decode(new File(rootpath + srcPath));// 创建一个GifImage对象
        FileOutputStream out = new FileOutputStream(rootpath + sReString);
        int width = gifImage.getScreenWidth();
        int height = gifImage.getScreenHeight();
        map.put("w", String.valueOf(width));
        map.put("h", String.valueOf(height));
        if (width > medium_size_width) {
            GifImage resizedGifImage2 = GifTransformer.resize(gifImage, medium_size_width, 0, true);// 1.缩放重新更改大小.
            GifEncoder.encode(resizedGifImage2, out, true);
            map.put("w", String.valueOf(medium_size_width));
            int h = Double.valueOf(height * medium_size_width / width).intValue();
            map.put("h", String.valueOf(h));
        } else {
            // GifImage resizedGifImage2 = GifTransformer.resize(gifImage, width, height, true);// 1.缩放重新更改大小.
            GifEncoder.encode(gifImage, out, true);
        }

        out.close();
        System.out.println("compImage gif m sReString:" + sReString);
        map.put("m", sReString);
        return map;
    }

    // 压缩中图 150
    public static String compSSImage(String srcPath, String rootpath, String exname) throws IOException,
            InterruptedException, IM4JavaException {

        Image img = grenImage(rootpath + (srcPath == null ? "" : srcPath));

        String outPath = UploadUtil.genImageName(srcPath, exname);
        String sReString = outPath;
        ConvertCmd cmd = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        op.quality(97.00);// 压缩率
        op.addImage();// input

        //图片的宽度大于等于4：3比例
        if (img.getWidth(null) >= (img.getHeight(null) * fx_wall_rete)) {
            if (img.getWidth(null) < s_small_size_width) { //< 188
                //保持原图，不做处理。
            } else {
                //图片的宽度大于取值范围
                //高度依然在取值范围内，截取宽中间的部分
                //高度也超过取值范围，将高度resize到高度的取值，截取高度和宽度范围的图片
                if (img.getHeight(null) < s_small_size_height) {   //h<156   ,取宽中间的部分
                    op.crop(s_small_size_width, img.getHeight(null), (img.getWidth(null) / 2) - s_small_size_width / 2, 0);
                } else {//h>=156
                    op.resize(null, s_small_size_height).gravity("center").crop(s_small_size_width, s_small_size_height, 0, 0).gravity("center");
                }
            }
        } else {
            //宽度小于4:3比例

            //宽度小于取值范围
            if (img.getWidth(null) < s_small_size_width) { //< 208
                //高度小于取值范围保持原图，不做处理。
                //高度大于取值范围宽度保留，高度截取中间156px的区域
                if (img.getHeight(null) < s_small_size_height) {

                } else {
                    op.crop(img.getWidth(null), s_small_size_height, 0, (img.getHeight(null) / 2) - s_small_size_height / 2);
                }
            } else {
                //宽度大于取值范围 resize到宽度的取值范围，裁剪
                if (img.getHeight(null) >= s_small_size_height) {
                    op.resize(s_small_size_width, null).gravity("center").crop(s_small_size_width, s_small_size_height, 0, 0).gravity("center");
                }
            }

        }

        op.addImage();// output
        cmd.run(op, rootpath + srcPath, rootpath + outPath);

        System.out.println("compImage ss  sReString:" + sReString);
        return sReString;
    }

    // 压缩中图 150
    public static String compGifSSImage(String srcPath, String rootpath, String exname) throws IOException {

        String outPath = UploadUtil.genImageName(srcPath, exname);
        String sReString = outPath;

        GifImage gifImage = GifDecoder.decode(new File(rootpath + srcPath));// 创建一个GifImage对象
        FileOutputStream out = new FileOutputStream(rootpath + sReString);
        int width = gifImage.getScreenWidth();
        int height = gifImage.getScreenHeight();


        if (width >= (height * fx_wall_rete)) {
            if (width < s_small_size_width) {
                //保持原图
                GifEncoder.encode(gifImage, out);
            } else {
                if (height < s_small_size_height) {   //h<156   ,取宽中间的部分
                    Rectangle rect = new Rectangle((width / 2) - s_small_size_width / 2, 0, s_small_size_width, height);
                    GifImage cropIMG = GifTransformer.crop(gifImage, rect);
                    //GifImage resizegif = GifTransformer.resize(cropIMG, s_small_size_width, s_small_size_height, true);// 1.缩放重新更改大小.
                    GifEncoder.encode(cropIMG, out);
                } else {//h>=156
                    GifImage resizegif = GifTransformer.resize(gifImage, 0, s_small_size_height, true);// 1.缩放重新更改大小.  xxxxxxxxxxxx
                    Rectangle rect = new Rectangle((resizegif.getScreenWidth() / 2) - s_small_size_width / 2, 0, s_small_size_width, resizegif.getScreenHeight());
                    GifImage cropIMG = GifTransformer.crop(resizegif, rect);
                    GifEncoder.encode(cropIMG, out);
                }
            }

        } else {
            if (width < s_small_size_width) { //< 208
                if (height < s_small_size_height) {
                    //保持原图，不做处理。
                    GifEncoder.encode(gifImage, out);
                } else {   //宽度保留，高度截取中间156px的区域
                    Rectangle rect = new Rectangle(0, (height / 2) - s_small_size_height / 2, width, height);
                    GifImage cropIMG = GifTransformer.crop(gifImage, rect);
                    GifEncoder.encode(cropIMG, out);
                }

            } else {
                if (height >= s_small_size_height) {
                    GifImage resizegif = GifTransformer.resize(gifImage, s_small_size_width, 0, true);// 1.缩放重新更改大小.   xxxxxxxxxxxxxxx
                    Rectangle rect = new Rectangle(0, (resizegif.getScreenHeight() / 2 - resizegif.getScreenWidth() / 2), s_small_size_width, s_small_size_height);
                    GifImage cropIMG = GifTransformer.crop(resizegif, rect);

                    GifEncoder.encode(cropIMG, out);
                }
            }
        }

        System.out.println("compImage ss gif sReString:" + sReString);
        return sReString;
    }

    // 压缩小图110
    public static String compSImage(String srcPath, String rootpath, String exname) throws IOException,
            InterruptedException, IM4JavaException {

        Image img = grenImage(rootpath + (srcPath == null ? "" : srcPath));

        String outPath = UploadUtil.genImageName(srcPath, exname);
        String sReString = outPath;

        ConvertCmd cmd = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        op.quality(95.00);// 压缩率
        op.addImage();// input

        // a)如果高是宽的3.67倍（含）以上，并且宽度大于等于30px的图，宽度固定为30px，高度自适应，高度最高截取到110px
        if (img.getWidth(null) * beishu <= img.getHeight(null) && img.getWidth(null) >= btkg) {
            op.resize(btkg, img.getHeight(null));// ^
            op.crop(btkg, small_size_height, 0, 0);
            // b)如果高是宽的3.67倍（含）以上，并且宽度小于30px的图，宽度维持原大小，高度维持原大小，高度最高截取到110px
        } else if (img.getWidth(null) * beishu <= img.getHeight(null) && img.getWidth(null) < btkg) {
            // op.resize(img.getWidth(null), img.getHeight(null), '^');// ^
            op.crop(img.getWidth(null), small_size_height, 0, 0);
            // c)如果高是宽的1倍-3.67倍（不含），并且宽度和高度都大于110px，宽小于高，那么采用截图方式，居中截取110px的方形图
        } else if (((img.getWidth(null) * beishu > img.getHeight(null)) && (img.getHeight(null) > img.getWidth(null)))
                && ((img.getWidth(null) > small_size_width) && (img.getHeight(null) > small_size_height))
                && img.getWidth(null) < img.getHeight(null)) {
            op.crop(img.getWidth(null), img.getWidth(null), 0, ((img.getHeight(null) - img.getWidth(null)) / 2));
            op.resize(small_size_width, small_size_height, '^');// ^
            // d)如果高是宽的1倍-3.67倍（不含），并且宽度小于110px，高大于110px，那么宽度不变，高度最大为110px，以宽度110px的标准裁去高度超过110px的部分，需要居中裁剪
        } else if (((img.getWidth(null) * beishu > img.getHeight(null)) && (img.getHeight(null) > img.getWidth(null)))
                && ((img.getWidth(null) < small_size_width) && (img.getHeight(null) > small_size_height))
                && img.getWidth(null) < img.getHeight(null)) {
            op.crop(img.getWidth(null), small_size_height, 0, ((img.getHeight(null) - img.getWidth(null)) / 2));
            // e)如果宽和高都小于110px，那么不进行压缩
        } else if (img.getWidth(null) <= small_size_height && img.getHeight(null) <= small_size_height) {
            //2011-08-18 by zx 小图不处理
            // op.resize(small_size_height, small_size_height);

            // f)如果宽是高的3.67倍（含）以上，并且高度大于等于30px的图，高度固定为30px，宽度自适应，宽度最高截取到110px
        } else if ((img.getWidth(null) >= img.getHeight(null) * beishu) && img.getHeight(null) >= btkg) {
            op.resize(img.getWidth(null), btkg);// ^
            op.crop(small_size_width, btkg, 0, 0);

            // g)如果宽是高的3.67倍（含）以上，并且高度小于30px的图，高度维持原大小，宽度维持原大小，宽度最高截取到110px
        } else if (img.getWidth(null) > img.getHeight(null) * beishu && img.getHeight(null) < btkg) {
            // op.resize(btkg, img.getHeight(null), '^');// ^
            op.crop(small_size_height, img.getHeight(null), 0, 0);
            // h) 如果宽是高的1倍-3.67倍（不含），并且高度和宽度都大于110px，高小于宽，那么采用截图方式，居中截取110px的方形图
        } else if ((img.getWidth(null) > img.getHeight(null) && img.getWidth(null) < img.getHeight(null) * beishu)
                && (img.getWidth(null) > small_size_width && img.getHeight(null) > small_size_height)
                && img.getWidth(null) > img.getHeight(null)) {
            op.crop(img.getHeight(null), img.getHeight(null), ((img.getWidth(null) - img.getHeight(null)) / 2), 0);
            op.resize(small_size_width, small_size_height, '^');// ^
            // i)
            // 如果宽是高的1倍-3.67倍（不含），并且高度小于110px，宽大于110px，那么高度不变，宽度最大为110px，以高度110px的标准裁去宽度超过110px的部分，需要居中裁剪
        } else if ((img.getWidth(null) > img.getHeight(null) && img.getWidth(null) < img.getHeight(null) * beishu)
                && img.getHeight(null) < small_size_height && img.getWidth(null) > small_size_width) {
            op.crop(small_size_width, img.getHeight(null), ((img.getWidth(null) - small_size_width) / 2), 0);
        } else {
            op.resize(small_size_width, small_size_height, '^');// ^
        }

        op.addImage();// output
        cmd.run(op, rootpath + srcPath, rootpath + outPath);

        System.out.println("compImage s  sReString:" + sReString);
        return sReString;
    }

    // gif 压缩小图
    public static String compGifSImage(String srcPath, String rootpath, String exname) throws IOException,
            InterruptedException, IM4JavaException {

        String outPath = UploadUtil.genImageName(srcPath, exname);
        String sReString = outPath;

        GifImage gifImage = GifDecoder.decode(new File(rootpath + srcPath));// 创建一个GifImage对象
        FileOutputStream out = new FileOutputStream(rootpath + sReString);
        int width = gifImage.getScreenWidth();
        int height = gifImage.getScreenHeight();

        // a)如果高是宽的3.67倍（含）以上，并且宽度大于等于30px的图，宽度固定为30px，高度自适应，高度最高截取到110px
        if (width * beishu <= height && width >= btkg) {
            GifImage resizegif = GifTransformer.resize(gifImage, btkg, height, true);// 1.缩放重新更改大小.
            Rectangle rect = new Rectangle(0, 0, btkg, small_size_height);
            GifImage cropIMG = GifTransformer.crop(resizegif, rect);
            GifEncoder.encode(cropIMG, out);

            // b)如果高是宽的3.67倍（含）以上，并且宽度小于30px的图，宽度维持原大小，高度维持原大小，高度最高截取到110px
        } else if (width * beishu <= height && width < btkg) {
            Rectangle rect = new Rectangle(0, 0, width, small_size_height);
            GifImage cropIMG = GifTransformer.crop(gifImage, rect);
            GifEncoder.encode(cropIMG, out);

            // c)如果高是宽的1倍-3.67倍（不含），并且宽度和高度都大于110px，宽小于高，那么采用截图方式，居中截取110px的方形图
        } else if (((width * beishu > height) && (height > width)) && (width > small_size_width)
                && (height > small_size_height) && width < height) {
            Rectangle rect = new Rectangle(0, ((height - width) / 2), width, height);
            GifImage cropIMG = GifTransformer.crop(gifImage, rect);
            GifImage resizegif = GifTransformer.resize(cropIMG, small_size_width, small_size_height, true);// 1.缩放重新更改大小.
            GifEncoder.encode(resizegif, out);

            // d)如果高是宽的1倍-3.67倍（不含），并且宽度小于110px，高大于110px，那么宽度不变，高度最大为110px，以宽度110px的标准裁去高度超过110px的部分，需要居中裁剪
        } else if (((width * beishu > height) && (height > width))
                && ((width < small_size_width) && (height > small_size_height)) && width < height) {
            Rectangle rect = new Rectangle(0, ((height - width) / 2), width, small_size_height);
            GifImage cropIMG = GifTransformer.crop(gifImage, rect);
            GifEncoder.encode(cropIMG, out);

            // e)如果宽和高都小于110px，那么不进行压缩
        } else if (width <= small_size_height && height <= small_size_height) {
            //2011-08-18 by zx 小图不处理
            //GifImage resizegif = GifTransformer.resize(gifImage, small_size_width, small_size_height, true);// 1.缩放重新更改大小.
            GifEncoder.encode(gifImage, out);

            // f)如果宽是高的3.67倍（含）以上，并且高度大于等于30px的图，高度固定为30px，宽度自适应，宽度最高截取到110px
        } else if ((width >= height * beishu) && height >= btkg) {
            GifImage resizegif = GifTransformer.resize(gifImage, width, btkg, true);// 1.缩放重新更改大小.
            Rectangle rect = new Rectangle(0, 0, small_size_width, btkg);
            GifImage cropIMG = GifTransformer.crop(resizegif, rect);
            GifEncoder.encode(cropIMG, out);

            // g)如果宽是高的3.67倍（含）以上，并且高度小于30px的图，高度维持原大小，宽度维持原大小，宽度最高截取到110px
        } else if (width > height * beishu && height < btkg) {
            Rectangle rect = new Rectangle(0, 0, small_size_width, height);
            GifImage cropIMG = GifTransformer.crop(gifImage, rect);
            GifEncoder.encode(cropIMG, out);

            // h) 如果宽是高的1倍-3.67倍（不含），并且高度和宽度都大于110px，高小于宽，那么采用截图方式，居中截取110px的方形图
        } else if ((width > height && width < height * beishu)
                && (width > small_size_width && height > small_size_height) && width > height) {

            // 2011-9-20 由于某些gif总出错，所以调整了先压缩，后剪切
            GifImage resizegif = GifTransformer.resize(gifImage, 0, small_size_height, true);// 1.缩放重新更改大小.

            Rectangle rect = new Rectangle(((resizegif.getScreenWidth() - resizegif.getScreenHeight()) / 2), 0, small_size_width, height);
            GifImage cropIMG = GifTransformer.crop(resizegif, rect);
            GifEncoder.encode(cropIMG, out);

            // i)
            // 如果宽是高的1倍-3.67倍（不含），并且高度小于110px，宽大于110px，那么高度不变，宽度最大为110px，以高度110px的标准裁去宽度超过110px的部分，需要居中裁剪
        } else if ((width > height && width < height * beishu) && height < small_size_height
                && width > small_size_width) {
            Rectangle rect = new Rectangle(((width - small_size_width) / 2), 0, small_size_width, height);
            GifImage cropIMG = GifTransformer.crop(gifImage, rect);
            GifEncoder.encode(cropIMG, out);
        } else {
            GifImage resizegif = GifTransformer.resize(gifImage, small_size_width, small_size_height, true);// 1.缩放重新更改大小.
            GifEncoder.encode(resizegif, out);
        }

        System.out.println("compImage gif s  sReString:" + sReString);
        return sReString;
    }

    // 压缩大图尺寸
    public static String compImage(String srcPath, String rootpath, int i) throws IOException, InterruptedException,
            IM4JavaException {

        double dQualty = 100.00 - i;
        String sReString = null;
        sReString = srcPath;

        ConvertCmd cmd = new ConvertCmd(true);
        IMOperation op = new IMOperation();
        // op.transparentColor("white");
        // op.transparentColor("black");
        op.quality(dQualty);// 压缩率
        op.addImage();// input
        op.addImage();// output
        cmd.run(op, rootpath + srcPath, rootpath + srcPath);
        System.out.println("compImage sReString:" + sReString);
        return sReString;
    }

//    private static String grenImgName(String srcImg, String exname) {
//        String fileqian = srcImg.substring(srcImg.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1, srcImg.indexOf("."));
//        if (fileqian.lastIndexOf("_") != -1)
//            fileqian = fileqian.substring(0, fileqian.lastIndexOf("_"));
//        String filehou = srcImg.substring(srcImg.indexOf(".")); // .jpg
//        String filepa = srcImg.substring(0, srcImg.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1);// 文件路径
//        logger.debug("grenImgName:" + filepa + fileqian + exname + filehou);                                                            // /r001/image/xxxx/xx/
//        return filepa + fileqian + exname + filehou;
//    }

    /**
     * GIF文件缩略图处理函数 srcImg 源图 destImg 缩略图
     */
    private static String createGifImageresize(InputStream inputStream, String inputFileName, String rootpath,
                                               String outputDir, String outputFileName) {
        String reString = null;

        try {
            GifImage gifImage = GifDecoder.decode(inputStream);// 创建一个GifImage对象
            FileOutputStream out = new FileOutputStream(rootpath + outputDir + outputFileName + ".gif");
            // GifImage resizedGifImage2 = GifTransformer.resize(gifImage, 0, 0,
            // true);//1.缩放重新更改大小.
            GifEncoder.encode(gifImage, out, true);
            out.close();
            reString = outputDir + outputFileName + ".gif";
            System.out.println("reString:" + reString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reString;

    }

    // 压缩大图尺寸
    private static String compGifImage(String srcPath, String rootpath) throws IOException {

        String sReString = null;
        sReString = srcPath;

        GifImage gifImage = GifDecoder.decode(new File(rootpath + srcPath));// 创建一个GifImage对象
        FileOutputStream out = new FileOutputStream(rootpath + srcPath);
        // true);//1.缩放重新更改大小.
        GifEncoder.encode(gifImage, out, true);
        out.close();
        System.out.println("reString:" + srcPath);

        return sReString;
    }

    public static void main(String[] arg) throws FileNotFoundException {

        // hadnlePhoto(InputStream inputStream, String inputFileName, String
        // rootpath, String outputDir, String outputFileName)

        FileInputStream fis = new FileInputStream(new File("d:/3.png"));
        try {
            handlePhoto(fis, "444", "d:/", "/", "4");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IM4JavaException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
